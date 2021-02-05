package ac.sliet.complaintmanagement.UI.Fragments.NewComplaint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewComplaintViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NewComplaintViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}