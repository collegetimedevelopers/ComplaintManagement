package ac.sliet.complaintmanagement.UI.Fragments.Profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ac.sliet.complaintmanagement.Common.Common;
import ac.sliet.complaintmanagement.Model.UserModel;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<UserModel> userModelMutableLiveData;

    public ProfileViewModel() {
       if (userModelMutableLiveData== null)
       {
           userModelMutableLiveData = new MutableLiveData<>();
           setData();
       }
    }

    private void setData() {

        if (Common.currentUser!=null)
            userModelMutableLiveData.setValue(Common.currentUser);

    }


    public MutableLiveData<UserModel> getUserModelMutableLiveData() {
        return userModelMutableLiveData;
    }

    public void setUserModelMutableLiveData(MutableLiveData<UserModel> userModelMutableLiveData) {
        this.userModelMutableLiveData = userModelMutableLiveData;
    }
}