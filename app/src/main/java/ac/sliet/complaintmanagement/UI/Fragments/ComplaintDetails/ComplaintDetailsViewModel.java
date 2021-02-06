package ac.sliet.complaintmanagement.UI.Fragments.ComplaintDetails;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ac.sliet.complaintmanagement.Common.Common;
import ac.sliet.complaintmanagement.Model.ComplaintModel;

public class ComplaintDetailsViewModel extends ViewModel {

    MutableLiveData<ComplaintModel> complaintModel;


    public ComplaintDetailsViewModel() {
        if (complaintModel == null) {
            complaintModel = new MutableLiveData<>();
            if (Common.selectedComplaint != null) {
                setComplaintModel(Common.selectedComplaint);
            }
        }

    }

    public MutableLiveData<ComplaintModel> getComplaintModel() {
        return complaintModel;
    }

    public void setComplaintModel(ComplaintModel complaint) {
        complaintModel.setValue(complaint);
    }
}