package students;

import javafx.beans.property.SimpleIntegerProperty;
import mainWindow.HomePageController;

public class StudentHandling extends HomePageController {

    public static class Batch {
        private final SimpleIntegerProperty batchNo;
        private final SimpleIntegerProperty totalSeats;
        private final SimpleIntegerProperty totalStudents;

        public Batch(int batchNo, int totalSeats, int totalStudents) {
            this.batchNo = new SimpleIntegerProperty(batchNo);
            this.totalSeats = new SimpleIntegerProperty(totalSeats);
            this.totalStudents = new SimpleIntegerProperty(totalStudents);
        }

        public int getBatchNo() {
            return batchNo.get();
        }

        public int getTotalSeats() {
            return totalSeats.get();
        }
        public int getTotalStudents() {
            return totalStudents.get();
        }
    }
}
