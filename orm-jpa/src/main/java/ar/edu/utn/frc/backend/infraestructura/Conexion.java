package ar.edu.utn.frc.backend.infraestructura;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Conexion {
	public static final String DB_URL = "jdbc:h2:file:./.h2/testdb";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}