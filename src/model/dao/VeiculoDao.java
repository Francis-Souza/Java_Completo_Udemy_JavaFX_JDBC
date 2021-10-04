package model.dao;

import java.util.List;

import model.entities.CategoriaVeiculo;
import model.entities.Veiculo;

public interface VeiculoDao {
	
	void insert (Veiculo veiculo);
	void update (Veiculo veiculo);
	void deleteById(Integer id);
	Veiculo findById(Integer id);
	List<Veiculo> findAll();
	List<Veiculo> findCategoriaVeiculo(CategoriaVeiculo categoriaVeiculo);

}
