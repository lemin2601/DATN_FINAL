package com.leemin.genealogy.control.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.leemin.genealogy.model.UserModel;
import com.leemin.genealogy.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@RestController
public class AddressRestController {

    private static int DEFAULT_CODE_THANH_PHO = 48;
    private static int DEFAULT_CODE_QUAN_HUYEN = 490;
    private static int DEFAULT_CODE_PHUONG_XA = 20198;
    @GetMapping(value = "/rest/address/thanh_pho")
    public Collection<UserModel> getThanhPho(){
        File file = new File(getClass().getResource("jsonschema.json").getFile());
//        JsonNode mySchema = JsonLoader.fromFile(file);
        return  null;
    }
    @GetMapping(value = "/rest/address/quan_huỵen/{code}")
    public Collection<UserModel> getQuanHuyen(
            @PathVariable(name ="code",required = false) int code
                                             ){

            return null;
    }
    @GetMapping(value = "/rest/address/phuong_xa/{code}")
    public Collection<UserModel> getPhuongXa(){
        return null;
    }
}
