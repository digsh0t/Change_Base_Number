/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package j1.s.p0011_change_base_number;

import java.math.BigInteger;
import java.util.Scanner;
import java.util.Stack;

/**
 *
 * @author wintl
 */
public class J1SP0011_Change_Base_Number {

    /**
     * @param args the command line arguments
     */
    static int baseInput, baseOutput;
    static String inputValue;
    
    //To input base number input and output
    public static void inputBase() {
        Scanner sc;
        int cont = 0;
        System.out.println("Choose the base number input\n(1 is binary, 2 is decimal,  3 is hexadecimal)");
        do {
            cont = 1;
            try {
                sc = new Scanner(System.in);
                baseInput = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid input");
                cont = 0;
            }
            if (baseInput > 3 || baseInput < 1 || cont == 0) {
                System.out.println("Invalid base (1 is binary, 2 is decimal,  3 is hexadecimal)");
            }
        } while (baseInput > 3 || baseInput < 1 || cont == 0);
        System.out.println("Choose the base number output\n (1 is binary, 2 is decimal,  3 is hexadecimal)");
        do {
            cont = 1;
            try {
            sc = new Scanner(System.in);
            baseOutput = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid input");
                cont = 0;
            }
            if (baseOutput > 3 || baseOutput < 1 || cont == 0) {
                System.out.println("Invalid base (1 is binary, 2 is decimal,  3 is hexadecimal)");
            }
        } while (baseOutput > 3 || baseOutput < 1 || cont == 0);
        
        do {
            cont = 0;
            System.out.println("Input value: ");
            if (baseInput == 1) {
                sc = new Scanner(System.in);
                inputValue = sc.nextLine();
                if (!inputValue.matches("[0-1]*$")) {
                    System.out.println("Invalid binary");
                } else {
                    cont = 1;
                }  
            }
            if (baseInput == 2) {
                do {
                    sc = new Scanner(System.in);
                    inputValue = sc.nextLine();
                } while (inputValue.isBlank());
                if (!inputValue.matches("^\\-?[0-9]*$")) {
                    System.out.println("Invalid decimal");
                } else {
                    cont = 1;
                }  
            }
            if (baseInput == 3) {
                sc = new Scanner(System.in);
                inputValue = sc.nextLine();
                inputValue = inputValue.toLowerCase();
                if (!inputValue.matches("[a-fA-F0-9]+")) {
                    System.out.println("Invalid hex");
                } else {
                    cont = 1;
                }           
            }
        } while (cont == 0);
    }
    
    public static void selectConverter() {
        if (baseInput == 2 && baseOutput == 1) {
            System.out.println(decToBit(inputValue));
        } else if (baseInput == 3 && baseOutput == 1) {
            System.out.println(hexToBi(inputValue));
        } else if (baseInput == 1 && baseOutput == 2) {
            System.out.println(biToDec(inputValue));
        } else if (baseInput == 1 && baseOutput == 3) {
            System.out.println(biToHex(inputValue));
        } else if (baseInput == 2 && baseOutput == 3) {
            System.out.println(decToHex(inputValue));
        } else if (baseInput == 3 && baseOutput == 2) {
            System.out.println(hexToDec(inputValue));
        } else {
            System.out.println(inputValue);
        }
    }
    
    public static String stackToString(Stack<String> stack) {
        String string = "";
        while (!stack.isEmpty()) {
            string = string.concat(stack.pop());
        }
        return string;
    }
    
    public static String decToBit(String inputValue) {
        boolean isNegative = false;
        if (inputValue.charAt(0) == '-') {
            isNegative = true;
            inputValue = inputValue.substring(1);
        }
        Stack<String> stack = new Stack();
        BigInteger num = new BigInteger(inputValue.strip());
        if (num.toString().equals("0")) {
            stack.add(String.valueOf(0));
        } else {
            while (!num.toString().equals("0")) {
               stack.add((String.valueOf(num.remainder(BigInteger.TWO))));
               num = num.divide(BigInteger.TWO);
            }
        }
        if (isNegative == true) {
            byte[] byteArray = stackToString(stack).getBytes();
            byteArray = toFormat(byteArray);
            xorByteArray(byteArray);
            plusByte(byteArray);
            return byteToString(byteArray);
        }
        return stackToString(stack);
    }
    
    //Convert byte array to String
    public static String byteToString(byte[] bytee) {
        String toString = "";
        for (int i = 0;i < bytee.length;i++) {
            toString = toString.concat(Byte.toString(bytee[i]));
        }
        return toString;
    }
    
