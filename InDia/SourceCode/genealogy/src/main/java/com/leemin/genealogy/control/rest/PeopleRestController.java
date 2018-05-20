package com.leemin.genealogy.control.rest;

import com.leemin.genealogy.config.ConfigFormat;
import com.leemin.genealogy.config.ErrorKey;
import com.leemin.genealogy.config.tree.ChartConfig;
import com.leemin.genealogy.config.tree.Child;
import com.leemin.genealogy.config.tree.ConfigTree;
import com.leemin.genealogy.config.tree.Text;
import com.leemin.genealogy.data.*;
import com.leemin.genealogy.data.cachgoiten.CachGoiTen;
import com.leemin.genealogy.model.*;
import com.leemin.genealogy.repository.GenealogyPedigreeRepository;
import com.leemin.genealogy.repository.PeopleRepository;
import com.leemin.genealogy.repository.UserGenealogyRepository;
import com.leemin.genealogy.repository.UserRepository;
import com.leemin.genealogy.service.PedigreeService;
import com.leemin.genealogy.service.PeopleService;
import com.leemin.genealogy.service.StorageService;
import com.leemin.genealogy.util.ExcelImportUtil;
import com.leemin.genealogy.util.Util;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.awt.*;
import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@RestController
public class PeopleRestController {

    @Autowired
    PedigreeService  pedigreeService;

    @Autowired
    PeopleService peopleService;

    @Autowired
    StorageService storageService;

    @Autowired
    UserGenealogyRepository userGenealogyRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PeopleRepository peopleRepository;

    @Autowired
    GenealogyPedigreeRepository genealogyPedigreeRepository;


    private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
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


    @GetMapping(value = "/rest/genealogy/{idGenealogy}/pedigree/{idPedigree}/people/{idPeople}/husband-wife" )
    public ResponseEntity<?> getHusbandOrWifeById(
            Principal principal,
            @PathVariable(name = "idGenealogy")long idGenealogy,
            @PathVariable(name = "idPedigree")long idPedigree,
            @PathVariable(name = "idPeople") long idPeople
                                                   ){
        UserGenealogyModel userGenealogy = userGenealogyRepository.findTopByUserAndGenealogy_Id(userRepository.findByEmail(principal.getName()), idGenealogy);
        if(userGenealogy != null) {
            Permission permission = Permission.values()[(int) userGenealogy.getPermission().getId()];
            if(!(permission.equals(Permission.ADMIN) || permission.equals(Permission.MOD) || permission.equals(Permission.VIEW))){
                return new ResponseEntity<>("" , HttpStatus.EXPECTATION_FAILED);
            }
        }
        PeopleModel people = peopleService.findById(idPeople);
        PedigreeModel pedigree = pedigreeService.findByIdPedigreeModel(idPedigree);
        String keyParent = PeopleModel.getKeyParent(people);
        int relationfind = QuanHe.VO.ordinal();
        if(people.getGender() == GioiTinh.NU.ordinal()) relationfind = QuanHe.CHONG.ordinal();
        List<PeopleModel> result = peopleRepository.findAllByPedigreeAndParentKeyAndRelationEquals(pedigree, keyParent, relationfind);
        List<FindHusbandWife> findHusbandWives = new ArrayList<>();
        for (PeopleModel c :result ) {
            FindHusbandWife item  = new FindHusbandWife();
            item.setId(c.getId());
            item.setName(c.getName());
            findHusbandWives.add(item);
        }
        return new ResponseEntity<>(findHusbandWives , HttpStatus.OK);
    }


    @GetMapping(value = "/rest/genealogy/{idGenealogy}/pedigree/{idPedigree}/people/{idPeople}/get-mother" )
    public ResponseEntity<?> getMother(
            Principal principal,
            @PathVariable(name = "idGenealogy")long idGenealogy,
            @PathVariable(name = "idPedigree")long idPedigree,
            @PathVariable(name = "idPeople") long idPeople
                                                 ){
        UserGenealogyModel userGenealogy = userGenealogyRepository.findTopByUserAndGenealogy_Id(userRepository.findByEmail(principal.getName()), idGenealogy);
        if(userGenealogy != null) {
            Permission permission = Permission.values()[(int) userGenealogy.getPermission().getId()];
            if(!(permission.equals(Permission.ADMIN) || permission.equals(Permission.MOD) || permission.equals(Permission.VIEW))){
                return new ResponseEntity<>("" , HttpStatus.EXPECTATION_FAILED);
            }
        }
        PeopleModel people = peopleService.findById(idPeople);
        Long idMother = people.getIdMother();
        if(idMother != null){
            PeopleModel mother = peopleService.findById(idMother);
            FindHusbandWife findHusbandWife = new FindHusbandWife();
            findHusbandWife.setId(mother.getId());
            findHusbandWife.setName(mother.getName());
            return new ResponseEntity<>(findHusbandWife , HttpStatus.OK);

        }
        return new ResponseEntity<>("" , HttpStatus.FAILED_DEPENDENCY);

    }

