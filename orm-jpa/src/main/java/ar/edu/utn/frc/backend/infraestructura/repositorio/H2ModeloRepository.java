package ar.edu.utn.frc.backend.infraestructura.repositorio;

import ar.edu.utn.frc.backend.dominio.modelo.Marca;
import ar.edu.utn.frc.backend.dominio.modelo.Modelo;
import ar.edu.utn.frc.backend.dominio.modelo.TipoAuto;
import ar.edu.utn.frc.backend.dominio.repositorio.ModeloRepository;
import ar.edu.utn.frc.backend.infraestructura.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ar.edu.utn.frc.backend.infraestructura.Conexion.DB_URL;

public class H2ModeloRepository implements ModeloRepository {

    @Override
    public Modelo get(Integer id) {
        String sql = """
            SELECT 
                m.ID as modelo_id,
                m.NOMBRE as modelo_nombre, 
                m.ANIO as modelo_anio,
                marca.ID as marca_id, 
                marca.NOMBRE as marca_nombre
            FROM MODELO m 
            INNER JOIN MARCA marca ON (m.ID_MARCA = marca.ID) 
            WHERE m.ID = ?
        """;

        try (Connection connection = DriverManager.getConnection(Conexion.DB_URL, "", "")) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    // Crear la Marca
                    Marca marca = new Marca(
                            resultSet.getInt("marca_id"),
                            resultSet.getString("marca_nombre")
                    );

                    // Obtener los tipos para este modelo
                    List<TipoAuto> tipos = obtenerTiposPorModelo(connection, id);

                    // Crear el Modelo
                    return (new Modelo(
                            resultSet.getInt("modelo_id"),
                            resultSet.getString("modelo_nombre"),
                            resultSet.getInt("modelo_anio"),
                            marca,
                            tipos
                    ));
                }

                // Si no encontramos el modelo, lanzamos excepción o devolvemos null
                // El test espera un Modelo, no un Optional
                throw new RuntimeException("Modelo con ID " + id + " no encontrado");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener modelo: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene los tipos de auto asociados a un modelo específico
     */
    private List<TipoAuto> obtenerTiposPorModelo(Connection connection, Integer modeloId) throws SQLException {
        String sql = """
            SELECT 
                ta.ID as tipo_id, 
                ta.NOMBRE as tipo_nombre
            FROM TIPO_AUTO ta
            INNER JOIN MODELO_TIPO_AUTO mta ON (ta.ID = mta.ID_TIPO_AUTO)
            WHERE mta.ID_MODELO = ?
            ORDER BY ta.ID
        """;

        List<TipoAuto> tipos = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, modeloId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tipos.add(new TipoAuto(
                            rs.getInt("tipo_id"),
                            rs.getString("tipo_nombre")
                    ));
                }
            }
        }

        return tipos;
    }
}