package com.example.cybertestscroll;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PageTwo extends AppCompatActivity implements View.OnClickListener {

    // components linking
    private Button logIn, register;
    private EditText editTextmail, editTextPW;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private TextView forgetPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_two);

        // assign values
        register = (Button) findViewById(R.id.btnReg);
        register.setOnClickListener(this);

        // login button
        logIn = (Button) findViewById(R.id.btnLogIn);
        logIn.setOnClickListener(this);

        // forget password
        forgetPw = (TextView) findViewById(R.id.btnForgetPassword);
        forgetPw.setOnClickListener(this);

        // fire base auth
        mAuth = FirebaseAuth.getInstance();

        // for progress bar
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // text views
        editTextmail = (EditText) findViewById(R.id.txtEmail);
        editTextPW = (EditText) findViewById(R.id.txtPassword);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            /// for register new user
            case R.id.btnReg:
                startActivity(new Intent(this,MainActivity.class));
                break;

            /// log in button
            case R.id.btnLogIn:
                userLogin();
                break;

                // forgot PW link lable
            case R.id.btnForgetPassword:
                fogotPW();
                break;
        }
    }

    private void fogotPW() {

        EditText resetMail = new EditText(this);
        final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(this);
        passwordResetDialog.setTitle("Reset Password..?");
        passwordResetDialog.setMessage("Enter your mail to Received Reset Link");
        passwordResetDialog.setView(resetMail);

        passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String mail = resetMail.getText().toString();
                mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(PageTwo.this,"Reset Link Sent to Your Email", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PageTwo.this,"Error..! Reset Link is not Sent", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

            }
        });

        passwordResetDialog.create().show();
    }

    private void userLogin() {

        // get values
        String email = editTextmail.getText().toString().trim();
        String password = editTextPW.getText().toString().trim();

        //----- validation----
        if (email.isEmpty())
        {
            editTextmail.setError("Email is required..!!");
            editTextmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editTextmail.setError("Please provide valid Email...!");
            editTextmail.requestFocus();
            return;
        }

        if (password.isEmpty())
        {
            editTextPW.setError("Password is required...!");
            editTextPW.requestFocus();
            return;
        }

        if (password.length()<6)
        {
            editTextPW.setError("Min password length should be 6 characters..!");
            editTextPW.requestFocus();
            return;
        }
        //----- end validation-----

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    // get current user
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified())
                    {
                        Toast.makeText(PageTwo.this,"Success..!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(PageTwo.this, Welcome.class);
                        startActivity(intent);
                    }
                    else
                    {
                        user.sendEmailVerification();
                        Toast.makeText(PageTwo.this,"Check your email to verify your account..!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }

                }
                else
                {
                    Toast.makeText(PageTwo.this,"Failed to login..! Try again..!!!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}