package ac.sliet.complaintmanagement.Events;

import com.google.firebase.Timestamp;

public class DateSelectedEvent {
    Timestamp selectedDate;

    public DateSelectedEvent(Timestamp selectedDate) {
        this.selectedDate = selectedDate;
    }

    public Timestamp getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Timestamp selectedDate) {
        this.selectedDate = selectedDate;
    }
}
