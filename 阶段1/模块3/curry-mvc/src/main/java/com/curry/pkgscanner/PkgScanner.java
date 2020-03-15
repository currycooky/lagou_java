package com.curry.pkgscanner;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 包扫描
 *
 * @author curry
 */
public class PkgScanner {
    /**
     * 包名
     */
    private String pkgName;

    /**
     * 包对应的路径名
     */
    private String pkgPath;

    /**
     * 注解的class对象
     */
    private Class anClazz;

    private ClassLoader cl;


    public PkgScanner(String pkgName) {
        this.pkgName = pkgName;
        this.pkgPath = PathUtils.packageToPath(pkgName);

        cl = Thread.currentThread().getContextClassLoader();
    }

    public PkgScanner(String pkgName, Class anClazz) {
        this(pkgName);
        this.anClazz = anClazz;
    }

    /**
     * 执行扫描操作.
     *
     * @return
     * @throws IOException
     */
    public List<Class<?>> scan() {
        List<String> list = null;
        try {
            list = loadResource();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (list == null || list.size() < 1) {
            return Collections.emptyList();
        }
        return filterComponents(list);
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
        this.pkgPath = PathUtils.packageToPath(pkgName);
    }

    public void setAnnocation(Class an) {
        this.anClazz = an;
    }

    private List<String> loadResource() throws IOException {
        List<String> list = null;

        Enumeration<URL> urls = cl.getResources(pkgPath);
        while (urls.hasMoreElements()) {
            URL u = urls.nextElement();

            ResourceType type = determineType(u);

            switch (type) {
//                case JAR:
//                    String path = PathUtils.distillPathFromJarURL(u.getPath());
//                    list = scanJar(path);
//                    break;
                case FILE:
                    list = scanFile(u.getPath(), pkgName);
                    break;
                default:
            }
        }

        return list;
    }

    /**
     * 根据URL判断是JAR包还是文件目录
     * @param url
     * @return
     */
    private ResourceType determineType(URL url) {
        if (url.getProtocol().equals(ResourceType.FILE.getTypeString())) {
            return ResourceType.FILE;
        }

        if (url.getProtocol().equals(ResourceType.JAR.getTypeString())) {
            return ResourceType.JAR;
        }

        throw new IllegalArgumentException("不支持该类型:" + url.getProtocol());
    }

    /**
     * 扫描JAR文件
     * @param path
     * @return
     * @throws IOException
     */
    private List<String> scanJar(String path) throws IOException {
        JarFile jar = new JarFile(path);

        List<String> classNameList = new ArrayList<>(20);

        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();

            if( (name.startsWith(pkgPath)) && (name.endsWith(ResourceType.CLASS_FILE.getTypeString())) ) {
                name = PathUtils.trimSuffix(name);
                name = PathUtils.pathToPackage(name);

                classNameList.add(name);
            }
        }

        return classNameList;
    }

    /**
     * 扫描文件目录下的类
     * @param path
     * @return
     */
    private List<String> scanFile(String path, String basePkg) {
        File f = new File(path);

        List<String> classNameList = new ArrayList<>(10);

        // 得到目录下所有文件(目录)
        File[] files = f.listFiles();
        if (null != files) {
            int LEN = files.length;

            for (int ix = 0 ; ix < LEN ; ++ix) {
                File file = files[ix];

                // 判断是否还是一个目录
                if (file.isDirectory()) {
                    // 递归遍历目录
                    List<String> list = scanFile(file.getAbsolutePath(), PathUtils.concat(basePkg, ".", file.getName()));
                    classNameList.addAll(list);

                } else if (file.getName().endsWith(ResourceType.CLASS_FILE.getTypeString())) {
                    // 如果是以.class结尾
                    String className = PathUtils.trimSuffix(file.getName());
                    // 如果类名中有"$"不计算在内
                    if (-1 != className.lastIndexOf("$")) {
                        continue;
                    }

                    // 命中
                    String result = PathUtils.concat(basePkg, ".", className);
                    classNameList.add(result);
                }
            }
        }

        return classNameList;
    }

    /**
     * 过虑掉没有指定注解的类
     * @param classNameList
     * @return
     */
    private List<Class<?>> filterComponents(List<String> classNameList) {
        List<Class<?>> classList = new ArrayList<>(20);
        if (this.anClazz == null) {
            for (String name : classNameList) {
                try {
                    classList.add(Class.forName(name));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return classList;
        }
        for (String name : classNameList) {
           try {
               Class clazz = Class.forName(name);
               Annotation an = clazz.getAnnotation(this.anClazz);
               if (null != an) {
                   classList.add(clazz);
               }
           } catch (ClassNotFoundException e) {
               e.printStackTrace();
           }
        }
        return classList;
    }
}
