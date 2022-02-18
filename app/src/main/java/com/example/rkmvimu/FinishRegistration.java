package com.example.rkmvimu;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rkmvimu.data.ActualJob;
import com.example.rkmvimu.data.BasicInfo;
import com.example.rkmvimu.data.BasicInformationObject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class FinishRegistration extends AppCompatActivity {

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    public FirebaseAuth auth;

    EditText et;
    String str="";
    long newRowId;

    private DatabaseReference ref;
    ProgressBar progressBar;

    String num="",name="",password="",email="",dept="",role="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_registration);
        ref= FirebaseDatabase.getInstance().getReference();

        Bundle bundle = getIntent().getExtras();
        num = bundle.getString("num");
        name=bundle.getString("name");
        password=bundle.getString("password");
        email=bundle.getString("email");
        dept=bundle.getString("dept");
        role=bundle.getString("role");

        startFirebaseLogin();

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    num,                     // Phone number to verify
                    60,                           // Timeout duration
                    TimeUnit.SECONDS,                // Unit of timeout
                    com.example.rkmvimu.FinishRegistration.this,        // Activity (for callback binding)
                    mCallbacks);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public void startFirebaseLogin()
    {
        auth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) { }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Toast tst=Toast.makeText(com.example.rkmvimu.FinishRegistration.this,"Verification Failed.",Toast.LENGTH_SHORT);
                tst.show();

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    tst=Toast.makeText(com.example.rkmvimu.FinishRegistration.this,"Invalid request.",Toast.LENGTH_SHORT);
                    tst.show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    tst=Toast.makeText(com.example.rkmvimu.FinishRegistration.this,"Too many users.",Toast.LENGTH_SHORT);
                    tst.show();
                }

                startActivity(new Intent(com.example.rkmvimu.FinishRegistration.this, com.example.rkmvimu.LoginActivity.class));
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
    }

    public void finishRegistration(View v)
    {
        Button button=findViewById(R.id.nxt);
        button.setBackgroundColor(getResources().getColor(R.color.button));

        progressBar=findViewById(R.id.wait);
        progressBar.setVisibility(View.VISIBLE);

        TextView textView=findViewById(R.id.text_wait);
        textView.setText("");

        et=findViewById(R.id.otp);
        str=et.getText().toString().trim();

        if(str.length()<1)
        {
            Toast t=Toast.makeText(this,"OTP cannot be empty !",Toast.LENGTH_SHORT);
            t.show();
        }
        else
        {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, str);
            verifyOTP(credential);
        }

    }

    public void verifyOTP(PhoneAuthCredential credential)
    {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveInfo();
                            if(newRowId!= -1)
                            {
                                progressBar.setVisibility(View.INVISIBLE);
                                BasicInformationObject bio=new BasicInformationObject(num,name,password,email,dept,role);
                                ref.child("Users").child(num).setValue(bio);

                                startActivity(new Intent(com.example.rkmvimu.FinishRegistration.this,MainActivity.class));

                                Toast t=Toast.makeText(com.example.rkmvimu.FinishRegistration.this,"Created successfully",Toast.LENGTH_SHORT);
                                t.show();
                            }
                            else
                            {
                                Toast t=Toast.makeText(com.example.rkmvimu.FinishRegistration.this,"Unexpected error.",Toast.LENGTH_SHORT);
                                t.show();
                                startActivity(new Intent(com.example.rkmvimu.FinishRegistration.this, com.example.rkmvimu.LoginActivity.class));
                            }
                            finish();
                        } else {
                            Toast.makeText(com.example.rkmvimu.FinishRegistration.this,"Incorrect OTP",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void saveInfo()
    {
        ActualJob dbHelper = new ActualJob(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BasicInfo.EntryColumns.COLUMN_NAME_MOBILE, num);
        values.put(BasicInfo.EntryColumns.COLUMN_NAME_NAME, name);
        values.put(BasicInfo.EntryColumns.COLUMN_NAME_PASSWORD, password);
        values.put(BasicInfo.EntryColumns.COLUMN_NAME_EMAIL,email);
        values.put(BasicInfo.EntryColumns.COLUMN_NAME_DEPARTMENT,dept);
        values.put(BasicInfo.EntryColumns.COLUMN_NAME_ROLE,role);
        values.put(BasicInfo.EntryColumns.COLUMN_NAME_LOCAL_LOGIN,"1");


        newRowId = db.insert(BasicInfo.EntryColumns.TABLE_NAME, null, values);
        db.close();
    }

    public void resendOTP(View v)
    {
        startFirebaseLogin();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                num,                     // Phone number to verify
                60,                           // Timeout duration
                TimeUnit.SECONDS,                // Unit of timeout
                com.example.rkmvimu.FinishRegistration.this,        // Activity (for callback binding)
                mCallbacks);
    }
}