package com.lagou.edu.factory;

import com.fasterxml.jackson.databind.util.ClassUtil;
import com.lagou.edu.pojo.Account;
import com.lagou.edu.pojo.BeanDefinition;
import com.lagou.edu.stereotype.Autowired;
import com.lagou.edu.stereotype.Component;
import com.lagou.edu.stereotype.Service;
import com.lagou.edu.stereotype.Transactional;
import com.lagou.edu.utils.ClassUtils;
import com.lagou.edu.utils.FileClassLoader;
import com.lagou.edu.utils.ReflectionUtils;
import com.lagou.edu.utils.StringUtils;
import net.sf.cglib.core.CollectionUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * @author 应癫
 *
 * 工厂类，生产对象（使用反射技术）
 */
public class BeanFactory {

    /**
     * 任务一：读取解析xml，通过反射技术实例化对象并且存储待用（map集合）
     * 任务二：对外提供获取实例对象的接口（根据id获取）
     */

    private static Map<String,Object> map = new HashMap<>();  // 存储对象


    static {
        // 任务一：读取解析xml，通过反射技术实例化对象并且存储待用（map集合）
        // 加载xml
        InputStream resourceAsStream = BeanFactory.class.getClassLoader().getResourceAsStream("beans.xml");
        // 解析xml
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();
            List<Element> beanList = rootElement.selectNodes("//bean");
            for (int i = 0; i < beanList.size(); i++) {
                Element element =  beanList.get(i);
                // 处理每个bean元素，获取到该元素的id 和 class 属性
                String id = element.attributeValue("id");        // accountDao
                String clazz = element.attributeValue("class");  // com.lagou.edu.dao.impl.JdbcAccountDaoImpl
                // 通过反射技术实例化对象
                Class<?> aClass = Class.forName(clazz);

                Object o = aClass.getConstructor().newInstance();  // 实例化之后的对象

                // 存储到map中待用
                map.put(id,o);

            }

            // 实例化完成之后维护对象的依赖关系，检查哪些对象需要传值进入，根据它的配置，我们传入相应的值
            // 有property子元素的bean就有传值需求
            List<Element> propertyList = rootElement.selectNodes("//property");
            // 解析property，获取父元素
            for (int i = 0; i < propertyList.size(); i++) {
                Element element =  propertyList.get(i);   //<property name="AccountDao" ref="accountDao"></property>
                String name = element.attributeValue("name");
                String ref = element.attributeValue("ref");

                // 找到当前需要被处理依赖关系的bean
                Element parent = element.getParent();

                // 调用父元素对象的反射功能
                String parentId = parent.attributeValue("id");
                Object parentObject = map.get(parentId);
                // 遍历父对象中的所有方法，找到"set" + name
                Method[] methods = parentObject.getClass().getMethods();
                for (int j = 0; j < methods.length; j++) {
                    Method method = methods[j];
                    if(method.getName().equalsIgnoreCase("set" + name)) {  // 该方法就是 setAccountDao(AccountDao accountDao)
                        method.invoke(parentObject,map.get(ref));
                    }
                }

                // 把处理之后的parentObject重新放到map中
                map.put(parentId,parentObject);

            }

            List<Element> component_scan = rootElement.selectNodes("//component-scan");
            if( null != component_scan && component_scan.size() > 0 ) {
                String basePackage = component_scan.get(0).attributeValue("base-package");
                String packageSearchPath = ClassUtils.convertClassNameToResourcePath(basePackage);
                List<BeanDefinition> beanDefinitions = loadDefinitions(packageSearchPath);
                finishBeanInitialization(beanDefinitions);
            }


        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据BeanDefinition进行实例初始化
     * @param beanDefinitions
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    private static void finishBeanInitialization(List<BeanDefinition> beanDefinitions) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Map<String,Object> typeAndValue = new HashMap<>();  // 存储对象
        for (BeanDefinition beanDefinition : beanDefinitions) {
            if(beanDefinition.getSource() instanceof Class ) {
                Class temp = (Class)beanDefinition.getSource();
                Constructor constructor = temp.getConstructor();
                Object instance = constructor.newInstance();
                map.put(beanDefinition.getBeanName(), instance);
                typeAndValue.put(temp.getCanonicalName(), instance);
            }
        }

        for (BeanDefinition beanDefinition : beanDefinitions) {
            for (Field autowiredField : beanDefinition.getAutowiredFields()) {
                ReflectionUtils.makeAccessible(autowiredField);
                autowiredField.set(typeAndValue.get(beanDefinition.getBeanClass()), retrieveValue(typeAndValue, autowiredField));
            }
        }

        for (BeanDefinition beanDefinition : beanDefinitions) {
            if( beanDefinition.getTransactionMethods().size() > 0 ) {
                Class temp = (Class)beanDefinition.getSource();
                Class<?>[] classes = temp.getInterfaces();
                ProxyFactory proxyFactory = (ProxyFactory) map.get("proxyFactory");
                if( classes.length > 0 ) {
                    map.put(beanDefinition.getBeanName(), proxyFactory.getJdkProxy(map.get(beanDefinition.getBeanName())));
                }else {
                    map.put(beanDefinition.getBeanName(), proxyFactory.getCglibProxy(map.get(beanDefinition.getBeanName())));
                }
            }
        }
    }

    /**
     * 检索值
     * @param typeAndValue
     * @param autowiredField
     * @return
     */
    private static Object retrieveValue(Map<String, Object> typeAndValue, Field autowiredField) {
        Object value = typeAndValue.get(autowiredField.getType().getCanonicalName());
        if( value == null ) {
            for (Object o : typeAndValue.values()) {
                if( autowiredField.getType().isAssignableFrom(o.getClass()) ) {
                    return o;
                }
            }
        }
        return value;
    }


