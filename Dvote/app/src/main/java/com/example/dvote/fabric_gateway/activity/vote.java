package com.example.dvote.fabric_gateway.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dvote.R;
import com.example.dvote.fabric_gateway.adapters.candidate_adpater;
import com.example.dvote.fabric_gateway.custom_dialog.progress_dialog;
import com.example.dvote.fabric_gateway.models.Candidate;
import com.example.dvote.fabric_gateway.models.ContractElection;
import com.example.dvote.fabric_gateway.retrofit.Request_Maker;
import com.example.dvote.fabric_gateway.retrofit.Retrofit_Base_Class;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import retrofit2.Retrofit;

public class vote extends AppCompatActivity {
    Candidate[] candidates;
    ArrayList<String> voters;
    FloatingActionButton back_button;
    Button cast_vote;
    MaterialTextView Title;
    TextView Description;
    ContractElection election;

    ContractElection oldelection;
    Dialog dialog, delaydialog;
    Request_Maker request_maker;

    Retrofit retrofitobj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        Title = findViewById(R.id.etitle);
        Description = findViewById(R.id.lorem_ipsum);
        back_button = findViewById(R.id.back_button);
        cast_vote = findViewById(R.id.cast_vote);
        /*
        candidates = new Candidate[2];
        voters = new ArrayList<>();
        voters.add("user45");
        voters.add("user95");
        candidates[0] = new Candidate("Homlander ", "WHO", voters, convertToByteObjectArray(drawableToByteArray((getResources().getDrawable(R.drawable.homelande)))));
        voters.add("user38");
        candidates[1] = new Candidate("John Wick", "WHO", voters, convertToByteObjectArray(drawableToByteArray((getResources().getDrawable(R.drawable.candidate)))));*/
        RecyclerView recyclerView = findViewById(R.id.recycler);
        if (getIntent() != null && getIntent().hasExtra("election")) {
            election = (ContractElection) getIntent().getSerializableExtra("election");
            oldelection = election;
        }
        assert election != null;
        candidate_adpater candidateAdpater = new candidate_adpater(election.getCandidates(), vote.this, sharedPreferences);
        Description.setText(election.getElection_describtion() == null ? ".." : election.getElection_describtion());
        Title.setText(election.getElection_name());
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(candidateAdpater);
        back_button.setOnClickListener(v -> {
            onBackPressed();
        });
        cast_vote.setOnClickListener(c -> {
            retrofitobj = Retrofit_Base_Class.getClient();
            request_maker = new Request_Maker();
            delaydialog = progress_dialog.delaydialogCircular(this);
            delaydialog.show();
            if (candidateAdpater.getLast_position() != -1) {
                voters = new ArrayList<>();
                voters.add(sharedPreferences.getString("userid", "0"));
                if(election.getCandidates()[candidateAdpater.getLast_position()].getVotes() == null)
                {
                    election.getCandidates()[candidateAdpater.getLast_position()].setVotes(voters);
                }
                else {
                    voters.addAll(election.getCandidates()[candidateAdpater.getLast_position()].getVotes());
                    election.getCandidates()[candidateAdpater.getLast_position()].setVotes(voters);
                }
            }
            //election.getCandidates()[candidateAdpater.getLast_position()].getVotes().add(sharedPreferences.getString("userid", "0"));
            async_update_election(getBaseContext(), election, delaydialog, retrofitobj, oldelection);
        });
    }

    /**
     * Calls the create_election function in Request_Maker in a new thread
     *
     * @param context     The context of the calling activity
     * @param election    The election to be posted
     * @param delaydialog A progress bar
     * @param retrofitobj Retrofit object
     */
    public void async_update_election(Context context, ContractElection election, Dialog delaydialog, Retrofit retrofitobj, ContractElection OldElection) {
        new Thread(() ->
        {
            request_maker.update(retrofitobj, context, election, OldElection, delaydialog);
        }).start();
    }


}