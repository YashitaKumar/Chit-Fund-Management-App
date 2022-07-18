package com.example.chitfund;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CustomerAdapter customerAdapter;
    FirebaseAuth mAuth;

    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        recyclerView=findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAuth= FirebaseAuth.getInstance();


        FirebaseRecyclerOptions<CustomerModel> options =
                new FirebaseRecyclerOptions.Builder<CustomerModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users/"+mAuth.getCurrentUser().getUid()+"/Customers"), CustomerModel.class)
                        .build();

        customerAdapter = new CustomerAdapter(options);
        recyclerView.setAdapter(customerAdapter);


        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerActivity.this, AddActivity.class));
            }
        });
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