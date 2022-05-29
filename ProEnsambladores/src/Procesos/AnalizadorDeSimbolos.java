package Procesos;

import static Procesos.AnalizadorDeSimbolos.veriConstNum;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.table.DefaultTableModel;

public class AnalizadorDeSimbolos {
    public String[] vectorRegistros = {"AX","AH","AL","BX","BH","BL","CX","CH","CL","DX","DH","DL","SI","DI","SS","DS","ES","CS","BP","SP","IP"};
    public String[] vectorInstruccionesC = {"SCASW","HLT","XLATB","INTO","AAD","MOVSW"};
    public String[] vectorInstruccionesCC = {"AF","F4","D7","CE","D5 0A","A5"};
    public String[] vectorCompuestasP = {".CODE SEGMENT",".DATA SEGMENT",".STACK SEGMENT","ENDS","DB","DW","EQU","DUP","BYTE PTR","WORD PTR"};
    public String[] vectorInstrucciones = {"IMUL","INC","INT","POP","SAR","ADC","ADD","SUB","JBE","JNGE","JNA","JS","JL","JNZ"};
    public String[] vectorInstruccionesCod = {"","","","","","","","","0F 86","0F 8C","0F 86","0F 88","0F 8C","0F 85"};
    public String[] vectorSaltos = {"JBE","JNGE","JNA","JS","JL","JNZ"};
    public String[] vectorSaltosCod = {"0F 86","0F 8C","0F 86","0F 88","0F 8C","0F 85"};
    public String[] vectorReg = {"AX", "BX", "CX", "DX", "AH", "AL", "BL", "BH", "CH", "CL", "DH", "DL", "DI", "SI", "BP", "SP"};
    public String[] vectorReg2 = {"DI", "SI", "BP", "SP"};
    public String[] vectorSReg = {"SS","DS","ES","CS"};
    public String[] vectorCompuestasS = {"BYTE PTR","WORD PTR"};
    public String[] vectorReservadas = {".CODE SEGMENT",".DATA SEGMENT",".STACK SEGMENT","ENDS"};
    public String[] vectorResultados = {"Pseudoinstrucción","Instrucción","Registro","Símbolo","Constante numérica decimal","Constante numérica hexadecimal","Constante numérica binaria","Constante tipo caracter","Elemento no identificado"};
    public Vector<String> todoIdentificado = new Vector<String>();
    public int j;
    public final String mensaje = "Sintáxis correcta";
    
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
    
    public void iniTS(DefaultTableModel m, int limi){
        for(int i = 0; i < limi; i += 1){
            Object[] datos = {(i+1)+"","---","---","---","---"};
            m.addRow(datos);
        }        
    }
    
    public void iniTS2(DefaultTableModel m, int limi){
        for(int i = 0; i < limi; i += 1){
            //System.out.println(m.getValueAt(i, 0));
            m.setValueAt(" ", i, 0);
        }        
    }
    
    public void corrijeTable(DefaultTableModel m){
        int cont;
        //Nombre
        for(int i = m.getRowCount()-1; i > -1; i -= 1){
            cont = 0;
            for(int j = 1; j < m.getColumnCount(); j += 1){
                //System.out.println(m.getValueAt(i, j).equals("---"));
                if(m.getValueAt(i,j).equals("---")||m.getValueAt(i,j).equals("\\s")){
                    cont += 1;
                }
            }
            //System.out.println(cont);
            if(cont > 0){
                m.removeRow(i);
            }
        }        
    }
    
    public void recti(DefaultTableModel m, String a, int lim){        
        String[] p = a.split(" ");
        int bandT = 0;
        //Nombre
        for(int i = 0; i < p.length; i += 1){
            if(p[i].matches("\\s*[A-Z]{1}[A-Z0-9]{0,9}")){
               m.setValueAt(p[i], lim, 1);
               m.setValueAt("Variable", lim, 2);
               i = p.length;
            }
        }
        //
        for(int i = 0; i < p.length; i += 1){
            if(p[i].matches("\\s*DB\\s*")){
               m.setValueAt("DB", lim, 3); 
               i = p.length;
               bandT = 1;
            }else if(p[i].matches("\\s*DW\\s*")){
               m.setValueAt("DW", lim, 3);
               //m.removeRow(i);
               i = p.length;
               bandT = 2;
            }else if(p[i].matches("\\s*EQU\\s*")){
               m.setValueAt("DW", lim, 3);
               m.setValueAt("Constante", i, 2);
               i = p.length;
               bandT = 2;
            }
        }
        if(bandT == 1){
            for(int i = 0; i < p.length; i += 1){
                if(p[i].matches("\\s*^\".?\"*\\s*")){
                   m.setValueAt(p[i], lim, 4); 
                   i = p.length;
                }else if(p[i].matches("\\s*^\'.?\\s*")){
                   m.setValueAt(p[i], lim, 4);  
                   i = p.length;
                }else if(p[i].matches("\\s*0*[A-F0-9]{0,2}H\\s*")){
                   m.setValueAt(p[i], lim, 4);  
                   i = p.length;
                }else if(p[i].matches("\\s*[0-9]{0,3}\\s*")){
                   m.setValueAt(p[i], lim, 4);  
                   i = p.length;
                }else if(p[i].matches("\\s*0*[01]{0,8}B\\s*")){
                   m.setValueAt(p[i], lim, 4);  
                   i = p.length;
                }
            }
        }else if(bandT == 2){
            for(int i = 0; i < p.length; i += 1){
                if(p[i].matches("\\s*^\".{0,2}\"*\\s*")){
                   m.setValueAt(p[i], lim, 4); 
                   i = p.length;
                }else if(p[i].matches("\\s*^\'.{0,2}\\s*")){
                   m.setValueAt(p[i], lim, 4);  
                   i = p.length;
                }else if(p[i].matches("\\s*0*[A-F0-9]{0,4}H\\s*")){
                   m.setValueAt(p[i], lim, 4);  
                   i = p.length;
                }else if(p[i].matches("\\s*[0-9]{0,5}\\s*")){
                   m.setValueAt(p[i], lim, 4);  
                   i = p.length;
                }else if(p[i].matches("\\s*0*[01]{0,16}B\\s*")){
                   m.setValueAt(p[i], lim, 4);  
                   i = p.length;
                }
            }
        }
    }
    
    public void AgregaEtiq(DefaultTableModel m, Vector<String> vecto){
        int band = 0;
        int band2 = 0;
        int band3 = 0;
        for(int i = 0; i < vecto.size(); i += 1){
            if(vecto.get(i).matches("\\s*\\.CODE\\s+SEGMENT\\s*")){
                //m.setValueAt("Sintáxis correcta", i, 2);
                //m.setValueAt("La definición es correcta", i, 3);
                band = i;
                band3 = 1;
            }else if(vecto.get(i).matches("\\s*ENDS\\s*")&& band3 > 0){
                //m.setValueAt("Sintáxis correcta", i, 2);
                //m.setValueAt("La definición es correcta", i, 3);
                band2 = i;
                i = vecto.size();
            }
        }
        for(int i = band+1; i < band2; i++){
            //System.out.println(i);
            if(vecto.get(i).matches("\\s*[A-Z]{1}[A-Z0-9]{1,9}:\\s*")){
                String a[] = vecto.get(i).split(":");
                Object[] datos = {(i+1)+"",a[0],"Etiqueta","*","*"};
                m.addRow(datos);
            }
        }
    }
    
    public void CreaTablaDeSimbolos(DefaultTableModel m2, DefaultTableModel m, Vector<String> vecto){
        int numInstru = 0;
        int band = 0, band2 = 0, band3 = 0;
        for(int i = 0; i < vecto.size(); i += 1){
            if(vecto.get(i).matches("\\s*\\.DATA\\s+SEGMENT\\s*")){
                //m.setValueAt("Sintáxis correcta", i, 2);
                //m.setValueAt("La definición es correcta", i, 3);
                band = i;
                band3 = 1;
            }else if(vecto.get(i).matches("\\s*ENDS\\s*")&& band3 > 0){
                //m.setValueAt("Sintáxis correcta", i, 2);
                //m.setValueAt("La definición es correcta", i, 3);
                band2 = i;
                i = vecto.size();
            }
        } 
        //iniTS(m2, band2 - band);
        for(int i = band+1; i < band2; i++){
            String regex = "\\s*(\\w+)\\s*(\\w+)\\s*(\\w+)\\s*";
            String regex2 = "\\s*(\\w+)\\s*(\\w+)\\s*(\\w+)\\s*DUP\\s*\\((\\w+)\\)\\s*";
            String regexb = "\\s*(\\w+)\\s*(\\w+)\\s*\"?\'?(\\w*|\\s*)\"?\'?\\s*";
            String regex2b = "\\s*(\\w+)\\s*(\\w+)\\s*(\\w+)\\s*DUP\\s*\\(\"?\'?(\\w*|\\s*|\\$)\"?\'?\\)\\s*";
            Pattern pat = Pattern.compile(regex);
            Pattern pat2 = Pattern.compile(regex2);
            Pattern pat3 = Pattern.compile(regexb);
            Pattern pat4 = Pattern.compile(regex2b);
            Matcher mat = pat.matcher(vecto.get(i));
            if(vecto.get(i).matches(regex)){
                mat = pat.matcher(vecto.get(i));
                if (mat.find()) {
                    //System.out.println(mat.group(3));
                    if(veriSimbol(mat.group(1)).equals("")){
                        switch (mat.group(2)) {
                            case "DB":
                                if(veriConstNum(mat.group(3),mat.group(2)).equals("")){
                                    m.setValueAt(mat.group(1), i, 1);
                                    m.setValueAt("Variable", i, 2);
                                    m.setValueAt(mat.group(2), i, 3);
                                    m.setValueAt(mat.group(3), i, 4);
                                }break;
                            case "DW":
                                if(veriConstNum(mat.group(3),mat.group(2)).equals("")){
                                    m.setValueAt(mat.group(1), i, 1);
                                    m.setValueAt("Variable", i, 2);
                                    m.setValueAt(mat.group(2), i, 3);
                                    m.setValueAt(mat.group(3), i, 4);
                                }break;
                            case "EQU":
                                if(veriConstNum(mat.group(3),"DW").equals("")){
                                    m.setValueAt(mat.group(1), i, 1);
                                    m.setValueAt("Constante", i, 2);
                                    m.setValueAt("DW", i, 3);
                                    m.setValueAt(mat.group(3), i, 4);
                                }break;
                            default:
                                break;
                        }
                    }
                }
            }else if(vecto.get(i).matches(regex2)){
                mat = pat2.matcher(vecto.get(i));
                if (mat.find()) {
                    //System.out.println(mat.group(4));
                    if(veriSimbol(mat.group(1)).equals("")){
                        switch (mat.group(2)) {
                            case "DB":
                                if(veriConstNum(mat.group(3),"DW").equals("")){
                                    if(veriConstNum(mat.group(4),"DW").equals("")){
                                        m.setValueAt(i+"", i, 0);
                                        m.setValueAt(mat.group(1), i, 1);
                                        m.setValueAt("Variable", i, 2);
                                        m.setValueAt(mat.group(2), i, 3);
                                        m.setValueAt(mat.group(3)+" DUP ("+mat.group(4)+")", i, 4);
                                    }
                                }break;
                            case "DW":
                                if(veriConstNum(mat.group(3),mat.group(2)).equals("")){
                                    if(veriConstNum(mat.group(4),mat.group(2)).equals("")){
                                        m.setValueAt(i+"", i, 0);
                                        m.setValueAt(mat.group(1), i, 1);
                                        m.setValueAt("Variable", i, 2);
                                        m.setValueAt(mat.group(2), i, 3);
                                        m.setValueAt(mat.group(3)+" DUP ("+mat.group(4)+")", i, 4);
                                    }
                                }break;
                            default:
                                break;
                        }
                    }
                }
            }else if(vecto.get(i).matches(regexb)){
                mat = pat3.matcher(vecto.get(i));
                if (mat.find()) {
                    //System.out.println(mat.group(3));
                    if(veriSimbol(mat.group(1)).equals("")){
                        switch (mat.group(2)) {
                            case "DB":
                                if(veriConsText(mat.group(3),mat.group(2),1).equals("")){
                                    m.setValueAt(i+"", i, 0);
                                    m.setValueAt(mat.group(1), i, 1);
                                    m.setValueAt("Variable", i, 2);
                                    m.setValueAt(mat.group(2), i, 3);
                                    m.setValueAt("'"+mat.group(3)+"'", i, 4);
                                }break;
                            case "DW":
                                if(veriConsText(mat.group(3),mat.group(2),1).equals("")){
                                    m.setValueAt(i+"", i, 0);
                                    m.setValueAt(mat.group(1), i, 1);
                                    m.setValueAt("Variable", i, 2);
                                    m.setValueAt(mat.group(2), i, 3);
                                    m.setValueAt("'"+mat.group(3)+"'", i, 4);
                                }break;
                            default:
                                break;
                        }
                    }
                }
            }else if(vecto.get(i).matches(regex2b)){
                mat = pat4.matcher(vecto.get(i));
                if (mat.find()) {
                    //System.out.println(mat.group(4));
                    if(veriSimbol(mat.group(1)).equals("")){
                        switch (mat.group(2)) {
                            case "DB":
                                if(veriConstNum(mat.group(3),"DW").equals("")){
                                    if(veriConsText(mat.group(4),"DW",0).equals("")){
                                        m.setValueAt(i+"", i, 0);
                                        m.setValueAt(mat.group(1), i, 1);
                                        m.setValueAt("Variable", i, 2);
                                        m.setValueAt(mat.group(2), i, 3);
                                        m.setValueAt(mat.group(3)+" DUP ('"+mat.group(4)+"')", i, 4);
                                    }break;
                                }
                            case "DW":
                                if(veriConstNum(mat.group(3),mat.group(2)).equals("")){
                                    if(veriConsText(mat.group(4),mat.group(2),0).equals("")){
                                        m.setValueAt(i+"", i, 0);
                                        m.setValueAt(mat.group(1), i, 1);
                                        m.setValueAt("Variable", i, 2);
                                        m.setValueAt(mat.group(2), i, 3);
                                        m.setValueAt(mat.group(3)+" DUP ('"+mat.group(4)+"')", i, 4);
                                    }break;
                                }
                            default:
                                break;
                        }
                    }
                }
            }
        }
        
    }
       
