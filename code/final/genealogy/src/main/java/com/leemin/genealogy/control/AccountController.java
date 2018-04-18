package com.leemin.genealogy.control;

import com.leemin.genealogy.model.RoleModel;
import com.leemin.genealogy.model.UserModel;
import com.leemin.genealogy.service.SecurityService;
import com.leemin.genealogy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.Authenticator;
import java.security.Principal;
import java.util.Iterator;

@Controller
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/confirm")
    public String confirm() {
        return "/account/confirm";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(
            @ModelAttribute(value = "user")
                    UserModel user, HttpServletRequest request) {
        System.out.println(user.toString());
        ModelAndView mv;
        mv = new ModelAndView("/account/login");
//        String    userName  = "not logged in"; // Any default user  name
//        Principal principal = request.getUserPrincipal();
//        if (principal != null) {
//            userName = principal.getName();
//        }
//        if (request.isUserInRole("ROLE_ADMIN")) {
//            mv = new ModelAndView("forward:/admin");
//        }
        return mv;
    }

    @GetMapping("/profile")
    public ModelAndView profile(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("/account/profile");
        UserModel userByEmail =userService.findUserByEmail(request.getUserPrincipal().getName());
        mv.addObject("user",userByEmail);
//        if (request.isUserInRole("ROLE_ADMIN")) {
//            return "redirect:/admin";
//        }
        return mv;
    }

    //region Register
    @GetMapping("/register")
    public String register() {
        return "/account/register";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView registration() {
        ModelAndView modelAndView = new ModelAndView();
        UserModel    user         = new UserModel();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("/account/register");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(
            @Valid
            @ModelAttribute(value = "user")
                    UserModel user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        UserModel    userExists   = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult.rejectValue("email", "register.error.exist.email");
        }
        if (bindingResult.hasErrors()) {
//            modelAndView.addObject("user", user);
            modelAndView.setViewName("/account/register");
        }
        else {
            userService.saveUser(user);

            securityService.autoLogin(user.getEmail(), bCryptPasswordEncoder.encode(user.getPassword()));


            modelAndView.addObject("success", "");
            modelAndView.addObject("user", user);
            ///account/register
            modelAndView.setViewName("redirect:/genealogy");

        }
        return modelAndView;
    }
    //endregion

}
