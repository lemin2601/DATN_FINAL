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
    public ModelAndView viewHome(
            Principal principal,
            @PathVariable(name = "idGenealogy") long idGenealogy,
            @PathVariable(name = "idPedigree") long idPedigree
                            ){
        return getModelAndView(principal, idGenealogy, idPedigree, "/genealogy/people");
    }


    @GetMapping("/genealogy/{idGenealogy}/pedigree/[idPedigree}/people/add")
    public ModelAndView viewAdd(
            Principal principal,
            @PathVariable(name = "idGenealogy") long idGenealogy,
            @PathVariable(name = "idPedigree") long idPedigree
                            ){
        return getModelAndView(principal, idGenealogy, idPedigree, "/genealogy/people-add");
    }

    private ModelAndView getModelAndView(Principal principal,
            @PathVariable(name = "idGenealogy")
                    long idGenealogy,
            @PathVariable(name = "idPedigree")
                    long idPedigree, String s) {
        ModelAndView mv;
        if(principal == null){
            return new ModelAndView("/account/login");
        }
        mv = new ModelAndView(s);
        mv.addObject("idGenealogy",idGenealogy);
        mv.addObject("idPedigree",idPedigree);
        return mv;
    }


}
