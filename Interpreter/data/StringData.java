
class StringData{
    /*
    This class define all kind of functions in RPAL langugage for string data type.
    Methods names are named according do RPAL syntax
    Functions :- ge,ne,ls,gr,Stem,Stern,eq,le,ge,Conc,Isstring
    */
    
    public static boolean ge(String str1, String str2) {
        //Check two str1 is greater than or equal to str2 or not
        return str1.compareTo(str2) >= 0;
    }
    public static boolean le(String str1, String str2) {
        //Check two str1 is less than or equal to str2 or not
        return str1.compareTo(str2) <= 0;
    }
    public static boolean gr(String str1, String str2) {
        //Check two str1 is greater than to str2 or not
        return str1.compareTo(str2) > 0;
    }
    public static boolean ls(String str1, String str2) {
        //Check two str1 is less than to str2 or not
        return str1.compareTo(str2) < 0;
    }    
    public static String Stern(String str)
    {//Return the input string without first letter
        if(str.length() == 0){
            return str;
        }
        return str.substring(1);
    }
    public static String Stem(String str)
    {//returns the first letter of input string
        if(str.length() == 0){
            return str;
        }
        return String.valueOf(str.charAt(0));
    }
    public static boolean eq(String str1, String str2)
    {//Check two string is equal
        return (str1 == null ? str2 == null : str1.equals(str2));
    }
    public static boolean ne(String str1, String str2)
    {//Check two string is not equal
        return (str1 == null ? str2 != null : !str1.equals(str2));
    }
    public static String Conc(String str1, String str2)
    {//Concatenate strings
        return str1 + str2;
    }
    public static boolean Isstring(CseElement object){
        /*
        This function check whether input parameter is a String or not.
        */
        try{
            if (object == null) {
                return false;
        }
            else if("STR".equals(object.type) || "STRING".equals(object.type)){
            
            return true;
            }
            else{
                return false;
            }
        }
        catch(Exception ex){
            return false;
        }
    }
    
    
}
