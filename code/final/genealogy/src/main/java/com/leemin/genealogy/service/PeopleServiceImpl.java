package com.leemin.genealogy.service;
import com.leemin.genealogy.model.PedigreeModel;
import com.leemin.genealogy.model.PeopleModel;
import com.leemin.genealogy.repository.PeopleRepository;
import com.leemin.genealogy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PeopleServiceImpl implements PeopleService {
    @Autowired
    PeopleRepository peopleRepository;

    @Autowired
    PedigreeService pedigreeService;

    @Override
    public PeopleModel add(PeopleModel peopleModel) {
        return peopleRepository.save(peopleModel);
    }

    @Override
    public PeopleModel findById(long idPeople) {
        return peopleRepository.findOne(idPeople);
    }

    @Override
    public List<PeopleModel> findAll() {
        return peopleRepository.findAll();
    }

    @Override
    public List<PeopleModel> findAllByPedigreeAndParentKeyStartsWith(PedigreeModel pedigreeModel,String parentKeyStartWith){
        return peopleRepository.findAllByPedigreeAndParentKeyStartsWith(pedigreeModel,parentKeyStartWith);
    }

    @Override
    public List<PeopleModel> findAllByPedigreeAndParentKey(PedigreeModel pedigreeModel, String parentKey) {
        return peopleRepository.findAllByPedigreeAndParentKey(pedigreeModel,parentKey);
    }

}
