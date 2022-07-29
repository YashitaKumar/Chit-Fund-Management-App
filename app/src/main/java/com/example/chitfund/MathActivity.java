package com.example.chitfund;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class MathActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView textViewSelectTheMonth;
    private TextView textViewSlabAmountPerMonth;
    private TextView textViewMonthItEnds;
    private EditText editTextTotalAmount;
    private EditText editTextNumberOfCustomers;
    private Button buttonSubmitMath;
    private int year;
    private int month;
    private String date;

    private DatePickerDialog.OnDateSetListener onDateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);

        editTextTotalAmount = findViewById(R.id.editTextNumberAmount);
        editTextNumberOfCustomers = findViewById(R.id.editTextNumberCustomers);

        buttonSubmitMath = findViewById(R.id.buttonSubmitMath);
        textViewSlabAmountPerMonth = findViewById(R.id.textViewSlabAmountPerMonth);

        buttonSubmitMath.setOnClickListener(this);
    }


    private void DoTheMath(){
        String totalAmount=editTextTotalAmount.getText().toString().replace(",","");
        String noOfCustomers=editTextNumberOfCustomers.getText().toString().replace(",","");
        if(TextUtils.isEmpty(totalAmount)){
            editTextTotalAmount.setError("Enter total amount");
            editTextTotalAmount.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(noOfCustomers)){
            editTextNumberOfCustomers.setError("Enter total number of customer involved");
            editTextNumberOfCustomers.requestFocus();
            return;
        }
        double slabAmountPerMonth=Double.parseDouble(totalAmount)/Double.parseDouble(noOfCustomers);
        textViewSlabAmountPerMonth.setText("Every month amount that should be paid: "+ slabAmountPerMonth);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.buttonSubmitMath):
                DoTheMath();
                break;

        }
    }

}