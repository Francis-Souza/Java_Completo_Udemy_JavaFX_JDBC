package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

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
		System.out.println("Teste menu about");		
	};
	
	
	@Override	
	public void initialize(URL uri, ResourceBundle rb) {
		

		
	}
}
	 
	