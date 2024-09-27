package com.example.dvote.fabric_gateway.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.example.dvote.R;
import com.example.dvote.fabric_gateway.adapters.view_election_adapter;
import com.example.dvote.fabric_gateway.models.ContractElection;
import com.example.dvote.fabric_gateway.retrofit.Request_Maker;

import java.util.ArrayList;

import retrofit2.Retrofit;

public class view_elections extends AppCompatActivity {
    int view_value = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_election);
        RecyclerView recyclerView = findViewById(R.id.recycler);
        if (getIntent() != null && getIntent().hasExtra("view_value")) {
            view_value = getIntent().getIntExtra("view_value",0);
        }
        view_election_adapter viewElectionAdapter = new view_election_adapter(view_elections.this, dashboard.getElections(), view_value);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(viewElectionAdapter);
    }

}