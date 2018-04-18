package com.leemin.genealogy.control.rest;

import com.leemin.genealogy.model.PedigreeModel;
import com.leemin.genealogy.model.PeopleModel;
import com.leemin.genealogy.service.PedigreeService;
import com.leemin.genealogy.service.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@RestController
public class PeopleRestController {

    @Autowired
    PeopleService peopleService;

    @GetMapping(value = "/rest/genealogy/{idGenealogy}/pedigree/{idPedigree}/list" , produces = "application/json")
    public Collection<PeopleModel> getAll(
            Principal principal,
            @PathVariable(name = "idGenealogy")long idGenealogy,
            @PathVariable(name = "idPedigree")long idPedigree
            ){

        List<PeopleModel> all = peopleService.findAll();

//        Iterator<PedigreeModel> iterator = all.iterator();
//        while(iterator.hasNext()){
//            PedigreeModel next = iterator.next();
//            String history = next.getHistory();
//            next.setHistory(history.substring(0,history.length()>50?50:history.length()));
//        }
        return all;
    }
}