    @GetMapping(value = "/rest/genealogy/{idGenealogy}/pedigree/{idPedigree}/people/{idPeople}/get-info-add" )
    public ResponseEntity<?> getInfoFormAddChild(
            Principal principal,
            @PathVariable(name = "idGenealogy")long idGenealogy,
            @PathVariable(name = "idPedigree")long idPedigree,
            @PathVariable(name = "idPeople") long idPeople
                                                 ){
        UserGenealogyModel userGenealogy = userGenealogyRepository.findTopByUserAndGenealogy_Id(userRepository.findByEmail(principal.getName()), idGenealogy);
        if(userGenealogy != null) {
            Permission permission = Permission.values()[(int) userGenealogy.getPermission().getId()];
            if(!(permission.equals(Permission.ADMIN) || permission.equals(Permission.MOD) || permission.equals(Permission.VIEW))){
                return new ResponseEntity<>("" , HttpStatus.EXPECTATION_FAILED);
            }
        }
        PeopleModel people = peopleService.findById(idPeople);
        if(people== null) return new ResponseEntity<>(idPeople , HttpStatus.NOT_FOUND);
        String keyParent = PeopleModel.getKeyParent(people);
        PedigreeModel pedigree = pedigreeService.findByIdPedigreeModel(idPedigree);
        int relationfind = QuanHe.VO.ordinal();
        if(people.getGender() == GioiTinh.NU.ordinal()) relationfind = QuanHe.CHONG.ordinal();
        //keim tra relation ==> neu la vo/chong
        List<FindHusbandWife> findHusbandWives  = new ArrayList<>();
        System.out.println(QuanHe.values()[people.getRelation()].name());
        switch (QuanHe.values()[people.getRelation()]) {
            case CHA:
            case ME:
                List<PeopleModel> result = peopleRepository.findAllByPedigreeAndParentKeyAndRelationEquals(pedigree, keyParent, relationfind);
                FindHusbandWife itemDontKnow  = new FindHusbandWife();
                itemDontKnow.setId(-1);
                itemDontKnow.setName("Không rõ");
                findHusbandWives.add(itemDontKnow);
                for (PeopleModel c :result ) {
                    FindHusbandWife item  = new FindHusbandWife();
                    item.setId(c.getId());
                    item.setName(c.getName());
                    findHusbandWives.add(item);
                }
                break;
            case CHONG:
            case VO:
                keyParent = people.getParentKey();
                FindHusbandWife item  = new FindHusbandWife();
                item.setId(people.getId());
                item.setName(people.getName());
                findHusbandWives.add(item);
                break;
        }
        PeopleModel parent = peopleService.findById(Long.parseLong(keyParent.substring(keyParent.lastIndexOf("_") + 1 )));
        FindInfoFormAddChild findInfoFormAddChild = new FindInfoFormAddChild();
        findInfoFormAddChild.setIdParent(parent.getId());
        findInfoFormAddChild.setNameParent(parent.getName());
        findInfoFormAddChild.setFindHusbandWifes(findHusbandWives);

        return new ResponseEntity<>(findInfoFormAddChild , HttpStatus.OK);
    }



