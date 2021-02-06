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


    private void getComplaintsFromFireStore() {
        FirebaseFirestore.getInstance().collection(Common.COMPLAINT_COLLECTION_REFERENCE)
                .whereEqualTo("complainantUid", Common.currentUser.getUid())// .orderBy("complaintFilingDate", Query.Direction.DESCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<ComplaintModel> tempList = new ArrayList<>();
                if (!queryDocumentSnapshots.isEmpty()) {
                   for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        ComplaintModel complaintModel = documentSnapshot.toObject(ComplaintModel.class);
                        tempList.add(complaintModel);
                       DocumentSnapshot.ServerTimestampBehavior behavior = ESTIMATE;
                       Date date = documentSnapshot.getDate("complaintFilingDate", behavior);
//                       System.out.println(date.getTime() +" below");
//
//
//                       complaintModel.setComplaintFilingDate(documentSnapshot.getTimestamp("complaintFilingDate",behavior));


//                        String dDate= complaintModel.getComplaintFilingDate().toDate().toString();
//                       SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
//                       try {
//                           Date cDate = df.parse(dDate);
//                           System.out.println( cDate.getTime() +" time ");
//
//                       } catch (ParseException e) {
//                           e.printStackTrace();
//                       }

                       System.out.println(complaintModel.getComplaintFilingDate()+" filing date");
                    }
                    setComplaintsList(tempList);
                }
                else {
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
    }

}