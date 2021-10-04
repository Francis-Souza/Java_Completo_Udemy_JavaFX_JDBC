package model.dao;

import java.util.List;

import model.entities.CategoriaVeiculo;

public interface CategoriaVeiculoDao {
	
	void insert (CategoriaVeiculo categoriaVeiculo);
	void update (CategoriaVeiculo categoriaVeiculo);
	void deleteById(Integer id);
	CategoriaVeiculo findById(Integer id);	
	List<CategoriaVeiculo> findAll();	

}
