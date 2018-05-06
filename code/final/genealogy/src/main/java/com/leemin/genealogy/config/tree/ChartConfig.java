package com.leemin.genealogy.config.tree;

import com.fasterxml.jackson.annotation.JsonInclude;

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
        if(nodeStructure == null){
            nodeStructure = child;
            return;
        }

        String keyParent = child.getParentKey();
        Child parent = getParentFromNode(getNodeStructure(),keyParent);

        if(parent != null) {
            List<Child> children = parent.getChildren();
            if(children == null){
                children = new ArrayList<>();
            }
            children.add(child);
            parent.setChildren(children);
        }else{
            System.out.println("null parrent");
//            List<Child> children = nodeStructure.getChildren();
//            if(children == null) children = new ArrayList<>();
//            children.add(child);
//            nodeStructure.setChildren(children);
        }
    }

    private Child getParentFromNode(Child child,String keyParent) {
        if(keyParent.equalsIgnoreCase(child.getParentKey() +"_" + child.getId())){
            return child;
        }
        else{
            List<Child> children = child.getChildren();
            if(children == null) return null;
            for (Child c: children) return getParentFromNode(c, keyParent);
        }
        return null;
    }
}
