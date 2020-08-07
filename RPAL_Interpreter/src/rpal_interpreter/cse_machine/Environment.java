package rpal_interpreter.cse_machine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import rpal_interpreter.cse_machine.cse_element.CseElement;
import rpal_interpreter.tree.Node;


public class Environment{
    /*
    This method is used to create the environment tree nodes for CSE Machine.
    */
    public int number;
    public HashMap<String,CseElement> identifiers;
    public Environment parent;
    public ArrayList<Environment> children;
    
    

    public Environment(int number, Environment parent) {
        /*
        Attributes:-
            number => Number of the environment
            identifiers => Identifier values that known by the environment. ex:- x = 4
            parent => parent of the environment
            children => Children of the environment tree node
        */
        this.number = number;
        this.parent = parent;
        this.children = new ArrayList<>();
        this.identifiers =new HashMap<>();
    }
    
    public void addIdentifier(String key , CseElement value){
        /*
        This method is used to add a new identifier(key,value argument pair) to the environment identifiers.
        Parameters - key => key for the new identifier
                     value => CseElement that will set for the value to the identifier
        */
        identifiers.put(key, value);
    }
    
    public CseElement checkIdentifiers(String key){
//        parameter - key => The name of the identifier that want to check
        // This method is used to check a identifier is exist by key of the identifier in the environment
            // Return the identifier if it exist. else return null.
        try{
            return getIdentifier(key);
        }
        catch(Exception ex){
            return null;
        }
    }
    
    public CseElement getIdentifier(String key) throws Exception{
        /*
        This method will return the identifier value.
        Argument:- key => key/name of the identifier that want to find.
        */
        CseElement result = null;
        if(this.identifiers.containsKey(key)){
            //Get the identifier value if this environment knows the value
            result =  identifiers.get(key);
        }
        else if(null != this.parent){
            //Check the parent if this environment doesn't know the value.
            result =  this.parent.getIdentifier(key);
        }
        if(result !=null){
            //If the found identifier value is another identifier, following code will find the real value of the identifier.
            // ex:- x = y, find the value of y.
        if("ID".equals(result.type)){
            try{
                if(null != this.parent){
                   return this.parent.getIdentifier(result.value); 
                }
                else{
                    return result;
                }
                
            }
            catch(Exception ex){
                return result;
            }
        }
        else{
            return result;
        }
        
    }
        // Throw a exception if identifier doesn't exists
        throw new Exception(key+" is not defined.");
    }
    
    public void createIdentifiers(HashMap<CseElement,CseElement> bounded_val){
        /*
        This method will create new identifiers for the environment as key value pair.
        Parameter :- bounded_val => HashMap contains the identifiers as key value pairs.
        */
        CseElement node;
        Iterator<Entry<CseElement,CseElement>> it = bounded_val.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<CseElement,CseElement> pair;
                        pair = (Map.Entry<CseElement,CseElement>) it.next();
                        identifiers.put(pair.getKey().type, pair.getValue());
		}
        
    }
    
    
    
    public void addChild(Environment child) {
        /*
        This method will add new child to the Environment
        */
        this.children.add(child);
    }
    //Getters and setters for attributes.
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public HashMap<String, CseElement> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(HashMap<String, CseElement> identifiers) {
        this.identifiers = identifiers;
    }
    @Override
    public String toString() {
        if(this.parent != null){
        return "Env Number:- "+this.number+"Parent :- "+this.parent.identifiers + "Bound values:- "+this.identifiers; //To change body of generated methods, choose Tools | Templates.
    }
        else{
                    return "Env Number:- "+this.number+ "Bound values:- "+this.identifiers; //To change body of generated methods, choose Tools | Templates.

        }
        
    }

    public Environment getParent() {
        return parent;
    }

    public void setParent(Environment parent) {
        this.parent = parent;
    }

    public ArrayList<Environment> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Environment> children) {
        this.children = children;
    }
    public static Environment copy(Environment node){
        /*
        This method used to create a copy of Environment object
        */
        try {
            return (Environment)node.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error happen while cloning");
            return null;
        }
    }
    
    
}

