package com.leemin.genealogy.service;
import com.leemin.genealogy.model.PedigreeModel;
import com.leemin.genealogy.model.PeopleModel;

import java.util.List;

public interface PeopleService {
    PeopleModel add(PeopleModel peopleModel);

    PeopleModel findById(long idPeople);

    PeopleModel findByIdAndFetch(long idPeople);

    List<PeopleModel> findAll();


    List<PeopleModel> findAllByPedigreeAndParentKeyStartsWith(PedigreeModel pedigreeModel,String parentKeyStartWith);

    List<PeopleModel> findAllByPedigreeAndParentKey(PedigreeModel pedigreeModel,String parentKey);

    void removeAllByIdPedigree(PedigreeModel pedigree);

    void deleteChild(Long id, String keyParent);

    void updateParentKey(String oldKey,String newKey);

    boolean gopPhaHe(long idGeneaogy,long idPedigreeFrom, long idPedigreeTo, long idParent,long idMother,int childIndex);
}
