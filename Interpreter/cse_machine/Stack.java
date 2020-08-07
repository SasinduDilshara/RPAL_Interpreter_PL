class Stack{
    /*
    
    This class is used to defined a stack.
    
    */
    public ArrayList<CseElement> stack;

    public Stack(ArrayList<CseElement> input_stack) {
        /*
        stack - ArrayList<CseElement> to save the stack elements
        */
        this.stack = input_stack;
    }

    public Stack() {
        /*
        
        Default constructor.
        
        */
        this.stack = new ArrayList<>();
    }
    public CseElement pop(){
        //pop the last element in the stack
        // return type CseElement 
        int size = this.stack.size() - 1;
        CseElement element_node =  this.stack.get(size);
        this.stack.remove(size);
        return element_node;
    }
    public void push(CseElement node){
        //push element to stack
        this.stack.add(node);
    }
    public void addList(ArrayList<CseElement> array){
        // This method is use to add a ArrayList to the stack
        this.stack.addAll(array);
    }
    public boolean empty(){
        //Check the stack is empty or not
        return this.stack.isEmpty();
    }
    public CseElement check_n_last(int n){
        // Check the elements in the stack.
        int size = this.stack.size() - 1;
        return this.stack.get(size - n);
    }
    public CseElement getFirst(){
        //Method to view the top element
        return this.stack.get(0);
    }
    public int getSize(){
        //Method to get the stack size.
        return this.stack.size();
    }
    public CseElement findElement(String type){
        /*
        This method is used to get the specific type element in the stack.
        */
        CseElement node;
        int size = getSize() - 1;
        for (int i = size; i >= 0; i--) {
            node = this.stack.get(i);
            if(type.equals(node.type)){ 
                return node;
            } else {
            }
    }
//        System.out.println("Invalid syntax!");
        return null;
    }
    
    public static Stack copy(Stack stack){
        // This method is used to get the copy of a stack
        try {
            boolean x = stack instanceof Stack;
            return (Stack)(stack.clone());
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error happen while stack cloning");
            return null;
        }
    }

    @Override
    public String toString() {
        String val = "";
        CseElement node;
        for (Iterator<CseElement> it = this.stack.iterator(); it.hasNext();) {
            node = it.next();
            if(null == node.type){
                val = "Null cse element!";
            }
            else{
            if("env".equals(node.type)){
            val+=node.type+" "+node.value+" , ";
        }
            else if(node.type.equals("lambda")){
                val += node.type+" "+((SpecialCseElement)node).bounded_var + " , ";
            }
            else if("tau".equals(node.type)){
                val += node.type+" taus:- "+node.tau_values;
            }
            else if(node.type != null){
                val += node.type+" "+node.value + " , ";
            }
            
            else{
                val += node.type + " , ";
            }
            }
        }
        return "Stack{" + "stack= " + val + '}';
    }
}
