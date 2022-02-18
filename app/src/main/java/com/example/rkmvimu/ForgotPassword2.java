package com.example.rkmvimu;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class ForgotPassword2 extends AppCompatActivity {

    private DatabaseReference ref2;
    ValueEventListener postListener;
    String num="";
    int invalid=0;
    int x=0;
    String otp;

    ProgressBar progressBar;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    public FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password2);
        progressBar=findViewById(R.id.number_check);
        progressBar.setVisibility(View.VISIBLE);

        invalid=1;

        Button b1=findViewById(R.id.requestOTP);
        b1.setBackgroundColor(getResources().getColor(R.color.button));
        Button b2=findViewById(R.id.nxt);
        b2.setBackgroundColor(getResources().getColor(R.color.button));

        Bundle bundle=getIntent().getExtras();
        num=bundle.getString("num");

        ref2 = FirebaseDatabase.getInstance().getReference().child("Users").child(num);
        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    invalid=1;
                    progressBar.setVisibility(View.INVISIBLE);
                    TextView tv=findViewById(R.id.text);
                    tv.setText("No account exists with the given number ❗ "+"\n"+"Please create new account. "+"\n"+"You will NOT receive an OTP ❗ ");
                    Button b1=findViewById(R.id.requestOTP);
                    b1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.button));
                    Button b2=findViewById(R.id.nxt);
                    b2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.button));
                    Toast.makeText(com.example.rkmvimu.ForgotPassword2.this,"Create new account.",Toast.LENGTH_SHORT).show();
                }
                else {
                    Button b1=findViewById(R.id.requestOTP);
                    b1.setBackground(getResources().getDrawable(R.drawable.buttonview));

                    Button b2=findViewById(R.id.nxt);
                    b2.setBackground(getResources().getDrawable(R.drawable.buttonview));

                    progressBar.setVisibility(View.INVISIBLE);
                    TextView tv=findViewById(R.id.text);
                    tv.setText("Click on GET OTP.");
                    invalid=0;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        ref2.addValueEventListener(postListener);

    }


    public void getOTP(View v)
    {
        if(invalid==1)
        {
            Toast.makeText(com.example.rkmvimu.ForgotPassword2.this,"Invalid.",Toast.LENGTH_SHORT).show();
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            startFirebaseLogin();

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    num,                     // Phone number to verify
                    60,                           // Timeout duration
                    TimeUnit.SECONDS,                // Unit of timeout
                    com.example.rkmvimu.ForgotPassword2.this,        // Activity (for callback binding)
                    mCallbacks);
            x=1;
            progressBar.setVisibility(View.INVISIBLE);
            Button b1=findViewById(R.id.requestOTP);
            b1.setBackgroundColor(getResources().getColor(R.color.button));
        }
    }

    public void next(View v)
    {
        TextView tv=findViewById(R.id.some_text);
        tv.setText("");

        progressBar.setVisibility(View.VISIBLE);

        EditText et=findViewById(R.id.OTP_number);
        otp=et.getText().toString().trim();

        if(invalid==1 || x==0)
        {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(com.example.rkmvimu.ForgotPassword2.this,"Invalid.",Toast.LENGTH_SHORT).show();
        }
        else if(otp.length()<1)
        {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(com.example.rkmvimu.ForgotPassword2.this," OTP too small ! ",Toast.LENGTH_SHORT).show();
        }
        else
            {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
                verifyOTP(credential);
        }
    }

    public void b2(View v)
    {
        finish();
    }

    public void startFirebaseLogin()
    {
        auth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) { }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Toast tst=Toast.makeText(com.example.rkmvimu.ForgotPassword2.this,"Verification Failed.",Toast.LENGTH_SHORT);
                tst.show();

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    tst=Toast.makeText(com.example.rkmvimu.ForgotPassword2.this,"Invalid request.",Toast.LENGTH_SHORT);
                    tst.show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    tst=Toast.makeText(com.example.rkmvimu.ForgotPassword2.this,"Too many users.",Toast.LENGTH_SHORT);
                    tst.show();
                }

                startActivity(new Intent(com.example.rkmvimu.ForgotPassword2.this,LoginActivity.class));
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                TextView tv=findViewById(R.id.some_text);
                tv.setText("OTP Sent."+"\n"+"Please wait for a few seconds for the OTP to reach your device.");

                mVerificationId = verificationId;
                mResendToken = token;

                Button b1=findViewById(R.id.requestOTP);
                b1.setBackgroundColor(getResources().getColor(R.color.button));
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(com.example.rkmvimu.ForgotPassword2.this,"Already Requested",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
    }

    public void verifyOTP(PhoneAuthCredential credential)
    {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast t=Toast.makeText(com.example.rkmvimu.ForgotPassword2.this,"OTP verification successful",Toast.LENGTH_SHORT);
                                t.show();

                            Intent intent = new Intent(com.example.rkmvimu.ForgotPassword2.this, NewPassword.class);
                            intent.putExtra("number", num);
                            startActivity(intent);

                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(com.example.rkmvimu.ForgotPassword2.this,"Incorrect OTP",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}