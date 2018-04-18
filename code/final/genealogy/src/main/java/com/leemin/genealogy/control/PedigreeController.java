package com.leemin.genealogy.control;

import com.leemin.genealogy.model.GenealogyModel;
import com.leemin.genealogy.model.PedigreeModel;
import com.leemin.genealogy.model.RoleModel;
import com.leemin.genealogy.model.UserModel;
import com.leemin.genealogy.service.GenealogyService;
import com.leemin.genealogy.service.PedigreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
public class PedigreeController {

    @Autowired
    PedigreeService pedigreeService;

    @Autowired
    GenealogyService genealogyService;

    @GetMapping("/pedigree/{idGenealogy}")
    public ModelAndView home(@PathVariable(name = "idGenealogy")long idGenealogy){
        ModelAndView mv = new ModelAndView("/genealogy/pedigree");
        mv.addObject("idGenealogy",idGenealogy);
        return mv;
    }


    @RequestMapping(value = "/pedigree/{idGenealogy}/add", method = RequestMethod.GET)
    public ModelAndView viewList(
            Principal principal,
            @PathVariable(name = "idGenealogy")Long idGenealogy) {
        ModelAndView mv = new ModelAndView("/genealogy/pedigree-add");
        PedigreeModel pedigreeModel = new PedigreeModel();

        mv.addObject("pedigree",pedigreeModel);
        mv.addObject("idGenealogy",idGenealogy);
        return mv;
    }

    @RequestMapping(value = "/pedigree/{idGenealogy}/add", method = RequestMethod.POST)
    public ModelAndView createGenealogy(
            Principal principal,
            @Valid
            @ModelAttribute(value = "pedigree")
                    PedigreeModel pedigreeModel,
            @PathVariable(name = "idGenealogy")
                    long idGenealogy,
            BindingResult bindingResult,
            HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("/genealogy/pedigree-detail");
        GenealogyModel genealogyModel = genealogyService.find(idGenealogy, principal.getName());
        PedigreeModel add = pedigreeService.add(pedigreeModel, idGenealogy, principal.getName());

        mv.addObject("genealogy",genealogyModel);
        mv.addObject("pedigree",add);
        return mv;
    }


}
