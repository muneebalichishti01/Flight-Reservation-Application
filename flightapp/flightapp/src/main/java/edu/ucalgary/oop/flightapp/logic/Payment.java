package edu.ucalgary.oop.flightapp.logic;

import java.sql.SQLException;

public class Payment {
    // Calculate price
    public static Double calculatePrice(boolean cancellationInsurance,Seat seat) {
        double price;
        if (cancellationInsurance == true) {
            price = seat.getCost() + 50.0;
        }
        price = seat.getCost();
        return price;
    }

    // Process payment method
    public static BookingInfo processPayment(Seat seat, boolean cancellationInsurance, FlightInfo flightInfo, double price) throws SQLException {
        int bookingId = seat.getSeatID() + flightInfo.getFlightId();
        flightInfo.setAvailability(seat.getSeatID());
        BookingInfo temp = new BookingInfo(bookingId, flightInfo, price, seat, cancellationInsurance);
        flightInfo.setAvailability(seat.getSeatID());
        Database.createBooking(temp);
        return temp;
    }
}
