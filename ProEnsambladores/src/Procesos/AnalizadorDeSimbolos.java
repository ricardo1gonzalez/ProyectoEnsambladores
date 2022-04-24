package Procesos;

import javax.swing.*;
import java.util.Vector;

public class AnalizadorDeSimbolos {
    public String[] vectorRegistros = {"AX","AH","AL","BX","BH","BL","CX","CH","CL","DX","DH","DL","SI","DI","SS","DS","ES","CS","BP","SP","IP"};
    public String[] vectorInstruccionesC = {"SCASW","HLT","XLATB","INTO","AAD","MOVSW"};
    public String[] vectorCompuestasP = {".CODE SEGMENT",".DATA SEGMENT",".STACK SEGMENT","ENDS","DB","DW","EQU","DUP","BYTE PTR","WORD PTR"};
    public String[] vectorInstrucciones = {"IMUL","INC","INT","POP","SAR","ADC","ADD","SUB","JBE","JNGE","JNA","JS","JL","JNZ"};
    public String[] vectorCompuestasS = {"BYTE PTR","WORD PTR"};
    public String[] vectorResultados = {"Pseudoinstrucción","Instrucción","Registro","Símbolo","Constante numérica decimal","Constante numérica hexadecimal","Constante numérica binaria","Constante tipo caracter","Elemento no identificado"};
    public Vector<String> todoIdentificado = new Vector<String>();
    public int j;
    
    public AnalizadorDeSimbolos(){

    }

    public String adaptar(String cadena){
        String cadenaTratada = new String("");
        char[] aCaracteres = cadena.toCharArray();

        int band1 = 0,bandP = 0, cont = 0;
        for (int i = 0; i < aCaracteres.length; i += 1){
            if(aCaracteres[i] != ';' && band1 != 2 && bandP != 1){
                if(aCaracteres[i] != ' ' && band1 < 1 && aCaracteres[i] != '\t' && aCaracteres[i] != '\n' && aCaracteres[i] != 9){
                    band1 = 1;
                    cadenaTratada += aCaracteres[i];
                }else if(band1 == 1){
                    cadenaTratada += aCaracteres[i];
                }
            }else if(aCaracteres[i] == '.' && bandP == 1){
                bandP = 1;
                if(aCaracteres[i] == ' ' && cont<1){
                    cadenaTratada += aCaracteres[i];
                    cont++;
                }
                if(cont>0){
                   i = aCaracteres.length;
                }
            }else{
                band1 = 2;
                i = aCaracteres.length;
            }
        }
        return cadenaTratada;
    }
    
    public int veriPinstrucciones(Vector<String> vectorConTodosLosElementos, int j, int band){
        for(int k = 0; k<vectorCompuestasP.length; k += 1){
            if(vectorConTodosLosElementos.get(j).equals(vectorCompuestasP[k])){
                todoIdentificado.add(vectorConTodosLosElementos.get(j));
                todoIdentificado.add(vectorResultados[0]);
                k = vectorCompuestasP.length+1;
                band = 0;
            }else{
                band += 1;
            }
        }
        return band;
    }
    
    public int veriInstruccionesC(Vector<String> vectorConTodosLosElementos, int j, int band2){
        for(int k = 0; k<vectorInstruccionesC.length; k += 1){
            if(vectorConTodosLosElementos.get(j).equals(vectorInstruccionesC[k])){
                todoIdentificado.add(vectorConTodosLosElementos.get(j));
                todoIdentificado.add(vectorResultados[1]);
                k = vectorInstruccionesC.length+1;
                band2 = 0;
            }else{
                band2 += 1;
            }
        }
        return band2;
    }
    
    public int veriInstrucciones(Vector<String> vectorConTodosLosElementos, int j, int band3){
        for(int k = 0; k<vectorInstrucciones.length; k += 1){
            if(vectorConTodosLosElementos.get(j).equals(vectorInstrucciones[k])){
                todoIdentificado.add(vectorConTodosLosElementos.get(j));
                todoIdentificado.add(vectorResultados[1]);
                k = vectorInstrucciones.length+1;
                band3 = 0;
            }else{
                band3 += 1;
            }
        }
        return band3;
    }
    
    public int verRegistros(Vector<String> vectorConTodosLosElementos, int j, int band4){
        for(int k = 0; k<vectorRegistros.length; k += 1){
            if(vectorConTodosLosElementos.get(j).equals(vectorRegistros[k])){
                todoIdentificado.add(vectorConTodosLosElementos.get(j));
                todoIdentificado.add(vectorResultados[2]);
                k = vectorRegistros.length+1;
                band4 = 0;
            }else{
                band4 += 1;
            }
        }
        return band4;
    }
    
