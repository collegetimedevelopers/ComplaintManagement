package ac.sliet.complaintmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.chaos.view.PinView;
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

import java.util.concurrent.TimeUnit;

import ac.sliet.complaintmanagement.Common.Common;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

public class VerifyPhoneActivity extends AppCompatActivity {


    PhoneAuthProvider.ForceResendingToken forceResendingToken;
    PhoneAuthProvider phoneAuthProvider;
    PhoneAuthCredential phoneAuthCredential;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks onVerificationStateChangedCallbacks;

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
                Toast.makeText(getApplicationContext(), "verified", Toast.LENGTH_SHORT).show();
                loginUser(phoneAuthCredential);


            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(VerifyPhoneActivity.this, "error = " + e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                forceResendingToken = forceResendingToken;
                verificationID = verificationId;

                Toast.makeText(getApplicationContext(), "code sent", Toast.LENGTH_SHORT).show();
                TSnackbar snackbar = TSnackbar.make(findViewById(android.R.id.content), "Code Sent", TSnackbar.LENGTH_LONG);
                // snackbar.setActionTextColor(Color.WHITE);
                View snackbarView = snackbar.getView();
                snackbarView.setBackgroundColor(Color.parseColor("#12B517"));
                TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                textView.setGravity(Gravity.CENTER);
                snackbar.show();

            }
        };

        setListeners();
    }

    private void loginUser(PhoneAuthCredential phoneAuthCredential) {

        firebaseAuth.signInWithCredential(phoneAuthCredential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
//
//                        TSnackbar snackbar = TSnackbar.make(findViewById(android.R.id.content), "Logged In Successfully", TSnackbar.LENGTH_LONG);
//                        // snackbar.setActionTextColor(Color.WHITE);
//                        View snackbarView = snackbar.getView();
//                        snackbarView.setBackgroundColor(Color.parseColor("#12B517"));
//                        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
//                        textView.setTextColor(Color.YELLOW);
//                        snackbar.show();

                        Common.showSnackBarAtTop("Logged In Successfully", "#12B517",Color.WHITE, VerifyPhoneActivity.this);


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

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


        numberLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 10) {

                    sendOtp.setVisibility(View.VISIBLE);

                    phoneNumber = new StringBuilder("+91")
                            .append(numberLayout.getEditText()
                                    .getText().toString()).toString();

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

                if (verificationID != null && !pinView.getText().toString().isEmpty()) {
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationID, pinView.getText().toString());
                    loginUser(phoneAuthCredential);
                } else {
                    if (verificationID == null)
                        Common.showSnackBarAtTop("Please Send OTP First", "#BF0101",Color.WHITE, VerifyPhoneActivity.this);

                    if (pinView.getText().toString().isEmpty()) {
                        Common.showSnackBarAtTop("Please Enter OTP First", "#BF0101", Color.WHITE,VerifyPhoneActivity.this);

                    }
                }

            }
        });
    }


    private void sendOTPToUser() {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(onVerificationStateChangedCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


}
