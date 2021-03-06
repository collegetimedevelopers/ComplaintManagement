package ac.sliet.complaintmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.chaos.view.PinView;
import com.google.android.gms.auth.api.phone.SmsCodeAutofillClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import ac.sliet.complaintmanagement.Common.Common;
import ac.sliet.complaintmanagement.Model.UserModel;
import ac.sliet.complaintmanagement.UI.MainActivity;
import ac.sliet.complaintmanagement.UI.SignUpActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

public class VerifyPhoneActivity extends AppCompatActivity {


    PhoneAuthProvider.ForceResendingToken forceResendingToken;
    PhoneAuthProvider phoneAuthProvider;
    PhoneAuthCredential phoneAuthCredential;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks onVerificationStateChangedCallbacks;
    PhoneAuthOptions options;


    String phoneNumber, verificationID;


    Unbinder unbinder;

    @BindView(R.id.verify_phone_btn_verify)
    MaterialButton btn_verify;

    @BindView(R.id.verify_phone_txt_send_otp)
    TextView sendOtp;

    @BindView(R.id.verify_phone_til_number)
    TextInputLayout numberLayout;

    @BindView(R.id.verify_phone_pinView)
    PinView pinView;

    @BindView(R.id.verify_phone_progress)
    ProgressBar progressBar;


    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);


        unbinder = ButterKnife.bind(this);
        firebaseAuth = FirebaseAuth.getInstance();


        onVerificationStateChangedCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                System.out.println("In verification completed    ");

                loginUser(phoneAuthCredential);

                if (null != phoneAuthCredential.getSmsCode())
                    pinView.setText(phoneAuthCredential.getSmsCode());


            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(VerifyPhoneActivity.this, "error = " + e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken resendingToken) {

                forceResendingToken = resendingToken;
                verificationID = verificationId;

                sendOtp.setText("Resend OTP");

                TSnackbar snackbar = TSnackbar.make(findViewById(android.R.id.content), "Code Sent", TSnackbar.LENGTH_LONG);
                View snackbarView = snackbar.getView();
                snackbarView.setBackgroundColor(Color.parseColor("#12B517"));
                TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                textView.setGravity(Gravity.CENTER);
                snackbar.show();
                progressBar.setVisibility(View.GONE);
            }
        };



        setListeners();
    }


    private void loginUser(PhoneAuthCredential phoneAuthCredential) {

        firebaseAuth.signInWithCredential(phoneAuthCredential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        Common.showSnackBarAtTop("Number Verified Successfully", "#12B517"
                                , Color.WHITE, VerifyPhoneActivity.this);

                        checkUserInDatabase();


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);

            }
        });
    }


    private void checkUserInDatabase() {

        FirebaseFirestore.getInstance().collection(Common.USERS_COLLECTION_REF).document(firebaseAuth.getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    UserModel userModel = documentSnapshot.toObject(UserModel.class);
                    Common.currentUser = userModel;
                    //user exists goto home
                    Common.updateToken(VerifyPhoneActivity.this);

                    startActivity(new Intent(VerifyPhoneActivity.this, MainActivity.class));
                } else {
                    // user doesn't exist , needs to sign up

                    startActivity(new Intent(VerifyPhoneActivity.this, SignUpActivity.class));

                }
                progressBar.setVisibility(View.GONE);

                finish();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);

                        Common.showSnackBarAtTop("Some Error occurred", Common.ERROR_COLOR, Color.WHITE, VerifyPhoneActivity.this);
                    }
                });

    }


    private void setListeners() {


        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOTPToUser();
            }
        });


        Objects.requireNonNull(numberLayout.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 10) {

                    sendOtp.setVisibility(View.VISIBLE);

                    phoneNumber = "+91" +
                            numberLayout.getEditText()
                                    .getText().toString();

                } else
                    sendOtp.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (verificationID != null && !Objects.requireNonNull(pinView.getText()).toString().isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);

                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationID, pinView.getText().toString());
                    loginUser(phoneAuthCredential);
                } else {
                    if (verificationID == null)
                        Common.showSnackBarAtTop("Please Send OTP First", "#BF0101", Color.WHITE, VerifyPhoneActivity.this);

                    if (Objects.requireNonNull(pinView.getText()).toString().isEmpty()) {
                        Common.showSnackBarAtTop("Please Enter OTP First", "#BF0101", Color.WHITE, VerifyPhoneActivity.this);

                    }
                }

            }
        });
    }


    private void sendOTPToUser() {
        progressBar.setVisibility(View.VISIBLE);

        options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneNumber)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(VerifyPhoneActivity.this)// Activity (for callback binding)

                .setCallbacks(onVerificationStateChangedCallbacks)          // OnVerificationStateChangedCallbacks
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }


}
