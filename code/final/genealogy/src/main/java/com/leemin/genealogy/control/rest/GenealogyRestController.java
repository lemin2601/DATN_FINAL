package com.leemin.genealogy.control.rest;
import com.leemin.genealogy.model.GenealogyModel;
import com.leemin.genealogy.model.UserModel;
import com.leemin.genealogy.service.GenealogyService;
import com.leemin.genealogy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@RestController
public class GenealogyRestController {

    @Autowired
    GenealogyService genealogyService;

    @GetMapping(value = "/rest/genealogy/list" , produces = "application/json")
    public Collection<GenealogyModel> getAll(){
        List<GenealogyModel> all = genealogyService.findAll();
        Iterator<GenealogyModel> iterator = all.iterator();
        while(iterator.hasNext()){
            GenealogyModel next = iterator.next();
            String history = next.getHistory();
            next.setHistory(history.substring(0,10));
        }
        return all;
    }
}
