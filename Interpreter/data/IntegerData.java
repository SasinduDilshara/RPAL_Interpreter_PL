class IntegerData{
    /*
    This class define all kind of functions in RPAL langugage for Integer data type.
    Functions :- +,-,/,*,Itos,neg,**,eq,ne,ls,gr,le,ge,<,>,<=,>=
    */
    
    public static int plus(int a,int b){
    
        return a + b;
}
    
    public static int minus(int a,int b){
    
        return a - b;
}
        
    public static int multiply(int a,int b){
    
        return a * b;
}
            
    public static int divide(int a,int b){
    
        return a / b;
}
    
    public static int power(int a,int b){
    
        return (int)Math.pow(a, b);
}
    public static int neg(int a){
    //take the negation
        return -1*a;
}
    
    public static boolean eq(int a,int b){
    
        return a == b;
}
    
    public static boolean ne(int a,int b){
    
        return a != b;
}
    
    public static boolean ls(int a,int b){
    
        return a < b;
}
    
    public static boolean gr(int a,int b){
    
        return a > b;
}
    
    public static boolean le(int a,int b){
    
        return a <= b;
}
    
    public static boolean ge(int a,int b){
    
        return a >= b;
}
    public static boolean Isinteger(Object object){
        /*
        This function check whether input parameter is a Integer or not.
        */
        try{
            CseElement obj = (CseElement)object;
            if (object == null) {
                return false;
        }
            return obj.type.equals("INT");
        }
        catch(Exception ex){
            return false;
        }

    }    

    static String ItoS(int arg) {
        /*
        This method will convert Integer to a String and return the value
        */
        return Integer.toString(arg);
    }
}

