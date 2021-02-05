package ac.sliet.complaintmanagement.UI.Fragments.NewComplaint;

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


public class NewComplaintFragment extends Fragment {

    private NewComplaintViewModel newComplaintViewModel;

    Unbinder unbinder;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newComplaintViewModel =
                new ViewModelProvider(this).get(NewComplaintViewModel.class);
        View root = inflater.inflate(R.layout.fragment_new_complaints, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        newComplaintViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        unbinder = ButterKnife.bind(this,root)
;

        return root;
    }
}