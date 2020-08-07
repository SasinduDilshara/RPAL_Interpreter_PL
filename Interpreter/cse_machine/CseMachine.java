class CseMachine{
    /*
    
    Class for CSE Machine.
    This Class has attributes of control structures , Stack ,Environments
    This class evaluate control structures by using 12 CSE rules.
    */
    public Environment current_env;
    public HashMap<Integer,ArrayList<CseElement>> control_structures;
    public Stack control;
    public Stack element_stack;
    public ArrayList<Environment> env_list;
    public List<String> builtins_unops;
    public List<String> builtins_binops;
    public List<String> builtins_otherfuncs;
    public int env_num = 0;
    

    public CseMachine( HashMap<Integer, ArrayList<CseElement>> control_structures) {
        /*
        
        current_env = Store the current environment of CSE machine.
        env_list = Store the list of environments currently active in the stack.
        control_structures = Store the all control structures for the input code.
        control = currently active control structure on that moment
        element_stack = Stack of the CSE machine
        builtins_unops = Store the names of unary functions
        builtins_binops = Store the names of binary functions
        builtins_otherfuncs = Store the names of other functions. Ex:- Print
        
        */
        this.current_env = new Environment(0,null);
        this.env_list = new ArrayList<>();
        this.env_list.add(current_env);
        this.control_structures = control_structures;
        this.control = new Stack();
        this.element_stack = new Stack();
        add_env_element(0);
        this.control.addList(this.control_structures.get(0)); 
        builtins_unops = Arrays.asList( "Stem", "Stern","Isinteger","Istruthvalue","Isstring","Istuple","Isfunction","Isdummy","Order","not","neg","ItoS");
        builtins_binops = Arrays.asList("or","&","eq","ne","+","-","*","/","**","ls","gr","<","le","ge",">",">=","<=","Conc","aug");
        builtins_otherfuncs = Arrays.asList("Print");
    }

    
        
    public CseElement apply(boolean DEBUG) throws Exception{
        /*
        
        Apply CSE machine rules and evaluate control structures by step by step.
        Return CseElement if last element remaining the stack.
        
        */
        //First move
        move(DEBUG);
        while(this.element_stack.getSize() > 1){
            //Apply cse rules for iterations until stack remain one element or empty.
            move(DEBUG);
        }
        if(this.element_stack.getSize() == 0){
            //Return null for empty stack
            return null;
        }
        //return the remaining element in stack
        return this.element_stack.getFirst();
    }
    public void move(boolean DEBUG) throws Exception{
        
        /*
        
        In this method apply single CSE Machine rule for control structure and stack.
        
        */
        /*
        
        control_last = Last element in the current control
        element_stack_last = last element in the stack
        element_stack_second_last = second last element in the stack
        element_stack_last_copy = copy of element_stack_last
        element_stack_second_last_copy = copy of element_stack_second_last
        
        */
        if(DEBUG){
            System.out.println("\n");
            System.out.println("Control ");
            System.out.println(this.control);
            System.out.println("Stack ");
            System.out.println(this.element_stack);
            System.out.println("Environment");
            System.out.println(this.current_env);
        }
                        
        CseElement control_last,element_stack_last,element_stack_last_copy,element_stack_second_last,element_stack_second_last_copy,name_obj;
        control_last = this.control.pop();        
        element_stack_last = this.element_stack.pop();
        element_stack_second_last = null;
        if(this.element_stack.getSize() >=1){
        element_stack_second_last = this.element_stack.pop();
        }
        if(element_stack_last != null){
            element_stack_last_copy = element_stack_last;
        }
        else{
            element_stack_last_copy = new CseElement("","");
        }
        if(element_stack_second_last != null){
            element_stack_second_last_copy = element_stack_second_last;
        }
        else{
            element_stack_second_last_copy = new CseElement("","");
        }
        
        //CSE Machine rule number 5. (exit env)
        if(control_last.type.equals("env") & control_last.equals(element_stack_second_last)){
            this.element_stack.push(element_stack_last);
               //Get the nearest environment in control
               CseElement new_env = this.element_stack.findElement("env");
               if(new_env != null){
                    //Remove current environment from list
                    this.env_list.remove(this.current_env);
                    //Change current environment.
                    setEnv(getEnvironmentByElement(new_env));
               }
        }
        //CSE Machine rule 9 (tuple formation)
        else if(control_last.type.equals("tau"))
        {
            control_last.tau_values.clear();
            if(element_stack_second_last != null){
                this.element_stack.push(element_stack_second_last);
            }
            if(element_stack_last != null){
                this.element_stack.push(element_stack_last);
            }
            // Adding children to the tuple
            for(int i = 0;i<control_last.children_size;i++){
                control_last.tau_values.add(this.element_stack.pop());
            }
            // Push the tuple to stack
            this.element_stack.push(control_last);
        }
        // CSE Machine rule 10 (tuple selection)
        else if(element_stack_last_copy.type.equals("tau") & "INT".equals(element_stack_second_last_copy.type) & "gamma".equals(control_last.type)){
            //Apply tuple slice function and push the result
            CseElement result = apply_binfuncs("slice",element_stack_last,element_stack_second_last);
            this.element_stack.push(result);
        } 
        // CSE Machine rule number 1. (Stack a name)
        else if(control_last.type.equals("ID") || this.builtins_unops.contains(control_last.type) || this.builtins_binops.contains(control_last.type) || this.builtins_otherfuncs.contains(control_last.type) ){
            String type,value,func_name;
            CseElement result;
            CseElement key_val;
            type = control_last.type;
            value = control_last.value;
            //Initialize func name by the ID name of the element.
            if(control_last.type.equals("ID")){
                func_name = value;
            }
            else{
                func_name = type;
            }
            //Check the identifier from the current environment and its parents
            key_val = this.current_env.checkIdentifiers(control_last.value);
            //Check If identifier has been found by the current environment or its parents;
            if(key_val != null){
                    // Push the value to stack.
                    if(element_stack_second_last != null){
                        this.element_stack.push(element_stack_second_last);
                    }
                    if(element_stack_last != null){
                        this.element_stack.push(element_stack_last);
                    }
                    
                    this.element_stack.push(key_val);
        
            }
//            if identifier name in one of RPAL function. CSE Machine  rule 3(apply rator)
//            Check identifier name contain a name of a unary function
            else if(this.builtins_unops.contains(func_name)){
                //get the result by applying unary function
               result = apply_unifuncs(func_name,element_stack_last);
               if(control_last.type.equals("ID")){
                   this.control.pop();
               }
               if(element_stack_second_last != null){
                this.element_stack.push(element_stack_second_last);
            }
               //Push the result
               this.element_stack.push(result);
            }
//             Check identifier name contain a name of a binary function
            else if(this.builtins_binops.contains(func_name)){
                //get the result by applying binary function
               result = apply_binfuncs(func_name,element_stack_last,element_stack_second_last);
               if(control_last.type.equals("ID")){
                   this.control.pop();
                   this.control.pop();
               }
               //push the result
               this.element_stack.push(result);
            }
            
//             Check identifier name contain a name of a other function
            else if(this.builtins_otherfuncs.contains(func_name)){
                //get the result by applying other function
               result = apply_specialfuncs(func_name,element_stack_last);
               if(control_last.type.equals("ID")){
                   this.control.pop();
               }
               
            }
            else{
                //Throw a exception if identifier is not found
                throw new Exception("Key is not found:- "+control_last.type);
            }
 
    }
        else if(control_last.type.equals("dummy") || control_last.type.equals("nil") || control_last.type.equals("boolean") || control_last.type.equals("INT") || control_last.type.equals("STR") || control_last.type.equals("INTEGER") || control_last.type.equals("STRING")){
            //Push the value to the stack if control last element is element of RPAL define data type.
            if(element_stack_second_last != null){
                this.element_stack.push(element_stack_second_last);
            }
            if(element_stack_last != null){
                this.element_stack.push(element_stack_last);
            }
            this.element_stack.push(control_last);
        }
//        CSE Machine rule number 2 (Stack lambda)
        else if(control_last.type.equals("lambda")){
            if(element_stack_second_last != null){
                this.element_stack.push(element_stack_second_last);
            }
            if(element_stack_last != null){
                this.element_stack.push(element_stack_last);
            }
            //Set current environment to lambda and push to the stack
            ((SpecialCseElement)(control_last)).cur_env = this.current_env;
            this.element_stack.push(control_last);
        }
        // CSE Machine rule number 4 (apply lambda)
        else if(control_last.type.equals("gamma") & element_stack_last.type.equals("lambda")){
            //change the environment number
             this.env_num += 1;
             // Create a new environment
             Environment new_env = new Environment(this.env_num,this.current_env);
             // Bind values for new environment
            if(element_stack_second_last.type.equals("tau")){
               if(((SpecialCseElement)element_stack_last).bounded_var.size() == 1){
                   // Bind tuple to a variable. ex:= x = (1,2,3)
                   ((SpecialCseElement)element_stack_last).bind(element_stack_second_last);
                   //Bind identifiers to current environment
                    new_env.createIdentifiers(((SpecialCseElement)element_stack_last).bounded_val);
               } 
                else{
                   //CSE Machine Rule number 11(n-aray functions).
                    ((SpecialCseElement)element_stack_last).bind(element_stack_second_last.tau_values);
                    //Bind identifiers to current environment
                    new_env.createIdentifiers(((SpecialCseElement)element_stack_last).bounded_val);
            }
            }
            else{
                //Non tuple binding
                ((SpecialCseElement)element_stack_last).bind(element_stack_second_last);
                //Bind identifiers to current environment
                new_env.createIdentifiers(((SpecialCseElement)element_stack_last).bounded_val);
            }
            //Set the new environment as current environment
            this.setEnv(new_env);
            this.env_list.add(new_env);
            add_env_element(this.env_num);
            //Set the parent to new environment as lamda's creating environment
            new_env.setParent(((SpecialCseElement)element_stack_last).getCurrent_environment());
            //Add the corresponding control structure to control 
            ArrayList<CseElement> new_cs = this.control_structures.get(((SpecialCseElement)element_stack_last).cs_number);
            this.control.addList(new_cs);
        }
        //CSE Machine rule 8(Conditional)
        else if("Beeta".equals(control_last.type)){
            String arr[] = control_last.value.split(",");
            // get the index of the control structure if stack value is true
            int then_index = Integer.parseInt(arr[0]);
            // get the index of the control structure if stack value is false
            int else_index = Integer.parseInt(arr[1]);
            // Print error when type is not boolean
            if(!"boolean".equals(element_stack_last.type)){
                System.out.println("Invalid arguments for boolean operator!");                
            }
            else{
                ArrayList<CseElement> new_cs;
                if(Boolean.parseBoolean(element_stack_last.value)){
                    // get the control structure when condition true
                    new_cs = this.control_structures.get(then_index);
                    // Add the control structure when condition true
                    this.control.addList(new_cs);
                }
                else{
                    new_cs = this.control_structures.get(else_index);
                    // Add the control structure when condition false
                    this.control.addList(new_cs);;
                }
            }
            if(element_stack_second_last != null){
                this.element_stack.push(element_stack_second_last);
            }
        }
//        CSE Machine rule 12 (Applying Y)
        else if(element_stack_last_copy.type.equals("Y") & "lambda".equals(element_stack_second_last_copy.type) & "gamma".equals(control_last.type)){
            //Create the corresponding neeta element.
            SpecialCseElement neeta_element = new SpecialCseElement("neeta",element_stack_second_last.value,((SpecialCseElement)element_stack_second_last).cur_env,((SpecialCseElement)element_stack_second_last).cs_number,((SpecialCseElement)element_stack_second_last).bounded_var,(SpecialCseElement)element_stack_second_last);
            //Bind identifiers to neeta.
            neeta_element.bounded_val = ((SpecialCseElement)element_stack_second_last).bounded_val;
            //Push neeta to stack
            this.element_stack.push(neeta_element);
            
        }
//        CSE Machine rule 13 (applying f.p)
        else if(control_last.type.equals("gamma") & "neeta".equals(element_stack_last_copy.type)){
            ArrayList<CseElement> bounds = new ArrayList<>();
            bounds.add(element_stack_last);
            if(element_stack_second_last != null){
                this.element_stack.push(element_stack_second_last);
            }
            if(element_stack_last != null){
                this.element_stack.push(element_stack_last);
            }
            // create the new lambda element
            SpecialCseElement lambda_element = new SpecialCseElement("lambda",null,((SpecialCseElement)element_stack_last).cur_env,((SpecialCseElement)element_stack_last).cs_number,((SpecialCseElement)element_stack_last).bounded_var);
            if(control_last != null){
                this.control.push(control_last);
            }
            //push the lambda element and new gamma element.
            this.element_stack.push(lambda_element);
            this.control.push(new CseElement("gamma",null));
        }
        //Default situation. Pop and push control last element
        else{
            if(element_stack_second_last != null){
                this.element_stack.push(element_stack_second_last);
            }
            if(element_stack_last != null){
                this.element_stack.push(element_stack_last);
            }
            this.element_stack.push(control_last);
        }
                
    }
    
    
    public Environment getEnvironmentByElement(CseElement env_element){
        /*
        
        This method will select a environment from environment list by using env_element's environment number.
        Parameters : - * env_element => type - CseElement
        Return type Environment..
        
        */
        Environment env;
        for (Iterator<Environment> it = this.env_list.iterator(); it.hasNext();) {
            env = it.next();
            // Take the environment element match.
            if(env_element.value.equals(String.valueOf(env.number))){
                return env;
            }            
        }
        return null;
    }

    
    public CseElement apply_binfuncs(String func,CseElement arg_element1,CseElement arg_element2){
        /*
        
        parameters :- func => type String  --Function name. 
                      arg_element1 => type - CseElement -- Left argument to the binary function
                      arg_element2 => type - CseElement -- right argument to the binary function
        This function is used to apply arg_element1 , arg_element2 to binary function and return the result.
        
        */
        try{
            //val is used to get the output result value.
            //type is used to get the output result type
            String arg1,arg2,val = null,type = null;
            // Get the argument values
            arg1 = arg_element1.value;
            arg2 = arg_element2.value;
            // Following conditions will apply relavant binary functions by function name.
            if(func.equals("or")){
                val =  String.valueOf(TruthValueData.or(Boolean.parseBoolean(arg1),Boolean.parseBoolean(arg2)));
                type = "boolean";
            }
            else if(func.equals("&")){
                val = String.valueOf(TruthValueData.and(Boolean.parseBoolean(arg1),Boolean.parseBoolean(arg2)));
                type = "boolean";
            }
            else if(func.equals("eq")){
                val = String.valueOf(Data.eq(arg_element1,arg_element2));
                type = "boolean";
            }
            else if(func.equals("ne")){
                val = String.valueOf(Data.ne(arg_element1,arg_element2));
                type = "boolean";
            }
            else if(func.equals("+")){
                val = String.valueOf(IntegerData.plus(Integer.parseInt(arg1),Integer.parseInt(arg2)));
                type = "INT";
            }
            else if(func.equals("-")){
                val = String.valueOf(IntegerData.minus(Integer.parseInt(arg1),Integer.parseInt(arg2)));
                type = "INT";
            }
            else if(func.equals("*")){
                val = String.valueOf(IntegerData.multiply(Integer.parseInt(arg1),Integer.parseInt(arg2)));
                type = "INT";
            }
            else if(func.equals("/")){
                val = String.valueOf(IntegerData.divide(Integer.parseInt(arg1),Integer.parseInt(arg2)));
                type = "INT";
            }
            else if(func.equals("**")){
                val = String.valueOf(IntegerData.power(Integer.parseInt(arg1),Integer.parseInt(arg2)));
                type = "INT";
            }
            else if(func.equals("Conc")){
                val = String.valueOf(StringData.Conc(arg1,arg2));
                type = "STR";
            }
            // aug method for tuples. Return the tuple after adding new element.
            else if(func.equals("aug")){
                if(null != arg_element1.type)
                switch (arg_element1.type) {
                    case "nil":
                        CseElement result = new CseElement("tau",null);
                        result.tau_values.add(arg_element2);
                        return result;
                    case "tau":
                        arg_element1.tau_values.add(arg_element2);
                        return arg_element1;
                    default:
                        System.out.println("Invalid Syntax\naug for non tuple");
                        break;
                }
                
            }
            else if(func.equals("gr") || func.equals(">")){
                val = String.valueOf(Data.gr(arg_element1,arg_element2));
                type = "boolean";
            }
            else if(func.equals("ge") || func.equals(">=")){
                val = String.valueOf(Data.ge(arg_element1,arg_element2));
                type = "boolean";
            }
            else if(func.equals("ls") || func.equals("<")){
                val = String.valueOf(Data.ls(arg_element1,arg_element2));
                type = "boolean";
            }
            else if(func.equals("le") || func.equals("<=")){
                val = String.valueOf(Data.le(arg_element1,arg_element2));
                type = "boolean";
            }
            // Slicing tuples. Return the expected position of the tuple.
            else if(func.equals("slice")){
                if(!"tau".equals(arg_element1.type)){
                    System.out.println("Expected a tuple. Found "+arg_element1.type);
                    return null;
                }
                else{
                    try{
                        return arg_element1.tau_values.get(Integer.parseInt(arg_element2.value) - 1);
                    }
                    catch(Exception ex){
                        System.out.println("Invalid slicing\nArray size:- "+arg_element1.tau_values.size()+"\nExpect:- "+arg_element2.value);
                        return null;
                    }
                }
            }
            //Return CseElement by output type and value.
            return new CseElement(type,val);
        }
        catch(Exception ex){
            // Catch errors when appling binary fuctions
            System.out.println("Invalid types for "+func);
            return null;
        }
    }
    public CseElement apply_specialfuncs(String func,CseElement arg){
        /*
        
        parameters :- func => type String  --Function name. 
                      arg => type - CseElement -- argument to the special function
        This function is used to apply arg to function.
        Print is only function for this type of functon.
        
        */
        if(func.equals("Print")){
            //Print tuples
            if("tau".equals(arg.type)){
                System.out.println(CseElement.tau_print(arg));
                return null;
            }
            //Print lambda closures
            else if("lambda".equals(arg.type)){
                String print_val="";
                for (Iterator<CseElement> it = ((SpecialCseElement)arg).bounded_var.iterator(); it.hasNext();) {
                    
                    print_val += ((CseElement)it.next()).type;
                    
                }
                
                System.out.println("[lambda closure: "+print_val+"]");
                return null;
            }
            // Print any other elements.
            else{
                System.out.println(arg.value);
                return null;
            }
            
    }
        //Print errors.
        System.out.println("Error:-\nFunction "+func+ " Not Found");
        return null;
    }
    
    public CseElement apply_unifuncs(String func,CseElement arg_element){
        /*
        
        parameters :- func => type String  --Function name. 
                      arg_element => type - CseElement -- argument to the unary function
        This function is used to apply arg_element to unary function and return the result.
        
        */
        try{
            //val is used to get the output result value.
            //type is used to get the output result type
            String arg,val = null,type = null;
            arg = arg_element.value;
            if(func.equals("Stem")){
                val =  String.valueOf(StringData.Stem(arg));
                type = "STR";
            }
            else if(func.equals("Stern")){
                val =  String.valueOf(StringData.Stern(arg));
                type = "STR";
            }
            else if(func.equals("ItoS")){
                val =  String.valueOf(IntegerData.ItoS(Integer.parseInt(arg)));
                type = "STR";
            }
            else if(func.equals("Isinteger")){
                val =  String.valueOf(IntegerData.Isinteger(arg_element));
                type = "boolean";
            }
            else if(func.equals("Istruthvalue")){
                val =  String.valueOf(TruthValueData.Istruthvalue(arg_element));
                type = "boolean";
            }
            else if(func.equals("Isstring")){
                val =  String.valueOf(StringData.Isstring(arg_element));
                type = "boolean";
            }
            else if(func.equals("Istuple")){
                val =  String.valueOf(arg_element.type.equals("tau") || arg_element.type.equals("nil"));
                type = "boolean";
            }
            else if(func.equals("Order")){
                
                val = String.valueOf(arg_element.tau_values.size());
                type = "INT";
                
            }
            else if(func.equals("Isfunction")){
                val =  String.valueOf(this.current_env.getIdentifiers().containsKey(arg));
                type = "boolean";
            }
            else if(func.equals("Isdummy")){
                val =  String.valueOf(Data.Isdummy(arg_element));
                type = "boolean";
            }
            else if(func.equals("not")){
                val =  String.valueOf(TruthValueData.not(Boolean.parseBoolean(arg)));
                type = "boolean";
            }
            else if(func.equals("neg")){
                val =  String.valueOf(IntegerData.neg(Integer.parseInt(arg)));
                type = "INT";
            }
            // Return the resulted CseElement
            return new CseElement(type,val);
        }
        catch(Exception ex){
            //Print the error.
            System.out.println("Invalid types for "+func);
            return null;
        }
    }
    
    

        
    public void add_env_element(int num){
        /*
        
        Parameters :- num type => Integer - Environment number to add
        This method is used add environment element to the top of the both control and stack.
        
        */
        CseElement env_element =  new CseElement("env",String.valueOf(num));
        this.control.push(env_element);
        this.element_stack.push(env_element);
        
    }

    /*
    Setters and getters for attributes.
    */
    
    public Environment getEnv() {
        return current_env;
    }

    public void setEnv(Environment env) {
        this.current_env = env;
    }

    public HashMap<Integer, ArrayList<CseElement>> getControl_structures() {
        return control_structures;
    }

    public void setControl_structures(HashMap<Integer, ArrayList<CseElement>> control_structures) {
        this.control_structures = control_structures;
    }

    public Stack getControl() {
        return control;
    }

    public void setControl(Stack control) {
        this.control = control;
    }

    public Stack getElement_stack() {
        return element_stack;
    }

    public void setElement_stack(Stack element_stack) {
        this.element_stack = element_stack;
    }
    
}