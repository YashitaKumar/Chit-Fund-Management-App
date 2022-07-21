package com.example.chitfund;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class InvoiceActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    InvoiceAdapter invoiceAdapter;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        recyclerView=findViewById(R.id.rvInvoice);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAuth= FirebaseAuth.getInstance();


        FirebaseRecyclerOptions<InvoiceModel> options =
                new FirebaseRecyclerOptions.Builder<InvoiceModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users/"+mAuth.getCurrentUser().getUid()+"/Invoice"), InvoiceModel.class)
                        .build();
        invoiceAdapter = new InvoiceAdapter(options);
        recyclerView.setAdapter(invoiceAdapter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        invoiceAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        invoiceAdapter.stopListening();
    }
}