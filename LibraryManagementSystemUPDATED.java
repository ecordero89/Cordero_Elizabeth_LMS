import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Iterator;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;
import javax.swing.SwingUtilities;


/*
Elizabeth Cordero
Software Development I
202420-CEN3024C-CRN24667
04/07/2024
*/


//This class represents a Book with id, title, author, and checkout status
class Book {
    private int id;
    private String title;
    private String author;
    private boolean checkedOut;
    private LocalDate dueDate;


    //Constructor to initialize a Book object with id, title, and author
    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.checkedOut = false;
        this.dueDate = null;
    }
    public LocalDate getDueDate() {
        return dueDate;
    }
    //Getter method to retrieve the book properties
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }

    public boolean isCheckedOut() {
        return checkedOut;
    }

    public void checkOut(){

        checkedOut = true;
        dueDate =LocalDate.now().plusDays(30); //Selected random due date
    }

    public void checkIn() {

        checkedOut = false;
        dueDate = null;
    }
    //Override toString method, this shows the Book object as a string.
    @Override
    public String toString() {
        return id + "," + title + "," + author+ "," + (checkedOut ? "Checked Out" : "Available");
    }

}

/*
Elizabeth Cordero
Software Development I
202420-CEN3024C-CRN24667
04/07/2024
*/


//Library class to manage a book collection.
class Library {
    private List<Book> books;

    // Constructor to initialize the book list.
    public Library() {
        this.books = new ArrayList<>();

    }

    public List<Book> getAllBooks() {
        return books;
    }

    //Method to add a new book to the library
    public void addBook(int id, String title, String author) {
        //Check if book with "unique" id already exists to avoid duplicates
        if (books.stream().anyMatch(book -> book.getId() == id)) {
            System.out.println("Book with ID " + id + " already exists.");
        } else {
//If the book id is unique, create a new Book object and add it to library collection
            Book newBook = new Book(id, title, author);
            books.add(newBook);
        }
    }

    //Method to remove a book from the library using its title
    public void removeBookByTitle(String title) {
        books.removeIf(book -> book.getTitle().equals(title));
    }

    //Method to display a list of all the books in the library
    public String listAllBooks() {
        StringBuilder result = new StringBuilder();
        for (Book book : books) {
            result.append(book.toString()).append("\n");
        }
        //Include current books in the collection
        result.append("\nCurrent Library:\n");
        for (Book book : books) {
            result.append(book.toString()).append("\n");
        }
        return result.toString();
    }

    //Method to remove books
    public boolean removeBookByBarcode(int barcode) {
        for (Iterator<Book> iterator = books.iterator(); iterator.hasNext(); ) {
            Book book = iterator.next();
            if (book.getId() == barcode) {
                iterator.remove();
                return true; // Book found and removed
            }
        }
        return false; // Book not found
    }


    public void checkOutBookByTitle(String title) {
        for (Book book : books) {
            if (book.getTitle().equals(title) && !book.isCheckedOut()) {
                book.checkOut();
                System.out.println("Book checked out successfully.");
                return;

            }

        }

        System.out.println("Book not available for checkout.");

    }

    public void checkInBookByTitle(String title) {
        for (Book book : books) {
            if (book.getTitle().equals(title) && book.isCheckedOut()) {
                book.checkIn();
                System.out.println("Book checked in successfully.");
                return;
            }
        }
        System.out.println("Book not available for check-in.");

    }


    //Method to load books from a file into the library
    public void loadBooksFromFile(String fileName) {
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String title = parts[1];
                String author = parts[2];
                addBook(id, title, author);
            }
            JOptionPane.showMessageDialog(null, "File successfully entered.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "File not found: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    //If the fileName is the barcode
    //Method to save the current list of books to a file
    public void saveBooksToFile(String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (Book book : books) {
                writer.println(book);
            }
        } catch (IOException e) {
            System.out.println("Error, unable to write to file: " + e.getMessage());
        }
    }

    //Method to get list of books
    public List<Book> getBooks() {
        return books;
    }

}

/*
Elizabeth Cordero
Software Development I
202420-CEN3024C-CRN24667
04/07/2024
*/
public class LibraryManagementSystem {

    public void connectToDatabase() {
        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Establish the connection
            String url = "jdbc:sqlite:C:/Users/AnEmb/IdeaProjects/LibraryManagementSystem/out/Library_Management_System.db";
            Connection connection = DriverManager.getConnection(url);

            // Do something with the connection (e.g., create tables, execute queries)
            // For example:
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS books (id INTEGER PRIMARY KEY, title TEXT, author TEXT, checked_out BOOLEAN, due_date TEXT)");

            // Close the connection when done
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            // Handle any exceptions here
        }
    }

    public static void main(String[] args) {
        // Instantiate the Library class and load books from a file
        Library library = new Library();
        Scanner scanner = new Scanner(System.in);
        library.loadBooksFromFile("books.txt");

        // Establish database connection
        LibraryManagementSystem librarySystem = new LibraryManagementSystem();
        librarySystem.connectToDatabase();

        // Example: Add, remove, and list books.
        library.listAllBooks();

        // Save changes
        library.saveBooksToFile("books.txt");

        SwingUtilities.invokeLater(() -> {
            LibraryManagementSystemGUI gui = new LibraryManagementSystemGUI(library, scanner);
        });


        // Example: Add, remove, and list books.
        while (true) {System.out.println("\nLibrary Management System");
            System.out.println("1. Add a book");
            System.out.println("2. Remove a book by title");
            System.out.println("3. List all books");
            System.out.println("4. Remove a book by barcode");
            System.out.println("5. Check out a book by title");
            System.out.println("6. Check in a book by title");
            System.out.println("7. Exit");

            System.out.println("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter file name: ");
                    String fileName = scanner.nextLine();
                    library.loadBooksFromFile(fileName);
                    System.out.println("Books added from file.");
                    break;
                case 2:
                    System.out.print("Enter title to remove: ");
                    String titleToRemove = scanner.nextLine();
                    library.removeBookByTitle(titleToRemove);
                    System.out.println("Book removed by title. ");
                    System.out.println("Updated database:");
                    library.listAllBooks();
                    break;
                case 3:
                    System.out.println("Printing the database to screen...");
                    library.listAllBooks();
                    break;
                case 4:
                    System.out.print("Enter barcode to remove: ");
                    int barcodeToRemove = scanner.nextInt();
                    library.removeBookByBarcode(barcodeToRemove);
                    System.out.println("Book removed by barcode.");
                    System.out.println("Updated database: ");
                    library.listAllBooks();
                    break;


                case 5:
                    System.out.print("Enter title to check out: ");
                    String titleToCheckOut = scanner.nextLine();
                    library.checkOutBookByTitle(titleToCheckOut);
                    System.out.println("Book checked out.");
                    System.out.println("Updated database: ");
                    library.listAllBooks();
                    break;
                case 6:
                    System.out.print("Enter title to check in: ");
                    String titleToCheckIn = scanner.nextLine();
                    library.checkInBookByTitle(titleToCheckIn);
                    System.out.println("Book checked in.");
                    System.out.println("Updated database: ");
                    library.listAllBooks();
                    break;
                case 7:
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again. ");

            }
        }
    }
}
