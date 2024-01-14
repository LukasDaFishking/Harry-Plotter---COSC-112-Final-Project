import java.util.Arrays;

public class StringParser{
    //the way we parse strings is pretty bad and makes it hard to follow
    //it basically is just a bunch of if statements about what the current char is
    //and what previous ones were
    static int power;
    static double currentProd;
    static String currentNum;
    static boolean xTerm;
    static boolean yTerm;
    public static double[] parseEquation(String eq){
        eq = eq.replaceAll("\\s+","");
        //remove spaces
        //set up starting values
        double[] coefficients = new double[10];
        currentNum = "";
        currentProd = 1;
        double[] error = new double[1];
        power = 0;
        xTerm = false;
        yTerm = false;
        char lastC = 'a';
        String lastOperation = "";
        //parse through the length of the string
        for(int i = 0; i < eq.length(); i++){
            //current char
            char c = eq.charAt(i);
            if(i > 0){
                //no lastC if we are at the start
                lastC = eq.charAt(i-1);
            }
            //checks if the character is one we recognize
            if(!Character.isDigit(c) && c != '+' && c != '-' && c != '^' && c != '*' && c != '/' && c!= 'x' && c!= 'y' && c!= '='){
                error[0] = -1; //unrecognized character included
                return error;
            }
            if(currentNum.isEmpty()){
                //expecting to begin reading a new number
                if(Character.isDigit(c) || (c == '-' && lastC != 'x')){
                    currentNum = currentNum + c;
                }
                else if(c == 'x'){
                    if(xTerm || yTerm){
                        error[0] = -3; //Only one x or y per term allowed
                        return error;
                    }
                    else {
                        xTerm = true;
                        power = 1;
                        //the number was empty so this represents x on its own
                    }
                }
                else if(c == 'y'){
                    if(xTerm || yTerm){
                        error[0] = -3; //Only one x or y per term allowed
                        return error;
                    }
                    else {
                        yTerm = true;
                        power = 1;
                        //again we know y is on its own
                    }
                }
                else if(c == '^' && (lastC == 'x' || lastC == 'y')){
                    lastOperation = "^";
                }
                else if((lastC == 'x' || lastC == 'y') && (c == '+' || c == '-' || c == '=')){
                    currentProd = 1;
                    power = 1;
                    if(yTerm){
                        coefficients[9] = power;
                    }
                    else{
                        if(power > -1 && power < 9) {
                            coefficients[power] += currentProd;
                        }
                        else{
                            error[0] = -6; //Powers of x must be from 0 to 8
                            return(error);
                        }
                    }
                    xTerm = false;
                    yTerm = false;
                    power = 0;
                    lastOperation = "+";
                    if(c == '-'){
                        currentNum = "-";
                        lastOperation = "-";
                    }
                }
                else if((lastC == 'x' || lastC == 'y') && (c == '*' || c == '/')){
                    currentProd = 1;
                    currentNum = "";
                    lastOperation = Character.toString(c);
                }
                else{
                    error[0] = -2; //Expected a number, found an operation
                    return error;
                }
            }
            else if(currentNum.equals("-")){
                if(Character.isDigit(c)){
                    currentNum = currentNum + c;
                }
                else if(c == 'x'){
                    if(xTerm || yTerm){
                        error[0] = -3; //Only one x or y per term allowed
                        return error;
                    }
                    else {
                        currentProd *= -1;
                        xTerm = true;
                        power = 1;
                        currentNum = "";
                    }
                }
                else if(c == 'y'){
                    error[0] = -4; //Y cannot have a coefficient
                    return error;
                }
                else{
                    error[0] = -2; //Expected a number, found an operation
                    return error;
                }
            }
            else{
                if(Character.isDigit(c)){
                    currentNum += c;
                }
                else if(c == '+' || c == '-' || c == '='){
                    if(lastOperation.equals("^")){
                        power *= Integer.parseInt(currentNum);
                    }
                    else{
                        if (!(lastOperation.equals("/"))) {
                            currentProd *= Integer.parseInt(currentNum);
                        }
                        else{
                            currentProd /= Integer.parseInt(currentNum);
                        }
                    }
                    if(yTerm){
                        if(currentProd != 1){
                            error[0] = -4; //Y cannot have a coefficient
                            return error;
                        }
                        else{
                            coefficients[9] = power;
                        }
                    }
                    else{
                        if(power > -1 && power < 9) {
                            coefficients[power] += currentProd;
                        }
                        else{
                            error[0] = -6; //Powers of x must be from 0 to 8
                            return(error);
                        }
                    }
                    currentProd = 1;
                    currentNum = "";
                    xTerm = false;
                    yTerm = false;
                    power = 0;
                    lastOperation = "+";
                    if(c == '-'){
                        currentNum = "-";
                        lastOperation = "-";
                    }
                }
                else if(c == '*' || c == '/'){
                    if(lastOperation.equals("^")){
                        power *= Integer.parseInt(currentNum);
                    }
                    else{
                        if (!(lastOperation == "/")) {
                            currentProd *= Integer.parseInt(currentNum);
                        }
                        else{
                            currentProd /= Integer.parseInt(currentNum);
                        }
                    }
                    currentNum = "";
                    lastOperation = Character.toString(c);
                }
                else if(c == 'x'){
                    if(xTerm || yTerm){
                        error[0] = -3; //Only one x or y per term allowed
                    }
                    else{
                        xTerm = true;
                        if(power != -1){
                            power = 1;
                        }
                    }
                }
                else if(c == 'y'){
                    if(xTerm || yTerm){
                        error[0] = -3; //Only one x or y per term allowed
                        return error;
                    }
                    else{
                        yTerm = true;
                        currentProd *= Integer.parseInt(currentNum);
                        power = 1;
                    }
                }
                else if(c == '^'){
                    if(lastC != 'x' && lastC != 'y'){
                        error[0] = -5; //Only use ^ after x or y
                        return(error);
                    }
                    else{
                        currentProd *= Integer.parseInt(currentNum);
                        currentNum = "";
                        if(lastOperation.equals("/")){
                            power = -1;
                        }
                    }
                    lastOperation = "^";
                }
            }
        }
        if(lastOperation == "^"){
            power *= Integer.parseInt(currentNum);
        }
        else{
            if(currentNum.equals("-")){
                currentNum = "-1";
            }
            if (!(lastOperation.equals("/"))) {
                if(!currentNum.isEmpty()) {
                    currentProd *= Integer.parseInt(currentNum);
                }
            }
            else{
                currentProd /= Integer.parseInt(currentNum);
            }
        }
        if(yTerm){
            if(currentProd != 1){
                error[0] = -4; //Y cannot have a coefficient
                return error;
            }
            else{
                coefficients[9] = power;
            }
        }
        else{
            if(power > -1 && power < 9) {
                coefficients[power] += currentProd;
            }
            else{
                error[0] = -6; //Powers of x must be from 0 to 8
                return(error);
            }
        }
        return(coefficients);
    }
}