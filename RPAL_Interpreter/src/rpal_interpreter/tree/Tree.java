package rpal_interpreter.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import rpal_interpreter.cse_machine.cse_element.CseElement;
import rpal_interpreter.cse_machine.cse_element.SpecialCseElement;

public class Tree {
    /*
    
    This method will use to Evaluate the tree related methods.
    
    */
    String tree_list;

    public Tree(String input_list) {
        /*
        
        Parameters :- input_list = User input tree for the AST. This is read from user input file.
        
        */
        this.tree_list = input_list;
    }
    
    public static void print(Node root){
        /*
        This method is used to print the tree.
        */
        Node node;
        System.out.println(root.type + "    Parent :-   "+root.parent);

        for (Iterator<Node> it = root.children.iterator(); it.hasNext();) {
            node = it.next();
            Tree.print(node);
        }
    }
    
    public static Node createTree(ArrayList<Node> list)
    {
        /*
        This method is used to create the tree from Node list.
        Logic = Find the deepest element in the tree at each iteration and add it as a child to the previous element in the list
        */
        int min_ind;
        Node node,parent_node;
        ArrayList<Node> parent_children;
        while(list.size() != 1){
           // Get the deepest element in the list. 
           min_ind = list.indexOf(Collections.max(list));
           node = list.get(min_ind);            
           parent_node = list.get(min_ind - 1);
           node.parent = parent_node;
           parent_node.addChild(node);
           list.set(min_ind - 1,parent_node);
           list.remove(min_ind);
        }
        // Return the root of the tree
        return list.get(0);
    }
    
    public static Node getST(Node root){
        /*
        Parameters :- root - AST of the program
        This method is used to standarize the AST in bottom up manner.
        This method called standarizeTree method from top to botoom.
        */
        Node node,standatd_root;
        for (Iterator<Node> it = root.children.iterator(); it.hasNext();) {
            node = it.next();
            Tree.getST(node);
        }
        standatd_root =  Tree.standarizeTree(root);
        //Return the root of standarize root.
        return standatd_root;
    }
    
