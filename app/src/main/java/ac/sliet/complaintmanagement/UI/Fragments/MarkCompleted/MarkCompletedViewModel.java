package ac.sliet.complaintmanagement.UI.Fragments.MarkCompleted;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.airbnb.lottie.L;

import java.util.List;

import ac.sliet.complaintmanagement.Common.Common;
import ac.sliet.complaintmanagement.Model.ItemModel;

public class MarkCompletedViewModel extends ViewModel {
    MutableLiveData<List<ItemModel>> listMutableLiveData;

    public MarkCompletedViewModel() {
        if (listMutableLiveData == null) {
            listMutableLiveData = new MutableLiveData<>();

            setListMutableLiveData(Common.addedItemList);

        }
    }

    public MutableLiveData<List<ItemModel>> getListMutableLiveData() {
        return listMutableLiveData;
    }

    public void setListMutableLiveData(List<ItemModel> itemList) {
        listMutableLiveData.setValue(itemList);
    }
}