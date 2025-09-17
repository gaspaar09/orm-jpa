package ar.edu.utn.frc.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.edu.utn.frc.backend.dominio.modelo.Marca;
import ar.edu.utn.frc.backend.dominio.repositorio.MarcaRepository;
import ar.edu.utn.frc.backend.infraestructura.Conexion;
import ar.edu.utn.frc.backend.infraestructura.repositorio.H2MarcaRepository;

public class MarcaTest {
	private MarcaRepository marcaRepository;
	@BeforeEach
	public void setUp(){
		marcaRepository = new H2MarcaRepository();
	}

	@Test
	public void obtieneLaCantidadMarcasCorrectamente() {
		assertEquals(9, marcaRepository.getAll().size());
	}

	@Test
	public void laMarcaAudiEsCorrecta() {
		final Marca marcaEsperada = new Marca(6, "Audi");
		final Marca marca = marcaRepository
			.getAll()
			.stream()
			.filter(tipo -> tipo.getNombre().equalsIgnoreCase("Audi"))
			.findFirst()
			.orElseThrow();
		assertEquals(marcaEsperada, marca);
	}

	@Test
	public void testConnection() {
    try (Connection conn = DriverManager.getConnection(Conexion.DB_URL, "", "")) {
        System.out.println("Conexión exitosa: " + conn.getMetaData().getURL());
    } catch (SQLException e) {
        System.err.println("Error de conexión: " + e.getMessage());
    }
}

	// @Test
	// public void guardaUnaMarcaCorrectamente() {
	// 	final Marca nuevaMarca = new Marca("Marca Temporal ");

	// 	Marca marca = marcaRepository.save(nuevaMarca);

	// 	Optional<Marca> marcaObtenida = marcaRepository.get(marca.getId());

	// 	assertTrue(marcaObtenida.isPresent());
	// 	assertEquals(marca, marcaObtenida.get());

	// 	// cleanup
	// 	marcaRepository.delete(marca);
	// }

	// @Test
	// public void eliminaUnaMarcaCorrectamente() {
	// 	final int sizeBefore = marcaRepository.getAll().size();
	// 	final Marca aEliminar = new Marca("Marca A Eliminar ");
	// 	marcaRepository.save(aEliminar);

	// 	// ahora eliminar
	// 	marcaRepository.delete(aEliminar);

	// 	final var todas = marcaRepository.getAll();
	// 	assertEquals(sizeBefore, todas.size());
	// 	assertFalse(todas.contains(aEliminar));
	// }

}
