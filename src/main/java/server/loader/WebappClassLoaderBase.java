/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.cert.Certificate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.Manifest;

/**
 * Specialized web application class loader.
 * <p>
 * This class loader is a full reimplementation of the
 * <code>URLClassLoader</code> from the JDK. It is designed to be fully
 * compatible with a normal <code>URLClassLoader</code>, although its internal
 * behavior may be completely different.
 * <p>
 * <strong>IMPLEMENTATION NOTE</strong> - By default, this class loader follows
 * the delegation model required by the specification. The system class
 * loader will be queried first, then the local repositories, and only then
 * delegation to the parent class loader will occur. This allows the web
 * application to override any shared class except the classes from J2SE.
 * Special handling is provided from the JAXP XML parser interfaces, the JNDI
 * interfaces, and the classes from the servlet API, which are never loaded
 * from the webapp repositories. The <code>delegate</code> property
 * allows an application to modify this behavior to move the parent class loader
 * ahead of the local repositories.
 * <p>
 * <strong>IMPLEMENTATION NOTE</strong> - Due to limitations in Jasper
 * compilation technology, any repository which contains classes from
 * the servlet API will be ignored by the class loader.
 * <p>
 * <strong>IMPLEMENTATION NOTE</strong> - The class loader generates source
 * URLs which include the full JAR URL when a class is loaded from a JAR file,
 * which allows setting security permission at the class level, even when a
 * class is contained inside a JAR.
 * <p>
 * <strong>IMPLEMENTATION NOTE</strong> - Local repositories are searched in
 * the order they are added via the initial constructor.
 * <p>
 * <strong>IMPLEMENTATION NOTE</strong> - No check for sealing violations or
 * security is made unless a security manager is present.
 * <p>
 * <strong>IMPLEMENTATION NOTE</strong> - As of 8.0, this class
 * loader implements ..., permitting web
 * application classes to instrument other classes in the same web
 * application. It does not permit instrumentation of system or container
 * classes or classes in other web apps.
 *
 * @author Remy Maucherat
 * @author Craig R. McClanahan
 */
public abstract class WebappClassLoaderBase extends URLClassLoader {

    private static final String CLASS_FILE_SUFFIX = ".class";

    protected final ClassLoader parent;

    private String basePath;


    // ----------------------------------------------------------- Constructors

    /**
     * Construct a new ClassLoader with no defined repositories and no
     * parent ClassLoader.
     */
    protected WebappClassLoaderBase(String basePath) {

        super(new URL[0]);

        ClassLoader p = getParent();
        if (p == null) {
            p = getSystemClassLoader();
        }
        this.parent = p;
        this.basePath = basePath;
    }


    /**
     * Construct a new ClassLoader with no defined repositories and the given
     * parent ClassLoader.
     * <p>
     *
     * @param parent Our parent class loader
     */
    protected WebappClassLoaderBase(ClassLoader parent, String basePath) {

        super(new URL[0], parent);

        ClassLoader p = getParent();
        if (p == null) {
            p = getSystemClassLoader();
        }
        this.parent = p;
        this.basePath = basePath;
    }


    // ----------------------------------------------------- Instance Variables

    /**
     * The cache of ResourceEntry for classes and resources we have loaded,
     * keyed by resource path, not binary name. Path is used as the key since
     * resources may be requested by binary name (classes) or path (other
     * resources such as property files) and the mapping from binary name to
     * path is unambiguous but the reverse mapping is ambiguous.
     */
    protected final Map<String, ResourceEntry> resourceEntries =
            new ConcurrentHashMap<>();



    private String binaryNameToPath(String binaryName, boolean withLeadingSlash) {
        // 1 for leading '/', 6 for ".class"
        StringBuilder path = new StringBuilder(7 + binaryName.length());
        if (withLeadingSlash) {
            path.append('/');
        }
        path.append(binaryName.replace('.', '/'));
        path.append(CLASS_FILE_SUFFIX);
        return path.toString();
    }


    private String nameToPath(String name) {
        if (name.startsWith("/")) {
            return name;
        }
        StringBuilder path = new StringBuilder(
                1 + name.length());
        path.append('/');
        path.append(name);
        return path.toString();
    }


    /**
     * Returns true if the specified package name is sealed according to the
     * given manifest.
     *
     * @param name Path name to check
     * @param man Associated manifest
     * @return <code>true</code> if the manifest associated says it is sealed
     */
    protected boolean isPackageSealed(String name, Manifest man) {

        String path = name.replace('.', '/') + '/';
        Attributes attr = man.getAttributes(path);
        String sealed = null;
        if (attr != null) {
            sealed = attr.getValue(Name.SEALED);
        }
        if (sealed == null) {
            if ((attr = man.getMainAttributes()) != null) {
                sealed = attr.getValue(Name.SEALED);
            }
        }
        return "true".equalsIgnoreCase(sealed);

    }


    /**
     * Finds the class with the given name if it has previously been
     * loaded and cached by this class loader, and return the Class object.
     * If this class has not been cached, return <code>null</code>.
     *
     * @param name The binary name of the resource to return
     * @return a loaded class
     */
    protected Class<?> findLoadedClass0(String name) {

        String path = binaryNameToPath(name, true);

        ResourceEntry entry = resourceEntries.get(path);
        if (entry != null) {
            return entry.loadedClass;
        }
        return null;
    }

