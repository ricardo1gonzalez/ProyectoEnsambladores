package Procesos;

import Ventanas.VentanaPrincipal;
import Ventanas.VentanaSintaxis;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

public class AnalizadorDeSimbolos {
    public String[] vectorRegistros = {"AX","AH","AL","BX","BH","BL","CX","CH","CL","DX","DH","DL","SI","DI","SS","DS","ES","CS","BP","SP","IP"};
    public String[] vectorInstruccionesC = {"SCASW","HLT","XLATB","INTO","AAD","MOVSW"};
    public String[] vectorCompuestasP = {".CODE SEGMENT",".DATA SEGMENT",".STACK SEGMENT","ENDS","DB","DW","EQU","DUP","BYTE PTR","WORD PTR"};
    public String[] vectorInstrucciones = {"IMUL","INC","INT","POP","SAR","ADC","ADD","SUB","JBE","JNGE","JNA","JS","JL","JNZ"};
    public String[] vectorCompuestasS = {"BYTE PTR","WORD PTR"};
    public String[] vectorReservadas = {".CODE SEGMENT",".DATA SEGMENT",".STACK SEGMENT","ENDS"};
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
    
    public void imprimirSystem(Vector<String> vectorConTodosLosElementos){
        for (int j = 0; j<vectorConTodosLosElementos.size(); j += 1){
            System.out.println(vectorConTodosLosElementos.get(j)+"\n");
        }
        System.out.println("\n----------------------------------------------------------------------\n");
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
    
    public void AjustaCorchetes2(Vector<String> vectorConTodosLosElementos, Vector<String> vectorSalida){         
        String temp;       
        for(int j = 0; j < vectorConTodosLosElementos.size(); j += 1){
            vectorSalida.add(vectorConTodosLosElementos.get(j));
            String parts[] = vectorConTodosLosElementos.get(j).split("\\+");
            try{
                if(vectorConTodosLosElementos.get(j).endsWith("]") && vectorConTodosLosElementos.get(j).charAt(0) == '['){
                    for(int k = 0; k < parts.length; k += 1){
                        //System.out.println(parts[k]);
                        temp = "";
                        for(int m=0;m<parts[k].length();m++){
                            if(parts[k].charAt(m) != '[' && parts[k].charAt(m) != ']'){
                                temp += parts[k].charAt(m);
                            }
                        }
                        
                        vectorSalida.add(temp);
                    }
                }
            }catch(Exception ex){
                vectorSalida.add(vectorConTodosLosElementos.get(j));
            }            
        }
    }
    
    public void AjustaCorchetes3(Vector<String> vectorConTodosLosElementos, Vector<String> vectorSalida){  
        String temp;
        for(int j = 0; j < vectorConTodosLosElementos.size(); j += 1){
            vectorSalida.add(vectorConTodosLosElementos.get(j));
            String parts[] = vectorConTodosLosElementos.get(j).split("\\:");
            try{
                if(vectorConTodosLosElementos.get(j).endsWith("]") && vectorConTodosLosElementos.get(j).charAt(0) == '['){
                    for(int k = 0; k < parts.length; k += 1){
                        temp = "";
                        for(int m=0;m<parts[k].length();m++){
                            if(parts[k].charAt(m) != '[' && parts[k].charAt(m) != ']'){
                                temp += parts[k].charAt(m);
                            }
                        }
                        vectorSalida.add(temp);
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

    public Vector<String> identificador(Vector<String> vectorConTodosLosElementos, JTextArea cuadro){
        int band, band2, band3, band4, band5, band6, band7, band8, band9, band10, band11;
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
            }else{
                band10 = 1;
            }
            //Identificador de []
            condiT = band > 0 && band2 > 0 && band3 > 0 && band4 > 0 && band5 > 0 && band7 > 0 && band8 > 0 && band9 > 0 && band6 > 0 && band10 > 0;
            if(condiT){
                if(vectorConTodosLosElementos.get(j).endsWith("]") && vectorConTodosLosElementos.get(j).charAt(0) == '['){
                    todoIdentificado.add(vectorConTodosLosElementos.get(j));
                    todoIdentificado.add("Estructura");
                    band11 = 0;
                }else{
                    band11 = 1;
                }
            }else{
                band11 = 1;
            }
            //System.out.println(band+","+band2+","+band3+","+band4+","+band5+","+band6+","+band7+","+band8+","+band9+","+band10);
            //System.out.println(vectorConTodosLosElementos.get(j));
            //No identificado
            condi = vectorConTodosLosElementos.get(j).equals("") == false && condiT  && band11 > 0;            
            if(condi){
                todoIdentificado.add(vectorConTodosLosElementos.get(j));
                todoIdentificado.add(vectorResultados[8]);
            }
        }
 
        for (int j = 0; j<todoIdentificado.size()-1; j += 2){
            cuadro.insert(todoIdentificado.get(j)+"---------> "+todoIdentificado.get(j+1)+"\n", cuadro.getText().length());
        }
        return todoIdentificado;
    }
    
    public void llnaSimple(DefaultTableModel tabla, Vector<String> vecto){
        try {
            VentanaPrincipal venDat = new VentanaPrincipal();
            String direc = new String(venDat.getTxtRuta().getText());
            Scanner scn = null;
            System.out.println(direc);
            File archi = new File(direc);
            try {
                scn = new Scanner(archi);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            String a = new String();
            String b = new String();
            while (scn.hasNextLine()) {
                a = scn.nextLine();
                System.out.println(a);
                //vecto.add(a.toUpperCase());
                Object[] newRow = {a,"---","----"};
                tabla.addRow(newRow);
            }
        } catch (IOException ex) {
            Logger.getLogger(VentanaSintaxis.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int[] SegmentoD(Vector<String> vecto){
        int aux[] = {0,0};
        for(int i = 0; i < vecto.size(); i += 1){
            if(vecto.get(i).toUpperCase().equals(".DATA SEGMENT")){
                aux[0] = i;
            }else if(vecto.get(i).toUpperCase().equals("ENDS")){
                aux[1] = i;
                i = vecto.size();
            }
        }
        return aux;
    }
    
    public void iniTS(DefaultTableModel m, Vector<String> vecto, int [] limi){
        for(int i = 0; i < (limi[1] - limi[0]); i += 1){
            Object[] datos = {(i+1)+"","---","---","---"};
            m.addRow(datos);
        }        
    }
    
    public void recti(DefaultTableModel m, String a, int limi){        
        String[] p = a.split(" ");
        for(int i = 0; i < p.length; i += 1){
            if(p[i].equals("") == false){
               m.setValueAt(p[i], limi, 2);
               m.setValueAt("Variable", limi, 4);
               i = p.length;
            }
        }
        for(int i = 0; i < p.length; i += 1){
            if(p[i].matches("\\s*DB\\s*")){
               m.setValueAt("8 bits", limi, 3); 
               i = p.length;
            }else if(p[i].matches("\\s*DW\\s*")){
               m.setValueAt("16 bits", limi, 3);
               i = p.length;
            }else if(p[i].matches("\\s*EQU\\s*")){
               m.setValueAt("16 bits", limi, 3);
               m.setValueAt("Constante", limi, 4);
               i = p.length;
            }
        }
        for(int i = 0; i < p.length; i += 1){
            if(p[i].matches("\\s*^\".+\"*\\s*")){
               m.setValueAt(p[i], limi, 4); 
               i = p.length;
            }else if(p[i].matches("\\s*^\'.+\\s*")){
               System.out.println(p[i]); 
               i = p.length;
            }else if(p[i].matches("\\s*0*[A-F0-9]+H\\s*")){
               System.out.println(p[i]+" Constante tipo numerico hexadecimal"); 
               i = p.length;
            }else if(p[i].matches("\\s*0*[01]+B\\s*")){
               System.out.println(p[i]+" Constante tipo numerico binario"); 
               i = p.length;
            }
        }
    }
    
    public void CreaTablaDeSimbolos(DefaultTableModel m, Vector<String> vecto, int [] limi){
        int numInstru = 0;
        int band = 0, band2 = 0, band3 = 0;
        for(int i = 0; i < vecto.size(); i += 1){
            if(vecto.get(i).matches("\\s*\\.DATA\\s+SEGMENT\\s*")){
                m.setValueAt("Sintáxis correcta", i, 2);
                m.setValueAt("La definición es correcta", i, 3);
                band = i;
                band3 = 1;
            }else if(vecto.get(i).matches("\\s*ENDS\\s*")&& band3 > 0){
                m.setValueAt("Sintáxis correcta", i, 2);
                m.setValueAt("La definición es correcta", i, 3);
                band2 = i;
                i = vecto.size();
            }
        } 
        for(int i = band; i < band2; i++){
            if(vecto.get(i).matches("\\s*[A-Z]{1}[A-Z0-9]*\\s+D[B|W]{1}\\s+[0*[A-F0-9]+H|B*]+\\s*")){
                m.setValueAt("Sintáxis Correcta", i, 2);
                m.setValueAt("La definición es correcta", i, 3);
                band = i;
            }else{
                m.setValueAt("Sintáxis Incorrecta", i, 2);
                m.setValueAt("La definición no coincide con las especificaciones", i, 3);
                band = i;
            }
        }
    }
    
    public void AnalizadorDePila(DefaultTableModel m, Vector<String> vecto){
        int band = 0, band2 = 0, band3 = 0, band4 = 0, band5 = 0;
        for(int i = 0; i < vecto.size(); i += 1){
            if(vecto.get(i).matches("\\s*\\.STACK\\s+SEGMENT\\s*")){
                m.setValueAt("Sintáxis correcta", i, 2);
                m.setValueAt("La definición es correcta", i, 3);
                band = i;
                band3 = 1;
            }else if(vecto.get(i).matches("\\s*ENDS\\s*") && band3 > 0){
                m.setValueAt("Sintáxis correcta", i, 2);
                m.setValueAt("La definición es correcta", i, 3);
                band2 = i;
                i = vecto.size();
            }
        }
        for(int i = band+1; i < band2; i++){
            if(vecto.get(i).matches("\\s*DW\\s+[0*[A-F0-9]+H|B*]+\\s+DUP\\([0*[A-F0-9]+H|B*]+\\)\\s*")){
                m.setValueAt("Sintáxis Correcta", i, 2);
                m.setValueAt("La definición es correcta", i, 3);
                band = i;
            }else{
                m.setValueAt("Sintáxis Incorrecta", i, 2);
                m.setValueAt("La definición no coincide con las especificaciones", i, 3);
                band = i;
            }
        }
    }
    /*  .data segment (pseudoinstrucción que identifica el inicio de la definición del segmento de datos)
        símbolo db constante caracter
        símbolo db constante numérica byte con/sin signo
        símbolo db constante numérica palabra sin signo dup (constante caracter byte)
        símbolo db constante numérica palabra sin signo dup (constante numérica byte con/sin signo)
        símbolo dw constante numérica palabra con/sin signo
        símbolo dw constante numérica palabra sin signo dup(constante numérica palabra con/sin signo)
        símbolo equ constante numérica palabra con/sin signo
        ends (pseudoinstrucción que identifica el fin de la definición de un segmento)*/
    public void AnalizadorDeDatos(DefaultTableModel m, Vector<String> vecto){
        int numInstru = 0;
        int band = 0, band2 = 0, band3 = 0;
        for(int i = 0; i < vecto.size(); i += 1){
            if(vecto.get(i).matches("\\s*\\.DATA\\s+SEGMENT\\s*")){
                m.setValueAt("Sintáxis correcta", i, 2);
                m.setValueAt("La definición es correcta", i, 3);
                band = i;
                band3 = 1;
            }else if(vecto.get(i).matches("\\s*ENDS\\s*")&& band3 > 0){
                m.setValueAt("Sintáxis correcta", i, 2);
                m.setValueAt("La definición es correcta", i, 3);
                band2 = i;
                i = vecto.size();
            }
        } 
        for(int i = band+1; i < band2; i++){
            if(vecto.get(i).matches("\\s*[A-Z]{1}[A-Z0-9]*\\s+D[B|W]{1}\\s+[0*[A-F0-9]+H|B*]+\\s*")){
                m.setValueAt("Sintáxis Correcta", i, 2);
                m.setValueAt("La definición es correcta", i, 3);
                band = i;
            }else if(vecto.get(i).matches("\\s*[A-Z]{1}[A-Z0-9]*\\s+EQU\\s+[0*[A-F0-9]+H|B*]+\\s*")){
                m.setValueAt("Sintáxis Correcta", i, 2);
                m.setValueAt("La definición es correcta", i, 3);
                band = i;
            }else if(vecto.get(i).matches("\\s*[A-Z]{1}[A-Z0-9]*\\s+D[B|W]{1}\\s+\".*\"\\s*")){
                m.setValueAt("Sintáxis Correcta", i, 2);
                m.setValueAt("La definición es correcta", i, 3);
                band = i;
            }else if(vecto.get(i).matches("\\s*[A-Z]{1}[A-Z0-9]*\\s+D[B|W]{1}\\s+\'.*\'\\s*")){
                m.setValueAt("Sintáxis Correcta", i, 2);
                m.setValueAt("La definición es correcta", i, 3);
                band = i;
            }else{
                m.setValueAt("Sintáxis Incorrecta", i, 2);
                m.setValueAt("La definición no coincide con las especificaciones", i, 3);
                band = i;
            }
        }
    }
    
    public void AnalizadorDeCodigo(DefaultTableModel m, Vector<String> vecto){
        int band = 0, band2 = 0, band3 = 0, band4 = 0, band5 = 0;
        for(int i = 0; i < vecto.size(); i += 1){
            //System.out.println(vecto.get(i)+" es "+vecto.get(i).toUpperCase().equals(".CODE SEGMENT"));
            if(vecto.get(i).matches("\\s*\\.CODE\\sSEGMENT\\s*")){
                m.setValueAt("Sintáxis correcta", i, 2);
                m.setValueAt("La definición es correcta", i, 3);
                band = i;
                band3 = 1;
            }else if(vecto.get(i).matches("\\s*ENDS\\s*") && band3 > 0){
                m.setValueAt("Sintáxis correcta", i, 2);
                m.setValueAt("La definición es correcta", i, 3);
                band2 = i;
                i = vecto.size();
            }
        }
        //System.out.println(vecto.size());
        for(int i = band+1; i < band2; i++){
            if(vecto.get(i).matches("^SCASW|^SCASW[\\s]*|\\s*SCASW|\\s*SCASW\\s*")){
                m.setValueAt("Sintáxis Correcta", i, 2);
                m.setValueAt("La definición es correcta", i, 3);
                band = i;
            }else if(vecto.get(i).matches("^HLT|^HLT[\\s]*|\\s*HLT|\\s*HLT\\s*")){
                m.setValueAt("Sintáxis Correcta", i, 2);
                m.setValueAt("La definición es correcta", i, 3);
                band = i;
            }else if(vecto.get(i).matches("^XLATB|^XLATB[\\s]*|\\s*XLATB|\\s*XLATB\\s*")){
                m.setValueAt("Sintáxis Correcta", i, 2);
                m.setValueAt("La definición es correcta", i, 3);
                band = i;
            }else if(vecto.get(i).matches("^INTO|^INTO[\\s]*|\\s*INTO|\\s*INTO\\s*")){
                m.setValueAt("Sintáxis Correcta", i, 2);
                m.setValueAt("La definición es correcta", i, 3);
                band = i;
            }else if(vecto.get(i).matches("^AAD|^AAD[\\s]*|\\s*AAD|\\s*AAD\\s*")){
                m.setValueAt("Sintáxis Correcta", i, 2);
                m.setValueAt("La definición es correcta", i, 3);
                band = i;
            }else if(vecto.get(i).matches("^MOVSW|^MOVSW[\\s]*|\\s*MOVSW|\\s*MOVSW\\s*")){
                m.setValueAt("Sintáxis Correcta", i, 2);
                m.setValueAt("La definición es correcta", i, 3);
                band = i;
            }else if(vecto.get(i).matches("\\s*IMUL\\s+[A-Z][0-9]+\\s*")){
                m.setValueAt("Sintáxis Correcta", i, 2);
                m.setValueAt("La definición es correcta", i, 3);
                band = i;
            }else if(vecto.get(i).matches("\\s*.:+\\s*")){
                m.setValueAt("Sintáxis Correcta", i, 2);
                m.setValueAt("La definición es correcta", i, 3);
                band = i;
            }else{
                m.setValueAt("Sintáxis Incorrecta", i, 2);
                m.setValueAt("La definición no coincide con las especificaciones", i, 3);
                band = i;
            }
        }
    }
    
    public void preparaInstu(Vector<String> vecto, Vector<String> vecto2, DefaultTableModel m){
        String aux = "";
        for(int i = 0; i < vecto.size(); i += 1){
            for(int j = 0; j < vectorInstruccionesC.length;j += 1){
                //System.out.println(vectorInstruccionesC[j]+" con "+vecto.get(i)+" es "+vecto.get(i).equals(vectorInstruccionesC[j]));
                if(vecto.get(i).equals(vectorInstruccionesC[j])){
                    if(vecto.get(i).equals(vectorInstruccionesC[j])){
                        
                    }
                }
            }
            /*for(int j = 0; j < vectorInstrucciones.length;j += 1){
                //System.out.println(vectorInstruccionesC[j]+" con "+vecto.get(i)+" es "+vecto.get(i).equals(vectorInstruccionesC[j]));
                if(vecto.get(i).equals(vectorInstrucciones[j])){
                    if(vecto.get(i).equals(vectorInstruccionesC[j])){
                        
                    }
                }
            }
            for(int j = 0; j < vectorReservadas.length;j += 1){
                //System.out.println(vectorInstruccionesC[j]+" con "+vecto.get(i)+" es "+vecto.get(i).equals(vectorInstruccionesC[j]));
                if(vecto.get(i).equals(vectorReservadas[j])){
                    if(vecto.get(i).equals(vectorInstruccionesC[j])){
                        
                    }
                }
            }*/
            Object[] datos = {(i+1)+"",vecto.get(i),"---","---"};
            m.addRow(datos);
        }  
    }
}
