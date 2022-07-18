package com.example.chitfund;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BillPayNowActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonPayNow;
    private TextView textViewDateFromTo;
    private TextView textViewAmountDue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_paynow);

        buttonPayNow=findViewById(R.id.buttonPayNow);
        textViewAmountDue = findViewById(R.id.textViewAmountDue);
        textViewDateFromTo = findViewById(R.id.textViewDateFromTo);

        buttonPayNow.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.buttonPayNow):

                break;
        }
    }
}
