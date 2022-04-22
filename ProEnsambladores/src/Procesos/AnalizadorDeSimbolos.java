package Procesos;

import javax.swing.*;
import java.util.Vector;

public class AnalizadorDeSimbolos {
    public String[] vectorRegistros = {"AX","AH","AL","BX","BH","BL","CX","CH","CL","DX","DH","DL","SI","DI"};
    public String[] vectorInstruccionesC = {"SCASW","HLT","XLATB","INTO","AAD","MOVSW"};
    public String[] vectorCompuestasP = {".CODE SEGMENT",".DATA SEGMENT",".STACK SEGMENT","ENDS","DB","DW","CODE SEGMENT","DATA SEGMENT","STACK SEGMENT"};
    public String[] vectorInstrucciones = {"IMUL","INC","INT","POP","SAR","ADC","ADD","SUB","JBE","JNGE","JNA","JS","JL","JNZ"};
    public String[] vectorCompuestasS = {"BYTE PTR","WORD PTR"};
    public String[] vectorResultados = {"Pseudoinstrucción","Instrucción","Registro","Símbolo","Constante numérica decimal","Constante numérica hexadecimal","Constante numérica binaria","Constante tipo caracter","Elemento no identificado"};
    public Vector<String> todoIdentificado = new Vector<String>();
    public AnalizadorDeSimbolos(){

    }

    public String adaptar(String cadena){
        String cadenaTratada = new String("");
        char[] aCaracteres = cadena.toCharArray();
        char[] aCaracteres2 = "dup(0)			".toCharArray();

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

    public void identificador(Vector<String> vectorConTodosLosElementos, JTextArea cuadro){
        int band, band2, band3, band4, band5, band6, band7, band8, band9, band10;
        /*for (int j = 0; j<vectorConTodosLosElementos.size(); j += 1){
            cuadro.insert(vectorConTodosLosElementos.get(j)+"\n", cuadro.getText().length());
        }*/

        for (int j = 0; j<vectorConTodosLosElementos.size(); j += 1){
            band = 0;
            band2 = 0;
            band3 = 0;
            band4 = 0;
            band5 = 0;
            band6 = 0;
            band7 = 0;
            band8 = 0;
            band9 = 0;
            band10 = 0;
            //Identificador de Pseudoinstrucciones compuestas
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
            //Identificador de Instrucciones compuestas
            for(int k = 0; k<vectorInstruccionesC.length; k += 1){
                if(vectorConTodosLosElementos.get(j).equals(vectorInstruccionesC[k])){
                    todoIdentificado.add(vectorConTodosLosElementos.get(j));
                    todoIdentificado.add(vectorResultados[0]);
                    k = vectorInstruccionesC.length+1;
                    band2 = 0;
                }else{
                    band2 += 1;
                }
            }
            //Identificador de Instrucciones
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
            //Identificador de Registros
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
            //Identificador de constantes numericas Hexadecimales
            //Identificador de constantes numericas Binarias
            //Identificador de constantes numericas decimales
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
            //Identificador de constantes tipo caracter
            //Identificador de Simbolos
            if(band > 0 && band2 > 0 && band3 > 0 && band4 > 0 && band5 > 0){
                try{int auxNumTemp = Integer.parseInt(vectorConTodosLosElementos.get(j).charAt(0)+"");band6 += 1;
                }catch(Exception exs){
                    String temp = "";
                    int k = 0;
                    if(vectorConTodosLosElementos.get(j).charAt(0) > 64 && vectorConTodosLosElementos.get(j).charAt(0) < 91){
                        if(vectorConTodosLosElementos.get(j).length() <11){
                            /*while(vectorConTodosLosElementos.get(j).charAt(k) != ',' && k<vectorConTodosLosElementos.get(j).length()){
                                System.out.println(vectorConTodosLosElementos.get(j).length());
                                System.out.println(vectorConTodosLosElementos.get(j).charAt(k));

                                k += 1;
                            } */   

                            for(k = 0; k < vectorConTodosLosElementos.get(j).length(); k += 1){
                                temp += vectorConTodosLosElementos.get(j).charAt(k);
                            }
                            System.out.println("LLego");
                            todoIdentificado.add(temp);
                            todoIdentificado.add(vectorResultados[3]);
                            band6 = 0;
                        }else{
                            band6 += 1;
                        }
                    }else{
                        band6 += 1;
                    }
                }
            }
            
            //Identificador de
            //No identificado
            
            Boolean condi;
            condi = vectorConTodosLosElementos.get(j).equals("") == false && band > 0 && band2 > 0 && band3 > 0 && band4 > 0 && band5 > 0 && band6 > 0;
            if(condi){
                todoIdentificado.add(vectorConTodosLosElementos.get(j));
                todoIdentificado.add(vectorResultados[8]);
            }
        }
        //cuadro.insert("\n----------------------------------------------------------------------\n", cuadro.getText().length());
        for (int j = 0; j<todoIdentificado.size()-1; j += 2){
            cuadro.insert(todoIdentificado.get(j)+"---------> "+todoIdentificado.get(j+1)+"\n", cuadro.getText().length());
        }

    }
}
