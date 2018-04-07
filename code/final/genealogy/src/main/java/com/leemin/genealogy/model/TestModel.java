package com.leemin.genealogy.model;
import javax.persistence.*;

@Entity
@Table(name = "testTable")
public class TestModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="role_id")
    private long id;
    @Column(name="role")
    private String role;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }


}
