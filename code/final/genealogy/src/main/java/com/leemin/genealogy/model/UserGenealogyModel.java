package com.leemin.genealogy.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
//
//@Embeddable
//public class UserGenealogyModel implements Serializable {
//
//    private long idUser;
//    private long idGenealogy;
//    private long idPermistion;
//
//    @Override
//    public int hashCode() {
//        return (int) (idUser+idGenealogy);
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj)
//            return true;
//        if (obj == null)
//            return false;
//        if (getClass() != obj.getClass())
//            return false;
//        UserGenealogyModel other = (UserGenealogyModel) obj;
//        if (idUser != other.idUser || idGenealogy != other.idGenealogy ) {
//            return false;
//        }
//        return true;
//    }
//}

@Entity
@Table(name = "user_genealogy")
public class UserGenealogyModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_genealogy_id")
    private long id;

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "genealogy_id" )
//    private long idGenealogy;
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "permission_id")
//    private long idPermission;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns({ @JoinColumn(name="user_id", referencedColumnName="user_id")})
//    @JoinColumn(name="user_id", nullable=true)
    private UserModel user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns({ @JoinColumn(name="genealogy_id", referencedColumnName="genealogy_id")})//, nullable=false, insertable=false, updatable=false
    private GenealogyModel genealogy;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns({ @JoinColumn(name="permission_id", referencedColumnName="permission_id")})
//    @JoinColumn(name="permission_id", nullable=true)
    private PermissionModel  permission;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public GenealogyModel getGenealogy() {
        return genealogy;
    }

    public void setGenealogy(GenealogyModel genealogy) {
        this.genealogy = genealogy;
    }

    public PermissionModel getPermission() {
        return permission;
    }

    public void setPermission(PermissionModel permission) {
        this.permission = permission;
    }

}