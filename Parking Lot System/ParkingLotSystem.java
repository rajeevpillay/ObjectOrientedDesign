//package Parking Lot System;
import java.util.*;

public class ParkingLotSystem {
    public static void main(String[] args) {
        System.out.println("Hello Parking");
    }
}

// Enums

enum VehicleType {
    CAR,
    TRUCK,
    ELECTRIC,
    VAN,
    MOTORBIKE 
}

enum ParkingSpotType {
    HANDICAPPED,
    ELECTRIC,
    LARGE,
    COMPACT,
    MOTORBIKE
}

enum AccountStatus {
    ACTIVE,
    BLOCKED,
    ARCHIVED,
    BANNED,
    COMPROMISED,
    UNKNOWN
}

// Basic Classes

class Address {
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private String country; 
}

class Person {
    private String name;
    private Address address;
    private String email;
    private String phone;
}

abstract class Account {
    private String userName;
    private String password;
    private AccountStatus status;
    private Person person;

    public abstract boolean resetPassword();
}

class Admin extends Account {
    public boolean addParkingFloor(ParkingFloor floor) {
        return true;
    }

    public boolean addParkingSpot(String floorName, ParkingSpot spot) {
        return true;
    }

    public boolean addParkingDisplayBoard(String floorName, ParkingDisplayBoard displayBoard) {
        return true;
    }

    public boolean addCustomerInfoPanel(String floorName, CustomerInfoPanel infoPanel) {
        return true;
    }

    public boolean addEntrancePanel(EntrancePanel entrancePanel) {
        return true;
    }

    public boolean addExitPanel(ExitPanel exitPanel) {
        return true;
    }

    public boolean resetPassword() {
        return true;
    }
}

class ParkingAttendant extends Account {
    public boolean processTicket(String ticketNumber) {
        return true;
    }

    public boolean resetPassword() {
        return true;
    }
}

// Parking Spot Types

abstract class ParkingSpot {
    private String number;
    private boolean free = true;
    private Vehicle vehicle;
    private final ParkingSpotType type;

    public ParkingSpot(ParkingSpotType type) {
        this.type = type;
    }

    public boolean isFree() {
        return free;
    }

    public String getNumber() {
        return number;
    }

    public ParkingSpotType getType() {
        return type;
    }

    public boolean assignVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.free = false;
        return true;
    }

    public boolean removeVehicle() {
        this.vehicle = null;
        this.free = true;
        return true;
    }
}

class HandicappedSpot extends ParkingSpot {
    public HandicappedSpot() {
        super(ParkingSpotType.HANDICAPPED);
    }
}

class CompactSpot extends ParkingSpot {
    public CompactSpot() {
        super(ParkingSpotType.COMPACT);
    }
}

class MotorbikeSpot extends ParkingSpot {
    public MotorbikeSpot() {
        super(ParkingSpotType.MOTORBIKE);
    }
}

class ElectricSpot extends ParkingSpot {
    public ElectricSpot() {
        super(ParkingSpotType.ELECTRIC);
    }
}

class LargeSpot extends ParkingSpot {
    public LargeSpot() {
        super(ParkingSpotType.LARGE);
    }
}

// Vehicle Types

abstract class Vehicle {
    private String licenseNumber;
    private VehicleType type;
    private ParkingTicket ticket;

    public Vehicle(VehicleType type) {
        this.type = type;
    }

    public void assignTicket(ParkingTicket ticket) {
        this.ticket = ticket;
    }

    public VehicleType getType() {
        return type;
    }
}

class Car extends Vehicle {
    public Car() {
        super(VehicleType.CAR);
    }
}

class Van extends Vehicle {
    public Van() {
        super(VehicleType.VAN);
    }
}

class Truck extends Vehicle {
    public Truck() {
        super(VehicleType.TRUCK);
    }
}

class Motorbike extends Vehicle {
    public Motorbike() {
        super(VehicleType.MOTORBIKE);
    }
}

class Electric extends Vehicle {
    public Electric() {
        super(VehicleType.ELECTRIC);
    }
}

// Placeholder for ParkingTicket

class ParkingTicket {
    private String ticketNumber;
    private Date issuedAt;
    private Date paidAt;
    private double amount;
    private boolean active;

    public ParkingTicket() {
        this.ticketNumber = UUID.randomUUID().toString();
        this.issuedAt = new Date();
        this.active = true;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void saveInDB() {
        // Simulate saving in DB
    }
}

// Placeholder for EntrancePanel

class EntrancePanel {
    private String id;

    public ParkingTicket getNewTicket(Vehicle vehicle) throws ParkingFullException {
        return ParkingLot.getInstance().getNewParkingTicket(vehicle);
    }
}

// Placeholder for ExitPanel

class ExitPanel {
    private String id;

    public void scanTicket(ParkingTicket ticket) {
        // Simulate scanning
    }
}

// Placeholder for CustomerInfoPanel

class CustomerInfoPanel {
    private String id;

    public void showAvailableSpots() {
        // Show spot info
    }
}

// Placeholder for Location

class Location {
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}

// Placeholder for ParkingRate

class ParkingRate {
    private double hourlyRate;
    private double dailyRate;

    public double calculateFee(long hours) {
        return hours * hourlyRate;
    }
}

// Placeholder for ParkingFullException

class ParkingFullException extends Exception {
    public ParkingFullException() {
        super("Parking lot is full");
    }
}

// Minimal ParkingLot implementation

class ParkingLot {
    private static ParkingLot instance = null;

    private ParkingLot() {}

    public static ParkingLot getInstance() {
        if (instance == null) {
            instance = new ParkingLot();
        }
        return instance;
    }

    public ParkingTicket getNewParkingTicket(Vehicle vehicle) throws ParkingFullException {
        // Always issue a ticket for demo purposes
        ParkingTicket ticket = new ParkingTicket();
        vehicle.assignTicket(ticket);
        return ticket;
    }
}

// Minimal ParkingFloor implementation

class ParkingFloor {
    private String name;

    public ParkingFloor(String name) {
        this.name = name;
    }
} 
// ParkingDisplayBoard implementation

class ParkingDisplayBoard {
    private String id;
    private HandicappedSpot handicappedSpot;
    private CompactSpot compactSpot;
    private LargeSpot largeSpot;
    private MotorbikeSpot motorbikeSpot;
    private ElectricSpot electricSpot;

    public void setHandicappedSpot(HandicappedSpot spot) {
        this.handicappedSpot = spot;
    }

    public void setCompactSpot(CompactSpot spot) {
        this.compactSpot = spot;
    }

    public void setLargeSpot(LargeSpot spot) {
        this.largeSpot = spot;
    }

    public void setMotorbikeSpot(MotorbikeSpot spot) {
        this.motorbikeSpot = spot;
    }

    public void setElectricSpot(ElectricSpot spot) {
        this.electricSpot = spot;
    }

    public void showEmptySpotNumber() {
        System.out.println("Available Spots:");

        System.out.println("Handicapped: " + (handicappedSpot != null && handicappedSpot.isFree()
            ? handicappedSpot.getNumber() : "Full"));

        System.out.println("Compact: " + (compactSpot != null && compactSpot.isFree()
            ? compactSpot.getNumber() : "Full"));

        System.out.println("Large: " + (largeSpot != null && largeSpot.isFree()
            ? largeSpot.getNumber() : "Full"));

        System.out.println("Motorbike: " + (motorbikeSpot != null && motorbikeSpot.isFree()
            ? motorbikeSpot.getNumber() : "Full"));

        System.out.println("Electric: " + (electricSpot != null && electricSpot.isFree()
            ? electricSpot.getNumber() : "Full"));
    }
}