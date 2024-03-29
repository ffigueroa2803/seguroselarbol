package CapaNegocios;

import CapaDatos.Conexion;
import aseguradora.AseguradoraView;
import com.mysql.jdbc.CallableStatement;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
public class ContratoVida {

    
    private int idContratoVida;

    private int idSeguroVida;

    private String descripcion;

    private String profesion;

    private Date fechaContrato;

    private Date fechaPago;

    private double mora;

    private Date vencimiento;

    private int numeroPagos;

    private boolean activo;

    private double montoPagoSeguro;
    
    private Cliente cliente=null;
    
    private String identificacion;

    public ContratoVida () {
    }

    public boolean getActivo () {
        return activo;
    }

    public void setActivo (boolean val) {
        this.activo = val;
    }

    public String getDescripcion () {
        return descripcion;
    }

    public void setDescripcion (String val) {
        this.descripcion = val;
    }

    public Date getFechaContrato () {
        return fechaContrato;
    }

    public void setFechaContrato (Date val) {
        this.fechaContrato = val;
    }

    public Date getFechaPago () {
        return fechaPago;
    }

    public void setFechaPago (Date val) {
        this.fechaPago = val;
    }

    public int getIdContratoVida () {
        return idContratoVida;
    }

    public void setIdContratoVida (int val) {
        this.idContratoVida = val;
    }

    public int getIdSeguroVida () {
        return idSeguroVida;
    }

    public void setIdSeguroVida (int val) {
        this.idSeguroVida = val;
    }

    public double getMontoPagoSeguro () {
        return montoPagoSeguro;
    }

    public void setMontoPagoSeguro (double val) {
        this.montoPagoSeguro = val;
    }

    public double getMora () {
        return mora;
    }

    public void setMora (double val) {
        this.mora = val;
    }

    public int getNumeroPagos () {
        return numeroPagos;
    }

    public void setNumeroPagos (int val) {
        this.numeroPagos = val;
    }

    public String getProfesion () {
        return profesion;
    }

    public void setProfesion (String val) {
        this.profesion = val;
    }

    public Date getVencimiento () {
        return vencimiento;
    }

    public void setVencimiento (Date val) {
        this.vencimiento = val;
    }

