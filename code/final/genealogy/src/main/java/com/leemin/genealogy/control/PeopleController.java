package com.leemin.genealogy.control;

import com.leemin.genealogy.model.PeopleModel;
import com.leemin.genealogy.service.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Parameter;
import java.security.Principal;
import java.util.List;

@Controller
public class PeopleController {

    @Autowired
    PeopleService peopleService;


    @GetMapping("/genealogy/{idGenealogy}/pedigree/[idPedigree}/people")
    public ModelAndView home(
            Principal principal,
            @PathVariable(name = "idGenealogy") long idGenealogy,
            @PathVariable(name = "idPedigree") long idPedigree
                            ){
        ModelAndView mv;
        if(principal == null){
            return new ModelAndView("/account/login");
        }
        mv = new ModelAndView("/genealogy/people");
        List<PeopleModel> all = peopleService.findAll();
        mv.addObject("idGenealogy",idGenealogy);
        mv.addObject("idPedigree",idPedigree);
        return mv;
    }



}
