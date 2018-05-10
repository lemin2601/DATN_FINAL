package com.leemin.genealogy.config.tree;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    private List<Child> children;




    public Child() {

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
}
