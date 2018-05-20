package com.leemin.genealogy.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "genealogy_pedigree")
public class GenealogyPedigreeModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "genealogy_pedigree")
    private long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumns({ @JoinColumn(name="genealogy_id", referencedColumnName="genealogy_id")})
    private GenealogyModel genealogy;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumns({ @JoinColumn(name="pedigree_id", referencedColumnName="pedigree_id")})
    private PedigreeModel  pedigree;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public GenealogyModel getGenealogy() {
        return genealogy;
    }

    public void setGenealogy(GenealogyModel genealogy) {
        this.genealogy = genealogy;
    }

    public PedigreeModel getPedigree() {
        return pedigree;
    }

    public void setPedigree(PedigreeModel pedigree) {
        this.pedigree = pedigree;
    }
}