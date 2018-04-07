package com.leemin.genealogy.control.rest;

import com.leemin.genealogy.model.UserModel;
import com.leemin.genealogy.service.UserService;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Book;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
public class AdminRestController {


    @Autowired
    UserService userService;

    @GetMapping(value = "/admin/account/findAll")
    public Collection<UserModel> getALl(){
        return userService.findAll();
    }
    @GetMapping(value = "/admin/account/findAllBy", produces = "application/json")
    public Collection<UserModel> getAllBy(){
        return userService.findAllBy();
    }
    @PostMapping(value = "/admin/account/")
    public ResponseEntity<String> editAccount(UserModel userModel){
        UserModel userById = userService.findUserById(userModel.getId());
        BeanUtils.copyProperties(userModel,userById, getNullPropertyNames(userModel));
        System.out.println(userModel.toString());
        System.out.println(userById.toString());
        return ResponseEntity.status(HttpStatus.OK).build();

    }
    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper               src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
