/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ventanas;

import Procesos.AnalizadorDeSimbolos;
import Procesos.AnalizadorSintactico;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author yosoy
 */
public class VentanaSintaxis extends JFrame implements ActionListener{
    public String[] columnTabla1 = {"Número","Símbolo", "Tipo", "Tamaño","Valor"};
    public String[] columnTabla2 = {"CP","Intrucción", "Sintáxis", "Detalles"};
    public DefaultTableModel dtm = new DefaultTableModel(null, columnTabla1);
    public JTable table = new JTable(dtm);

    public DefaultTableModel dtm2 = new DefaultTableModel(null, columnTabla2);
    public JTable table2 = new JTable(dtm2);
    public JPanel panel;
    public JPanel panel2;
    public Vector<String> vect = new Vector<String>();
    public String mensaje = "Sintáxis correcta";
    public JButton btnAnalizar;
    public JButton btnCodigo;
    
    public VentanaSintaxis(){    
        AnalizadorDeSimbolos a = new AnalizadorDeSimbolos();
        try {
            VentanaPrincipal venDat = new VentanaPrincipal();
        } catch (IOException ex) {
            Logger.getLogger(VentanaSintaxis.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.setSize( new Dimension(1100, 600) );
        this.setBackground(new Color(25, 56, 255));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);

        int xArea = 400;
        int yArea = 200;

        int xArea2 = 400;
        int yArea2 = 600;

        //Colocamos un layout para los componentes swing
        setLayout(new FlowLayout());

        panel = new JPanel(new BorderLayout());
        panel.setBounds(10,80,xArea,yArea);
        panel2 = new JPanel(new BorderLayout());
        panel2.setBounds(420,600,xArea,yArea);        
        
        table.setMinimumSize(new Dimension(1000, 800));
        table.setBounds(10, 80, 800, 600);
        table.setPreferredScrollableViewportSize(new Dimension(1000, 150));
        table2.setPreferredScrollableViewportSize(new Dimension(1000, 300));
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER); 
        
        btnAnalizar = new JButton("Analizar");
        btnCodigo = new JButton("Codificar");
        
        JScrollPane scrollPane2 = new JScrollPane(table2);
        panel2.add(scrollPane2, BorderLayout.CENTER); 
        Object[] newRow = {};
        
        TableColumnModel columnModel1 = table.getColumnModel();
        TableColumnModel columnModel = table2.getColumnModel();
        
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(200);
        columnModel.getColumn(3).setPreferredWidth(250);
        
        //columnModel.getColumn(3).setMinWidth(600);
        columnModel1.getColumn(0).setPreferredWidth(150);
        columnModel1.getColumn(1).setPreferredWidth(150);
        columnModel1.getColumn(2).setPreferredWidth(200);
        columnModel1.getColumn(3).setPreferredWidth(200);
        columnModel1.getColumn(4).setPreferredWidth(200);
        
        btnAnalizar.setBorderPainted(true);
        btnAnalizar.setFocusPainted(false);
        btnAnalizar.setContentAreaFilled(false);
        btnAnalizar.setForeground(Color.BLUE);
        btnAnalizar.setBounds(420,10,50,50);
        
        btnCodigo.setBorderPainted(true);
        btnCodigo.setFocusPainted(false);
        btnCodigo.setContentAreaFilled(false);
        btnCodigo.setForeground(Color.BLACK);
        btnCodigo.setBounds(520,10,50,50);
        
        table.setEnabled(false);
        table2.setEnabled(false);
        
        this.add(btnAnalizar);
        this.add(btnCodigo);
        this.add(panel);
        this.add(panel2);
        
        btnAnalizar.addActionListener(this);
        btnCodigo.addActionListener(this);
    }
    
    public VentanaSintaxis(int p){
        
    }
    
