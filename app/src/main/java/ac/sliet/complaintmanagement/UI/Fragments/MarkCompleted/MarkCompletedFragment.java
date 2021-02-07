package ac.sliet.complaintmanagement.UI.Fragments.MarkCompleted;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import ac.sliet.complaintmanagement.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MarkCompletedFragment extends Fragment {

    private MarkCompletedViewModel mViewModel;
    Unbinder unbinder;

    @BindView(R.id.mark_compl_no_fresh_check)
    CheckBox noFresh_DismantleCheck;

    @BindView(R.id.mark_compl_item_recycler)
    RecyclerView itemRecycler;

    @BindView(R.id.mark_compl_item_name)
    TextInputLayout itemName;

    @BindView(R.id.mark_compl_item_qty)
    TextInputLayout itemQty;


    @BindView(R.id.mark_compl_spinner)
    Spinner spinner;

    @BindView(R.id.mark_compl_btn_add_item)
    MaterialButton addItem;

    @BindView(R.id.mark_compl_btn_close_complaint)
    MaterialButton closeComplaint;

    @BindView(R.id.mark_compl_layout_satisfied)
    LinearLayout satisfiedLayout;

    @BindView(R.id.mark_compl_layout_unsatisfied)
    LinearLayout unsatisfiedLayout;

    @BindView(R.id.mark_compl_items_parent_cardview)
    CardView recyclerParentCard;

    public static MarkCompletedFragment newInstance() {
        return new MarkCompletedFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(MarkCompletedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mark_completed, container, false);
        unbinder = ButterKnife.bind(this, root);


        if (noFresh_DismantleCheck.isChecked()) {
            recyclerParentCard.setVisibility(View.GONE);

        }

        setListeners();
        return root;
    }

    private void setListeners() {

        satisfiedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                satisfiedLayout.setBackgroundColor(getResources().getColor(R.color.satisfied_feedback));
                unsatisfiedLayout.setBackgroundColor(getResources().getColor(android.R.color.white));

            }
        });


        unsatisfiedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unsatisfiedLayout.setBackgroundColor(getResources().getColor(R.color.unsatisfied_feedback));
                satisfiedLayout.setBackgroundColor(getResources().getColor(android.R.color.white));

            }
        });
    }


}