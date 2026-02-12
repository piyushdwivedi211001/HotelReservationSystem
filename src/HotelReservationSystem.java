import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class HotelReservationSystem {

    static ArrayList<Room> rooms = new ArrayList<>();
    static ArrayList<Booking> bookings = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static final String FILE_NAME = "bookings.txt";

    public static void main(String[] args) {

        // Create rooms
        rooms.add(new Room(101, "Standard"));
        rooms.add(new Room(102, "Standard"));
        rooms.add(new Room(201, "Deluxe"));
        rooms.add(new Room(202, "Deluxe"));
        rooms.add(new Room(301, "Suite"));

        loadBookings(); // load from file

        int choice;
        do {
            System.out.println("\n===== Hotel Reservation System =====");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Book Room");
            System.out.println("3. Cancel Booking");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    viewAvailableRooms();
                    break;
                case 2:
                    bookRoom();
                    break;
                case 3:
                    cancelBooking();
                    break;
                case 4:
                    saveBookings();
                    System.out.println("Data saved. Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 4);
    }

    static void viewAvailableRooms() {
        System.out.println("\n--- Available Rooms ---");
        for (Room room : rooms) {
            if (!room.isBooked) {
                System.out.println("Room No: " + room.roomNumber +
                        " | Category: " + room.category);
            }
        }
    }

    static void bookRoom() {
        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        System.out.print("Enter room number: ");
        int roomNo = sc.nextInt();
        sc.nextLine();

        for (Room room : rooms) {
            if (room.roomNumber == roomNo && !room.isBooked) {

                System.out.println("Processing payment...");
                System.out.println("Payment successful!");

                room.isBooked = true;
                bookings.add(new Booking(name, roomNo));

                System.out.println("Room booked successfully!");
                return;
            }
        }

        System.out.println("Room not available!");
    }

    static void cancelBooking() {
        System.out.print("Enter room number to cancel: ");
        int roomNo = sc.nextInt();
        sc.nextLine();

        for (Room room : rooms) {
            if (room.roomNumber == roomNo && room.isBooked) {
                room.isBooked = false;

                bookings.removeIf(b -> b.roomNumber == roomNo);

                System.out.println("Booking cancelled!");
                return;
            }
        }

        System.out.println("No booking found.");
    }

    static void saveBookings() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Booking b : bookings) {
                writer.println(b.customerName + "," + b.roomNumber);
            }
        } catch (IOException e) {
            System.out.println("Error saving file.");
        }
    }

    static void loadBookings() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] data = line.split(",");
                String name = data[0];
                int roomNo = Integer.parseInt(data[1]);

                bookings.add(new Booking(name, roomNo));

                for (Room room : rooms) {
                    if (room.roomNumber == roomNo) {
                        room.isBooked = true;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading file.");
        }
    }
}
