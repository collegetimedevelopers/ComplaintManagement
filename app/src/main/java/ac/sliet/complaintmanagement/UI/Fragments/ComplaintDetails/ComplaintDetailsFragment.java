package ac.sliet.complaintmanagement.UI.Fragments.ComplaintDetails;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baoyachi.stepview.VerticalStepView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.io.LittleEndianDataInputStream;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ac.sliet.complaintmanagement.Adapters.ItemsAdapter;
import ac.sliet.complaintmanagement.Common.Common;
import ac.sliet.complaintmanagement.Events.DateSelectedEvent;
import ac.sliet.complaintmanagement.Events.OpenComplaintDetailsEvent;
import ac.sliet.complaintmanagement.Events.OpenMarkCompletedEvent;
import ac.sliet.complaintmanagement.Model.ComplaintModel;
import ac.sliet.complaintmanagement.Pickers.DatePickerFragment;
import ac.sliet.complaintmanagement.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ComplaintDetailsFragment extends Fragment {

    private ComplaintDetailsViewModel mViewModel;

    Unbinder unbinder;

    @BindView(R.id.comp_detail_comp_id)
    TextView complaintId;

    @BindView(R.id.comp_detail_comp_filing_date)
    TextView filingDate;

    @BindView(R.id.comp_detail_comp_category_txt)
    TextView category;
    @BindView(R.id.comp_detail_comp_desc_txt)
    TextView description;
    @BindView(R.id.comp_detail_comp_name_txt)
    TextView complainantName;
    @BindView(R.id.comp_detail_comp_intercom_txt)
    TextView complainantIntercom;
    @BindView(R.id.comp_detail_comp_phone_txt)
    TextView complainantPhone;
    @BindView(R.id.comp_detail_comp_email_txt)
    TextView complainantEmail;
    @BindView(R.id.comp_detail_comp_address_txt)
    TextView complainantAddress;
    @BindView(R.id.comp_detail_status_step_view)
    VerticalStepView statusStepView;
    @BindView(R.id.comp_detail_linear_layout_postppne_compl)
    LinearLayout postpone_completedLayout;

    @BindView(R.id.comp_detail_comp_postpone)
    TextView postponeComplaint;
    @BindView(R.id.comp_detail_comp_completed)
    TextView completeComplaint;

    @BindView(R.id.comp_detail_items_recycler_parent_card)
    CardView recyclerParentCard;

    @BindView(R.id.comp_detail_comp_items_recycler)
    RecyclerView replacedItemsRecyclerView;

    @BindView(R.id.comp_detail_progressBar)
    ProgressBar progressBar;


    @BindView(R.id.comp_detail_content_linear_layout)
    LinearLayout contentLayout;

    public static ComplaintDetailsFragment newInstance() {
        return new ComplaintDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(ComplaintDetailsViewModel.class);

        View root = inflater.inflate(R.layout.fragment_complaint_details, container, false);
        unbinder = ButterKnife.bind(this, root);

        progressBar.setVisibility(View.VISIBLE);

        mViewModel.getComplaintModel().observe(getViewLifecycleOwner(), new Observer<ComplaintModel>() {
            @Override
            public void onChanged(ComplaintModel complaintModel) {
                if (complaintModel != null) {

                    setValuesToFields(complaintModel);
                    contentLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                }
            }
        });

        setListeners();
        return root;

    }

    private void setListeners() {

        postponeComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment(0);
                datePicker.show(getParentFragmentManager(), "picker");

            }
        });


        completeComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                EventBus.getDefault().post(new OpenMarkCompletedEvent(true));
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void postponeComplaintOnFirebase(DateSelectedEvent event) {

        Map<String, Object> updateMap = new HashMap<>();

        updateMap.put("availableOnDate", event.getSelectedDate());
        updateMap.put("postponedDate", FieldValue.serverTimestamp());
        updateMap.put("postponed", true);
        updateMap.put("status", Common.COMPLAINT_STATUS_POSTPONED);


        FirebaseFirestore.getInstance().collection(Common.COMPLAINT_COLLECTION_REFERENCE)
                .document(Common.selectedComplaint.getComplaintId())
                .update(updateMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Common.showSnackBarAtTop("Postponed Successfully",Common.GREEN_COLOR,Color.WHITE,getActivity());

                        Common.selectedComplaint.setPostponed(true);
                        Common.selectedComplaint.setStatus(Common.COMPLAINT_STATUS_POSTPONED);


                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        Timestamp postponeTime = new com.google.firebase.Timestamp(calendar.getTime());
                        Common.selectedComplaint.setPostponedDate(postponeTime);
                        Common.selectedComplaint.setAvailableOnDate(event.getSelectedDate());
                        // to update ui

                        try {
                            getActivity().getSupportFragmentManager().popBackStack();


                            EventBus.getDefault().post(new OpenComplaintDetailsEvent(true));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }



    private void setValuesToFields(ComplaintModel complaintModel) {

        complaintId.setText(complaintModel.getComplaintId());
        Date date = new Date(complaintModel.getComplaintFilingDate().toDate().getTime());

        DateFormat dateFormat = android.text.format.DateFormat.getMediumDateFormat(getContext());
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(getContext());
        filingDate.setText(new StringBuilder(dateFormat.format(date)).append("\t at \t").append(timeFormat.format(date)));

        category.setText(complaintModel.getComplaintCategory());
        description.setText(complaintModel.getComplaintDescription());

        complainantName.setText(complaintModel.getComplainantName());
        complainantIntercom.setText(complaintModel.getInterComNumber());
        complainantPhone.setText(complaintModel.getPhoneNumber());
        complainantEmail.setText(complaintModel.getComplainantEmail());
        complainantAddress.setText(complaintModel.getComplainantAddress());

        if (complaintModel.getStatus() == 2 || complaintModel.getStatus() == 4) {
            // user can only interact if the status is  2 means will be attended today or 4 means will be attended on --users selected date--
            // and the postpone button will be disabled if user has once postponed
            postpone_completedLayout.setVisibility(View.VISIBLE);

            if (complaintModel.isPostponed()) {
                postponeComplaint.setTextColor(getResources().getColor(R.color.uncompleted_status_text_color));
                postponeComplaint.setEnabled(false);
            }

        } else {
            postpone_completedLayout.setVisibility(View.GONE);
        }


        // this card will be only shown if threr are items to  show in reycler view

        if (null != complaintModel.getItemsReplaced() && complaintModel.getStatus() == 5 && complaintModel.getItemsReplaced().size() != 0) {
            recyclerParentCard.setVisibility(View.VISIBLE);
            ItemsAdapter itemsAdapter = new ItemsAdapter(complaintModel.getItemsReplaced(), getContext(), false);

            replacedItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            replacedItemsRecyclerView.setHasFixedSize(true);
            replacedItemsRecyclerView.setAdapter(itemsAdapter);
        } else {

            recyclerParentCard.setVisibility(View.GONE);

        }


        setStatus(complaintModel);
    }

    private void setStatus(ComplaintModel complaintModel) {

        List<String> list = new ArrayList<>();
        Date filingDate = new Date(complaintModel.getComplaintFilingDate().toDate().getTime());
        Date availaibleOnDate = new Date(complaintModel.getAvailableOnDate().toDate().getTime());


        DateFormat dateFormat = android.text.format.DateFormat.getMediumDateFormat(getContext());
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(getContext());

        list.add("Requested on " + dateFormat.format(filingDate) + " at " + timeFormat.format(filingDate));

        list.add("Accepted");

        if (!complaintModel.isPostponed()) {

            list.add("Will Be Attending on " + dateFormat.format(availaibleOnDate) );
        } else {
            Date postponeDate = new Date(complaintModel.getPostponedDate().toDate().getTime());

            list.add("Was Attended on " + dateFormat.format(postponeDate));
        }


        if (complaintModel.getStatus() == Common.COMPLAINT_STATUS_POSTPONED || complaintModel.isPostponed()) {

            Date postponeDate = new Date(complaintModel.getPostponedDate().toDate().getTime());

            list.add("Postponed on " + dateFormat.format(postponeDate) + " at " + timeFormat.format(postponeDate));

            if (complaintModel.getStatus() < Common.COMPLAINT_STATUS_COMPLETED)
            {
                list.add("Will Be Attending on " + dateFormat.format(availaibleOnDate));
            } else {
                list.add("Was Attended on " + dateFormat.format(availaibleOnDate));
            }

        }


        if (complaintModel.getStatus() == 5) {
            Date date = new Date(complaintModel.getComplaintClosingDate().toDate().getTime());


            list.add("Completed on " + dateFormat.format(date) + " at " + timeFormat.format(date));
        } else {
            Date usersGivenAvailableDate = new Date(complaintModel.getAvailableOnDate().toDate().getTime());

            list.add("Would be Completed on " + dateFormat.format(usersGivenAvailableDate));
        }

        statusStepView.setStepsViewIndicatorComplectingPosition(complaintModel.getStatus() + 1)//设置完成的步数
                .reverseDraw(false)//default is true
                .setStepViewTexts(list)
                .setLinePaddingProportion(0.85f)
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(getActivity(), R.color.completed_status_line_color))//设置StepsViewIndicator完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(getActivity(), android.R.color.black))//设置StepsViewIndicator未完成线的颜色
                .setStepViewComplectedTextColor(ContextCompat.getColor(getActivity(), android.R.color.black))//设置StepsView text完成线的颜色
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(getActivity(), R.color.uncompleted_status_text_color))//设置StepsView text未完成线的颜色
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_baseline_check_circle))//设置StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(getActivity(), R.drawable.default_icon))//设置StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_outline_current));//设置StepsViewIndicator AttentionIcon

    }


    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);

    }
}