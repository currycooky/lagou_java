package com.curry.controller;

import com.curry.dao.ResumeDao;
import com.curry.entity.Resume;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 简历控制层，省略service层逻辑代码，直接写入到控制层
 *
 * @author curry
 */
@Controller
@RequestMapping(value = "/resume")
public class ResumeController {
    @Autowired
    @SuppressWarnings("all")
    private ResumeDao resumeDao;

    /**
     * 查询全部的简历数据
     *
     * @return
     */
    @RequestMapping(value = "/", method = {RequestMethod.GET})
    public String selectAll(ModelMap modelMap) {
        System.out.println("进入了查询方法");
        modelMap.addAttribute("list", resumeDao.findAll());
        return "main";
    }

    /**
     * 添加或者修改一个新的简历信息
     *
     * @param resume
     * @return
     */
    @RequestMapping(value = "/", method = {RequestMethod.POST, RequestMethod.PUT})
    public String add(Resume resume) {
        resumeDao.save(resume);
        return "main";
    }

    /**
     * 删除指定的简历数据
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    public String delete(@PathVariable("id") Long id) {
        resumeDao.delete(id);
        return "main";
    }

}
