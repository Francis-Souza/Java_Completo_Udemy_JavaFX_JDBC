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
import model.entities.CategoriaVeiculo;
import model.entities.Veiculo;
import model.entities.enums.EstadoVeiculoEnum;
import model.exceptions.ValidationException;
import model.services.CategoriaVeiculoService;
import model.services.VeiculoService;

public class VeiculoFormController implements Initializable {
	
	private Veiculo entity; 	
	
	private VeiculoService service;
	private CategoriaVeiculoService categoriaVeiculoService;
	
	public void setServices(VeiculoService service, CategoriaVeiculoService categoriaVeiculoService) {
		this.service = service;
		this.categoriaVeiculoService = categoriaVeiculoService;
	}
	
	private List<DataChangeListener>  dataChangeListeners = new ArrayList<>();
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}		

	public void setVeiculo(Veiculo entity) {
		this.entity = entity;
	}
	
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtNameVeiculo;
	
	@FXML
	private TextField txtColorCar;
	
	@FXML
	private DatePicker dpFabricationDate;
	
	@FXML
	private TextField txtBrand;
	
	@FXML
	private TextField txtPlate;
	
	@FXML
	private TextField txtVersion;
	
	@FXML
	private TextField txtFuel;		
	
	@FXML
	private TextField txtPrice;
	
	@FXML
	private ComboBox<EstadoVeiculoEnum> comboBoxState;
	
	@FXML
	private ComboBox<CategoriaVeiculo> comboBoxCategoriaVeiculo;	
	
	
	@FXML
	private Button btnSave;
	
	@FXML
	private Button btnCancel;
	
	@FXML
	private Label lblError;
	
	@FXML
	private Label lblErrorColor;
	
	@FXML
	private Label lblErrorVersion;
	
	@FXML
	private Label lblErrorfabricationDate;
	
	@FXML
	private Label lblErrorBrand;
	
	@FXML
	private Label lblErrorPlate;
	
	@FXML
	private Label lblErrorFuel;
	
	@FXML
	private Label lblErrorPrice;
	
	
	private ObservableList<CategoriaVeiculo> obsList;

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
			service.saveOrUpdateVeiculo(entity);
			notifyDataChangeListeners();
			
			Utils.currentStage(event).close();
		} 
		catch(ValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch (DbIntegrityException e) {
			Alerts.showAlert("Erros saving object", null, e.getMessage(), AlertType.ERROR);
		}

	}
	
	private void notifyDataChangeListeners() {
		for(DataChangeListener listener : dataChangeListeners ) {
			listener.onDataChanged();
		}
		
	}


	private Veiculo getFormData() {
		
		Veiculo obj = new Veiculo();
		
		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		
		if(txtNameVeiculo.getText() == null || txtNameVeiculo.getText().trim().equals("")) {
			exception.addError("nameCar", " Field can't be empty ");
		}		
		obj.setNameCar(txtNameVeiculo.getText());
		
		
		
		if(txtColorCar.getText() == null || txtColorCar.getText().trim().equals("")) {
			exception.addError("colorCar", " Field can't be empty ");
		}
		obj.setColorCar(txtColorCar.getText());		
		
		
		if(txtVersion.getText() == null || txtVersion.getText().trim().equals("")) {
			exception.addError("versionCar", " Field can't be empty ");
		}
		obj.setVersionCar(txtVersion.getText());
		
		
		if(txtBrand.getText() == null || txtBrand.getText().trim().equals("")) {
			exception.addError("brandCar", " Field can't be empty ");
		}
		obj.setBrandCar(txtBrand.getText());	
		
		
		if (dpFabricationDate.getValue() == null) {
			exception.addError("fabricationDate", " Field can't be empty ");
		} else {
			Instant instant = Instant.from(dpFabricationDate.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setFabricationDate(Date.from(instant));
		}
		
		if(txtPlate.getText() == null || txtPlate.getText().trim().equals("")) {
			exception.addError("plateCar", " Field can't be empty ");
		}
		obj.setPlateCar(txtPlate.getText());	
		
		if(txtFuel.getText() == null || txtFuel.getText().trim().equals("")) {
			exception.addError("fuelCar", " Field can't be empty ");
		}
		obj.setFuelCar(txtFuel.getText());	
		
		
		if(txtPrice.getText() == null || txtPrice.getText().trim().equals("")) {
			exception.addError("price", " Field can't be empty ");
		}
		obj.setPrice(Utils.tryParseToDouble(txtPrice.getText()));			
		
		obj.setEstate(comboBoxState.getValue());
		obj.setCategoriaVeiculo(comboBoxCategoriaVeiculo.getValue());
		
		

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
		Constraints.setTextFieldMaxLength(txtNameVeiculo, 60);
		Constraints.setTextFieldMaxLength(txtColorCar, 40);	
		Constraints.setTextFieldMaxLength(txtVersion, 40);	
		Constraints.setTextFieldMaxLength(txtBrand, 40);	
		Utils.formatDatePicker(dpFabricationDate, "dd/MM/yyyy");
		Constraints.setTextFieldMaxLength(txtPlate, 9);	
		Constraints.setTextFieldMaxLength(txtFuel, 10);		
		Constraints.setTextFieldDouble(txtPrice);
		
		initializeComboBoxStateCar();
		initializeComboBoxCategoriaVeiculo();
		
	}
	
	public void updateFormData() {

		if (entity == null) {
			throw new IllegalStateException("Entity was nll");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtNameVeiculo.setText(entity.getNameCar());
		txtColorCar.setText(entity.getVersionCar());
		txtVersion.setText(entity.getColorCar());
		txtBrand.setText(entity.getBrandCar());
		if (entity.getFabricationDate() != null) {
			dpFabricationDate.setValue(LocalDate.ofInstant(entity.getFabricationDate().toInstant(), ZoneId.systemDefault()));
		}
		txtPlate.setText(entity.getPlateCar());
		txtFuel.setText(entity.getFuelCar());
		
		
		Locale.setDefault(Locale.US);
		txtPrice.setText(String.format("%.2f", entity.getPrice()));
		
		
		if (entity.getStateCar() == null ) {
			comboBoxState.getSelectionModel().selectFirst();
		}
		comboBoxState.setValue(entity.getStateCar());	
		
		
		if (entity.getCategoriaVeiculo() == null ) {
			comboBoxCategoriaVeiculo.getSelectionModel().selectFirst();
		}
		comboBoxCategoriaVeiculo.setValue(entity.getCategoriaVeiculo());	
		
	}
	
	
	public void loadAssociatedObjects() {

		if (categoriaVeiculoService == null) {
			throw new IllegalStateException("Category Car Service was null");
		}

		List<CategoriaVeiculo> list = categoriaVeiculoService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxCategoriaVeiculo.setItems(obsList);
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		//==================Operador Ternário substitui IF ELSE==========================================
	
		lblError.setText((fields.contains("nameCar") ? errors.get("nameCar"): ""));
		lblErrorColor.setText((fields.contains("colorCar") ? errors.get("colorCar"): ""));
		lblErrorVersion.setText((fields.contains("versionCar") ? errors.get("versionCar"): ""));
		lblErrorBrand.setText((fields.contains("brandCar") ? errors.get("brandCar"): ""));
		lblErrorfabricationDate.setText((fields.contains("fabricationDate") ? errors.get("fabricationDate"): ""));
		lblErrorPlate.setText((fields.contains("plateCar") ? errors.get("plateCar"): ""));
		lblErrorFuel.setText((fields.contains("fuelCar") ? errors.get("fuelCar"): ""));
		lblErrorPrice.setText((fields.contains("price") ? errors.get("price"): ""));	
		
		//		if (fields.contains("price")) {
		//			lblErrorBaseSalary.setText(errors.get("price"));
		//		} else {
		//			lblErrorBaseSalary.setText("");
		//		}

	
	}

	private void initializeComboBoxCategoriaVeiculo() {
		Callback<ListView<CategoriaVeiculo>, ListCell<CategoriaVeiculo>> factory = lv -> new ListCell<CategoriaVeiculo>() {
			@Override
			protected void updateItem(CategoriaVeiculo item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNameCategory());
			}
		};
		comboBoxCategoriaVeiculo.setCellFactory(factory);
		comboBoxCategoriaVeiculo.setButtonCell(factory.call(null));
	}	

	
	private void initializeComboBoxStateCar() {		
		comboBoxState.getItems().setAll(EstadoVeiculoEnum.values());		
	}

}
