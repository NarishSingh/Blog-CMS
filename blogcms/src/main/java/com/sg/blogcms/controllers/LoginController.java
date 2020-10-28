package com.sg.blogcms.controllers;

import com.sg.blogcms.model.PostDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @Autowired
    PostDao pDao;

    /**
     * GET - Load login page
     *
     * @param model {Model} holds list of static pages
     * @return {String} load login page
     */
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("staticPages", pDao.readAllStatic());

        return "login";
    }
}
