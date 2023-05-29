/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import model.Booking;
import model.Club;
import model.ClubDAOException;
import model.Member;

/**
 * FXML Controller class
 *
 * @author user
 */
public class FXMLVerMisReservasController implements Initializable {


    @FXML
    private Button cancelReservation;

    @FXML
    private TableColumn<Booking, String> courtColumn;

    @FXML
    private ListView<String> listView;

    @FXML
    private TableView<Booking> tableView;

    @FXML
    private TableColumn<Booking, Booking> timeColumn;

    private Member member;

    ObservableList<String> observableList = javafx.collections.FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*
        try {
            test();
        } catch (ClubDAOException | IOException e) {
            throw new RuntimeException(e);
        }

         */

        // the observable list is bound to the list view
        listView.setItems(observableList);


        // retrieve the bookings of the member
        try {
            List<Booking> bookings = Club.getInstance().getUserBookings(member.getNickName());

            // fill the table view with the bookings
            tableView.getItems().addAll(bookings);

            timeColumn.setCellValueFactory(bookingsCellData -> new SimpleObjectProperty<>(bookingsCellData.getValue()));
            courtColumn.setCellValueFactory(courtsCellData -> new SimpleStringProperty(courtsCellData.getValue().getCourt().getName()));


            // set the time column cell factory
            timeColumn.setCellFactory(cellData -> new TimeTableCell());

            // add the court of the bookings to the court column
            // courtColumn.setCellValueFactory(cellData -> cellData.getValue().getCourt().getName());
        } catch (ClubDAOException | IOException e) {
            throw new RuntimeException(e);
        }


        // the listView listens to what object is selected in the tableView
        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        // the observable list is cleared
                        observableList.clear();
                        // the date and time of the booking are retrieved
                        LocalDate d = newValue.getMadeForDay();
                        LocalTime t = newValue.getFromTime();
                        String day = d.getDayOfWeek().toString().toLowerCase();
                        String date = String.format("%02d/%02d", d.getDayOfMonth(), d.getMonthValue());
                        String time = String.format("%02d:%02d", t.getHour(), t.getMinute());

                        // retrieve the date the booking was made
                        LocalDateTime bookingDate = newValue.getBookingDate();
                        String bookingDateString = String.format("%02d/%02d/%04d", bookingDate.getDayOfMonth(), bookingDate.getMonthValue(), bookingDate.getYear());

                        // the observable list is filled with the information of the booking
                        observableList.addAll(
                                "Miembro: " + newValue.getMember().getName() + " " + newValue.getMember().getSurname(),
                                "Username: " + newValue.getMember().getNickName(),
                                "Pista: " + newValue.getCourt().getName(),
                                "Reserva hecha para: " + day + " " + date + " - " + time,
                                "Reserva realizada el: " + bookingDateString
                        );
                    }
                }
        );

        // sort the bookings by date
        tableView.getSortOrder().add(timeColumn);

        // only show the ten most recent bookings
        tableView.setPrefHeight(25 * 10 + 26);

        // disable the cancel reservation button if no booking is selected
        cancelReservation.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
    }

    // method to cancel a reservation
    @FXML
    private void cancelReservation() {
        // get the selected booking
        Booking booking = tableView.getSelectionModel().getSelectedItem();
        // if there is a booking selected and the booking is not made in 24h from now
        if (booking != null && booking.getMadeForDay().isAfter(LocalDate.now().plusDays(1)) ) {
            // cancel the booking
            try {
                Club.getInstance().removeBooking(booking);
            } catch (ClubDAOException | IOException e) {
                throw new RuntimeException(e);
            }
            // remove the booking from the table view
            tableView.getItems().remove(booking);
            // clear the observable list
            observableList.clear();
        } else if (booking != null && booking.getMadeForDay().isBefore(LocalDate.now().plusDays(1))) {
            // if the booking is made in 24h from now, show an alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No se puede cancelar la reserva");
            alert.setContentText("No se puede cancelar la reserva porque se ha hecho en menos de 24h");
            alert.showAndWait();
        }
    }


    // method to initialize the member in the home screen
    public void initializeMember(Member member) {
        this.member = member;
    }

    // for testing purposes
    public void test() throws ClubDAOException, IOException {
        // clear the club
        Club.getInstance().setInitialData();

        // get an avatar image
        Image image = new Image(new FileInputStream("C:\\Users\\ilias\\MASTER\\II-HCI\\project (1)\\ClubDeTenisGreenBall\\src\\resources\\avatars\\default.png"));
        // create a dummy member
        Club.getInstance().registerMember("test", "test", "test", "test", "test", "test", 123, image);
        initializeMember(Club.getInstance().getMemberByCredentials("test", "test"));

        // make some bookings for the member over the course of the past 3 days
        Club.getInstance().registerBooking(LocalDateTime.now().minusDays(5),
                LocalDate.now().minusDays(5),
                LocalTime.of(10, 0,0),
                false,
                Club.getInstance().getCourt(Club.getInstance().getCourts().get(0).getName()),
                Club.getInstance().getMemberByCredentials("test", "test"));

        Club.getInstance().registerBooking(LocalDateTime.now().minusDays(5),
                LocalDate.now().minusDays(3),
                LocalTime.of(10, 0,0),
                false,
                Club.getInstance().getCourt(Club.getInstance().getCourts().get(0).getName()),
                Club.getInstance().getMemberByCredentials("test", "test"));

        // booking not cancellable
        Club.getInstance().registerBooking(LocalDateTime.now().minusDays(5),
                LocalDate.now(),
                LocalTime.now().plusHours(2),
                false,
                Club.getInstance().getCourt(Club.getInstance().getCourts().get(0).getName()),
                Club.getInstance().getMemberByCredentials("test", "test"));

        Club.getInstance().registerBooking(LocalDateTime.now().minusDays(5),
                LocalDate.now().plusDays(2),
                LocalTime.of(11, 0,0),
                false,
                Club.getInstance().getCourt(Club.getInstance().getCourts().get(1).getName()),
                Club.getInstance().getMemberByCredentials("test", "test"));

    }


    static class TimeTableCell extends TableCell<Booking, Booking> {
        @Override
        protected void updateItem(Booking b, boolean empty) {
            super.updateItem(b, empty);
            if (b == null || empty) {
                setText(null);
                setGraphic(null);
            } else {
                LocalDate d = b.getMadeForDay();
                LocalTime t = b.getFromTime();
                String day = d.getDayOfWeek().toString().substring(0, 2);
                String date = String.format("%02d/%02d", d.getDayOfMonth(), d.getMonthValue());
                String time = String.format("%02d:%02d", t.getHour(), t.getMinute());

                setText(day + " " + date + " - " + time);
            }
        }
    }
    
}
