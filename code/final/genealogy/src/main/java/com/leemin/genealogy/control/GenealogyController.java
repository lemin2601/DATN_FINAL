package com.leemin.genealogy.control;

import com.leemin.genealogy.model.GenealogyModel;
import com.leemin.genealogy.model.RoleModel;
import com.leemin.genealogy.model.UserModel;
import com.leemin.genealogy.service.GenealogyService;
import org.hibernate.annotations.Parameter;
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
public class GenealogyController {

    @Autowired
    GenealogyService genealogyService;


    @GetMapping("/genealogy")
    public String home(){
        return "/genealogy/home";
    }


    /*@RequestMapping(value = "/genealogy/detail", method = RequestMethod.GET)
    public ModelAndView detail(
            Principal principal,
            @RequestParam( value = "id",required = false,defaultValue = "-1") int id,
            @RequestParam( value = "edit",required = false,defaultValue = "false") boolean edit,
            @ModelAttribute(value = "user") UserModel user,
            HttpServletRequest request) {
        ModelAndView mv;
        if(principal == null){
            mv = new ModelAndView("/account/login");
        }else{
            mv = new ModelAndView("/genealogy/detail");
        }
        List<GenealogyModel> all = genealogyService.findAll();

        System.out.println(user.toString());
        System.out.println(principal.getName());


        System.out.println("Request detail id:" + id);
        List<RoleModel> ls = Arrays.asList(new RoleModel("EUA"), new RoleModel( "Brasil"), new RoleModel( "Italia"));
        mv.addObject("selecionado","Italia");
        mv.addObject("ls", ls);
        mv.addObject("id", id);
        return mv;
    }*/
    @RequestMapping(value = "/genealogy/detail", method = RequestMethod.GET)
    public ModelAndView detail(
            Principal principal,
            @ModelAttribute(value = "user") UserModel user,
            HttpServletRequest request) {
        ModelAndView mv;
        if(principal == null){
            mv = new ModelAndView("/account/login");
            return mv;
        }else{
            mv = new ModelAndView("/genealogy/home");
            return mv;
        }
//        principal.getName();
//        List<GenealogyModel>genealogyModels = genealogyService.findAllByUserName(principal.getName());
//        mv.addObject("listGenealogy", genealogyModels);

//        List<GenealogyModel> all = genealogyService.findAll();



//        System.out.println(user.toString());
//        System.out.println(principal.getName());
//
//
//        System.out.println("Request detail id:" + id);
//        List<RoleModel> ls = Arrays.asList(new RoleModel("EUA"), new RoleModel( "Brasil"), new RoleModel( "Italia"));
//        mv.addObject("selecionado","Italia");
//        mv.addObject("ls", ls);
//        mv.addObject("id", id);
//        return mv;
    }
    @RequestMapping(value = "/genealogy/{id}", method = RequestMethod.GET)
    public ModelAndView detail(
            Principal principal,
            @PathVariable( value = "id",required = false) int id,
            HttpServletRequest request) {
        ModelAndView mv;
        if (principal == null) {
            return new ModelAndView("/account/login");
        }
        else {
            mv = new ModelAndView("/genealogy/detail");
            GenealogyModel genealogyModel = genealogyService.find(id, principal.getName());
            mv.addObject("genealogy", genealogyModel);
            return mv;

        }
    }
    @RequestMapping(value = "/genealogy/{id}/{method}", method = RequestMethod.GET)
    public ModelAndView edit(
            Principal principal,
            @PathVariable( value = "id",required = false) int id,
            @PathVariable( value = "method",required = false) String method,
            HttpServletRequest request) {
        ModelAndView mv;
        if(principal == null){
            return new ModelAndView("/account/login");
        }else{
            if(method.equalsIgnoreCase("edit")){
                mv = new ModelAndView("/genealogy/detail-edit");
                GenealogyModel genealogyModel = genealogyService.find(id, principal.getName());
                mv.addObject("genealogy", genealogyModel);
                return mv;
            }else if(method.equalsIgnoreCase("pedigree")){
                mv = new ModelAndView("/genealogy/pedigree");
                mv.addObject("idGenealogy",id);
                return mv;
            }else if(method.equalsIgnoreCase("member")){
                mv = new ModelAndView("/genealogy/member");
                GenealogyModel genealogyModel = genealogyService.find(id, principal.getName());
                mv.addObject("genealogy", genealogyModel);
                return mv;
            }else{
                mv = new ModelAndView("/genealogy/detail");
                GenealogyModel genealogyModel = genealogyService.find(id, principal.getName());
                mv.addObject("genealogy", genealogyModel);
                return mv;
            }
        }
//        List<GenealogyModel> all = genealogyService.findAll();

//        System.out.println(user.toString());
//        System.out.println(principal.getName());
//
//
//        System.out.println("Request detail id:" + id);
//        List<RoleModel> ls = Arrays.asList(new RoleModel("EUA"), new RoleModel( "Brasil"), new RoleModel( "Italia"));
//        mv.addObject("selecionado","Italia");
//        mv.addObject("ls", ls);
//        mv.addObject("id", id);

    }
    @RequestMapping(value = "/genealogy/pedigree/tree", method = RequestMethod.GET)
    public ModelAndView viewTree(
            Principal principal,
            Authentication authentication,
            @RequestParam(value = "id", required = false, defaultValue = "10") int id,
            HttpServletRequest request) {
        ModelAndView mv;
        mv = new ModelAndView("/genealogy/tree");
        List<RoleModel> ls = Arrays.asList(new RoleModel("EUA"), new RoleModel( "Brasil"), new RoleModel( "Italia"));
        mv.addObject("selecionado","Italia");
        mv.addObject("ls", ls);
        return mv;
    }

    @RequestMapping(value = "/genealogy/pedigree/list", method = RequestMethod.GET)
    public ModelAndView viewList(
            Principal principal,
            Authentication authentication,
            @ModelAttribute(value = "user") UserModel user,
            HttpServletRequest request) {
        ModelAndView mv;
        mv = new ModelAndView("/genealogy/people");
        return mv;
    }


    @RequestMapping(value = "/genealogy/add", method = RequestMethod.GET)
    public ModelAndView viewList(
            Principal principal,
            HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("/genealogy/detail-add");

        GenealogyModel genealogy= new GenealogyModel();
        genealogy.setName("ABC");
        genealogy.setHistory("h");
        genealogy.setThuyTo("thh");
        mv.addObject("genealogy", genealogy);
        return mv;
    }

    @RequestMapping(value = "/genealogy/add", method = RequestMethod.POST)
    public ModelAndView createGenealogy(
            Principal principal,
            @Valid
            @ModelAttribute(value = "genealogy")
                    GenealogyModel genealogy,
            BindingResult bindingResult,
            HttpServletRequest request) {
        ModelAndView mv;
        GenealogyModel genealogyModel = genealogyService.create(genealogy, principal.getName());
        mv = new ModelAndView("/genealogy/detail");
        mv.addObject("genealogy",genealogyModel);
        return mv;
    }


    @RequestMapping(value = "/genealogy/edit", method = RequestMethod.GET)
    public ModelAndView viewList(
            Principal principal,
            @ModelAttribute(value = "user") UserModel user,
            HttpServletRequest request) {
        ModelAndView mv;
        mv = new ModelAndView("/genealogy/detail-edit");
//        mv.addObject("genealogy")
        return mv;
    }







    @GetMapping("/genealogy/create")
    public String create(){
        return "/genealogy/create";
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