    public int verCND(Vector<String> vectorConTodosLosElementos, int j, int band5){
        for(int k = 0; k<vectorInstrucciones.length; k += 1){
            try{
                int a = Integer.parseInt(vectorConTodosLosElementos.get(j));
                todoIdentificado.add(vectorConTodosLosElementos.get(j));
                todoIdentificado.add(vectorResultados[4]);
                k = vectorInstrucciones.length+1;
                band5 = 0;
            }catch(Exception h){
                band5 = 1;
            }
        }
        return band5;
    }
    
    public int verCNH(Vector<String> vectorConTodosLosElementos, int j, int band8, String temp){
        try{
            int a = Integer.parseInt(vectorConTodosLosElementos.get(j).charAt(0)+"");
            a = vectorConTodosLosElementos.get(j).length();
            if(vectorConTodosLosElementos.get(j).charAt(a-1) == 'H'){
                for(int k = 0; k < vectorConTodosLosElementos.get(j).length()-1; k += 1){
                    if((vectorConTodosLosElementos.get(j).charAt(k) > 47  && vectorConTodosLosElementos.get(j).charAt(k) < 58)||(vectorConTodosLosElementos.get(j).charAt(k) > 64  && vectorConTodosLosElementos.get(j).charAt(k) < 71)){                                
                        temp += vectorConTodosLosElementos.get(j).charAt(k);  
                    }else{
                        band8 += 1;
                    }                       
                }
                if(band8 < 1){
                    todoIdentificado.add(vectorConTodosLosElementos.get(j));
                    todoIdentificado.add(vectorResultados[5]);
                    band8 = 0;
                }
            }else{
                band8 += 1;
            }
        }catch(Exception h){
            band8 = 1;
        }   
        return band8;
    }
    
    public int verCNB(Vector<String> vectorConTodosLosElementos, int j, int band9, String temp){
        try{
            int a = Integer.parseInt(vectorConTodosLosElementos.get(j).charAt(0)+"");
            a = vectorConTodosLosElementos.get(j).length();
            if(vectorConTodosLosElementos.get(j).charAt(a-1) == 'B'){
                for(int k = 0; k < vectorConTodosLosElementos.get(j).length()-1; k += 1){
                    if(vectorConTodosLosElementos.get(j).charAt(k) == '0'  || vectorConTodosLosElementos.get(j).charAt(k) == '1'){                                
                        temp += vectorConTodosLosElementos.get(j).charAt(k);  
                    }else{
                        band9 = 1;
                    }                       
                }
                if(band9 < 1){
                    todoIdentificado.add(vectorConTodosLosElementos.get(j));
                    todoIdentificado.add(vectorResultados[6]);
                    band9 = 0;
                }
            }else{
                band9 += 1;
            }
        }catch(Exception h){
            band9 = 1;
        } 
        return band9;
    }
    
    public int verCNC(Vector<String> vectorConTodosLosElementos, int band7, String temp){
        temp = "";
            int bandComa = 0;
            int tempCont = 0;
            char aux = ' ';
            int opc = 0;
            if(vectorConTodosLosElementos.get(j).charAt(0) == 39 || vectorConTodosLosElementos.get(j).charAt(0) == 34){
                if(vectorConTodosLosElementos.get(j).charAt(0) == 39){
                    opc = 1;
                }
                while(bandComa < 2){
                    for(int k = 0; k < vectorConTodosLosElementos.get(j).length(); k += 1){
                        if(vectorConTodosLosElementos.get(j).charAt(k) != 39  && vectorConTodosLosElementos.get(j).charAt(k) != 34){                                
                            temp += vectorConTodosLosElementos.get(j).charAt(k);  
                        }else{
                            temp += vectorConTodosLosElementos.get(j).charAt(k);
                            bandComa +=1 ;
                        }                       
                    }
                    if(bandComa < 2){
                        j += 1;
                        temp += " ";
                        tempCont += 1;
                        if(tempCont == 3){
                            if(opc == 1){
                                aux = (char) 39;
                                temp += aux;
                                bandComa = 2;
                                j -= 1;
                            }else{
                                aux = (char) 34;
                                temp += aux;
                                bandComa = 2;
                                j -= 1;
                            }
                        }
                    }
                }
                todoIdentificado.add(temp);
                todoIdentificado.add(vectorResultados[7]);
                band7 = 0;
            }else{
                band7 += 1;
            }
        return band7;
    }
    
