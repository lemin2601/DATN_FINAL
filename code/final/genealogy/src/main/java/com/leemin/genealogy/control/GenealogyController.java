package com.leemin.genealogy.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GenealogyController {

    @GetMapping("/genealogy")
    public String home(){
        return "/genealogy/home";
    }
    @GetMapping("/genealogy/create")
    public String create(){
        return "/genealogy/create";
    }
    @GetMapping("/genealogy/detail")
    public String detail(){
        return "/genealogy/detail";
    }

    @GetMapping("/genealogy/find")
    public String find(){
        return "/genealogy/find";
    }

    @GetMapping("/genealogy/member")
    public String memberManager(){
        return "/genealogy/member";
    }

    @GetMapping("/genealogy/people")
    public String peopleManager(){
        return "/genealogy/people";
    }



}
