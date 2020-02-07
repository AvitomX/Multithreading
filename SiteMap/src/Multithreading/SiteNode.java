package Multithreading;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class SiteNode {
    private String name;
    private Set<SiteNode> children;

    public SiteNode(String name) {
        this.name = name;
        children = new HashSet<>();
    }

    public static ArrayList<String> getAllChildren(SiteNode node, String intent) {
        ArrayList<String> list = new ArrayList<>();
        list.add(intent + node.getName());
        if (!node.getChildren().isEmpty()) {
            for (SiteNode child : node.getChildren()) {
                for (String value : getAllChildren(child, intent + '-')){
                    list.add(value);
                }
            }
        }
        return list;
    }

    public String getName() {
        return name;
    }

    public Set<SiteNode> getChildren() {
        return children;
    }

    public synchronized void addChild(SiteNode child){
        children.add(child);
    }
}
