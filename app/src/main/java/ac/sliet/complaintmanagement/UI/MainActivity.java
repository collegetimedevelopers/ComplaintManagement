package ac.sliet.complaintmanagement.UI;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ac.sliet.complaintmanagement.Common.Common;
import ac.sliet.complaintmanagement.Events.OpenComplaintDetailsEvent;
import ac.sliet.complaintmanagement.Events.OpenMarkCompletedEvent;
import ac.sliet.complaintmanagement.R;

import static androidx.navigation.Navigation.findNavController;

public class MainActivity extends AppCompatActivity {
    NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        navController = findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);



         Common.isAppOpenedFromNotification=   getIntent().getBooleanExtra(Common.IS_OPENED_FROM_NOTIFICATION, false);

         if (Common.isAppOpenedFromNotification)// if the app is opened from notification we will extract complaint id
         {
             Common.complaintIdFromNotification = getIntent().getStringExtra(Common.COMPLAINT_ID_FROM_NOTIFICATION);
             navView.getMenu().getItem(1).setChecked(true);// to show my complaints as selected
             OpenComplaintDetails(new OpenComplaintDetailsEvent(true));
         }



    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OpenComplaintDetails(OpenComplaintDetailsEvent event) {

        if (event.isOpenDetailsFragment())
            navController.navigate(R.id.navigation_complaint_details);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OpenComplaintDetails(OpenMarkCompletedEvent event) {
        if (event.isOpenMarkCompleted())
            navController.navigate(R.id.navigation_complaint_mark_completed);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // this is to handle onbackpress when back button is pressed of toolbar in fragments opened by navigator ( like complaint detail )
        switch (item.getItemId()) {
            case android.R.id.home:
                if (Common.has_User_Pressed_Back_Button_on_Acknowledgement_Screen)
                {


                    NavOptions.Builder navBuilder = new NavOptions.Builder();
                    NavOptions navOptions = navBuilder.setPopUpTo(R.id.navigation_complaint_closing_acknowledge,true).build();
                    NavHostFragment.findNavController(Common.fragment_acknowledge).navigate(R.id.navigation_dashboard,null,navOptions);

                    // below method is used to navigate without pushing fragment in backstack and using mobile navigation direction

                    Common.has_User_Pressed_Back_Button_on_Acknowledgement_Screen =false;


                    ActionBar actionBar = getSupportActionBar();
                    if (actionBar != null) {
                        actionBar.show();
                    }


                }
                else
                    onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onBackPressed() {
        if (Common.has_User_Pressed_Back_Button_on_Acknowledgement_Screen)
        {
            // below method is used to navigate without pushing fragment in backstack and using mobile navigation direction
            NavOptions.Builder navBuilder = new NavOptions.Builder();
            NavOptions navOptions = navBuilder.setPopUpTo(R.id.navigation_complaint_closing_acknowledge,true).build();
            NavHostFragment.findNavController(Common.fragment_acknowledge).navigate(R.id.navigation_complaint_details,null,navOptions);



            Common.has_User_Pressed_Back_Button_on_Acknowledgement_Screen =false;

            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }

        }
        else
            super.onBackPressed();
    }


}