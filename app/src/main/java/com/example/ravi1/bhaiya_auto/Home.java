package com.example.ravi1.bhaiya_auto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    private Button signUpBtn, logInBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        addListenerOnButton();

    }
    public void  addListenerOnButton()
    {
        signUpBtn =(Button)findViewById(R.id.signUpButton);
        logInBtn=(Button)findViewById(R.id.logInButton);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent normalSignup = new Intent(v.getContext(), AutoDriverSignUp.class);
                startActivity(normalSignup);
                //Toast.makeText(Home.this,"SignUpButton Clicked",Toast.LENGTH_SHORT).show();
            }
        });

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent login=new Intent(v.getContext(),Login.class);
               startActivity(login);
            }
        });
    }
}
