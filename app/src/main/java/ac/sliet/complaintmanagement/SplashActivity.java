package ac.sliet.complaintmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import ac.sliet.complaintmanagement.Common.Common;
import ac.sliet.complaintmanagement.Model.UserModel;
import ac.sliet.complaintmanagement.UI.MainActivity;
import ac.sliet.complaintmanagement.UI.SignUpActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    Unbinder unbinder;

    @BindView(R.id.splashProgress)
    ProgressBar contentLoadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        unbinder = ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        contentLoadingProgressBar.setVisibility(View.VISIBLE);

        if (firebaseUser == null) {
            startActivity(new Intent(SplashActivity.this, VerifyPhoneActivity.class));

        } else {
            // checkUserInDatabase: user is logged in already
            checkUserInDatabase();
        }

    }


    private void checkUserInDatabase() {

        FirebaseFirestore.getInstance().collection(Common.USERS_COLLECTION_REF).document(firebaseAuth.getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    UserModel userModel = documentSnapshot.toObject(UserModel.class);
                    Common.currentUser = userModel;
                    Common.updateToken(SplashActivity.this);

                    Intent mainActivityIntent = new Intent(SplashActivity.this, MainActivity.class);

                    if (getIntent().getBooleanExtra(Common.IS_OPENED_FROM_NOTIFICATION, false)) {
                        // putting these values to handle notification click so that we can automatically open details fragment

                        mainActivityIntent.putExtra(Common.IS_OPENED_FROM_NOTIFICATION, true);
                        mainActivityIntent.putExtra(Common.COMPLAINT_ID_FROM_NOTIFICATION, getIntent().getStringExtra(Common.COMPLAINT_ID_FROM_NOTIFICATION));

                    }

                    //user exists goto home
                    startActivity(mainActivityIntent);
                    finish();

                } else {
                    // user doesn't exist , needs to sign up

                    startActivity(new Intent(SplashActivity.this, SignUpActivity.class));

                    finish();
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Common.showSnackBarAtTop("Some Error occurred", Common.ERROR_COLOR, Color.WHITE, SplashActivity.this);
                    }
                });

    }
}