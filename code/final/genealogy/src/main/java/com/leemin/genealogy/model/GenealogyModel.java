package com.leemin.genealogy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "genealogy")
public class GenealogyModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "genealogy_id")
    private long id;

    @Column(name = "name")
    private String name;

    @Lob
    @Fetch(FetchMode.SELECT)
    @Column(name = "history")
    private String history;

    @JsonIgnore
    @Lob
    @Fetch(FetchMode.JOIN)
    @Column(name = "thuy_to")
    private String thuyTo;
/*
    @ManyToMany
//    @JoinTable(name = "user_genealogy",
//               joinColumns = @JoinColumn(name = "genealogy_id", referencedColumnName = "genealogy_id"),
//               inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"))
    @JoinTable(name = "user_genealogy", joinColumns = @JoinColumn(name = "genealogy_id"))
    private Set<UserModel> users = new HashSet<>();


    @ManyToMany
//    @JoinTable(name = "user_genealogy",
//               joinColumns = @JoinColumn(name = "genealogy_id", referencedColumnName = "genealogy_id"),
//               inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "permission_id"))
    @JoinTable(name = "user_genealogy", joinColumns = @JoinColumn(name = "genealogy_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<PermissionModel> permission = new HashSet<>();*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getThuyTo() {
        return thuyTo;
    }

    public void setThuyTo(String thuyTo) {
        this.thuyTo = thuyTo;
    }

}