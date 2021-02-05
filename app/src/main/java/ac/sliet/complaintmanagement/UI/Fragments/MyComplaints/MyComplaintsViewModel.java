package ac.sliet.complaintmanagement.UI.Fragments.MyComplaints;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyComplaintsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyComplaintsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}