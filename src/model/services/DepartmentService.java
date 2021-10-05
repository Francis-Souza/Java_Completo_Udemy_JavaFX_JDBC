package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
	
	
	private DepartmentDao  daoDepartment = DaoFactory.createDepartmentDao();
	
	public List<Department> findAll(){		
		return daoDepartment.findAll();
	}

	
	public void saveOrUpdateDepartment(Department obj) {
		if (obj.getId() == null) {
			daoDepartment.insert(obj);
		} else {
			daoDepartment.update(obj);
		}

	}
	
	public void removeDepartment(Department obj) {
		daoDepartment.deleteById(obj.getId());
	}

}