    public static Vector<String> llenaEtiq(DefaultTableModel m2){
        Vector<String> vecto = new Vector<String>();
        for(int i = m2.getRowCount() - 1; i > 0; i -= 1){
            if(m2.getValueAt(i, 2).equals("Etiqueta")){
                vecto.add((String) m2.getValueAt(i, 1));
            }
        }
        return vecto;
    }
    
    public static Vector<String> llenaVar(DefaultTableModel m2){
        Vector<String> vecto = new Vector<String>();
        for(int i = m2.getRowCount() - 1; i > 0; i -= 1){
            if(m2.getValueAt(i, 2).equals("Variable")){
                vecto.add((String) m2.getValueAt(i, 1));
            }
        }
        return vecto;
    }
    
    public static Vector<String> llenaConst(DefaultTableModel m2){
        Vector<String> vecto = new Vector<String>();
        for(int i = m2.getRowCount() - 1; i > 0; i -= 1){
            if(m2.getValueAt(i, 2).equals("Constante")){
                vecto.add((String) m2.getValueAt(i, 1));
            }
        }
        return vecto;
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

            Object[] datos = {(i+1)+"",vecto.get(i),"---","---"};
            m.addRow(datos);
        }  
    }
    
    public static String veriConstNum(String constante, String tipo){
        String regex;
        Pattern pat;
        int a;
        if(tipo.equals("DW")){
            if(constante.matches("[0-9]{0,6}")){
                a = Integer.parseInt(constante);
                if(a > -1 && a < 65536){
                    return "";
                }else{
                    return "Excede el número de bits "+tipo;
                }
            }else if(constante.matches("0?[A-F0-9]{0,4}H")){
                return "";
            }else if(constante.matches("0?[01]{0,16}B")){
                return "";
            }else if(constante.matches("[0-9]+")){
                return "Excede el número de bits "+tipo;
            }else if(constante.matches("0?[A-F0-9]+H")){
                return "Excede el número de bits "+tipo;
            }else if(constante.matches("0?[01]+B")){
                return "Excede el número de bits "+tipo;
            }else{
                return "Sintaxis no reconocida";
            }
        }else if(tipo.equals("DB")){
            if(constante.matches("[0-9]{0,3}")){
                a = Integer.parseInt(constante);
                if(a > -1 && a < 256){
                    return "";
                }
            }else if(constante.matches("0?[A-F0-9]{0,2}H")){
                return "";
            }else if(constante.matches("0?[01]{0,8}B")){
                return "";
            }else if(constante.matches("[0-9]+")){
                return "Excede el número de bits "+tipo;
            }else if(constante.matches("0?[A-F0-9]+H")){
                return "Excede el número de bits "+tipo;
            }else if(constante.matches("0?[01]+B")){
                return "Excede el número de bits "+tipo;
            }else{
                return "Sintaxis no reconocida";
            }
        }else{
            return "Sintaxis no reconocida";
        }
        return "Sintaxis no reconocida";
    }
    
    public void Pila(DefaultTableModel m, Vector<String> vecto){
        int band = 0, band2 = 0, band3 = 0, band4 = 0, band5 = 0;
        for(int i = 0; i < vecto.size(); i += 1){
            if(vecto.get(i).matches("\\s*\\.STACK\\s+SEGMENT\\s*")){
                m.setValueAt(mensaje, i, 2);
                m.setValueAt("La definición es correcta", i, 3);
                band = i;
                band3 = 1;
            }else if(vecto.get(i).matches("\\s*ENDS\\s*") && band3 > 0){
                m.setValueAt(mensaje, i, 2);
                m.setValueAt("La definición es correcta", i, 3);
                band2 = i;
                i = vecto.size();
            }
        }
        for(int i = band+1; i < band2; i++){
            String regex = "\\s*(\\w+)\\s*(\\w+)\\s*DUP\\s*\\((\\w+)\\)\\s*";
            Pattern pat = Pattern.compile(regex);
            //String a = new String("  DB 0FFH DUP (01001110101101011B)    ");
            Matcher mat = pat.matcher(vecto.get(i));
            if (mat.find()) {
                if(veriConstNum(mat.group(2),mat.group(1)).equals("") && veriConstNum(mat.group(3),mat.group(1)).equals("")){
                    m.setValueAt(mensaje, i, 2);
                    m.setValueAt("La definición es correcta", i, 3);
                }else if(veriConstNum(mat.group(2),mat.group(1)).equals("")){
                    m.setValueAt("Sintáxis Incorrecta", i, 2);
                    m.setValueAt(veriConstNum(mat.group(3),mat.group(1)), i, 3);
                }else if(veriConstNum(mat.group(3),mat.group(1)).equals("")){
                    m.setValueAt("Sintáxis Incorrecta", i, 2);
                    m.setValueAt(veriConstNum(mat.group(2),mat.group(1)), i, 3);
                }else{
                    m.setValueAt("Sintáxis Incorrecta", i, 2);
                    m.setValueAt("La definicion no es correcta DW [D|B|H] DUP ([B|B|H])", i, 3);
                }
            }
        }
    }
    
    public String veriConsText(String constante, String tipo, int b){
        if(b == 0){
            switch (tipo) {
                case "DW":
                    if(constante.matches(".{0,2}")){
                        return "";
                    }else if(constante.matches(" {0,2}")){
                        return "";
                    }else if(constante.matches(".+")){
                        return "Excede numero de bits "+tipo;
                    }else{
                        return "Excede numero de bits "+tipo;
                    }
                case "DB":
                    if(constante.matches(".?")){
                        return "";
                    }else if(constante.matches(" ?")){
                        return "";
                    }else if(constante.matches(".+")){
                        return "Excede numero de bits "+tipo;
                    }else{
                        return "Excede numero de bits "+tipo;
                    }
                default:
                    return "Sintaxis no reconocida";
            }
        }else{
            switch (tipo) {
                case "DW":
                    if(constante.matches(".+")){
                        return "";
                    }else if(constante.matches("\\s+\\$*")){
                        return "";
                    }break;
                case "DB":
                    if(constante.matches(".+")){
                        return "";
                    }else if(constante.matches("\\s+\\$*")){
                        return "";
                    }break;
                default:
                    return "Sintaxis no reconocida";
            }
        }
        return "Sintaxis no reconocida";
    }
    
    public String veriSimbol(String constante){
        if(constante.matches("[A-Z]{1}[A-Z0-9]{0,9}")){
            return "";
        }else if(constante.matches("[A-Z]{1}[A-Z0-9]+")){
            return "Excede los 10 caracteres";
        }else{
            return "Sintaxis no reconocida";
        }
    }
    
    public void Datos(DefaultTableModel m, Vector<String> vecto){
        int band = 0, band2 = 0, band3 = 0, band4 = 0, band5 = 0;
        for(int i = 0; i < vecto.size(); i += 1){
            if(vecto.get(i).matches("\\s*\\.DATA\\s+SEGMENT\\s*")){
                m.setValueAt("Sintáxis correcta", i, 2);
                m.setValueAt("La definición es correcta", i, 3);
                band = i;
                band3 = 1;
            }else if(vecto.get(i).matches("\\s*ENDS\\s*") && band3 > 0){
                m.setValueAt(mensaje, i, 2);
                m.setValueAt("La definición es correcta", i, 3);
                band2 = i;
                i = vecto.size();
            }
        }
        for(int i = band+1; i < band2; i++){
            String regex = "\\s*(\\w+)\\s*(\\w+)\\s*(\\w+)\\s*";
            String regex2 = "\\s*(\\w+)\\s*(\\w+)\\s*(\\w+)\\s*DUP\\s*\\((\\w*|\\$?)\\)\\s*";
            String regexb = "\\s*(\\w+)\\s*(\\w+)\\s*\"?\'?(\\w*|\\s*|\\$)\"?\'?\\s*";
            String regex2b = "\\s*(\\w+)\\s*(\\w+)\\s*(\\w+)\\s*DUP\\s*\\(\"?\'?(\\w*|\\$?|\\s*)\"?\'?\\)\\s*";
            Pattern pat = Pattern.compile(regex);
            Pattern pat2 = Pattern.compile(regex2);
            Pattern pat3 = Pattern.compile(regexb);
            Pattern pat4 = Pattern.compile(regex2b);
            Matcher mat = pat.matcher(vecto.get(i));
            if(vecto.get(i).matches("\\s*(\\w+)\\s*(\\w+)\\s*")){
                m.setValueAt("Sintáxis Incorrecta", i, 2);
                m.setValueAt("No hay constante", i, 3);
                i += 1;
            }
            if(vecto.get(i).matches(regex)){
                mat = pat.matcher(vecto.get(i));
                if (mat.find()) {
                    //System.out.println(mat.group(3));
                    if(veriSimbol(mat.group(1)).equals("")){
                        switch (mat.group(2)) {
                            case "DB":
                                if(veriConstNum(mat.group(3),mat.group(2)).equals("")){
                                    m.setValueAt(mensaje, i, 2);
                                    m.setValueAt("La sintaxis es correcta", i, 3);
                                }else{
                                    m.setValueAt("Sintáxis Incorrecta", i, 2);
                                    m.setValueAt(veriConstNum(mat.group(3),mat.group(2)), i, 3);
                                }   break;
                            case "DW":
                                if(veriConstNum(mat.group(3),mat.group(2)).equals("")){
                                    m.setValueAt(mensaje, i, 2);
                                    m.setValueAt("La sintaxis es correcta", i, 3);
                                }else{
                                    m.setValueAt("Sintáxis Incorrecta", i, 2);
                                    m.setValueAt(veriConstNum(mat.group(3),mat.group(2)), i, 3);
                                }   break;
                            case "EQU":
                                if(veriConstNum(mat.group(3),"DW").equals("")){
                                    m.setValueAt(mensaje, i, 2);
                                    m.setValueAt("La sintaxis es correcta", i, 3);
                                }else{
                                    m.setValueAt("Sintáxis Incorrecta", i, 2);
                                    m.setValueAt(veriConstNum(mat.group(3),"DW"), i, 3);
                                }   break;
                            default:
                                m.setValueAt("Sintáxis Incorrecta", i, 2);
                                m.setValueAt("Sintaxis no reconocida", i, 3);
                                break;
                        }
                    }else{
                        m.setValueAt("Sintáxis Incorrecta", i, 2);
                        m.setValueAt(veriSimbol(mat.group(1)), i, 3);
                        //i = vecto.size();
                    }
                }
            }else if(vecto.get(i).matches(regex2)){
                mat = pat2.matcher(vecto.get(i));
                if (mat.find()) {
                    //System.out.println(mat.group(4));
                    if(veriSimbol(mat.group(1)).equals("")){
                        switch (mat.group(2)) {
                            case "DB":
                                if(veriConstNum(mat.group(3),"DW").equals("")){
                                    if(veriConstNum(mat.group(4),mat.group(2)).equals("")){
                                        m.setValueAt(mensaje, i, 2);
                                        m.setValueAt("La sintaxis es correcta", i, 3);
                                    }else{
                                        m.setValueAt("Sintáxis Incorrecta", i, 2);
                                        m.setValueAt(veriConstNum(mat.group(4),mat.group(2)), i, 3);
                                    }   break;
                                }else{
                                    m.setValueAt("Sintáxis Incorrecta", i, 2);
                                    m.setValueAt(veriConstNum(mat.group(3),"DW"), i, 3);
                                }   break;
                            case "DW":
                                if(veriConstNum(mat.group(3),mat.group(2)).equals("")){
                                    if(veriConstNum(mat.group(4),mat.group(2)).equals("")){
                                        m.setValueAt(mensaje, i, 2);
                                        m.setValueAt("La sintaxis es correcta", i, 3);
                                    }else{
                                        m.setValueAt("Sintáxis Incorrecta", i, 2);
                                        m.setValueAt(veriConstNum(mat.group(4),mat.group(2)), i, 3);
                                    }   break;
                                }else{
                                    m.setValueAt("Sintáxis Incorrecta", i, 2);
                                    m.setValueAt(veriConstNum(mat.group(3),mat.group(2)), i, 3);
                                }   break;
                            default:
                                m.setValueAt("Sintáxis Incorrecta", i, 2);
                                m.setValueAt(mat.group(2), i, 3);
                                break;
                        }
                    }else{
                        m.setValueAt("Sintáxis Incorrecta", i, 2);
                        m.setValueAt(veriSimbol(mat.group(1)), i, 3);
                        //i = vecto.size();
                    }
                }
            }else if(vecto.get(i).matches(regexb)){
                mat = pat3.matcher(vecto.get(i));
                if (mat.find()) {
                    //System.out.println(mat.group(3));
                    if(veriSimbol(mat.group(1)).equals("")){
                        switch (mat.group(2)) {
                            case "DB":
                                if(veriConsText(mat.group(3),mat.group(2),1).equals("")){
                                    m.setValueAt(mensaje, i, 2);
                                    m.setValueAt("La sintaxis es correcta", i, 3);
                                }else{
                                    m.setValueAt("Sintáxis Incorrecta", i, 2);
                                    m.setValueAt(veriConsText(mat.group(3),mat.group(2),1), i, 3);
                                }   break;
                            case "DW":
                                if(veriConsText(mat.group(3),mat.group(2),1).equals("")){
                                    m.setValueAt(mensaje, i, 2);
                                    m.setValueAt("La sintaxis es correcta", i, 3);
                                }else{
                                    m.setValueAt("Sintáxis Incorrecta", i, 2);
                                    m.setValueAt(veriConsText(mat.group(3),mat.group(2),1), i, 3);
                                }   break;
                            default:
                                m.setValueAt("Sintáxis Incorrecta", i, 2);
                                m.setValueAt(mat.group(2), i, 3);
                                break;
                        }
                    }else{
                        m.setValueAt("Sintáxis Incorrecta", i, 2);
                        m.setValueAt(veriSimbol(mat.group(1)), i, 3);
                        //i = vecto.size();
                    }
                }
            }else if(vecto.get(i).matches(regex2b)){
                mat = pat4.matcher(vecto.get(i));
                if (mat.find()) {
                    //System.out.println(mat.group(4));
                    if(veriSimbol(mat.group(1)).equals("")){
                        switch (mat.group(2)) {
                            case "DB":
                                if(veriConstNum(mat.group(3),"DW").equals("")){
                                    if(veriConsText(mat.group(4),"DW",0).equals("")){
                                        m.setValueAt(mensaje, i, 2);
                                        m.setValueAt("La sintaxis es correcta", i, 3);
                                    }else{
                                        m.setValueAt("Sintáxis Incorrecta", i, 2);
                                        m.setValueAt(veriConsText(mat.group(4),"DW",0), i, 3);
                                    }   break;
                                }else{
                                    m.setValueAt("Sintáxis Incorrecta", i, 2);
                                    m.setValueAt(veriConstNum(mat.group(3),"DW"), i, 3);
                                }   break;
                            case "DW":
                                if(veriConstNum(mat.group(3),mat.group(2)).equals("")){
                                    if(veriConsText(mat.group(4),mat.group(2),0).equals("")){
                                        m.setValueAt(mensaje, i, 2);
                                        m.setValueAt("La sintaxis es correcta", i, 3);
                                    }else{
                                        m.setValueAt("Sintáxis Incorrecta", i, 2);
                                        m.setValueAt(veriConsText(mat.group(4),mat.group(2),0), i, 3);
                                    }   break;
                                }else{
                                    m.setValueAt("Sintáxis Incorrecta", i, 2);
                                    m.setValueAt(veriConstNum(mat.group(3),mat.group(2)), i, 3);
                                }   break;
                            default:
                                m.setValueAt("Sintáxis Incorrecta", i, 2);
                                m.setValueAt(mat.group(2), i, 3);
                                break;
                        }
                    }else{
                        m.setValueAt("Sintáxis Incorrecta", i, 2);
                        m.setValueAt(veriSimbol(mat.group(1)), i, 3);
                        //i = vecto.size();
                    }
                }
            }
        }
    }
    
    public String veriInstruccionSA(String instruccion){
        String regex = "\\s*(\\w+)\\s*.*\\s*";
        String regexEtiqu = "\\s*(\\w+):\\s*";
        Pattern pat = Pattern.compile(regex);
        Matcher mat;
        int a;
        for(int i = 0; i < vectorInstruccionesC.length; i += 1){
            mat = pat.matcher(instruccion);
            if(mat.find()){
                if(mat.group(1).equals(vectorInstruccionesC[i])){                    
                    if(instruccion.matches("\\s*(\\w+)\\s*")){
                        return "";
                    }else if(instruccion.matches("\\s*(\\w+)\\s+.+\\s*")){
                        return "La instrucción "+instruccion+" no requiere de argumentos";
                    }else{
                        return "---";
                    }
                }
            }
        }
        return "---";
    }
    
    public String veriInstruEtiq(String instruccion){
        String regex = "\\s*(\\w+):\\s*";
        Pattern pat = Pattern.compile(regex);
        Matcher mat;
        int a;
        for(int i = 0; i < vectorInstruccionesC.length; i += 1){
            mat = pat.matcher(instruccion);
            if(instruccion.matches(regex)){
                return "";
            }else if(instruccion.matches("\\s*(\\w+):.+\\s*")){
                return "Las etiquetas no requieren de ningun argumento";
            }else{
                return "---";
            }
        }
        return "---";
    }
    
    public String verInstruSalt(String instruccion, DefaultTableModel m2){
        String regex = "\\s*(\\w+)\\s+(\\w+)\\s*";
        Pattern pat = Pattern.compile(regex);
        Matcher mat;
        Vector<String> vecto2 = llenaEtiq(m2);
        /*for(int j1= 0; j1 < vecto2.size(); j1 += 1){
            System.out.println(vecto2.get(j1)+" "+j1);                     
        }*/
        String a = "---", band = "no";
        for(int i = 0; i < vectorSaltos.length; i += 1){
            mat = pat.matcher(instruccion);
            if(mat.find()){               
                if(mat.group(1).equals(vectorSaltos[i])){                   
                    for(int j1= 0; j1 < vecto2.size(); j1 += 1){
                        //System.out.println(mat.group(2)+" "+j1);
                        if(mat.group(2).equals(vecto2.get(j1))){
                            a = "";
                            band = "1";
                            i = vectorSaltos.length;
                            j1 = vecto2.size();
                        }else if(mat.group(2).length() > 10){
                            a = "Se excede al número de caracteres";
                            band = "1";
                            i = vectorSaltos.length;
                            j1 = vecto2.size();
                        }else if(mat.group(2).matches("\\[[\\w\\d]+\\]")){
                            a = "No se admite memoria";
                            band = "1";
                            i = vectorSaltos.length;
                            j1 = vecto2.size();
                        }else if(mat.group(2).matches("\\d+")){
                            //System.out.println("11515");
                            a = "No se admiten constantes numericas";
                            band = "1";
                            i = vectorSaltos.length;
                            j1 = vecto2.size();
                        }else if(mat.group(2).matches("[A-F0-9]+H")){
                            a = "No se admiten constantes numericas hexadecimales";
                            band = "1";
                            i = vectorSaltos.length;
                            j1 = vecto2.size();
                        }else if(mat.group(2).matches("[01]+B")){
                            a = "No se admiten constantes numericas binarias";
                            band = "1";
                            i = vectorSaltos.length;
                            j1 = vecto2.size();
                        }
                    }
                    if(a.equals("") == false){
                        if(mat.group(2).matches("\\w+") && band.equals("no")){
                            a = "Etiqueta indefinida";
                            i = vectorSaltos.length;
                        }
                    }
                }
            }else{
                if(instruccion.matches("\\s*"+vectorSaltos[i]+"\\s*")){
                    a = "Se necesita una etiqueta";
                    i = vectorSaltos.length;
                }else{
                    a = "---";
                }
            }
        }
        return a;
    }
    
    public String veriUP(String instruccion, DefaultTableModel m2){
        String regex = "\\s*(\\w+)\\s+(\\w+)\\s*";
        Pattern pat = Pattern.compile(regex);
        Matcher mat;
        String a = "---", band = "no";
        Vector <String> vecto2 = llenaVar(m2);
        Vector <String> vecto3 = llenaEtiq(m2);
        //System.out.println(instruccion);
        mat = pat.matcher(instruccion);
        if(mat.find() && instruccion.contains(",") == false){  
            //System.out.println(mat.group(1)+mat.group(2));
            if(mat.group(1).equals("IMUL")){                   
                for(int j1= 0; j1 < vectorReg.length; j1 += 1){
                    if(mat.group(2).equals(vectorReg[j1])){
                        a = "";
                        band = "1";
                        j1 = vectorReg.length;
                    }else if(veriConstNum(mat.group(2),"DW").equals("")){
                        a = "No se admiten constantes numericas";
                        j1 = vectorReg.length;
                    }
                }
                if(band.equals("1") == false){
                    for(int j1= 0; j1 < vecto2.size(); j1 += 1){
                        if(mat.group(2).equals(vecto2.get(j1))){
                            a = "";
                            band = "1";
                            j1 = vecto2.size();
                        }
                    }
                }
                if(band.equals("1") == false){
                    for(int j1= 0; j1 < vecto3.size(); j1 += 1){
                        if(mat.group(2).equals(vecto3.get(j1))){
                            a = "No se admiten etiquetas";
                            band = "1";
                            j1 = vecto3.size();
                        }
                    }
                }
                //Modulo verifica memoria compuesta
            }else if(mat.group(1).equals("INC")){
                for(int j1= 0; j1 < vectorReg.length; j1 += 1){
                    if(mat.group(2).equals(vectorReg[j1])){
                        a = "";
                        band = "1";
                    }else if(mat.group(2).length() > 10){
                        a = "Se excede al número de caracteres";
                        band = "1";
                    }else if(mat.group(2).matches("\\[[\\w\\d\\+\\:]+\\]")){
                        a = "";
                        band = "1";
                    }else if(mat.group(2).matches("\\d+")){
                        a = "No se admiten constantes numericas";
                        band = "1";
                    }else if(mat.group(2).matches("[A-F0-9]+H")){
                        a = "No se admiten constantes numericas hexadecimales";
                        band = "1";
                    }else if(mat.group(2).matches("[01]+B")){
                        a = "No se admiten constantes numericas binarias";
                        band = "1";
                    }
                }
                if(a.equals("") == false){
                    if(mat.group(2).matches("\\w+") && band.equals("no")){
                        a = "No se admiten variables o constantes";
                    }
                }
            }else if(mat.group(1).equals("INT")){
                String as = veriConstNum(mat.group(2), "DB");
                if(as.equals("")){
                    a = as;
                }else{
                    a = as;
                }
            }else if(mat.group(1).equals("POP")){
                for(int j1= 0; j1 < 4; j1 += 1){
                    if(mat.group(2).equals(vectorReg[j1])){
                        a = "";
                        band = "1";
                    }else if(mat.group(2).length() > 10){
                        a = "Se excede al número de caracteres";
                        band = "1";
                    }else if(mat.group(2).matches("\\[[\\w\\d\\+\\:]+\\]")){
                        a = "";
                        band = "1";
                    }else if(mat.group(2).matches("\\d+")){
                        a = "No se admiten constantes numericas";
                        band = "1";
                    }else if(mat.group(2).matches("[A-F0-9]+H")){
                        a = "No se admiten constantes numericas hexadecimales";
                        band = "1";
                    }else if(mat.group(2).matches("[01]+B")){
                        a = "No se admiten constantes numericas binarias";
                        band = "1";
                    }
                }
                for(int j1= 0; j1 < vectorSReg.length; j1 += 1){
                    if(mat.group(2).equals(vectorSReg[j1])){
                        a = "";
                        band = "1";
                    }else if(mat.group(2).length() > 10){
                        a = "Se excede al número de caracteres";
                        band = "1";
                    }else if(mat.group(2).matches("\\[[\\w\\d\\+\\:]+\\]")){
                        a = "";
                        band = "1";
                    }else if(mat.group(2).matches("\\d+")){
                        a = "No se admiten constantes numericas";
                        band = "1";
                    }else if(mat.group(2).matches("[A-F0-9]+H")){
                        a = "No se admiten constantes numericas hexadecimales";
                        band = "1";
                    }else if(mat.group(2).matches("[01]+B")){
                        a = "No se admiten constantes numericas binarias";
                        band = "1";
                    }
                }
                if(a.equals("") == false){
                    if(mat.group(2).matches("\\w+") && band.equals("no")){
                        a = "No se admiten variables o constantes";
                    }
                }
            }
        }else{
            //System.out.println(mat.group(2));
            //System.out.println(instruccion);
            if(instruccion.matches("\\s*IMUL\\s*")||instruccion.matches("\\s*INT\\s*")||instruccion.matches("\\s*INC\\s*")||instruccion.matches("\\s*POP\\s*")){
                a = "Se necesita un parametro";
            }else if((instruccion.matches("\\s*IMUL\\s+(\\w+)\\s*\\,\\s*(\\w+)\\s*")||instruccion.matches("\\s*INT\\s*(\\w+)\\s*\\,\\s*(\\w+)\\s*")||instruccion.matches("\\s*INC\\s*(\\w+)\\s*\\,\\s*(\\w+)\\s*")||instruccion.matches("\\s*POP\\s*(\\w+)\\s*\\,\\s*(\\w+)\\s*")) && band.equals("no")){
                a = "Solo se necesita un parametro";
            }else{
                a = "---";
            }
        }
        return a;
    }
    
    public String veriReg(String constante, String tipo){
        if(tipo.equals("DW")){
            for(int i = 0; i < 4; i += 1){
                if(constante.equals(vectorReg[i])){
                    return "";
                }
            }
            for(int i = vectorReg.length-1; i > vectorReg.length-5; i -= 1){
                if(constante.equals(vectorReg[i])){
                    return "";
                }
            }
            for(int i = 0; i < vectorSReg.length; i += 1){
                if(constante.equals(vectorSReg[i])){
                    return "";
                }
            }
        }else if(tipo.equals("DB")){
            for(int i = 4; i < vectorReg.length-4; i += 1){
                if(constante.equals(vectorReg[i])){
                    return "";
                }
            }
        }else{
            return "---";
        }
        return "---";
    }
    
    public String veriDP(String instruccion, DefaultTableModel m2){
        String a = "---", band = "no";
        String [] arr = instruccion.split("\\s");
        Vector <String> vecto2 = llenaVar(m2);
        if(instruccion.contains("WORD PTR")){   
            if(arr[0].equals("SAR")){
                    
            }else if(arr[0].equals("ADC")){

            }else if(arr[0].equals("ADD")){

            }else if(arr[0].equals("SUB")){

            }                    
        }else if(instruccion.contains("BYTE PTR")){
            if(arr[0].equals("SAR")){
                    
            }else if(arr[0].equals("ADC")){

            }else if(arr[0].equals("ADD")){

            }else if(arr[0].equals("SUB")){

            }           
        }else if(instruccion.contains("\\]")){
            
        }else{
            if(arr.length == 4){
                if(arr[0].equals("SAR")){
                    if(veriReg(arr[1], "DW").equals("")){
                        if(arr[3].equals("CL")){
                            return "";
                        }else if(veriConstNum(arr[3],"DB").equals("")){
                            return "";
                        }
                    }else{
                        for(int j = 0; j < vecto2.size(); j += 1){
                            if(arr[1].equals(vecto2.get(j))){
                                if(arr[3].equals("CL")){
                                    return "";
                                }else if(veriConstNum(arr[3],"DB").equals("")){
                                    return "";
                                }
                            } 
                        }
                    }
                }else if(arr[0].equals("ADC")){
                    if(veriReg(arr[1], "DW").equals("")){
                        if(veriReg(arr[3], "DW").equals("")){
                            return "";
                        }else if(veriConstNum(arr[3],"DW").equals("")){
                            return "";
                        }
                    }else if(veriReg(arr[1], "DB").equals("")){
                        if(veriReg(arr[3], "DB").equals("")){
                            return "";
                        }else if(veriConstNum(arr[3],"DB").equals("")){
                            return "";
                        }
                    }else{
                        for(int j = 0; j < vecto2.size(); j += 1){
                            if(arr[1].equals(vecto2.get(j))){
                                if(veriReg(arr[3], "DB").equals("")){
                                    return "";
                                }else if(veriConstNum(arr[3],"DB").equals("")){
                                    return "";
                                }
                            } 
                            if(arr[1].equals(vecto2.get(j))){
                                if(veriReg(arr[3], "DW").equals("")){
                                    return "";
                                }else if(veriConstNum(arr[3],"DW").equals("")){
                                    return "";
                                }
                            } 
                        }
                    }
                }else if(arr[0].equals("ADD")){
                    if(veriReg(arr[1], "DW").equals("")){
                        if(veriReg(arr[3], "DW").equals("")){
                            return "";
                        }else if(veriConstNum(arr[3],"DW").equals("")){
                            return "";
                        }
                    }else if(veriReg(arr[1], "DB").equals("")){
                        if(veriReg(arr[3], "DB").equals("")){
                            return "";
                        }else if(veriConstNum(arr[3],"DB").equals("")){
                            return "";
                        }
                    }
                }else if(arr[0].equals("SUB")){
                    if(veriReg(arr[1], "DW").equals("")){
                        if(veriReg(arr[3], "DW").equals("")){
                            return "";
                        }else if(veriConstNum(arr[3],"DW").equals("")){
                            return "";
                        }
                    }else if(veriReg(arr[1], "DB").equals("")){
                        if(veriReg(arr[3], "DB").equals("")){
                            return "";
                        }else if(veriConstNum(arr[3],"DB").equals("")){
                            return "";
                        }
                    }
                }  
            }else if(arr.length == 3){
                if(arr[1].contains(",")){
                    arr[1] = arr[1].replaceFirst(".$","");
                    if(arr[0].equals("SAR")){
                        if(veriReg(arr[1], "DW").equals("")){
                            if(arr[2].equals("CL")){
                                return "";
                            }else if(veriConstNum(arr[2],"DB").equals("")){
                                return "";
                            }
                        }else{
                            for(int j = 0; j < vecto2.size(); j += 1){
                                if(arr[0].equals(vecto2.get(j))){
                                    if(veriConstNum(arr[2],"DB").equals("")){
                                        band = "si";
                                        return veriConstNum(arr[2],"DB");
                                    }
                                }
                            }
                            if(band.equals("no")){
                                return "Variable indefinida";
                            }
                        }
                    }else if(arr[0].equals("ADC")){
                        if(veriReg(arr[1], "DW").equals("")){
                            if(veriReg(arr[2], "DW").equals("")){
                                return "";
                            }else if(veriConstNum(arr[2],"DW").equals("")){
                                return "";
                            }
                        }else if(veriReg(arr[1], "DB").equals("")){
                            if(veriReg(arr[2], "DB").equals("")){
                                return "";
                            }else if(veriConstNum(arr[2],"DB").equals("")){
                                return "";
                            }
                        }
                    }else if(arr[0].equals("ADD")){
                        if(veriReg(arr[1], "DW").equals("")){
                            if(veriReg(arr[2], "DW").equals("")){
                                return "";
                            }else if(veriConstNum(arr[2],"DW").equals("")){
                                return "";
                            }
                        }else if(veriReg(arr[1], "DB").equals("")){
                            if(veriReg(arr[2], "DB").equals("")){
                                return "";
                            }else if(veriConstNum(arr[2],"DB").equals("")){
                                return "";
                            }
                        }
                    }else if(arr[0].equals("SUB")){
                        if(veriReg(arr[1], "DW").equals("")){
                            if(veriReg(arr[2], "DW").equals("")){
                                return "";
                            }else if(veriConstNum(arr[2],"DW").equals("")){
                                return "";
                            }
                        }else if(veriReg(arr[1], "DB").equals("")){
                            if(veriReg(arr[2], "DB").equals("")){
                                return "";
                            }else if(veriConstNum(arr[2],"DB").equals("")){
                                return "";
                            }
                        }
                    }
                }else if(arr[2].contains(",")){
                     arr[2] = arr[2].substring(1);
                     if(arr[0].equals("SAR")){
                        if(veriReg(arr[1], "DW").equals("")){
                            if(arr[2].equals("CL")){
                                return "";
                            }else if(veriConstNum(arr[2],"DB").equals("")){
                                return "";
                            }
                        }else{
                            for(int j = 0; j < vecto2.size(); j += 1){
                                if(arr[0].equals(vecto2.get(j))){
                                    if(veriConstNum(arr[2],"DB").equals("")){
                                        band = "si";
                                        return veriConstNum(arr[2],"DB");
                                    }
                                }
                            }
                            if(band.equals("no")){
                                return "Variable indefinida";
                            }
                        }
                    }else if(arr[0].equals("ADC")){
                        if(veriReg(arr[1], "DW").equals("")){
                            if(veriReg(arr[2], "DW").equals("")){
                                return "";
                            }else if(veriConstNum(arr[2],"DW").equals("")){
                                return "";
                            }
                        }else if(veriReg(arr[1], "DB").equals("")){
                            if(veriReg(arr[2], "DB").equals("")){
                                return "";
                            }else if(veriConstNum(arr[2],"DB").equals("")){
                                return "";
                            }
                        }
                    }else if(arr[0].equals("ADD")){
                        if(veriReg(arr[1], "DW").equals("")){
                            if(veriReg(arr[2], "DW").equals("")){
                                return "";
                            }else if(veriConstNum(arr[2],"DW").equals("")){
                                return "";
                            }
                        }else if(veriReg(arr[1], "DB").equals("")){
                            if(veriReg(arr[2], "DB").equals("")){
                                return "";
                            }else if(veriConstNum(arr[2],"DB").equals("")){
                                return "";
                            }
                        }
                    }else if(arr[0].equals("SUB")){
                        if(veriReg(arr[1], "DW").equals("")){
                            if(veriReg(arr[2], "DW").equals("")){
                                return "";
                            }else if(veriConstNum(arr[2],"DW").equals("")){
                                return "";
                            }
                        }else if(veriReg(arr[1], "DB").equals("")){
                            if(veriReg(arr[2], "DB").equals("")){
                                return "";
                            }else if(veriConstNum(arr[2],"DB").equals("")){
                                return "";
                            }
                        }
                    }
                }               
            }else if(arr.length == 2){
                System.out.println(instruccion);
                String[] arr2 = arr[1].split(",");
                if(arr[0].equals("SAR")){
                        if(veriReg(arr2[0], "DW").equals("")){
                            if(arr2[1].equals("CL")){
                                return "";
                            }else if(veriConstNum(arr2[1],"DB").equals("")){
                                return "";
                            }
                        }else{
                            for(int j = 0; j < vecto2.size(); j += 1){
                                if(arr[0].equals(vecto2.get(j))){
                                    if(veriConstNum(arr2[1],"DB").equals("")){
                                        band = "si";
                                        return veriConstNum(arr2[1],"DB");
                                    }
                                }
                            }
                            if(band.equals("no")){
                                return "Variable indefinida";
                            }
                        }
                    }else if(arr[0].equals("ADC")){
                        if(veriReg(arr2[0], "DW").equals("")){
                            if(veriReg(arr2[1], "DW").equals("")){
                                return "";
                            }else if(veriConstNum(arr2[1],"DW").equals("")){
                                return "";
                            }
                        }else if(veriReg(arr2[0], "DB").equals("")){
                            if(veriReg(arr2[1], "DB").equals("")){
                                return "";
                            }else if(veriConstNum(arr2[1],"DB").equals("")){
                                return "";
                            }
                        }
                    }else if(arr[0].equals("ADD")){
                        if(veriReg(arr2[0], "DW").equals("")){
                            if(veriReg(arr2[1], "DW").equals("")){
                                return "";
                            }else if(veriConstNum(arr2[1],"DW").equals("")){
                                return "";
                            }
                        }else if(veriReg(arr2[0], "DB").equals("")){
                            if(veriReg(arr2[1], "DB").equals("")){
                                return "";
                            }else if(veriConstNum(arr2[1],"DB").equals("")){
                                return "";
                            }
                        }
                    }else if(arr[0].equals("SUB")){
                        if(veriReg(arr2[0], "DW").equals("")){
                            if(veriReg(arr2[1], "DW").equals("")){
                                return "";
                            }else if(veriConstNum(arr2[1],"DW").equals("")){
                                return "";
                            }
                        }else if(veriReg(arr2[0], "DB").equals("")){
                            if(veriReg(arr2[1], "DB").equals("")){
                                return "";
                            }else if(veriConstNum(arr2[1],"DB").equals("")){
                                return "";
                            }
                        }
                    }
            }
        }
        if(instruccion.matches("\\s*.+\\s*")){
            return "Se necesitan 2 argumentos";
        }
        return a;
    }
    
    public String veriDP2(String instruccion, DefaultTableModel m2){
        String a = "---", band = "no";
        String [] arr = instruccion.split("\\s");
        Vector <String> vecto2 = llenaVar(m2);
        if(instruccion.contains("WORD PTR")){   
            String regex = "\\s*(\\w+)\\s*WORD PTR\\s*\\[(\\w+)\\]\\s*\\,\\s*(\\w+)\\s*";
            String regex2 = "\\s*(\\w+)\\s*(\\w+),\\s*WORD PTR\\s*\\[(\\w+)\\]\\s*\\s*";
            Pattern pat = Pattern.compile(regex);
            Pattern pat2 = Pattern.compile(regex2);
            Matcher mat = pat.matcher(instruccion);
            Matcher mat2 = pat2.matcher(instruccion);
            if(mat.find()){
                if(mat.group(1).equals("SAR")){                    
                    if(veriReg(mat.group(2),"DW").equals("")){
                        //System.out.println(mat.group(3));
                        if(mat.group(3).equals("CL")){
                            return "";
                        }else if(veriConstNum(mat.group(3),"DB").equals("")){
                            return veriConstNum(mat.group(3),"DB");
                        }else if(veriReg(mat.group(2),"DW").equals("")){
                            return "No se admiten registros de 16 bits";
                        }
                    }else{
                        for(int j = 0; j < vecto2.size(); j += 1){
                            if(mat.group(3).equals(vecto2.get(j))){
                                band = "si";
                                return "";
                            }
                        }
                        if(band.equals("no")){
                            return "Variable indefinida";
                        }
                    }
                }else if(mat.group(1).equals("ADC")){
                    if(veriReg(mat.group(2),"DW").equals("")){
                        //System.out.println(mat.group(3));
                        if(mat.group(3).equals("CL")){
                            return "";
                        }else if(veriConstNum(mat.group(3),"DB").equals("")){
                            return veriConstNum(mat.group(3),"DB");
                        }else if(veriReg(mat.group(2),"DW").equals("")){
                            return "No se admiten registros de 16 bits";
                        }
                    }else{
                        for(int j = 0; j < vecto2.size(); j += 1){
                            if(mat.group(3).equals(vecto2.get(j))){
                                band = "si";
                                return "";
                            }
                        }
                        if(band.equals("no")){
                            return "Variable indefinida";
                        }
                    }
                }else if(mat.group(1).equals("ADD")){
                    if(veriReg(mat.group(2),"DW").equals("")){
                        //System.out.println(mat.group(3));
                        if(mat.group(3).equals("CL")){
                            return "";
                        }else if(veriConstNum(mat.group(3),"DB").equals("")){
                            return veriConstNum(mat.group(3),"DB");
                        }else if(veriReg(mat.group(2),"DW").equals("")){
                            return "No se admiten registros de 16 bits";
                        }
                    }else{
                        for(int j = 0; j < vecto2.size(); j += 1){
                            if(mat.group(3).equals(vecto2.get(j))){
                                band = "si";
                                return "";
                            }
                        }
                        if(band.equals("no")){
                            return "Variable indefinida";
                        }
                    }
                }else if(mat.group(1).equals("SUB")){
                    if(veriReg(mat.group(2),"DW").equals("")){
                        //System.out.println(mat.group(3));
                        if(mat.group(3).equals("CL")){
                            return "";
                        }else if(veriConstNum(mat.group(3),"DB").equals("")){
                            return veriConstNum(mat.group(3),"DB");
                        }else if(veriReg(mat.group(2),"DW").equals("")){
                            return "No se admiten registros de 16 bits";
                        }
                    }else{
                        for(int j = 0; j < vecto2.size(); j += 1){
                            if(mat.group(3).equals(vecto2.get(j))){
                                band = "si";
                                return "";
                            }
                        }
                        if(band.equals("no")){
                            return "Variable indefinida";
                        }
                    }
                } 
            }else if(mat2.find()){
                if(mat2.group(1).equals("SAR")){                    
                    if(veriReg(mat2.group(2),"DW").equals("")){
                        //System.out.println(mat.group(3));
                        if(mat2.group(3).equals("CL")){
                            return "";
                        }else if(veriConstNum(mat2.group(3),"DB").equals("")){
                            return veriConstNum(mat2.group(3),"DB");
                        }else if(veriReg(mat2.group(2),"DW").equals("")){
                            return "No se admiten registros de 16 bits";
                        }
                    }else{
                        for(int j = 0; j < vecto2.size(); j += 1){
                            if(mat2.group(3).equals(vecto2.get(j))){
                                band = "si";
                                return "";
                            }
                        }
                        if(band.equals("no")){
                            return "Variable indefinida";
                        }
                    }
                }else if(mat2.group(1).equals("ADC")){
                    if(veriReg(mat2.group(2),"DW").equals("")){
                        //System.out.println(mat.group(3));
                        if(mat2.group(3).equals("CL")){
                            return "";
                        }else if(veriConstNum(mat2.group(3),"DB").equals("")){
                            return veriConstNum(mat2.group(3),"DB");
                        }else if(veriReg(mat2.group(2),"DW").equals("")){
                            return "No se admiten registros de 16 bits";
                        }
                    }else{
                        for(int j = 0; j < vecto2.size(); j += 1){
                            if(mat2.group(3).equals(vecto2.get(j))){
                                band = "si";
                                return "";
                            }
                        }
                        if(band.equals("no")){
                            return "Variable indefinida";
                        }
                    }
                }else if(mat2.group(1).equals("ADD")){
                    if(veriReg(mat2.group(2),"DW").equals("")){
                        //System.out.println(mat.group(3));
                        if(mat2.group(3).equals("CL")){
                            return "";
                        }else if(veriConstNum(mat2.group(3),"DB").equals("")){
                            return veriConstNum(mat2.group(3),"DB");
                        }else if(veriReg(mat2.group(2),"DW").equals("")){
                            return "No se admiten registros de 16 bits";
                        }
                    }else{
                        for(int j = 0; j < vecto2.size(); j += 1){
                            if(mat2.group(3).equals(vecto2.get(j))){
                                band = "si";
                                return "";
                            }
                        }
                        if(band.equals("no")){
                            return "Variable indefinida";
                        }
                    }
                }else if(mat2.group(1).equals("SUB")){
                    if(veriReg(mat2.group(2),"DW").equals("")){
                        //System.out.println(mat.group(3));
                        if(mat2.group(3).equals("CL")){
                            return "";
                        }else if(veriConstNum(mat2.group(3),"DB").equals("")){
                            return veriConstNum(mat2.group(3),"DB");
                        }else if(veriReg(mat2.group(2),"DW").equals("")){
                            return "No se admiten registros de 16 bits";
                        }
                    }else{
                        for(int j = 0; j < vecto2.size(); j += 1){
                            if(mat2.group(3).equals(vecto2.get(j))){
                                band = "si";
                                return "";
                            }
                        }
                        if(band.equals("no")){
                            return "Variable indefinida";
                        }
                    }
                }
            }                   
        }else if(instruccion.contains("BYTE PTR")){
            String regex = "\\s*(\\w+)\\s*BYTE PTR\\s*\\[(\\w+)\\]\\s*\\,\\s*(\\w+)\\s*";
            String regex2 = "\\s*(\\w+)\\s*(\\w+),\\s*BYTE PTR\\s*\\[(\\w+)\\]\\s*\\s*";
            Pattern pat = Pattern.compile(regex);
            Pattern pat2 = Pattern.compile(regex2);
            Matcher mat = pat.matcher(instruccion);
            Matcher mat2 = pat2.matcher(instruccion);
            if(mat.find()){
                if(mat.group(1).equals("SAR")){                    
                    if(veriReg(mat.group(2),"DB").equals("")){
                        //System.out.println(mat.group(3));
                        if(mat.group(3).equals("CL")){
                            return "";
                        }else if(veriConstNum(mat.group(3),"DB").equals("")){
                            return veriConstNum(mat.group(3),"DB");
                        }else if(veriReg(mat.group(2),"DW").equals("")){
                            return "No se admiten registros de 16 bits";
                        }
                    }else{
                        for(int j = 0; j < vecto2.size(); j += 1){
                            if(mat.group(3).equals(vecto2.get(j))){
                                band = "si";
                                return "";
                            }
                        }
                        if(band.equals("no")){
                            return "Variable indefinida";
                        }
                    }
                }else if(mat.group(1).equals("ADC")){
                    if(veriReg(mat.group(2),"DB").equals("")){
                        //System.out.println(mat.group(3));
                        if(mat.group(3).equals("CL")){
                            return "";
                        }else if(veriConstNum(mat.group(3),"DB").equals("")){
                            return veriConstNum(mat.group(3),"DB");
                        }else if(veriReg(mat.group(2),"DW").equals("")){
                            return "No se admiten registros de 16 bits";
                        }
                    }else{
                        for(int j = 0; j < vecto2.size(); j += 1){
                            if(mat.group(3).equals(vecto2.get(j))){
                                band = "si";
                                return "";
                            }
                        }
                        if(band.equals("no")){
                            return "Variable indefinida";
                        }
                    }
                }else if(mat.group(1).equals("ADD")){
                    if(veriReg(mat.group(2),"DB").equals("")){
                        //System.out.println(mat.group(3));
                        if(mat.group(3).equals("CL")){
                            return "";
                        }else if(veriConstNum(mat.group(3),"DB").equals("")){
                            return veriConstNum(mat.group(3),"DB");
                        }else if(veriReg(mat.group(2),"DW").equals("")){
                            return "No se admiten registros de 16 bits";
                        }
                    }else{
                        for(int j = 0; j < vecto2.size(); j += 1){
                            if(mat.group(3).equals(vecto2.get(j))){
                                band = "si";
                                return "";
                            }
                        }
                        if(band.equals("no")){
                            return "Variable indefinida";
                        }
                    }
                }else if(mat.group(1).equals("SUB")){
                    if(veriReg(mat.group(2),"DB").equals("")){
                        //System.out.println(mat.group(3));
                        if(mat.group(3).equals("CL")){
                            return "";
                        }else if(veriConstNum(mat.group(3),"DB").equals("")){
                            return veriConstNum(mat.group(3),"DB");
                        }else if(veriReg(mat.group(2),"DW").equals("")){
                            return "No se admiten registros de 16 bits";
                        }
                    }else{
                        for(int j = 0; j < vecto2.size(); j += 1){
                            if(mat.group(3).equals(vecto2.get(j))){
                                band = "si";
                                return "";
                            }
                        }
                        if(band.equals("no")){
                            return "Variable indefinida";
                        }
                    }
                } 
            }else if(mat2.find()){
                if(mat2.group(1).equals("SAR")){                    
                    if(veriReg(mat2.group(2),"DB").equals("")){
                        //System.out.println(mat.group(3));
                        if(mat2.group(3).equals("CL")){
                            return "";
                        }else if(veriConstNum(mat2.group(3),"DB").equals("")){
                            return veriConstNum(mat2.group(3),"DB");
                        }else if(veriReg(mat2.group(2),"DW").equals("")){
                            return "No se admiten registros de 16 bits";
                        }
                    }else{
                        for(int j = 0; j < vecto2.size(); j += 1){
                            if(mat2.group(3).equals(vecto2.get(j))){
                                band = "si";
                                return "";
                            }
                        }
                        if(band.equals("no")){
                            return "Variable indefinida";
                        }
                    }
                }else if(mat2.group(1).equals("ADC")){
                    if(veriReg(mat2.group(2),"DB").equals("")){
                        //System.out.println(mat.group(3));
                        if(mat2.group(3).equals("CL")){
                            return "";
                        }else if(veriConstNum(mat2.group(3),"DB").equals("")){
                            return veriConstNum(mat2.group(3),"DB");
                        }else if(veriReg(mat2.group(2),"DW").equals("")){
                            return "No se admiten registros de 16 bits";
                        }
                    }else{
                        for(int j = 0; j < vecto2.size(); j += 1){
                            if(mat2.group(3).equals(vecto2.get(j))){
                                band = "si";
                                return "";
                            }
                        }
                        if(band.equals("no")){
                            return "Variable indefinida";
                        }
                    }
                }else if(mat2.group(1).equals("ADD")){
                    if(veriReg(mat2.group(2),"DB").equals("")){
                        //System.out.println(mat.group(3));
                        if(mat2.group(3).equals("CL")){
                            return "";
                        }else if(veriConstNum(mat2.group(3),"DB").equals("")){
                            return veriConstNum(mat2.group(3),"DB");
                        }else if(veriReg(mat2.group(2),"DW").equals("")){
                            return "No se admiten registros de 16 bits";
                        }
                    }else{
                        for(int j = 0; j < vecto2.size(); j += 1){
                            if(mat2.group(3).equals(vecto2.get(j))){
                                band = "si";
                                return "";
                            }
                        }
                        if(band.equals("no")){
                            return "Variable indefinida";
                        }
                    }
                }else if(mat2.group(1).equals("SUB")){
                    if(veriReg(mat2.group(2),"DB").equals("")){
                        //System.out.println(mat.group(3));
                        if(mat2.group(3).equals("CL")){
                            return "";
                        }else if(veriConstNum(mat2.group(3),"DB").equals("")){
                            return veriConstNum(mat2.group(3),"DB");
                        }else if(veriReg(mat2.group(2),"DW").equals("")){
                            return "No se admiten registros de 16 bits";
                        }
                    }else{
                        for(int j = 0; j < vecto2.size(); j += 1){
                            if(mat2.group(3).equals(vecto2.get(j))){
                                band = "si";
                                return "";
                            }
                        }
                        if(band.equals("no")){
                            return "Variable indefinida";
                        }
                    }
                }  
            }                            
        }else if(instruccion.contains("\\]")){
            String regex = "\\s*(\\w+)\\s*\\[\\s*(\\w+)\\+?\\s*(\\w*)\\+?(\\w*)\\]\\s*\\,\\s*(\\w+)\\s*";
            Pattern pat = Pattern.compile(regex);
            pat = Pattern.compile(regex);
            Matcher mat = pat.matcher(instruccion);
            if(mat.find()){
                if(mat.group(1).equals("SAR")){
                    if(mat.group(3).equals("") && mat.group(4).equals("")){
                        if(veriReg(mat.group(2),"DW").equals("")){
                            if(veriReg(mat.group(5),"DB").equals("")){
                                return "";
                            }else if(veriConstNum(mat.group(5),"DB").equals("")){
                                return "";
                            }else{
                                for(int j = 0; j < vecto2.size(); j += 1){
                                    if(mat.group(5).equals(vecto2.get(j))){
                                        band = "si";
                                        return "";
                                    }
                                }
                                if(band.equals("no")){
                                    return "Variable indefinida";
                                }

                            }
                        }
                    }else{
                        if(veriReg(mat.group(2),"DW").equals("")){
                            if(veriReg(mat.group(3),"DW").equals("")){
                                if(veriConstNum(mat.group(4),"DW").equals("")){
                                    if(veriReg(mat.group(3),"DB").equals("")){
                                        return "";
                                    }else{
                                        for(int j = 0; j < vecto2.size(); j += 1){
                                            if(mat.group(3).equals(vecto2.get(j))){
                                                band = "si";
                                                return "";
                                            }
                                        }
                                        if(band.equals("no")){
                                            return "Variable indefinida";
                                        }
                                    }
                                    return veriConstNum(mat.group(4),"DW");
                                }
                            }else{
                                return veriReg(mat.group(4),"DW");
                            }
                        }
                    }
                }else if(mat.group(1).equals("ADD")){
                    if(mat.group(3).equals("") && mat.group(4).equals("")){
                        if(veriReg(mat.group(2),"DW").equals("")){
                            if(veriReg(mat.group(5),"DB").equals("")){
                                return "";
                            }else if(veriConstNum(mat.group(5),"DB").equals("")){
                                return "";
                            }else{
                                for(int j = 0; j < vecto2.size(); j += 1){
                                    if(mat.group(5).equals(vecto2.get(j))){
                                        band = "si";
                                        return "";
                                    }
                                }
                                if(band.equals("no")){
                                    return "Variable indefinida";
                                }

                            }
                        }
                    }else{
                        if(veriReg(mat.group(2),"DW").equals("")){
                            if(veriReg(mat.group(3),"DW").equals("")){
                                if(veriConstNum(mat.group(4),"DW").equals("")){
                                    if(veriReg(mat.group(3),"DB").equals("")){
                                        return "";
                                    }else{
                                        for(int j = 0; j < vecto2.size(); j += 1){
                                            if(mat.group(3).equals(vecto2.get(j))){
                                                band = "si";
                                                return "";
                                            }
                                        }
                                        if(band.equals("no")){
                                            return "Variable indefinida";
                                        }
                                    }
                                    return veriConstNum(mat.group(4),"DW");
                                }
                            }else{
                                return veriReg(mat.group(4),"DW");
                            }
                        }
                    }
                }else if(mat.group(1).equals("ADC")){
                    if(mat.group(3).equals("") && mat.group(4).equals("")){
                        if(veriReg(mat.group(2),"DW").equals("")){
                            if(veriReg(mat.group(5),"DB").equals("")){
                                return "";
                            }else if(veriConstNum(mat.group(5),"DB").equals("")){
                                return "";
                            }else{
                                for(int j = 0; j < vecto2.size(); j += 1){
                                    if(mat.group(5).equals(vecto2.get(j))){
                                        band = "si";
                                        return "";
                                    }
                                }
                                if(band.equals("no")){
                                    return "Variable indefinida";
                                }

                            }
                        }
                    }else{
                        if(veriReg(mat.group(2),"DW").equals("")){
                            if(veriReg(mat.group(3),"DW").equals("")){
                                if(veriConstNum(mat.group(4),"DW").equals("")){
                                    if(veriReg(mat.group(3),"DB").equals("")){
                                        return "";
                                    }else{
                                        for(int j = 0; j < vecto2.size(); j += 1){
                                            if(mat.group(3).equals(vecto2.get(j))){
                                                band = "si";
                                                return "";
                                            }
                                        }
                                        if(band.equals("no")){
                                            return "Variable indefinida";
                                        }
                                    }
                                    return veriConstNum(mat.group(4),"DW");
                                }
                            }else{
                                return veriReg(mat.group(4),"DW");
                            }
                        }
                    }
                }else if(mat.group(1).equals("SUB")){
                    if(mat.group(3).equals("") && mat.group(4).equals("")){
                        if(veriReg(mat.group(2),"DW").equals("")){
                            if(veriReg(mat.group(5),"DB").equals("")){
                                return "";
                            }else if(veriConstNum(mat.group(5),"DB").equals("")){
                                return "";
                            }else{
                                for(int j = 0; j < vecto2.size(); j += 1){
                                    if(mat.group(5).equals(vecto2.get(j))){
                                        band = "si";
                                        return "";
                                    }
                                }
                                if(band.equals("no")){
                                    return "Variable indefinida";
                                }

                            }
                        }
                    }else{
                        if(veriReg(mat.group(2),"DW").equals("")){
                            if(veriReg(mat.group(3),"DW").equals("")){
                                if(veriConstNum(mat.group(4),"DW").equals("")){
                                    if(veriReg(mat.group(3),"DB").equals("")){
                                        return "";
                                    }else{
                                        for(int j = 0; j < vecto2.size(); j += 1){
                                            if(mat.group(3).equals(vecto2.get(j))){
                                                band = "si";
                                                return "";
                                            }
                                        }
                                        if(band.equals("no")){
                                            return "Variable indefinida";
                                        }
                                    }
                                    return veriConstNum(mat.group(4),"DW");
                                }
                            }else{
                                return veriReg(mat.group(4),"DW");
                            }
                        }
                    }
                }
            }else{
                return "Sintaxis invalida";
            }
            
        }else if(instruccion.matches("s*(\\w+)\\s+(\\w+)\\s*\\,\\s*(\\w+)\\s*")){
            String regex = "\\s*(\\w+)\\s+(\\w+)\\s*\\,\\s*(\\w+)\\s*";
            Pattern pat = Pattern.compile(regex);
            pat = Pattern.compile(regex);
            Matcher mat = pat.matcher(instruccion);
            if(mat.find()){
                if(mat.group(1).equals("SAR")){
                    if(veriReg(mat.group(2),"DW").equals("")){
                        if(veriReg(mat.group(3),"DB").equals("")){
                            return "";
                        }else if(veriConstNum(mat.group(3),"DB").equals("")){
                            return "";
                        }else{
                            for(int j = 0; j < vecto2.size(); j += 1){
                                if(mat.group(3).equals(vecto2.get(j))){
                                    band = "si";
                                    return "";
                                }
                            }
                            if(band.equals("no")){
                                return "Variable indefinida";
                            }
                            
                        }
                    }else if(veriReg(mat.group(2),"DB").equals("")){
                        if(veriReg(mat.group(3),"DB").equals("")){
                            return "";
                        }else if(veriConstNum(mat.group(3),"DB").equals("")){
                            return "";
                        }else{
                            for(int j = 0; j < vecto2.size(); j += 1){
                                if(mat.group(3).equals(vecto2.get(j))){
                                    band = "si";
                                    return "";
                                }
                            }
                            if(band.equals("no")){
                                return "Variable indefinida";
                            }
                        }
                    }else if(veriSimbol(mat.group(1)).equals("")){
                        for(int j = 0; j < vecto2.size(); j += 1){
                            if(mat.group(2).equals(vecto2.get(j))){     
                                band = "si";
                                if(veriReg(mat.group(3),"DB").equals("")){
                                    band = "si";
                                    return "";
                                }else if(veriConstNum(mat.group(3),"DB").equals("")){
                                    band = "si";
                                    return "";
                                }
                            }
                        }
                        if(band.equals("no")){
                            return "Variable indefinida";
                        }
                    }
                }else if(mat.group(1).equals("ADC")){
                    if(veriReg(mat.group(2),"DW").equals("")){
                        if(veriReg(mat.group(3),"DW").equals("")){
                            return "";
                        }else if(veriConstNum(mat.group(3),"DW").equals("")){
                            return "";
                        }else{
                            for(int j = 0; j < vecto2.size(); j += 1){
                                if(mat.group(3).equals(vecto2.get(j))){
                                    band = "si";
                                    return "";
                                }
                            }
                            if(band.equals("no")){
                                return "Variable indefinida";
                            }
                            
                        }
                    }else if(veriReg(mat.group(2),"DB").equals("")){
                        if(veriReg(mat.group(3),"DB").equals("")){
                            return "";
                        }else if(veriConstNum(mat.group(3),"DB").equals("")){
                            return "";
                        }else{
                            for(int j = 0; j < vecto2.size(); j += 1){
                                if(mat.group(3).equals(vecto2.get(j))){
                                    band = "si";
                                    return "";
                                }
                            }
                            if(band.equals("no")){
                                return "Variable indefinida";
                            }
                        }
                    }else if(veriSimbol(mat.group(1)).equals("")){
                        for(int j = 0; j < vecto2.size(); j += 1){
                            if(mat.group(2).equals(vecto2.get(j))){     
                                band = "si";
                                if(veriReg(mat.group(3),"DB").equals("")){
                                    band = "si";
                                    return "";
                                }else if(veriConstNum(mat.group(3),"DB").equals("")){
                                    band = "si";
                                    return "";
                                }
                            }
                        }
                        if(band.equals("no")){
                            return "Variable indefinida";
                        }
                    }
                }else if(mat.group(1).equals("ADD")){
                    if(veriReg(mat.group(2),"DW").equals("")){
                        if(veriReg(mat.group(3),"DW").equals("")){
                            return "";
                        }else if(veriConstNum(mat.group(3),"DW").equals("")){
                            return "";
                        }else{
                            for(int j = 0; j < vecto2.size(); j += 1){
                                if(mat.group(3).equals(vecto2.get(j))){
                                    band = "si";
                                    return "";
                                }
                            }
                            if(band.equals("no")){
                                return "Variable indefinida";
                            }
                        }
                    }else if(veriReg(mat.group(2),"DB").equals("")){
                        if(veriReg(mat.group(3),"DB").equals("")){
                            return "";
                        }else if(veriConstNum(mat.group(3),"DB").equals("")){
                            return "";
                        }else{
                            for(int j = 0; j < vecto2.size(); j += 1){
                                if(mat.group(3).equals(vecto2.get(j))){
                                    band = "si";
                                    return "";
                                }
                            }
                            if(band.equals("no")){
                                return "Variable indefinida";
                            }
                        }
                    }else if(veriSimbol(mat.group(1)).equals("")){
                        for(int j = 0; j < vecto2.size(); j += 1){
                            if(mat.group(2).equals(vecto2.get(j))){     
                                band = "si";
                                if(veriReg(mat.group(3),"DW").equals("")){
                                    band = "si";
                                    return "";
                                }else if(veriReg(mat.group(3),"DB").equals("")){
                                    band = "si";
                                    return "";
                                }else if(veriConstNum(mat.group(3),"DB").equals("")){
                                    band = "si";
                                    return "";
                                }
                            }
                        }
                        if(band.equals("no")){
                            return "Variable indefinida";
                        }
                    }
                }else if(mat.group(1).equals("SUB")){
                    if(veriReg(mat.group(2),"DW").equals("")){
                        if(veriReg(mat.group(3),"DW").equals("")){
                            return "";
                        }else if(veriConstNum(mat.group(3),"DW").equals("")){
                            return "";
                        }else{
                            for(int j = 0; j < vecto2.size(); j += 1){
                                if(mat.group(3).equals(vecto2.get(j))){
                                    band = "si";
                                    return "";
                                }
                            }
                            if(band.equals("no")){
                                return "Variable indefinida";
                            }
                        }
                    }else if(veriReg(mat.group(2),"DB").equals("")){
                        if(veriReg(mat.group(3),"DB").equals("")){
                            return "";
                        }else if(veriConstNum(mat.group(3),"DB").equals("")){
                            return "";
                        }else{
                            for(int j = 0; j < vecto2.size(); j += 1){
                                if(mat.group(3).equals(vecto2.get(j))){
                                    band = "si";
                                    return "";
                                }
                            }
                            if(band.equals("no")){
                                return "Variable indefinida";
                            }
                        }
                    }else if(veriSimbol(mat.group(1)).equals("")){
                        for(int j = 0; j < vecto2.size(); j += 1){
                            if(mat.group(2).equals(vecto2.get(j))){     
                                band = "si";
                                if(veriReg(mat.group(3),"DB").equals("")){
                                    band = "si";
                                    return "";
                                }else if(veriConstNum(mat.group(3),"DB").equals("")){
                                    band = "si";
                                    return "";
                                }
                            }
                        }
                        if(band.equals("no")){
                            return "Variable indefinida";
                        }
                    }
                }
            }
        }else if(instruccion.matches("\\s*ADC\\s*") || instruccion.matches("\\s*ADD\\s*") || instruccion.matches("\\s*SAR\\s*") || instruccion.matches("\\s*SUB\\s*")){
            return "Se necesitan 2 argumentos";
        }else if(instruccion.matches("\\s*ADC\\s*.+\\s*") ||instruccion.matches("\\s*ADD\\s*.+\\s*") || instruccion.matches("\\s*SAR\\s*.+\\s*") || instruccion.matches("\\s*SUB\\s*.+\\s*")){
            return "Se necesita 1 argumento mas";
        }
        return "---";
    }
    
    public void Codigo(DefaultTableModel m, DefaultTableModel m2 ,Vector<String> vecto){
        int band = 0, band2 = 0, band3 = 0, band4 = 0, band5 = 0;
        String a = "";
        Vector<String> vecto2 = new Vector<String>();
        for(int i = 0; i < vecto.size(); i += 1){
            //System.out.println(vecto.get(i)+" es "+vecto.get(i).toUpperCase().equals(".CODE SEGMENT"));
            if(vecto.get(i).matches("\\s*\\.CODE\\s*SEGMENT\\s*")){
                m.setValueAt(mensaje, i, 2);
                m.setValueAt("La definición es correcta", i, 3);
                band = i;
                band3 = 1;
            }else if(vecto.get(i).matches("\\s*ENDS\\s*") && band3 > 0){
                m.setValueAt(mensaje, i, 2);
                m.setValueAt("La definición es correcta", i, 3);
                band2 = i;
                i = vecto.size();
            }
        }
        vecto2 = llenaEtiq(m2);
        
        for(int i = band+1; i < band2;i += 1){
            a  = veriInstruccionSA(vecto.get(i));
            if(m.getValueAt(i, 3).equals("---")){
                if(a.equals("")){
                    m.setValueAt(mensaje, i, 2);
                    m.setValueAt("La definición es correcta", i, 3);
                }else{
                    m.setValueAt("Sintáxis Incorrecta", i, 2);
                    m.setValueAt(a, i, 3);
                }
            }
        }
        for(int i = band+1; i < band2;i += 1){
            a  = veriInstruEtiq(vecto.get(i));
            if(m.getValueAt(i, 3).equals("---")){
                if(a.equals("")){
                    m.setValueAt(mensaje, i, 2);
                    m.setValueAt("La definición es correcta", i, 3);
                }else{
                    m.setValueAt("Sintáxis Incorrecta", i, 2);
                    m.setValueAt(a, i, 3);
                }
            }
        }
        for(int i = band+1; i < band2;i += 1){
            a  = verInstruSalt(vecto.get(i), m2);
            if(m.getValueAt(i, 3).equals("---")){
                if(a.equals("")){
                    m.setValueAt(mensaje, i, 2);
                    m.setValueAt("La definición es correcta", i, 3);
                }else{
                    m.setValueAt("Sintáxis Incorrecta", i, 2);
                    m.setValueAt(a, i, 3);
                }
            }
        }
        for(int i = band+1; i < band2;i += 1){
            a  = veriUP(vecto.get(i), m2);
            if(m.getValueAt(i, 3).equals("---")){
                if(a.equals("")){
                    m.setValueAt(mensaje, i, 2);
                    m.setValueAt("La definición es correcta", i, 3);
                }else{
                    m.setValueAt("Sintáxis Incorrecta", i, 2);
                    m.setValueAt(a, i, 3);
                }
            }
        }
        for(int i = band+1; i < band2;i += 1){
            a  = veriDP2(vecto.get(i), m2);
            if(m.getValueAt(i, 3).equals("---")){
                if(a.equals("")){
                    m.setValueAt(mensaje, i, 2);
                    m.setValueAt("La definición es correcta", i, 3);
                }else{
                    m.setValueAt("Sintáxis Incorrecta", i, 2);
                    m.setValueAt(a, i, 3);
                }
            }
        }
        for(int i = band+1; i < band2;i += 1){
            if(m.getValueAt(i, 3).equals("---")){
                m.setValueAt("Sintáxis Incorrecta", i, 2);
                m.setValueAt("Instrucción desconocida", i, 3);
            }
        }
    }
    
    public int corrijeEntero(int dire, int suma){
        dire += suma;
        return dire;
    }
    
    public int convierteEntero(String num){
        int numEn = 0;
        if(num.endsWith("H")){
            for(int i = 0; i < num.length() - 1 ; i += 1){
                //System.out.println(num.charAt(i));
                switch(num.charAt(i)){
                    case '0':
                        numEn += 0;
                        break;
                    case '1':
                        numEn += (Math.pow(16, (num.length()-2)-i));
                        break;
                    case '2':
                        numEn += 2*(Math.pow(16, (num.length()-2)-i));
                        break;
                    case '3':
                        numEn += 3*(Math.pow(16, (num.length()-2)-i));
                        break;
                    case '4':
                        numEn += 4*(Math.pow(16, (num.length()-2)-i));
                        break;                   
                    case '5':
                        numEn += 5*(Math.pow(16, (num.length()-2)-i));
                        break;
                    case '6':
                        numEn += 6*(Math.pow(16, (num.length()-2)-i));
                        break;
                    case '7':
                        numEn += 7*(Math.pow(16, (num.length()-2)-i));
                        break;
                    case '8':
                        numEn += 8*(Math.pow(16, (num.length()-2)-i));
                        break;
                    case '9':
                        numEn += 9*(Math.pow(16, (num.length()-2)-i));
                        break;
                    case 'A':
                        numEn += 10*(Math.pow(16, (num.length()-2)-i));
                        break;
                    case 'B':
                        numEn += 11*(Math.pow(16, (num.length()-2)-i));
                        break;
                    case 'C':
                        numEn += 12*(Math.pow(16, (num.length()-2)-i));
                        break;
                    case 'D':
                        numEn += 13*(Math.pow(16, (num.length()-2)-i));
                        break;
                    case 'E':
                        numEn += 14*(Math.pow(16, (num.length()-2)-i));
                        break;
                    case 'F':
                        numEn += 15*(Math.pow(16, (num.length()-2)-i));
                        break;
                    default:
                        break;
                }
            }
        }else if(num.endsWith("B")){
            for(int i = 0; i < num.length()-1; i += 1){
                //System.out.println(num.charAt(i)+" "+numEn+" "+(num.length()-2-i));
                switch(num.charAt(i)){                    
                    case '0':
                        numEn += 0;
                        break;
                    case '1':
                        numEn += Math.pow(2,(num.length()-2)-i);
                        break;
                    default:
                        numEn += 0;
                        break;
                }
            }
        }else{
            numEn = Integer.parseInt(num);
        }       
        return numEn;
    }
    
    public int GeneraDirecciones(String inst){
        int direc = 0;           
        if(inst.matches("\\s*(\\w+)\\s+(\\w+)\\s+\"?\'?(\\w+)\"?\'?\\s*")){           
            String regex = "\\s*(\\w+)\\s*(\\w+)\\s*\"?\'?(\\w+)\"?\'?\\s*";
            String regex2 = "\\s*(\\w+)\\s*(\\w+)\\s*\"?\'?(\\w+)\"?\'?\\s*";
            Pattern pat = Pattern.compile(regex);
            Matcher mat = pat.matcher(inst);
            if(mat.find()){
                //System.out.println(mat.group(2)+"    "+inst);
                if(inst.contains("\"") || inst.contains("\'")){
                    direc = 1*mat.group(3).length();
                }else if(mat.group(1).equals("DW")){
                    direc = 2*convierteEntero(mat.group(2));
                }else if(mat.group(2).equals("DB")){
                    direc = 1;
                }else if(mat.group(2).equals("DW")){
                    direc = 2;
                }               
            }else{
                //System.out.println(inst);
                pat = Pattern.compile(regex2);
                mat = pat.matcher(inst);
                if(mat.find()){
                    if(mat.group(2).equals("DB")){
                        direc = 1*mat.group(3).length();
                    }else if(mat.group(2).equals("DW")){
                        direc = 2*mat.group(3).length();
                    }
                }
            }
        }else if(inst.matches("\\s*(\\w+)\\s+(\\w+)\\s+(\\w+)\\s+DUP\\s*\\(\"?\'?(\\w*|\\$?|\\s*)\"?\'?\\)\\s*")){                      
            String regex = "\\s*(\\w+)\\s*(\\w+)\\s*(\\w+)\\s*DUP\\s*\\(\"?\'?(\\w*|\\$?|\\s*)\"?\'?\\)\\s*";
            Pattern pat = Pattern.compile(regex);
            Matcher mat = pat.matcher(inst);
            //System.out.println(inst);
            if(mat.find()){
                if(mat.group(1).equals("DW") == false){
                    if(mat.group(2).equals("DB")){
                        direc = 1*convierteEntero(mat.group(3));
                    }else if(mat.group(2).equals("DW")){
                        direc = 2*convierteEntero(mat.group(3));
                    }
                }
            }else{
                String regex2 = "\\s*(\\w+)\\s*(\\w+)\\s*(\\w+)\\s*DUP\\s*\\(\"?\'?(\\w+)\"?\'?\\)\\s*";
                pat = Pattern.compile(regex2);
                mat = pat.matcher(inst);
                if(mat.find()){
                    if(mat.group(2).equals("DB")){
                        direc = 1*convierteEntero(mat.group(3));
                    }else if(mat.group(2).equals("DW")){
                        direc = 2*convierteEntero(mat.group(3));
                    }
                }else if(inst.matches("\\s*(\\w+)\\s*(\\w+)\\s*DUP\\s*\\((\\w+)\\)")){
                    //System.out.println(inst);
                    regex = "\\s*(\\w+)\\s*(\\w+)\\s*DUP\\s*\\((\\w+)\\)";
                    pat = Pattern.compile(regex);
                    mat = pat.matcher(inst);
                    if(mat.find()){
                        if(mat.group(1).equals("DW")){
                            direc = 2*convierteEntero(mat.group(2));
                        }
                    }
                }
            }
        }else if(inst.matches("\\s*(\\w+)\\s*(\\w+)\\s*DUP\\s*\\((\\w+)\\)")){
            //System.out.println(inst);
            String regex = "\\s*(\\w+)\\s*(\\w+)\\s*DUP\\s*\\((\\w+)H\\)";
            Pattern pat = Pattern.compile(regex);
            Matcher mat = pat.matcher(inst);
            if(mat.find()){
                if(mat.group(1).equals("DW")){
                    direc = 2*convierteEntero(mat.group(2)+"H");
                }
            }else{
                regex = "\\s*(\\w+)\\s*(\\w+)\\s*DUP\\s*\\((\\w+)B\\)";
                pat = Pattern.compile(regex);
                mat = pat.matcher(inst);
                if(mat.find()){
                    if(mat.group(1).equals("DW")){
                        direc = 2*convierteEntero(mat.group(2)+"B");
                    }
                }else{
                    regex = "\\s*(\\w+)\\s*(\\w+)\\s*DUP\\s*\\((\\w+)\\)";
                    pat = Pattern.compile(regex);
                    mat = pat.matcher(inst);
                    if(mat.find()){
                        if(mat.group(1).equals("DW")){
                            direc = 2*convierteEntero(mat.group(2));
                        }
                    } 
                } 
            }
        }else if(inst.matches("\\s*(\\w+)\\s*")){
            String regex = "\\s*(\\w+)\\s*";
            Pattern pat = Pattern.compile(regex);
            Matcher mat = pat.matcher(inst);
            if(mat.find()){
                if(mat.group(1).equals("ENDS") == false){
                    if(mat.group(1).contains(":") == false){
                        direc = 1;
                    }else{
                        direc = 0;
                    }
                }else if(mat.group(1).equals("ADC")){
                    direc = 2;
                }else{
                    for(int j = 0; j < vectorInstrucciones.length; j += 1){
                        if(mat.group(1).equals(vectorInstrucciones[j])){
                            direc = 1;
                            break;
                        }
                    }
                    if(mat.group(1).contains("LOOP")){
                        direc = 2;
                    }
                }
            }
        }else if(inst.matches("\\s*(\\w+)\\s+(\\w+)\\s*")){
            //Aqui empiezan;ksdnclksncljanscljnsaljncljsanclnalcnajlnscjnaj
            String regex = "\\s*(\\w+)\\s+(\\w+)\\s*";
            Pattern pat = Pattern.compile(regex);
            Matcher mat = pat.matcher(inst);
            if(mat.find()){
                if(mat.group(1).equals("ADC")){
                    if(veriReg(mat.group(2),"DW").equals("")){
                        
                    }
                }
            }
            direc = 4;
            
        }else if(inst.matches("\\s*(\\w+)\\s+(\\w+)\\s+(\\w+)\\s*")){
            direc = 3;
        }
        return direc;
    }
    
    public String veriMemory(String instru, Vector <String> vecto){
        String band = "";
        String regex = "\\s*(\\w+)s*";
        String regex2 = "\\s*\\[(\\w+)\\+(\\w+)\\+(\\w+)\\]s*";
        String regex3 = "\\s*\\[(\\w+)\\]s*";
        String regex4 = "\\s*\\[(\\w+)\\+(\\w+)\\]s*";
        if(instru.matches(regex)){
            Pattern pat = Pattern.compile(regex);
            Matcher mat = pat.matcher(instru);
        }else if(instru.matches(regex2)){
            Pattern pat = Pattern.compile(regex2);
            Matcher mat = pat.matcher(instru);
        }else if(instru.matches(regex3)){
            Pattern pat = Pattern.compile(regex3);
            Matcher mat = pat.matcher(instru);
        }else if(instru.matches(regex4)){
            Pattern pat = Pattern.compile(regex4);
            Matcher mat = pat.matcher(instru);
        }
        return band;
    }
    
    public String codificaVar(String inst){
        String direc = "";       
        if(inst.matches("\\s*(\\w+)\\s*")){
            String regex = "\\s*(\\w+)\\s*";
            String regex2 = "\\s*(\\w+)\\s+(\\w+)\\s+\"?\'?(\\w+)\"?\'?\\s*";
            Pattern pat = Pattern.compile(regex);
            Matcher mat = pat.matcher(inst);
            if(mat.find()){
                for(int i = 0; i < vectorInstruccionesC.length; i += 1 ){
                    if(mat.group(1).equals(vectorInstruccionesC[i])){
                        direc = vectorInstruccionesCC[i];
                        i = vectorInstruccionesC.length;
                    }
                }
            }
        }else if(inst.matches("\\s*(\\w+)\\s+(\\w+)\\s*")){
            
        }else if(inst.matches("\\s*(\\w+)\\s+(\\w+)\\s*")){
            
        }
        return direc;
    }
}
