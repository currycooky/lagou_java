package com.curry.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author curry
 */
@Controller
public class LoginController {

    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    public String login(HttpServletRequest request, HttpSession session, Model model) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if ("admin".equals(username) && "admin".equals(password)) {
            session.setAttribute("user", "admin");
            return "main";
        }
        model.addAttribute("errorMsg", "账号或密码错误！");
        return "index";
    }
}
