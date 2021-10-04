package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {
	
	private Department entity; 	
	
	private DepartmentService service;
	
	public void setService(DepartmentService service) {
		this.service = service;
	}
	
	private List<DataChangeListener>  dataChangeListeners = new ArrayList<>();
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
		

	public void setDeparment(Department entity) {
		this.entity = entity;
	}
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtNameDepartment;
	
	@FXML
	private Button btnSave;
	
	@FXML
	private Button btnCancel;
	
	@FXML
	private Label lxlError;

	@FXML
	public void onBtnSaveAction(ActionEvent event) {

		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}

		if (service == null) {
			throw new IllegalStateException("Service was null");
		}

		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			
			Utils.currentStage(event).close();
		} catch (DbException e) {
			Alerts.showAlert("	Erros saving object", null, e.getMessage(), AlertType.ERROR);
		}

	}
	
	private void notifyDataChangeListeners() {
		for(DataChangeListener listener : dataChangeListeners ) {
			listener.onDataChanged();
		}
		
	}


	private Department getFormData() {
		Department obj = new Department();

		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setName(txtNameDepartment.getText());

		return obj;
	}

	@FXML
	public void onBtnCancelAction(ActionEvent event) {
		Utils.currentStage(event).close(); 
	}
	
	@Override	
	public void initialize(URL url, ResourceBundle rb) {
		
		initializeNodes();				
	}
	
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNameDepartment, 30);
	}
	
	public void updateFormData() {
		
		if (entity == null ) {
			throw new IllegalStateException("Entity was nll");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtNameDepartment.setText(entity.getName());
		
	}

}
