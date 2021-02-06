package ac.sliet.complaintmanagement.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleCoroutineScope;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import ac.sliet.complaintmanagement.Model.ComplaintModel;
import ac.sliet.complaintmanagement.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyComplaintsAdapter extends RecyclerView.Adapter<MyComplaintsAdapter.MyViewHolder> {
    List<ComplaintModel> complaintModelList;
    Context context;

    public MyComplaintsAdapter(List<ComplaintModel> complaintModelList, Context context) {
        this.complaintModelList = complaintModelList;
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
        holder.compplaintDate.setText(new StringBuilder(timeFormat.format(date)).append("\t on \t").append(dateFormat.format(date)));

        holder.complaintIntercom.setText(complaintModelList.get(position).getInterComNumber());

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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);

        }
    }
}
