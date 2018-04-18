package com.leemin.genealogy.service;
import com.leemin.genealogy.model.PeopleModel;

import java.util.List;

public interface PeopleService {
    PeopleModel add(PeopleModel peopleModel);

    List<PeopleModel> findAll();
}
