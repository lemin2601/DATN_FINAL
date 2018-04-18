package com.leemin.genealogy.control.rest;
import com.leemin.genealogy.model.GenealogyModel;
import com.leemin.genealogy.model.PedigreeModel;
import com.leemin.genealogy.service.GenealogyService;
import com.leemin.genealogy.service.PedigreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@RestController
public class PedigreeRestController {

    @Autowired
    PedigreeService pedigreeService;

    @GetMapping(value = "/rest/pedigree/list/{idGenealogy}" , produces = "application/json")
    public Collection<PedigreeModel> getAll(
            Principal principal,
            @PathVariable(name = "idGenealogy")long idGenealogy){

        List<PedigreeModel> all = pedigreeService.findAll(idGenealogy);

        Iterator<PedigreeModel> iterator = all.iterator();
        while(iterator.hasNext()){
            PedigreeModel next = iterator.next();
            String history = next.getHistory();
            next.setHistory(history.substring(0,history.length()>50?50:history.length()));
        }
        return all;
    }
}
