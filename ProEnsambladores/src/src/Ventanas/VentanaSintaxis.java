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
public class VentanaSintaxis extends JFrame{
    public String[] columnTabla1 = {"Número","Símbolo", "Tipo", "Tamaño","Valor"};
    public String[] columnTabla2 = {"Número","Intrucción", "Sintáxis", "Detalles"};
    public DefaultTableModel dtm = new DefaultTableModel(null, columnTabla1);
    public JTable table = new JTable(dtm);

    public DefaultTableModel dtm2 = new DefaultTableModel(null, columnTabla2);
    public JTable table2 = new JTable(dtm2);
    public JPanel panel;
    public JPanel panel2;
    public Vector<String> vect = new Vector<String>();
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
        columnModel1.getColumn(2).setPreferredWidth(300);
        columnModel1.getColumn(3).setPreferredWidth(350);
        columnModel1.getColumn(4).setPreferredWidth(800);
        
        
        table.setEnabled(false);
        table2.setEnabled(false);

        this.add(panel);
        this.add(panel2);
    }
}
