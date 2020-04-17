package com.noobcode.fond.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.noobcode.fond.R;

public class AgeAndGender extends AppCompatActivity {

    Button go;
    String userGender;
    TextView fondAgeIntro;
    Spinner gender;
    EditText age;
    boolean isSelected = false;
    int intSelected = -1;
    String[] categories = new String[]{
            "Select a Category",
            "Male",
            "Female",
            "Transgender"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_and_gender);

        fondAgeIntro = (TextView) findViewById(R.id.fondAgeIntro);
        age = (EditText) findViewById(R.id.ageField);
        gender = (Spinner) findViewById(R.id.genderField);
        go = (Button) findViewById(R.id.nexttwo);

        fondAgeIntro.setText("What is your age, " + getIntent().getStringExtra("name"));

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinnercategorieslayout, categories
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.whitespinnerlayout);
        gender.setAdapter(spinnerArrayAdapter);

        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                if(selectedItem.equals("Select a Category")) {
                    isSelected = false;
                    intSelected = -1;
                }
                else{
                    isSelected = true;
                    intSelected = i;
                    switch(intSelected){
                        case -1:{
                            isSelected = false;
                            intSelected = -1;
                        }break;
                        case 0:{
                            isSelected = false;
                            intSelected = -1;
                        }break;
                        case 1:{
                            isSelected = true;
                            intSelected = 1;
                            userGender = "Male";
                        }break;
                        case 2:{
                            isSelected = true;
                            intSelected = 2;
                            userGender = "Female";
                        }break;
                        case 3:{
                            isSelected = true;
                            intSelected = 3;
                            userGender = "Transgender";
                        }break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                isSelected = false;
                intSelected = -1;
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isSelected || intSelected == -1){
                    Toast.makeText(AgeAndGender.this, "Please select your gender.", Toast.LENGTH_SHORT).show();
                }else if(age.getText().toString().isEmpty() || age.getText().toString() == null){
                    age.setError("Please give your age.");
                }else if(Integer.valueOf(age.getText().toString()) < 18){
                    age.setError("Age must be 18+");
                }else{
                    Intent intent = new Intent(AgeAndGender.this, InterestAndLookingFor.class);
                    intent.putExtra("name",getIntent().getStringExtra("name"));
                    intent.putExtra("gender",userGender);
                    intent.putExtra("userId", getIntent().getStringExtra("userId"));
                    intent.putExtra("facebookdp", getIntent().getStringExtra("facebookdp"));
                    intent.putExtra("id", getIntent().getStringExtra("id"));
                    intent.putExtra("age",age.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }
}
