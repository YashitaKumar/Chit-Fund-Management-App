package com.example.chitfund;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class InvoiceAdapter extends FirebaseRecyclerAdapter<InvoiceModel, InvoiceAdapter.MyViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public InvoiceAdapter(@NonNull FirebaseRecyclerOptions<InvoiceModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull InvoiceModel model) {

        String amounts;
        amounts=model.getAmount() + " INR";
        holder.name.setText(model.getName());
        holder.amount.setText(amounts);
        holder.upiId.setText(model.getUpiId());
        holder.status.setText(model.getStatus());
        holder.date.setText(model.getDate());
        holder.time.setText(model.getTime());
        holder.note.setText(model.getNote());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invoice_item,parent,false);
        return new MyViewHolder(view);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,amount,upiId,note,status,date,time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.PayeeName);
            status = (TextView)itemView.findViewById(R.id.status);
            upiId = (TextView)itemView.findViewById(R.id.upiid);
            amount = (TextView)itemView.findViewById(R.id.amount);
            note = (TextView) itemView.findViewById(R.id.paynote);
            date = (TextView)itemView.findViewById(R.id.paydate);
            time = (TextView)itemView.findViewById(R.id.paytime);


        }
    }
}
