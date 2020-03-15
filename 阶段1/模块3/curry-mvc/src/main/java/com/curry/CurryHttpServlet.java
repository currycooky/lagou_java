package com.curry;

import com.curry.annotation.CurryController;
import com.curry.annotation.CurryRequestMapping;
import com.curry.annotation.CurrySecurity;
import com.curry.model.RequestMappingModel;
import com.curry.pkgscanner.PkgScanner;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 请求处理拦截器
 *
 * @author curry
 */
public class CurryHttpServlet extends HttpServlet {
    /**
     * 缓存url与方法的映射关系
     */
    private Map<String, RequestMappingModel> modelMap = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 处理请求的具体逻辑
        System.out.println("请求了...");
        // 根据接口地址取出映射对象
        String uri = req.getRequestURI();
        RequestMappingModel requestMappingModel = modelMap.get(uri);
        if (requestMappingModel == null) {
            resp.sendError(404, "找不到访问想要访问的地址！");
            return;
        }
        // 获取权限配置
        String[] securities = requestMappingModel.getSecurities();
        if (securities != null && securities.length > 0) {
            // 对请求携带的用户名进行校验，判断是否有权限访问
            String username = req.getParameter("username");
            if (username == null) {
                resp.sendError(400, "请求参数错误");
                return;
            }
            boolean canReq = false;
            for (String sname : securities) {
                if (sname.equals(username)) {
                    canReq = true;
                    break;
                }
            }
            if (!canReq) {
                resp.sendError(403, "用户：" + username + "没有权限访问此接口！");
                return;
            }
        }
        // 可以访问，执行控制层的方法
        Object obj = requestMappingModel.getObj();
        if (obj == null) {
            resp.sendError(500, "控制器实例化时出现错误，无法访问！");
            return;
        }
        try {
            requestMappingModel.getMethod().invoke(obj);
            ServletOutputStream outputStream = resp.getOutputStream();
            outputStream.write("success".getBytes());
            return;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        resp.sendError(500, "执行控制器方法时出现错误，执行异常！");
    }

    @Override
    public void init() throws ServletException {
        // 扫描出所有的控制类
        List<Class<?>> controllerClassList = new PkgScanner("com", CurryController.class).scan();
        for (Class<?> clazz : controllerClassList) {
            // 查看是否设置了类mapping
            String baseUrl = "";
            CurryRequestMapping classMapping = clazz.getAnnotation(CurryRequestMapping.class);
            if (classMapping != null) {
                baseUrl = classMapping.value();
            }
            // 遍历出类中所有的方法
            Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                // 先判断方法上是否标注了url注解
                CurryRequestMapping methodMapping = declaredMethod.getAnnotation(CurryRequestMapping.class);
                if (methodMapping == null) {
                    continue;
                }
                // 拼接出请求的接口地址
                String url = baseUrl + methodMapping.value();
                // 获取配置的权限拦截
                CurrySecurity methodSecurity = declaredMethod.getAnnotation(CurrySecurity.class);
                RequestMappingModel requestMappingModel = new RequestMappingModel(url, declaredMethod, this.createInstance(clazz), null);
                if (methodSecurity != null) {
                    requestMappingModel.setSecurities(methodSecurity.value());
                }
                modelMap.put(url, requestMappingModel);
            }
        }
    }

    /**
     * 创建类型的实体对象，如果出现错误则返回空
     *
     * @param clazz
     * @return
     */
    private Object createInstance(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
