package com.noobcode.fond.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.noobcode.fond.R;

public class InterestAndLookingFor extends AppCompatActivity {

    TextView fondInterestIntro;
    Spinner lookingForField;
    Spinner wishToSeeField;
    Button go;
    String lookingFor;
    String matchWith;
    boolean isSelectedLookingFor;
    boolean isSelectedMatchWith;
    int intSelectedLookingFor;
    int intSelectedMatchWith;

    String[] lookingfor = new String[]{
            "Select a Category",
            "Marriage",
            "Serious Relationship",
            "Casual Dating",
            "Not sure"
    };

    String[] matchwith = new String[]{
            "Select a Category",
            "Men",
            "Women",
            "Other Genders",
            "All Genders",
            "Both, Men and Women"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_and_looking_for);

        fondInterestIntro = (TextView) findViewById(R.id.fondInterestIntro);
        lookingForField = (Spinner) findViewById(R.id.lookingForField);
        wishToSeeField = (Spinner) findViewById(R.id.wishToSeeField);
        go = (Button) findViewById(R.id.nextthree);

        fondInterestIntro.setText(getIntent().getStringExtra("name") + ", What are looking from this app?");

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinnercategorieslayout, lookingfor
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.whitespinnerlayout);
        lookingForField.setAdapter(spinnerArrayAdapter);

        lookingForField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                if (selectedItem.equals("Select a Category")) {
                    isSelectedLookingFor = false;
                    intSelectedLookingFor = -1;
                } else {
                    isSelectedLookingFor = true;
                    intSelectedLookingFor = i;
                    switch (intSelectedLookingFor) {
                        case -1: {
                            isSelectedLookingFor = false;
                            intSelectedLookingFor = -1;
                        }
                        break;
                        case 0: {
                            isSelectedLookingFor = false;
                            intSelectedLookingFor = -1;
                        }
                        break;
                        case 1: {
                            isSelectedLookingFor = true;
                            intSelectedLookingFor = 1;
                            lookingFor = "Marriage";
                        }
                        break;
                        case 2: {
                            isSelectedLookingFor = true;
                            intSelectedLookingFor = 2;
                            lookingFor = "Serious Relationship";
                        }
                        break;
                        case 3: {
                            isSelectedLookingFor = true;
                            intSelectedLookingFor = 3;
                            lookingFor = "Casual Dating";
                        }
                        break;
                        case 4: {
                            isSelectedLookingFor = true;
                            intSelectedLookingFor = 4;
                            lookingFor = "Not sure";
                        }
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                isSelectedLookingFor = false;
                intSelectedLookingFor = -1;
            }
        });

        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(
                this, R.layout.spinnercategorieslayout, matchwith
        );
        spinnerArrayAdapter2.setDropDownViewResource(R.layout.whitespinnerlayout);
        wishToSeeField.setAdapter(spinnerArrayAdapter2);

        wishToSeeField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                if (selectedItem.equals("Select a Category")) {
                    isSelectedMatchWith = false;
                    intSelectedMatchWith = -1;
                } else {
                    isSelectedMatchWith = true;
                    intSelectedMatchWith = i;
                    switch (intSelectedMatchWith) {
                        case -1: {
                            isSelectedMatchWith = false;
                            intSelectedMatchWith = -1;
                        }
                        break;
                        case 0: {
                            isSelectedMatchWith = false;
                            intSelectedMatchWith = -1;
                        }
                        break;
                        case 1: {
                            isSelectedMatchWith = true;
                            intSelectedMatchWith = 1;
                            matchWith = "Men";
                        }
                        break;
                        case 2: {
                            isSelectedMatchWith = true;
                            intSelectedMatchWith = 2;
                            matchWith = "Women";
                        }
                        break;
                        case 3: {
                            isSelectedMatchWith = true;
                            intSelectedMatchWith = 3;
                            matchWith = "Other Genders";
                        }
                        break;
                        case 4: {
                            isSelectedMatchWith = true;
                            intSelectedMatchWith = 4;
                            matchWith = "All Genders";
                        }
                        break;
                        case 5: {
                            isSelectedMatchWith = true;
                            intSelectedMatchWith = 5;
                            matchWith = "Both, Men and Women";
                        }
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                isSelectedMatchWith = false;
                intSelectedMatchWith = -1;
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intSelectedLookingFor == -1 || !isSelectedLookingFor) {
                    Toast.makeText(InterestAndLookingFor.this, "Please select what are you looking for.", Toast.LENGTH_SHORT).show();
                } else if (intSelectedMatchWith == -1 || !isSelectedMatchWith) {
                    Toast.makeText(InterestAndLookingFor.this, "Please select which people to meet with.", Toast.LENGTH_SHORT).show();
                } else if (intSelectedMatchWith == -1 && intSelectedLookingFor == -1) {
                    Toast.makeText(InterestAndLookingFor.this, "Please Fill the form correctly.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(InterestAndLookingFor.this, LocationActivity.class);
                    intent.putExtra("matchWith", matchWith);
                    intent.putExtra("lookingFor", lookingFor);
                    intent.putExtra("userId", getIntent().getStringExtra("userId"));
                    intent.putExtra("facebookdp", getIntent().getStringExtra("facebookdp"));
                    intent.putExtra("id", getIntent().getStringExtra("id"));
                    intent.putExtra("age", getIntent().getStringExtra("age"));
                    intent.putExtra("gender", getIntent().getStringExtra("gender"));
                    intent.putExtra("name", getIntent().getStringExtra("name"));
                    startActivity(intent);
                }
            }
        });
    }
}