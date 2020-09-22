package xyz.adamkovacs.nflweeklyfantasy.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import xyz.adamkovacs.nflweeklyfantasy.Database.NFLDatabaseHelper;
import xyz.adamkovacs.nflweeklyfantasy.R;

public class SignUpActivity extends AppCompatActivity {

    TextView tv_login;
    Button btn_signup;
    EditText et_username, et_email, et_password;
    NFLDatabaseHelper dbHelper;
    boolean isUserAlreadyRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        tv_login = findViewById(R.id.tv_signup_login);
        btn_signup = findViewById(R.id.btn_signup_register);
        et_username = findViewById(R.id.et_signup_username);
        et_email = findViewById(R.id.et_signup_email);
        et_password = findViewById(R.id.et_signup_password);
        dbHelper = new NFLDatabaseHelper(this);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_username.getText().toString() == "" || et_email.getText().toString().equals("") || et_password.getText().toString().equals("")){
                    Toast.makeText(v.getContext(), "Error: Fill in all the fields to register", Toast.LENGTH_SHORT).show();
                }
                if(!isValidEmail(et_email.getText().toString())){
                    Toast.makeText(v.getContext(), "Error: Email address is not valid!", Toast.LENGTH_SHORT).show();
                } else {
                    isUserAlreadyRegistered = dbHelper.isUserInDatabase(et_username.getText().toString(), et_password.getText().toString());
                    if(!isUserAlreadyRegistered){
                        dbHelper.AddUser(et_username.getText().toString(),et_email.getText().toString(),et_password.getText().toString());
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        if(i.resolveActivity(getPackageManager()) != null ) {
                            startActivity(i);
                        }
                    } else {
                        Toast.makeText(v.getContext(), "Error: User already registered. Log in!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                if (i.resolveActivity(getPackageManager()) != null) {
                    startActivity(i);
                }
            }
        });
    }

    //Solution from @AdamvadenHoven from StackOverFlow
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
