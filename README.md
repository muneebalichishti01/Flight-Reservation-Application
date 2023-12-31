# Flight Reservation Web Application (ENSF480 Final Project) - Group 8

## Introduction
### Learning Curves:
**A Flight Reservation Web Application:** To analyze, design, and develop a web-based system that can be used by different type of users, tourism agents, airline agents, and system admin(s).

## System’s Description

### Product Perspective:
The Flight Reservation Web Application is a comprehensive online platform designed to streamline and enhance the air travel experience for various user roles within a single airline company. The goal of the system is to give travellers, flight crew and system administrators an easy to use and seamless platform for managing and booking flights. The system aims to optimise the reservation process and enhance the overall travel experience for all users involved.

## In Progress:
We currently are using GUI instead of Web-based implementation of front-end. Therefore, the instructions down below may NOT work as expected. Use .jar file to run the program of GUI.

### Using the Program:
Start the front-end:
```terminal
  mvn spring-boot:run
```

1. To run the program, make sure the *keys* are right; changes to the keys can be done in **Database.java** file in the `initializeDatabase()` method
2. Navigate to the directory: `ENSF480_FinalProject_Grp8/flightapp/flightapp/src/main/java`
```terminal
  cd flightapp/flightapp/src/main/java
```

3. Type the following command in terminal to *compile*:
```terminal
  javac -cp .;mysql-connector-java-8.0.23.jar edu/ucalgary/oop/flightapp/logic/Database.java
```
4. Finally, write the following command in terminal to *run the program*:
```terminal
  java -cp .;mysql-connector-java-8.0.23.jar edu.ucalgary.oop.flightapp.logic.Database
```

### Product Features:
•	User Authentication and Authorization
•	Flight Search and Browse
•	Seat Map Visualization
•	Seat Selection
•	Ticket Cancellation Insurance Option
•	Secure Payment Processing
•	E-ticket and Receipt Generation
•	Flight Cancellation
•	Passenger List Viewing for Airline Agents
•	Crew List Viewing for Airline Agents
•	Aircraft List Management for Admins
•	Crew Management for Admins
•	Airport Lounge Access Management for Registered Users
•	Monthly Promotion News for Registered Users
•	Free Companion Ticket Management for Registered Users
•	User Registration and Profile Management
•	Credit Card Application Process
•	Dynamic Pricing Management
•	Promotional Offer Management
•	User History and Preferences Tracking
