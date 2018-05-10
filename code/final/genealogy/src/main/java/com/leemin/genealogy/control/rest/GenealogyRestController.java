package com.leemin.genealogy.control.rest;
import com.leemin.genealogy.model.*;
import com.leemin.genealogy.repository.UserGenealogyRepository;
import com.leemin.genealogy.repository.UserRepository;
import com.leemin.genealogy.service.GenealogyService;
import com.leemin.genealogy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@RestController
public class GenealogyRestController {

    @Autowired
    GenealogyService genealogyService;

    @Autowired
    UserGenealogyRepository userGenealogyRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping(value = "/rest/genealogy/list", produces = "application/json")
    public Collection<FindGenealogy> getAll(Principal principal) {
        UserModel  userModel = userRepository.findByEmail(principal.getName());
        List<GenealogyModel> all = genealogyService.findAllByUserName(principal.getName());
        List<FindGenealogy> result = new ArrayList<>();
        for (GenealogyModel gene : all) {
            FindGenealogy item = new FindGenealogy();
            item.setId(gene.getId());
            item.setName(gene.getName());
            String his = gene.getHistory();
            int length = his.length();
            if(length>50) length = 50;
            his = his.substring(0,length);
            item.setHistory(HtmlUtils.htmlEscape(his));
            UserGenealogyModel byUserAndGenealogy_id = userGenealogyRepository.findTopByUserAndGenealogy_Id(userModel, gene.getId());
            if(byUserAndGenealogy_id != null){
                item.setStatus((int) byUserAndGenealogy_id.getPermission().getId());
            }
            result.add(item);
        }
        return result;
    }

    @PostMapping(value = "/rest/genealogy/find", produces = "application/json")
    public Collection<FindGenealogy> findAll(
            @RequestParam(value = "textSearch", required = false, defaultValue = "")
                    String search,
            Principal principal
                                             ) {

        UserModel  userModel = userRepository.findByEmail(principal.getName());
        List<GenealogyModel> all = genealogyService.findAllLike("%" + search + "%");
        List<FindGenealogy> result = new ArrayList<>();
        for (GenealogyModel gene : all) {
            FindGenealogy item = new FindGenealogy();
            item.setId(gene.getId());
            item.setName(gene.getName());
            String his = gene.getHistory();
            int length = his.length();
            if(length>50) length = 50;
            his = his.substring(0,length);
            item.setHistory(HtmlUtils.htmlEscape(his));
            UserGenealogyModel byUserAndGenealogy_id = userGenealogyRepository.findTopByUserAndGenealogy_Id(userModel, gene.getId());
            if(byUserAndGenealogy_id != null){
                item.setStatus((int) byUserAndGenealogy_id.getPermission().getId());
            }
            result.add(item);
        }
        return result;
    }

    @PostMapping(value = "/rest/genealogy/register", produces = "application/json")
    public long registerGenealogy(
            @RequestParam(value = "idGenealogy", required = false, defaultValue = "")
                    long idGenealogy,
            Principal principal) {

        UserModel  userModel = userRepository.findByEmail(principal.getName());
        GenealogyModel genealogyModel = genealogyService.findById(idGenealogy);
        PermissionModel permissionModel = new PermissionModel();
        permissionModel.setId(3);
        permissionModel.setName("REGISTER");
        UserGenealogyModel userGenealogyModel = new UserGenealogyModel();
        userGenealogyModel.setUser(userModel);
        userGenealogyModel.setGenealogy(genealogyModel);
        userGenealogyModel.setPermission(permissionModel);
        UserGenealogyModel save = userGenealogyRepository.save(userGenealogyModel);
        return 1;
    }
    @PostMapping(value = "/rest/genealogy/unregister", produces = "application/json")
    public long unRegisterGenealogy(
            @RequestParam(value = "idGenealogy",required = false, defaultValue = "")
                    long idGenealogy,
            Principal principal) {

        UserModel  userModel = userRepository.findByEmail(principal.getName());
        List<UserGenealogyModel> byUserAndGenealogy_id = userGenealogyRepository.findByUserAndGenealogy_Id(userModel, idGenealogy);
        userGenealogyRepository.delete(byUserAndGenealogy_id);
        return 1;
    }


    @GetMapping(value = "/rest/genealogy/{idGenealogy}/member", produces = "application/json")
    public Collection<RestMember> findAllByGenealogyId(
            @PathVariable(value = "idGenealogy") long idGenealogy,
            @RequestParam(value = "textSearch", required = false, defaultValue = "")
                    String search,
            Principal principal) {
        UserModel byEmail = userRepository.findByEmail(principal.getName());
        List<UserGenealogyModel> byUserAndGenealogy_id1 = userGenealogyRepository.findByGenealogy_Id(idGenealogy);
        List<RestMember> result = new ArrayList<>();

        for (UserGenealogyModel model: byUserAndGenealogy_id1) {
            RestMember item = new RestMember();
            item.setId(model.getId());
            item.setIdUser(model.getUser().getId());
            item.setName(model.getUser().getName());
            item.setEmail(model.getUser().getEmail());
            item.setStatus((int) model.getPermission().getId());
            result.add(item);
        }
        return result;
    }


    @PostMapping(value = "/rest/genealogy/member/update", produces = "application/json")
    public long updateMemberGenealogy(
            @RequestParam(value = "idPermission", required = true, defaultValue = "") long idPermission,
            @RequestParam(value = "idUserGenealogy", required = true, defaultValue = "") long idUserGenealogy,
            Principal principal) {
        PermissionModel permissionModel = new PermissionModel();
        switch ((int) idPermission){
            case 0:
                userGenealogyRepository.delete(idUserGenealogy);
                return 1;
            case 1:
                permissionModel.setId(1);
                permissionModel.setName("ADMIN");
                break;
            case 2:
                permissionModel.setId(2);
                permissionModel.setName("MOD");
                break;
            case 3:
                permissionModel.setId(3);
                permissionModel.setName("REGISTER");
                break;
            case 4:
                permissionModel.setId(4);
                permissionModel.setName("VIEW");
                break;
        }

        UserGenealogyModel one = userGenealogyRepository.findOne(idUserGenealogy);
        one.setPermission(permissionModel);
        userGenealogyRepository.save(one);
        return 1;
    }


}
