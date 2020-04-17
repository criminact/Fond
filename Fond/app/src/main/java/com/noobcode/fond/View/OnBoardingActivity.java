package com.noobcode.fond.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.noobcode.fond.R;

public class OnBoardingActivity extends AppCompatActivity {

    EditText name;
    Button go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        name = (EditText) findViewById(R.id.nameField);
        go = (Button) findViewById(R.id.nextone);

        name.setText(getIntent().getStringExtra("name"));

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().isEmpty() || name.getText().toString() == null){
                    name.setError("Name can't be empty.");
                }else{
                    Intent intent = new Intent(OnBoardingActivity.this, AgeAndGender.class);
                    intent.putExtra("name",name.getText().toString().trim());
                    intent.putExtra("id", getIntent().getStringExtra("id"));
                    intent.putExtra("userId", getIntent().getStringExtra("userId"));
                    intent.putExtra("facebookdp", getIntent().getStringExtra("facebookdp"));
                    startActivity(intent);
                }
            }
        });

    }
}
