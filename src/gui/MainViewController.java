package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable {
	
	@FXML
	private MenuItem menuItemSeller;
	@FXML
	private MenuItem menuItemDepartment;
	@FXML
	private MenuItem menuItemCategoryCar;
	@FXML
	private MenuItem menuItemCar;
	@FXML
	private MenuItem menuItemAbout;
	

	@FXML
	public void  onMenuItemDepartmentAction(){
		System.out.println("Teste menu department");		
	};	

	@FXML
	public void  onMenuItemSellerAction(){
		System.out.println("Teste menu seller");		
	};
	
	@FXML
	public void  onMenuItemCategoryCarAction(){
		System.out.println("Teste menu categoryCar");		
	};	

	@FXML
	public void  onMenuItemCarAction(){
		System.out.println("Teste menu car");		
	};	

	@FXML
	public void  onMenuItemAboutAction(){
		loadView("/gui/util/About.fxml");
	};
	
	
	@Override	
	public void initialize(URL uri, ResourceBundle rb) {
		
	}
	
	private synchronized void  loadView(String absoluteName) {
		
		try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
		VBox newBox = loader.load();
		
		Scene mainScene = Main.getMainScene();
		VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
		
		Node mainMenu = mainVBox.getChildren().get(0);
		mainVBox.getChildren().clear();
		mainVBox.getChildren().add(mainMenu);
		mainVBox.getChildren().addAll(newBox.getChildren());
		
		} catch (IOException e) {
			 Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		
		}
		
	}
}
	 
	