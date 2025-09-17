package ar.edu.utn.frc.backend.dominio.repositorio;

import java.util.List;
import java.util.Optional;

import ar.edu.utn.frc.backend.dominio.modelo.Marca;

public interface MarcaRepository {
	List<Marca> getAll();

	Optional<Marca> get(Integer id);

	//Marca save(Marca marca);

	//void delete(Marca marca);
}
