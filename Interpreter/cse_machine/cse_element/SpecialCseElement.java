class SpecialCseElement extends CseElement{
    /*
    This class contain the lambda CseElements and neeta CseElement.
    Subclass of CseElement.
    */
    public Environment cur_env;
    public int cs_number;
    public ArrayList<CseElement> bounded_var;
    public HashMap<CseElement,CseElement> bounded_val;
    public CseElement bound_lambda;

    /*
    Attributes :- 
            => type => Type of the data contain in CseElement.
            => value => value of the data contain in CseElement.
            => cur_env => The environment that the this object was created
            => cs_number => Related control structure number.
            => bounded_var => Bounded variable names related to the SpecialCseElement.
            => bounded_val => HashMap contains the bounded variable names and values related to the SpecialCseElement.
    */
    public SpecialCseElement(String type, String value , Environment current_environment, int cs_number, ArrayList<CseElement> bounded_var) {
        
        super(type, value);
        this.cur_env = current_environment;
        this.cs_number = cs_number;
        this.bounded_var = bounded_var;
        this.bounded_val = new HashMap<>();
        
    }
    
    public SpecialCseElement(String type, String value , Environment current_environment, int cs_number, ArrayList<CseElement> bounded_var , SpecialCseElement lambda) {
        super(type, value);
        this.cur_env = current_environment;
        this.cs_number = cs_number;
        this.bounded_var = bounded_var;
        this.bounded_val = new HashMap<>();
        if(type.equals("neeta")){
            this.bound_lambda = lambda;
        }
    }
    
    
    public void bind(CseElement arg){
        /*
        This method used to bind a single CseElement to the object attribute bounded_val
        */
        this.bounded_val.put(this.bounded_var.get(0),arg);
    }
    
    public void bind(ArrayList<CseElement> args){
        /*
        This method used to bind many CseElements to the object attribute bounded_val
        */
        int arg_n,bound_n;
        CseElement element;
        arg_n = args.size();
        bound_n = this.bounded_var.size();
        if(arg_n!=bound_n){
            System.out.println("Error! - Invalid Binding.\n Expected :-"+bound_n+"\n Found:- "+arg_n);
        }
        else{
            int i = 0;
            for (Iterator<CseElement> it = args.iterator(); it.hasNext();) {
            element = it.next();
            this.bounded_val.put(this.bounded_var.get(i),element);
            i+=1;
        }
        }
    }
    //getters and setters for attributes
    public Environment getCurrent_environment() {
        return cur_env;
    }

    public void setCurrent_environment(Environment current_environment) {
        this.cur_env = current_environment;
    }

    public int getCs_number() {
        return cs_number;
    }

    public void setCs_number(int cs_number) {
        this.cs_number = cs_number;
    }

    public ArrayList<CseElement> getBounded_var() {
        return bounded_var;
    }

    public void setBounded_var(ArrayList<CseElement> bounded_var) {
        this.bounded_var = bounded_var;
    }

    @Override
    public String toString() {
        return "lambda "+ " cs_number "+this.cs_number+ " bounded_var "+this.bounded_var+ " current_environment "+this.cur_env; //To change body of generated methods, choose Tools | Templates.
    }
            
}
