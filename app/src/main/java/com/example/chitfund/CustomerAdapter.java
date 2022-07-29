package com.example.chitfund;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerAdapter extends FirebaseRecyclerAdapter<CustomerModel, CustomerAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CustomerAdapter(@NonNull FirebaseRecyclerOptions<CustomerModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull CustomerModel model){
       holder.fname.setText(model.getFname());
       holder.lname.setText(model.getLname());
       holder.email.setText(model.getEmail());
       holder.slab.setText(model.getSlab());
       holder.mobile.setText(model.getMobile());
       holder.UID.setText(model.getUID());

        Glide.with(holder.img.getContext())
                .load(model.getImage())
                .placeholder(R.drawable.ic_person)
                .circleCrop()
                .error(R.drawable.ic_person)
                .into(holder.img);

        holder.btnEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true,1600)
                        .create();

                //dialogPlus.show();

                View view1 = dialogPlus.getHolderView();

                EditText fnameText = view1.findViewById(R.id.txtfname);
                EditText lnameText = view1.findViewById(R.id.txtlname);
                EditText emailText = view1.findViewById(R.id.txtemail);
                EditText mobileText = view1.findViewById(R.id.txtmobile);
                EditText slabText = view1.findViewById(R.id.txtslab);
                EditText imageText = view1.findViewById(R.id.txtimage);
                EditText uidText = view1.findViewById(R.id.txtuid);

                Button btnUpdate = view1.findViewById(R.id.btnUpdate);

                fnameText.setText(model.getFname());
                lnameText.setText(model.getLname());
                emailText.setText(model.getEmail());
                mobileText.setText(model.getMobile());
                slabText.setText(model.getSlab());
                uidText.setText(model.getUID());
                imageText.setText(model.getImage());

                dialogPlus.show();

                btnUpdate.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("fname",fnameText.getText().toString());
                        map.put("lname",lnameText.getText().toString());
                        map.put("email",emailText.getText().toString());
                        map.put("mobile",mobileText.getText().toString());
                        map.put("slab",slabText.getText().toString());
                        map.put("image",imageText.getText().toString());
                        map.put("uid",uidText.getText().toString());

                        FirebaseAuth mAuth;
                        mAuth= FirebaseAuth.getInstance();


                        FirebaseDatabase.getInstance().getReference().child("Users/"+mAuth.getCurrentUser().getUid()+"/Customers")
                                .child(getRef(holder.getAdapterPosition()).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.fname.getContext(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.fname.getContext(), "Error While Updating", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });

                    }
                });
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.fname.getContext());
                builder.setTitle("Are you sure you want to delete");
                builder.setMessage("Deleted data cant be undone");
                FirebaseAuth mAuth;
                mAuth= FirebaseAuth.getInstance();

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("Users/"+mAuth.getCurrentUser().getUid()+"/Customers")
                                .child(getRef(holder.getAdapterPosition()).getKey()).removeValue();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.fname.getContext(),"Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_item, parent,false);
        return new myViewHolder(view);
    }


    class myViewHolder extends RecyclerView.ViewHolder{

        CircleImageView img;
        TextView fname,lname,email,mobile,slab,UID;

        Button btnEdit,btnDelete;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = (CircleImageView) itemView.findViewById(R.id.img1);
            fname = (TextView) itemView.findViewById(R.id.fnametext);
            lname = (TextView) itemView.findViewById(R.id.lnametext);
            email = (TextView) itemView.findViewById(R.id.emailtext);
            mobile = (TextView) itemView.findViewById(R.id.mobiletext);
            slab = (TextView) itemView.findViewById(R.id.slabtext);
            UID = (TextView) itemView.findViewById(R.id.UIDtext);

            btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
        }
    }


}
