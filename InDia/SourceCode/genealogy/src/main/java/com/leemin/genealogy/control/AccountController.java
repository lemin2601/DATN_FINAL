package com.leemin.genealogy.control;

import com.leemin.genealogy.config.ErrorKey;
import com.leemin.genealogy.data.Permission;
import com.leemin.genealogy.model.*;
import com.leemin.genealogy.service.SecurityService;
import com.leemin.genealogy.service.StorageService;
import com.leemin.genealogy.service.UserService;
import com.leemin.genealogy.util.ExcelImportUtil;
import com.leemin.genealogy.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

    @Autowired
    StorageService storageService;

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

    @GetMapping("/profile/edit")
    public ModelAndView editProfile(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("/account/profile-edit");
        UserModel userByEmail =userService.findUserByEmail(request.getUserPrincipal().getName());
        mv.addObject("user",userByEmail);
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


    @PostMapping(value = "/profile/edit")
    public ResponseEntity<?> editProfile(
            Principal principal,
            @RequestParam(value = "firstName", required = true, defaultValue = "") String  firstName,
            @RequestParam(value = "lastName", required = true, defaultValue = "") String lastName,
            @RequestParam(value = "name", required = true, defaultValue = "") String name,
            @RequestParam(value = "phone", required = true, defaultValue = "") String phone,
            @RequestParam(value = "address", required = true, defaultValue = "") String address,
            @RequestParam(value = "birthday", required = true, defaultValue = "") String birthday,
            @RequestParam("img") MultipartFile img
    ){

        UserModel userByEmail = userService.findUserByEmail(principal.getName());
        userByEmail.setFirstName(firstName);
        userByEmail.setLastName(lastName);
        userByEmail.setName(name);
        userByEmail.setPhone(phone);
        userByEmail.setAddress(address);
        userByEmail.setBirthday(ExcelImportUtil.getDate(birthday));

        String uploadedFileName = img.getOriginalFilename();
        if(!uploadedFileName.isEmpty()){
            uploadedFileName = +  userByEmail.getId() + "_" + System.currentTimeMillis() + uploadedFileName.substring(uploadedFileName.lastIndexOf("."));
            String fileImg = "img/"  + uploadedFileName;
            storageService.store(img,fileImg);
            userByEmail.setImg("/image/" + uploadedFileName);
        }
        userService.save(userByEmail);

        return new ResponseEntity<>("OK" , HttpStatus.OK);

//        return new ResponseEntity<>(errorCode , HttpStatus.OK);

    }

    @PostMapping(value = "/profile/changePass")
    public ResponseEntity<?> changePass(
            Principal principal,
            @RequestParam(value = "oldPass", required = true, defaultValue = "") String  oldPass,
            @RequestParam(value = "newPass", required = true, defaultValue = "") String newPass,
            @RequestParam(value = "renewPass", required = true, defaultValue = "") String renewPass
    ){
        if(newPass.equals(renewPass)){
            UserModel userByEmail = userService.findUserByEmail(principal.getName());
            if(bCryptPasswordEncoder.matches(oldPass,userByEmail.getPassword())){
                userByEmail.setPassword(bCryptPasswordEncoder.encode(newPass));
                userService.save(userByEmail);
                return new ResponseEntity<>("1" , HttpStatus.OK);
            }
            return new ResponseEntity<>("-1" , HttpStatus.OK);

        }
        return new ResponseEntity<>("-2" , HttpStatus.OK);
    }
}
