package com.leemin.genealogy.repository;
import com.leemin.genealogy.model.PedigreeModel;
import com.leemin.genealogy.model.PeopleModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeopleRepository extends JpaRepository<PeopleModel, Long> {

}
