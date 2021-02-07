package ac.sliet.complaintmanagement.UI.Fragments.MarkCompleted;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ac.sliet.complaintmanagement.Adapters.ItemsAdapter;
import ac.sliet.complaintmanagement.Common.Common;
import ac.sliet.complaintmanagement.Model.ItemModel;
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

    @BindView(R.id.mark_compl_item_gp_no)
    TextInputLayout itemGPNumber;


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


    @BindView(R.id.mark_compl_img_satisfied)
    ImageView satisfiedImg;

    @BindView(R.id.mark_compl_img_unsatisfied)
    ImageView unsatisfiedImg;

    @BindView(R.id.mark_compl_edt_comment)
    EditText userComment;

    boolean isFeedbackLayoutClicked = false;
    boolean isSatisfied = false;


    public static MarkCompletedFragment newInstance() {
        return new MarkCompletedFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(MarkCompletedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mark_completed, container, false);
        unbinder = ButterKnife.bind(this, root);


        mViewModel.getListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<ItemModel>>() {
            @Override
            public void onChanged(List<ItemModel> itemModels) {
                itemRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                itemRecycler.setHasFixedSize(true);
                ItemsAdapter itemsAdapter = new ItemsAdapter(itemModels, getContext(),true);
                itemRecycler.setAdapter(itemsAdapter);
            }
        });

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.item_state));

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spinnerAdapter);

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
                satisfiedImg.setBackground(getResources().getDrawable(R.drawable.ic_baseline_thumb_up_selected));
                unsatisfiedImg.setBackground(getResources().getDrawable(R.drawable.ic_outline_thumb_down));
                isFeedbackLayoutClicked = true;
                isSatisfied = true;

            }
        });


        unsatisfiedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unsatisfiedLayout.setBackgroundColor(getResources().getColor(R.color.unsatisfied_feedback));
                satisfiedLayout.setBackgroundColor(getResources().getColor(android.R.color.white));
                satisfiedImg.setBackground(getResources().getDrawable(R.drawable.ic_outline_thumb_up_24));
                unsatisfiedImg.setBackground(getResources().getDrawable(R.drawable.ic_baseline_thumb_down_selected));
                isFeedbackLayoutClicked = true;
                isSatisfied = false;

            }
        });


        noFresh_DismantleCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    recyclerParentCard.setVisibility(View.GONE);
                } else {
                    recyclerParentCard.setVisibility(View.VISIBLE);
                }
            }
        });


        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemName.getEditText().getText().toString().trim().isEmpty()) {
                    itemName.setError("Enter valid name");
                    return;
                }

                if (itemQty.getEditText().getText().toString().trim().isEmpty() || itemQty.getEditText().getText().toString().trim().equals("0")) {
                    itemQty.setError("Enter valid name");
                    return;
                }


                if (spinner.getSelectedItem().toString().equals(getResources().getStringArray(R.array.item_state)[0])) {
                    Common.showSnackBarAtTop("Please Select Item Status", Common.ERROR_COLOR, Color.WHITE, getActivity());
                    return;
                }
                ItemModel item = new ItemModel();

                item.setItemName(itemName.getEditText().getText().toString().trim());
                item.setItemQuantity(Double.parseDouble(itemQty.getEditText().getText().toString().trim()));
                item.setGpNumber(itemGPNumber.getEditText().getText().toString().trim());
                item.setNewItem(spinner.getSelectedItem().equals(getResources().getStringArray(R.array.item_state)[1]));

                Common.addedItemList.add(item);
                mViewModel.setListMutableLiveData(Common.addedItemList);
            }
        });


        closeComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.selectedComplaint.getStatus() == 5) {// so that user cant close agian with another values
                    Common.showSnackBarAtTop("Complaint already closed", Common.ERROR_COLOR, Color.WHITE, getActivity());
                    return;
                }
                if (!isFeedbackLayoutClicked) {
                    Common.showSnackBarAtTop("Please give feedback.", Common.ERROR_COLOR, Color.WHITE, getActivity());
                    return;
                }
                if (userComment.getText().toString().trim().isEmpty()) {
                    Common.showSnackBarAtTop("Please write your Comment.", Common.ERROR_COLOR, Color.WHITE, getActivity());
                    return;
                }

                updateValuesOnFirestore();

            }
        });
    }

    private void updateValuesOnFirestore() {


        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("status", Common.COMPLAINT_STATUS_COMPLETED);
        updateMap.put("satisfactory", isSatisfied);
        updateMap.put("userComment", userComment.getText().toString().trim());
        updateMap.put("complaintClosingDate", FieldValue.serverTimestamp());

        if (!noFresh_DismantleCheck.isChecked()) {
            System.out.println("size of itemlist = "+Common.addedItemList.size());
            updateMap.put("itemsReplaced", Common.addedItemList);
//            Common.addedItemList.clear();

        } else {
            try {
                Common.addedItemList.clear();
            } finally {

            }
        }


        FirebaseFirestore.getInstance().collection(Common.COMPLAINT_COLLECTION_REFERENCE)
                .document(Common.selectedComplaint.getComplaintId())
                .update(updateMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                try {
                    Common.showSnackBarAtTop("Complaint Closed", Common.GREEN_COLOR, Color.WHITE, getActivity());
                    Common.selectedComplaint.setStatus(Common.COMPLAINT_STATUS_COMPLETED);
                    Common.addedItemList.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.
                        out.println("Error in closing complaint = " + e.getMessage());
            }
        });
    }


}