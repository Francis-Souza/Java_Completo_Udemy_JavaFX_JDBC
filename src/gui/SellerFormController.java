package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {
	
	private Seller entity; 	
	
	private SellerService service;
	private DepartmentService departmentService;
	
	public void setServices(SellerService service, DepartmentService departmentService) {
		this.service = service;
		this.departmentService = departmentService;
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
	private ComboBox<Department> comboBoxDepartment;
	
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
	
	private ObservableList<Department> obsList;

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
			exception.addError("name", "Field can't be empty ");
		}
		
		obj.setName(txtNameSeller.getText());
		
		if(txtEmailSeller.getText() == null || txtEmailSeller.getText().trim().equals("")) {
			exception.addError("email", "Field can't be empty ");
		}
		obj.setEmail(txtEmailSeller.getText());		
		
		if (dpBirthDate.getValue() == null) {
			exception.addError("birthDate", "Field can't be empty ");
		} else {
			Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setBirthDate(Date.from(instant));
		}
		
		if(txtbaseSalary.getText() == null || txtbaseSalary.getText().trim().equals("")) {
			exception.addError("baseSalary", "Field can't be empty ");
		}
		obj.setBaseSalary(Utils.tryParseToDouble(txtbaseSalary.getText()));		
		
		obj.setDepartment(comboBoxDepartment.getValue());
		
		

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
		
		initializeComboBoxDepartment();
		
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
		
		if (entity.getDepartment() == null ) {
			comboBoxDepartment.getSelectionModel().selectFirst();
		}
		comboBoxDepartment.setValue(entity.getDepartment());		
	}
	
	
	public void loadAssociatedObjects() {

		if (departmentService == null) {
			throw new IllegalStateException("Department Service was null");
		}

		List<Department> list = departmentService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxDepartment.setItems(obsList);
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		//==================Operador Ternário substitui IF ELSE==========================================
	
		lblError.setText((fields.contains("name") ? errors.get("name"): ""));
		lblErrorEmail.setText((fields.contains("email") ? errors.get("baseSalary"): ""));
		lblErrorBaseSalary.setText((fields.contains("baseSalary") ? errors.get("baseSalary"): ""));
		lblErrorBirthDate.setText((fields.contains("birthDate") ? errors.get("birthDate"): ""));
		
		//		if (fields.contains("baseSalary")) {
		//			lblErrorBaseSalary.setText(errors.get("baseSalary"));
		//		} else {
		//			lblErrorBaseSalary.setText("");
		//		}

		
	
	
	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());

			}
		};
		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}

}
