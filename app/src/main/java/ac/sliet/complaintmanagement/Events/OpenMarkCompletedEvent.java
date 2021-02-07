package ac.sliet.complaintmanagement.Events;

public class OpenMarkCompletedEvent {
    boolean openMarkCompleted;

    public OpenMarkCompletedEvent(boolean openMarkCompleted) {
        this.openMarkCompleted = openMarkCompleted;
    }

    public boolean isOpenMarkCompleted() {
        return openMarkCompleted;
    }

    public void setOpenMarkCompleted(boolean openMarkCompleted) {
        this.openMarkCompleted = openMarkCompleted;
    }
}
