/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SeleccionarPolizaSeguroHogar.java
 *
 * Created on 12/11/2011, 06:00:17 PM
 */
package ModuloSeguros;

import CapaNegocios.Cliente;
import CapaNegocios.ContratoAuto;
import CapaNegocios.ContratoHogar;
import CapaNegocios.ContratoVida;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author HP G42
 */
public class SeleccionarPolizaSeguro extends javax.swing.JDialog {
    
    public static int SELECCIONAR_POLIZAS_SEGURO_VIDA = 1;
    public static int SELECCIONAR_POLIZAS_SEGURO_HOGAR = 0;
    public static int SELECCIONAR_POLIZAS_SEGURO_AUTO = 2;

    
    private int tipoPolizas = 0;  //0 es para seguro de hogar, 1 es para seguro de vida, 2 es para seguro de autos
    private ContratoVida[] listaPolizasVida = null;
    private ContratoHogar[] listaPolizasHogar = null;
    private ContratoAuto[] listaPolizasAuto = null;
    private Object seleccionado = null;
    /** Creates new form SeleccionarPolizaSeguroHogar */
    public SeleccionarPolizaSeguro(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    public Object cargarSeguros(int bandera){
        this.tipoPolizas = bandera;
        
        try{
            if (tipoPolizas==1){ //si se esta buscando un seguro de vida
                listaPolizasVida = ContratoVida.listaPolizasVida();
                llenarTablaPolizasVida();
                
            }
            else if (tipoPolizas==0){ //o si se esta seleccionado un seguro de hogar.
                listaPolizasHogar = ContratoHogar.listaPolizasHogar();
                llenarTablaPolizasHogar();
            }
            else if (tipoPolizas==2){
                listaPolizasAuto = ContratoAuto.listaPolizasAuto();
                llenarTablaPolizasAuto();
            }
            
        }
        catch (SQLException ex){
            JOptionPane.showMessageDialog(rootPane, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        this.setVisible(true);
        return this.seleccionado;
    }
    
    public Object cargarSegurosHogarActivosPorCliente(Cliente unCliente){
        try {
            this.tipoPolizas = 0;
            listaPolizasHogar = ContratoHogar.polizasActivasPorCliente(unCliente);
            llenarTablaPolizasHogar();
            this.setVisible(true);
            return this.seleccionado;
        } catch (SQLException ex) {
            //Logger.getLogger(SeleccionarPolizaSeguro.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(rootPane, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    public Object cargarSegurosHogarPorCliente(Cliente unCliente){
        try {
            this.tipoPolizas = 0;
            listaPolizasHogar = ContratoHogar.polizasPorCliente(unCliente);
            llenarTablaPolizasHogar();
            this.setVisible(true);
            return this.seleccionado;
        } catch (SQLException ex) {
            //Logger.getLogger(SeleccionarPolizaSeguro.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(rootPane, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    public Object cargarSegurosHogarNoActivosPorCliente(Cliente unCliente){
        try {
            this.tipoPolizas = 0;
            listaPolizasHogar = ContratoHogar.polizasNoActivasPorCliente(unCliente);
            llenarTablaPolizasHogar();
            this.setVisible(true);
            return this.seleccionado;
        } catch (SQLException ex) {
            //Logger.getLogger(SeleccionarPolizaSeguro.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(rootPane, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    public Object cargarSegurosVidaActivosPorCliente(Cliente unCliente){
        try {
            this.tipoPolizas = 1;
            listaPolizasVida = ContratoVida.polizasActivasPorCliente(unCliente);
            llenarTablaPolizasVida();
            this.setVisible(true);
            return this.seleccionado;
        } catch (SQLException ex) {
            //Logger.getLogger(SeleccionarPolizaSeguro.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(rootPane, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    public Object cargarSegurosVidaNoActivosPorCliente(Cliente unCliente){
        try {
            this.tipoPolizas = 1;
            listaPolizasVida = ContratoVida.polizasNoActivasPorCliente(unCliente);
            llenarTablaPolizasVida();
            this.setVisible(true);
            return this.seleccionado;
        } catch (SQLException ex) {
            //Logger.getLogger(SeleccionarPolizaSeguro.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(rootPane, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    public Object cargarSegurosVidaPorCliente(Cliente unCliente){
        try {
            this.tipoPolizas = 1;
            listaPolizasVida = ContratoVida.polizasPorCliente(unCliente);
            llenarTablaPolizasVida();
            this.setVisible(true);
            return this.seleccionado;
        } catch (SQLException ex) {
            //Logger.getLogger(SeleccionarPolizaSeguro.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(rootPane, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    public Object cargarSegurosAutoActivosPorCliente(Cliente unCliente){
        try {
            this.tipoPolizas = 2;
            listaPolizasAuto = ContratoAuto.polizasActivasPorCliente(unCliente);
            llenarTablaPolizasAuto();
            this.setVisible(true);
            return this.seleccionado;
        } catch (SQLException ex) {
            //Logger.getLogger(SeleccionarPolizaSeguro.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(rootPane, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    public Object cargarSegurosAutoPorCliente(Cliente unCliente){
        try {
            this.tipoPolizas = 2;
            listaPolizasAuto = ContratoAuto.polizasPorCliente(unCliente);
            llenarTablaPolizasAuto();
            this.setVisible(true);
            return this.seleccionado;
        } catch (SQLException ex) {
            //Logger.getLogger(SeleccionarPolizaSeguro.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(rootPane, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    public Object cargarSegurosAutoNoActivosPorCliente(Cliente unCliente){
        try {
            this.tipoPolizas = 2;
            listaPolizasAuto = ContratoAuto.polizasNoActivasPorCliente(unCliente);
            llenarTablaPolizasAuto();
            this.setVisible(true);
            return this.seleccionado;
        } catch (SQLException ex) {
            //Logger.getLogger(SeleccionarPolizaSeguro.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(rootPane, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    private void llenarTablaPolizasVida(){
        jTable1.removeAll();
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Numero de póliza");
        modelo.addColumn("Descripcion");
        modelo.addColumn("DPI cliente");
        modelo.addColumn("NIT cliente");
        modelo.addColumn("Nombre");
        
        for (ContratoVida i:listaPolizasVida){
            Object [] fila = new Object[5];
            fila[0]=i.getIdentificacion();
            fila[1]=i.getDescripcion();
            fila[2]=i.getCliente().getDPI();
            fila[3]=i.getCliente().getNIT();
            fila[4]=i.getCliente().getNombres()+" "+i.getCliente().getApellidos();
            modelo.addRow(fila);
        }
        
        jTable1.setModel(modelo);
    }
    
    public void llenarTablaPolizasHogar(){
        jTable1.removeAll();
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Numero de póliza");
        modelo.addColumn("Descripcion");
        modelo.addColumn("DPI cliente");
        modelo.addColumn("NIT cliente");
        modelo.addColumn("Nombre");
        
        for (ContratoHogar i:listaPolizasHogar){
            Object [] fila = new Object[5];
            fila[0]=i.getIdentificacion();
            fila[1]=i.getDescripcion();
            fila[2]=i.getCliente().getDPI();
            fila[3]=i.getCliente().getNIT();
            fila[4]=i.getCliente().getNombres()+" "+i.getCliente().getApellidos();
            modelo.addRow(fila);
        }
        
        jTable1.setModel(modelo);
    }
    
    private void llenarTablaPolizasAuto()
    {
        jTable1.removeAll();
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Numero de póliza");
        modelo.addColumn("Descripcion");
        modelo.addColumn("DPI cliente");
        modelo.addColumn("NIT cliente");
        modelo.addColumn("Nombre");
        
        for (ContratoAuto i:listaPolizasAuto){
            Object [] fila = new Object[5];
            fila[0]=i.getIdentificacion();
            fila[1]=i.getDescripcion();
            fila[2]=i.getCliente().getDPI();
            fila[3]=i.getCliente().getNIT();
            fila[4]=i.getCliente().getNombres()+" "+i.getCliente().getApellidos();
            modelo.addRow(fila);
        }
        
        jTable1.setModel(modelo);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(aseguradora.AseguradoraApp.class).getContext().getResourceMap(SeleccionarPolizaSeguro.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.setName("jTable1"); // NOI18N
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jTextField1.setText(resourceMap.getString("jTextField1.text")); // NOI18N
        jTextField1.setName("jTextField1"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setName("jButton3"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 895, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 422, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jButton3)
                    .addComponent(jButton2))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (!jTextField1.getText().isEmpty()){
            Object valorDeIteracion;
            String buscado = jTextField1.getText();
            TableModel model = jTable1.getModel();
            int filas;
            int indiceEncontrado = -1;
            filas = model.getRowCount();
            for (int i=0; i<filas; i++){
                valorDeIteracion = model.getValueAt(i,0);
                if (buscado.equals(valorDeIteracion.toString())){
                    indiceEncontrado = i;
                    break;
                }
            }            
            if (indiceEncontrado>=0){                
                if (tipoPolizas==1){
                    seleccionado = listaPolizasVida[indiceEncontrado];                    
                }
                else if (tipoPolizas==0){
                    seleccionado = listaPolizasHogar[indiceEncontrado];                    
                }
                else if (tipoPolizas==2){
                    seleccionado = listaPolizasAuto[indiceEncontrado];                    
                }
                
                jTable1.setRowSelectionInterval(indiceEncontrado, indiceEncontrado);
            }           
        }        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        int selectedRow = jTable1.getSelectedRow();
        if (tipoPolizas==1){
            seleccionado = listaPolizasVida[selectedRow];                    
        }
        else if (tipoPolizas==0){
            seleccionado = listaPolizasHogar[selectedRow];                    
        }
        else if (tipoPolizas==2){
            seleccionado = listaPolizasAuto[selectedRow];                    
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        seleccionado=null;
        this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SeleccionarPolizaSeguro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SeleccionarPolizaSeguro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SeleccionarPolizaSeguro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SeleccionarPolizaSeguro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                SeleccionarPolizaSeguro dialog = new SeleccionarPolizaSeguro(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
