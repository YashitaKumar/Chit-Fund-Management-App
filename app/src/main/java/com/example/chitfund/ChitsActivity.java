package com.example.chitfund;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ChitsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ChitsAdapter chitAdapter;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chits);

        recyclerView=findViewById(R.id.rvChits);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAuth= FirebaseAuth.getInstance();


        FirebaseRecyclerOptions<ChitModel> options =
                new FirebaseRecyclerOptions.Builder<ChitModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users/"+mAuth.getCurrentUser().getUid()+"/Chits"), ChitModel.class)
                        .build();
        chitAdapter = new ChitsAdapter(options);
        recyclerView.setAdapter(chitAdapter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        chitAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        chitAdapter.stopListening();
    }
}