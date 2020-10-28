package com.sg.blogcms.controllers;

import com.sg.blogcms.entity.Category;
import com.sg.blogcms.model.CategoryDao;
import com.sg.blogcms.model.PostDao;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CategoryController {

    @Autowired
    CategoryDao cDao;
    @Autowired
    PostDao pDao;
    Set<ConstraintViolation<Category>> violations = new HashSet<>();

    /**
     * GET - load the category creation main page - admin/creator
     *
     * @param model {Model} load related lists and errors
     * @return {String} load create category page
     */
    @GetMapping("/createCategory")
    public String displayCreateCategoryPage(Model model) {
        model.addAttribute("staticPages", pDao.readAllStatic());
        model.addAttribute("categories", cDao.readAllCategories());
        model.addAttribute("errors", violations);

        return "createCategory";
    }

    /**
     * Create a new category to hashtag a post
     *
     * @param request {HttpServletRequest} pulls in form data
     * @param model   {Model} holds errors if category is invalid
     * @return {String} redirect to post creation page if created, reload page
     *         with errors if fail
     */
    @PostMapping("createCategory")
    public String addCategory(HttpServletRequest request, Model model) {
        Category c = new Category();
        String categoryString = request.getParameter("newCategory");
        c.setCategory(categoryString);

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(c);

        if (violations.isEmpty()) {
            cDao.createCategory(c);
        } else {
            model.addAttribute("errors", violations);
            return "createCategory";
        }

        return "redirect:/createPost";
    }

    /**
     * GET - load edit category page
     *
     * @param model   {Model} holds category obj and errors
     * @param request {HttpServletRequest} pulls in id for edit
     * @return {String} load edit category page
     */
    @GetMapping("/editCategory")
    public String editCategoryDisplay(Model model, HttpServletRequest request) {
        model.addAttribute("staticPages", pDao.readAllStatic());
        Category category = cDao.readCategoryById(Integer.parseInt(request.getParameter("id")));

        model.addAttribute("category", category);
        model.addAttribute("errors", violations);

        return "editCategory";
    }

    /**
     * POST - attempt a category edit
     *
     * @param request {HttpServletRequest} pulls in id
     * @param model   {Model} holds obj and errors on fail to edit
     * @return {String} redirect to post management page if edited, reload with
     *         errors if failed
     */
    @PostMapping(value = "/editCategory")
    public String editCategoryAction(HttpServletRequest request, Model model) {
        Category c = cDao.readCategoryById(Integer.parseInt(request.getParameter("id")));
        String categoryString = request.getParameter("category");
        c.setCategory(categoryString);

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(c);

        if (violations.isEmpty()) {
            cDao.updateCategory(c);
        } else {
            model.addAttribute("category", c);
            model.addAttribute("errors", violations);
            return "editCategory";
        }

        return "redirect:/postManagement";
    }

    /**
     * POST - delete a category/hashtag
     *
     * @param request {HttpServletRequest} will retrieve id from data table
     * @return {String} reload the post management page
     */
    @PostMapping("/deleteCategory")
    public String deleteCategory(HttpServletRequest request) {
        cDao.deleteCategoryById(Integer.parseInt(request.getParameter("id")));

        return "redirect:/postManagement";
    }

}
