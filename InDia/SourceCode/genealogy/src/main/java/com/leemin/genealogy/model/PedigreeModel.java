package com.leemin.genealogy.model;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "pedigree")
public class PedigreeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pedigree_id")
    private long id;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "history")
    private String history;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }
}
