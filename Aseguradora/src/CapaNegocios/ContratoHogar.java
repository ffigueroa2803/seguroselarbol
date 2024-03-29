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
import javax.swing.JOptionPane;
public class ContratoHogar {

    
    private int idContratoHogar;

    private int idSeguroHogar;

    private Date fechaContrato;

    private Date fechaPago;

    private double Mora;

    private String Descripcion;

    private Date vencimiento;

    private double valorInmueble;

    private double valorMuebles;

    private int numeroPagos;

    private boolean activo;

    private double montoPagoseguro;
    
    private Cliente cliente=null;
    
    private String identificacion;

    public ContratoHogar () {
    }

    public String getDescripcion () {
        return Descripcion;
    }

    public void setDescripcion (String val) {
        this.Descripcion = val;
    }

    public double getMora () {
        return Mora;
    }

    public void setMora (double val) {
        this.Mora = val;
    }

    public boolean getActivo () {
        return activo;
    }

    public void setActivo (boolean val) {
        this.activo = val;
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

    public int getIdContratoHogar () {
        return idContratoHogar;
    }

    public void setIdContratoHogar (int val) {
        this.idContratoHogar = val;
    }

    public int getIdSeguroHogar () {
        return idSeguroHogar;
    }

    public void setIdSeguroHogar (int val) {
        this.idSeguroHogar = val;
    }

    public double getMontoPagoseguro () {
        return montoPagoseguro;
    }

    public void setMontoPagoseguro (double val) {
        this.montoPagoseguro = val;
    }

    public int getNumeroPagos () {
        return numeroPagos;
    }

    public void setNumeroPagos (int val) {
        this.numeroPagos = val;
    }

    public double getValorInmueble () {
        return valorInmueble;
    }

    public void setValorInmueble (double val) {
        this.valorInmueble = val;
    }

    public double getValorMuebles () {
        return valorMuebles;
    }

    public void setValorMuebles (double val) {
        this.valorMuebles = val;
    }

    public Date getVencimiento () {
        return vencimiento;
    }

    public void setVencimiento (Date val) {
        this.vencimiento = val;
    }
    
    public void setCliente(Cliente unCliente){
        this.cliente=unCliente;
    }
    public Cliente getCliente(){
        return this.cliente;
    }
    public void setIdentificacion(String identificacion){
        this.identificacion = identificacion;
    }
    public String getIdentificacion(){
        return this.identificacion;
    }

    public void insertarEnBaseDatos(Cliente clienteSeleccionado, SeguroHogar seguroSeleccionado, ArrayList<Beneficiario> listaBeneficiariosNuevos, ArrayList<Beneficiario> listaBeneficiariosExistentes) throws SQLException {
        Connection con = (Connection) Conexion.obtenerConexion();            
            Statement comando = (Statement) con.createStatement();  
            PreparedStatement cmd = (PreparedStatement) con.prepareStatement("SELECT insersionNuevaPolizaHogarRetorno(?,?,?,?,?,?,?,?,?,?)");
            //insertarTuplaSeguroHogarClienteSeguro(agente int, cliente int, contratoHogar int)
            CallableStatement funcion = (CallableStatement) con.prepareCall("{CALL insertarTuplaSeguroHogarClienteSeguro(?,?,?)}");
            try{
                this.setIdSeguroHogar(seguroSeleccionado.getIdSeguroHogar());
                cmd.setInt(1, seguroSeleccionado.getIdSeguroHogar());
                cmd.setDate(2, this.fechaContrato);
                cmd.setDate(3, fechaPago);
                cmd.setDouble(4, Mora);
                cmd.setString(5, Descripcion);
                cmd.setDate(6, vencimiento);
                cmd.setDouble(7,valorInmueble);
                cmd.setDouble(8, valorMuebles);
                cmd.setInt(9, numeroPagos);
                cmd.setDouble(10, montoPagoseguro);

                boolean execute = comando.execute("BEGIN;");

                execute = cmd.execute(); //ejecuta la llamada a la funcion de insersión de contrato vida            
                ResultSet rs = cmd.getResultSet(); //obtene el resultado de la consulta
                execute = rs.next();
                this.setIdContratoHogar(rs.getInt(1)); //ese resultado lo almacena en una variable, es el id del ultimo registro insertado

                funcion.setInt(1, aseguradora.AseguradoraView.idEmpleado);
                funcion.setInt(2, clienteSeleccionado.getIdCliente());
                funcion.setInt(3,this.idContratoHogar);

                for (Beneficiario i:listaBeneficiariosNuevos){
                    CallableStatement insertarBeneficiario = (CallableStatement) con.prepareCall("{CALL insertarBeneficiarioSeguroHogar(?,?,?,?,?,?,?,?)}");
                    i.setIdContratoVida(this.idContratoHogar);
                    insertarBeneficiario.setInt(1, this.idContratoHogar);
                    insertarBeneficiario.setString(2, i.getDPI());
                    insertarBeneficiario.setString(3, i.getNombres());
                    insertarBeneficiario.setString(4, i.getApellidos());
                    insertarBeneficiario.setDate(5, i.getFechaNacimiento());
                    insertarBeneficiario.setString(6, i.getDireccion());
                    insertarBeneficiario.setString(7, i.getTelefono());
                    insertarBeneficiario.setString(8, i.getCelular());
                    execute = insertarBeneficiario.execute();

                }            
                for (Beneficiario j:listaBeneficiariosExistentes){
                    /*
                     * IMPORTANTISIMO: AQUI SE HACE UNA INSERSIÓN A UNA TABLA INTERMEDIA ENTRE SEGURO Y BENEFICIARIO, YA NO HACIA BENEFICIARIO, PORQUE ÉSTE YA EXISTE
                     */
                    String consulta = "INSERT INTO SeguroHogarBeneficiarios (Beneficiarios_idBeneficiarios, ContratoHogar_idContratoHogar) VALUES ("+j.getIdBeneficiario()+","+this.idContratoHogar+")";
                    execute = comando.execute(consulta);
                }


                execute = funcion.execute();

                execute = comando.execute("COMMIT;");

                funcion.close();
                comando.close();
                cmd.close();
            }
            catch (SQLException ex){
                 boolean execute = comando.execute("ROLLBACK");
            }
    }

    public static ContratoHogar[] listaPolizasHogar() throws SQLException {
        ArrayList<ContratoHogar> lista = new ArrayList<ContratoHogar>();
        Connection con = (Connection) Conexion.obtenerConexion();
        String consulta = "SELECT CH.idContratoHogar, CH.Identificacion,CH.Descripcion,Cl.Nombres,Cl.Apellidos,Cl.NIT,Cl.DPI FROM ContratoHogar AS CH INNER JOIN ClienteSeguro as CS on CS.ContratoHogar_idContratoHogar = CH.idContratoHogar INNER JOIN Cliente AS Cl on CS.Cliente_idAgente = Cl.idCliente;";
        
        Statement query = (Statement) con.createStatement();
        
        ResultSet rs = query.executeQuery(consulta);
        
        while (rs.next()){
            ContratoHogar i = new ContratoHogar();
            Cliente ci = new Cliente();            
            i.setIdContratoHogar(rs.getInt(1));
            i.setIdentificacion(rs.getString(2));
            i.setDescripcion(rs.getString(3));
            ci.setNombres(rs.getString(4));
            ci.setApellidos(rs.getString(5));
            ci.setNIT(rs.getString(6));
            ci.setDPI(rs.getString(7));
            i.setCliente(ci);            
            lista.add(i);
        }      
        ContratoHogar [] ls = new ContratoHogar[lista.size()];
        ls = lista.toArray(ls);
        return ls;
    }
    
    public static ContratoHogar[] polizasPorCliente(Cliente unCliente) throws SQLException {
        ArrayList<ContratoHogar> lista = new ArrayList<ContratoHogar>();
        Connection con = (Connection) Conexion.obtenerConexion();
        String consulta = "SELECT CH.idContratoHogar, CH.Identificacion,CH.Descripcion FROM ContratoHogar AS CH INNER JOIN ClienteSeguro as CS on CS.ContratoHogar_idContratoHogar = CH.idContratoHogar INNER JOIN Cliente AS Cl on CS.Cliente_idAgente = Cl.idCliente WHERE Cl.idCliente = "+Integer.toString(unCliente.getIdCliente());
        
        Statement query = (Statement) con.createStatement();
        
        ResultSet rs = query.executeQuery(consulta);
        
        while (rs.next()){
            ContratoHogar i = new ContratoHogar();          
            i.setIdContratoHogar(rs.getInt(1));
            i.setIdentificacion(rs.getString(2));
            i.setDescripcion(rs.getString(3));
            i.setCliente(unCliente);            
            lista.add(i);
        }      
        ContratoHogar [] ls = new ContratoHogar[lista.size()];
        ls = lista.toArray(ls);
        return ls;
    }
    
    public void completarDatos() throws SQLException{
        Connection con = (Connection) Conexion.obtenerConexion();
        String consulta = "SELECT "
                + "CH.SeguroHogar_idSeguroHogar,"
                + "CH.FechaContrato,"
                + "CH.FechaPago,"
                + "CH.Mora,"
                + "CH.Vencimiento,"
                
                + "CH.ValorInmueble,"
                + "CH.ValorMueble,"
                + "CH.NumeroPagos,"
                + "CH.MontoPagoSeguro,"               
                + "Cl.idCliente,"
                
                + "Cl.Direccion,"
                + "Cl.Telefono,"
                + "Cl.Celular,"
                + "Cl.FechaNacimiento,"
                + "Cl.edad "
                
                + "FROM ContratoHogar AS CH "
                + "INNER JOIN ClienteSeguro as CS on CS.ContratoHogar_idContratoHogar = CH.idContratoHogar "
                + "INNER JOIN Cliente AS Cl on CS.Cliente_idAgente = Cl.idCliente "
                + "WHERE CH.idContratoHogar = "
                + Integer.toString(this.idContratoHogar);
        
        // SeguroHogar_idSeguroHogar, FechaContrato, FechaPago, Mora,  Vencimiento, ValorInmueble, ValorMueble, NumeroPagos, Activo, MontoPagoSeguro
        //idCliente, DPI, NIT, Nombres, Apellidos, Direccion, Telefono, Celular, FechaNacimiento, edad
        Statement query = (Statement) con.createStatement();
        
        ResultSet rs = query.executeQuery(consulta);
        
        while (rs.next()){
            
            Cliente ci = this.getCliente();            

            this.setIdSeguroHogar(rs.getInt(1));
            this.setFechaContrato(rs.getDate(2));
            this.setFechaPago(rs.getDate(3));
            this.setMora(rs.getDouble(4));
            this.vencimiento=rs.getDate(5);
            
            this.valorInmueble = rs.getDouble(6);
            this.valorMuebles = rs.getDouble(7);
            this.numeroPagos = rs.getInt(8);
            this.montoPagoseguro = rs.getDouble(9);
            ci.setIdCliente(rs.getInt(10));

            ci.setDireccion(rs.getString(11));
            ci.setTelefono(rs.getString(12));
            ci.setCelular(rs.getString(13));
            ci.setFechaNacimiento(rs.getDate(14));
            ci.setEdad(rs.getInt(15));
            this.setCliente(ci);            
        }
    }
    public static void desactivarSegurosVencidos() throws SQLException{
        ContratoAuto.desactivarSegurosVencidos();
    }

    public void cancelarPoliza(String razonCancelacion) throws SQLException {
        String consulta = "UPDATE ContratoHogar SET Activo = 0 WHERE idContratoHogar = "+Integer.toString(this.idContratoHogar);
        Connection con = (Connection) Conexion.obtenerConexion();
        Statement st = (Statement) con.createStatement();
        st.executeUpdate(consulta);
        consulta = "INSERT INTO HistorialSeguro (Anotacion, Fecha, Hora, idSeguroHogar, Agente_idAgente, Cliente_idCliente) "
                + "VALUES ("
                + "'"+razonCancelacion+"',"
                + "CURDATE(),"
                + "CURTIME(),"
                + Integer.toString(this.idContratoHogar)+","
                + Integer.toString(AseguradoraView.idEmpleado)+","
                + Integer.toString(this.cliente.getIdCliente())
                + ")";
        st.execute(consulta);
        st.close();
    }

    public static ContratoHogar[] polizasActivasPorCliente(Cliente unCliente) throws SQLException {
        ArrayList<ContratoHogar> lista = new ArrayList<ContratoHogar>();
        Connection con = (Connection) Conexion.obtenerConexion();
        String consulta = "SELECT CH.idContratoHogar, CH.Identificacion,CH.Descripcion "
                + "FROM ContratoHogar AS CH "
                + " INNER JOIN ClienteSeguro as CS on CS.ContratoHogar_idContratoHogar = CH.idContratoHogar "
                + "INNER JOIN Cliente AS Cl on CS.Cliente_idAgente = Cl.idCliente "
                + "WHERE CH.Activo = 1 AND Cl.idCliente = "+Integer.toString(unCliente.getIdCliente());
        
        Statement query = (Statement) con.createStatement();
        
        ResultSet rs = query.executeQuery(consulta);
        
        while (rs.next()){
            ContratoHogar i = new ContratoHogar();          
            i.setIdContratoHogar(rs.getInt(1));
            i.setIdentificacion(rs.getString(2));
            i.setDescripcion(rs.getString(3));
            i.setCliente(unCliente);            
            lista.add(i);
        }      
        ContratoHogar [] ls = new ContratoHogar[lista.size()];
        ls = lista.toArray(ls);
        return ls;
    }
    
    public static ContratoHogar[] polizasNoActivasPorCliente(Cliente unCliente) throws SQLException {
        ArrayList<ContratoHogar> lista = new ArrayList<ContratoHogar>();
        Connection con = (Connection) Conexion.obtenerConexion();
        String consulta = "SELECT CH.idContratoHogar, CH.Identificacion,CH.Descripcion "
                + "FROM ContratoHogar AS CH "
                + " INNER JOIN ClienteSeguro as CS on CS.ContratoHogar_idContratoHogar = CH.idContratoHogar "
                + "INNER JOIN Cliente AS Cl on CS.Cliente_idAgente = Cl.idCliente "
                + "WHERE CH.Activo = 0 AND Cl.idCliente = "+Integer.toString(unCliente.getIdCliente());
        
        Statement query = (Statement) con.createStatement();
        
        ResultSet rs = query.executeQuery(consulta);
        
        while (rs.next()){
            ContratoHogar i = new ContratoHogar();          
            i.setIdContratoHogar(rs.getInt(1));
            i.setIdentificacion(rs.getString(2));
            i.setDescripcion(rs.getString(3));
            i.setCliente(unCliente);            
            lista.add(i);
        }      
        ContratoHogar [] ls = new ContratoHogar[lista.size()];
        ls = lista.toArray(ls);
        return ls;
    }

    public void renovar() {
        
        //
        //FechaContrato, FechaPago, Mora, Descripcion, Vencimiento, 
        //ValorInmueble, ValorMueble, NumeroPagos,MontoPagoSeguro
        try{
            String consulta = "UPDATE ContratoHogar "
                    + "SET Activo = 1, "
                    + "FechaContrato = ?,"
                    + "Descripcion = ?,"
                    + "FechaPago = ?,"
                    + "Mora = ?,"
                    + "ValorInmueble = ?,"
                    + "ValorMueble = ?,"
                    + "Vencimiento = ?,"
                    + "NumeroPagos = ?,"
                    + "MontoPagoSeguro = ? "
                    + "WHERE idContratoHogar = "+Integer.toString(this.idContratoHogar);
            Connection con = (Connection) Conexion.obtenerConexion();
            Statement st = (Statement) con.createStatement();
            PreparedStatement ps = (PreparedStatement) con.prepareStatement(consulta);
            //st.executeUpdate(consulta);
            ///idHistorialSeguro, Anotacion, Fecha, Hora, idSeguroVida, idSeguroHogar, idSeguroAuto, Agente_idAgente, Cliente_idCliente
            ps.setDate(1, this.fechaContrato);
            ps.setString(2, this.Descripcion);
            ps.setDate(3, this.fechaPago);
            ps.setDouble(4, Mora);
            ps.setDouble(5, valorInmueble);
            ps.setDouble(6, valorMuebles);
            ps.setDate(7, vencimiento);
            ps.setInt(8, numeroPagos);
            ps.setDouble(9, montoPagoseguro);

            boolean execute = ps.execute();

            consulta = "INSERT INTO HistorialSeguro (Anotacion, Fecha, Hora, idSeguroHogar, Agente_idAgente, Cliente_idCliente) "
                    + "VALUES ("
                    + "'Póliza de seguro de hogar renovada',"
                    + "CURDATE(),"
                    + "CURTIME(),"
                    + Integer.toString(this.idContratoHogar)+","
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

