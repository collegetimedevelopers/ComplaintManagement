package ac.sliet.complaintmanagement.Adapters;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleCoroutineScope;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import ac.sliet.complaintmanagement.Common.Common;
import ac.sliet.complaintmanagement.Events.OpenComplaintDetailsEvent;
import ac.sliet.complaintmanagement.Model.ComplaintModel;
import ac.sliet.complaintmanagement.R;
import ac.sliet.complaintmanagement.UI.Fragments.MyComplaints.MyComplaintsFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyComplaintsAdapter extends RecyclerView.Adapter<MyComplaintsAdapter.MyViewHolder> {
    List<ComplaintModel> complaintModelList;
    Activity activity;
    Context context;

    public MyComplaintsAdapter(List<ComplaintModel> complaintModelList, Activity activity, Context context) {
        this.complaintModelList = complaintModelList;
        this.activity = activity;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyComplaintsAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_my_complaints, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.complaintID.setText(complaintModelList.get(position).getComplaintId());

        Date date = new Date(complaintModelList.get(position).getComplaintFilingDate().toDate().getTime());

        DateFormat dateFormat = android.text.format.DateFormat.getMediumDateFormat(context);
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(context);
        holder.compplaintDate.setText(new StringBuilder("Filed on \t" + dateFormat.format(date)).append("\t at \t").append(timeFormat.format(date)));

        holder.complaintIntercom.setText(complaintModelList.get(position).getInterComNumber());
        holder.complaintCategory.setText(complaintModelList.get(position).getComplaintCategory());

        Date availabilityDate = new Date(complaintModelList.get(position).getAvailableOnDate().toDate().getTime());

        holder.complaintStatus.setText(Common.getComplaintStatus(complaintModelList.get(position).getStatus(),availabilityDate,context));



        if (complaintModelList.get(position).getStatus() == Common.COMPLAINT_STATUS_REQUESTED)
            holder.lottieAnimationView.setAnimation(R.raw.pending);

        if (complaintModelList.get(position).getStatus() == Common.COMPLAINT_STATUS_ACCEPTED)
            holder.lottieAnimationView.setAnimation(R.raw.accepted);

        if (complaintModelList.get(position).getStatus() == Common.COMPLAINT_STATUS_ATTENDED_TODAY
                || complaintModelList.get(position).getStatus() == Common.COMPLAINT_STATUS_ATTENDED_ON_POSTPONED_DATE)
            holder.lottieAnimationView.setAnimation(R.raw.attending);


        if (complaintModelList.get(position).getStatus() == Common.COMPLAINT_STATUS_POSTPONED)
            holder.lottieAnimationView.setAnimation(R.raw.postpone);

        if (complaintModelList.get(position).getStatus() == Common.COMPLAINT_STATUS_COMPLETED)
            holder.lottieAnimationView.setAnimation(R.raw.completed);



        holder.copyImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Complaint Id", complaintModelList.get(position).getComplaintId());
                clipboard.setPrimaryClip(clip);
                Common.showSnackBarAtTop("Copied Successfully", Common.GREEN_COLOR, Color.WHITE, activity);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.selectedComplaint = complaintModelList.get(position);

                EventBus.getDefault().post(new OpenComplaintDetailsEvent(true));
            }
        });
    }

    @Override
    public int getItemCount() {
        return complaintModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        Unbinder unbinder;

        @BindView(R.id.lay_my_comp_txt_comp_date)
        TextView compplaintDate;

        @BindView(R.id.lay_my_comp_txt_compID)
        TextView complaintID;

        @BindView(R.id.lay_my_comp_txt_intercom)
        TextView complaintIntercom;

        @BindView(R.id.lay_my_comp_txt_comp_category)
        TextView complaintCategory;

        @BindView(R.id.lay_my_comp_txt_status)
        TextView complaintStatus;

        @BindView(R.id.lottie_view_complaints)
        LottieAnimationView lottieAnimationView;

        @BindView(R.id.lay_my_comp_img_copy)
        ImageView copyImg;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);

        }
    }
}
