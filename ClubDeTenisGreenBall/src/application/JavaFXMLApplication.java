/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;


public class JavaFXMLApplication extends Application {

    private static Scene scene;
    private static HashMap<String, Parent> roots = new HashMap<>();

    public static void setRoot(Parent root) {
        scene.setRoot(root);
    }

    public static void setRoot(String key) {
        Parent root = roots.get(key);
        if(root != null) {
            scene.setRoot(root);
        } else {
            System.err.printf("Root %s not found", key);
        }
    }

    protected static Parent getRoot(String key) {
        Parent root = roots.get(key);
        if(root != null) {
            scene.setRoot(root);
        } else {
            System.err.printf("Root %s not found", key);
        }
        return root;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root;
        FXMLLoader loader;
        // welcome screen
        loader = new FXMLLoader(getClass().getResource("../view/FXMLWelcomeScreen.fxml"));
        root = loader.load();
        roots.put("welcome", root);
        // register screen
        loader = new FXMLLoader(getClass().getResource("../view/FXMLRegisterScreen.fxml"));
        root = loader.load();
        roots.put("register", root);
        // user home screen
        loader = new FXMLLoader(getClass().getResource("../view/FXMLUserHomescreen.fxml"));
        root = loader.load();
        roots.put("userhome", root);

        scene = new Scene(roots.get("welcome"));
        stage.setScene(scene);
        stage.setWidth(600);

        stage.setTitle("Welcome");
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        
    }


    
}
