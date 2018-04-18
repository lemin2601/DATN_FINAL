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
    public GenealogyModel find(long idGenealogy, String username) {
        UserModel byEmail = userRepository.findByEmail(username);
        List<UserGenealogyModel> byUserAndGenealogy_id = userGenealogyRepository.findByUserAndGenealogy_Id(byEmail, idGenealogy);
        GenealogyModel genealogyModel = byUserAndGenealogy_id.get(0).getGenealogy();
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

        PermissionModel permissionModel = new PermissionModel();
        permissionModel.setName("ADMIN");
        PermissionModel save1 = permissionRepository.save(permissionModel);

        GenealogyModel genealogyModel1 = genealogyRepository.save(genealogyModel);

        UserGenealogyModel userGenealogyModel = new UserGenealogyModel();
        userGenealogyModel.setUser(user);
        userGenealogyModel.setPermission(permissionModel);
        userGenealogyModel.setGenealogy(genealogyModel1);
        UserGenealogyModel save = userGenealogyRepository.save(userGenealogyModel);

        return genealogyModel1;
    }

    @Override
    public boolean update(GenealogyModel genealogyModel) {
        return false;
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
