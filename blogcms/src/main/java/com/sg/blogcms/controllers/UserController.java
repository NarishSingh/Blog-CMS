package com.sg.blogcms.controllers;

import com.sg.blogcms.entity.Role;
import com.sg.blogcms.entity.User;
import com.sg.blogcms.model.ImageDao;
import com.sg.blogcms.model.RoleDao;
import com.sg.blogcms.model.UserDao;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UserController {

    @Autowired
    RoleDao rDao;
    @Autowired
    UserDao uDao;
    @Autowired
    ImageDao iDao;
    @Autowired
    PasswordEncoder encoder;
    private final String userUploadDir = "Users";
    Set<ConstraintViolation<User>> violations = new HashSet<>();

    /*MAIN*/
    /**
     * GET - load subdomain with all users
     *
     * @param model {Model} holds all users, regardless of role
     * @return {String} load subdomain
     */
    @GetMapping("/admin")
    public String displayAdminPage(Model model) {
        model.addAttribute("roles", rDao.readAllRoles());
        model.addAttribute("users", uDao.readAllUsers());
        model.addAttribute("errors", violations);

        return "admin";
    }

    /*CREATE*/
    /**
     * POST - create a new user account from form data
     *
     * @param request {HttpServletRequest} marshals form data for obj
     *                construction and validation
     * @param file    {MultipartFile} an image upload
     * @return {String} reload admin subdomain
     */
    @PostMapping("/addUser")
    public String addUser(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        String filePath = iDao.saveImage(file, Long.toString(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)), userUploadDir);

        User user = new User();
        String usernameString = request.getParameter("username");
        String passwordString = request.getParameter("password");

        if (usernameString.isBlank() || passwordString.isBlank()) {
            return "admin";
        } else {
            user.setUsername(usernameString);
            user.setPassword(encoder.encode(passwordString));
            user.setEnabled(true); //admin creates accounts, assume true
            user.setFirstName(request.getParameter("firstName"));
            user.setLastName(request.getParameter("lastName"));
            user.setEmail(request.getParameter("email"));
            user.setPhotoFilename(filePath);

            Set<Role> userRoles = new HashSet<>();
            userRoles.add(rDao.readRoleById(Integer.parseInt(request.getParameter("roleId"))));
            user.setRoles(userRoles);
        }

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(user);

        if (violations.isEmpty()) {
            uDao.createUser(user);
        }

        return "redirect:/admin";
    }

    /*EDIT USER*/
    /**
     * GET - Load up the edit page with info from user in db
     *
     * @param model   {Model} holds user obj and lists and errors for edit
     * @param request {HttpServletRequest} pulls in id
     * @param error   {Integer} errors coming in from a page reload
     * @return {String} load edit page
     */
    @GetMapping("/editUser")
    public String editUserDisplay(Model model, HttpServletRequest request, Integer error) {
        User user = uDao.readUserById(Integer.parseInt(request.getParameter("id")));
        List<Role> roles = rDao.readAllRoles();

        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        model.addAttribute("errors", violations);

        if (error != null) {
            if (error == 1) {
                model.addAttribute("passwordError", "Passwords did not match, password was not updated");
            }
        }

        return "editUser";
    }

    /**
     * POST - attempt to edit a user, sans password change
     *
     * @param request {HttpServletRequest} pull in form data for edit
     * @param file    {MultipartFile} pulls in image for profile picture
     * @param enabled {Boolean} param to set if an account is enabled/disabled
     * @param model   {Model} holds user obj's and role list
     * @return {String} redirect to admin page if edited, reload editUser page
     *         if error
     */
    @PostMapping(value = "/editUser")
    public String editUserAction(HttpServletRequest request, @RequestParam MultipartFile file,
            Boolean enabled, Model model) {
        User user = uDao.readUserById(Integer.parseInt(request.getParameter("id")));

        user.setUsername(request.getParameter("username"));
        //seperate method for password
        if (enabled != null) {
            user.setEnabled(true);
        } else {
            user.setEnabled(false);
        }
        user.setFirstName(request.getParameter("firstName"));
        user.setLastName(request.getParameter("lastName"));
        user.setEmail(request.getParameter("email"));
        user.setPhotoFilename(iDao.updateImage(file, user.getPhotoFilename(), userUploadDir));

        Set<Role> roles = new HashSet<>();
        roles.add(rDao.readRoleById(Integer.parseInt(request.getParameter("roleId"))));
        user.setRoles(roles);

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(user);

        if (violations.isEmpty()) {
            uDao.updateUser(user);
        } else {
            model.addAttribute("user", user);
            model.addAttribute("roles", rDao.readAllRoles());
            model.addAttribute("errors", violations);

            return "editUser"; //reload w errors
        }

        return "redirect:/admin";
    }

    /**
     * POST - attempt to edit a user's password
     *
     * @param request {HttpServletRequest} pulls in form data for edit
     * @return {String} redirect to admin page if updated, reload with errors if
     *         fail
     */
    @PostMapping("editPassword")
    public String editPassword(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        User user = uDao.readUserById(id);
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (!password.isBlank() && !confirmPassword.isBlank()
                && password.equals(confirmPassword)) {
            user.setPassword(encoder.encode(password));
            uDao.updateUser(user);

            return "redirect:/admin";
        } else {
            return "redirect:/editUser?id=" + id + "&error=1";
        }
    }

    /*DELETE*/
    /**
     * GET - load delete confirmation page for an account
     *
     * @param request {HttpServletRequest} will retrieve id from data table
     * @param model   {Model} hold the user obj
     * @return {String} load delete confirmation page
     */
    @GetMapping("/deleteUser")
    public String deleteUser(HttpServletRequest request, Model model) {
        User user = uDao.readUserById(Integer.parseInt(request.getParameter("id")));
        model.addAttribute("user", user);

        return "deleteUser";
    }

    /**
     * POST - delete a user account
     *
     * @param request {HttpServletRequest} will retrieve id from confirmation
     * @return {String} redirect the admin page
     */
    @PostMapping("/performDeleteUser")
    public String performDeleteUser(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        uDao.deleteUserById(id);

        return "redirect:/admin";
    }
}
