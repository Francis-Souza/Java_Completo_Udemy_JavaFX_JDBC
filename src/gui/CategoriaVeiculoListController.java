package gui;

import java.io.IOException;
import java.net.URL;
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
import model.entities.CategoriaVeiculo;
import model.services.CategoriaVeiculoService;

public class CategoriaVeiculoListController implements Initializable, DataChangeListener {

	private CategoriaVeiculoService serviceCategoriaVeiculo;

	public void setCategoriaVeiculoService(CategoriaVeiculoService serviceCategoriaVeiculo) {
		this.serviceCategoriaVeiculo = serviceCategoriaVeiculo;
	}

	@FXML
	private TableView<CategoriaVeiculo> tableViewCategoriaVeiculo;

	@FXML
	private TableColumn<CategoriaVeiculo, Integer> tableColumnId;

	@FXML
	private TableColumn<CategoriaVeiculo, String> tableColumnNameCategoriaVeiculo;

	@FXML
	private TableColumn<CategoriaVeiculo, CategoriaVeiculo> tableColumnEdit;

	@FXML
	private TableColumn<CategoriaVeiculo, CategoriaVeiculo> tableColumnRemove;

	@FXML
	private Button btNewCategoriaVeiculo;

	private ObservableList<CategoriaVeiculo> obsList;

	@FXML
	public void onBtNewCategoriaVeiculoAction(ActionEvent event) {

		Stage parentStage = Utils.currentStage(event);
		CategoriaVeiculo obj = new CategoriaVeiculo();
		createDialogForm(obj, "/gui/CategoriaVeiculoForm.fxml", parentStage);
	}

	@Override
	public void initialize(URL urls, ResourceBundle rb) {
		inicializeNodes();

	}

	private void inicializeNodes() {

		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id"));
		tableColumnNameCategoriaVeiculo.setCellValueFactory(new PropertyValueFactory<>("nameCategory"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewCategoriaVeiculo.prefHeightProperty().bind(stage.heightProperty());

	}

	public void updateTableView() {

		if (serviceCategoriaVeiculo == null) {
			throw new IllegalStateException("Service was null");
		}

		List<CategoriaVeiculo> list = serviceCategoriaVeiculo.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewCategoriaVeiculo.setItems(obsList);

		initEditButtons();
		initRemoveButtons();

	}

	private void createDialogForm(CategoriaVeiculo obj, String absoluteName, Stage parentStage) {

		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			CategoriaVeiculoFormController controller = loader.getController();
			controller.setCategoryCar(obj);
			controller.updateFormData();
			controller.setService(new CategoriaVeiculoService());
			controller.subscribeDataChangeListener(this);

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter new CategoriaVeiculo");
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
		tableColumnEdit.setCellFactory(param -> new TableCell<CategoriaVeiculo, CategoriaVeiculo>() {
			private final Button button = new Button("Edit");

			@Override
			protected void updateItem(CategoriaVeiculo obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/CategoriaVeiculoForm.fxml", Utils.currentStage(event)));
			}

		});
	}

	private void initRemoveButtons() {
		tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemove.setCellFactory(param -> new TableCell<CategoriaVeiculo, CategoriaVeiculo>() {
			private final Button button = new Button("Remove");

			@Override
			protected void updateItem(CategoriaVeiculo obj, boolean empty) {
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

	private void removeEntity(CategoriaVeiculo obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete ?");

		if (result.get() == ButtonType.OK) {
			if (serviceCategoriaVeiculo == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				serviceCategoriaVeiculo.removeCategoriaVeiculo(obj);
				updateTableView();
			} catch (DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

}
