# Vehicle Rental Management System

A console-based Vehicle Rental Management System developed using Core Java, JDBC and MySQL.

## Technologies Used

- Java
- JDBC
- MySQL
- SQL
- Eclipse IDE
- MySQL Connector/J

## Features

### Admin
- Admin Login
- Customer Management
- Vehicle Management
- Rental Management
- Payment Management
- Reports

### Customer
- Customer Registration
- Customer Login
- Forgot Password
- View Available Vehicles
- Search Vehicles
- Rent Vehicle
- Return Vehicle
- View Rental History
- Make Payment
- View Payment History

## Database

The application uses MySQL with the following tables:

- ADMIN
- CUSTOMER
- VEHICLE
- RENTAL
- PAYMENT

Database and tables are created using JDBC.

## JDBC Concepts Used

- Connection
- PreparedStatement
- Statement
- ResultSet
- executeQuery()
- executeUpdate()
- JOIN
- Transactions
- Commit and Rollback

## Project Structure

```text
Vehicle_Rental_Management_System
└── src
    └── jdbc.vehicle_project
        ├── Main.java
        ├── DBConnection.java
        ├── DatabaseSetup.java
        ├── AdminOperation.java
        ├── CustomerOperation.java
        ├── VehicleOperation.java
        ├── RentalOperation.java
        ├── ReturnOperation.java
        └── PaymentOperation.java



Project Flow

Customer Registration
→ Customer Login
→ View/Search Vehicle
→ Rent Vehicle
→ Return Vehicle
→ Payment
→ Rental History



Author

Janani Srinivasan




---

# Step 4 — Add `.gitignore`

Create:

```text
.gitignore

# Java
*.class

# Eclipse
.classpath
.project
.settings/

# Build folders
bin/
target/

# Logs
*.log

# IDE files
.metadata/

# OS files
.DS_Store
Thumbs.db
