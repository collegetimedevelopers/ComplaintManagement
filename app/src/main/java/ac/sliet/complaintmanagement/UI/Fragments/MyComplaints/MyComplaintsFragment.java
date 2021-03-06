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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

import ac.sliet.complaintmanagement.Adapters.MyComplaintsAdapter;
import ac.sliet.complaintmanagement.Common.Common;
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

    @BindView(R.id.my_com_empty_txt)
    TextView empty_txt;


    @BindView(R.id.my_com_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;


    LinearLayoutManager linearLayoutManager;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myComplaintsViewModel =
                new ViewModelProvider(this).get(MyComplaintsViewModel.class);

        myComplaintsViewModel.activity = getActivity();

        View root = inflater.inflate(R.layout.fragment_my_complaints, container, false);
        linearLayoutManager = new LinearLayoutManager(getContext());
        unbinder = ButterKnife.bind(this, root);
        progressBar.setVisibility(View.VISIBLE);
        myComplaintsViewModel.getComplaintsList().observe(getViewLifecycleOwner(), new Observer<List<ComplaintModel>>() {
            @Override
            public void onChanged(List<ComplaintModel> complaintModels) {
                if (complaintModels.isEmpty()|| complaintModels.size()==0)
                {

                    empty_txt.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
               else  {
                    empty_txt.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    MyComplaintsAdapter myComplaintsAdapter = new MyComplaintsAdapter(complaintModels, getActivity(),getContext());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation()));
                    recyclerView.setAdapter(myComplaintsAdapter);
                  //  swipeRefreshLayout.pro

                    try {
                        swipeRefreshLayout.setRefreshing(false);
                        recyclerView.getLayoutManager().onRestoreInstanceState(Common.recyclerViewState);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                progressBar.setVisibility(View.GONE);

            }
        });



        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //this is to retain the state of recycler view during the whole app lifecycle on a particular app launch
                //thus we are storing the state of recycler view in common
                Common.recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();


            }
        });



        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                myComplaintsViewModel.getComplaintsFromFireStore();

            }
        });

        return root;
    }
}