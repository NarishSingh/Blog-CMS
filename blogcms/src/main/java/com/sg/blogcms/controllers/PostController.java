package com.sg.blogcms.controllers;

import com.sg.blogcms.entity.Category;
import com.sg.blogcms.entity.Post;
import com.sg.blogcms.entity.User;
import com.sg.blogcms.model.CategoryDao;
import com.sg.blogcms.model.ImageDao;
import com.sg.blogcms.model.PostDao;
import com.sg.blogcms.model.UserDao;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PostController {

    @Autowired
    UserDao uDao;
    @Autowired
    CategoryDao cDao;
    @Autowired
    ImageDao iDao;
    @Autowired
    PostDao pDao;
    private final String coverUploadDir = "Posts";
    Set<ConstraintViolation<Post>> violations = new HashSet<>();

    /*MAIN - ADMIN/CREATOR*/
    /**
     * GET - load the post creation main page - admin/creator
     *
     * @param model {Model} load related lists and errors
     * @return {String} load create post page
     */
    @GetMapping("/createPost")
    public String displayCreatePage(Model model) {
        model.addAttribute("staticPages", pDao.readAllStatic());
        model.addAttribute("categories", cDao.readAllCategories());
        model.addAttribute("errors", violations);
        String now = LocalDateTime.now().withSecond(0).withNano(0).toString();
        model.addAttribute("now", now); //to spoof a time for postOn

        return "createPost";
    }

    /**
     * GET - load blog feed
     *
     * @param model {Model} holds all publishable posts
     * @return {String} load blog page
     */
    @GetMapping("/blog")
    public String displayBlog(Model model) {
        model.addAttribute("posts", pDao.readAllForPublication()); //only publishable poss
        model.addAttribute("staticPages", pDao.readAllStatic());

        return "blog";
    }

    /**
     * GET - load post management main page - admin only
     *
     * @param model {Model} holds post list
     * @return {String} load post management page
     */
    @GetMapping("/postManagement")
    public String displayPostManagementPage(Model model) {
        model.addAttribute("staticPages", pDao.readAllStatic());
        model.addAttribute("posts", pDao.readAllPosts());
        model.addAttribute("categories", cDao.readAllCategories());

        return "postManagement";
    }

    /*CREATE - ADMIN/CREATOR*/
    /**
     * POST - create a new blog post
     *
     * @param request    {HttpServletRequest} pull in form data
     * @param file       {MultipartFile} pull in cover photo
     * @param staticPage {Boolean} from checkbox indicating if page is static or
     *                   not
     * @param auth       {Authentication} to access authenticated user
     * @param postOn     {LocalDateTime} for when to post blog to feed
     * @param expireOn   {LocalDateTime} for when to remove blog from feed
     * @param model      {Model} hold lists and errors on fail to post
     * @return {String} redirect to blog scroll, reload page w errors if fail
     */
    @PostMapping("/addPost")
    public String addPost(Model model, HttpServletRequest request, @RequestParam("file") MultipartFile file,
            Boolean staticPage, Authentication auth,
            @RequestParam("postOn") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime postOn,
            @RequestParam("expireOn") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime expireOn) {
        Post post = new Post();
        post.setTitle(request.getParameter("title"));
        post.setBody(request.getParameter("body"));
        post.setApproved(false); //admin must approve

        //static page status from check box
        if (staticPage != null) {
            post.setStaticPage(true);
        } else {
            post.setStaticPage(false);
        }

        //datetime setting
        post.setCreatedOn(LocalDateTime.now().withNano(0));
        post.setPostOn(postOn);
        post.setExpireOn(expireOn);

        //pull name from Spring security authentication then read in User obj
        User author = uDao.readUserByUsername(auth.getName());
        post.setUser(author);

        String filePath = iDao.saveImage(file, Long.toString(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)), coverUploadDir);
        post.setPhotoFilename(filePath);

        //set categories from multi select
        List<Category> categories = new ArrayList<>();
        String[] categoryIds = request.getParameterValues("categoryId");
        if (categoryIds != null) {
            for (String id : categoryIds) {
                categories.add(cDao.readCategoryById(Integer.parseInt(id)));
            }
            post.setCategories(categories);
        }

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(post);

        if (violations.isEmpty()) {
            pDao.createPost(post);
        } else {
            model.addAttribute("categories", cDao.readAllCategories());
            model.addAttribute("errors", violations);
            String now = LocalDateTime.now().withSecond(0).withNano(0).toString();
            model.addAttribute("now", now);

            return "createPost"; //reload w errors
        }

        return "redirect:/blog";
    }

    /*READ/VIEW - ADMIN AND PUBLIC*/
    /**
     * GET - load up a post for viewing
     *
     * @param model   {Model} holds post obj
     * @param request {HttpServletRequest} used to pull the id from GET request
     * @return {String} load viewing page for post
     */
    @GetMapping("/viewPost")
    public String displayViewPostPage(Model model, HttpServletRequest request) {
        model.addAttribute("staticPages", pDao.readAllStatic());
        Post post = pDao.readPostById(Integer.parseInt(request.getParameter("id")));
        model.addAttribute("post", post);

        return "viewPost";
    }

    /*EDIT - ADMIN ONLY*/
    /**
     * GET - load up an edit post page with the data from original post
     *
     * @param model   {Model} holds post obj and related data for edit
     * @param request {HttpServletRequest} pulls in id from get request
     * @return {String} load edit post page
     */
    @GetMapping("/editPost")
    public String editPostDisplay(Model model, HttpServletRequest request) {
        model.addAttribute("staticPages", pDao.readAllStatic());
        Post post = pDao.readPostById(Integer.parseInt(request.getParameter("id")));

        model.addAttribute("post", post);
        model.addAttribute("categories", cDao.readAllCategories());
        model.addAttribute("errors", violations);
        String now = LocalDateTime.now().withSecond(0).withNano(0).toString();
        model.addAttribute("now", now); //to spoof a time for postOn

        return "editPost";
    }

    /**
     * POST - attempt a edit of an existing post - admin only, and only way to
     * approve a post
     *
     * @param model      {Model} holds post obj and related lists in case of
     *                   error
     * @param request    {HttpServletRequest} pulls in form data
     * @param file       {MultipartFile} pull in cover photo edit
     * @param staticPage {Boolean} static page indicator from form data
     * @param approved   {Boolean} admin approval of post from form data
     * @param postOn     {LocalDateTime} for when to post blog to feed
     * @param expireOn   {LocalDateTime} for when to remove blog from feed
     * @return {String} redirect to management page if edited, reload with
     *         errors if failed
     */
    @PostMapping(value = "/editPost")
    public String editPostAction(Model model, HttpServletRequest request, @RequestParam("file") MultipartFile file,
            Boolean staticPage, Boolean approved,
            @RequestParam("postOn") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime postOn,
            @RequestParam("expireOn") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime expireOn) {
        Post post = pDao.readPostById(Integer.parseInt(request.getParameter("id")));

        post.setTitle(request.getParameter("title"));
        post.setBody(request.getParameter("body"));

        //approval status
        if (approved != null) {
            post.setApproved(true);
        } else {
            post.setApproved(false);
        }

        //static page status from check box
        if (staticPage != null) {
            post.setStaticPage(true);
        } else {
            post.setStaticPage(false);
        }

        //date time setting
        //leave created on as is
        post.setPostOn(postOn);
        post.setExpireOn(expireOn);

        //leave author as is
        //cover photo
        String filePath = iDao.saveImage(file, Long.toString(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)), coverUploadDir);
        post.setPhotoFilename(filePath);

        //set categories from multi select
        List<Category> categories = new ArrayList<>();
        String[] categoryIds = request.getParameterValues("categoryId");
        if (categoryIds != null) {
            for (String id : categoryIds) {
                categories.add(cDao.readCategoryById(Integer.parseInt(id)));
            }
            post.setCategories(categories);
        }

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(post);

        if (violations.isEmpty()) {
            pDao.updatePost(post);
        } else {
            model.addAttribute("post", post);
            model.addAttribute("categories", cDao.readAllCategories());
            model.addAttribute("errors", violations);
            String now = LocalDateTime.now().withSecond(0).withNano(0).toString();
            model.addAttribute("now", now);

            return "editPost"; //reload with errors
        }

        return "redirect:/postManagement";
    }

    /*DELETE - ADMIN ONLY*/
    /**
     * POST - load delete confirmation page for a post - admin only
     *
     * @param request {HttpServletRequest} pulls in id
     * @param model   {Model} holds post info
     * @return {String} redirect to the post management page
     */
    @GetMapping("/deletePost")
    public String deletePost(HttpServletRequest request, Model model) {
        model.addAttribute("staticPages", pDao.readAllStatic());
        Post post = pDao.readPostById(Integer.parseInt(request.getParameter("id")));

        model.addAttribute("post", post);

        return "deletePost";
    }

    /**
     * POST - Delete a post from site
     *
     * @param request {HttpServletRequest} pulls in the post id
     * @return {String} redirect to management page on delete
     */
    @PostMapping("/performDeletePost")
    public String performDeletePost(HttpServletRequest request) {
        pDao.deletePostById(Integer.parseInt(request.getParameter("id")));

        return "redirect:/postManagement";
    }

}
