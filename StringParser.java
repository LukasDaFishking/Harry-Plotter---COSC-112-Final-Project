import java.util.Arrays;

public class StringParser{
    static int power;
    static int currentProd;
    static String currentNum;
    static boolean xTerm;
    static boolean yTerm;
    public static int[] parseEquation(String eq){
        int[] coefficients = new int[10];
        currentNum = "";
        currentProd = 1;
        int[] error = new int[1];
        power = 0;
        xTerm = false;
        yTerm = false;
        char lastC = 'a';
        String lastOperation = "";
        for(int i = 0; i < eq.length(); i++){
            char c = eq.charAt(i);
            if(i > 0){
                lastC = eq.charAt(i-1);
            }
            if(!Character.isDigit(c) && c != '+' && c != '-' && c != '^' && c != '*' && c != '/' && c!= 'x' && c!= 'y' && c!= '='){
                error[0] = -1; //unrecognized character included
                return error;
            }
            if(currentNum.isEmpty()){
                //expecting to begin reading a new number
                if(Character.isDigit(c) || c == '-'){
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
                    }
                }
                else if(c == '^' && (lastC == 'x' || lastC == 'y')){
                    lastOperation = "^";
                }
                else if((lastC == 'x' || lastC == 'y') && (c == '+' || c == '-' || c == '=')){
                    System.out.println(currentProd);
                    currentProd = 1;
                    power = 1;
                    if(yTerm){
                        coefficients[9] = power;
                        System.out.println(Arrays.toString(coefficients));
                    }
                    else{
                        coefficients[power] += currentProd;
                        System.out.println(power);
                        System.out.println("prod" + currentProd);
                        System.out.println(Arrays.toString(coefficients));
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
                }
                else{
                    error[0] = -2; //Expected a number, found an operation
                    System.out.println(c);
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
                    System.out.println(currentProd);
                    if(lastOperation == "^"){
                        power *= Integer.parseInt(currentNum);
                        System.out.println("power" + power);
                        System.out.println(currentProd);
                    }
                    else{
                        if (!(lastOperation == "/")) {
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
                            System.out.println(Arrays.toString(coefficients));
                        }
                    }
                    else{
                        coefficients[power] += currentProd;
                        System.out.println(power);
                        System.out.println("prod" + currentProd);
                        System.out.println(Arrays.toString(coefficients));
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
                    if(lastOperation == "^"){
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
                        if(lastOperation == "/"){
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
            if (!(lastOperation == "/")) {
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
                coefficients[10] = power;
            }
        }
        else{
            coefficients[power] += currentProd;
        }
        return(coefficients);
    }
}