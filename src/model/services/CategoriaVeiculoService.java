package model.services;

import java.util.List;

import model.dao.CategoriaVeiculoDao;
import model.dao.DaoFactory;
import model.entities.CategoriaVeiculo;

public class CategoriaVeiculoService {
	
	private CategoriaVeiculoDao cateVeiculoDao = DaoFactory.createCategoriaVeiculoDao();
	
	
	public List<CategoriaVeiculo> findAll(){
		return cateVeiculoDao.findAll();
	}; 
	
	
	public void saveOrUpdateCategoryCar(CategoriaVeiculo catVeiculo) {
		if(catVeiculo.getId() == null) {
			cateVeiculoDao.insert(catVeiculo);
		} else {
			cateVeiculoDao.update(catVeiculo);
		}
	}
	
	
	public void removeCategoriaVeiculo(CategoriaVeiculo catVeiculo) {
		cateVeiculoDao.deleteById(catVeiculo.getId());
	}	

}
