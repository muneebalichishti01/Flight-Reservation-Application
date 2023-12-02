package edu.ucalgary.oop.flightapp.logic;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
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

    public static void main(String[] args) {
        Database dbInstance = Database.getInstance();
        try {
            User newUser1 = new User("ali", "ali@bigman.com", "888-654-321", true);
            boolean userAdded1 = dbInstance.addUserWithValidation(newUser1);
            
            if (userAdded1) {
                Database.storeUserPassword(newUser1.getUserId(), "password888");
                System.out.println("New user added: " + newUser1);
    
                // Rest of your testing code...
            } else {
                System.out.println("User already exists and was not added: " + newUser1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Database.closeDatabase();
        }
    }
    
//-----------------------------------DATABSE-CONNECTION--------------------------------------//
    // Initialize database connection
    public static void initializeDatabase() {
        String url = "jdbc:mysql://localhost:3306/flightappdatabase";
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
//-----------------------------------------User----------------------------------------------//
    // Method to get User by username
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int userId = resultSet.getInt("userId");
                String email = resultSet.getString("email");
                String phoneNumber = resultSet.getString("phoneNumber");
                boolean hasCancellationInsurance = resultSet.getBoolean("hasCancellationInsurance");
                // Assuming these are the columns in your users table
                return new User(userId, username, email, phoneNumber, hasCancellationInsurance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to get the next User ID
    // public static int getNextUserId() {
    //     String sql = "SELECT MAX(userId) AS maxId FROM users";
    //     try (Statement statement = connection.createStatement();
    //          ResultSet resultSet = statement.executeQuery(sql)) {
    //         if (resultSet.next()) {
    //             return resultSet.getInt("maxId") + 1;
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    //     return 1; // Start from 1 if table is empty
    // }

//---------------------------------------Aircraft--------------------------------------------//
    // Method to add Aircraft
    public static void addAircraft(Aircraft aircraft) {
        String sql = "INSERT INTO aircrafts (aircraftId, name) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, aircraft.getAircraftId());
            statement.setString(2, aircraft.getName());
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
    public static void editAircraft(Aircraft aircraft) {
        String sql = "UPDATE aircrafts SET name = ? WHERE aircraftId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, aircraft.getName());
            statement.setInt(2, aircraft.getAircraftId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    // Method to get all available flights
    public static List<FlightInfo> getAvailableFlights() {
        List<FlightInfo> availableFlights = new ArrayList<>();
        String sql = "SELECT * FROM flightInfo WHERE /* your condition for available flights */";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int flightId = resultSet.getInt("flightId");
                String flightName = resultSet.getString("flightName");
                String destination = resultSet.getString("destination");
                String origin = resultSet.getString("origin");
                String departureDate = resultSet.getString("departureDate");
                // Add other fields as per your FlightInfo class

                FlightInfo flight = new FlightInfo(flightId, flightName, destination, origin, departureDate);
                // Adjust the constructor call as per your FlightInfo class
                availableFlights.add(flight);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception appropriately
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
            statement.setInt(3, seat.getFlightID());
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
        String sql = "INSERT INTO flightAttendants (FlightAttendantId, name) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, attendant.getFlightAttendantId());
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
    public static void editCrew(FlightAttendant attendant) {
        String sql = "UPDATE flightAttendants SET name = ? WHERE FlightAttendantId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, attendant.getName());
            statement.setInt(2, attendant.getFlightAttendantId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }    
//-----------------------------------Flight Attendant----------------------------------------//
//-----------------------------------------User----------------------------------------------//
    // Method to add a new User
    public static void addUser(User newUser) {
        String sql = "INSERT INTO users (username, email, phoneNumber, hasCancellationInsurance) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, newUser.getUsername());
            statement.setString(2, newUser.getEmail());
            statement.setString(3, newUser.getPhoneNumber());
            statement.setBoolean(4, newUser.hasCancellationInsurance());
            statement.executeUpdate();

            // Retrieve the generated userId and set it to newUser
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newUser.setUserId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to store User's password
    public static void storeUserPassword(int userId, String password) {
        String sql = "INSERT INTO userPasswords (userId, passwordHash) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setString(2, password); // Hash the password in production
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }    

    // Method to get User's password hash
    public String getUserPasswordHash(int userId) {
        String sql = "SELECT passwordHash FROM userPasswords WHERE userId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("passwordHash");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if password hash not found or error occurred
    }

    // Method to add a new User with validation
    public boolean addUserWithValidation(User newUser) {
        if (userExists(newUser.getUsername(), newUser.getEmail())) {
            return false; // User already exists, do not add
        }
    
        String sql = "INSERT INTO users (username, email, phoneNumber, hasCancellationInsurance) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, newUser.getUsername());
            statement.setString(2, newUser.getEmail());
            statement.setString(3, newUser.getPhoneNumber());
            statement.setBoolean(4, newUser.hasCancellationInsurance());
            statement.executeUpdate();
    
            // Retrieve the generated userId and set it to newUser
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newUser.setUserId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    // Method to check if a user already exists
    private boolean userExists(String username, String email) {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT COUNT(*) FROM users WHERE username = ? OR email = ?")) {
            statement.setString(1, username);
            statement.setString(2, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // Return true if count is greater than 0
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to update a User
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET email = ?, phoneNumber = ?, hasCancellationInsurance = ? WHERE userId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPhoneNumber());
            statement.setBoolean(3, user.hasCancellationInsurance());
            statement.setInt(4, user.getUserId());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
//-----------------------------------------User----------------------------------------------//
//--------------------------------------Credit Card------------------------------------------//
    // Method to add a new CreditCard
    public static void addCreditCard(CreditCard creditCard) {
        String sql = "INSERT INTO creditCards (userId, cardNumber, expiryDate, cvv) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, creditCard.getUserId());
            statement.setString(2, creditCard.getCardNumber());
            statement.setString(3, creditCard.getExpiryDate());
            statement.setString(4, creditCard.getCvv());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//--------------------------------------Credit Card------------------------------------------//
//--------------------------------------Booking Info-----------------------------------------//
    // Method to create a new booking
    public static void createBooking(BookingInfo bookingInfo) throws SQLException {
        String sql = "INSERT INTO bookingInfo (userId, flightId, ticketPrice, seatType) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, bookingInfo.getUser().getUserId());
            statement.setInt(2, bookingInfo.getFlightInfo().getFlightId());
            statement.setDouble(3, bookingInfo.getTicketPrice());
            statement.setString(4, bookingInfo.getSeatType());
            statement.executeUpdate();

            // Retrieve the generated bookingId
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    bookingInfo.setBookingId(resultSet.getInt(1));
                }
            }
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
        
        String sql = "SELECT flightAttendants.name " + 
                     "FROM flightAttendants " + 
                     "JOIN flightinfo ON flightAttendants.name = flightinfo.name " +
                     "WHERE flightinfo.flightId = ?";

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

    public static void updateCompanionTicketDatabase(LocalDate date, int userId) {
        String sql = "UPDATE registeredUsers SET lastCompanionTicketSetDate = ? WHERE userId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            Date sqlDate = java.sql.Date.valueOf(date);;
            statement.setDate(1, (java.sql.Date) sqlDate);
            statement.setInt(2, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static LocalDate getLastCompanionTicketDate(int userId) {
        String sql = "SELECT lastCompanionTicketSetDate FROM registeredUsers WHERE userId = ?";
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
        
        String sql = "SELECT users.username" +
                     "FROM users" +
                     "JOIN flightinfo ON users.username = flightinfo.name " +
                     "WHERE flightinfo.flightId = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    passengerList.add(name);
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

    public static ArrayList<FlightInfo> getAllFlights() {
        ArrayList<FlightInfo> flightList = new ArrayList<>();

        String sql = "SELECT * FROM flightInfo";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int flightId = resultSet.getInt("flightId");
                String flightName = resultSet.getString("flightName");
                String destination = resultSet.getString("destination");
                String origin = resultSet.getString("origin");
                String departureDate = resultSet.getString("departureDate");

                FlightInfo flight = new FlightInfo(flightId, flightName, destination, origin, departureDate);
                
                flightList.add(flight);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flightList;
    }


}

   

   