package controller;

import application.JavaFXMLApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Club;
import model.ClubDAOException;
import model.Member;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class WelcomeScreenController implements Initializable {
    // the buttons and textfields
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Text errorMessage;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private Button availableCourtsButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // bind the login button to the enter key
        loginButton.defaultButtonProperty().bind(loginButton.focusedProperty());

        // disable the login button if the username or password fields are empty
        loginButton.disableProperty().bind(usernameField.textProperty().isEmpty().or(passwordField.textProperty().isEmpty()));

        try {
            Club club = Club.getInstance();
            club.setInitialData();
            club.addSimpleData();
        } catch (ClubDAOException | IOException e) {
            throw new RuntimeException(e);
        }


    }

    @FXML
    private void handleLoginButtonAction() throws ClubDAOException, IOException {
        Club club = Club.getInstance();

        String username = usernameField.getText();
        String password = passwordField.getText();
        // check if the username and password are correct
        if (club.existsLogin(username)) {
            // go to the main screen
            Member member = club.getMemberByCredentials(username, password);
            if (member != null) {
                // go to the main screen
                FXMLLoader myFXMLLoader = new FXMLLoader(getClass().getResource("/view/FXMLUserHomescreen.fxml"));
                Parent root = myFXMLLoader.load();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Person detailed view");
                stage.initModality(Modality.APPLICATION_MODAL);
                //the main window is not active
                stage.show();
            } else {
                // show an error message
                errorMessage.setText("Password incorrect");
            }

        } else {
            // show an error message
            errorMessage.setText("Username does not exist");
        }
    }



    @FXML
    private void handleRegisterButtonAction() throws IOException {
        // go to the register screen
        JavaFXMLApplication.setRoot("register");

    }

    @FXML
    private void handleAvailableCourtsButtonAction() {
        // go to the available courts screen

    }



}
