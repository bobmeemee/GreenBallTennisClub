/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.converter.LocalDateStringConverter;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * FXML Controller class
 *
 * @author user
 */
public class FXMLReservarpistaController implements Initializable {

    /**
     * Initializes the controller class.
     */

    @FXML
    private ComboBox<String> courtComboBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ToggleButton toggle10;

    @FXML
    private ToggleButton toggle11;

    @FXML
    private ToggleButton toggle12;

    @FXML
    private ToggleButton toggle13;

    @FXML
    private ToggleButton toggle14;

    @FXML
    private ToggleButton toggle15;

    @FXML
    private ToggleButton toggle16;

    @FXML
    private ToggleButton toggle17;

    @FXML
    private ToggleButton toggle18;

    @FXML
    private ToggleButton toggle19;

    @FXML
    private ToggleButton toggle20;

    @FXML
    private ToggleButton toggle21;

    @FXML
    private ToggleButton toggle9;

    @FXML
    private Button reserveButton;

    private Member member;
    
    @Override
    public void initialize(URL url, ResourceBundle rb){   

        LocalDate today = LocalDate.now();

        datePicker.setValue(today);
        datePicker.setShowWeekNumbers(true);
        datePicker.setConverter(new LocalDateStringConverter());
        datePicker.setPromptText("dd-MM-yyyy");

        // fill the combo box with the courts
        try {
            List<Court> courts = Club.getInstance().getCourts();
            List<String> courtNames = new ArrayList<>();
            for (Court court : courts) {
                courtNames.add(court.getName());
            }
            courtComboBox.getItems().addAll(courtNames);
        } catch (ClubDAOException | IOException e) {
            throw new RuntimeException(e);
        }

        // set the toggle buttons to unselected
        List<ToggleButton> toggleButtons = Arrays.asList(toggle9, toggle10, toggle11, toggle12, toggle13, toggle14, toggle15, toggle16, toggle17, toggle18, toggle19, toggle20, toggle21);
        for (ToggleButton toggleButton : toggleButtons) {
            toggleButton.setSelected(false);
        }

        // set the reservar button to disabled if no court toggle or court is selected
        reserveButton.disableProperty().bind(courtComboBox.getSelectionModel().selectedItemProperty().isNull().or(
                datePicker.valueProperty().isNull()));


        // when the calendar is changed, set the toggle buttons to unselected
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            for (ToggleButton toggleButton : toggleButtons) {
                toggleButton.setSelected(false);
            }
        });

        // when the calender is changed, get the bookings for the selected date and court and set the toggle buttons to disabled if the time is already booked
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                List<Booking> bookings = Club.getInstance().getCourtBookings(courtComboBox.getSelectionModel().getSelectedItem(), datePicker.getValue());
                for (Booking booking : bookings) {
                    int hour = booking.getBookingDate().getHour();
                    ToggleButton toggleButton = (ToggleButton) datePicker.getScene().lookup("#toggle" + (hour + 9));
                    toggleButton.setDisable(true);
                }
            } catch (ClubDAOException | IOException e) {
                throw new RuntimeException(e);
            }
        });

        courtComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                List<Booking> bookings = Club.getInstance().getCourtBookings(courtComboBox.getSelectionModel().getSelectedItem(), datePicker.getValue());
                for (Booking booking : bookings) {
                    int hour = booking.getBookingDate().getHour();
                    ToggleButton toggleButton = (ToggleButton) datePicker.getScene().lookup("#toggle" + (hour + 9));
                    toggleButton.setDisable(true);
                }
            } catch (ClubDAOException | IOException e) {
                throw new RuntimeException(e);
            }
        });


        // set the reserve button to disabled if no toggle button is selected
        reserveButton.disableProperty().bind(toggle9.selectedProperty().not().and(
                toggle10.selectedProperty().not()).and(
                toggle11.selectedProperty().not()).and(
                toggle12.selectedProperty().not()).and(
                toggle13.selectedProperty().not()).and(
                toggle14.selectedProperty().not()).and(
                toggle15.selectedProperty().not()).and(
                toggle16.selectedProperty().not()).and(
                toggle17.selectedProperty().not()).and(
                toggle18.selectedProperty().not()).and(
                toggle19.selectedProperty().not()).and(
                toggle20.selectedProperty().not()).and(
                toggle21.selectedProperty().not()));

        try {
            testMember();
        } catch (ClubDAOException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void setMember(Member member) {
        this.member = member;
    }

    public void testMember() throws ClubDAOException, IOException {
        List<Member> members = new ArrayList<>();
        members = Club.getInstance().getMembers();
        this.member = members.get(0);
        System.out.println(member.getNickName());
    }

    @FXML
    private void clickReservar(ActionEvent event) throws ClubDAOException, IOException {
        // get the selected court
        String court = courtComboBox.getSelectionModel().getSelectedItem();
        Court reservedCourt = Club.getInstance().getCourt(court);
        // get the selected date
        LocalDate bookedDate = datePicker.getValue();
        // get the current time
        LocalDateTime bookingDate = LocalDateTime.now();
        // get if the court is payed by credit card
        boolean isPaid = Club.getInstance().hasCreditCard(member.getNickName());
        // get the selected toggle buttons out of the 12 toggle buttons
        List<LocalTime> selectedTime = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            ToggleButton toggleButton = (ToggleButton) datePicker.getScene().lookup("#toggle" + (i + 9));
            if (toggleButton.isSelected()) {
                // cast integer i to localdatetime
                selectedTime.add(LocalTime.of(i + 9, 0, 0));
                System.out.println("Selected time: " + selectedTime);
            }
        }


        // check if not two hours consecutevly booked
        LocalTime previousTime = null;
        LocalTime previousPreviousTime;
        for (LocalTime time : selectedTime) {
            previousPreviousTime = previousTime;
            previousTime = time;
            if (previousTime.plusHours(1) == time
                    && Objects.requireNonNull(previousPreviousTime).plusHours(2) == time) {
                // show a notification that you can only book two hours at a time
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Three hours consecutively booked");
                alert.setHeaderText("Three consecutive hours booked");
                alert.setContentText("You can only book two hours at a time");
                alert.showAndWait();

            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Confirm your booking");
                alert.setContentText("Do you want to confirm your booking at " + time + " on " + bookedDate +
                        " on court " + court + " ?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK){
                    System.out.println("OK");
                    System.out.println("Booked time: " + time);
                    LocalTime bookingHour = LocalTime.of(time.getHour(), 0);
                    System.out.println("Booking hour: " + bookingHour);
                    Club.getInstance().registerBooking(bookingDate, bookedDate, time, isPaid, reservedCourt, member);
                } else {
                    System.out.println("CANCEL");
                }
            }


        }

    }


    @FXML
    private void clickBorrar(ActionEvent event) {
    }

    @FXML
    private void clickRaton(ActionEvent event) {
    }
    
}