    public int veriSimbolos(Vector<String> vectorConTodosLosElementos, int j, int band6, String temp){
        try{
            int auxNumTemp = Integer.parseInt(vectorConTodosLosElementos.get(j).charAt(0)+"");
            band6 += 1;
        }catch(Exception exs){
            temp = "";
            int lim;
            if(vectorConTodosLosElementos.get(j).charAt(vectorConTodosLosElementos.get(j).length()-1) == ':'){
                lim = vectorConTodosLosElementos.get(j).length()-1;
            }else{
                lim = vectorConTodosLosElementos.get(j).length();
            }
            if(vectorConTodosLosElementos.get(j).charAt(0) > 64 && vectorConTodosLosElementos.get(j).charAt(0) < 91){
                if(vectorConTodosLosElementos.get(j).length() <11){
                    for(int k = 0; k < lim; k += 1){
                        if((vectorConTodosLosElementos.get(j).charAt(k) > 47 && vectorConTodosLosElementos.get(j).charAt(k) < 58) || (vectorConTodosLosElementos.get(j).charAt(k) > 64 && vectorConTodosLosElementos.get(j).charAt(k) < 91)){
                            temp += vectorConTodosLosElementos.get(j).charAt(k);
                        }else{
                            k = lim;
                            band6 = 6;
                        }
                    }
                    if(band6 < 1){
                        todoIdentificado.add(temp);
                        todoIdentificado.add(vectorResultados[3]);
                        band6 = 0;
                    }
                }else{
                    band6 += 1;
                }
            }else{
                band6 += 1;
            }
        }
        return band6;
    }
    
    public void ver(Vector<String> vectorConTodosLosElementos, JTextArea cuadro){
        for (int j = 0; j<vectorConTodosLosElementos.size(); j += 1){
            cuadro.insert(vectorConTodosLosElementos.get(j)+"\n", cuadro.getText().length());
        }
        cuadro.insert("\n----------------------------------------------------------------------\n", cuadro.getText().length());
    }
    
    public void AjustaComas(Vector<String> vectorConTodosLosElementos, Vector<String> vectorSalida){
        for(int j = 0; j < vectorConTodosLosElementos.size(); j += 1){
            String parts[] = vectorConTodosLosElementos.get(j).split(",");
            for(int k = 0; k < parts.length; k += 1){
                vectorSalida.add(parts[k]);
            }
        }
    }
    
    public void AjustaCorchetes(Vector<String> vectorConTodosLosElementos, Vector<String> vectorSalida){        
        for(int j = 0; j < vectorConTodosLosElementos.size(); j += 1){
            String parts[] = vectorConTodosLosElementos.get(j).split("\\[");
            try{
                for(int k = 0; k < parts.length; k += 1){
                    if(parts[k] == ""){
                        vectorSalida.add("["+parts[k]);
                    }else if(parts[k].charAt(parts.length-1) == ']'){
                        vectorSalida.add("["+parts[k]);
                    }else{
                        vectorSalida.add(parts[k]);
                    }
                }
            }catch(Exception ex){
                vectorSalida.add(vectorConTodosLosElementos.get(j));
            }
        }
    }
    
    public void AjustaDUP(Vector<String> vectorConTodosLosElementos, Vector<String> vectorSalida){
        String dup;
        String temp;
        for(int j = 0; j < vectorConTodosLosElementos.size(); j += 1){ 
            dup = "";
            temp = "";
            try{
                dup += vectorConTodosLosElementos.get(j).charAt(0)+"";
                dup += vectorConTodosLosElementos.get(j).charAt(1);
                dup += vectorConTodosLosElementos.get(j).charAt(2);
                dup += vectorConTodosLosElementos.get(j).charAt(3);
                if(dup.equals("DUP(")){
                    //System.out.println(vectorConTodosLosElementos.get(j));
                    //System.out.println("Entro");
                    int i = 4;
                    while(vectorConTodosLosElementos.get(j).charAt(i) != ')'){
                        temp += vectorConTodosLosElementos.get(j).charAt(i);
                        i += 1;
                    }                                        
                    vectorSalida.add("DUP("+temp+")");
                    vectorSalida.add(temp);
                }else{
                    //System.out.println("Entro");                   
                    vectorSalida.add(vectorConTodosLosElementos.get(j));
                }
            }catch(Exception es){
                vectorSalida.add(vectorConTodosLosElementos.get(j));
            }
        }       
    }
    
    public String queEs(String elemento){
        String tipo = "";
        String temp = "";
        if(elemento.endsWith("H")){
            for(int k = 0; k < elemento.length()-1; k += 1){
                if((elemento.charAt(k) > 47  && elemento.charAt(k) < 58)||(elemento.charAt(k) > 64  && elemento.charAt(k) < 71)){                                
                    temp += elemento.charAt(k);  
                }else{
                    k = elemento.length();
                    temp = "";
                }                       
            }
            if(!"".equals(temp)){
                tipo = vectorResultados[5];
            }
        }else if(elemento.endsWith("B")){
            for(int k = 0; k < elemento.length()-1; k += 1){
                if(elemento.charAt(k) > 47  && elemento.charAt(k) < 50){                                
                    temp += elemento.charAt(k);  
                }else{
                    k = elemento.length();
                    temp = "";
                }                       
            }
            if(!"".equals(temp)){
                tipo = vectorResultados[6];
            }
        }else if(elemento.charAt(elemento.length()-1) == 39 || elemento.charAt(elemento.length()-1) == 34){
            tipo = vectorResultados[7];
        }else{
            try{
                int a = Integer.parseInt(elemento);
                tipo = vectorResultados[4];
            }catch(Exception es){
                tipo = vectorResultados[8];
            }
        }
        return tipo;
    }

