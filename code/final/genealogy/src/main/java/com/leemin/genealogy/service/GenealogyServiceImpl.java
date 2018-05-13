package com.leemin.genealogy.service;
import com.leemin.genealogy.model.*;
import com.leemin.genealogy.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GenealogyServiceImpl implements GenealogyService {
    @Autowired
    GenealogyRepository genealogyRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    UserGenealogyRepository userGenealogyRepository;

    @Autowired
    GenealogyPedigreeRepository genealogyPedigreeRepository;

    @Override
    public List<GenealogyModel> findAll() {
        List<GenealogyModel> all = genealogyRepository.findAll();
        List<UserGenealogyModel> userGenealogyModels = userGenealogyRepository.findAll();
        Iterator<UserGenealogyModel> iterator = userGenealogyModels.iterator();
        all = new ArrayList<>();
        while (iterator.hasNext()){
            all.add(iterator.next().getGenealogy());
        }
        return all;
    }

    @Override
    public List<GenealogyModel> findAllByUserName(String username) {
        UserModel byEmail = userRepository.findByEmail(username);
        List<UserGenealogyModel> byUser = userGenealogyRepository.findByUser(byEmail);
        List<GenealogyModel> all =  new ArrayList<>();
        for (UserGenealogyModel aByUser : byUser) {
            all.add(aByUser.getGenealogy());
        }
        return all;
    }

    @Override
    public List<GenealogyModel> findAllLike(String search) {
        return genealogyRepository.findAllByNameLike(search);
    }

    @Override
    public GenealogyModel find(long idGenealogy, String username) {
        UserModel byEmail = userRepository.findByEmail(username);
        UserGenealogyModel topByUserAndGenealogy_id = userGenealogyRepository.findTopByUserAndGenealogy_Id(byEmail, idGenealogy);
//        List<UserGenealogyModel> byUserAndGenealogy_id = userGenealogyRepository.findByUserAndGenealogy_Id();
        GenealogyModel genealogyModel = topByUserAndGenealogy_id.getGenealogy();
        return genealogyModel;
    }

    @Override
    public boolean create(String name, String history, String thuyTo, String username) {
        GenealogyModel genealogyModel = new GenealogyModel();
        genealogyModel.setName(name);
        genealogyModel.setHistory(history);
        genealogyModel.setThuyTo(thuyTo);
        UserModel user = userRepository.findByEmail(username);

        return false;
    }

    @Override
    public GenealogyModel create(GenealogyModel genealogyModel,String UsernameOrEmail) {

        UserModel user = userRepository.findByEmail(UsernameOrEmail);

        PermissionModel permission = permissionRepository.findOne((long) 1);
        GenealogyModel genealogyModel1 = genealogyRepository.save(genealogyModel);

        UserGenealogyModel userGenealogyModel = new UserGenealogyModel();
        userGenealogyModel.setUser(user);
        userGenealogyModel.setPermission(permission);
        userGenealogyModel.setGenealogy(genealogyModel1);
        UserGenealogyModel save = userGenealogyRepository.save(userGenealogyModel);

        return genealogyModel1;
    }

    @Override
    public GenealogyModel update(GenealogyModel genealogyModel) {
        return genealogyRepository.save(genealogyModel);
    }

    @Override
    public GenealogyModel findById(long id) {
        return genealogyRepository.findOne(id);
    }

    @Override
    public boolean delete(GenealogyModel genealogyModel) {
        return false;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }
}
