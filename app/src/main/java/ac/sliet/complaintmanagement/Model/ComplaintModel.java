package ac.sliet.complaintmanagement.Model;

import com.google.firebase.Timestamp;

import java.util.List;

public class ComplaintModel {
    String complainantName, complainantAddress, phoneNumber, interComNumber, complainantEmail, complaintCategory, complaintDescription, complainantUid;
    boolean postponed;
    long complaintId;
    int status;
    Timestamp availableOnDate, postponedDate;
    List<ItemModel>itemsReplaced;

    public ComplaintModel() {

    }

    public ComplaintModel(String complainantName, String complainantAddress, String phoneNumber, String interComNumber,
                          String complainantEmail, String complaintCategory, String complaintDescription, String complainantUid, boolean postponed,
                          long complaintId, int status, Timestamp availableOnDate, Timestamp postponedDate, List<ItemModel> itemsReplaced) {
        this.complainantName = complainantName;
        this.complainantAddress = complainantAddress;
        this.phoneNumber = phoneNumber;
        this.interComNumber = interComNumber;
        this.complainantEmail = complainantEmail;
        this.complaintCategory = complaintCategory;
        this.complaintDescription = complaintDescription;
        this.complainantUid = complainantUid;
        this.postponed = postponed;
        this.complaintId = complaintId;
        this.status = status;
        this.availableOnDate = availableOnDate;
        this.postponedDate = postponedDate;
        this.itemsReplaced = itemsReplaced;
    }

    public String getComplainantName() {
        return complainantName;
    }

    public void setComplainantName(String complainantName) {
        this.complainantName = complainantName;
    }

    public String getComplainantAddress() {
        return complainantAddress;
    }

    public void setComplainantAddress(String complainantAddress) {
        this.complainantAddress = complainantAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getInterComNumber() {
        return interComNumber;
    }

    public void setInterComNumber(String interComNumber) {
        this.interComNumber = interComNumber;
    }

    public String getComplainantEmail() {
        return complainantEmail;
    }

    public void setComplainantEmail(String complainantEmail) {
        this.complainantEmail = complainantEmail;
    }

    public String getComplaintCategory() {
        return complaintCategory;
    }

    public void setComplaintCategory(String complaintCategory) {
        this.complaintCategory = complaintCategory;
    }

    public String getComplaintDescription() {
        return complaintDescription;
    }

    public void setComplaintDescription(String complaintDescription) {
        this.complaintDescription = complaintDescription;
    }

    public String getComplainantUid() {
        return complainantUid;
    }

    public void setComplainantUid(String complainantUid) {
        this.complainantUid = complainantUid;
    }

    public boolean isPostponed() {
        return postponed;
    }

    public void setPostponed(boolean postponed) {
        this.postponed = postponed;
    }

    public long getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(long complaintId) {
        this.complaintId = complaintId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getAvailableOnDate() {
        return availableOnDate;
    }

    public void setAvailableOnDate(Timestamp availableOnDate) {
        this.availableOnDate = availableOnDate;
    }

    public Timestamp getPostponedDate() {
        return postponedDate;
    }

    public void setPostponedDate(Timestamp postponedDate) {
        this.postponedDate = postponedDate;
    }

    public List<ItemModel> getItemsReplaced() {
        return itemsReplaced;
    }

    public void setItemsReplaced(List<ItemModel> itemsReplaced) {
        this.itemsReplaced = itemsReplaced;
    }
}
