package xyz.adamkovacs.nflweeklyfantasy.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import xyz.adamkovacs.nflweeklyfantasy.Database.NFLDatabaseHelper;
import xyz.adamkovacs.nflweeklyfantasy.R;

public class MainActivity extends AppCompatActivity {
    EditText et_username, et_password;
    Button btn_Login;
    TextView tv_signUp;
    String username;
    String password;
    NFLDatabaseHelper dbHelper;
    boolean isUserRegistered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_username = findViewById(R.id.et_main_login_username);
        et_password = findViewById(R.id.et_main_login_password);
        btn_Login = findViewById(R.id.btn_main_login);
        tv_signUp = findViewById(R.id.tv_main_signup);
        dbHelper = new NFLDatabaseHelper(this);

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = et_username.getText().toString();
                password = et_password.getText().toString();
                isUserRegistered = dbHelper.isUserInDatabase(username,password);
                if(isUserRegistered){
                    Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
                    i.putExtra("Username", username);
                    if(i.resolveActivity(getPackageManager()) != null){
                        startActivity(i);
                    }
                } else {
                    Toast.makeText(v.getContext(), "Username/password is not correct!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        

        tv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
                if (i.resolveActivity(getPackageManager()) != null) {
                    startActivity(i);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Cant go back.", Toast.LENGTH_SHORT).show();
    }
}
