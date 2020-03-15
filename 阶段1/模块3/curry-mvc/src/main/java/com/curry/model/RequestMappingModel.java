package com.curry.model;

import java.lang.reflect.Method;

/**
 * 处理请求url和方法的映射模型
 *
 * @author curry
 */
public class RequestMappingModel {
    public RequestMappingModel() {
    }

    public RequestMappingModel(String url, Method method, Object obj, String[] securities) {
        this.url = url;
        this.method = method;
        this.obj = obj;
        this.securities = securities;
    }

    /**
     * url
     */
    private String url;

    /**
     * url映射到的方法
     */
    private Method method;

    /**
     * 执行方法的实体
     */
    private Object obj;

    /**
     * 方法权限设置
     */
    private String[] securities;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String[] getSecurities() {
        return securities;
    }

    public void setSecurities(String[] securities) {
        this.securities = securities;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
