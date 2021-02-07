package ac.sliet.complaintmanagement.Pickers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;



import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;

public  class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    int flag; // 1 for FROM DATE and 2 for TO DATE

    public DatePickerFragment(int flag) {
        this.flag = flag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(requireActivity(), this, year, month, day);
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        // Create a new instance of DatePickerDialog and return it
        return dialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        Date date = calendar.getTime();


    }
}
