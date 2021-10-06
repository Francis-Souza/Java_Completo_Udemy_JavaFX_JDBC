package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

public class SellerFormController implements Initializable {
	
	private Seller entity; 	
	
	private SellerService service;
	
	public void setService(SellerService service) {
		this.service = service;
	}
	
	private List<DataChangeListener>  dataChangeListeners = new ArrayList<>();
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
		

	public void setSeller(Seller entity) {
		this.entity = entity;
	}
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtNameSeller;
	
	@FXML
	private TextField txtEmailSeller;
	
	@FXML
	private DatePicker dpBirthDate;
	
	@FXML
	private TextField txtbaseSalary;
	
	@FXML
	private Button btnSave;
	
	@FXML
	private Button btnCancel;
	
	@FXML
	private Label lblError;
	
	@FXML
	private Label lblErrorEmail;
	
	@FXML
	private Label lblErrorBaseSalary;
	
	@FXML
	private Label lblErrorBirthDate;

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
			service.saveOrUpdateSeller(entity);
			notifyDataChangeListeners();
			
			Utils.currentStage(event).close();
		} 
		catch(ValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch (DbIntegrityException e) {
			Alerts.showAlert("	Erros saving object", null, e.getMessage(), AlertType.ERROR);
		}

	}
	
	private void notifyDataChangeListeners() {
		for(DataChangeListener listener : dataChangeListeners ) {
			listener.onDataChanged();
		}
		
	}


	private Seller getFormData() {
		
		Seller obj = new Seller();
		
		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if(txtNameSeller.getText() == null || txtNameSeller.getText().trim().equals("")) {
			exception.addError("Name", "Field Name Seller can't be empty ");
		}
		obj.setName(txtNameSeller.getText());

		if (exception.getErrors().size() > 0 ) {
			throw exception; 
		}
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
		Constraints.setTextFieldMaxLength(txtNameSeller, 60);	
		Constraints.setTextFieldDouble(txtbaseSalary);
		Constraints.setTextFieldMaxLength(txtEmailSeller, 60);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
		
	}
	
	public void updateFormData() {

		if (entity == null) {
			throw new IllegalStateException("Entity was nll");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtNameSeller.setText(entity.getName());
		txtEmailSeller.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		txtbaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
		if (entity.getBirthDate() != null) {
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		if (fields.contains("Name")) {
			lblError.setText(errors.get("Name"));
		}

	}

}
