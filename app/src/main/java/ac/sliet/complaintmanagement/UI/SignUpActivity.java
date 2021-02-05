package ac.sliet.complaintmanagement.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import ac.sliet.complaintmanagement.Common.Common;
import ac.sliet.complaintmanagement.Model.UserModel;
import ac.sliet.complaintmanagement.R;
import ac.sliet.complaintmanagement.VerifyPhoneActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SignUpActivity extends AppCompatActivity {

    Unbinder unbinder;

    @BindView(R.id.signUpSpinner)
    Spinner houseCategorySpinner;

    @BindView(R.id.signUpBtnSignUp)
    MaterialButton btnSignUp;

    @BindView(R.id.signUpHouseNumber)
    TextInputLayout houseNumber;

    @BindView(R.id.signUpEmail)
    TextInputLayout userEmail;

    @BindView(R.id.signUpName)
    TextInputLayout userName;


    @BindView(R.id.signUpTypeNumber)
    TextInputLayout typeNumber;

    String uid = FirebaseAuth.getInstance().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        unbinder = ButterKnife.bind(this);
        Common.showSnackBarAtTop("Sign Up Required", Common.BLUE_COLOR, Color.WHITE, SignUpActivity.this);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(SignUpActivity.this, android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.house_category));

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        houseCategorySpinner.setAdapter(spinnerAdapter);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userName.getEditText().getText().toString().isEmpty()) {
                    userName.setError("Please enter valid name");
                    return;
                }

                if (userEmail.getEditText().getText().toString().isEmpty()) {
                    userEmail.setError("Please enter Email");
                    return;
                }

                if (houseNumber.getEditText().getText().toString().isEmpty()) {
                    houseNumber.setError("Please enter valid House Number");
                    return;
                }

                if (typeNumber.getEditText().getText().toString().isEmpty()) {
                    typeNumber.setError("Please enter valid Type Number");
                    return;
                }


                if (!Patterns.EMAIL_ADDRESS.matcher(userEmail.getEditText().getText().toString()).matches()) {
                    userEmail.setError("Please enter valid Email");
                    return;
                }


                UserModel userModel = new UserModel();
                userModel.setPhoneNumber(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber());
                userModel.setEmail(userEmail.getEditText().getText().toString());
                userModel.setUserName(userName.getEditText().getText().toString());
                userModel.setUid(uid);
                userModel.setAddress(houseNumber.getEditText().getText().toString() + ", Type " + typeNumber.getEditText().getText().toString() + ", " + houseCategorySpinner.getSelectedItem().toString());

                uploadDataToFirestore(userModel);
            }
        });

    }


    private void uploadDataToFirestore(UserModel userModel) {


        FirebaseFirestore.getInstance().collection(Common.USERS_COLLECTION_REF).document(uid).set(userModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Common.showSnackBarAtTop("Signed Up Successfully", Common.GREEN_COLOR, Color.WHITE, SignUpActivity.this);
                        startActivity(new Intent(SignUpActivity.this,MainActivity.class));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Common.showSnackBarAtTop("Some Error Occurred", Common.ERROR_COLOR, Color.WHITE, SignUpActivity.this);

                    }
                })
               ;
    }
}