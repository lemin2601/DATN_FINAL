package com.leemin.genealogy.control;

import com.leemin.genealogy.data.Permission;
import com.leemin.genealogy.model.GenealogyModel;
import com.leemin.genealogy.model.RoleModel;
import com.leemin.genealogy.model.UserGenealogyModel;
import com.leemin.genealogy.model.UserModel;
import com.leemin.genealogy.repository.UserGenealogyRepository;
import com.leemin.genealogy.repository.UserRepository;
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

    @Autowired
    UserGenealogyRepository userGenealogyRepository;

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/genealogy", method = RequestMethod.GET)
    public ModelAndView home(Principal principal) {
        ModelAndView mv;
        if (principal == null) {
            return new ModelAndView("/account/login");
        }
        else {
            UserModel userModel = userRepository.findByEmail(principal.getName());
            return new ModelAndView("/genealogy/home");
            /*List<UserGenealogyModel> byUser = userGenealogyRepository.findByUser(userModel);
            boolean check = false;
            if(byUser == null || byUser.size() == 0){
                check = false;
            }else{
                check = true;
    //
    //                for (UserGenealogyModel model: byUser) {
    //                    if(model.getPermission().getId() != 3){
    //                        check = true;
    //                    }
    //                }
            }


            if(!check){
                mv = new ModelAndView("/genealogy/find");
            }else{
                mv = new ModelAndView("/genealogy/home");
            }
            return mv;*/
        }
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
            @ModelAttribute(value = "user")
                    UserModel user,
            HttpServletRequest request) {
        ModelAndView mv;
        if (principal == null) {
            mv = new ModelAndView("/account/login");
            return mv;
        }
        else {
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
            @PathVariable(value = "id", required = false)
                    int id,
            HttpServletRequest request) {
        ModelAndView mv;
        if (principal == null) {
            return new ModelAndView("/account/login");
        }
        else {
            UserGenealogyModel userGenealogy = userGenealogyRepository.findTopByUserAndGenealogy_Id(userRepository.findByEmail(principal.getName()), id);
            if(userGenealogy != null) {
                Permission permission = Permission.values()[(int) userGenealogy.getPermission().getId()];
                if(permission.equals(Permission.ADMIN) || permission.equals(Permission.MOD) || permission.equals(Permission.VIEW)){
                    mv = new ModelAndView("/genealogy/detail");
                    mv.addObject("idPermission", permission.ordinal());
                    mv.addObject("genealogy", userGenealogy.getGenealogy());
                    return mv;
                }
            }

            mv = new ModelAndView();
            mv.setViewName("redirect:/genealogy");

//            mv = new ModelAndView("/genealogy/detail");
//            GenealogyModel genealogyModel = genealogyService.find(id, principal.getName());
//            mv.addObject("genealogy", genealogyModel);
            return mv;

        }
    }

    @RequestMapping(value = "/genealogy/{id}/{method}", method = RequestMethod.GET)
    public ModelAndView edit(
            Principal principal,
            @PathVariable(value = "id", required = false)
                    int id,
            @PathVariable(value = "method", required = false)
                    String method,
            HttpServletRequest request) {
        ModelAndView mv;
        if (principal == null) {
            return new ModelAndView("/account/login");
        }
        else {
            if (method.equalsIgnoreCase("edit")) {
                UserGenealogyModel userGenealogy = userGenealogyRepository.findTopByUserAndGenealogy_Id(userRepository.findByEmail(principal.getName()), id);
                if(userGenealogy != null) {
                    Permission permission = Permission.values()[(int) userGenealogy.getPermission().getId()];
                    if(permission.equals(Permission.ADMIN) || permission.equals(Permission.MOD)){
                        mv = new ModelAndView("/genealogy/detail-edit");
                        mv.addObject("genealogy", userGenealogy.getGenealogy());
                        return mv;
                    }
                }
            }
            else if (method.equalsIgnoreCase("pedigree")) {
                mv = new ModelAndView("/genealogy/pedigree");
                UserGenealogyModel userGenealogy = userGenealogyRepository.findTopByUserAndGenealogy_Id(userRepository.findByEmail(principal.getName()), id);
                if(userGenealogy != null) {
                    Permission permission = Permission.values()[(int) userGenealogy.getPermission().getId()];
                    if(permission.equals(Permission.ADMIN) || permission.equals(Permission.MOD) || permission.equals(Permission.VIEW)){
                        GenealogyModel genealogyModel = userGenealogy.getGenealogy();
                        mv.addObject("genealogy", genealogyModel);
                        mv.addObject("idPermission", userGenealogy.getPermission().getId());
                        mv.addObject("idGenealogy", id);
                        return mv;
                    }
                }else{
                    mv.setViewName("redirect:/genealogy");
                }

                return mv;
            }
            else if (method.equalsIgnoreCase("member")) {
                mv = new ModelAndView("/genealogy/member");
//                GenealogyModel genealogyModel = genealogyService.find(id, principal.getName());
                UserGenealogyModel byUserAndGenealogy_id = userGenealogyRepository.findTopByUserAndGenealogy_Id(userRepository.findByEmail(principal.getName()), id);
                UserModel byEmail = userRepository.findByEmail(principal.getName());
                GenealogyModel genealogyModel = byUserAndGenealogy_id.getGenealogy();
                mv.addObject("genealogy", genealogyModel);
                mv.addObject("idUser", byEmail.getId());
                mv.addObject("idGenealogy", genealogyModel.getId());
                mv.addObject("idPermission", byUserAndGenealogy_id.getPermission().getId());
                return mv;
            }
            else {
                mv = new ModelAndView("/genealogy/detail");
                GenealogyModel genealogyModel = genealogyService.find(id, principal.getName());
                mv.addObject("genealogy", genealogyModel);
                return mv;
            }
        }
        mv = new ModelAndView("/genealogy/detail");
        GenealogyModel genealogyModel = genealogyService.find(id, principal.getName());
        mv.addObject("genealogy", genealogyModel);
        return mv;
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
            @RequestParam(value = "id", required = false, defaultValue = "10")
                    int id,
            HttpServletRequest request) {
        ModelAndView mv;
        mv = new ModelAndView("/genealogy/tree");
        List<RoleModel> ls = Arrays.asList(new RoleModel("EUA"), new RoleModel("Brasil"), new RoleModel("Italia"));
        mv.addObject("selecionado", "Italia");
        mv.addObject("ls", ls);
        return mv;
    }

    @RequestMapping(value = "/genealogy/pedigree/list", method = RequestMethod.GET)
    public ModelAndView viewList(
            Principal principal,
            Authentication authentication,
            @ModelAttribute(value = "user")
                    UserModel user,
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
        GenealogyModel genealogy = new GenealogyModel();
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
        mv.addObject("genealogy", genealogyModel);
        return mv;
    }
    @RequestMapping(value = "/genealogy/{idGenealogy}/edit", method = RequestMethod.POST)
    public ModelAndView createGenealogy(
            Principal principal,
            @PathVariable(name = "idGenealogy")
                    long idGenealogy,
            @Valid
            @ModelAttribute(value = "genealogy")
                    GenealogyModel genealogy,
            BindingResult bindingResult,
            HttpServletRequest request) {
        ModelAndView mv;
        UserGenealogyModel userGenealogy = userGenealogyRepository.findTopByUserAndGenealogy_Id(userRepository.findByEmail(principal.getName()), idGenealogy);
        if(userGenealogy != null) {
            Permission permission = Permission.values()[(int) userGenealogy.getPermission().getId()];
            if(permission.equals(Permission.ADMIN) || permission.equals(Permission.MOD)){
                genealogy.setId(idGenealogy);
                genealogy = genealogyService.update(genealogy);
            }
        }
        mv = new ModelAndView("/genealogy/detail");
        mv.addObject("genealogy", genealogy);
        return mv;
    }
    @RequestMapping(value = "/genealogy/find", method = RequestMethod.GET)
    public ModelAndView findGenealogy(
            Principal principal,
            HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("/genealogy/find");
        return mv;
    }

    @RequestMapping(value = "/genealogy/{idGenealogy}/register", method = RequestMethod.POST)
    public ModelAndView editGenealogy(
            Principal principal,
            @PathVariable(name = "idGenealogy")
                    long idGenealogy,
            @Valid
            @ModelAttribute(value = "genealogy")
                    GenealogyModel genealogy,
            BindingResult bindingResult,
            HttpServletRequest request) {
        ModelAndView mv;
        genealogy.setId(idGenealogy);
        GenealogyModel genealogyModel = genealogyService.update(genealogy);
        mv = new ModelAndView("/genealogy/detail");
        mv.addObject("genealogy", genealogyModel);
        return mv;
    }

}
