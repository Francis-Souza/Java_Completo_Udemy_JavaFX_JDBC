package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import gui.util.Alerts;
import gui.util.Constraints;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Person;

public class ViewController implements Initializable {
	@FXML
	private TextField txtNumber_1;

	@FXML
	private TextField txtNumber_2;

	@FXML
	private Button btnSum;

	@FXML
	private Label labelResult;

	@FXML
	private Button btnTeste;
	
	@FXML
	private Button btnAll;

	@FXML
	private ComboBox<Person> cmbPerson;

	private ObservableList<Person> obsList;

//	@FXML
//	public void onBtnTesteAction() {
//		System.out.println("Você clicou no botão.....");
//	}

	@FXML
	public void onBtnTesteAction() {
		Alerts.showAlert("Atenção!", null, "Você clicou no botão", AlertType.INFORMATION);
	}

	@FXML
	public void onbtnSumAction() {

		try {
			Locale.setDefault(Locale.US);
			double number1 = Double.parseDouble(txtNumber_1.getText());
			double number2 = Double.parseDouble(txtNumber_1.getText());

			double sum = number1 + number2;

			labelResult.setText(String.format("%.2f", sum));
		} catch (NumberFormatException e) {
			Alerts.showAlert("Atenção", null, "Campo Inválido", AlertType.ERROR);
		}
	}
	
	
	@FXML
	public void onCmbAction() {
		Person person = cmbPerson.getSelectionModel().getSelectedItem();
		System.out.println(person);
	}
	
	@FXML
	public void onBtnAll() {		
		for(Person person : cmbPerson.getItems()) {
			System.out.println(person);	
		}
		
	}
	
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Constraints.setTextFieldDouble(txtNumber_1);
		Constraints.setTextFieldDouble(txtNumber_2);

		Constraints.setTextFieldMaxLength(txtNumber_1, 12);
		Constraints.setTextFieldMaxLength(txtNumber_2, 12);

		List<Person> list = new ArrayList<>();
		list.add(new Person(1, "Francisco Souza", "franciscosouza@gmail.com"));
		list.add(new Person(2, "Maria Souza", "maria@gmail.com"));
		list.add(new Person(3, "José Souza", "jose@gmail.com"));
		list.add(new Person(4, "Pedro Souza", "pedrp@gmail.com"));

		obsList = FXCollections.observableArrayList(list);
		cmbPerson.setItems(obsList);

		Callback<ListView<Person>, ListCell<Person>> factory = lv -> new ListCell<Person>() {
			@Override
			protected void updateItem(Person item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		cmbPerson.setCellFactory(factory);
		cmbPerson.setButtonCell(factory.call(null));

	}

}
