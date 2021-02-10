package ac.sliet.complaintmanagement.UI.Fragments.ComplaintDetails;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import ac.sliet.complaintmanagement.Common.Common;
import ac.sliet.complaintmanagement.Model.ComplaintModel;

public class ComplaintDetailsViewModel extends ViewModel {

    MutableLiveData<ComplaintModel> complaintModel;


    public ComplaintDetailsViewModel() {

        if (complaintModel == null) {

            complaintModel = new MutableLiveData<>();

            if (Common.isAppOpenedFromNotification) {

                Common.isAppOpenedFromNotification = false; // disabling because when next time user clicks on a complaint then the vakues should be set locally

                FirebaseFirestore.getInstance().collection(Common.COMPLAINT_COLLECTION_REFERENCE)
                        .document(Common.complaintIdFromNotification)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                Common.selectedComplaint=documentSnapshot.toObject(ComplaintModel.class);
                                setComplaintModel(Common.selectedComplaint);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

            } else {

                if (Common.selectedComplaint != null) {
                    setComplaintModel(Common.selectedComplaint);
                }
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