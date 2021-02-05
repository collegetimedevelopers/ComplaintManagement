package ac.sliet.complaintmanagement.Common;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;

import ac.sliet.complaintmanagement.Model.UserModel;

public class Common {


    public static final String USERS_COLLECTION_REF = "Users" ;
    public static final String COMPLAINT_COLLECTION_REFERENCE = "Complaints" ;
    public static UserModel currentUser;

    public  static final String ERROR_COLOR = "#BF0101";
    public  static final String GREEN_COLOR = "#12B517";
    public  static final String BLUE_COLOR = "#2626D9";


    public static void showSnackBarAtTop(String text, String color, int textColor, Activity activity)
    {
        TSnackbar snackbar = TSnackbar.make(activity.findViewById(android.R.id.content), text, TSnackbar.LENGTH_LONG);
        // snackbar.setActionTextColor(Color.WHITE);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.parseColor(color));
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(textColor);
        snackbar.show();
    }
}
