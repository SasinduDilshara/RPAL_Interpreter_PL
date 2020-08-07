

class TruthValueData{
    /*
    This class define all kind of functions in RPAL langugage for TruthValue data type.
    Methods names are named according do RPAL syntax
    Functions :- eq,ne,or,and,not,Istruthvalue
    */
    
    public static boolean eq(boolean a,boolean b){
    
        return a == b;
}
    
    public static boolean ne(boolean a,boolean b){
    
        return a != b;
}
    public static boolean or(boolean a,boolean b){
    
        return a || b;
}
    public static boolean and(boolean a,boolean b){
    
        return a & b;
}
    public static boolean not(boolean a){
    
        return !a;
}

    public static boolean Istruthvalue(Object object){
        try{
            //chec whether input object is in boolean type.
            CseElement obj = (CseElement)object;
            if (object == null) {
                return false;
        }
            return obj.type.equals("boolean");
        }
        catch(Exception ex){
            return false;
        }
    }

    
}