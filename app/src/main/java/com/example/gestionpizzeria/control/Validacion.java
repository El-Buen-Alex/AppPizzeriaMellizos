package com.example.gestionpizzeria.control;

import android.util.Log;

public class Validacion {
    public static boolean esEntero(String numero){
        boolean flag=false;
        int prueba=0;
        try{
            prueba=Integer.parseInt(numero);
            flag=true;
        }catch(NumberFormatException ex){
            Log.d("Error Format ","No es un entero");
        }
        return flag;
    }
    public static int aEntero(String numero){
        int num=0;
        try{
            num=Integer.parseInt(numero);
        }catch(NumberFormatException ex){
            Log.d("Error Format ","No es un entero");
        }
        return num;
    }
    public static double aDecimal(String numero){
        double num=0;
        try{
            num=Double.parseDouble(numero);
        }catch(NumberFormatException ex){
            Log.d("Error Format ","No es un Double");
        }
        return num;
    }
}
