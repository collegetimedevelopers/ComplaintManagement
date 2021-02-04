package ac.sliet.complaintmanagement.Common;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;

public class Common {


    public static void showSnackBarAtTop(String text, String color,int textColor, Activity activity)
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
