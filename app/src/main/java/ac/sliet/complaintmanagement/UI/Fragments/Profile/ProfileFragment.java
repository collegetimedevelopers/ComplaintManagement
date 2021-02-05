package ac.sliet.complaintmanagement.UI.Fragments.Profile;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import ac.sliet.complaintmanagement.Common.Common;
import ac.sliet.complaintmanagement.Model.UserModel;
import ac.sliet.complaintmanagement.R;
import ac.sliet.complaintmanagement.UI.MainActivity;
import ac.sliet.complaintmanagement.UI.SignUpActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    Unbinder unbinder;


    @BindView(R.id.profileSpinner)
    Spinner houseCategorySpinner;

    @BindView(R.id.profile_btn_updte)
    MaterialButton btnUpdate;

    @BindView(R.id.profileHouseNumber)
    TextInputLayout houseNumber;

    @BindView(R.id.profileEmail)
    TextInputLayout userEmail;

    @BindView(R.id.profileName)
    TextInputLayout userName;


    @BindView(R.id.profileTypeNumber)
    TextInputLayout typeNumber;

    @BindView(R.id.profile_txt_contact)
    TextView phoneNumber;


    @BindView(R.id.profile_txt_update_contact)
    TextView updatePhoneNumber;


    @BindView(R.id.profile_btn_cancel)
    Button btn_cancel_number_update;
    @BindView(R.id.profile_ccp)
    CountryCodePicker countryCodePicker;
    @BindView(R.id.profile_new_number)
    TextInputEditText new_number;

    @BindView(R.id.profile_update_layout)
    LinearLayout number_update_layout;


    @BindView(R.id.profile_btn_verify_code)
    Button btn_verify_code;
    @BindView(R.id.profile_otp)
    PinView otp_code;

    @BindView(R.id.profile_card_contact)
    CardView update_number_parent_card;


    @BindView(R.id.profile_progress_code)
    ProgressBar progress_code;

    @BindView(R.id.profile_resend_code)
    TextView txt_resend_code;


    @BindView(R.id.profile_txt_otp_status)
    TextView txt_otp_status;

    String uid = FirebaseAuth.getInstance().getUid();

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    PhoneAuthProvider.ForceResendingToken forceResendingToken;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks onVerificationStateChangedCallbacks;

    String newNumber, verificationId;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        profileViewModel.getUserModelMutableLiveData().observe(getViewLifecycleOwner(), new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {
                setDataToFields(userModel);
            }
        });
        unbinder = ButterKnife.bind(this, root);


        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.house_category));

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        houseCategorySpinner.setAdapter(spinnerAdapter);

        setListeners();


        onVerificationStateChangedCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                updatePhoneNumber(phoneAuthCredential);
                Toast.makeText(getContext(),"Verifidation Completed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Common.showSnackBarAtTop("Some Error Occurred",Common.ERROR_COLOR,Color.WHITE,getActivity());
            }

            @Override
            public void onCodeSent(@NonNull String verificationID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingT) {
                // super.onCodeSent(s, forceResendingToken);
                verificationId = verificationID;
                forceResendingToken = forceResendingT;


            }
        };

        return root;
    }

    private void setListeners() {

        updatePhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showUpdateLayout(v);

            }
        });


        btn_cancel_number_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideUpdateLayout();
            }
        });

        // to update other data ( contact has to be updated separately
        btnUpdate.setOnClickListener(new View.OnClickListener() {
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
                userModel.setEmail(userEmail.getEditText().getText().toString().trim());
                userModel.setUserName(userName.getEditText().getText().toString().trim());
                userModel.setUid(uid);
                userModel.setAddress(houseNumber.getEditText().getText().toString() + ", Type " + typeNumber.getEditText().getText().toString() + ", " + houseCategorySpinner.getSelectedItem().toString());

                uploadDataToFirestore(userModel);
            }
        });
    }


    private void hideUpdateLayout() {
        new_number.setText("");
        otp_code.setText("");
        txt_resend_code.setVisibility(View.GONE);
        progress_code.setVisibility(View.GONE);

        txt_otp_status.setText("Enter OTP Code");
        otp_code.setLineColor(Color.parseColor("#AEACAC"));

        updatePhoneNumber.setVisibility(View.VISIBLE);

        updatePhoneNumber.animate()
                .translationY(0.0f)
                .alpha(1.0f)
                .setDuration(700).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                number_update_layout.animate().translationY(number_update_layout.getHeight()).setDuration(700);

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                number_update_layout.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }

        });

    }


    private void showUpdateLayout(View view) {
        view.animate()
                .translationY(view.getHeight())
                .alpha(0.0f)
                .setDuration(700).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                // to slide up  from bottom

                number_update_layout.setVisibility(View.VISIBLE);
                number_update_layout.animate().translationY(0.0f).setDuration(700);

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                updatePhoneNumber.setVisibility(View.GONE);


            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }

        });
    }


    private void setDataToFields(UserModel userModel) {

        userName.getEditText().setText(userModel.getUserName());
        userEmail.getEditText().setText(userModel.getEmail());
        houseCategorySpinner.setSelection(userModel.getAddress().contains("Old") ? 0 : 1, true);

        String[] split = userModel.getAddress().split("Type");
        String userHouseNumber = split[0].replace(",", "");
        String userTypeNumber = split[1].substring(0, split[1].indexOf(","));

        phoneNumber.setText(userModel.getPhoneNumber());
        typeNumber.getEditText().setText(userTypeNumber.trim());
        houseNumber.getEditText().setText(userHouseNumber.trim());
    }


    private void uploadDataToFirestore(UserModel userModel) {


        FirebaseFirestore.getInstance().collection(Common.USERS_COLLECTION_REF).document(uid).set(userModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Common.showSnackBarAtTop("Updated Successfully", Common.GREEN_COLOR, Color.WHITE, getActivity());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                })
        ;
    }


    @OnTextChanged(R.id.profile_new_number)
    public void onNumberEntered() {

        String number = new_number.getText().toString();
        String ccp = countryCodePicker.getDefaultCountryCodeWithPlus();

        if (number.length() == 10) {
            newNumber = ccp + number;
            progress_code.setVisibility(View.VISIBLE);

            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(firebaseAuth)
                            .setPhoneNumber(newNumber)       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(getActivity())                 // Activity (for callback binding)
                            .setCallbacks(onVerificationStateChangedCallbacks)          // OnVerificationStateChangedCallbacks
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        }
    }



    @OnClick(R.id.profile_resend_code)
    public void onClickResendCode() {
        String number = new_number.getText().toString();
        String ccp = countryCodePicker.getDefaultCountryCodeWithPlus();

        if (number.length() == 10) {
            newNumber = ccp + number;
            progress_code.setVisibility(View.VISIBLE);
            txt_resend_code.setVisibility(View.GONE);
            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(firebaseAuth)
                            .setPhoneNumber(newNumber)       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(getActivity())                 // Activity (for callback binding)
                            .setCallbacks(onVerificationStateChangedCallbacks)          // OnVerificationStateChangedCallbacks
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);

            Common.showSnackBarAtTop("Sending Verification Code...", Common.BLUE_COLOR, Color.WHITE, getActivity());
        }
    }


    @OnClick(R.id.profile_btn_verify_code)
    public void onClickBtn_verify_code() {
        if (otp_code.getText().toString().length() == 6 && new_number.getText().toString().length() == 10) {

            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, otp_code.getText().toString());
            updatePhoneNumber(phoneAuthCredential);

        } else if (new_number.getText().toString().length() == 10) {
            Snackbar.make(update_number_parent_card, "Please Enter a Valid Number", Snackbar.LENGTH_LONG).setTextColor(Color.RED).show();
            return;
        } else {
            Snackbar.make(update_number_parent_card, "Please Enter a Valid OTP", Snackbar.LENGTH_LONG).setTextColor(Color.RED).show();
            return;
        }
    }




    private void updatePhoneNumber(PhoneAuthCredential phoneAuthCredential) {


        FirebaseAuth.getInstance().getCurrentUser().updatePhoneNumber(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // now setting up values in  database

                    Common.showSnackBarAtTop("Updating Number...", Common.GREEN_COLOR, Color.WHITE, getActivity());

                    otp_code.setLineColor(Color.GREEN);

                    FirebaseAuth.getInstance().getCurrentUser().reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Common.currentUser.setPhoneNumber(newNumber);

                            UserModel userModel = new UserModel();
                            userModel.setPhoneNumber(newNumber);
                            userModel.setEmail(userEmail.getEditText().getText().toString());
                            userModel.setUserName(userName.getEditText().getText().toString());
                            userModel.setUid(uid);
                            userModel.setAddress(houseNumber.getEditText().getText().toString() + ", Type " + typeNumber.getEditText().getText().toString() + ", " + houseCategorySpinner.getSelectedItem().toString());


                            FirebaseFirestore.getInstance().collection(Common.USERS_COLLECTION_REF).document(uid).set(userModel)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Common.currentUser.setPhoneNumber(newNumber);
                                            phoneNumber.setText(newNumber);

                                            Common.showSnackBarAtTop("Number Updated Successfully", Common.GREEN_COLOR, Color.WHITE, getActivity());

                                            progress_code.setVisibility(View.GONE);
                                            hideUpdateLayout();
                                            Snackbar.make(update_number_parent_card, "Phone Number Updated Successfully", Snackbar.LENGTH_LONG).setTextColor(Color.GREEN).show();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Common.showSnackBarAtTop("Some Error Occurred", Common.ERROR_COLOR, Color.WHITE, getActivity());
                                            progress_code.setVisibility(View.GONE);
                                            txt_resend_code.setVisibility(View.VISIBLE);

                                        }
                                    })
                            ;
                        }
                    });


                    // new code


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress_code.setVisibility(View.GONE);
           //     updateSnackbar.dismiss();
                txt_resend_code.setVisibility(View.VISIBLE);

                Snackbar snackbar = Snackbar.make(update_number_parent_card, "Failed to Update Number," + e.getMessage() + ", Please Try Again....", Snackbar.LENGTH_INDEFINITE).setTextColor(Color.RED);
                snackbar.setAction("Okay", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
                snackbar.setActionTextColor(Color.GREEN);
                snackbar.show();

            }
        });

    }
}