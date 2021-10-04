package model.dao;

import db.DB;
import model.dao.impl.CategoriaVeiculoDaoJDBC;
import model.dao.impl.DepartmentDaoJDBC;
import model.dao.impl.SellerDaoJDBC;
import model.dao.impl.VeiculoDaoJDBC;

public class DaoFactory {
	
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection());
	}

	public static DepartmentDao createDepartmentDao() {
		return new DepartmentDaoJDBC(DB.getConnection());
	}
	
	public static CategoriaVeiculoDao createCategoriaVeiculoDao() {
		return new CategoriaVeiculoDaoJDBC(DB.getConnection());
	}

	public static VeiculoDao createVeiculoDao() {		
		return new VeiculoDaoJDBC(DB.getConnection());
	}
}
