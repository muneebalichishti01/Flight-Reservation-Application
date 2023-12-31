package edu.ucalgary.oop.flightapp.logic;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;

// public class Database extends PopulatingDatabase {
public class Database {
    private static Database instance;
    private static Connection connection;

    // Private constructor to prevent direct instantiation
    private Database() {
        initializeDatabase();  // Initialize database connection
    }

    // Static method to get the single instance of the class
    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
            connection = Database.getConnection();
        }
        return instance;
    }

    // get connection
    public static Connection getConnection() {
        return connection;
    }
    
//-----------------------------------DATABSE-CONNECTION--------------------------------------//
    // Initialize database connection
    public static void initializeDatabase() {
        String url = "jdbc:mysql://localhost:3306/flightinfo";
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "root");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, properties);
            System.out.println("-----------------------------------");
            System.out.println("Database connection successful!");
            System.out.println("-----------------------------------");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Close database connection
    public static void closeDatabase() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("-----------------------------------");
                System.out.println("Database connection closed.");
                System.out.println("-----------------------------------");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
//-----------------------------------DATABSE-CONNECTION--------------------------------------//

//---------------------------------------Aircraft--------------------------------------------//
    // Method to add Aircraft
    public static void addAircraft(Aircraft aircraft) {
        String sql = "INSERT INTO aircrafts (aircraftId) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, aircraft.getAircraftId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to remove Aircraft
    public static void removeAircraft(int aircraftId) {
        String sql = "DELETE FROM aircrafts WHERE aircraftId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, aircraftId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Method to update Aircraft
    public static void editAircraft(int oldId, int newId) {
        String sql = "UPDATE aircrafts SET aircraftId = ? WHERE aircraftId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, newId);
            statement.setInt(2, oldId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve all Aircrafts
    public static ArrayList<Aircraft> getAllAircrafts() {
        ArrayList<Aircraft> aircraftList = new ArrayList<>();
        String sql = "SELECT * FROM aircrafts";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int aircraftId = resultSet.getInt("aircraftId");
                Aircraft aircraft = new Aircraft(aircraftId);
                aircraftList.add(aircraft);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return aircraftList;
    }
//---------------------------------------Aircraft--------------------------------------------//
//----------------------------------------Flight---------------------------------------------//
    // Method to add Flight
    public static void addFlight(FlightInfo flight) {
        String sql = "INSERT INTO flightInfo (flightId, flightName, destination, origin, departureDate) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, flight.getFlightId());
            statement.setString(2, flight.getFlightName());
            statement.setString(3, flight.getDestination());
            statement.setString(4, flight.getOrigin());
            statement.setString(5, flight.getDepartureDate());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to remove Flight
    public static void removeFlight(int flightId) {
        String sql = "DELETE FROM flightInfo WHERE flightId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, flightId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update Flight
    public static void editFlight(FlightInfo flight) {
        String sql = "UPDATE flightInfo SET flightName = ?, destination = ?, origin = ?, departureDate = ? WHERE flightId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, flight.getFlightName());
            statement.setString(2, flight.getDestination());
            statement.setString(3, flight.getOrigin());
            statement.setString(4, flight.getDepartureDate());
            statement.setInt(5, flight.getFlightId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve all Flights
    public static ArrayList<FlightInfo> getAllFlights() {
        ArrayList<FlightInfo> flightList = new ArrayList<>();
        String sql = "SELECT * FROM flightinfo";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int flightId = resultSet.getInt("flightId");
                String flightName = resultSet.getString("flightName");
                String destination = resultSet.getString("destination");
                String origin = resultSet.getString("origin");
                String departureDate = resultSet.getString("departureDate");
                FlightInfo flight = new FlightInfo(flightId, flightName, destination, origin, departureDate);
                flight.getSeats().addAll(getSeatsForFlight(flightId));
                flightList.add(flight);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flightList;
    }

    // Method to retrieve passengers for a specific flight
    public static ArrayList<String> getPassengersByFlightId(int flightId) {
        ArrayList<String> passengerList = new ArrayList<>();
        String sql = "SELECT name FROM passengers WHERE flightId = ?";

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, flightId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    passengerList.add(resultSet.getString("name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return passengerList;
    }

    // Method to retrieve available flights
    public static ArrayList<String> retrieveAvailableFlights() {
        ArrayList<String> availableFlights = new ArrayList<>();
        String sql = "SELECT flightId, flightName FROM flightInfo";

        Connection connection = getConnection(); // Ensure you have a method to get the DB connection
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int flightId = resultSet.getInt("flightId");
                    String flightName = resultSet.getString("flightName");
                    availableFlights.add("Flight " + flightId + ": " + flightName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return availableFlights;
    }
//----------------------------------------Flight---------------------------------------------//
//-----------------------------------------Seat----------------------------------------------//
    // Method to add Seats
    public static void addSeat(Seat seat) {
        String sql = "INSERT INTO seats (seatId, type, occupancy, flightId) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, seat.getSeatID());
            statement.setInt(2, seat.getType());
            statement.setBoolean(3, seat.getBooked());
            statement.setInt(4, seat.getFlightID());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update Seats
    public static void editSeat(Seat seat) {
        String sql = "UPDATE seats SET occupancy = ? WHERE flightId = ? AND seatId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, seat.getBooked());
            statement.setInt(2, seat.getFlightID());
            statement.setInt(3, seat.getSeatID());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }    
//-----------------------------------------seat----------------------------------------------//
//-----------------------------------Flight Attendant----------------------------------------//
    // Method to add Flight Attendant
    public static void addFlightAttendant(FlightAttendant attendant) {
        String sql = "INSERT INTO flightAttendants (FlightAttendantId, username, name) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, attendant.getFlightAttendantId());
            statement.setString(2, attendant.getUsername());
            statement.setString(2, attendant.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Method to remove Flight Attendant
    public static void removeFlightAttendant(int attendantId) {
        String sql = "DELETE FROM flightAttendants WHERE FlightAttendantId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, attendantId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update Flight Attendant
    public static void editFlightAttendant(int id, String newName, String newUsername) {
        String sql = "UPDATE flightAttendants SET name = ?, username = ? WHERE FlightAttendantId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newName);
            statement.setString(2, newUsername);
            statement.setInt(3, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
//-----------------------------------Flight Attendant----------------------------------------//
//-----------------------------------------User----------------------------------------------//
    // Method to add a new User
    public static void addUser(User newUser) {
        String sql = "INSERT INTO users (username, email, phoneNumber, hasCreditCard, lastCompanionTicketSetDate, companionTicket) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newUser.getUsername());
            statement.setString(2, newUser.getEmail());
            statement.setString(3, newUser.getPhoneNumber());
            statement.setBoolean(4, newUser.getHasCreditCard());
            statement.setDate(5, java.sql.Date.valueOf(newUser.getLastCompanionTicketSetDate()));
            statement.setBoolean(6, newUser.getCompanionTicket());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Method to update a User
    public static void updateUser(User user) {
        String sql = "UPDATE users SET username = ?, email = ?, phoneNumber = ?, hasCreditCard = ?, lastCompanionTicketSetDate = ?, companionTicket = ? WHERE userId = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPhoneNumber());
            statement.setBoolean(4, user.getHasCreditCard());
            statement.setDate(5, java.sql.Date.valueOf(user.getLastCompanionTicketSetDate()));
            statement.setBoolean(6, user.getCompanionTicket());
            statement.setInt(7, user.getUserId()); // Ensure this line is present and correct
    
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
//-----------------------------------------User----------------------------------------------//
//
//--------------------------------------Booking Info-----------------------------------------//
    // Method to create a new booking
    public static void createBooking(BookingInfo bookingInfo) throws SQLException {
        String sql = "INSERT INTO bookingInfo (flightId, ticketPrice, cancellationInsurance) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, bookingInfo.getFlightInfo().getFlightId());
            statement.setDouble(2, bookingInfo.getTicketPrice());
            statement.setBoolean(3, bookingInfo.getCancellationInsurance());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Method to cancel a booking
    public static boolean cancelBooking(int bookingId) throws SQLException {
        String sql = "DELETE FROM bookingInfo WHERE bookingId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookingId);
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        }
    }    

//--------------------------------------Booking Info-----------------------------------------//
//----------------------------------------Payment--------------------------------------------//
    // Method to update payment status
    public static void updatePaymentStatus(int paymentId, String status) {
        String sql = "UPDATE payments SET paymentStatus = ? WHERE paymentId = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setInt(2, paymentId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//----------------------------------------Payment--------------------------------------------//
    // Other methods ...

    public static List<String> browseCrew(int id) {
        List<String> crewList = new ArrayList<>();
        
        String sql = "SELECT passengers.name " + 
                     "FROM passengers " + 
                     "JOIN flightAttendants ON passengers.name = flightAttendants.name " +
                     "WHERE passengers.flightId = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    crewList.add(name);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return crewList;
    }

    // Method to update companion ticket status
    public static void updateCompanionTicketDatabase(LocalDate date, int userId) {
        String sql = "UPDATE users SET lastCompanionTicketSetDate = ? WHERE userId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            Date sqlDate = java.sql.Date.valueOf(date);;
            statement.setDate(1, (java.sql.Date) sqlDate);
            statement.setInt(2, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to get last companion ticket date
    public static LocalDate getLastCompanionTicketDate(int userId) {
        String sql = "SELECT lastCompanionTicketSetDate FROM users WHERE userId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDate("lastCompanionTicketSetDate").toLocalDate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return null; 
    }    
    
    public static ArrayList<String> browsePassenger(int id) {
        ArrayList<String> passengerList = new ArrayList<>();
    
        String sql = "SELECT users.username " +
                     "FROM users " +
                     "JOIN passengers ON users.username = passengers.name " +
                     "WHERE passengers.flightId = ?";
    
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
    
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String username = resultSet.getString("username");
                    passengerList.add(username);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return passengerList;
    }
    

    public static ArrayList<Seat> browseSeats(int id){
        ArrayList<Seat> seats = new ArrayList<>();

        String sql = "SELECT * FROM seats WHERE flightId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int seatId = resultSet.getInt("seatId");
                    int type = resultSet.getInt("type");
                    boolean booked = resultSet.getBoolean("booked");                    
                    if(type == 1){
                        seats.add(new OrdinarySeat(seatId, booked, id, type));
                    } else if(type == 2) {
                        seats.add(new BusinessSeat(new OrdinarySeat(seatId, booked, id, type)));
                    } else if (type == 3) {
                        seats.add(new ComfortSeat(new OrdinarySeat(seatId, booked, id, type)));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    
    
    private static ArrayList<Seat> getSeatsForFlight(int flightId) {
        ArrayList<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM seats WHERE flightId = ?";
    
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, flightId);
    
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int seatId = resultSet.getInt("seatId");
                    int type = resultSet.getInt("type");
                    boolean booked = resultSet.getBoolean("booked");
                    
                    Seat seat = new OrdinarySeat(seatId, booked, flightId, type); // Assuming OrdinarySeat is a concrete implementation of Seat
                    // Decorate the seat based on its type
                    if (type == 2) { // Assuming 2 represents a business seat
                        seat = new BusinessSeat(seat);
                    } else if (type == 3) { // Assuming 3 represents a comfort seat
                        seat = new ComfortSeat(seat);
                    }
                    seats.add(seat);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }
    

}