package jdbc.vehicle_project;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int choice;

        do {

            System.out.println();
            System.out.println("================================");
            System.out.println("     VEHICLE RENTAL SYSTEM");
            System.out.println("================================");
            System.out.println("1. Admin Login");
            System.out.println("2. Customer Registration");
            System.out.println("3. Customer Login");
            System.out.println("4. Forgot Password");
            System.out.println("5. Exit");
            System.out.println("================================");

            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:

                    System.out.print("Enter Admin Username: ");
                    String username = sc.nextLine();

                    System.out.print("Enter Admin Password: ");
                    String password = sc.nextLine();

                    boolean adminLogin =
                            AdminOperation.adminLogin(username, password);

                    if (adminLogin) {
                        adminMenu(sc);
                    }

                    break;

                case 2:

                    customerRegistration(sc);

                    break;

                case 3:

                    System.out.print("Enter Email: ");
                    String email = sc.nextLine();

                    System.out.print("Enter Password: ");
                    String customerPassword = sc.nextLine();

                    int customerId =
                            CustomerOperation.customerLogin(
                                    email,
                                    customerPassword
                            );

                    if (customerId != 0) {

                        System.out.println("Welcome Customer!");

                        customerMenu(sc, customerId);
                    }

                    break;

                case 4:
                    CustomerOperation.forgotPassword(sc);
                    break;
                    
                case 5:

                    System.out.println("Thank you!");

                    break;

                default:

                    System.out.println("Invalid choice.");
            }

        } while (choice != 5);
        sc.close();
    }

    public static void customerRegistration(Scanner sc) {

        System.out.println();
        System.out.println("===== CUSTOMER REGISTRATION =====");

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Phone: ");
        String phone = sc.nextLine();

        System.out.print("Enter Email: ");
        String email = sc.nextLine();

        System.out.print("Enter License Number: ");
        String licenseNumber = sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        CustomerOperation.registerCustomer(
                name,
                phone,
                email,
                licenseNumber,
                password
        );
    }


    public static void adminMenu(Scanner sc) {

        int choice;

        do {

            System.out.println();
            System.out.println("================================");
            System.out.println("          ADMIN PANEL");
            System.out.println("================================");
            System.out.println("1. Customer Management");
            System.out.println("2. Vehicle Management");
            System.out.println("3. Rental Management");
            System.out.println("4. Payment Management");
            System.out.println("5. Reports");
            System.out.println("6. Logout");
            System.out.println("================================");

            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {

            case 1:

                System.out.println("===== CUSTOMER MANAGEMENT =====");

                System.out.println("1. View All Customers");
                System.out.println("2. Search Customer");
                System.out.println("3. Update Customer Details");
                System.out.println("4. Delete Customer");
                System.out.println("5. Back");

                int customerChoice = sc.nextInt();

                switch (customerChoice) {

                    case 1:
                        CustomerOperation.viewAllCustomers();
                        break;

                    case 2:
                        CustomerOperation.searchCustomer(sc);
                        break;

                    case 3:
                        CustomerOperation.updateCustomer(sc);
                        break;

                    case 4:
                        CustomerOperation.deleteCustomer(sc);
                        break;

                    case 5:
                        System.out.println("Returning to Admin Panel...");
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }

                break;

            case 2:

            	System.out.println("===== VEHICLE MANAGEMENT =====");
            	System.out.println("1. View All Vehicles");
            	System.out.println("2. Add Vehicle");
            	System.out.println("3. Search Vehicle");
            	System.out.println("4. Update Vehicle");
            	System.out.println("5. Delete Vehicle");
            	System.out.println("6. View Available Vehicles");
            	System.out.println("7. View Rented Vehicles");
            	System.out.println("8. Send Vehicle to Maintenance");
            	System.out.println("9. Back");
                int vehicleChoice = sc.nextInt();
                sc.nextLine();

                switch (vehicleChoice) {

                    case 1:
                        VehicleOperation.viewAllVehicles();
                        break;

                    case 2:
                        VehicleOperation.addVehicle(sc);
                        break;

                    case 3:
                        VehicleOperation.searchVehicle(sc);
                        break;

                    case 4:
                        VehicleOperation.updateVehicle(sc);
                        break;

                    case 5:
                        VehicleOperation.deleteVehicle(sc);
                        break;

                    case 6:
                    	  VehicleOperation.viewAvailableVehicles();
                        break;
                        
                    case 7:
                        VehicleOperation.viewRentedVehicles();
                        break;

                    case 8:
                        VehicleOperation.sendVehicleToMaintenance(sc);
                        break;

                    case 9:
                        System.out.println("Returning to Admin Panel.");
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }

                break;
            case 3:

                System.out.println("===== RENTAL MANAGEMENT =====");

                System.out.println("1. View All Rentals");
                System.out.println("2. View Active Rentals");
                System.out.println("3. View Completed Rentals");
                System.out.println("4. View Customer Rental History");
                System.out.println("5. Cancel Rental");
                System.out.println("6. Back");

                int rentalChoice = sc.nextInt();

                switch (rentalChoice) {

                    case 1:
                        RentalOperation.viewAllRentals();
                        break;

                    case 2:
                        RentalOperation.viewActiveRentals();
                        break;

                    case 3:
                        RentalOperation.viewCompletedRentals();
                        break;

                    case 4:
                        RentalOperation.viewCustomerRentalHistory(sc);
                        break;

                    case 5:
                        RentalOperation.cancelRental(sc);
                        break;

                    case 6:
                        System.out.println("Returning to Admin Panel.");
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }

                break;

            case 4:

                int paymentChoice;

                do {

                    System.out.println("===== PAYMENT MANAGEMENT =====");

                    System.out.println("1. View All Payments");
                    System.out.println("2. View Paid Payments");
                    System.out.println("3. View Pending Payments");
                    System.out.println("4. View Payment by Rental ID");
                    System.out.println("5. Back");

                    System.out.print("Enter your choice: ");

                    paymentChoice = sc.nextInt();

                    switch (paymentChoice) {

                        case 1:
                            PaymentOperation.viewAllPayments();
                            break;

                        case 2:
                            PaymentOperation.viewPaidPayments();
                            break;

                        case 3:
                            PaymentOperation.viewPendingPayments();
                            break;

                        case 4:
                            PaymentOperation.viewPaymentByRentalId(sc);
                            break;

                        case 5:
                            System.out.println("Returning to Admin Panel...");
                            break;

                        default:
                            System.out.println("Invalid choice.");
                    }

                } while (paymentChoice != 5);

                break;

            case 5:

                int reportChoice;

                do {

                    System.out.println();
                    System.out.println("========== REPORTS ==========");
                    System.out.println("1. Available Vehicles Report");
                    System.out.println("2. Rented Vehicles Report");
                    System.out.println("3. Customer Rental History");
                    System.out.println("4. Vehicle Rental History");
                    System.out.println("5. Total Revenue");
                    System.out.println("6. Monthly Revenue");
                    System.out.println("7. Pending Payments");
                    System.out.println("8. Most Rented Vehicle");
                    System.out.println("9. Back");
                    System.out.print("Enter your choice: ");

                    reportChoice = sc.nextInt();

                    switch (reportChoice) {

                        case 1:
                        	 VehicleOperation.availableVehiclesReport();
                            break;

                        case 2:
                        	 VehicleOperation.rentedVehiclesReport();
                            break;

                        case 3:
                        	  RentalOperation.viewCustomerRentalHistory(sc);
                            break;

                        case 4:
                        	 RentalOperation.viewVehicleRentalHistory(sc);
                            break;

                        case 5:
                        	 RentalOperation.totalRentalRevenue();
                            break;

                        case 6:
                        	RentalOperation.monthlyRevenue();
                            break;

                        case 7:
                        	PaymentOperation.viewPendingPayments();
                            break;

                        case 8:
                        	RentalOperation.mostRentedVehicle();
                            break;

                        case 9:
                            System.out.println("Returning to Admin Panel...");
                            break;

                        default:
                            System.out.println("Invalid choice.");
                    }

                } while (reportChoice != 9);

                break;

                case 6:
                    System.out.println("Admin logged out.");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 6);
    }

    public static void customerMenu(Scanner sc, int customerId) {

        int choice;

        do {

        	System.out.println("================================");
        	System.out.println("        CUSTOMER PANEL");
        	System.out.println("================================");

        	System.out.println("1. View Available Vehicles");
        	System.out.println("2. Search Vehicle");
        	System.out.println("3. Rent Vehicle");
        	System.out.println("4. Return Vehicle");
        	System.out.println("5. View My Rentals");
        	System.out.println("6. Make Payment");
        	System.out.println("7. View My Payments");
        	System.out.println("8. Logout");

        	System.out.println("================================");
        	System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                	VehicleOperation.viewAvailableVehicles();
                    break;

                case 2:
                	 VehicleOperation.searchVehicle(sc);
                    break;

                case 3:
                	 RentalOperation.rentVehicle(sc, customerId);
                    break;

                case 4:
                	ReturnOperation.returnVehicle(sc, customerId);

                    break;

                case 5:
                	 RentalOperation.viewMyRentals(customerId);
                    break;

                case 6:
                	 PaymentOperation.makePayment(sc, customerId);
                    break;

                case 7:
                	 PaymentOperation.viewMyPayments(customerId);
                    break;
                    
                case 8:
                    System.out.println("Customer logged out.");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 8);
    }
}