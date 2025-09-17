package ar.edu.utn.frc.backend.infraestructura.repositorio;

import ar.edu.utn.frc.backend.dominio.modelo.Marca;
import ar.edu.utn.frc.backend.dominio.repositorio.MarcaRepository;
import ar.edu.utn.frc.backend.infraestructura.Conexion;

import java.sql.*;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

import static ar.edu.utn.frc.backend.infraestructura.Conexion.DB_URL;

public class H2MarcaRepository implements MarcaRepository {

    @Override
    public Optional<Marca> get(Integer id) {
        String sql = "SELECT ID, NOMBRE FROM MARCA WHERE ID = ?";

        try (Connection connection = DriverManager.getConnection(Conexion.DB_URL, "", "")) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);;

            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    int marcaId = resultSet.getInt("ID");
                    String nombre = resultSet.getString("NOMBRE");

                    Marca marca = new Marca(marcaId, nombre);
                    return Optional.of(marca);
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener marca con ID " + id + ": " + e.getMessage(), e);
        }
    }
    @Override
    public List<Marca> getAll(){
        String sql = "SELECT * FROM MARCA";
        List<Marca> marcas = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(Conexion.DB_URL, "", "")) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery(); {
                while (resultSet.next()) {
                    int marcaId = resultSet.getInt("ID");
                    String nombre = resultSet.getString("NOMBRE");
                    Marca marca = new Marca(marcaId, nombre);
                    marcas.add(marca);
                    
                }
                return marcas;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener las marcas " + e.getMessage(), e);
        }
        
    };
}