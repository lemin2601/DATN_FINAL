package com.leemin.genealogy.service;
import com.leemin.genealogy.data.FindHusbandWife;
import com.leemin.genealogy.data.GioiTinh;
import com.leemin.genealogy.data.QuanHe;
import com.leemin.genealogy.model.GenealogyPedigreeModel;
import com.leemin.genealogy.model.PedigreeModel;
import com.leemin.genealogy.model.PeopleModel;
import com.leemin.genealogy.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PeopleServiceImpl implements PeopleService {
    @Autowired
    PeopleRepository peopleRepository;

    @Autowired
    PedigreeService pedigreeService;

    @Autowired
    UserGenealogyRepository userGenealogyRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GenealogyRepository genealogyRepository;

    @Autowired
    PedigreeRepository pedigreeRepository;

    @Autowired
    GenealogyPedigreeRepository genealogyPedigreeRepository;
    @Override
    public PeopleModel add(PeopleModel peopleModel) {
        return peopleRepository.save(peopleModel);
    }

    @Override
    public PeopleModel findById(long idPeople) {
        return peopleRepository.findOne(idPeople);
    }

    @Override
    public PeopleModel findByIdAndFetch(long idPeople) {
        return peopleRepository.findByIdAndFetch(idPeople);
    }

    @Override
    public List<PeopleModel> findAll() {
        return peopleRepository.findAll();
    }


    @Override
    public List<PeopleModel> findAllByPedigreeAndParentKeyStartsWith(PedigreeModel pedigreeModel,String parentKeyStartWith){
        return peopleRepository.findAllByPedigreeAndParentKeyStartsWithOrderByParentKeyAscIdMotherAscRelationAsc(pedigreeModel,parentKeyStartWith);
    }

    @Override
    public List<PeopleModel> findAllByPedigreeAndParentKey(PedigreeModel pedigreeModel, String parentKey) {
        return peopleRepository.findAllByPedigreeAndParentKey(pedigreeModel,parentKey);
    }

    @Override
    public void removeAllByIdPedigree(PedigreeModel pedigree) {
        peopleRepository.removeAllByPedigree(pedigree);
    }

    @Override
    public void deleteChild(Long id, String keyParent) {
        peopleRepository.deleteAllByIdOrParentKeyStartsWith(id,keyParent);
    }

    @Override
    public void updateParentKey(String oldKey, String newKey) {
        peopleRepository.updateKeyParent(oldKey,newKey);
    }

    @Override
    public boolean gopPhaHe(long idGenealogy,long idPedigreeFrom, long idPedigreeTo, long idParent, long idMother, int childIndex) {
        GenealogyPedigreeModel from = genealogyPedigreeRepository.findByGenealogy_IdAndPedigreeId(idGenealogy, idPedigreeFrom);
        GenealogyPedigreeModel to = genealogyPedigreeRepository.findByGenealogy_IdAndPedigreeId(idGenealogy, idPedigreeTo);
        if(from == null || to == null) return false;

        PedigreeModel oldPedigree = pedigreeService.findByIdPedigreeModel(idPedigreeFrom);
        PedigreeModel pedigreeTo = pedigreeService.findByIdPedigreeModel(idPedigreeTo);
        if(oldPedigree == null || pedigreeTo == null) return false;

        String newParentKey = "r";
        String oldParentKey = "r";
        int offsetIndexLife =  0 ;
        List<PeopleModel> peopleNeedUpdate = peopleRepository.findAllByPedigreeAndParentKeyStartsWith(oldPedigree, "r");
        PeopleModel oldParent = null;
        PeopleModel newParent = peopleRepository.findOne(idParent);
        for (PeopleModel p: peopleNeedUpdate) {
            if(p.getParentKey().equals("r")){
                oldParent = p;
                break;
            }
        }
        if(oldParent == null) return false;
        if(idParent != -1){
            newParentKey = PeopleModel.getKeyParent(newParent);
            offsetIndexLife = newParent.getLifeIndex() - oldParent.getChildIndex() + 1;
            if(idMother != -1){
                //tim em idMother có phải vợ hoặc chồng không
                String keyParent = PeopleModel.getKeyParent(newParent);
                int relationFind = QuanHe.VO.ordinal();
                if(newParent.getGender() == GioiTinh.NU.ordinal()) relationFind = QuanHe.CHONG.ordinal();
                List<PeopleModel> result = peopleRepository.findAllByPedigreeAndParentKeyAndRelationEquals(pedigreeTo, keyParent, relationFind);
                boolean check = false;
                for (PeopleModel p: result) {
                    if(p.getId() == idMother){
                        check =true;
                    }
                }
                if(!check){
                    return false;
                }
            }
        }
        if(idParent == -1){
            peopleRepository.removeAllByPedigreeId(idPedigreeTo);
            oldParent.setIdMother(null);
            oldParent.setParent(null);
        }else{
            if(idMother == -1){
                oldParent.setIdMother(null);
            }else{
                oldParent.setIdMother(idMother);

            }
            oldParent.setParent(newParent);
        }
        oldParent.setChildIndex(childIndex);
        for (PeopleModel p: peopleNeedUpdate) {
            p.setPedigree(pedigreeTo);
            p.setParentKey(p.getParentKey().replace(oldParentKey,newParentKey));
        }
        peopleRepository.save(peopleNeedUpdate);
        return true;
        //cập nhật lại gốc ( idPedigree  + parrentKey + indexLife
        //cập nhật lại idPedigree
        // cập nhật lại parentKey theo gốc mới
    }

}
