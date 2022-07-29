package com.example.chitfund;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ChitsAdapter extends FirebaseRecyclerAdapter<ChitModel, ChitsAdapter.MyViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ChitsAdapter(@NonNull FirebaseRecyclerOptions<ChitModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull ChitModel model) {
        holder.chitid.setText(model.getChitid());
        holder.slab.setText(model.getSlab());
        holder.monthly.setText(model.getMonthly());
        holder.timeline.setText(model.getTimeline());
        holder.Startdate.setText(model.getStartdate());
        holder.Duedate.setText(model.getDuedate());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chit_item,parent,false);
        return new ChitsAdapter.MyViewHolder(view);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView chitid,slab,monthly,timeline,Startdate, Duedate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            chitid = (TextView)itemView.findViewById(R.id.chitID);
            slab = (TextView)itemView.findViewById(R.id.slab);
            monthly = (TextView)itemView.findViewById(R.id.monthly);
            timeline = (TextView)itemView.findViewById(R.id.timeline);
            Startdate = (TextView) itemView.findViewById(R.id.startdate);
            Duedate = (TextView)  itemView.findViewById(R.id.duedate);
        }
    }
}
