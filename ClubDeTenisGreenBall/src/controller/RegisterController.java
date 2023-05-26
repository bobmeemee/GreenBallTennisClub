package controller;

import application.JavaFXMLApplication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import model.Club;
import model.ClubDAOException;
import model.Member;

public class RegisterController implements Initializable {

    @FXML
    private Button cancelButton;

    @FXML
    private Button choosePictureButton;

    @FXML
    private Button confirmButton;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField creditcardField;

    @FXML
    private TextField cscField;

    @FXML
    private Text errorMessage;

    @FXML
    private TextField firstnameField;

    @FXML
    private TextField lastnameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ImageView profilePictureImageView;

    @FXML
    private Button uploadPicureButton;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField telephoneField;

    private Image profilePicture = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // bind the confirm button to the enter key
        confirmButton.defaultButtonProperty().bind(confirmButton.focusedProperty());

        // disable the confirm button if the username, password, password confirmation, firstname or lastname fields are empty
        confirmButton.disableProperty().bind(usernameField.textProperty().isEmpty()
                .or(passwordField.textProperty().isEmpty())
                .or(confirmPasswordField.textProperty().isEmpty())
                .or(firstnameField.textProperty().isEmpty())
                .or(lastnameField.textProperty().isEmpty()));

    }

    @FXML
    private void handleRegisterButtonAction() throws ClubDAOException, IOException {
        Club club = Club.getInstance();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String firstname = firstnameField.getText();
        String lastname = lastnameField.getText();
        String creditcard = creditcardField.getText();
        String csc = cscField.getText();
        String telephone = telephoneField.getText();

        // check if the username exists
        if(club.existsLogin(username)) {
            errorMessage.setText("Username already exists");
            // show the error message
        }else if(!password.equals(confirmPassword)) {
            errorMessage.setText("Passwords do not match");
            // show the error message
        }else if(!creditcard.matches("[0-9]+") || creditcard.length() != 16) {
            errorMessage.setText("Credit card number must be a number");
            // show the error message
        }else if(!csc.matches("[0-9]+") || csc.length() != 3) {
            errorMessage.setText("CSC must be a number with 3 digits");
            // show the error message
        } else {
            club.registerMember(firstname, lastname, telephone, username, password, creditcard, Integer.parseInt(csc), profilePicture);
            // show notification of succesful registration and go to main screen after pressing ok

            // go to the main screen
            JavaFXMLApplication.setRoot("userhome");


        }


    }
    @FXML
    private void handleCancelButtonAction() {
        // go to the welcome screen

    }

    @FXML
    private void handleChoosePictureButtonAction() {
        // choose a picture from the provided ones

    }

    @FXML
    private void handleUploadPictureButtonAction() {
        // choose picture from the computer
    }



}
