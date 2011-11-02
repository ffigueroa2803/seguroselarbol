package CapaNegocios;

import CapaDatos.Conexion;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class SeguroVida {

    private int idSeguroVida;

    private int tipoSeguro;

    private String descripcion;

    private double prima;

    private double couta;
    
    private int correlativo;
    
    private String serie;
    
    public SeguroVida () {
    }

    public double getCouta () {
        return couta;
    }

    public void setCouta (double val) {
        this.couta = val;
    }

    public String getDescripcion () {
        return descripcion;
    }

    public void setDescripcion (String val) {
        this.descripcion = val;
    }

    public int getIdSeguroVida () {
        return idSeguroVida;
    }

    public void setIdSeguroVida (int val) {
        this.idSeguroVida = val;
    }

    public double getPrima () {
        return prima;
    }

    public void setPrima (double val) {
        this.prima = val;
    }

    public int getTipoSeguro () {
        return tipoSeguro;
    }

    public void setTipoSeguro (int val) {
        this.tipoSeguro = val;
    }
    public void setSerie(String text) {
        this.serie = text;
    }

    public void setCorrelativo(int parseInt) {
        this.correlativo=parseInt;
    }
    public String getSerie(){
        return this.serie;
    }
    public int getCorrelativo(){
        return this.correlativo;
    }
    
    public void insertarEnBaseDeDatos() throws SQLException {
        Connection con = (Connection) Conexion.obtenerConexion();        
        Statement st = (Statement) con.createStatement();
        //idSeguroVida, TipoSeguro, Descripcion, Prima, Serie, Correlativo
        String sentencia = "INSERT INTO SeguroVida (TipoSeguro,Descripcion,Prima,Serie,Correlativo) VALUES ("
                +String.valueOf(this.tipoSeguro+1)+","
                +"'"+this.descripcion+"',"
                +String.valueOf(prima)+","
                +"'"+this.serie+"',"
                +String.valueOf(this.correlativo) +")";
        st.execute("LOCK TABLE SeguroVida WRITE;");
        
        st.execute(sentencia);
        
        st.execute("UNLOCK TABLES;");
        st.close();
    }
    public static  SeguroVida [] consultarListaSegurosVida() throws SQLException{
        ArrayList<SeguroVida> lista = new ArrayList<SeguroVida>();
        
        Connection con = (Connection) Conexion.obtenerConexion();
        Statement st = (Statement) con.createStatement();
        String consulta = "SELECT idSeguroVida,Descripcion FROM SeguroVida";
        
        ResultSet rs = st.executeQuery(consulta);
        
        while (rs.next()){
            SeguroVida i = new SeguroVida();
            i.setIdSeguroVida((Integer)rs.getObject(1));
            i.setDescripcion(rs.getString(2));
            lista.add(i);
        }
        
        SeguroVida []a = new SeguroVida[lista.size()];
        a = lista.toArray(a);
        return a;
    }
}
