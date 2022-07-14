package com.example.chitfund;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

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
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull CustomerModel model) {
       holder.fname.setText(model.getFname());
       holder.lname.setText(model.getLname());
       holder.email.setText(model.getEmail());
       holder.mobile.setText(model.getMobile());
       holder.slab.setText(model.getSlab());

        Glide.with(holder.img.getContext())
                .load(model.getImage())
                .placeholder(R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_item, parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        CircleImageView img;
        TextView fname,lname,email,mobile,slab;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = (CircleImageView) itemView.findViewById(R.id.img1);
            fname = (TextView) itemView.findViewById(R.id.fnametext);
            lname = (TextView) itemView.findViewById(R.id.lnametext);
            email = (TextView) itemView.findViewById(R.id.emailtext);
            mobile = (TextView) itemView.findViewById(R.id.mobiletext);
            slab = (TextView) itemView.findViewById(R.id.slabtext);
        }
    }


}