    public void setCliente (Cliente unCliente){
        this.cliente = unCliente;
    }
    public Cliente getCliente(){
        return this.cliente;
    }
    public void setIdentificacion(String identificacion){
        this.identificacion=identificacion;
    }
    public String getIdentificacion(){
        return this.identificacion;
    }
    public void insertarEnBaseDeDatos(Cliente clienteSeleccionado, SeguroVida seguroSeleccionado, ArrayList<Beneficiario> listaBeneficiariosNuevos,ArrayList<Beneficiario> listaBeneficiariosExistenetes) throws SQLException{

            Connection con = (Connection) Conexion.obtenerConexion();            
            Statement comando = (Statement) con.createStatement();            
            PreparedStatement cmd = (PreparedStatement) con.prepareStatement("SELECT insersionNuevaPolizaVidaRetorno(?,?,?,?,?,?,?,?,?)");
            
            CallableStatement funcion = (CallableStatement) con.prepareCall("{CALL insertarTuplaSeguroVidaClienteSeguro(?,?,?)}");
            
        try {            
            cmd.setInt(1,seguroSeleccionado.getIdSeguroVida());
            cmd.setString(2, this.getDescripcion());
            cmd.setString(3, this.profesion);
            cmd.setDate(4, this.fechaContrato);
            cmd.setDate(5, this.fechaPago);
            cmd.setDouble(6, this.mora);
            cmd.setDate(7, this.vencimiento);
            cmd.setInt(8, this.numeroPagos);
            cmd.setDouble(9, this.montoPagoSeguro);
            //insertarTuplaSeguroVidaClienteSeguro-agente int, cliente int, contratoVida int
   
            boolean execute = comando.execute("Begin;");  //inicia la transaccion
            
            execute = cmd.execute(); //ejecuta la llamada a la funcion de insersión de contrato vida            
            ResultSet rs = cmd.getResultSet(); //obtene el resultado de la consulta
            execute = rs.next();
            this.setIdContratoVida(rs.getInt(1)); //ese resultado lo almacena en una variable, es el id del ultimo registro insertado
            
            funcion.setInt(1, aseguradora.AseguradoraView.idEmpleado);
            funcion.setInt(2, clienteSeleccionado.getIdCliente());
            funcion.setInt(3,this.idContratoVida);
            
            for (Beneficiario i:listaBeneficiariosNuevos){
                CallableStatement insertarBeneficiario = (CallableStatement) con.prepareCall("{CALL insertarBeneficiarioSeguroVida(?,?,?,?,?,?,?,?)}");
                i.setIdContratoVida(this.idContratoVida);
                insertarBeneficiario.setInt(1, this.idContratoVida);
                insertarBeneficiario.setString(2, i.getDPI());
                insertarBeneficiario.setString(3, i.getNombres());
                insertarBeneficiario.setString(4, i.getApellidos());
                insertarBeneficiario.setDate(5, i.getFechaNacimiento());
                insertarBeneficiario.setString(6, i.getDireccion());
                insertarBeneficiario.setString(7, i.getTelefono());
                insertarBeneficiario.setString(8, i.getCelular());
                execute = insertarBeneficiario.execute();
                
            }
            
            for (Beneficiario j:listaBeneficiariosExistenetes){
                /*
                 * IMPORTANTISIMO: AQUI SE HACE UNA INSERSIÓN A UNA TABLA INTERMEDIA ENTRE SEGURO Y BENEFICIARIO, YA NO HACIA BENEFICIARIO, PORQUE ÉSTE YA EXISTE
                 */
                String consulta = "INSERT INTO SeguroVidaBeneficiarios (Beneficiarios_idBeneficiarios,ContratoVida_idContratoVida) VALUES ("+j.getIdBeneficiario()+","+this.idContratoVida+")";
                execute = comando.execute(consulta);
            }
            
            execute = funcion.execute(); //ejecuta la llamada al procedimiento que inserta la tupla en la tabla cliente-seguro            
            execute = comando.execute("commit;");  //termina la transacción         
            
            funcion.close();
            comando.close();
            cmd.close();
            
        } catch (SQLException ex) {
            boolean execute = comando.execute("rollback;");
            Logger.getLogger(ContratoVida.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static ContratoVida[] listaPolizasVida() throws SQLException{
        ArrayList<ContratoVida> lista = new ArrayList<ContratoVida>();
        Connection con = (Connection) Conexion.obtenerConexion();
        String consulta = 
                "SELECT "
                + "CV.idContratoVida,"
                + "CV.Identificacion,"
                + "CV.Descripcion,"
                + "Cl.Nombres,"
                + "Cl.Apellidos,"
                + "Cl.NIT,"
                + "Cl.DPI "
                + "FROM ContratoVida AS CV "
                + "INNER JOIN ClienteSeguro as CS on CS.ContratoVida_idContratoVida = CV.idContratoVida "
                + "INNER JOIN Cliente AS Cl on CS.Cliente_idAgente = Cl.idCliente";
        
        Statement query = (Statement) con.createStatement();
        
        ResultSet rs = query.executeQuery(consulta);
        
        while (rs.next()){
            ContratoVida i = new ContratoVida();
            Cliente ci = new Cliente();            
            i.setIdContratoVida(rs.getInt(1));
            i.setIdentificacion(rs.getString(2));
            i.setDescripcion(rs.getString(3));
            ci.setNombres(rs.getString(4));
            ci.setApellidos(rs.getString(5));
            ci.setNIT(rs.getString(6));
            ci.setDPI(rs.getString(7));
            i.setCliente(ci);            
            lista.add(i);
        }      
        ContratoVida [] ls = new ContratoVida[lista.size()];
        ls = lista.toArray(ls);
        return ls;
    }
    public void completarDatos() throws SQLException{
        Connection con = (Connection) Conexion.obtenerConexion();
        
        String consulta = "SELECT "
                + "CV.SeguroVida_idSeguroVida,"
                + "CV.Profesion,"
                + "CV.FechaContrato,"
                + "CV.FechaPago,"
                + "CV.Mora,"
                
                + "CV.Vencimiento,"
                + "CV.NumeroPagos,"
                + "CV.MontoPagoSeguro,"
                
                + "Cl.Direccion,"
                + "Cl.Telefono,"
                + "Cl.Celular,"
                + "Cl.FechaNacimiento,"
                + "Cl.edad "
                
                + "FROM ContratoVida AS CV "
                + "INNER JOIN ClienteSeguro as CS on CS.ContratoVida_idContratoVida = CV.idContratoVida "
                + "INNER JOIN Cliente AS Cl on CS.Cliente_idAgente = Cl.idCliente "
                + "WHERE CV.idContratoVida = "
                + Integer.toString(this.idContratoVida);
        
        Statement query = (Statement) con.createStatement();
        
        ResultSet rs = query.executeQuery(consulta);
        
        while (rs.next()){
            
            Cliente ci = this.cliente;
            this.setCliente(ci);

            this.idSeguroVida = rs.getInt(1);
            this.profesion = rs.getString(2);
            this.fechaContrato = rs.getDate(3);
            this.fechaPago = rs.getDate(4);
            this.mora = rs.getDouble(5);

            this.vencimiento = rs.getDate(6);
            this.numeroPagos = rs.getInt(7);
            this.montoPagoSeguro = rs.getDouble(8);

            ci.setDireccion(rs.getString(9));
            ci.setTelefono(rs.getString(10));
            ci.setCelular(rs.getString(11));
            ci.setFechaNacimiento(rs.getDate(12));
            ci.setEdad(rs.getInt(13));
            this.cliente=ci;
        }
    }
    public static void desactivarSegurosVencidos() throws SQLException{
        ContratoAuto.desactivarSegurosVencidos();
    }
    
    public static ContratoVida[] polizasPorCliente(Cliente unCliente) throws SQLException {
        
        ArrayList<ContratoVida> lista = new ArrayList<ContratoVida>();
        Connection con = (Connection) Conexion.obtenerConexion();
        String consulta = "SELECT CV.idContratoVida, "
                + "CV.Identificacion,"
                + "CV.Descripcion "
                + "FROM ContratoVida AS CV "
                + "INNER JOIN ClienteSeguro as CS on CS.ContratoVida_idContratoVida = CV.idContratoVida "
                + "INNER JOIN Cliente AS Cl on CS.Cliente_idAgente = Cl.idCliente WHERE "
                + "Cl.idCliente = "+Integer.toString(unCliente.getIdCliente());
        
        Statement query = (Statement) con.createStatement();
        
        ResultSet rs = query.executeQuery(consulta);
        
        while (rs.next()){
            ContratoVida i = new ContratoVida();          
            i.setIdContratoVida(rs.getInt(1));
            i.setIdentificacion(rs.getString(2));
            i.setDescripcion(rs.getString(3));
            i.setCliente(unCliente);            
            lista.add(i);
        }      
        ContratoVida [] ls = new ContratoVida[lista.size()];
        ls = lista.toArray(ls);
        return ls;
    }
    
     public void cancelarPoliza(String razonCancelacion) throws SQLException {
        String consulta = "UPDATE ContratoVida SET Activo = 0 WHERE idContratoVida = "+Integer.toString(this.idContratoVida);
        Connection con = (Connection) Conexion.obtenerConexion();
        Statement st = (Statement) con.createStatement();
        st.executeUpdate(consulta);
        ///idHistorialSeguro, Anotacion, Fecha, Hora, idSeguroVida, idSeguroHogar, idSeguroAuto, Agente_idAgente, Cliente_idCliente
        consulta = "INSERT INTO HistorialSeguro (Anotacion, Fecha, Hora, idSeguroVida, Agente_idAgente, Cliente_idCliente) "
                + "VALUES ("
                + "'"+razonCancelacion+"',"
                + "CURDATE(),"
                + "CURTIME(),"
                + Integer.toString(this.idContratoVida)+","
                + Integer.toString(AseguradoraView.idEmpleado)+","
                + Integer.toString(this.cliente.getIdCliente())
                + ")";
        st.execute(consulta);
        st.close();
    }
     
     public static ContratoVida[] polizasActivasPorCliente(Cliente unCliente) throws SQLException {
        
        ArrayList<ContratoVida> lista = new ArrayList<ContratoVida>();
        Connection con = (Connection) Conexion.obtenerConexion();
        String consulta = "SELECT CV.idContratoVida, "
                + "CV.Identificacion,"
                + "CV.Descripcion "
                + "FROM ContratoVida AS CV "
                + "INNER JOIN ClienteSeguro as CS on CS.ContratoVida_idContratoVida = CV.idContratoVida "
                + "INNER JOIN Cliente AS Cl on CS.Cliente_idAgente = Cl.idCliente WHERE "
                + "CV.Activo = 1 AND Cl.idCliente = "+Integer.toString(unCliente.getIdCliente());
        
        Statement query = (Statement) con.createStatement();
        
        ResultSet rs = query.executeQuery(consulta);
        
        while (rs.next()){
            ContratoVida i = new ContratoVida();          
            i.setIdContratoVida(rs.getInt(1));
            i.setIdentificacion(rs.getString(2));
            i.setDescripcion(rs.getString(3));
            i.setCliente(unCliente);            
            lista.add(i);
        }      
        ContratoVida [] ls = new ContratoVida[lista.size()];
        ls = lista.toArray(ls);
        return ls;
    }
     
     public static ContratoVida[] polizasNoActivasPorCliente(Cliente unCliente) throws SQLException {
        
        ArrayList<ContratoVida> lista = new ArrayList<ContratoVida>();
        Connection con = (Connection) Conexion.obtenerConexion();
        String consulta = "SELECT CV.idContratoVida, "
                + "CV.Identificacion,"
                + "CV.Descripcion "
                + "FROM ContratoVida AS CV "
                + "INNER JOIN ClienteSeguro as CS on CS.ContratoVida_idContratoVida = CV.idContratoVida "
                + "INNER JOIN Cliente AS Cl on CS.Cliente_idAgente = Cl.idCliente WHERE "
                + "CV.Activo = 0 AND Cl.idCliente = "+Integer.toString(unCliente.getIdCliente());
        
        Statement query = (Statement) con.createStatement();
        
        ResultSet rs = query.executeQuery(consulta);
        
        while (rs.next()){
            ContratoVida i = new ContratoVida();          
            i.setIdContratoVida(rs.getInt(1));
            i.setIdentificacion(rs.getString(2));
            i.setDescripcion(rs.getString(3));
            i.setCliente(unCliente);            
            lista.add(i);
        }      
        ContratoVida [] ls = new ContratoVida[lista.size()];
        ls = lista.toArray(ls);
        return ls;
    }

    public void renovar() {
        //Descripcion, 
        //Profesion, FechaContrato, FechaPago, Mora, Vencimiento, NumeroPagos, Activo, MontoPagoSeguro
                try{
            String consulta = "UPDATE ContratoVida "
                    + "SET Activo = 1, "
                    + "FechaContrato = ?,"
                    + "Descripcion = ?,"
                    + "FechaPago = ?,"
                    + "Mora = ?,"
                    + "Profesion = ?,"
                    + "Vencimiento = ?,"
                    + "NumeroPagos = ?,"
                    + "MontoPagoSeguro = ? "
                    + "WHERE idContratoVida = "+Integer.toString(this.idContratoVida);
            Connection con = (Connection) Conexion.obtenerConexion();
            Statement st = (Statement) con.createStatement();
            PreparedStatement ps = (PreparedStatement) con.prepareStatement(consulta);
            //st.executeUpdate(consulta);
            ///idHistorialSeguro, Anotacion, Fecha, Hora, idSeguroVida, idSeguroHogar, idSeguroAuto, Agente_idAgente, Cliente_idCliente
            ps.setDate(1, this.fechaContrato);
            ps.setString(2, this.descripcion);
            ps.setDate(3, this.fechaPago);
            ps.setDouble(4, mora);
            ps.setString(5, profesion);
            ps.setDate(6, vencimiento);
            ps.setInt(7, numeroPagos);
            ps.setDouble(8, montoPagoSeguro);

            boolean execute = ps.execute();

            consulta = "INSERT INTO HistorialSeguro (Anotacion, Fecha, Hora, idSeguroVida, Agente_idAgente, Cliente_idCliente) "
                    + "VALUES ("
                    + "'Póliza de seguro de hogar renovada',"
                    + "CURDATE(),"
                    + "CURTIME(),"
                    + Integer.toString(this.idContratoVida)+","
                    + Integer.toString(AseguradoraView.idEmpleado)+","
                    + Integer.toString(this.cliente.getIdCliente())
                    + ")";
            st.execute(consulta);
            st.close();
            ps.close();
        }
        catch (SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

}

