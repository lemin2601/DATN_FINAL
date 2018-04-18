package com.leemin.genealogy.control;

import com.leemin.genealogy.model.UserModel;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String admin() {
        return "/admin/home";
    }

    @RequestMapping(value = "/admin/account", method = RequestMethod.GET)
    public ModelAndView login(Authentication authentication ,@ModelAttribute(value = "user") UserModel user, HttpServletRequest request) {
        ModelAndView mv;
        mv = new ModelAndView("/admin/account");
        authentication.getDetails();
        return mv;
    }
}
