package ac.sliet.complaintmanagement.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.style.AlignmentSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ac.sliet.complaintmanagement.Model.ItemModel;
import ac.sliet.complaintmanagement.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> {

    List<ItemModel> itemList;
    Context context;
    boolean isInEditMode;

    public ItemsAdapter(List<ItemModel> itemList, Context context, boolean isInEditMode) {
        this.itemList = itemList;
        this.context = context;
        this.isInEditMode = isInEditMode;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemsAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_material_recycler, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        if (isInEditMode)
        {
            holder.itemDelete.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.itemDelete.setVisibility(View.GONE);

        }

        holder.itemName.setTextColor(itemList.get(position).isNewItem() ? context.getResources().getColor(R.color.mate_green) : context.getResources().getColor(R.color.error_color));
        holder.itemName.setText(itemList.get(position).getItemName());
        holder.itemGPNumber.setText(itemList.get(position).getGpNumber());
        holder.itemQuantity.setText(String.valueOf(itemList.get(position).getItemQuantity()));


        holder.itemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemList.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        Unbinder unbinder;

        @BindView(R.id.layout_material_name)
        TextView itemName;

        @BindView(R.id.layout_material_quantity)
        TextView itemQuantity;

        @BindView(R.id.layout_material_gp_no)
        TextView itemGPNumber;

        @BindView(R.id.layout_material_delete)
        ImageView itemDelete;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);

        }
    }
}
