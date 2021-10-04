package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable {

	private DepartmentService serviceDepartment;

	public void setDepartmentService(DepartmentService serviceDepartment) {
		this.serviceDepartment = serviceDepartment;
	}

	@FXML
	private TableView<Department> tableViewDepartment;

	@FXML
	private TableColumn<Department, Integer> tableColumnId;

	@FXML
	private TableColumn<Department, String> tableColumnNameDepartment;

	@FXML
	private Button btNewDepartment;

	private ObservableList<Department> obsList;

	@FXML
	public void onBtNewDepartmentAction() {
		System.out.println("New Department");
	}

	@Override
	public void initialize(URL urls, ResourceBundle rb) {
		inicializeNodes();

	}

	private void inicializeNodes() {

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());

	}

	public void updateTableView() {

		if (serviceDepartment == null) {
			throw new IllegalStateException("Service was null");
		}

		List<Department> list = serviceDepartment.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartment.setItems(obsList);

	}

}
