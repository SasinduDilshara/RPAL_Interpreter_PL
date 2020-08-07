package rpal_interpreter.tree;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class IOEditor{
    /*
    This class is used to evaluate user inputs.
    */
    static int dotcount(String word){
        /*
        return the number of leading dots in the word.
        */
        int dots = 0;
        word = word.trim();
        for (int i = 0; i < word.length(); i++){
        char c = word.charAt(i);
        if(c != '.'){
            return dots;
        }
        dots+=1;
        
            }
        return dots;
    }
    
    public static ArrayList<Node> getList(String filename){
        /*
        return node objects for user input tree.
        input - filename
        Output - ArrayList of node containing the eacch node of AST.
        */
        ArrayList<Node> outputs = new ArrayList<>();
        BufferedReader stdin;
        try {
            stdin = new BufferedReader(new FileReader(filename));
            String line;
            try {
                int line_count = 0;
                int num_dot;
            while ((line = stdin.readLine()) != null) {
                line = line.trim();                
                num_dot = IOEditor.dotcount(line);
                outputs.add(new Node(line.substring(num_dot),new ArrayList<>(),line_count,null,num_dot));
                line_count = line_count + 1;
            }
            
        return outputs;
        } catch (IOException ex) {
            System.out.println("Error Ocuur when reading input");
        }
        } catch (FileNotFoundException ex) {
            System.out.println("File "+filename+" was not found..");
        }
        return null;
    }
    
}