package ac.sliet.complaintmanagement.UI.Fragments.NewComplaint;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.GregorianCalendar;

import ac.sliet.complaintmanagement.Common.Common;
import ac.sliet.complaintmanagement.Model.ComplaintModel;
import ac.sliet.complaintmanagement.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class NewComplaintFragment extends Fragment {

    private NewComplaintViewModel newComplaintViewModel;

    @BindView(R.id.new_comp_spinner_comp_category)
    Spinner complaintCategorySpinner;

    @BindView(R.id.new_comp_btn_file_complaint)
    MaterialButton btn_file_complaint;

    @BindView(R.id.new_comp_edt_problem_desc)
    EditText problemDescriptionEdtTxt;

    @BindView(R.id.new_comp_edt_intercom)
    TextInputLayout intercomEdit;

    @BindView(R.id.new_comp_progress)
    ProgressBar progressBar;

    @BindView(R.id.new_comp_cal_availaible_on_date)
    CalendarView calendarView_availaibleOnDate;

    Unbinder unbinder;
    Timestamp availableDateTimestamp;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newComplaintViewModel =
                new ViewModelProvider(this).get(NewComplaintViewModel.class);
        View root = inflater.inflate(R.layout.fragment_new_complaints, container, false);

        newComplaintViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {


            }
        });


        unbinder = ButterKnife.bind(this, root);
        calendarView_availaibleOnDate.setMinDate(System.currentTimeMillis() - 1000);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.complaint_category));

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        complaintCategorySpinner.setAdapter(spinnerAdapter);


        calendarView_availaibleOnDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                GregorianCalendar cal = new GregorianCalendar(year, month, dayOfMonth);

                availableDateTimestamp = new Timestamp(cal.getTime());

            }
        });

        btn_file_complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (complaintCategorySpinner.getSelectedItem().toString().equals(getResources().getStringArray(R.array.complaint_category)[0])) {
                    Common.showSnackBarAtTop("Please select problem category.", Common.ERROR_COLOR, Color.WHITE, getActivity());
                    return;
                }
                if (problemDescriptionEdtTxt.getText().toString().trim().isEmpty()) {

                    Common.showSnackBarAtTop("Please enter problem description.", Common.ERROR_COLOR, Color.WHITE, getActivity());
                    return;
                }
                if (intercomEdit.getEditText().getText().toString().trim().isEmpty()) {

                    Common.showSnackBarAtTop("Please enter InterCom number.", Common.ERROR_COLOR, Color.WHITE, getActivity());
                    return;
                }

                if (null == availableDateTimestamp) {
                    Common.showSnackBarAtTop("Please select your availability date", Common.ERROR_COLOR, Color.WHITE, getActivity());
                    return;
                }

                //  getTimeStampFromFirebase();
                uploadComplaintToFireStore();

            }
        });

        return root;
    }


    private void uploadComplaintToFireStore() {
        progressBar.setVisibility(View.VISIBLE);

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection(Common.COMPLAINT_COLLECTION_REFERENCE).document();
        String complaintId = documentReference.getId();

        ComplaintModel complaintModel = new ComplaintModel();

        complaintModel.setComplainantName(Common.currentUser.getUserName());
        complaintModel.setComplainantEmail(Common.currentUser.getEmail());
        complaintModel.setComplainantAddress(Common.currentUser.getAddress());
        complaintModel.setComplainantUid(Common.currentUser.getUid());
        complaintModel.setInterComNumber(intercomEdit.getEditText().getText().toString());
        complaintModel.setComplaintId(complaintId);
        complaintModel.setStatus(0);
        complaintModel.setPostponed(false);
        complaintModel.setAvailableOnDate(availableDateTimestamp);
        complaintModel.setComplaintDescription(problemDescriptionEdtTxt.getText().toString().trim());
        complaintModel.setPhoneNumber(Common.currentUser.getPhoneNumber());
        complaintModel.setComplaintCategory(complaintCategorySpinner.getSelectedItem().toString());

        documentReference.set(complaintModel)
                .addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(View.GONE);
                    Common.pushNotificationToTopic("New Complaint", "Complaint Filed By " + Common.currentUser.getUserName() + " with complaint id" + complaintModel.getComplaintId(),
                            complaintModel.getComplaintId(), complaintModel.getComplaintCategory(),
                            getActivity(),progressBar);

                    clearFields();

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);

                Common.showSnackBarAtTop("Failed to File Complaint 😑", Common.ERROR_COLOR, Color.WHITE, getActivity());
            }
        });
    }

    private void clearFields() {
        availableDateTimestamp = null;
        problemDescriptionEdtTxt.getText().clear();
        intercomEdit.getEditText().getText().clear();
        complaintCategorySpinner.setSelection(0);

    }
}