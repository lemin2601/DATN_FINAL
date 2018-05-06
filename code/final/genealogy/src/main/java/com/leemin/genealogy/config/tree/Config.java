package com.leemin.genealogy.config.tree;
import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class Config extends ConfigTree{
    private String container = "#collapsable-example";
    private  boolean animateOnInit = true;
    private Node node;
    private Animation animation;

    public Config() {
        node = new Node();
        animation = new Animation();
    }


    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public boolean isAnimateOnInit() {
        return animateOnInit;
    }

    public void setAnimateOnInit(boolean animateOnInit) {
        this.animateOnInit = animateOnInit;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }
}
