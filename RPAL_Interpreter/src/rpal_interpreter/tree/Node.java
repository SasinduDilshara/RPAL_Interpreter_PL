package rpal_interpreter.tree;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Node implements Comparable<Node>,Cloneable{
    /*
    This class is used to create objects for create the AST object tree from user input string.    
    */
    String type;
    ArrayList<Node> children;
    int number;
    Node parent;
    int depth;

    public Node(String type, ArrayList<Node> children, int number, Node parent,int depth) {
        /*
        Attributes:-
            - type => Type of the Node ex:- Identifier.
            - children => Children of the node. Children of the tree node.
            - number => number labeled for the node.
            - parent => parent of the node. Parent of the tree node.
            - depth => depth of the node in the tree.
        */
        this.type = type;
        this.children = children;
        this.number = number;
        this.parent = parent;
        this.depth = depth;
    }
    //Setters and getters for the node.
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Node> children) {
        this.children = children;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
    public void addChild(Node child) {
        this.children.add(child);
    }
    
    public Node getByType(String type)
    {
        //This method is used to find a children of a node by type.
        Node node;
        for (Iterator<Node> it = this.children.iterator(); it.hasNext();) {
            node = it.next();
            if(node.type.equals(type)){
                return node;
            }
        }
            return null;
    }

    public boolean checkUniqunessOfChildren(String type){
        //This method Check if node has same type of children
        Node node;
        for (Iterator<Node> it = this.children.iterator(); it.hasNext();) {
            node = it.next();
            if(!node.type.equals(type)){
                return false;
            }
        }
            return true;
    
    }
    
    public static Node copy(Node node){
        //This method create a copy of the Node object.
        try {
            return (Node)node.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error happen while cloning");
            return null;
        }
    }
    @Override
    public String toString() {
        return this.type; 
    }

    @Override
    public int compareTo(Node o) {
        //This method used to compare two nodes. Comparing is done according to the depth.
        return this.depth - o.depth;
    }
    
}