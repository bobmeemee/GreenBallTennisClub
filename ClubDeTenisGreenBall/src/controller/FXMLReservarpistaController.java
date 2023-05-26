/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import model.Booking;
import model.Club;
import model.ClubDAOException;

/**
 * FXML Controller class
 *
 * @author user
 */
public class FXMLReservarpistaController implements Initializable {

    /**
     * Initializes the controller class.
     */
    Club club = null;
    
    @FXML
    private DatePicker datePicker;
    
    @Override
    public void initialize(URL url, ResourceBundle rb){   
        
        try {
            club = Club.getInstance();
        } catch (ClubDAOException ex) {
            Logger.getLogger(FXMLReservarpistaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLReservarpistaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int currentWeek = LocalDate.now().get(weekFields.weekOfWeekBasedYear());
        int numDayNow=LocalDate.now().get(weekFields.dayOfWeek());
        
        LocalDate today = LocalDate.now();
        datePicker.setValue(today);
        
        club.getCourts();
        List<Booking> l = club.getForDayBookings(today);    
        for (int i=0;i<l.size();i++){
            Booking res = l.get(i);
        }

    }

    public void initializeClub() throws Exception{        
        club = Club.getInstance();
    }

    @FXML
    private void clickReservar(ActionEvent event) {
    }

    @FXML
    private void clickBorrar(ActionEvent event) {
    }

    @FXML
    private void clickRaton(ActionEvent event) {
    }
    
}
