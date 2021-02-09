package ac.sliet.complaintmanagement.Common;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import ac.sliet.complaintmanagement.Model.FCMResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;


import com.androidadvance.topsnackbar.TSnackbar;
import com.google.firebase.Timestamp;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ac.sliet.complaintmanagement.Model.ComplaintModel;
import ac.sliet.complaintmanagement.Model.FCMSendData;
import ac.sliet.complaintmanagement.Model.ItemModel;
import ac.sliet.complaintmanagement.Model.UserModel;
import ac.sliet.complaintmanagement.Remote.IFCMService;
import ac.sliet.complaintmanagement.Remote.RetrofitFCMClient;
import ac.sliet.complaintmanagement.UI.Fragments.ComplaintClosingAcnowledge.ComplaintClosingAcnowledgeFragment;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class Common {


    public static final String USERS_COLLECTION_REF = "Users";
    public static final String COMPLAINT_COLLECTION_REFERENCE = "Complaints";
    public static UserModel currentUser;

    public static final String ERROR_COLOR = "#BF0101";
    public static final String GREEN_COLOR = "#12B517";
    public static final String BLUE_COLOR = "#2626D9";
    public static ComplaintModel selectedComplaint;


    public static final int COMPLAINT_STATUS_REQUESTED = 0;
    public static final int COMPLAINT_STATUS_ACCEPTED = 1;
    public static final int COMPLAINT_STATUS_ATTENDED_TODAY = 2;
    public static final int COMPLAINT_STATUS_POSTPONED = 3;
    public static final int COMPLAINT_STATUS_ATTENDED_ON_POSTPONED_DATE = 4;
    public static final int COMPLAINT_STATUS_COMPLETED = 5;
    public static final String NOTI_TITLE = "title";
    public static final String NOTI_CONTENT = "content";
    public static final String NOTI_CID = "complaintId";


    public static List<ItemModel> addedItemList = new ArrayList<>();
    public static Timestamp selectedNextAvailableDate;

    public static boolean has_User_Pressed_Back_Button_on_Acknowledgement_Screen;
    public static ComplaintClosingAcnowledgeFragment fragment_acknowledge;
    public static Parcelable recyclerViewState;

    public static CompositeDisposable compositeDisposable = new CompositeDisposable();
    public static IFCMService ifcmService = (RetrofitFCMClient.getInstance()).create(IFCMService.class);

    public static void showSnackBarAtTop(String text, String color, int textColor, Activity activity) {
        TSnackbar snackbar = TSnackbar.make(activity.findViewById(android.R.id.content), text, TSnackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.parseColor(color));
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(textColor);
        snackbar.show();
    }

    public static String getComplaintStatus(int status, Date availabilityDate, Context context) {
        DateFormat dateFormat = android.text.format.DateFormat.getMediumDateFormat(context);
        switch (status) {
            case COMPLAINT_STATUS_REQUESTED:
                return "Requested";
            case COMPLAINT_STATUS_ACCEPTED:
                return "Accepted";

            case COMPLAINT_STATUS_ATTENDED_TODAY:
            case COMPLAINT_STATUS_ATTENDED_ON_POSTPONED_DATE:

                return "Will be attended on " + dateFormat.format(availabilityDate);
            case COMPLAINT_STATUS_POSTPONED:
                return "Postponed by Complainant";
            case COMPLAINT_STATUS_COMPLETED:
                return "Complaint Closed";
            default:
                return "N A";

        }

    }



    public static void pushNotificationToTopic(String title,String content,String complaintId,String topic)
    {
        Map<String,String> notificationData = new HashMap<>();

        notificationData.put(NOTI_TITLE,title);
        notificationData.put(NOTI_CONTENT,content);
        notificationData.put(NOTI_CID,complaintId);

        FCMSendData fcmSendData = new FCMSendData("/topics/"+topic,notificationData);

        compositeDisposable.add(ifcmService.sendNotification(fcmSendData).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<FCMResponse>() {
            @Override
            public void accept(FCMResponse fcmResponse) throws Exception {

            }
        }));

    }

}
