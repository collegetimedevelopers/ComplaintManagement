package ac.sliet.complaintmanagement.UI.Fragments.MyComplaints;

import android.app.Activity;
import android.graphics.Color;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ac.sliet.complaintmanagement.Common.Common;
import ac.sliet.complaintmanagement.Model.ComplaintModel;

import static com.google.firebase.firestore.DocumentSnapshot.ServerTimestampBehavior.ESTIMATE;

public class MyComplaintsViewModel extends ViewModel {
    private MutableLiveData<List<ComplaintModel>> complaintsList;
    Activity activity;

    public MyComplaintsViewModel() {
        if (complaintsList == null) {
            complaintsList = new MutableLiveData<>();
            getComplaintsFromFireStore();
        }
    }


    public void getComplaintsFromFireStore() {

        System.out.println("Refreshed");
        FirebaseFirestore.getInstance().collection(Common.COMPLAINT_COLLECTION_REFERENCE)
                .whereEqualTo("complainantUid", FirebaseAuth.getInstance().getUid())
                .orderBy("complaintFilingDate", Query.Direction.DESCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<ComplaintModel> tempList = new ArrayList<>();

                if (!queryDocumentSnapshots.isEmpty()) {

                   for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments())
                   {
                        ComplaintModel complaintModel = documentSnapshot.toObject(ComplaintModel.class);
                        tempList.add(complaintModel);

                   }
                    setComplaintsList(tempList);
                }
                else {
                    System.out.println("Not found");
                    setComplaintsList(tempList);
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                   //     setComplaintsList(tempList);
                        Common.showSnackBarAtTop("Some Error Occurred", Common.ERROR_COLOR, Color.WHITE, activity);
                    }
                });
    }

    public MutableLiveData<List<ComplaintModel>> getComplaintsList() {

        return complaintsList;
    }

    public void setComplaintsList(List<ComplaintModel> complaints) {
        complaintsList.setValue(complaints);
        System.out.println("value set");

    }

}