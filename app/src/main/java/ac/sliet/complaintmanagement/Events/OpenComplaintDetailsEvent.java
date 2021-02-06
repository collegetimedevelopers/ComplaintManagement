package ac.sliet.complaintmanagement.Events;

public class OpenComplaintDetailsEvent {
    boolean openDetailsFragment;

    public OpenComplaintDetailsEvent(boolean openDetailsFragment) {
        this.openDetailsFragment = openDetailsFragment;
    }

    public boolean isOpenDetailsFragment() {
        return openDetailsFragment;
    }

    public void setOpenDetailsFragment(boolean openDetailsFragment) {
        this.openDetailsFragment = openDetailsFragment;
    }
}
