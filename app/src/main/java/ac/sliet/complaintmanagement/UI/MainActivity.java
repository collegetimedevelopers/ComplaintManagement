package ac.sliet.complaintmanagement.UI;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ac.sliet.complaintmanagement.Events.OpenComplaintDetailsEvent;
import ac.sliet.complaintmanagement.Events.OpenMarkCompletedEvent;
import ac.sliet.complaintmanagement.R;

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
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
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
        super.onBackPressed();
    }
}