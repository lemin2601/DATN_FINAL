package com.leemin.genealogy.control;

import com.leemin.genealogy.data.Permission;
import com.leemin.genealogy.model.GenealogyModel;
import com.leemin.genealogy.model.PeopleModel;
import com.leemin.genealogy.model.UserGenealogyModel;
import com.leemin.genealogy.repository.UserGenealogyRepository;
import com.leemin.genealogy.repository.UserRepository;
import com.leemin.genealogy.service.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Parameter;
import java.security.Principal;
import java.util.List;

@Controller
public class PeopleController {

    @Autowired
    PeopleService peopleService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserGenealogyRepository userGenealogyRepository;


    @RequestMapping(value = "/genealogy/{idGenealogy}/pedigree/{idPedigree}/people-list", method = RequestMethod.GET)
    public ModelAndView viewPeopleList(
            Principal principal,
            @PathVariable(name = "idGenealogy") long idGenealogy,
            @PathVariable(name = "idPedigree") long idPedigree
                            ){
        String a = "" ;
        String v = "";
        a = a + v;
        return getModelAndView(principal, idGenealogy, idPedigree, "/genealogy/pedigree-list-people");
    }

    @GetMapping("/genealogy/{idGenealogy}/pedigree/{idPedigree}/people-tree")
    public ModelAndView viewAdd(
            Principal principal,
            @PathVariable(name = "idGenealogy") long idGenealogy,
            @PathVariable(name = "idPedigree") long idPedigree
                            ){
        ModelAndView mv;
        UserGenealogyModel userGenealogy = userGenealogyRepository.findTopByUserAndGenealogy_Id(userRepository.findByEmail(principal.getName()), idGenealogy);
        if(userGenealogy != null) {
            Permission permission = Permission.values()[(int) userGenealogy.getPermission().getId()];
            if(permission.equals(Permission.ADMIN) || permission.equals(Permission.MOD) || permission.equals(Permission.VIEW)){
                GenealogyModel genealogyModel = userGenealogy.getGenealogy();
                mv = new ModelAndView("/genealogy/pedigree-tree-people");
                mv.addObject("idPermission", userGenealogy.getPermission().getId());
                mv.addObject("idGenealogy",idGenealogy);
                mv.addObject("idPedigree",idPedigree);
                return mv;
            }
        }
        mv = new ModelAndView();
        mv.setViewName("redirect:/genealogy");
        return mv;
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
