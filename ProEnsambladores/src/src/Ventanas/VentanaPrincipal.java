package Ventanas;

import javax.imageio.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.Scanner;
import java.util.Vector;

import Procesos.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VentanaPrincipal extends JFrame implements ActionListener {
    private JLabel lblFondo;
    private JButton btnArchivo;
    private JButton btnAnalizar;
    private JButton btnSintaxis;
    private JTextField txtRuta;
    private JTextArea txaCodigo;
    private JTextArea txaAnalisis;
    private JScrollPane scroll;
    private JScrollPane scroll2;
    private JPanel panel;
    private JPanel panel2;

    public JTextField getTxtRuta() {
        return txtRuta;
    }

    public void setTxtRuta(JTextField txtRuta) {
        this.txtRuta = txtRuta;
    }

    public JTextArea getTxaCodigo() {
        return txaCodigo;
    }

    public void setTxaCodigo(JTextArea txaCodigo) {
        this.txaCodigo = txaCodigo;
    }

    public JTextArea getTxaAnalisis() {
        return txaAnalisis;
    }

    public void setTxaAnalisis(JTextArea txaAnalisis) {
        this.txaAnalisis = txaAnalisis;
    }
    
    

    public  VentanaPrincipal() throws IOException {
        int xArea = 400;
        int yArea = 600;

        int xArea2 = 400;
        int yArea2 = 600;

        //Colocamos un layout para los componentes swing
        setLayout(null);

        panel = new JPanel(new BorderLayout());
        panel.setBounds(10,80,xArea,yArea);
        panel2 = new JPanel(new BorderLayout());
        panel2.setBounds(420,80,xArea,yArea);

        //Propiedades de la ventana
        this.setSize( new Dimension(1230, 800) );
        this.setBackground(new Color(25, 56, 255));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        //Importamos la imagen de fondo de los botones
        String ruta = new String("src/Imagenes/archivo1.png");
        BufferedImage buttonIcon = ImageIO.read(new File(ruta));

        //Inicializamos e instanciamos los botones
        btnArchivo = new JButton(new ImageIcon(buttonIcon));
        btnAnalizar = new JButton("Analizar");
        btnSintaxis = new JButton("Sintáxis");

        //Propiedades de los botones
        btnArchivo.setBorderPainted(false);
        btnArchivo.setFocusPainted(false);
        btnArchivo.setContentAreaFilled(false);
        btnArchivo.setBounds(10,10,50,50);
        
        btnAnalizar.setBorderPainted(true);
        btnAnalizar.setFocusPainted(true);
        btnAnalizar.setContentAreaFilled(false);
        btnAnalizar.setForeground(Color.BLUE);
        btnAnalizar.setBounds(570,10,80,30);
        
        btnSintaxis.setBorderPainted(true);
        btnSintaxis.setFocusPainted(true);
        btnSintaxis.setContentAreaFilled(false);
        btnSintaxis.setForeground(Color.GREEN);
        btnSintaxis.setBounds(660,10,80,30);

        //Inicializamos los cuadros de texto
        txtRuta = new JTextField("c://");
        txaCodigo = new JTextArea("Todo el código se escribirá aquí...");
        txaAnalisis = new JTextArea("Analisis...");
        //txaCodigo.setVisible(false);
        //Propiedades de los cuadros de texto
        Font fuenteCodigo = new Font("SansSerif",Font.BOLD + Font.ITALIC,12);

        txtRuta.setBounds(65,10,500,30);
        txtRuta.setEditable(false);

        txaCodigo.setBounds(10,80,xArea,yArea);
        txaCodigo.setEditable(false);
        txaCodigo.setFont(fuenteCodigo);

        txaAnalisis.setBounds(420,80,xArea2,yArea2);
        txaAnalisis.setEditable(false);
        txaAnalisis.setFont(fuenteCodigo);

        scroll = new JScrollPane(txaCodigo);
        scroll2 = new JScrollPane(txaAnalisis);

        //Agregamos los componentes en la ventana
        this.add(btnArchivo);
        this.add(btnAnalizar);
        this.add(btnSintaxis);
        this.add(txtRuta);
        panel.add( scroll, BorderLayout.CENTER );
        panel2.add(scroll2, BorderLayout.CENTER);
        this.add(panel);
        this.add(panel2);

        //Relacionamos el boton con su evento
        btnArchivo.addActionListener(this);
        btnAnalizar.addActionListener(this);
        btnSintaxis.addActionListener(this);
    }

    public void crearTabla(){
        DefaultTableModel modelo = new DefaultTableModel();
        JTable tabla = new JTable (modelo);

    }

    public void escalar(){
        ImageIcon imgIcon = new ImageIcon(getClass().getResource("src/Imagenes/fondo1.jpg"));
        Image imgEscalada = imgIcon.getImage().getScaledInstance(lblFondo.getWidth(),
                lblFondo.getHeight(), Image.SCALE_SMOOTH);
        Icon iconoEscalado = new ImageIcon(imgEscalada);
        lblFondo.setIcon(iconoEscalado);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==btnArchivo) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

            FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos ensamblador", "asm");
            fileChooser.setFileFilter(filtro);

            int result = fileChooser.showOpenDialog(this);

            if (result != JFileChooser.CANCEL_OPTION) {

                File fileName = fileChooser.getSelectedFile();

                if ((fileName == null) || (fileName.getName().equals(""))) {
                    txtRuta.setText("...");
                } else {
                    txtRuta.setText(fileName.getAbsolutePath());
                }
            }

            File archivo = fileChooser.getSelectedFile(); // obtiene el archivo seleccionado

            // muestra error si es inválido
            if ((archivo == null) || (archivo.getName().equals(""))) {
                JOptionPane.showMessageDialog(this, "Nombre de archivo inválido", "Nombre de archivo inválido", JOptionPane.ERROR_MESSAGE);
            } // fin de if

            Scanner scn = null;
            try {
                scn = new Scanner(archivo);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            txaCodigo.setText("");
            while (scn.hasNext()) {
                txaCodigo.insert(scn.nextLine() + "\n", txaCodigo.getText().length());
            }
        }else if(e.getSource() == btnAnalizar){
            try{
                String direc = new String(txtRuta.getText());
                Scanner scn = null;

                File archi = new File(direc);
                try {
                    scn = new Scanner(archi);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
                txaAnalisis.setText("");
                String a = new String();
                String b = new String();
                
                Vector<String> todo = new Vector<String>();
                Vector<String> aux = new Vector<String>();
                Vector<String> aux2 = new Vector<String>();
                
                AnalizadorDeSimbolos analiza = new AnalizadorDeSimbolos();
                //En esta parte del código se obtiene el código y se transforma a caracteres manejables
                while (scn.hasNextLine()) {
                    a = scn.nextLine();
                    if(a.equals("\n") == false){
                        //System.out.println(a);
                        b = analiza.adaptar(a);
                        String[] parts = b.split(" ");
                        for(int j = 0; j<parts.length; j += 1){
                            //System.out.println(parts[j]+",");
                            if(!" ".equals(parts[j]) && !"\n".equals(parts[j]) && !"".equals(parts[j]) && !"\t".equals(parts[j])){
                                //System.out.println(parts[j].toUpperCase());
                                parts[j] = parts[j].replace(" ","");
                                parts[j] = parts[j].replace("\t","");
                                parts[j] = parts[j].replace(" ","");
                                if(parts[j].toUpperCase().equals(".CODE") || parts[j].toUpperCase().equals(".DATA") || parts[j].toUpperCase().equals(".STACK") || parts[j].toUpperCase().equals("BYTE") || parts[j].toUpperCase().equals("WORD")){
                                    todo.add(parts[j].toUpperCase()+" "+parts[j+1].toUpperCase());
                                    j += 1;
                                }else{
                                    todo.add(parts[j].toUpperCase());
                                }
                            }
                        }
                    }
                }
                //analiza.imprimirSystem(todo);
                analiza.AjustaComas(todo, aux);
                todo = new Vector<String>();
                analiza.AjustaCorchetes(aux, aux2);
                aux = new Vector<String>();               
                analiza.AjustaDUP(aux2, todo);
                aux2 = new Vector<String>();
                analiza.AjustaCorchetes2(todo, aux);
                todo = new Vector<String>(); 
                analiza.identificador(aux, txaAnalisis);
            }catch (Exception er){
                JOptionPane.showMessageDialog(this, "Ruta inválida", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }else if(e.getSource() == btnSintaxis){
            try{
                String direc = new String(txtRuta.getText());
                Scanner scn = null;
                Vector<String> todo = new Vector<String>();
                Vector<String> todo2 = new Vector<String>();
                File archi = new File(direc);
                try {
                    scn = new Scanner(archi);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
                
                VentanaSintaxis ventana2 = new VentanaSintaxis();
                //Limpiar isntrucciones
                String a = new String();
                String b = new String();
                
                Vector<String> aux = new Vector<String>();
                Vector<String> aux2 = new Vector<String>();
                
                AnalizadorDeSimbolos analiza = new AnalizadorDeSimbolos();
                //En esta parte del código se obtiene el código y se transforma a caracteres manejables
                while (scn.hasNextLine()) {
                    a = scn.nextLine();
                    if(a.equals("\n") == false){
                        //System.out.println(a);
                        b = analiza.adaptar(a);
                        todo.add(b.toUpperCase());
                        /*String[] parts = b.split(" ");
                        for(int j = 0; j<parts.length; j += 1){
                            //System.out.println(parts[j]+",");
                            if(!" ".equals(parts[j]) && !"\n".equals(parts[j]) && !"".equals(parts[j]) && !"\t".equals(parts[j])){
                                //System.out.println(parts[j].toUpperCase());
                                parts[j] = parts[j].replace(" ","");
                                parts[j] = parts[j].replace("\t","");
                                parts[j] = parts[j].replace(" ","");
                                if(parts[j].toUpperCase().equals(".CODE") || parts[j].toUpperCase().equals(".DATA") || parts[j].toUpperCase().equals(".STACK") || parts[j].toUpperCase().equals("BYTE") || parts[j].toUpperCase().equals("WORD")){
                                    todo.add(parts[j].toUpperCase()+" "+parts[j+1].toUpperCase());
                                    j += 1;
                                }else{
                                    todo.add(parts[j].toUpperCase());
                                }
                            }
                        }*/
                    }
                }
                
                //analiza.imprimirSystem(todo);
               /* analiza.AjustaComas(todo, aux);
                todo = new Vector<String>();
                analiza.AjustaCorchetes(aux, aux2);
                aux = new Vector<String>();               
                analiza.AjustaDUP(aux2, todo);
                aux2 = new Vector<String>();
                analiza.AjustaCorchetes2(todo, aux);
                //todo = aux; 
                todo = analiza.identificador(aux, txaAnalisis);*/
                //analiza.imprimirSystem(todo2);
                //Imprime la tabla de simbolos
                //int ve[] = analiza.SegmentoD(todo2);
                //analiza.iniTS(ventana2.dtm, todo2, ve);
                //int lim = analiza.limit(todo);
                analiza.iniTS(ventana2.dtm, todo.size());
                analiza.CreaTablaDeSimbolos(ventana2.dtm2,ventana2.dtm, todo);
                //analiza.pruebaElim(ventana2.dtm, todo.size()-1);
                analiza.corrijeTable(ventana2.dtm);
                analiza.corrijeTable(ventana2.dtm2);
                analiza.AgregaEtiq(ventana2.dtm, todo);
                //Impreme las instrucciones a la tabla
                analiza.preparaInstu(todo, aux2, ventana2.dtm2);                        
                //analiza.AnalizadorDeDatos(ventana2.dtm2, todo);
                analiza.Pila(ventana2.dtm2, todo);
                analiza.Datos(ventana2.dtm2, todo);
                //analiza.AnalizadorDeCodigo(ventana2.dtm2, todo, ventana2.dtm);
                analiza.Codigo(ventana2.dtm2, ventana2.dtm, todo);
                ventana2.setVisible(true);
            }catch(Exception ea){
                JOptionPane.showMessageDialog(this, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(VentanaSintaxis.class.getName()).log(Level.SEVERE, null, ea);
            }
        }
    }
}
