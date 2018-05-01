package com.leemin.genealogy.service;
import com.leemin.genealogy.model.PeopleModel;

import java.util.List;

public interface PeopleService {
    PeopleModel add(PeopleModel peopleModel);

    PeopleModel findById(long idPeople);

    List<PeopleModel> findAll();
}
