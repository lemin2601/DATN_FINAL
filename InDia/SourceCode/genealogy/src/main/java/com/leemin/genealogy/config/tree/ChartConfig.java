package com.leemin.genealogy.config.tree;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.leemin.genealogy.data.ClassColor;
import com.leemin.genealogy.data.GioiTinh;
import com.leemin.genealogy.data.QuanHe;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChartConfig {
    private Config chart;
    private Child nodeStructure;

    public ChartConfig() {
        chart = new Config();
//        nodeStructure = new Child();
    }

    public Config getChart() {
        return chart;
    }

    public void setChart(Config chart) {
        this.chart = chart;
    }

    public Child getNodeStructure() {
        return nodeStructure;
    }

    public void setNodeStructure(Child nodeStructure) {
        this.nodeStructure = nodeStructure;
    }

    public void addChild(Child child) {
        if (nodeStructure == null) {
            nodeStructure = child;
            return;
        }
        String keyParent = child.getParentKey();
        Child parent = getParentFromNode(getNodeStructure(), keyParent);
        if (parent != null) {
            List<Child> children = parent.getChildren();
            if (children == null) {
                children = new ArrayList<>();
            }
            if (parent != nodeStructure) { parent.setCollapsed(true); }
            children.add(child);
            parent.setChildren(children);
        }
        else {
            System.out.println("null parrent");
//            List<Child> children = nodeStructure.getChildren();
//            if(children == null) children = new ArrayList<>();
//            children.add(child);
//            nodeStructure.setChildren(children);
        }
    }

    public void addChildHaveMother(Child child) {
        //if null ==> create
        if (nodeStructure == null) {
            child.addHTMLclass(ClassColor.people_node_root);
            nodeStructure = child;
            return;
        }

        String keyParent = child.getParentKey();
        Child parent = getParentFromNode(getNodeStructure(), keyParent);
        if (parent != null) {
            List<Child> children ;
            //co quan he vo/chong ==> con gan nhat
            QuanHe quanHe = QuanHe.values()[child.getRelation()];
            switch (quanHe){
                case CHA:
                case ME:
                    child.addHTMLclass(GioiTinh.values()[child.getGender()]== GioiTinh.NAM ? ClassColor.people_node_son:ClassColor.people_node_daughter);
                    Long idMother = child.getIdMother();
                    if(idMother != null) {
                        parent = getMotherFromNode(parent,idMother);
                        if(parent!= null){
                            children = parent.getChildren();
                            if (children == null) {
                                children = new ArrayList<>();
                            }
                            if (parent != nodeStructure) { parent.setCollapsed(true); }
                            children.add(child);
                            parent.setChildren(children);
                        }
                    }else{
                        Child childUnknown = parent.getChildrenUnknown();
                        childUnknown.setHTMLid(parent.getHTMLid());
                        childUnknown.getChildren().add(child);
                    }
                    break;
                case CHONG:
                case VO:
                    child.addHTMLclass(GioiTinh.values()[child.getGender()]== GioiTinh.NAM ? ClassColor.people_node_husband:ClassColor.people_node_wife);
                    children = parent.getChildren();
                    if (children == null) {
                        children = new ArrayList<>();
                    }
//                    if (parent != nodeStructure) { parent.setCollapsed(true); }
                    children.add(child);
                    parent.setChildren(children);
                    break;
            }
//            parent.setChildren(children);
        }
        else {
            System.out.println("null parrent");
        }
    }

    private Child getParentFromNode(Child child, String keyParent) {
        if (keyParent.equalsIgnoreCase(child.getParentKey() + "_" + child.getId())) {
            return child;
        }
        else {
            List<Child> children = child.getChildren();
            if (children == null) { return null; }
            Child result = null;
            for (Child c : children) {
                result = getParentFromNode(c, keyParent);
                if (result != null) { break; }
            }
            return result;
        }
    }


    private Child getMotherFromNode(Child child,Long idMother) {
        if(idMother == null || child.getId() == idMother) return child;
        else{
            List<Child> children = child.getChildren();
            if (children == null) { return null; }
            Child result = null;
            for (Child c : children) {
                result = getMotherFromNode(c, idMother);
                if (result != null) { break; }
            }
            return result;
        }
    }
}
