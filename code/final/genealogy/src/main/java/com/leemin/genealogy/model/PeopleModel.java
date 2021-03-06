package com.leemin.genealogy.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leemin.genealogy.config.ConfigFormat;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.Date;

@Entity
@Table(name = "people")
public class PeopleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "people_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns({ @JoinColumn(name="parent_id", referencedColumnName="people_id")})
    private PeopleModel parent;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumns({ @JoinColumn(name="mother_id", referencedColumnName="people_id")})
//    private PeopleModel mother;

    @Column(name = "mother_id")
    private Long idMother;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumns({ @JoinColumn(name="pedigree_id", referencedColumnName="pedigree_id")})
    private PedigreeModel pedigree;

    @Column(name = "parent_key")
    @JsonIgnore
    private String parentKey;


    @Column(name = "parent_relation")
    private Integer relation;

    @Column(name = "index_life")
    private int lifeIndex;

    @Column(name = "name")
    private String name;

    @Column(name = "nick_name")
    private String nickName;


    @Column(name = "gender")
    private int gender;

    @Column(nullable=true, name="child_index")
    private Integer childIndex;

    @Column(name = "birthday")
    private Date birthday;


    @Column(name = "dead_day")
    private Date deadDay;

    @Column(name = "address")
    private String address;


    @Column(name = "degree")
    @JsonIgnore
    private String degree;

    @Column(name = "img")
    private String img;

    @Column(name = "des")
    @Lob
    @JsonIgnore
    private String des;

    @Column(name = "data_extra")
    @Lob
    @JsonIgnore
    private String dataExtra;

    public PeopleModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PeopleModel getParent() {
        return parent;
    }

    public void setParent(PeopleModel parent) {
        this.parent = parent;
    }

    public Long getIdMother() {
        return idMother;
    }

    public void setIdMother(Long idMother) {
        this.idMother = idMother;
    }

    public PedigreeModel getPedigree() {
        return pedigree;
    }

    public void setPedigree(PedigreeModel pedigree) {
        this.pedigree = pedigree;
    }

    public String getParentKey() {
        return parentKey;
    }

    public void setParentKey(String parentKey) {
        this.parentKey = parentKey;
    }

    public Integer getRelation() {
        return relation;
    }

    public void setRelation(Integer relation) {
        this.relation = relation;
    }

    public int getLifeIndex() {
        return lifeIndex;
    }

    public void setLifeIndex(int lifeIndex) {
        this.lifeIndex = lifeIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Integer getChildIndex() {
        return childIndex;
    }

    public void setChildIndex(Integer childIndex) {
        this.childIndex = childIndex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getDeadDay() {
        return deadDay;
    }

    public void setDeadDay(Date deadDay) {
        this.deadDay = deadDay;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDataExtra() {
        return dataExtra;
    }

    public void setDataExtra(String dataExtra) {
        this.dataExtra = dataExtra;
    }

    public static String getKeyParent(PeopleModel parent) {
        if(parent == null) return "r";
        return parent.getParentKey() + "_" + parent.getId();
    }

    public static int getIndexLife(PeopleModel parent) {
        if(parent == null) return 1;
        return parent.getLifeIndex() + 1;
    }


    @Override
    public String toString() {
        return "{" +
                " \"id\":" + id +
                ", \"parentKey\":\"" + parentKey + '"' +
                ", \"lifeIndex\":" + lifeIndex +
                ", \"name\":\"" + name + '"' +
                ", \"nickName\":\"" + (nickName==null?"":nickName) + '"' +
                ", \"gender\":" + gender +
                ", \"childIndex\":" + childIndex +
                ", \"birthday\":\"" + ConfigFormat.getStringFromDate(birthday)  + '"' +
                ", \"deadDay\":\"" + ConfigFormat.getStringFromDate(deadDay)  + '"' +
                ", \"address\":\"" + address + '"' +
                ", \"degree\":\"" + (degree==null?"":degree) + '"' +
                ", \"img\":\"" + img + '"' +
                ", \"des\":\"" + des + '"' +
                ", \"dataExtra\":\"" + (dataExtra==null?"":dataExtra) + '"' +
                '}';
    }
}
