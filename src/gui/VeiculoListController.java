package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Veiculo;
import model.services.CategoriaVeiculoService;
import model.services.VeiculoService;

public class VeiculoListController implements Initializable, DataChangeListener {

	private VeiculoService serviceVeiculo;

	public void setVeiculoService(VeiculoService serviceVeiculo) {
		this.serviceVeiculo = serviceVeiculo;
	}

	@FXML
	private TableView<Veiculo> tableViewVeiculo;

	@FXML
	private TableColumn<Veiculo, Integer> tableColumnId;

	@FXML
	private TableColumn<Veiculo, String> tableColumnNameVeiculo;	
	
	@FXML
	private TableColumn<Veiculo, Integer> tableColumnColorCar;
	
	@FXML
	private TableColumn<Veiculo, Integer> tableColumnVersionCar;
	
	@FXML
	private TableColumn<Veiculo, Integer> tableColumnBrandCar;

	@FXML
	private TableColumn<Veiculo, Date> tableColumnFabricationDate;
	
	@FXML
	private TableColumn<Veiculo, Integer> tableColumnBoardCar;
	
	@FXML
	private TableColumn<Veiculo, Integer> tableColumnFuelCar;

	@FXML
	private TableColumn<Veiculo, Double> tableColumnPrice;	
	
	@FXML
	private TableColumn<Veiculo, Enum> tableColumnStateCar;	
	
	@FXML
	private TableColumn<Veiculo, Veiculo> tableColumnEdit;

	@FXML
	private TableColumn<Veiculo, Veiculo> tableColumnRemove;

	@FXML
	private Button btNewVeiculo;

	private ObservableList<Veiculo> obsList;
	

	@FXML
	public void onBtNewVeiculoAction(ActionEvent event) {

		Stage parentStage = Utils.currentStage(event);
		Veiculo obj = new Veiculo();
		createDialogForm(obj, "/gui/VeiculoForm.fxml", parentStage);		
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		inicializeNodes();
	}

	private void inicializeNodes() {

		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNameVeiculo.setCellValueFactory(new PropertyValueFactory<>("nameCar"));
		tableColumnColorCar.setCellValueFactory(new PropertyValueFactory<>("colorCar"));	
		tableColumnVersionCar.setCellValueFactory(new PropertyValueFactory<>("versionCar"));
		tableColumnBrandCar.setCellValueFactory(new PropertyValueFactory<>("brandCar"));
		tableColumnFabricationDate.setCellValueFactory(new PropertyValueFactory<>("fabricationDate"));
		Utils.formatTableColumnDate(tableColumnFabricationDate, "dd/MM/yyyy");
		tableColumnBoardCar.setCellValueFactory(new PropertyValueFactory<>("plateCar"));
		tableColumnFuelCar.setCellValueFactory(new PropertyValueFactory<>("fuelCar"));		
		tableColumnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
		Utils.formatTableColumnDouble(tableColumnPrice, 2);
		tableColumnStateCar.setCellValueFactory(new PropertyValueFactory<>("stateCar"));		

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewVeiculo.prefHeightProperty().bind(stage.heightProperty());

	}

	public void updateTableView() {

		if (serviceVeiculo == null) {
			throw new IllegalStateException("Service was null");
		}

		List<Veiculo> list = serviceVeiculo.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewVeiculo.setItems(obsList);

		initEditButtons();
		initRemoveButtons();

	}

	private void createDialogForm(Veiculo obj, String absoluteName, Stage parentStage) {

		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			VeiculoFormController controller = loader.getController();
			controller.setVeiculo(obj);
			controller.setServices(new VeiculoService(), new CategoriaVeiculoService());
			controller.loadAssociatedObjects();
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();			

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter new Car");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();			
			

		} catch (IOException e) {
			e.getStackTrace();
			Alerts.showAlert("Io Exception", "Erro loader view ", e.getMessage(), AlertType.ERROR);
		}

	}

	@Override
	public void onDataChanged() {
		updateTableView();

	}

	private void initEditButtons() {
		tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEdit.setCellFactory(param -> new TableCell<Veiculo, Veiculo>() {
			private final Button button = new Button("Edit");

			@Override
			protected void updateItem(Veiculo obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/VeiculoForm.fxml", Utils.currentStage(event)));
			}

		});
	}

	private void initRemoveButtons() {
		tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemove.setCellFactory(param -> new TableCell<Veiculo, Veiculo>() {
			
			private final Button button = new Button("Remove");

			@Override
			protected void updateItem(Veiculo obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}

		});
	}

	private void removeEntity(Veiculo obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete ?");

		if (result.get() == ButtonType.OK) {
			if (serviceVeiculo == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				serviceVeiculo.removeCar(obj);
				updateTableView();
			} catch (DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

}
