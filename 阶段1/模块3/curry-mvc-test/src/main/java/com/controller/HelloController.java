package com.controller;

import com.curry.annotation.CurryController;
import com.curry.annotation.CurryRequestMapping;
import com.curry.annotation.CurrySecurity;

/**
 * @author curry
 */
@CurryController
@CurryRequestMapping(value = "/hello")
public class HelloController {
    /**
     * 只有管理员才能请求
     */
    @CurryRequestMapping(value = "/admin")
    @CurrySecurity(value = "admin")
    public void helloAdmin() {
        System.out.println("Hello admin.");
    }

    /**
     * 只有管理员和注册用户才能请求
     */
    @CurryRequestMapping(value = "/user")
    @CurrySecurity(value = {"admin", "user"})
    public void helloUser() {
        System.out.println("Hello user.");
    }

    /**
     * 不限制用户请求
     */
    @CurryRequestMapping(value = "/guest")
    public void helloGuest() {
        System.out.println("Hello everyone.");
    }
}
