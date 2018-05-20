package com.leemin.genealogy.service;
import com.leemin.genealogy.model.GenealogyModel;
import com.leemin.genealogy.model.PedigreeModel;

import java.util.List;

public interface PedigreeService {

    List<PedigreeModel> findAll();

    PedigreeModel findByIdPedigreeModel(long idPedigree);

    List<PedigreeModel> findAll(long idGenealogy);

    PedigreeModel add(PedigreeModel pedigreeModel,long idGenealogy, String username);

    PedigreeModel update(PedigreeModel pedigreeModel);

    boolean delete(PedigreeModel pedigreeModel);

}
