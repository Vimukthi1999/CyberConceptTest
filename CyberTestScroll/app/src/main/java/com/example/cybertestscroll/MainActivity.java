package com.example.cybertestscroll;


import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    /// variables
    // for get data from drop boxes
    private  String idType, province, district, city;
    // foe buttons
    private Button registerUser;
    // for edit texts
    private EditText editTextfirstName, editTextlastName, editTextshortName, editTextidNo, editTextaddress1, editTextaddress2, editTextaddress3, editTextpostalCode, editTextcity, editTextemailAddress, editTextconfirmPassword, editTextpassword;
    // progress bar
    private ProgressBar progressBarr;
    // fire base
    private FirebaseAuth mAuth;
    String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //// for get ID from drop down list
        Spinner spinner = findViewById(R.id.idDropDown);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.idTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //// for get Province drop down list
        Spinner spinner2 = findViewById(R.id.provinceDropDown);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.province, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);

        //// for get District from drop down list
        Spinner spinner3 = findViewById(R.id.districtDropDown);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.district, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);
        spinner3.setOnItemSelectedListener(this);

        //// for get City from drop down list
        Spinner spinner4 = findViewById(R.id.cityDropDown);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this, R.array.city, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(adapter4);
        spinner4.setOnItemSelectedListener(this);

        // fire base auth
        mAuth = FirebaseAuth.getInstance();

        // pregress bar
        progressBarr = (ProgressBar) findViewById(R.id.progressBar);

        // save button
        registerUser = (Button) findViewById(R.id.btnSave);
        registerUser.setOnClickListener(this);

        // link edit text boxes
        editTextfirstName = (EditText) findViewById(R.id.txtfirstname);
        editTextlastName = (EditText) findViewById(R.id.txtlastname);
        editTextshortName = (EditText) findViewById(R.id.txtshortname);
        editTextidNo = (EditText) findViewById(R.id.txtidNo);
        editTextaddress1 = (EditText) findViewById(R.id.txtAddress01);
        editTextaddress2 = (EditText) findViewById(R.id.txtAddress02);
        editTextaddress3 = (EditText) findViewById(R.id.txtAddress03);
        editTextpostalCode = (EditText) findViewById(R.id.txtpostalCode);
        editTextcity = (EditText) findViewById(R.id.txtCity);
        editTextemailAddress = (EditText) findViewById(R.id.txtemailAddress);
        editTextconfirmPassword = (EditText) findViewById(R.id.txtcomPassword);
        editTextpassword = (EditText) findViewById(R.id.txtpassword);
    }

    @Override
    public void onClick(View view) {
          registerUser();
    }

    private void registerUser() {

        // get values
        String firstName = editTextfirstName.getText().toString().trim();
        String lastName = editTextlastName.getText().toString().trim();
        String shortName = editTextshortName.getText().toString().trim();
        String NIC = editTextidNo.getText().toString().trim();
        String address01 = editTextaddress1.getText().toString().trim();
        String address02 = editTextaddress2.getText().toString().trim();
        String address03 = editTextaddress3.getText().toString().trim();
        String city = editTextcity.getText().toString().trim();
        String postCode = editTextpostalCode.getText().toString().trim();
        String emailAddress = editTextemailAddress.getText().toString().trim();
        String password = editTextpassword.getText().toString().trim();
        String cmPassword = editTextconfirmPassword.getText().toString().trim();

        //---- validations------
        if (firstName.isEmpty())
        {
            editTextfirstName.setError("Required...!");
            editTextfirstName.requestFocus();
            return;
        }

        if (shortName.isEmpty())
        {
            editTextshortName.setError("Required...!");
            editTextshortName.requestFocus();
            return;
        }

        if (NIC.isEmpty())
        {
            editTextidNo.setError("Required...!");
            editTextidNo.requestFocus();
            return;
        }

        if (address01.isEmpty())
        {
            editTextaddress1.setError("Required...!");
            editTextaddress1.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches())
        {
            editTextemailAddress.setError("Please provide valid Email...!");
            editTextemailAddress.requestFocus();
            return;
        }

        if (password.isEmpty())
        {
            editTextpassword.setError("Required...!");
            editTextpassword.requestFocus();
            return;
        }

        if (password.length()<6)
        {
            editTextpassword.setError("Min password length should be 6 characters..!");
            editTextpassword.requestFocus();
            return;
        }

        if (cmPassword.isEmpty())
        {
            editTextconfirmPassword.setError("Required...!");
            editTextconfirmPassword.requestFocus();
            return;
        }

        //----End validations------

        if (password.equals(cmPassword))
        {
            progressBarr.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(emailAddress,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful())
                            {
                                //
                                UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                User user = new User(firstName, lastName, shortName, idType, NIC, address01, address02, address03, province, district, city, postCode, emailAddress, password, UID);

                                FirebaseDatabase.getInstance().getReference("UsersRegister")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {


                                                if (task.isSuccessful())
                                                {
                                                    Toast.makeText(MainActivity.this,"User has been registered successfully..!", Toast.LENGTH_LONG).show();
                                                    progressBarr.setVisibility(View.GONE);
                                                    Intent intent = new Intent(MainActivity.this, PageTwo.class);
                                                    startActivity(intent);
                                                }
                                                else
                                                {
                                                    Toast.makeText(MainActivity.this,"Error"+task.getException(), Toast.LENGTH_LONG).show();
                                                    progressBarr.setVisibility(View.GONE);
                                                }
                                            }
                                        });
                            }
                            else
                            {
                                //Toast.makeText(register.this,"User has already registered..!", Toast.LENGTH_LONG).show();
                                Toast.makeText(MainActivity.this,"Error"+task.getException(), Toast.LENGTH_LONG).show();
                                progressBarr.setVisibility(View.GONE);
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(MainActivity.this,"Password did not matched..!!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch (adapterView.getId()){
            case R.id.idDropDown:
                idType = adapterView.getItemAtPosition(i).toString();
                break;

            case R.id.provinceDropDown:
                province = adapterView.getItemAtPosition(i).toString();
                break;

            case R.id.districtDropDown:
                district = adapterView.getItemAtPosition(i).toString();
                break;

            case R.id.cityDropDown:
                city = adapterView.getItemAtPosition(i).toString();
                if (city.equals("Other"))
                {
                    editTextcity.setEnabled(true);      //// enable edit text box for custom cities
                    editTextcity.setText("");
                }
                else
                {
                    editTextcity.setText(city);
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}