package com.leemin.genealogy.control.rest;

import com.fasterxml.jackson.annotation.JsonView;
import com.leemin.genealogy.config.tree.ChartConfig;
import com.leemin.genealogy.config.tree.Child;
import com.leemin.genealogy.config.tree.Config;
import com.leemin.genealogy.config.tree.ConfigTree;
import com.leemin.genealogy.config.tree.Text;
import com.leemin.genealogy.model.PedigreeModel;
import com.leemin.genealogy.model.PeopleModel;
import com.leemin.genealogy.service.PedigreeService;
import com.leemin.genealogy.service.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.*;

@RestController
public class PeopleRestController {

    @Autowired
    PedigreeService  pedigreeService;

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


    //"https://"+ document.location.host  +'/rest/genealogy/'+idGenealogy+'/pedigree/' + idPedigree+'list-people',
    @GetMapping(value = "/rest/genealogy/{idGenealogy}/pedigree/{idPedigree}/list-people" , produces = "application/json")
    public Collection<PeopleModel> getAllListPeople(
            Principal principal,
            @PathVariable(name = "idGenealogy")long idGenealogy,
            @PathVariable(name = "idPedigree")long idPedigree
                                                     ){

        //TODO check condition pedigree
        PedigreeModel byIdPedigreeModel = pedigreeService.findByIdPedigreeModel(idPedigree);
        List<PeopleModel> r = peopleService.findAllByPedigreeAndParentKeyStartsWith(byIdPedigreeModel, "r");
        Iterator<PeopleModel> iterator = r.iterator();
//        while(iterator.hasNext()){
//            PeopleModel = iterator.next();
//            String history = next.getHistory();
//            next.setHistory(history.substring(0,history.length()>50?50:history.length()));
//        }
        return r;
    }

    /*@GetMapping(value = "/rest/genealogy/{idGenealogy}/pedigree/{idPedigree}/list-people-tree" , produces = "application/json")
    public List<ConfigTree>  getAllListPeopleViewTree(
            Principal principal,
            @PathVariable(name = "idGenealogy")long idGenealogy,
            @PathVariable(name = "idPedigree")long idPedigree
                                                   ){
        //TODO check condition pedigree
        PedigreeModel pedigree = pedigreeService.findByIdPedigreeModel(idPedigree);
//        ChartConfig chartConfig = new ChartConfig();
//        chartConfig = getChartConfig(chartConfig,byIdPedigreeModel,"r");
//        return chartConfig;

        List<ConfigTree> configTrees = new ArrayList<>();
        List<PeopleModel> r = peopleService.findAllByPedigreeAndParentKeyStartsWith(pedigree, "r");
        r.sort((o1, o2) -> (o1.getParentKey().compareToIgnoreCase(o2.getParentKey())));
        for (PeopleModel peopleModel : r) {
            Child child = new Child();
            child.setImage(peopleModel.getImg());
            child.setHTMLid(peopleModel.getId() + "");
            PeopleModel parent = peopleModel.getParent();
            if (parent != null) {
                child.setParent(getParrentFromList(configTrees));
            }
            Text text = new Text();
            text.setName(peopleModel.getName());
            text.setTitle(peopleModel.getLifeIndex() +"");
            child.setText(text);
            configTrees.add(child);
        }
        configTrees.add(new Config());
        return configTrees;
    }

    private Child getParrentFromList(List<ConfigTree> r) {
        Iterator<ConfigTree> iterator = r.iterator();
        while(iterator.hasNext()){
            ConfigTree child = iterator.next();
            if(child.getClass().equals(Child.class)){
                return (Child) child;
            }
        }
        return null;
    }*/

    @GetMapping(value = "/rest/genealogy/{idGenealogy}/pedigree/{idPedigree}/list-people-tree" , produces = "application/json")
    public ChartConfig getAllListPeopleViewTree(
            Principal principal,
            @PathVariable(name = "idGenealogy")long idGenealogy,
            @PathVariable(name = "idPedigree")long idPedigree
                                                   ){
        //TODO check condition pedigree
        PedigreeModel pedigree = pedigreeService.findByIdPedigreeModel(idPedigree);
        List<PeopleModel> r = peopleService.findAllByPedigreeAndParentKeyStartsWith(pedigree, "r");
        r.sort((o1, o2) -> (o1.getParentKey().compareToIgnoreCase(o2.getParentKey())));

        ChartConfig chartConfig = getChartConfig(r);
        return chartConfig;
    }

    private ChartConfig getChartConfig(List<PeopleModel> r) {
        ChartConfig chartConfig = new ChartConfig();
        for (PeopleModel peopleModel: r) {
            Child child = getChildFromPeopleModle(peopleModel);
            chartConfig.addChild(child);
        }
        return chartConfig;
    }

    private Child getChildFromPeopleModle(PeopleModel peopleModel) {
        Child child = new Child();
        child.setHTMLid(peopleModel.getId()+"");

        Text text= new Text();
        text.setTitle(peopleModel.getLifeIndex()+"");
        text.setName(peopleModel.getName());
        child.setText(text);
        child.setImage(peopleModel.getImg());
        child.setHTMLclass(peopleModel.getParentKey());
        child.setParentKey(peopleModel.getParentKey());
        child.setId(peopleModel.getId());
        return  child;
    }

    private Child getParrentFromList(List<ConfigTree> r) {
        Iterator<ConfigTree> iterator = r.iterator();
        while(iterator.hasNext()){
            ConfigTree child = iterator.next();
            if(child.getClass().equals(Child.class)){
                return (Child) child;
            }
        }
        return null;
    }
}
