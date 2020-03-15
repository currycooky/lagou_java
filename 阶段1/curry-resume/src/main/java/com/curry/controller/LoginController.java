package com.curry.controller;

import com.curry.dao.ResumeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author curry
 */
@Controller
public class LoginController {

    @PostMapping(value = "/login")
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