    public void identificador(Vector<String> vectorConTodosLosElementos, JTextArea cuadro){
        int band, band2, band3, band4, band5, band6, band7, band8, band9, band10;
        Boolean condi,condiT;
        Vector<String> aux;

        for (j = 0; j<vectorConTodosLosElementos.size(); j += 1){
            //System.out.println(vectorConTodosLosElementos.get(j));
            band = 0;band2 = 0;band3 = 0;band4 = 0;band5 = 0;band6 = 0;band7 = 0;band8 = 0;band9 = 0;band10 = 0;
            //Identificador de Pseudoinstrucciones compuestas
            band = veriPinstrucciones(vectorConTodosLosElementos, j, band);
            //Identificador de Instrucciones compuestas
            band2 = veriInstruccionesC(vectorConTodosLosElementos, j, band2);
            //Identificador de Instrucciones
            band3 = veriInstrucciones(vectorConTodosLosElementos, j, band3);
            //Identificador de Registros
            band4 = verRegistros(vectorConTodosLosElementos, j, band4);
            //Identificador de constantes numericas decimales
            band5 = verCND(vectorConTodosLosElementos, j, band5);
            //Identificador de constantes numericas Hexadecimales
            String temp = "";
            condiT = band > 0 && band2 > 0 && band3 > 0 && band4 > 0 && band5 > 0;
            if(condiT){
                band8 = verCNH(vectorConTodosLosElementos, j, band8, temp);
            }else{
                band8 = 1;
            }
            //Identificador de constantes numericas Binarias
            temp = "";
            condiT = band > 0 && band2 > 0 && band3 > 0 && band4 > 0 && band5 > 0 && band8 > 0;
            if(condiT){
                band9 = verCNB(vectorConTodosLosElementos, j, band9, temp);           
            }else{
                band9 = 1;
            } 
            //Identificador de constantes tipo caracter
            condiT = band > 0 && band2 > 0 && band3 > 0 && band4 > 0 && band5 > 0 && band8 > 0 && band9 > 0;
            if(condiT){                
                band7 = verCNC(vectorConTodosLosElementos, band7, temp);
            }else{
                band7 = 1;
            }                            
            //Identificador de Simbolos   
            condiT = band > 0 && band2 > 0 && band3 > 0 && band4 > 0 && band5 > 0 && band7 > 0 && band8 > 0 && band9 > 0;
            if(condiT){               
                band6 = veriSimbolos(vectorConTodosLosElementos, j, band6, temp);
            }else{
                band6 = 1;
            } 
            //Identificador de DUP
            condiT = band > 0 && band2 > 0 && band3 > 0 && band4 > 0 && band5 > 0 && band7 > 0 && band8 > 0 && band9 > 0 && band6 > 0;
            if(condiT){
                String dup;
                dup = "";
                try{
                    dup += vectorConTodosLosElementos.get(j).charAt(0)+"";
                    dup += vectorConTodosLosElementos.get(j).charAt(1);
                    dup += vectorConTodosLosElementos.get(j).charAt(2);
                    dup += vectorConTodosLosElementos.get(j).charAt(3);
                    if(dup.equals("DUP(")){                                      
                        todoIdentificado.add(vectorConTodosLosElementos.get(j));
                        todoIdentificado.add(vectorResultados[0]);
                        band10 = 0;
                    }else{
                        band10 = 1;
                    }
                }catch(Exception es){
                    band10 = 1;
                }
            }
            //System.out.println(band+","+band2+","+band3+","+band4+","+band5+","+band6+","+band7+","+band8+","+band9+","+band10);
            //System.out.println(vectorConTodosLosElementos.get(j));
            //No identificado
            condi = vectorConTodosLosElementos.get(j).equals("") == false && condiT  && band10 > 0;            
            if(condi){
                todoIdentificado.add(vectorConTodosLosElementos.get(j));
                todoIdentificado.add(vectorResultados[8]);
            }
        }
 
        for (int j = 0; j<todoIdentificado.size()-1; j += 2){
            cuadro.insert(todoIdentificado.get(j)+"---------> "+todoIdentificado.get(j+1)+"\n", cuadro.getText().length());
        }

    }
}
