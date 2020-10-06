package books;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class BookHandling {

    public static class BookSection {
        private final SimpleStringProperty bookType;
        private final SimpleIntegerProperty bookCapacity;
        private final SimpleIntegerProperty totalBooks;
        private final SimpleIntegerProperty availBooks;

        public BookSection(String bookType, int bookCapacity, int totalBooks, int availBooks) {
            this.bookType = new SimpleStringProperty(bookType);
            this.bookCapacity = new SimpleIntegerProperty(bookCapacity);
            this.totalBooks = new SimpleIntegerProperty(totalBooks);
            this.availBooks = new SimpleIntegerProperty(availBooks);
        }

        public String getBookType() {
            return bookType.get();
        }

        public int getBookCapacity() {
            return bookCapacity.get();
        }

        public int getTotalBooks() {
            return totalBooks.get();
        }

        public int getAvailBooks() {
            return availBooks.get();
        }
    }
}
