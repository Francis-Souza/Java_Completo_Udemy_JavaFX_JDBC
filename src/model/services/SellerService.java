package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {

	private SellerDao daoSeller = DaoFactory.createSellerDao();

	public List<Seller> findAll() {
		return daoSeller.findAll();
	}

	public void saveOrUpdateSeller(Seller obj) {

		if (obj.getId() == null) {
			daoSeller.insert(obj);
		} else {
			daoSeller.update(obj);
		}
	}

	public void removeSeller(Seller obj) {
		daoSeller.deleteById(obj.getId());
	}

}
