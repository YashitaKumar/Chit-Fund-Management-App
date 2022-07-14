package com.example.chitfund;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CustomerAdapter customerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        recyclerView=findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        FirebaseRecyclerOptions<CustomerModel> options =
                new FirebaseRecyclerOptions.Builder<CustomerModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Customers"), CustomerModel.class)
                        .build();

        customerAdapter = new CustomerAdapter(options);
        recyclerView.setAdapter(customerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        customerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        customerAdapter.stopListening();
    }
}