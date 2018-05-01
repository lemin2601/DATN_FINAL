package com.leemin.genealogy.upload;
import com.leemin.genealogy.model.PedigreeModel;
import com.leemin.genealogy.model.PeopleModel;

import javax.persistence.*;
import java.util.Date;

public class PeopleUpload {

    private boolean isRealParent;

    private int statusUpload;

    private long id;

    private long idParent;

    private long idPedigree;

    private int lifeIndex;

    private String name;

    private String nickName;

    private int gender;

    private Date birthday;

    private Date deadDay;

    private String address;

    private String degree;

    private String img;

    private String des;

    private String dataExtra;

    public PeopleUpload() {
        this.isRealParent = false;
    }

    public int getStatusUpload() {
        return statusUpload;
    }

    public void setStatusUpload(int statusUpload) {
        this.statusUpload = statusUpload;
    }

    public boolean isRealParent() {
        return isRealParent;
    }

    public void setRealParent(boolean realParent) {
        isRealParent = realParent;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdParent() {
        return idParent;
    }

    public void setIdParent(long idParent) {
        this.idParent = idParent;
    }

    public long getIdPedigree() {
        return idPedigree;
    }

    public void setIdPedigree(long idPedigree) {
        this.idPedigree = idPedigree;
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
}
