package ac.sliet.complaintmanagement.UI.Fragments.ComplaintDetails;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyachi.stepview.VerticalStepView;
import com.google.common.io.LittleEndianDataInputStream;

import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ac.sliet.complaintmanagement.Events.OpenMarkCompletedEvent;
import ac.sliet.complaintmanagement.Model.ComplaintModel;
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

    public static ComplaintDetailsFragment newInstance() {
        return new ComplaintDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(ComplaintDetailsViewModel.class);

        View root = inflater.inflate(R.layout.fragment_complaint_details, container, false);
        unbinder = ButterKnife.bind(this, root);

        mViewModel.getComplaintModel().observe(getViewLifecycleOwner(), new Observer<ComplaintModel>() {
            @Override
            public void onChanged(ComplaintModel complaintModel) {
                if (complaintModel != null) {
                    setValuesToFields(complaintModel);
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

            }
        });


        completeComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new OpenMarkCompletedEvent(true));
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

        if (complaintModel.getStatus()==2 || complaintModel.getStatus()==4)
        {
            // user can only interact if the status is  2 means will be attended today or 4 means will be attended on --users selected date--
            // and the postpone button will be disabled if user has once postponed
            postpone_completedLayout.setVisibility(View.VISIBLE);

            if (complaintModel.isPostponed())
            {
                postponeComplaint.setTextColor(getResources().getColor(R.color.uncompleted_status_text_color));
                postponeComplaint.setEnabled(false);
            }

        }
        else {
            postpone_completedLayout.setVisibility(View.GONE);
        }




        setStatus(complaintModel);
    }

    private void setStatus(ComplaintModel complaintModel) {

        List<String> list = new ArrayList<>();
        list.add("Requested");
        list.add("Accepted");
        list.add("Will Be Attended Today");

        if (complaintModel.getStatus() == 3 || complaintModel.isPostponed()) {
            list.add("Postponed");
        }
        list.add("Completed");

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


}