package com.leemin.genealogy.control;

import com.leemin.genealogy.model.UserModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Controller
public class DefaultController {

/*    @GetMapping(value = "/")
    public String home(@ModelAttribute(value="user") UserModel user){
        return "/home";
    }*/
    @GetMapping("/")
    public String dashboard( Authentication authentication, @ModelAttribute(value="user") UserModel user) {
        String result = "/account/login";
        if(authentication != null){
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority()
                             .equals("ROLE_ADMIN")) {
                    result = "/admin/home";
                    break;
                }
                else if (authority.getAuthority()
                                  .startsWith("ROLE_USER")) {
                    result = "/genealogy/home";
                    break;
                }
            }
        }

//        while(itr.hasNext()) {
//            GrantedAuthority authority = (GrantedAuthority) itr.next();
//            if(authority.getAuthority().equals("ROLE_ADMIN")){
//                result = "/admin";
//                break;
//            }else if(authority.getAuthority().equals("ROLE_USER")){
//                result = "/genealogy";
//                break;
//            }
//        }
        return  result;
    }

    @GetMapping("/about")
    public String about() {
        return "/about";
    }



}