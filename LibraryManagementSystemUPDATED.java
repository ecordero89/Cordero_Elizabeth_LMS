import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/* Elizabeth Cordero
 * Software Development I CEN 3024C CRN 24667
 * Objective: Creating a software development plan for a Library Management System using SDLC following the requirments:
 * Each line represents a book, and the id, title and author seperated  by commas.
 * 03/03/2024
 * Professor Walauskis
 */

//Class representing a Book
class Book {
    private int id;
    private String title;
    private String author;
/* Book class that initializes book properties
 * The unique id
 * The author of the book
 * The Title of the book
 * Constructor
 */
    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    /*Getter method to retrieve the book properties
    *
    * Returns the id of the book
     */
    public int getId() {
        return id;
    }
    /*Gets the title of the book.
    *return gets the title of the book
     */
        public String getTitle() {
            return title;
        }
        /*Gets the author of the book
        *
        *Uses return to get the author
         */
            public String getAuthor() {
                return author;
            }
//Override toString method
            @Override
    public String toString() {
                return id + "," + title + "," + author;
            }
    }
    /*The Library class manages a collection of books, allowing the option to
    add or remove books from the listing.
    *
    *It also provides the methods for loading a book from a file and saving it.
     */
    class Library {
        private List<Book> books;
/*Library class
*Initialize the book list
 */
        public Library() {
            this.books = new ArrayList<>();
        }

        /*Method to ass a new book to the library
        *id The unique id of the book
        * title The title of the book
        * author for The author of the book
         */
        public void addBook(int id, String title, String author) {
            //Check if book with "unique" id already exists to avoid duplicates
            if (books.stream().anyMatch(book -> book.getId() == id)) {
                System.out.println("Book with ID " + id + " already exists.");
            }else {
//If the book id is unique, create a new Book object and add it to library collection
                Book newBook = new Book(id, title, author);
                books.add(newBook);
            }
        }
/*Method to remove a book from the library using its id
*id The unique id of the book to be removed.
 */
        public void removeBook(int id) {
            books.removeIf(book -> book.getId() == id);
        }
/*Method to display a list of all the books in the library
*
 */
        public void listAllBooks() {
            for (Book book : books) {
                System.out.println(book);
            }
        }
/*Method to load books from a file into the library
*fileName The name of the file containing book information
 */
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
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + e.getMessage());
            }

        }
/*Method to save the current list of books to a file
*fileName The name of the file to which books should be saved
 */
        public void saveBooksToFile(String fileName) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
                for (Book book : books) {
                    writer.println(book);
                }
            } catch (IOException e) {
                System.out.println("Error writing to file: " + e.getMessage());
            }
        }
    }


/* Elizabeth Cordero
 * Software Development I CEN 3024C CRN 24667
 * 03/03/2024
 * The LibraryManagementSystem class serves as the entry for the application
 * It demos the functions of the Library class. Performs actions like adding,
 * removing and listing books.
 */

public class LibraryManagementSystem {
    public static void main(String[] args) {
        //Instantiate the Library class and load books from a file
        Library library = new Library();
        library.loadBooksFromFile("books.txt");

        //Example: Add, remove and list books.
        library.addBook(4, "To Kill a Mockingbird", "Harper Lee");
        library.addBook(123, "1984", "George Orwell");
        library.addBook(321, "Harry Potter", "J.K. Rowling");
        library.removeBook(4);
        library.listAllBooks();

        //Save changes
        library.saveBooksToFile("books.txt");
    }
}