package com.sg.blogcms.controllers;

import com.sg.blogcms.model.PostDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    PostDao pDao;

    /**
     * Load the home page
     *
     * @param model {Model} holds list of static pages
     * @return {String} main domain
     */
    @GetMapping({"/", "/home"})
    public String displayHomePage(Model model) {
        model.addAttribute("staticPages", pDao.readAllStatic());

        return "home";
    }
}