    //"https://"+ document.location.host  +'/rest/genealogy/'+idGenealogy+'/pedigree/' + idPedigree+'list-people',
    @GetMapping(value = "/rest/genealogy/{idGenealogy}/pedigree/{idPedigree}/list-people" , produces = "application/json")
    public Collection<PeopleModel> getAllListPeople(
            Principal principal,
            @PathVariable(name = "idGenealogy")long idGenealogy,
            @PathVariable(name = "idPedigree")long idPedigree
                                                     ){
        UserGenealogyModel userGenealogy = userGenealogyRepository.findTopByUserAndGenealogy_Id(userRepository.findByEmail(principal.getName()), idGenealogy);
        if(userGenealogy != null) {
            Permission permission = Permission.values()[(int) userGenealogy.getPermission().getId()];
            if(!(permission.equals(Permission.ADMIN) || permission.equals(Permission.MOD) || permission.equals(Permission.VIEW))){
                return null;
            }
        }
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

    //"https://"+ document.location.host  +'/rest/genealogy/'+idGenealogy+'/pedigree/' + idPedigree+'list-people',
    @GetMapping(value = "/rest/genealogy/{idGenealogy}/pedigree/{idPedigree}/all-people-name")
    public ResponseEntity<?> getAllListPeopleForSelect(
            Principal principal,
            @PathVariable(name = "idGenealogy")long idGenealogy,
            @PathVariable(name = "idPedigree")long idPedigree
                                                   ){
        UserGenealogyModel userGenealogy = userGenealogyRepository.findTopByUserAndGenealogy_Id(userRepository.findByEmail(principal.getName()), idGenealogy);
        if(userGenealogy != null) {
            Permission permission = Permission.values()[(int) userGenealogy.getPermission().getId()];
            if(!(permission.equals(Permission.ADMIN) || permission.equals(Permission.MOD) || permission.equals(Permission.VIEW))){
                return null;
            }
        }
        //TODO check condition pedigree
        PedigreeModel byIdPedigreeModel = pedigreeService.findByIdPedigreeModel(idPedigree);
        List<PeopleModel> r = peopleService.findAllByPedigreeAndParentKeyStartsWith(byIdPedigreeModel, "r");
        List<FindHusbandWife> findHusbandWives = new ArrayList<>();
        for(PeopleModel people:r){
            FindHusbandWife item = new FindHusbandWife();
            item.setId(people.getId());
            item.setName(people.getName());
            findHusbandWives.add(item);
        }
        return new ResponseEntity<>(findHusbandWives, HttpStatus.OK);
    }
    //"https://"+ document.location.host  +'/rest/genealogy/'+idGenealogy+'/pedigree/' + idPedigree+'list-people',
    @GetMapping(value = "/rest/genealogy/{idGenealogy}/pedigree/{idPedigree}/all-people-name-node-parent")
    public ResponseEntity<?> getAllListPeopleForSelectParent(
            Principal principal,
            @PathVariable(name = "idGenealogy")long idGenealogy,
            @PathVariable(name = "idPedigree")long idPedigree
                                                      ){
        UserGenealogyModel userGenealogy = userGenealogyRepository.findTopByUserAndGenealogy_Id(userRepository.findByEmail(principal.getName()), idGenealogy);
        if(userGenealogy != null) {
            Permission permission = Permission.values()[(int) userGenealogy.getPermission().getId()];
            if(!(permission.equals(Permission.ADMIN) || permission.equals(Permission.MOD) || permission.equals(Permission.VIEW))){
                return null;
            }
        }
        //TODO check condition pedigree
        PedigreeModel byIdPedigreeModel = pedigreeService.findByIdPedigreeModel(idPedigree);
        List<PeopleModel> r = peopleService.findAllByPedigreeAndParentKeyStartsWith(byIdPedigreeModel, "r");
        List<FindHusbandWife> findHusbandWives = new ArrayList<>();
        for(PeopleModel people:r){
            QuanHe quanHe = QuanHe.values()[people.getRelation()];
            if(quanHe == QuanHe.CHA ||quanHe == QuanHe.ME){
                FindHusbandWife item = new FindHusbandWife();
                item.setId(people.getId());
                item.setName(people.getName());
                findHusbandWives.add(item);
            }
        }
        return new ResponseEntity<>(findHusbandWives, HttpStatus.OK);
    }

    @GetMapping(value = "/rest/people/detail/{idPeople}")
    public ResponseEntity<?> getDetailPeople(
            Principal principal,
            @PathVariable(name = "idPeople")long idPeople
                                                   ){
        //TODO check condition pedigree
        PeopleModel people = peopleService.findById(idPeople);
        FindInfoPeopleDetail findInfoPeopleDetail = new FindInfoPeopleDetail();
        if(people == null){
            return new ResponseEntity<>("", HttpStatus.FAILED_DEPENDENCY);
        }
        if(people.getIdMother()!= null){
            PeopleModel mother = peopleService.findById(people.getIdMother());
            if(mother != null){
                findInfoPeopleDetail.setNameMother(mother.getName());
            }
        }
        if(people.getParent() != null){
            findInfoPeopleDetail.setNameParent(people.getParent().getName());
        }
        findInfoPeopleDetail.setImg(people.getImg());
        findInfoPeopleDetail.setName(people.getName());
        findInfoPeopleDetail.setNickName(people.getNickName());
        findInfoPeopleDetail.setRelation(people.getRelation());
        findInfoPeopleDetail.setAddress(people.getAddress());
        findInfoPeopleDetail.setLifeIndex(people.getLifeIndex());
        findInfoPeopleDetail.setBirthDay(Util.dateToString(people.getBirthday()));
        findInfoPeopleDetail.setDeadDay(Util.dateToString(people.getDeadDay()));
        findInfoPeopleDetail.setDataExtra(people.getDataExtra());
        findInfoPeopleDetail.setDegree(people.getDegree());
        findInfoPeopleDetail.setChildIndex(people.getChildIndex());
        findInfoPeopleDetail.setDes(people.getDes());
        return new ResponseEntity<>(findInfoPeopleDetail, HttpStatus.OK);
    }

    @GetMapping(value = "/rest/people/relation/{idXemQuanHe1}/{idXemQuanHe2}")
    public ResponseEntity<?> getRelation(
            Principal principal,
            @PathVariable(name = "idXemQuanHe1")long idXemQuanHe1,
            @PathVariable(name = "idXemQuanHe2")long idXemQuanHe2
                                 ){
        if(idXemQuanHe1 == idXemQuanHe2) return new ResponseEntity<>("is one people" , HttpStatus.NOT_FOUND);
        PeopleModel nguoi1 = peopleService.findById(idXemQuanHe1);
        PeopleModel nguoi2 = peopleService.findById(idXemQuanHe2);
        /*keyParrent
        so sánh đến lúc khác
            get key 2 cái khác nhau
                so sánh vai vế ChildIndex
                    ==> bên ngoài lớn hơn
                    bằng nhau ==> sinh đôi
                        thằng id<hơn làm anh

        nếu key parrent là giới tính nữ => bên ngoài dòng họ
        nếu keyParrent cuối cùng là nữ và con là chồng thì vẫn là bên trong dòng họ

        so sánh độ dại + quan hệ
            Nếu là vợ chông
                đô dài key + 1
            nếu là cha me
                dữ nguyên
            ==> chênh lệch câps
        */
        if(nguoi1 == null || nguoi2 == null)  return new ResponseEntity<>("not found people with that id" , HttpStatus.NOT_FOUND);
        String parentKey1 = nguoi1.getParentKey();
        String parentKey2 = nguoi2.getParentKey();
        GioiTinh gender1 = GioiTinh.values()[nguoi1.getGender()];
        GioiTinh gender2 = GioiTinh.values()[nguoi2.getGender()];
        int level = 0;

        QuanHe quanHe1 = QuanHe.values()[nguoi1.getRelation()];
        QuanHe quanHe2 = QuanHe.values()[nguoi2.getRelation()];
        //neu la vo hoac chong thi dung o vai ve nguoi con lai de so sanh
        if(quanHe1 == QuanHe.CHONG || quanHe1 == QuanHe.VO){
            Long idParent = Long.parseLong(parentKey1.substring(parentKey1.lastIndexOf("_")+1));
            nguoi1 = peopleService.findById(idParent);
            parentKey1 = nguoi1.getParentKey();
            quanHe1 = QuanHe.values()[nguoi1.getRelation()];
        }
        if(quanHe2 == QuanHe.CHONG || quanHe2 == QuanHe.VO){
            Long idParent = Long.parseLong(parentKey2.substring(parentKey2.lastIndexOf("_")+1));
            nguoi2 = peopleService.findById(idParent);
            parentKey2 = nguoi2.getParentKey();
            quanHe2 = QuanHe.values()[nguoi2.getRelation()];
        }

        String[] arrKey1 = parentKey1.split("_");
        String[] arrKey2 = parentKey2.split("_");

        boolean people1Higher = false;
        boolean people2Higher = false;

        boolean peopleOutside1 = false;
        boolean peopleOutside2 = false;

        boolean isParent1 = false;
        boolean isParent2 = false;

        int level1 = arrKey1.length;
        int level2 = arrKey2.length;

        int length = arrKey1.length;
        if(arrKey2.length < arrKey1.length){
            length = arrKey2.length;
            people2Higher = false;
            people1Higher = true;
        }else if( arrKey2.length < arrKey1.length){
            people2Higher = true;
            people1Higher = false;
        }
        int indexDifferent = -1;
        if(!nguoi1.getId()
                  .equals(nguoi2.getId())){
            for( int i  = 1; i <length ; i++){
                if(!arrKey1[i].equals(arrKey2[i])){
                    PeopleModel temp1 = peopleService.findById(Long.parseLong(arrKey1[i]));
                    PeopleModel temp2 = peopleService.findById(Long.parseLong(arrKey2[i]));
                    if(temp1 != null && temp2 != null) {
                        people1Higher = temp1.getChildIndex() > temp2.getChildIndex() || temp1.getChildIndex() >= temp2.getChildIndex() && (temp1.getId() < temp2.getId());
                        people2Higher = ! people1Higher;
                        indexDifferent = i;
                        break;
                    }
                }
            }
            if(people1Higher == people2Higher) {
                people1Higher = nguoi1.getChildIndex() > nguoi2.getChildIndex() || nguoi1.getChildIndex() <= nguoi2.getChildIndex() && (nguoi1.getId() > nguoi2.getId());
            }
            people2Higher = !people1Higher;
        }else{
            isParent1 = true;
            isParent2 = true;
        }

        if(indexDifferent != -1){
            if(indexDifferent < arrKey1.length-1){
                for(int i = indexDifferent; i < arrKey1.length;i++){
                    PeopleModel temp = peopleService.findById(Long.parseLong(arrKey1[i]));
                    if(temp.getGender() == GioiTinh.NU.ordinal()){
                        if(!(i == arrKey1.length -1 && nguoi1.getRelation() == QuanHe.CHONG.ordinal())){
                            peopleOutside1 = true;
                            break;
                        }
                    }
                }
            }
            if(indexDifferent< arrKey2.length-1){
                for(int i = indexDifferent; i < arrKey2.length;i++){
                    PeopleModel temp = peopleService.findById(Long.parseLong(arrKey2[i]));
                    if(temp.getGender() == GioiTinh.NU.ordinal()){
                        if(!(i == arrKey2.length -1 && nguoi2.getRelation() == QuanHe.CHONG.ordinal())){
                            peopleOutside2 = true;
                            break;
                        }
                    }
                }
            }
        }
        if(quanHe1 == QuanHe.CHONG || quanHe1 == QuanHe.VO){
            level1 -= 1;
            if(arrKey1[arrKey1.length-1].equals(nguoi2.getId() + "")){
                isParent1 = true;
                isParent2 = true;
                people2Higher = false;
                people1Higher = false;
            }
            if(!(quanHe2 == QuanHe.CHONG || quanHe2 == QuanHe.VO)){
                if(nguoi1.getParentKey().equals(nguoi2.getParentKey())){
                    isParent1 = true;
                    isParent2 = true;
                    people1Higher = true;
                    people2Higher = false;
                }
            }
        }
        if(quanHe2 == QuanHe.CHONG || quanHe2 == QuanHe.VO){
            level2 -= 1;
            if(arrKey2[arrKey2.length-1].equals(nguoi1.getId() + "")){
                isParent1 = true;
                isParent2 = true;
                people2Higher = false;
                people1Higher = false;
            }
            if(!(quanHe1 == QuanHe.CHONG || quanHe1 == QuanHe.VO)){
                if(nguoi1.getParentKey().equals(nguoi2.getParentKey())){
                    isParent1 = true;
                    isParent2 = true;
                    people1Higher = false;
                    people2Higher = true;
                }
            }
        }
        if(arrKey2[arrKey2.length-1].equals(nguoi1.getId() + "")){
            isParent1 = true;
            isParent2 = true;
        }
        if(arrKey1[arrKey1.length-1].equals(nguoi2.getId() + "")){
            isParent1 = true;
            isParent2 = true;
        }
        level = Math.abs(level1 - level2);
        //check special if vk/ck



        String call1 = CachGoiTen.getInstance().getName(gender2,level,people1Higher,peopleOutside1,isParent1);
        String call2 = CachGoiTen.getInstance().getName(gender1,level,people2Higher,peopleOutside2,isParent2);
        return new ResponseEntity<>(call1 + "|"+ call2 , HttpStatus.OK);
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
//        r.sort(new Comparator<PeopleModel>() {
//            @Override
//            public int compare(PeopleModel o1, PeopleModel o2) {
//                int compare = o1.getParentKey().compareToIgnoreCase(o2.getParentKey());
//                if(compare == 0){
//                    return o1.getChildIndex() - o2.getChildIndex();
//                }
//                return compare;
//            }
//        });

        ChartConfig chartConfig = getChartConfigNew(r);
        return chartConfig;
    }

    private ChartConfig getChartConfig(List<PeopleModel> r) {
        ChartConfig chartConfig = new ChartConfig();
        for (PeopleModel peopleModel: r) {
            Child child = getChildFromPeopleModel(peopleModel);
            chartConfig.addChildHaveMother(child);
        }
        return chartConfig;
    }

    private ChartConfig getChartConfigNew(List<PeopleModel> r) {
        ChartConfig chartConfig = new ChartConfig();
        for (PeopleModel peopleModel: r) {
            Child child = getChildFromPeopleModel(peopleModel);
            chartConfig.addChildHaveMother(child);
        }
        return chartConfig;
    }

    private Child getChildFromPeopleModel(PeopleModel peopleModel) {
        Child child = new Child();
        child.setHTMLid(peopleModel.getId()+"");

        Text text= new Text();
        text.setTitle(ConfigFormat.getStringFromDate(peopleModel.getBirthday())+" - " + ConfigFormat.getStringFromDate(peopleModel.getDeadDay()));
        text.setName(peopleModel.getName());
        child.setText(text);
        child.setGender(peopleModel.getGender());
        child.setRelation(peopleModel.getRelation());
        child.setIdMother(peopleModel.getIdMother());
        child.setImage(peopleModel.getImg());
        child.setHTMLclass("people_chart_node");
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


    @PostMapping(value = "/rest/people/add")
    public ResponseEntity<?> addChildPeople(
            Principal principal,
            @RequestParam(value = "idGenealogy", required = true, defaultValue = "") long idGenealogy,
            @RequestParam(value = "idPedigree", required = true, defaultValue = "") long idPedigree,
            @RequestParam(value = "addChildIdParent", required = true, defaultValue = "") String addChildIdParent,
            @RequestParam(value = "addChildInputIdMother", required = true, defaultValue = "") String addChildInputIdMother,
            @RequestParam(value = "addChildInputRelation", required = true, defaultValue = "") String addChildInputRelation,
            @RequestParam(value = "addChildInputConThu", required = true, defaultValue = "") String addChildInputConThu,
            @RequestParam(value = "addChildInputName", required = true, defaultValue = "") String addChildInputName,
            @RequestParam(value = "addChildInputNickName", required = true, defaultValue = "") String addChildInputNickName,
            @RequestParam(value = "addChildInputGender", required = true, defaultValue = "") String addChildInputGender,
            @RequestParam(value = "addChildInputAddress", required = true, defaultValue = "") String addChildInputAddress,
            @RequestParam(value = "addChildInputBirthday", required = true, defaultValue = "") String addChildInputBirthday,
            @RequestParam(value = "addChildInputDeadDay", required = true, defaultValue = "") String addChildInputDeadDay,
            @RequestParam(value = "addChildInputDegree", required = false, defaultValue = "") String addChildInputDegree,
            @RequestParam(value = "addChildInputDes", required = false, defaultValue = "") String addChildInputDes,
            @RequestParam(value = "addChildInputDataExtra", required = false, defaultValue = "") String addChildInputDataExtra,
            @RequestParam("addChildInputFileImg") MultipartFile addChildInputFileImg
                                           ){
        //TODO check condition pedigree
        UserGenealogyModel userGenealogy = userGenealogyRepository.findTopByUserAndGenealogy_Id(userRepository.findByEmail(principal.getName()), idGenealogy);
        if(userGenealogy != null) {
            Permission permission = Permission.values()[(int) userGenealogy.getPermission().getId()];
            if(!(permission.equals(Permission.ADMIN) || permission.equals(Permission.MOD) || permission.equals(Permission.VIEW))){
                return null;
            }
        }
        int errorCode = ErrorKey.SUCCESS;
        PedigreeModel pedigreeModel = pedigreeService.findByIdPedigreeModel(idPedigree);
        PeopleModel peopleModel  = new PeopleModel();
        peopleModel.setName(addChildInputName);
        peopleModel.setNickName(addChildInputNickName);
        PeopleModel parent = null;
        if(!addChildIdParent.equals("") && !addChildIdParent.equals("-1")){ // -1 is root
            parent = peopleService.findById(Integer.parseInt(addChildIdParent));

            if(parent != null){

                peopleModel.setParent(parent);
            }else{
                errorCode = ErrorKey.PEDIGREE;
            }
        }
        peopleModel.setParentKey(PeopleModel.getKeyParent(parent));


        if(!addChildInputIdMother.equalsIgnoreCase("-1")){
            PeopleModel mother = peopleService.findById(Integer.parseInt(addChildInputIdMother));
            if(mother != null && mother.getParentKey().equals(peopleModel.getParentKey())){
                peopleModel.setIdMother(mother.getId());
            }else{
                errorCode = ErrorKey.MOTHER;
            }
        }

        peopleModel.setLifeIndex(PeopleModel.getIndexLife(parent));
        peopleModel.setPedigree(pedigreeModel);
        peopleModel.setBirthday(ExcelImportUtil.getDate(addChildInputBirthday));
        peopleModel.setDeadDay(ExcelImportUtil.getDate(addChildInputDeadDay));
        peopleModel.setRelation(Integer.parseInt(addChildInputRelation));
        peopleModel.setDegree(addChildInputDegree);
        peopleModel.setGender(Integer.parseInt(addChildInputGender));
        peopleModel.setDes(addChildInputDes);
        peopleModel.setChildIndex(Integer.parseInt(addChildInputConThu));
        peopleModel.setAddress(addChildInputAddress);
        peopleModel.setDataExtra(addChildInputDataExtra);
        peopleModel.setImg(ExcelImportUtil.getImgDefault(peopleModel.getGender()));
        if(errorCode == ErrorKey.SUCCESS){
            PeopleModel add = peopleService.add(peopleModel);
            String uploadedFileName = addChildInputFileImg.getOriginalFilename();
            if(!uploadedFileName.isEmpty()){
                uploadedFileName = +  add.getId() + "_" + System.currentTimeMillis() + uploadedFileName.substring(uploadedFileName.lastIndexOf("."));
                String fileImg = "img/"  + uploadedFileName;
                add.setImg("/image/" + uploadedFileName);
                storageService.store(addChildInputFileImg,fileImg);
                add = peopleService.add(peopleModel);
            }
//            Child child = getChildFromPeopleModel(add);
            return new ResponseEntity<>(errorCode , HttpStatus.OK);
        }

        return new ResponseEntity<>(errorCode , HttpStatus.OK);

    }

    @PostMapping(value = "/rest/people/edit")
    public ResponseEntity<?> editChild(
            Principal principal,
            @RequestParam(value = "idGenealogy", required = true, defaultValue = "") long idGenealogy,
            @RequestParam(value = "idPedigree", required = true, defaultValue = "") long idPedigree,
            @RequestParam(value = "editChildId", required = true, defaultValue = "") String editChildId,
            @RequestParam(value = "editChildIdParent", required = true, defaultValue = "") String editChildIdParent,
            @RequestParam(value = "editChildInputIdMother", required = true, defaultValue = "") String editChildInputIdMother,
            @RequestParam(value = "editChildInputRelation", required = true, defaultValue = "") String editChildInputRelation,
            @RequestParam(value = "editChildInputConThu", required = true, defaultValue = "") String editChildInputConThu,
            @RequestParam(value = "editChildInputName", required = true, defaultValue = "") String editChildInputName,
            @RequestParam(value = "editChildInputNickName", required = true, defaultValue = "") String editChildInputNickName,
            @RequestParam(value = "editChildInputGender", required = true, defaultValue = "") String editChildInputGender,
            @RequestParam(value = "editChildInputAddress", required = true, defaultValue = "") String editChildInputAddress,
            @RequestParam(value = "editChildInputBirthday", required = true, defaultValue = "") String editChildInputBirthday,
            @RequestParam(value = "editChildInputDeadDay", required = true, defaultValue = "") String editChildInputDeadDay,
            @RequestParam(value = "editChildInputDegree", required = false, defaultValue = "") String editChildInputDegree,
            @RequestParam(value = "editChildInputDes", required = false, defaultValue = "") String editChildInputDes,
            @RequestParam(value = "editChildInputDataExtra", required = false, defaultValue = "") String editChildInputDataExtra,
            @RequestParam("editChildInputFileImg") MultipartFile editChildInputFileImg
                                           ){
        //TODO check condition pedigree
        UserGenealogyModel userGenealogy = userGenealogyRepository.findTopByUserAndGenealogy_Id(userRepository.findByEmail(principal.getName()), idGenealogy);
        if(userGenealogy != null) {
            Permission permission = Permission.values()[(int) userGenealogy.getPermission().getId()];
            if(!(permission.equals(Permission.ADMIN) || permission.equals(Permission.MOD))){
                return null;
            }
        }

        int errorCode = ErrorKey.SUCCESS;
        PedigreeModel pedigreeModel = pedigreeService.findByIdPedigreeModel(idPedigree);
        PeopleModel peopleModel  = peopleService.findById(Integer.parseInt(editChildId));
        peopleModel.setName(editChildInputName);
        peopleModel.setNickName(editChildInputNickName);
//        if( !editChildInputIdMother.isEmpty() && !editChildInputIdMother.equalsIgnoreCase("-1")){
//            PeopleModel mother = peopleService.findById(Integer.parseInt(editChildInputIdMother));
//            if(mother != null && mother.getParentKey().equals(peopleModel.getParentKey())){
//                peopleModel.setIdMother(mother.getId());
//            }else{
//                errorCode = ErrorKey.MOTHER;
//            }
//        }
        peopleModel.setBirthday(ExcelImportUtil.getDate(editChildInputBirthday));
        peopleModel.setDeadDay(ExcelImportUtil.getDate(editChildInputDeadDay));
//        peopleModel.setRelation(Integer.parseInt(editChildInputRelation));
        peopleModel.setDegree(editChildInputDegree);
        peopleModel.setGender(Integer.parseInt(editChildInputGender));
        peopleModel.setDes(editChildInputDes);
        peopleModel.setChildIndex(Integer.parseInt(editChildInputConThu));
        peopleModel.setAddress(editChildInputAddress);
        peopleModel.setAddress(editChildInputDataExtra);
        peopleModel.setImg(ExcelImportUtil.getImgDefault(peopleModel.getGender()));
        if(errorCode == ErrorKey.SUCCESS){
            PeopleModel add = peopleService.add(peopleModel);
            String uploadedFileName = editChildInputFileImg.getOriginalFilename();
            if(!uploadedFileName.isEmpty()){
                uploadedFileName = +  add.getId() + "_" + System.currentTimeMillis() + uploadedFileName.substring(uploadedFileName.lastIndexOf("."));
                String fileImg = "img/"  + uploadedFileName;
                add.setImg("/image/" + uploadedFileName);
                storageService.store(editChildInputFileImg,fileImg);
                add = peopleService.add(peopleModel);
            }
//            Child child = getChildFromPeopleModel(add);
            return new ResponseEntity<>(errorCode , HttpStatus.OK);
        }

        return new ResponseEntity<>(errorCode , HttpStatus.OK);

    }
    @PostMapping(value = "/rest/genealogy/{idGenealogy}/pedigree/{idPedigree}/people/{idPeople}/delete")
    public ResponseEntity<?> deleteChildPeople(
            Principal principal,
            @PathVariable(value = "idGenealogy", required = true) long idGenealogy,
            @PathVariable(value = "idPedigree", required = true) long idPedigree,
            @PathVariable(value = "idPeople", required = true) long idPeople
            ){
        //TODO check condition pedigree
        UserGenealogyModel userGenealogy = userGenealogyRepository.findTopByUserAndGenealogy_Id(userRepository.findByEmail(principal.getName()), idGenealogy);
        if(userGenealogy != null) {
            Permission permission = Permission.values()[(int) userGenealogy.getPermission().getId()];
            if(!(permission.equals(Permission.ADMIN))){
                return null;
            }
        }
        int errorCode = ErrorKey.SUCCESS;
        GenealogyPedigreeModel byGenealogy_idAndPedigreeId = genealogyPedigreeRepository.findByGenealogy_IdAndPedigreeId(idGenealogy, idPedigree);
        if(byGenealogy_idAndPedigreeId != null){
            PeopleModel del = peopleService.findById(idPeople);
            if(del.getPedigree().getId() == byGenealogy_idAndPedigreeId.getPedigree().getId()){
                peopleService.deleteChild(del.getId(),PeopleModel.getKeyParent(del));
            }
        }
        return new ResponseEntity<>(errorCode , HttpStatus.OK);
    }

    @GetMapping(value = "/rest/genealogy/{idGenealogy}/pedigree/{idPedigree}/people/{idPeople}/update")
    public ResponseEntity<?> updateParrentKey(
            Principal principal,
            @PathVariable(value = "idGenealogy", required = true) long idGenealogy,
            @PathVariable(value = "idPedigree", required = true) long idPedigree,
            @PathVariable(value = "idPeople", required = true) long idPeople
                                              ){
        //TODO check condition pedigree
        UserGenealogyModel userGenealogy = userGenealogyRepository.findTopByUserAndGenealogy_Id(userRepository.findByEmail(principal.getName()), idGenealogy);
        if(userGenealogy != null) {
            Permission permission = Permission.values()[(int) userGenealogy.getPermission().getId()];
            if(!(permission.equals(Permission.ADMIN))){
                return null;
            }
        }
        int errorCode = ErrorKey.SUCCESS;
        GenealogyPedigreeModel byGenealogy_idAndPedigreeId = genealogyPedigreeRepository.findByGenealogy_IdAndPedigreeId(idGenealogy, idPedigree);
        if(byGenealogy_idAndPedigreeId != null){
            peopleService.updateParentKey("RR" , "r");
        }
        return new ResponseEntity<>(errorCode , HttpStatus.OK);
    }

    private void saveUploadedFiles(MultipartFile file,String fileName) throws IOException {
        if (file.isEmpty()) {
            return;
        }
        storageService.store(file,fileName);
    }

}
