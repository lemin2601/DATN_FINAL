package com.leemin.genealogy.config.tree;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.leemin.genealogy.data.ClassColor;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Child extends ConfigTree{

    @JsonProperty("HTMLid")
    private String HTMLid;

    @JsonProperty("HTMLclass")
    private String HTMLclass;

    private String image;
    private Text text;
    private boolean collapsed;
    private int childrenDropLevel;
    @JsonProperty("data-parentKey")
    private String parentKey;
    @JsonProperty("data-id")
    private long id;

    @JsonProperty("data-id_mother")
    private Long idMother;

    @JsonProperty("data-relation")
    private int relation;

    private List<Child> children;

    @JsonIgnore
    private int gender;



    public Child() {
        childrenDropLevel = 0;
    }

    @JsonIgnore
    public int getGender() {
        return gender;
    }
    @JsonIgnore
    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public Long getIdMother() {
        return idMother;
    }

    public void setIdMother(Long idMother) {
        this.idMother = idMother;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getParentKey() {
        return parentKey;
    }

    public void setParentKey(String parentKey) {
        this.parentKey = parentKey;
    }

    @JsonIgnore
    public String getHTMLid() {
        return HTMLid;
    }

    public void setHTMLid(String HTMLid) {
        this.HTMLid = HTMLid;
    }

    @JsonIgnore
    public String getHTMLclass() {
        return HTMLclass;
    }

    @JsonIgnore
    public void addHTMLclass(ClassColor className) {
        HTMLclass = HTMLclass + " " + className.name();
    }


    public void setHTMLclass(String HTMLclass) {
        this.HTMLclass = HTMLclass;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    public int getChildrenDropLevel() {
        return childrenDropLevel;
    }

    public void setChildrenDropLevel(int childrenDropLevel) {
        this.childrenDropLevel = childrenDropLevel;
    }

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }

    @JsonIgnore
    public Child getChildrenUnknown() {
        if(children == null) children = new ArrayList<>();
        for (Child c: children) {
            if(c.getId() == -2){
                return c;
            }
        }
        Child result = new Child();
        result.setId(-2);
        result.setCollapsed(true);
        result.setImage("/img/avatar-default-unknown.png");
        result.addHTMLclass(ClassColor.people_chart_node);
        result.addHTMLclass(ClassColor.people_node_unknown);
        Text text = new Text();
        text.setName("Không rõ");
        text.setTitle("? - ?");
        result.setText(text);
        result.setChildren(new ArrayList<>());
        children.add(result);
        return result;
    }
}