    public static Node standarizeTree(Node root){
        /*
        This method is used to standarize element in a tree.
        Parameters :- root - element of AST.
        */
        // Standarize let node
        if(root.type.equals("let")){
        Node equalNode = root.getByType(String.valueOf('='));
        Node equal_node_second_child,root_child_one;
        if(equalNode != null){
            root.type = "gamma";
            equal_node_second_child = equalNode.children.get(1);
            equalNode.type = "lambda";
            root_child_one = root.children.get(1);
            root_child_one.parent = equalNode;
            equal_node_second_child.parent = root;
            equalNode.children.set(1, root_child_one);
            root.children.set(1,equal_node_second_child);
            root.children.set(0,equalNode);
        }
       // Standarize where node 
    }else if(root.type.equals("where")){
        Node equalNode = root.getByType(String.valueOf('='));
        Node equal_node_second_child,root_child_zero;
        if(equalNode != null){
            root.type = "gamma";
            equal_node_second_child = equalNode.children.get(1);
            equalNode.type = "lambda";
            equal_node_second_child.parent = root;
            root_child_zero = root.children.get(0);
            root_child_zero.parent = equalNode;            
            equalNode.children.set(1, root_child_zero);
            root.children.set(1,equal_node_second_child);
            root.children.set(0,equalNode);
        }
        // Standarize function_form node
    }else if(root.type.equals("function_form")){
        int children_size;
        List<Node> arg_list;
        Node node,right_node,dynamic_node,last_arg,fn_name,lambda_node = root;
        children_size = root.children.size();
        arg_list = root.children.subList(1, children_size-1);
        Collections.reverse(arg_list);
        right_node = root.children.get(children_size - 1);
        fn_name = root.children.get(0);
        for (Iterator<Node> it = arg_list.iterator(); it.hasNext();) {
            node = it.next();
            lambda_node = new Node("lambda",new ArrayList<>(),-1,null,-1); 
            lambda_node.children.add(node);
            lambda_node.children.add(right_node);
            right_node.parent = lambda_node;
            node.parent = lambda_node;
            right_node = lambda_node;
        }
        root.children.clear();
        lambda_node.parent = root;
        root.children.add(fn_name);
        root.children.add(lambda_node);
        root.type = "=";
        
    }
     // Standarize lambda node
    else if(root.type.equals("lambda")){
        int children_num = root.children.size();
        List<Node> arg_list;
        Node node , lambda_node = Node.copy(root);
        if(children_num > 2){
            Node right_node;
            right_node = root.children.get(children_num - 1);
            arg_list = root.children.subList(0, children_num - 1);
            Collections.reverse(arg_list);
            for (Iterator<Node> it = arg_list.iterator(); it.hasNext();) {
                node = it.next();
                lambda_node = new Node("lambda",new ArrayList<>(),-1,null,-1); 
                lambda_node.children.add(node);
                lambda_node.children.add(right_node);
                right_node.parent = Node.copy(lambda_node);
                node.parent = Node.copy(lambda_node);
                right_node = Node.copy(lambda_node);
        }
            root.children.clear();
            lambda_node.parent = root;
            root.children = (lambda_node.children);
        }
         // Standarize within node
    }else if(root.type.equals("within")){
        Node left,right;
        left = root.children.get(0);
        right = root.children.get(1);
        if(left.type.equals("=") & right.type.equals("=") ){
            Node left_child_left,right_child_left,left_child_right,right_child_right,gamma_node,lambda_node;
            
            left_child_left = left.children.get(0);
            left_child_right = left.children.get(1);
            right_child_left = right.children.get(0);
            right_child_right = right.children.get(1);
            
            lambda_node = new Node("lambda",new ArrayList<>(),0,null,0);
            lambda_node.addChild(left_child_left);
            lambda_node.addChild(right_child_right);
            left_child_left.parent = lambda_node;
            right_child_right.parent = lambda_node;
            
            gamma_node = new Node("gamma",new ArrayList<>(),0,null,0);
            gamma_node.addChild(lambda_node);
            gamma_node.addChild(left_child_right);
            lambda_node.parent = gamma_node;
            left_child_right.parent = gamma_node;
            root.children.clear();
            root.type = "=";
            root.addChild(right_child_left);
            root.addChild(gamma_node);
            gamma_node.parent = root;
            right_child_left.parent = root;
        }
         // Standarize @ node
        }else if(root.type.equals("@")){
            Node left_child,middle_child,right_child,gamma_node;
            left_child = root.children.get(0);
            middle_child = root.children.get(1);
            right_child = root.children.get(2);
            gamma_node = new Node("gamma",new ArrayList<>(),0,null,0);
            gamma_node.addChild(middle_child);
            gamma_node.addChild(left_child);
            left_child.parent = gamma_node;
            middle_child.parent = gamma_node;
            gamma_node.parent = root;
            root.children.clear();
            root.type = "gamma";
            root.addChild(gamma_node);
            root.addChild(right_child);
            
        }
         // Standarize and node
        else if(root.type.equals("and")){
            if(root.checkUniqunessOfChildren("=")){
                Node tau_node,comma_node,arg,val;
                tau_node = new Node("tau",new ArrayList<>(),0,null,0);
                comma_node = new Node(",",new ArrayList<>(),0,null,0);
                int children_num = root.children.size();
                
                Node node;
                for (Iterator<Node> it = root.children.iterator(); it.hasNext();) {
                    node = it.next();
                    arg = node.children.get(0);
                    val = node.children.get(1);
                    arg.setParent(comma_node);
                    val.setParent(tau_node);
                    comma_node.addChild(arg);
                    tau_node.addChild(val);
        }
                root.type = "=";
                root.children.clear();
                root.addChild(comma_node);
                root.addChild(tau_node);
                comma_node.setParent(root);
                tau_node.setParent(root);
            }
            
        }
         // Standarize rec node
        else if(root.type.equals("rec")){            
            Node equalNode = root.getByType(String.valueOf('='));
            if(equalNode != null){
                Node equal_node_left, equal_node_left_copy , equal_node_right , gamma_node , y_node , lambda_node;
                equal_node_left = equalNode.getChildren().get(0);
                equal_node_left_copy = Node.copy(equal_node_left);
                equal_node_right = equalNode.getChildren().get(1);
                
                gamma_node = new Node("gamma",new ArrayList<>(),0,null,0);
                y_node = new Node("Y",new ArrayList<>(),0,null,0);
                lambda_node = new Node("lambda",new ArrayList<>(),0,null,0);
                
                equal_node_left_copy.setParent(lambda_node);
                lambda_node.addChild(equal_node_left_copy);
                lambda_node.addChild(equal_node_right);
                equal_node_right.setParent(lambda_node);
                gamma_node.addChild(y_node);
                y_node.setParent(gamma_node);
                gamma_node.addChild(lambda_node);
                lambda_node.setParent(gamma_node);
                root.setType("=");
                root.children.clear();
                root.addChild(equal_node_left);
                equal_node_left.setParent(root);
                root.addChild(gamma_node);
                gamma_node.setParent(root);
                
            }
            
        }
        return root;   
    }
    
    public static HashMap<Integer,ArrayList<CseElement>> preOrderTraversal(Node root){
        /*
        This method is used to get the Control structures from the standrize tree.
        This method will called the preOrderTraversal(Node root , HashMap<Integer,ArrayList<CseElement>> preorder_elements ,int index) method
        parameters - root => root of the standarize tree.
        */
        HashMap<Integer,ArrayList<CseElement>> preorder_elements = new HashMap<>();
        ArrayList<CseElement> cse_element_array = new ArrayList<>();
        preorder_elements.put(0,cse_element_array);
        return Tree.preOrderTraversal(root , preorder_elements , 0);
    }
    
