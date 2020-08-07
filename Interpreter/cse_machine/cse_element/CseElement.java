class CseElement{
    
    /*
    This class objects used to input and output to CSE Machine.
    CSE Machine control and stack contain with CseElement objects
    */
    
    public String type;
    public String value;
    public ArrayList<CseElement> tau_values;
    public HashMap<Integer,HashMap<Integer,ArrayList<CseElement>>> tau_funcs;
    public boolean tau_check;
    public int children_size;


    public CseElement(String type, String value) {
        /*
        Attributes :-
            => type = Type of the data contain in CseElement. ex:- ID,INT,STR,boolean
            => value => value of the data contain in CseElement ex:- 2,3,hello,true
            => tau_values => If CseElement is a tuple -> Tuple elements. Else -> Empty ArrayList
            => children_size => If CseElement is a tuple -> No of tuple elements. Else -> 0
        */
        this.type = type;        
        this.value = value;
        this.tau_values = new ArrayList<>();
        this.children_size = 0;
    }
    //Getters and setters for attributes
    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public static String tau_print(CseElement tuple){
//        This method is used to print the tuple elements. return the String as (1,2,3,4,5)
        CseElement tau_value;
        String result = "(";
        for (Iterator<CseElement> it = tuple.tau_values.iterator(); it.hasNext();) {
            tau_value = (CseElement)it.next();
            if(tau_value.type.equals("tau")){
                result += tau_print(tau_value) +" ,";
            }
            else{
                result += tau_value.value + " ,";
            }
        }
        if(result.endsWith(",")){
            int size = result.length();
            result = result.substring(0, size - 1);
    }
        result+=")";
        return result;
    }

    @Override
    public String toString() {
        if(this.type == null & this.value == null){
        System.out.println("Null cse element");
          }
        String result = "";
        
        if(this.value == null){
            result = "CSE OBJECT " + this.type + "val empty"; 
        }
        if("tau".equals(this.type)){
            result = this.type+" tau values// "+this.tau_values+" //";
        }
        else{
            result = this.type + " " + this.value;
        }
        return result;
    }
    
    
}