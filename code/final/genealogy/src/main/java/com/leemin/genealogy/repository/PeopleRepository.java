package com.leemin.genealogy.repository;
import com.leemin.genealogy.model.PedigreeModel;
import com.leemin.genealogy.model.PeopleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PeopleRepository extends JpaRepository<PeopleModel, Long> {

    List<PeopleModel> findAllByPedigreeAndParentKeyStartsWith(PedigreeModel pedigreeModel,String parentKeyStartWith);
    List<PeopleModel> findAllByPedigreeAndParentKey(PedigreeModel pedigreeModel,String parentKey);

    void removeAllByPedigree(PedigreeModel pedigreeModel);
}
