package br.com.bitpay.util;
import java.sql.Connection;
import java.sql.SQLException;

import oracle.jdbc.datasource.impl.OracleDataSource;

public class ConnectionFactory {

  
    private static final String StringConexao = "jdbc:oracle:thin:@localhost:1522/FREEPDB1"; 
    private static final String Usuario = "bitpay_schema";
    private static final String Senha = "senha123";

    
    public static Connection getConnection() throws SQLException {
        
        OracleDataSource ods = new OracleDataSource();
        
        ods.setURL(StringConexao); 
        ods.setUser(Usuario);
        ods.setPassword(Senha);
        
        return (Connection) ods.getConnection();
    }
    
}
