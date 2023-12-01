package edu.ucalgary.oop.flightapp.logic;

import java.util.ArrayList;
import java.util.Iterator;

public class FlightInfo {
    // Instance variables
    private int flightId;
    private String flightName;
    private String destination;
    private String origin;
    private String departureDate;

    // Added new variables
    private ArrayList<BookingInfo> passengerBookings;                                   // Track passengers for this flight
    private ArrayList<Seat> seat;                                                       // Track seats for this flight
    private ArrayList<FlightAttendant> flightAttendant;                                 // Track flight attendants for this flight
    private static ArrayList<FlightInfo> flightInfoList = new ArrayList<FlightInfo>();  // Track all flights in the system

    // Constructor
    public FlightInfo(int flightId, String flightName, String destination, String origin, String departureDate) {
        this.flightId = flightId;
        this.flightName = flightName;
        this.destination = destination;
        this.origin = origin;
        this.departureDate = departureDate;

        // Initialize new variables
        this.passengerBookings = new ArrayList<>();
        this.seat = new ArrayList<>();
        this.flightAttendant = new ArrayList<>();

        // Initialize ArrayLists
        initializeSeats();
        addFlightInfo(this);
        
    }

    // Method to initialize Seats
    private void initializeSeats() {
        for (int i = 0; i < 10; i++) {
            seat.add(new OrdinarySeat(i, false, flightId, 1));                   // Add ordinary seats
        }
        for (int i = 10; i < 15; i++) {
            seat.add(new BusinessSeat(new OrdinarySeat(i, false, flightId, 2))); // Add business seats
        }
        for (int i = 15; i < 20; i++) {
            seat.add(new ComfortSeat(new OrdinarySeat(i, false, flightId, 3)));  // Add comfort seats
        }
    }

    // Getters and Setters
    public static ArrayList<FlightInfo> getAllFlightInfo() {
        return flightInfoList;
    }

    public static FlightInfo getFlightInfoByFlightId(int flightId) {
        return flightInfoList.get(flightId);
    }

    public int getFlightId() {
        return flightId;
    }
    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public String getFlightName() {
        return flightName;
    }
    public void setFlightName(String flightName) {
        this.flightName = flightName;
    }

    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getOrigin() {
        return origin;
    }
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDepartureDate() {
        return departureDate;
    }
    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    // Getters and setters for the new attributes
    public ArrayList<BookingInfo> getPassengerBookings() {
        return passengerBookings;
    }
    public void setPassengerBookings(ArrayList<BookingInfo> passengerBookings) {
        this.passengerBookings = passengerBookings;
    }

    public ArrayList<Seat> getSeat() {
        return seat;
    }
    public void setSeat(ArrayList<Seat> seat) {
        this.seat = seat;
    }

    public ArrayList<FlightAttendant> getFlightAttendant() {
        return flightAttendant;
    }
    public void setFlightAttendant(ArrayList<FlightAttendant> flightAttendant) {
        this.flightAttendant = flightAttendant;
    }

    // Methods to add or remove a booking to the flight
    public void addBooking(BookingInfo booking) {
        if (booking != null && booking.getFlightInfo().getFlightId() == this.flightId) {
            passengerBookings.add(booking);
        }
    }
    public void removeBooking(BookingInfo booking) {
        passengerBookings.remove(booking);
    }

    // Methods to add or remove a FlightArrendant to the flight
    public void addCrew(FlightAttendant crew) {
        if (crew != null) {
            flightAttendant.add(crew);
        }
    }
    public void removeCrew(FlightAttendant crew) {
        flightAttendant.remove(crew);
    }

    // Method to add FlightInfo to a list of all FlightInfo Objects
    public static void addFlightInfo(FlightInfo flightInfo) {
        if(flightInfoList.isEmpty()) {

        }
        flightInfoList.add(flightInfo.getFlightId(), flightInfo);
    }

    // Method to book or unbook seats
    public void setBooking(int ID) {
        // Getting an iterator for the ArrayList
        Iterator<Seat> iterator = seat.iterator();

        // Iterating through the ArrayList using iterator
        while (iterator.hasNext()) {
            Seat item = iterator.next();
            if(item.getSeatID() == ID) {
                item.setBooked();
            }
        }
    }

    // Override toString method to display flight information
    @Override
    public String toString() {
        return "FlightInfo{" +
                "flightId=" + flightId +
                ", flightName='" + flightName + '\'' +
                ", destination='" + destination + '\'' +
                ", origin='" + origin + '\'' +
                ", departureDate='" + departureDate + '\'' +
                ", passengerBookings=" + passengerBookings +
                '}';
    }
}
