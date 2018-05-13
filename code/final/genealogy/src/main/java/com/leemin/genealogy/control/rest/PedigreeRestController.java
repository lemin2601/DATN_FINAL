package com.leemin.genealogy.control.rest;
import com.leemin.genealogy.config.ErrorKey;
import com.leemin.genealogy.data.Permission;
import com.leemin.genealogy.model.*;
import com.leemin.genealogy.repository.GenealogyPedigreeRepository;
import com.leemin.genealogy.repository.UserGenealogyRepository;
import com.leemin.genealogy.repository.UserRepository;
import com.leemin.genealogy.service.GenealogyService;
import com.leemin.genealogy.service.PedigreeService;
import com.leemin.genealogy.service.PeopleService;
import com.leemin.genealogy.util.ExcelImportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import java.awt.peer.PanelPeer;
import java.security.Principal;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@RestController
public class PedigreeRestController {

    @Autowired
    PedigreeService pedigreeService;

    @Autowired
    PeopleService peopleService;
    @Autowired
    UserGenealogyRepository userGenealogyRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GenealogyPedigreeRepository genealogyPedigreeRepository;

    @GetMapping(value = "/rest/pedigree/list/{idGenealogy}" , produces = "application/json")
    public Collection<PedigreeModel> getAll(
            Principal principal,
            @PathVariable(name = "idGenealogy")long idGenealogy){

        List<PedigreeModel> all = pedigreeService.findAll(idGenealogy);

        Iterator<PedigreeModel> iterator = all.iterator();
        while(iterator.hasNext()){
            PedigreeModel next = iterator.next();
            String history = next.getHistory();
            history = HtmlUtils.htmlEscape(history);
            next.setHistory(history.substring(0,history.length()>50?50:history.length()));
//            next.setHistory(history.substring(0,history.length()>50?50:history.length()));
        }
        return all;
    }


    @PostMapping(value = "/rest/pedigree/gop-pha-he")
    public ResponseEntity<?> addChildPeople(
            Principal principal,
            @RequestParam(value = "idGenealogy", required = true, defaultValue = "") long idGenealogy,
            @RequestParam(value = "inputSelectPedigree", required = true, defaultValue = "") long inputSelectPedigree,
            @RequestParam(value = "inputPedigreeTo", required = true, defaultValue = "") long inputPedigreeTo,
            @RequestParam(value = "inputParent", required = true, defaultValue = "") long inputParent,
            @RequestParam(value = "inputMother", required = true, defaultValue = "-1") long inputMother,
            @RequestParam(value = "inputIndexChild", required = true, defaultValue = "1") int inputIndexChild
                                           ){
        UserGenealogyModel userGenealogy = userGenealogyRepository.findTopByUserAndGenealogy_Id(userRepository.findByEmail(principal.getName()), idGenealogy);
        if(userGenealogy != null) {
            Permission permission = Permission.values()[(int) userGenealogy.getPermission().getId()];
            if(!(permission.equals(Permission.ADMIN))){
                return null;
            }
        }
        if(inputSelectPedigree == inputPedigreeTo) return new ResponseEntity<>(false, HttpStatus.OK);

        boolean b = peopleService.gopPhaHe(idGenealogy, inputSelectPedigree, inputPedigreeTo, inputParent, inputMother, inputIndexChild);
        if(b){
            return new ResponseEntity<>(true, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(false, HttpStatus.EXPECTATION_FAILED);
        }

    }

}
