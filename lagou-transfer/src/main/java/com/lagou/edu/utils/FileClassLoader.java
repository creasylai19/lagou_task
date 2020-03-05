package com.lagou.edu.utils;

import java.io.File;
import java.io.FileInputStream;

public class FileClassLoader extends ClassLoader {

    public Class<?> loadClassFromFile(File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        byte[] b = new byte[fis.available()];
        int len = fis.read(b);
        fis.close();
        return defineClass(null, b, 0, len);
    }


}
