import java.util.Date;
import java.util.List;
import java.time.LocalDate;

public class LibraryManagementSystem {
    public static void main(String[] args) {
        System.out.println("Hello Library");
    }
}

enum BookFormat {
    HARDCOVER, PAPERBACK, AUDIO_BOOK, EBOOK, NEWSPAPER, MAGAZINE, JOURNAL
}

enum BookStatus {
    AVAILABLE, RESERVED, LOANED, LOST
}

enum ReservationStatus {
    WAITING, PENDING, CANCELED, COMPLETED, NONE
}

enum AccountStatus {
    ACTIVE, CLOSED, CANCELED, BLACKLISTED, NONE
}

class Address {
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private String country;  // fixed capital "Country"
}

class Person {
    private String name;
    private String address;
    private String phone;
    private String email;
}

class Constants {
    public static final int MAX_BOOKS_ISSUED_TO_USER = 5;
    public static final int MAX_LENDING_DAYS = 10;
}

abstract class Account {
    private String id;
    private String password;
    private Person person;
    private AccountStatus status;

    public boolean resetPassword() {
        // Dummy implementation for now
        return true;
    }

    public String getId() {
        return id;
    }
}

class Librarian extends Account {
    public boolean addBookItem(BookItem bookItem) {
        return true;
    }

    public boolean blockMember(Member member) {
        return true;
    }

    public boolean unblockMember(Member member) {
        return true;
    }
}

class Member extends Account {
    private Date dateOfMembership;
    private int totalBooksCheckedOut;

    public int getTotalBooksCheckedOut() {
        return totalBooksCheckedOut;
    }

    public boolean reserveBookItem(BookItem bookItem) {
        return true;
    }

    private void incrementTotalBooksCheckedOut() {
        totalBooksCheckedOut++;
    }

    private void decrementTotalBooksCheckedOut() {
        totalBooksCheckedOut--;
    }

    public boolean checkoutBookItem(BookItem bookItem) {
        if (this.getTotalBooksCheckedOut() >= Constants.MAX_BOOKS_ISSUED_TO_USER) {
            showError("The user has already checked out the max number of books");
            return false;
        }
        BookReservation bookReservation = BookReservation.fetchReservationDetails(bookItem.getBarcode());
        if (bookReservation != null && !bookReservation.getMemberId().equals(this.getId())) {
            showError("This book has been reserved by another member");
            return false;
        } else if (bookReservation != null) {
            bookReservation.updateStatus(ReservationStatus.COMPLETED);
        }
        if (!bookItem.checkout(this.getId())) {
            return false;
        }
        this.incrementTotalBooksCheckedOut();
        return true;
    }

    private void checkForFine(String bookItemBarcode) {
        BookLending bookLending = BookLending.fetchLendingDetails(bookItemBarcode);
        Date dueDate = bookLending.getDueDate();
        Date today = new Date();
        if (today.compareTo(dueDate) > 0) {
            long diff = today.getTime() - dueDate.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);
            Fine.collectFine(this.getId(), diffDays);
        }
    }

    public void returnBookItem(BookItem bookItem) {
        this.checkForFine(bookItem.getBarcode());
        BookReservation bookReservation = BookReservation.fetchReservationDetails(bookItem.getBarcode());
        if (bookReservation != null) {
            bookItem.updateBookItemStatus(BookStatus.RESERVED);
            bookReservation.sendBookAvailableNotification();
        }
        bookItem.updateBookItemStatus(BookStatus.AVAILABLE);
    }

    public boolean renewBookItem(BookItem bookItem) {
        this.checkForFine(bookItem.getBarcode());
        BookReservation bookReservation = BookReservation.fetchReservationDetails(bookItem.getBarcode());
        if (bookReservation != null && !bookReservation.getMemberId().equals(this.getId())) {
            showError("This book is reserved by another member");
            this.decrementTotalBooksCheckedOut();
            bookItem.updateBookItemStatus(BookStatus.RESERVED);
            bookReservation.sendBookAvailableNotification();
            return false;
        } else if (bookReservation != null) {
            bookReservation.updateStatus(ReservationStatus.COMPLETED);
        }
        BookLending.lendBook(bookItem.getBarcode(), this.getId());
        bookItem.updateDueDate(LocalDate.now().plusDays(Constants.MAX_LENDING_DAYS));
        return true;
    }

    private void showError(String message) {
        System.out.println("Error: " + message);
    }
}

class BookReservation {
    private Date creationDate;
    private ReservationStatus status;
    private String bookItemBarcode;
    private String memberId;

    public static BookReservation fetchReservationDetails(String barcode) {
        return null; // dummy
    }

    public void updateStatus(ReservationStatus status) {
        this.status = status;
    }

    public void sendBookAvailableNotification() {
        // dummy
    }

    public String getMemberId() {
        return memberId;
    }
}

class BookLending {
    private Date creationDate;
    private Date dueDate;
    private Date returnDate;
    private String bookItemBarcode;
    private String memberId;

    public static boolean lendBook(String barcode, String memberId) {
        return true;
    }

    public static BookLending fetchLendingDetails(String barcode) {
        return new BookLending(); // dummy
    }

    public Date getDueDate() {
        return dueDate;
    }
}

class Fine {
    private Date creationDate;
    private double amount;
    private String memberId;

    public static void collectFine(String memberId, long days) {
        System.out.println("Collected fine for member " + memberId + ": " + days * 1.0);
    }
}

abstract class Book {
    private String ISBN;
    private String title;
    private String subject;
    private String publisher;
    private String language;
    private int numberOfPages;
    private List<Author> authors;
}

class BookItem extends Book {
    private String barcode;
    private boolean isReferenceOnly;
    private Date borrowed;
    private Date dueDate;
    private double price;
    private BookFormat format;
    private BookStatus status;
    private Date dateOfPurchase;
    private Date publicationDate;
    private Rack placedAt;

    public boolean checkout(String memberId) {
        if (this.isReferenceOnly) {
            System.out.println("This book is Reference only and can't be issued");
            return false;
        }
        if (!BookLending.lendBook(this.getBarcode(), memberId)) {
            return false;
        }
        this.updateBookItemStatus(BookStatus.LOANED);
        return true;
    }

    public void updateBookItemStatus(BookStatus status) {
        this.status = status;
    }

    public String getBarcode() {
        return barcode;
    }

    public void updateDueDate(LocalDate date) {
        // convert or store appropriately if needed
    }

    public boolean getIsReferenceOnly() {
        return isReferenceOnly;
    }
}

class Rack {
    private int number;
    private String locationIdentifier;
}

class Author {
    private String name;
}