package com.leemin.genealogy.repository;
import com.leemin.genealogy.model.PedigreeModel;
import com.leemin.genealogy.model.PeopleModel;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.jpa.QueryHints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.QueryHint;
import java.util.List;

public interface PeopleRepository extends JpaRepository<PeopleModel, Long> {

    List<PeopleModel> findAllByPedigreeAndParentKeyStartsWith(PedigreeModel pedigreeModel,String parentKeyStartWith);

//    List<PeopleModel> findAllByPedigreeAndParentKeyStartsWithAndOrderByParentKeyAscIdMotherAddressAscRelationAsc(PedigreeModel pedigreeModel,String parentKeyStartWith);
    List<PeopleModel> findAllByPedigreeAndParentKeyStartsWithOrderByParentKeyAscIdMotherAscRelationAsc(PedigreeModel pedigreeModel,String parentKeyStartWith);

//    @Query("SELECT p FROM PeopleModel p JOIN FETCH p.parent JOIN  FETCH  p.mother WHERE p.pedigree = :pedigree AND p.parentKey LIKE :parentKey% ORDER BY p.parentKey ASC , p.mother ASC , p.relation DESC ")
//    List<PeopleModel> findPeopleForTreeView(@Param("pedigree")PedigreeModel idPedigree,@Param("parentKey")String parentKeyStartWith);

    List<PeopleModel> findAllByPedigreeAndParentKey(PedigreeModel pedigreeModel,String parentKey);


    List<PeopleModel> findAllByPedigreeAndParentKeyAndRelationEquals(PedigreeModel pedigreeModel,String parentKey,Integer relation );


    void removeAllByPedigree(PedigreeModel pedigreeModel);

    void removeAllByPedigreeId(long idPedigree);

    @Query("SELECT p FROM PeopleModel p JOIN FETCH  p.parent WHERE p.id = :id")
    PeopleModel findByIdAndFetch(@Param("id") long id);

    void deleteAllByIdOrParentKeyStartsWith(Long idPeople,String parentKey);

    @Modifying(clearAutomatically = true)
    @Query( nativeQuery = true, value = "update people set people.parent_key = REPLACE( people.parent_key ,:oldKey ,:newKey) where people.parent_key like concat(:oldKey,'%')")
    void updateKeyParent(@Param("oldKey") String oldKey, @Param("newKey") String  newKey );

}
