package com.leemin.genealogy.control.rest;

import com.leemin.genealogy.data.UserRest;
import com.leemin.genealogy.model.GenealogyModel;
import com.leemin.genealogy.model.UserGenealogyModel;
import com.leemin.genealogy.model.UserModel;
import com.leemin.genealogy.service.GenealogyService;
import com.leemin.genealogy.service.UserService;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import java.awt.print.Book;
import java.util.*;

@RestController
public class AdminRestController {


    @Autowired
    UserService userService;

    @Autowired
    GenealogyService genealogyService;

    @GetMapping(value = "/admin/account/findAll")
    public Collection<UserModel> getALl() {

        return userService.findAll();
    }

    @GetMapping(value = "/admin/account/findAllBy", produces = "application/json")
    public Collection<UserModel> getAllBy() {

        return userService.findAllBy();
    }

    @PostMapping(value = "/admin/account/")
    public ResponseEntity<String> editAccount(UserModel userModel) {

        UserModel userById = userService.findUserById(userModel.getId());
        BeanUtils.copyProperties(userModel, userById, getNullPropertyNames(userModel));
        System.out.println(userModel.toString());
        System.out.println(userById.toString());
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    public static String[] getNullPropertyNames(Object source) {

        final BeanWrapper               src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }


    @GetMapping(value = "/rest/admin/account/list")
    public ResponseEntity<?> getAllAccount(Authentication authentication) {

        if (!isHavePermission(authentication)) return notHavePermisstion();


        List<UserModel> all    = userService.findAll();
        List<UserRest>  result = new ArrayList<>();
        for (UserModel u : all) {
            UserRest item = new UserRest();
            item.setId(u.getId());
            item.setName(u.getName());
            item.setEmail(u.getEmail());
            result.add(item);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @GetMapping(value = "/rest/admin/genealogy/list")
    public ResponseEntity<?> getAllGenealogy(Authentication authentication) {

        if (!isHavePermission(authentication)) return notHavePermisstion();

        List<GenealogyModel> all    = genealogyService.findAll();
        List<FindGenealogy>  result = new ArrayList<>();
        for (GenealogyModel gene : all) {
            FindGenealogy item = new FindGenealogy();
            item.setId(gene.getId());
            item.setName(gene.getName());
            String his    = gene.getHistory();
            int    length = his.length();
            if (length > 50) length = 50;
            his = his.substring(0, length);
            item.setHistory(HtmlUtils.htmlEscape(his));
            item.setStatus((int) 1);
            result.add(item);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);

    }


    @PostMapping(value = "/rest/admin/account/delete/{id}")
    public ResponseEntity<?> getAllAccount(Authentication authentication, @PathVariable(name = "id") long id) {

        if (!isHavePermission(authentication)) return notHavePermisstion();
        userService.delete(id);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }


    private boolean isHavePermission(Authentication authentication) {

        if (authentication != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority()
                        .equals("ROLE_ADMIN")) {
                    return true;
                } else if (authority.getAuthority()
                        .startsWith("ROLE_USER")) {
                    return false;
                }
            }
        }
        return false;
    }

    private ResponseEntity<?> notHavePermisstion() {
//        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
