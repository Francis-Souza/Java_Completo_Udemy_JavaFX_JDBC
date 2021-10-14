package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.VeiculoDao;
import model.entities.Veiculo;

public class VeiculoService {

	private VeiculoDao veiculoDao = DaoFactory.createVeiculoDao();

	public List<Veiculo> findAll() {
		return veiculoDao.findAll();
	}

	public void saveOrUpdateVeiculo(Veiculo veiculo) {

		if (veiculo.getId() == null) {
			veiculoDao.insert(veiculo);
		} else {

			veiculoDao.update(veiculo);
		}

	}

	public void removeCar(Veiculo veiculo) {
		veiculoDao.deleteById(veiculo.getId());
	}

}
