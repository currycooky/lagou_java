package com.curry.pkgscanner;

/**
 * 源文件类型
 *
 * @author curry
 */
public enum ResourceType {
    JAR("jar"),
    FILE("file"),
    CLASS_FILE(".class");

    private String typeString;

    ResourceType(String typeString) {
        this.typeString = typeString;
    }

    public String getTypeString() {
        return this.typeString;
    }
}