    //Hex to decimal
    public static String hexToDec(String inputValue) {
        Stack<Integer> stack = new Stack<>();
        int power;
        BigInteger powerBig = new BigInteger("16");
        BigInteger decTotal = new BigInteger("0");
        for (int i = 0;i < inputValue.length();i++) {
            switch (inputValue.charAt(i)) {
                case 'A':
                    stack.add(10);
                    break;
                case 'B':
                    stack.add(11);
                    break;
                case 'C':
                    stack.add(12);
                    break;
                case 'D':
                    stack.add(13);
                    break;
                case 'E':
                    stack.add(14);
                    break;
                case 'F':
                    stack.add(15);
                    break;
                default:
                    stack.add(Character.getNumericValue(inputValue.charAt(i)));
            }
        }
        power = 0;
        while (!stack.isEmpty()) {     
            decTotal = decTotal.add(powerBig.pow(power).multiply(new BigInteger(String.valueOf(stack.pop()))));
            power++;
        }
        return String.valueOf(decTotal);
    }
    
    //To right byte format
    public static byte[] toFormat(byte[] bytee) {
        int formattedLength = 16 - bytee.length%16;
        byte[] formatted = new byte[formattedLength + bytee.length];
        for (int i = 0;i < formattedLength;i++) {
            formatted[i] = 0;
        }
        for (int i = formattedLength;i < formatted.length;i++) {
            formatted[i] = bytee[i-formattedLength];
        }
        return formatted;
    }
    
    
    //Xor byte array
    public static byte[] xorByteArray(byte[] bytee) {
        for (int i = 0;i < bytee.length;i++) {
            if (bytee[i] == 0) {
                bytee[i] = 1;
            } else {
                bytee[i] = 0;
            }
        }
        return bytee;
    }
    
    //Print byte stream
    public static void printByte(byte[] bytee) {
        for (int i = 0;i < bytee.length;i++) {
            System.out.print(bytee[i]);
        }
        System.out.println("");
    }
    
    public static byte[] plusByte(byte[] bytee) {
        int i = bytee.length - 1;
        while (bytee[i] == 1) {
            bytee[i] = 0;
            i--;
            if (i < 0) {
                i = bytee.length - 1;
            }
        }
        if (bytee[i] == 0) {
            bytee[i] = 1;
        }
        return bytee;
    }
    
    //Decimal to hex
    public static String decToHex(String inputValue) {
        BigInteger num = new BigInteger(inputValue);
        Stack<String> stack = new Stack<>();
        if (inputValue.charAt(0) == '-') {
            return biToHex(decToBit(inputValue));
        }
        if (num.toString().equals("0")) {
            stack.add(String.valueOf(0));
        }
        while (num.compareTo(new BigInteger("0")) > 0) {
            switch (Integer.valueOf((num.mod(new BigInteger("16"))).toString())) {
                case 10:
                    stack.add("A");
                    break;
                case 11:
                    stack.add("B");
                    break;
                case 12:
                    stack.add("C");
                    break;
                case 13:
                    stack.add("D");
                    break;
                case 14:
                    stack.add("E");
                    break;
                case 15:
                    stack.add("F");
                    break;
                default:
                    stack.add((num.mod(new BigInteger("16"))).toString());                   
            }
            num = num.divide(new BigInteger("16"));
        }
        return stackToString(stack);
    }
    
    //Hex to binary
    public static String hexToBi(String inputValue) {
        return decToBit(hexToDec(inputValue));
    }
    
    public static String biToDec(String inputValue) {
        int power;
        BigInteger powerBig = new BigInteger("2");
        BigInteger num = new BigInteger("0");
        power = 0;
        for (int i = inputValue.length() - 1; i >= 0;i--) {
            num = num.add(powerBig.pow(power).multiply(new BigInteger(Character.toString(inputValue.charAt(i)))));
            power++;
        }
        return num.toString();
    }    
    
    public static String biToHex(String inputValue) {
        return decToHex(biToDec(inputValue));
    }
    
    public static void main(String[] args) {
        String cont;
        Scanner sc;
        do {
            inputBase();
            selectConverter();
            System.out.print("Do you want to continue? (N or No to exit): ");
            sc = new Scanner(System.in);
            cont = sc.nextLine();
        } while (!(cont.equalsIgnoreCase("N") || cont.equalsIgnoreCase("No")));
    }
    
}
