package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

public class DepartmentFormController implements Initializable {
	
	private Department entity; 	
	
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
	public void onBtnSaveAction() {		
		System.out.println("Save");		
	}
	
	@FXML
	public void onBtnCancelAction() {
		System.out.println("Cancel");		
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
