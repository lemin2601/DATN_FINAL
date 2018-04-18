package com.leemin.genealogy.service;
import com.leemin.genealogy.model.*;
import com.leemin.genealogy.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class PedigreeServiceImpl implements PedigreeService {

    @Autowired
    PedigreeRepository pedigreeRepository;

    @Autowired
    GenealogyPedigreeRepository genealogyPedigreeRepository;

    @Autowired
    GenealogyRepository genealogyRepository;

    @Override
    public List<PedigreeModel> findAll() {
        return pedigreeRepository.findAll();
    }

    @Override
    public List<PedigreeModel> findAll(long idGenealogy) {
        List<GenealogyPedigreeModel> allByGenealogy_id = genealogyPedigreeRepository.findByGenealogy_Id(idGenealogy);
        List<PedigreeModel> all =  new ArrayList<>();
        for (int i = 0 ; i < allByGenealogy_id.size();i++) {
            all.add(allByGenealogy_id.get(i).getPedigree());
        }
        return all;
    }

    @Override
    public PedigreeModel add(PedigreeModel pedigreeModel, long idGenealogy, String username) {
        PedigreeModel save = pedigreeRepository.save(pedigreeModel);
        GenealogyPedigreeModel genealogyPedigreeModel = new GenealogyPedigreeModel();
        genealogyPedigreeModel.setPedigree(save);
        genealogyPedigreeModel.setGenealogy(genealogyRepository.getOne(idGenealogy));
        genealogyPedigreeRepository.save(genealogyPedigreeModel);
        return save;
    }

    @Override
    public PedigreeModel update(PedigreeModel pedigreeModel) {
        PedigreeModel save = pedigreeRepository.save(pedigreeModel);
        return save;
    }

    @Override
    public boolean delete(PedigreeModel pedigreeModel) {
        return false;
    }
}
