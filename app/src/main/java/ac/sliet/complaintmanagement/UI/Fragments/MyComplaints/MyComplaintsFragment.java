package ac.sliet.complaintmanagement.UI.Fragments.MyComplaints;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ac.sliet.complaintmanagement.Adapters.MyComplaintsAdapter;
import ac.sliet.complaintmanagement.Model.ComplaintModel;
import ac.sliet.complaintmanagement.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MyComplaintsFragment extends Fragment {
    Unbinder unbinder;
    private MyComplaintsViewModel myComplaintsViewModel;


    @BindView(R.id.my_comp_progress)
    ProgressBar progressBar;

    @BindView(R.id.my_comp_recycler)
    RecyclerView recyclerView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myComplaintsViewModel =
                new ViewModelProvider(this).get(MyComplaintsViewModel.class);

        myComplaintsViewModel.activity = getActivity();

        View root = inflater.inflate(R.layout.fragment_my_complaints, container, false);

        unbinder = ButterKnife.bind(this, root);
        progressBar.setVisibility(View.VISIBLE);
        myComplaintsViewModel.getComplaintsList().observe(getViewLifecycleOwner(), new Observer<List<ComplaintModel>>() {
            @Override
            public void onChanged(List<ComplaintModel> complaintModels) {
                progressBar.setVisibility(View.GONE);

                MyComplaintsAdapter myComplaintsAdapter = new MyComplaintsAdapter(complaintModels,getContext());
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(myComplaintsAdapter);
            }
        });


        return root;
    }
}