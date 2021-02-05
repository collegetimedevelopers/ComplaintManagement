package ac.sliet.complaintmanagement.UI.Fragments.MyComplaints;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import ac.sliet.complaintmanagement.R;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MyComplaintsFragment extends Fragment {
    Unbinder unbinder;
      private MyComplaintsViewModel myComplaintsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myComplaintsViewModel =
                new ViewModelProvider(this).get(MyComplaintsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_complaints, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        myComplaintsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        unbinder= ButterKnife.bind(this,root);

        return root;
    }
}