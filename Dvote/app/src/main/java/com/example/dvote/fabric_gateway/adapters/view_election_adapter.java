package com.example.dvote.fabric_gateway.adapters;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dvote.R;
import com.example.dvote.fabric_gateway.activity.create_election;
import com.example.dvote.fabric_gateway.activity.vote;
import com.example.dvote.fabric_gateway.models.ContractElection;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class view_election_adapter extends RecyclerView.Adapter<view_election_adapter.ViewHolder> {
    List<ContractElection> contractElections;
    private final Context context;
    SharedPreferences sharedPreferences;

    private final int view_value;

    public view_election_adapter(Context context, List<ContractElection> contractElections, int view_value) {
        this.contractElections = contractElections;
        this.context = context;
        this.view_value = view_value;
        sharedPreferences =  context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.election_item, viewGroup, false);

        return new view_election_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        if(view_value == 0 && contractElections.get(position).getOwnerid().equals(sharedPreferences.getString("userid", "0")) )
        {
            String number_of_candidates = "number of candidates: " + (contractElections.get(position).getCandidates().length);
            viewHolder.getname().setText(contractElections.get(position).getElection_name());
            viewHolder.getvotes().setText(number_of_candidates);
        }
        else {
            String number_of_candidates = "number of candidates: " + (contractElections.get(position).getCandidates().length);
            viewHolder.getname().setText(contractElections.get(position).getElection_name());
            viewHolder.getvotes().setText(number_of_candidates);
        }

        viewHolder.getbutton().setOnClickListener(v -> {
            if(view_value == 0)
            {
                Intent i = new Intent(context, vote.class);
                i.putExtra("election", contractElections.get(position));
                context.startActivity(i);
            }
            else {
                Intent i = new Intent(context, create_election.class);
                i.putExtra("election", contractElections.get(position));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contractElections == null ? 0 : contractElections.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView election_name, number_of_votes;
        private final FloatingActionButton election;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            election_name = view.findViewById(R.id.election_title);
            number_of_votes = view.findViewById(R.id.number_of_candidates);
            election = view.findViewById(R.id.get_election);
        }

        public TextView getname() {
            return election_name;
        }

        public TextView getvotes() {
            return number_of_votes;
        }

        public FloatingActionButton getbutton() {
            return election;
        }

    }
}
