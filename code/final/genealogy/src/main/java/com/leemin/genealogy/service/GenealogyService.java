package com.leemin.genealogy.service;
import com.leemin.genealogy.model.GenealogyModel;

import java.util.List;

public interface GenealogyService {

    List<GenealogyModel> findAll();
    List<GenealogyModel> findAllByUserName(String username);


    GenealogyModel find(long idGenealogy,String username);

    boolean create(String name,String history,String thuyTo, String username);

    GenealogyModel create(GenealogyModel genealogyModel,String usernameOrEmal);

    boolean update(GenealogyModel genealogyModel);

    boolean delete(GenealogyModel genealogyModel);

    boolean delete(long id);
}
