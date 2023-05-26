/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author user
 */
public class UserHomescreenController implements Initializable {

    @FXML
    private Text title;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    public void reservarUnaPistaPressed(ActionEvent event)throws Exception{
        FXMLLoader loader= new  FXMLLoader(getClass().getResource("../view/FXMLReservarPista.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Reservar pista");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        title.getScene().getWindow().hide();
    }
    
    @FXML
    public void logoutPressed(ActionEvent event)throws Exception{
        FXMLLoader loader= new  FXMLLoader(getClass().getResource("../view/FXMLPantallaBienvenida.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Bienvenido");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        title.getScene().getWindow().hide();
    }
    
}
