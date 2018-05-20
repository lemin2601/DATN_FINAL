package com.leemin.genealogy.control.rest;

import com.leemin.genealogy.model.UserModel;
import com.leemin.genealogy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.security.Principal;

@Controller
public class UserRestController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/avatar", method = RequestMethod.GET)
    public ResponseEntity<String> getImage(
            Principal principal
          ){

        UserModel userModel = userRepository.findByEmail(principal.getName());
        if(userModel.getImg() == null || userModel.getImg().equals("")){
//            return  new ResponseEntity<>("/img/avatar-default-user.png", HttpStatus.OK);
            return  new ResponseEntity<>("/dist/img/user2-160x160.jpg", HttpStatus.OK);
        }else{
            return  new ResponseEntity<>(userModel.getImg(), HttpStatus.OK);
        }
    }
}