    public static HashMap<Integer,ArrayList<CseElement>> preOrderTraversal(Node root , HashMap<Integer,ArrayList<CseElement>> preorder_elements ,int index){
        /*
        This method is used to get the Control structures from the standrize tree.
        Parameters :- root => root of the standarize tree.
                      preorder_elements - HashMap to store control structures.
                      index => current number of control structure 
        */
        Node node;
        String root_type = root.type;
        int type_len;
        String type,value;
        CseElement root_cse_element;
        ArrayList<CseElement> cse_elements;
        /*
        root_type - type of the root
        type - new type of the root after change
        value - value of the root
        root_cse_element - relevant CseElement object reference
        */
        // Edit the type and values of elements in the tree according to the CseElement object
        int length_root_type = root_type.length();
        if(root_type.equals("<true>")){
                type = "boolean";
                value = "true";
            }
            else if(root_type.equals("<false>")){
                type = "boolean";
                value = "false";
            }
            else if(root_type.equals("<nil>")){
                type = "nil";
                value = "nil";
            }
            else if(root_type.equals("<dummy>")){
                type = "dummy";
                value = "dummy";
            }
            // Adjust strings Identifiers Integers according to CseElement structure.
            else if("<".equals(String.valueOf(root_type.charAt(0)))){
		String contain = root_type.substring(1,root_type.length() - 1);
		String[] arr = contain.split(":");
		type = arr[0];
                if("STR".equals(type) || "STRING".equals(type)){
                    type_len = arr[1].length();
                    if("''".equals(arr[1])){
                    value = "";
                }
                    else{
                        value = arr[1].substring(1,type_len - 1);
                    }
                    
                    //Avoid escape characters
                    if("\\n".equals(value)){
                        value = "\n";
                    }
                    if("\\t".equals(value)){
                        value = "\t";
                    }
                }
                else{
                    value = arr[1];
                }
                
        }
         else{
            type = root_type;
            value = null;
        }
        // Create the control structures for conditional node.
        if("->".equals(root_type)){
            Node left_child,right_child,middle_child;
            CseElement left,right,beta;
            left_child = root.getChildren().get(0);
            right_child = root.getChildren().get(2);
            middle_child = root.getChildren().get(1);
            
            int new_index = preorder_elements.size() - 1;
            // Create beeta element and add it to the current control structure. And create new two structures and add else and then statement's pre order traversal.
            beta = new CseElement("Beeta",String.valueOf(new_index + 1) + "," + String.valueOf(new_index + 2));
            cse_elements = preorder_elements.get(index);
            cse_elements.add(beta);
            Tree.preOrderTraversal(left_child ,preorder_elements ,index);
            preorder_elements.put(index,cse_elements);
            new_index = preorder_elements.size() - 1;
            ArrayList<CseElement> cse_element_array = new ArrayList<>();            
            preorder_elements.put(new_index+1,cse_element_array);
            ArrayList<CseElement> cse_element_array_1 = new ArrayList<>();            
            preorder_elements.put(new_index+2,cse_element_array_1);
            Tree.preOrderTraversal(middle_child ,preorder_elements ,new_index+1);
            Tree.preOrderTraversal(right_child ,preorder_elements ,new_index+2);
            
        }
        // Create new control structure for lambda element.
        else if("lambda".equals(root_type)){
            //env,csnum,boundedvar
            Node lamda_left_child,lamda_right_child;
            ArrayList<CseElement> bounded_type = new ArrayList<>();
            lamda_left_child = root.getChildren().get(0);
            lamda_right_child = root.getChildren().get(1);
            String left_type = lamda_left_child.type;
            // if left element of the element is ',' node. Then add the bounded variables as children in comma nodes.
            if(left_type.equals(",")){
                for (Iterator<Node> it = lamda_left_child.getChildren().iterator(); it.hasNext();) {
                    node = it.next();
                    String node_type =  node.type;
                    CseElement bounded_var = new CseElement(node_type.substring(1,node_type.length() - 1).split(":")[1],null);
                    bounded_type.add(bounded_var);
                
            }
            }
            // if left element of the element is not a ',' node. Then add it to the lambda's bounded variable.
            else{
                CseElement bounded_var = new CseElement(left_type.substring(1,left_type.length() - 1).split(":")[1],null);
                bounded_type.add(bounded_var);
                
        }
            // create a new control structure and add it to the current structures.
            int new_index = preorder_elements.size() - 1;
            root_cse_element = new SpecialCseElement(type,value,null,new_index+1,bounded_type);
            cse_elements = preorder_elements.get(index);        
            cse_elements.add(root_cse_element);
            preorder_elements.put(index,cse_elements);
            ArrayList<CseElement> cse_element_array = new ArrayList<>();            
            preorder_elements.put(new_index+1,cse_element_array);
            Tree.preOrderTraversal(lamda_right_child ,preorder_elements ,new_index+1);
            
        }
        
               
        else{
            
            root_cse_element = new CseElement(type,value);
            if("tau".equals(root_type)){
                // Change the tau element's size as it's children number.
                root_cse_element.children_size = root.children.size();
            
        }
            cse_elements = preorder_elements.get(index);        
            cse_elements.add(root_cse_element);
            preorder_elements.put(index,cse_elements);
            //Do the pre order traversal for element's children    
            for (Iterator<Node> it = root.getChildren().iterator(); it.hasNext();) {
                node = it.next();
                Tree.preOrderTraversal(node ,preorder_elements,index);
        }
        }
        // return control structures.
        return preorder_elements;
    }
    
}