    public static void AgregaColumna(String nombre, JTable tb){
        String[] datos =new String[tb.getModel().getRowCount()];
        
        //tb.addColumn(aColumn);
    }
    
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnAnalizar){
            int direc = 0;
            int indice = 0;
            
            AnalizadorDeSimbolos an = new AnalizadorDeSimbolos();
            TableModel m = table2.getModel();
            TableModel m2 = table.getModel();
            
            String arr [] = new String [table.getModel().getRowCount()-1];
            //AgregaColumna("Dirección", table);
            table.addColumn(new TableColumn());
            
            JTableHeader tableHeader = table.getTableHeader();
            TableColumnModel tableColumnModel = tableHeader.getColumnModel();
            TableColumn tableColumn = tableColumnModel.getColumn(5);
            tableColumn.setHeaderValue( "Dirección" );
            tableHeader.repaint();
            
            for(int i = 0; i < m.getRowCount(); i += 1){
                //System.out.println(m.getValueAt(i, 1));
                if((Integer.toHexString(direc)+"H").length() == 5){
                    m.setValueAt(Integer.toHexString(direc).toUpperCase()+"H", i, 0);
                }else if((Integer.toHexString(direc)+"H").length() == 4){
                    m.setValueAt("0"+Integer.toHexString(direc).toUpperCase()+"H", i, 0);
                }else if((Integer.toHexString(direc)+"H").length() == 3){
                    m.setValueAt("00"+Integer.toHexString(direc).toUpperCase()+"H", i, 0);
                }else if((Integer.toHexString(direc).toUpperCase()+"H").length() == 2){
                    m.setValueAt("000"+Integer.toHexString(direc).toUpperCase()+"H", i, 0);
                }
                if(m.getValueAt(i, 2).equals(mensaje)){                   
                    if(m.getValueAt(i, 1).toString().matches("\\s*\\.\\w+\\s+SEGMENT\\s*")){
                        direc = 0;                      
                    }else{
                        int def = an.GeneraDirecciones(m.getValueAt(i, 1).toString());
                        if(def == 0){
                            direc = direc;
                        }else{
                            direc = an.corrijeEntero(direc,def);
                        }
                    }
                }
            }
            for(int i = 0; i < m.getRowCount(); i += 1){
                if(m.getValueAt(i, 1).toString().equals("\\s+") || m.getValueAt(i, 1).equals("")){
                    m.setValueAt(" ", i, 0);
                }
            }
            btnAnalizar.setVisible(false);
        }else if(e.getSource() == btnCodigo){
            table2.addColumn(new TableColumn());
            
            JTableHeader tableHeader = table2.getTableHeader();
            TableColumnModel tableColumnModel = tableHeader.getColumnModel();
            TableColumn tableColumn = tableColumnModel.getColumn(4);
            tableColumn.setHeaderValue( "Código" );
            tableHeader.repaint();
            btnCodigo.setVisible(false);
            
            int direc = 0;
            int indice = 0;
            
            AnalizadorDeSimbolos an = new AnalizadorDeSimbolos();
            TableModel m = table2.getModel();
            TableModel m2 = table.getModel();
            
            String arr [] = new String [table.getModel().getRowCount()-1];
            int ini = 0;
            int fin = 0;
            for(int i = 0; i < m.getRowCount(); i += 1){                
                if(m.getValueAt(i, 2).equals(mensaje)){                   
                    if(m.getValueAt(i, 1).toString().matches("\\s*\\.CODE\\s+SEGMENT\\s*")){
                        ini = i;                   
                    }
                    if(ini != 0 && m.getValueAt(i, 1).toString().matches("\\s*ENDS\\s*")){
                        fin = i;
                    }
                }
            }
            
            for(int i = ini; i < fin + 1; i += 1){
                if(m.getValueAt(i, 1).toString().equals("\\s+") || m.getValueAt(i, 1).equals("")){
                    m.setValueAt(an.codificaVar(m.getValueAt(i, 1).toString()), i, 4);
                }
            }
            
            for(int i = 0; i < m.getRowCount(); i += 1){
                if(m.getValueAt(i, 1).toString().equals("\\s+") || m.getValueAt(i, 1).equals("")){
                    System.out.println(m.getValueAt(i, 4));;
                    m.setValueAt(" ", i, 3);
                }
            }
        }
    }
}