    /**
     * Find specified class in local repositories.
     *
     * @param name The binary name of the class to be loaded
     *
     * @return the loaded class, or null if the class isn't found
     */
    public Class<?> findClassInternal(String name) {

        if (name == null) {
            return null;
        }
        String path = binaryNameToPath(name, true);

        ResourceEntry entry = resourceEntries.get(path);

        if (entry == null) {

            entry = new ResourceEntry();

            // Add the entry in the local resource repository
            synchronized (resourceEntries) {
                // Ensures that all the threads which may be in a race to load
                // a particular class all end up with the same ResourceEntry
                // instance
                ResourceEntry entry2 = resourceEntries.get(path);
                if (entry2 == null) {
                    resourceEntries.put(path, entry);
                } else {
                    entry = entry2;
                }
            }
        }

        Class<?> clazz = entry.loadedClass;
        if (clazz != null)
            return clazz;

        synchronized (this) {
            clazz = entry.loadedClass;
            if (clazz != null)
                return clazz;

            byte[] binaryContent = null;
            try {
                FileInputStream fileInputStream = new FileInputStream(new File(basePath + path));
                binaryContent = fileInputStream.readAllBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }


            if (binaryContent == null) {
                // Something went wrong reading the class bytes (and will have
                // been logged at debug level).
                return null;
            }
            URL codeBase = null;
            try {
                //TODO get path
                codeBase = new File(basePath).toURI().toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Certificate[] certificates = null;


            // Looking up the package
            String packageName = null;
            int pos = name.lastIndexOf('.');
            if (pos != -1)
                packageName = name.substring(0, pos);

            Package pkg = null;

            if (packageName != null) {
                pkg = getPackage(packageName);
                // Define the package (if null)
                if (pkg == null) {
                    try {
                        definePackage(packageName, null, null, null, null, null, null, null);
                    } catch (IllegalArgumentException e) {
                        // Ignore: normal error due to dual definition of package
                    }
                    pkg = getPackage(packageName);
                }
            }


            try {
                clazz = defineClass(name, binaryContent, 0,
                        binaryContent.length, new CodeSource(codeBase, certificates));
            } catch (UnsupportedClassVersionError ucve) {
                throw new UnsupportedClassVersionError(
                        ucve.getLocalizedMessage() + "webappClassLoader.wrongVersion");
            }
            entry.loadedClass = clazz;
        }

        return clazz;
    }


    /**
     * Filter classes.
     *
     * @param name class name
     * @param isClassName <code>true</code> if name is a class name,
     *                <code>false</code> if name is a resource name
     * @return <code>true</code> if the class should be filtered
     */
    protected boolean filter(String name, boolean isClassName) {

        if (name == null)
            return false;

        char ch;
        if (name.startsWith("javax")) {
            /* 5 == length("javax") */
            if (name.length() == 5) {
                return false;
            }
            ch = name.charAt(5);
            if (isClassName && ch == '.') {
                /* 6 == length("javax.") */
                if (name.startsWith("servlet.jsp.jstl.", 6)) {
                    return false;
                }
                if (name.startsWith("el.", 6) ||
                    name.startsWith("servlet.", 6) ||
                    name.startsWith("websocket.", 6) ||
                    name.startsWith("security.auth.message.", 6)) {
                    return true;
                }
            } else if (!isClassName && ch == '/') {
                /* 6 == length("javax/") */
                if (name.startsWith("servlet/jsp/jstl/", 6)) {
                    return false;
                }
                if (name.startsWith("el/", 6) ||
                    name.startsWith("servlet/", 6) ||
                    name.startsWith("websocket/", 6) ||
                    name.startsWith("security/auth/message/", 6)) {
                    return true;
                }
            }
        } else if (name.startsWith("org")) {
            /* 3 == length("org") */
            if (name.length() == 3) {
                return false;
            }
            ch = name.charAt(3);
            if (isClassName && ch == '.') {
                /* 4 == length("org.") */
                if (name.startsWith("apache.", 4)) {
                    /* 11 == length("org.apache.") */
                    if (name.startsWith("tomcat.jdbc.", 11)) {
                        return false;
                    }
                    if (name.startsWith("el.", 11) ||
                        name.startsWith("catalina.", 11) ||
                        name.startsWith("jasper.", 11) ||
                        name.startsWith("juli.", 11) ||
                        name.startsWith("tomcat.", 11) ||
                        name.startsWith("naming.", 11) ||
                        name.startsWith("coyote.", 11)) {
                        return true;
                    }
                }
            } else if (!isClassName && ch == '/') {
                /* 4 == length("org/") */
                if (name.startsWith("apache/", 4)) {
                    /* 11 == length("org/apache/") */
                    if (name.startsWith("tomcat/jdbc/", 11)) {
                        return false;
                    }
                    if (name.startsWith("el/", 11) ||
                        name.startsWith("catalina/", 11) ||
                        name.startsWith("jasper/", 11) ||
                        name.startsWith("juli/", 11) ||
                        name.startsWith("tomcat/", 11) ||
                        name.startsWith("naming/", 11) ||
                        name.startsWith("coyote/", 11)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
