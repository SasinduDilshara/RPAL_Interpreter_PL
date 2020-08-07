package rpal_interpreter.data;

import rpal_interpreter.cse_machine.cse_element.CseElement;



public abstract class Data{    
    /*
    This class define the RPAL general functions and functions which can apply more than one data type.
    ex:- eq function can apply for Strings and integers.
         gr function can apply for Strings and integers.
    */
    public static boolean eq(Object a,Object b){
        /*
        RPAL eq function
            Two object parameters will cast down according to the data type.
        */
        try{
            CseElement element1 = (CseElement)a;
            CseElement element2 = (CseElement)b;
            if("STR".equals(element1.type) ||"STRING".equals(element1.type)){
                return StringData.eq(String.valueOf(element1.value), String.valueOf(element2.value));
            }
            if("INT".equals(element1.type) ||"INTEGER".equals(element1.type)){
                return IntegerData.eq(Integer.parseInt(element1.value),Integer.parseInt(element2.value));
            }
            if("boolean".equals(element1.type)){
                return TruthValueData.eq(Boolean.parseBoolean(element1.value),Boolean.parseBoolean(element2.value));
            }
            else{
                return a.equals(b);
            } 
            }
            catch(Exception ex){
                System.out.println("Invalid syntax in binary eq func");
                return false;
            }
               
}
    
    public static boolean ne(Object a,Object b){
        /*
        RPAL ne function
            Two object parameters will cast down according to the data type.
        */
        try{
            CseElement element1 = (CseElement)a;
            CseElement element2 = (CseElement)b;
            if("STR".equals(element1.type) ||"STRING".equals(element1.type)){
                return StringData.ne(String.valueOf(element1.value), String.valueOf(element2.value));
            }
            if("INT".equals(element1.type) ||"INTEGER".equals(element1.type)){
                return IntegerData.ne(Integer.parseInt(element1.value),Integer.parseInt(element2.value));
            }
            if("boolean".equals(element1.type)){
                return TruthValueData.ne(Boolean.parseBoolean(element1.value),Boolean.parseBoolean(element2.value));
            }
            else{
                return !a.equals(b);
            } 
            }
            catch(Exception ex){
                System.out.println("Invalid syntax in binary ne func");
                return false;
            }
}
    
    public static boolean ls(Object a,Object b){
        /*
        RPAL ls function
            Two object parameters will cast down according to the data type.
        */
        try{
            CseElement element1 = (CseElement)a;
            CseElement element2 = (CseElement)b;
            if("STR".equals(element1.type) ||"STRING".equals(element1.type)){
                return StringData.ls(String.valueOf(element1.value), String.valueOf(element2.value));
            }
            if("INT".equals(element1.type) ||"INTEGER".equals(element1.type)){
                return IntegerData.ls(Integer.parseInt(element1.value),Integer.parseInt(element2.value));
            }
            else{
                System.out.println("Invalid syntax in binary ls func");
                return false;
            } 
            }
            catch(Exception ex){
                System.out.println("Invalid syntax in binary ls func");
                return false;
            }
}
    
    public static boolean gr(Object a,Object b){ 
        /*
        RPAL gr function
            Two object parameters will cast down according to the data type.
        */
        try{
            CseElement element1 = (CseElement)a;
            CseElement element2 = (CseElement)b;
            if("STR".equals(element1.type) ||"STRING".equals(element1.type)){
                return StringData.gr(String.valueOf(element1.value), String.valueOf(element2.value));
            }
            if("INT".equals(element1.type) ||"INTEGER".equals(element1.type)){
                return IntegerData.gr(Integer.parseInt(element1.value),Integer.parseInt(element2.value));
            }
            else{
                System.out.println("Invalid syntax in binary gr func");
                return false;
            } 
            }
            catch(Exception ex){
                System.out.println("Invalid syntax in binary gr func");
                return false;
            }
}
    
    public static boolean le(Object a,Object b){    
        /*
        RPAL le function
            Two object parameters will cast down according to the data type.
        */
        try{
            CseElement element1 = (CseElement)a;
            CseElement element2 = (CseElement)b;
            if("STR".equals(element1.type) ||"STRING".equals(element1.type)){
                return StringData.le(String.valueOf(element1.value), String.valueOf(element2.value));
            }
            if("INT".equals(element1.type) ||"INTEGER".equals(element1.type)){
                return IntegerData.le(Integer.parseInt(element1.value),Integer.parseInt(element2.value));
            }
            else{
                System.out.println("Invalid syntax in binary le func");
                return false;
            } 
            }
            catch(Exception ex){
                System.out.println("Invalid syntax in binary le func");
                return false;
            }
}
    
    public static boolean ge(Object a,Object b){    
        /*
        RPAL ge function
            Two object parameters will cast down according to the data type.
        */
        try{
            CseElement element1 = (CseElement)a;
            CseElement element2 = (CseElement)b;
            if("STR".equals(element1.type) ||"STRING".equals(element1.type)){
                return StringData.ge(String.valueOf(element1.value), String.valueOf(element2.value));
            }
            if("INT".equals(element1.type) ||"INTEGER".equals(element1.type)){
                return IntegerData.ge(Integer.parseInt(element1.value),Integer.parseInt(element2.value));
            }
            else{
                System.out.println("Invalid syntax in binary ge func");
                return false;
            } 
            }
            catch(Exception ex){
                System.out.println("Invalid syntax in binary ge func");
                return false;
            }
    }
     public static boolean Isdummy(Object obj){ 
         /*
        RPAL Isdummy function
            This function will check whether input object is null or not.
        */
         try{
             CseElement element1 = (CseElement)obj;
             return "dummy".equals(element1.type);
         }
         catch(Exception ex){
             System.out.println("Invalid syntax in binary Isdummy func");
             return obj == null;
         }
                 
     }    
}