    private static List<BeanDefinition> loadDefinitions(String basePackage) throws Exception {
        File file = null;
        URL url = BeanFactory.class.getClassLoader().getResource(basePackage);
        try {
            String pathName = new URI(url.toString().replace(" ", "%20")).getSchemeSpecificPart();
            file = new File(pathName).getAbsoluteFile();
        }
        catch (URISyntaxException ex) {
            // Fallback for URLs that are not valid URIs (should hardly ever happen).
            file = new File(url.getFile()).getAbsoluteFile();
        }
        Set<String> result = new LinkedHashSet<>(8);
        loadClassNames(file, result, file.getAbsolutePath().substring(0, (file.getAbsolutePath().length() - basePackage.length())));
        return loadDefinitionsFromClassNames(result);
    }

    /**
     * 生成BeanDefinition
     * @param result
     * @return
     */
    private static List<BeanDefinition> loadDefinitionsFromClassNames(Set<String> result) {
        List<BeanDefinition> beanDefinitions = new ArrayList<>();
        try {
            for (String className : result) {
                Class<?> classObject = BeanFactory.class.getClassLoader().loadClass(className);
                if( Object.class.isAssignableFrom(classObject) && !classObject.isAnnotation()) {
                    Annotation[] annotations = classObject.getAnnotations();
                    for (Annotation annotation : annotations) {
                        if( annotationMatch(Component.class, annotation.annotationType()) ) {
                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setBeanClass(classObject.getCanonicalName());
                            beanDefinition.setSource(classObject);
                            //处理bean名
                            Method method = annotation.annotationType().getMethod("value");
                            ReflectionUtils.makeAccessible(method);
                            String value = (String)method.invoke(annotation);
                            value = "".equals(value) ? classObject.getSimpleName() : value;
                            beanDefinition.setBeanName(StringUtils.decapitalize(value));
                            //处理Autowired局部变量
                            Field[] fields = classObject.getDeclaredFields();
                            List<Field> autowiredFields = new ArrayList<>();
                            for (Field field : fields) {
                                Annotation[] fieldAnnotations = field.getAnnotations();
                                for (Annotation fieldAnnotation : fieldAnnotations) {
                                    if( annotationMatch(Autowired.class, fieldAnnotation.annotationType()) ) {
                                        autowiredFields.add(field);
                                        break;
                                    }
                                }
                            }
                            beanDefinition.setAutowiredFields(autowiredFields);
                            //处理事务方法
                            Method[] methods = classObject.getDeclaredMethods();
                            List<Method> transactionalMethods = new ArrayList<>();
                            if( annotationMatch(Transactional.class, annotation.annotationType()) ){
                                for (Method method1 : methods) {
                                    transactionalMethods.add(method1);
                                }
                            } else {
                                for (Method method1 : methods) {
                                    Annotation[] methodAnnotations = method1.getAnnotations();
                                    for (Annotation methodAnnotation : methodAnnotations) {
                                        if( annotationMatch(Transactional.class, methodAnnotation.annotationType()) ) {
                                            transactionalMethods.add(method1);
                                            break;
                                        }
                                    }
                                }
                            }
                            beanDefinition.setTransactionMethods(transactionalMethods);

                            beanDefinitions.add(beanDefinition);
                            break;
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return beanDefinitions;
    }

    private static boolean annotationMatch( Class<?> parent, Class<?> child ){
        if( parent.isAssignableFrom(child) ) {
            return true;
        } else {
            Annotation[] annotations = child.getAnnotations();
            for (Annotation annotation : annotations) {
                if( !isInJavaLangAnnotationPackage(annotation.annotationType().getName()) ) {
                    if(annotationMatch(parent, annotation.annotationType())){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isInJavaLangAnnotationPackage(String annotationType) {
        return (annotationType != null && annotationType.startsWith("java.lang.annotation"));
    }

    private static void loadClassNames(File file, Set<String> result, String projectPath){
        File[] files = file.listFiles();
        for (File content : files) {
            if( content.isDirectory() ) {
                loadClassNames(content, result, projectPath);
            } else {
                String currPath = content.getAbsolutePath();
                if( currPath.endsWith(".class") ) {
                    String classNameContainExtension = content.getAbsolutePath().substring(projectPath.length()).replace(File.separator, ".");
                    result.add(classNameContainExtension.substring(0, classNameContainExtension.length() - ".class".length()));
                }
            }
        }
    }


    // 任务二：对外提供获取实例对象的接口（根据id获取）
    public static  Object getBean(String id) {
        return map.get(id);
    }


    public static void main(String[] args) throws Exception {
//        Map<String, Account> map = new HashMap<>();
//        Account account = new Account();
//        account.setCardNo("CardNo");
//        map.put("1", account);
//        System.out.println(map);
//        Method method = account.getClass().getMethod("setName", String.class);
//        method.invoke(account, "Name");
//        Account newGet = map.get("1");
//        System.out.println("==================================");
//        System.out.println(newGet);
//        System.out.println(account == newGet);
//        System.out.println(map);

//        loadDefinitions("com/lagou/edu");
        System.out.println("");
//        System.out.println(annotationMatch(Component.class, Service.class));
//        System.out.println(Component.class.getCanonicalName());
//        Annotation[] annotations = Service.class.getAnnotations();
//        for (Annotation annotation : annotations) {
//            System.out.println("CanonicalName" + annotation.annotationType().getCanonicalName());
//            System.out.println("Name:" + annotation.annotationType().getName());
//            System.out.println("SimpleName:" + annotation.annotationType().getSimpleName());
//        }
//        System.out.println(Service.class.getCanonicalName());

    }
}
