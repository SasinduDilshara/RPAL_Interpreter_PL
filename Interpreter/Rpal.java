package rpal;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Rpal {
    public static void main(String[] args) {
        /*
        Main method for RPAL interpreter.
        */
        //File name => User Input
        String filename = "";
        Node root,standard_root;
        CseElement result;
        HashMap<Integer,ArrayList<CseElement>> control_structures;
        //Initialize variable DEBUG. True when debugging.
        boolean DEBUG = false;
        ArrayList<Node> outputs;
        //Get the filename from argument
        if(DEBUG == false & args.length ==0){
            System.out.println("Filename expected!..");
        }
        else{
            filename = args[0];
        
                
        //Create the AST tree from user input file
        outputs = IOEditor.getList(filename);
        root = Tree.createTree(outputs);
        if(DEBUG == true){
            //Print and check the AST when debugging
            Tree.print(root);
            System.out.println("\n"); 
        }
        //Get the Standaraize tree.
        standard_root = Tree.getST(root);
        if(DEBUG == true){
            //Print and check the standarize tree when debugging
            Tree.print(standard_root);
        }
        //Create the control structures for rpal code
        control_structures = Tree.preOrderTraversal(root);
        if(DEBUG == true){
            //Print and check the control structures when debugging
            System.out.println(control_structures);
            System.out.println("");
        }
        // Evaluate the RPAL code using CSE machine
        CseMachine cm = new CseMachine(control_structures);
        try {
            //Get the resulted output
            result = cm.apply(DEBUG);
        if(result == null){
            if(DEBUG == true){
                //Check the result when debugging
                System.out.println("Final Result:- No return value");
            }
        }
        else if("env".equals(result.type)){
            if(DEBUG == true){
                //Check the result when debugging
                System.out.println("Final Result:- No return value");
            }
        }
        else{
            if(DEBUG == true){
                //Check the result when debugging
                System.out.println(result.type +"  "+result.value);
            }
        }
        } catch (Exception ex) {
            System.out.println("\n\n"+ex+"\n\n");
            Logger.getLogger(Rpal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    }
}













