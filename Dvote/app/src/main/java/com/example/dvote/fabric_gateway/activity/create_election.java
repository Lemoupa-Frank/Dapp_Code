package com.example.dvote.fabric_gateway.activity;

import static com.example.dvote.fabric_gateway.data.ImageUtils.convertToByteObjectArray;
import static com.example.dvote.fabric_gateway.data.ImageUtils.getBytesFromImageUri;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dvote.R;
import com.example.dvote.fabric_gateway.adapters.create_candidate_adapter;
import com.example.dvote.fabric_gateway.custom_dialog.DatePickerFragment;
import com.example.dvote.fabric_gateway.custom_dialog.progress_dialog;
import com.example.dvote.fabric_gateway.models.Candidate;
import com.example.dvote.fabric_gateway.models.ContractElection;
import com.example.dvote.fabric_gateway.retrofit.Request_Maker;
import com.example.dvote.fabric_gateway.retrofit.Retrofit_Base_Class;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutorService;

import retrofit2.Retrofit;

public class create_election extends AppCompatActivity implements create_candidate_adapter.OnImageSelectedListener {
    Dialog dialog, delaydialog;
    Request_Maker request_maker;

    Retrofit retrofitobj;

    private ExecutorService executorService;
    private int selectedPosition;
    CheckBox private_box, public_box;
    public static final int PICK_IMAGE_REQUEST = 1;
    public MaterialTextView starttext, endtext, Post;
    EditText description;
    HorizontalScrollView scrollView;
    create_candidate_adapter createCandidateAdapter;
    FloatingActionButton add_participant;

    ContractElection election = new ContractElection();
    EditText Election_Title;

    boolean update_activity = false;

    boolean firstItem = true;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_election);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        RecyclerView recyclerView = findViewById(R.id.recycler);
        Election_Title = findViewById(R.id.election_title);
        private_box = findViewById(R.id._private_check);
        public_box = findViewById(R.id._public_check);
        description = findViewById(R.id.name);
        starttext = findViewById(R.id.start_date);
        endtext = findViewById(R.id.end_date);
        Post = findViewById(R.id.post);
        if (getIntent() != null && getIntent().hasExtra("election")) {
            election = (ContractElection) getIntent().getSerializableExtra("election");
            assert election != null;
            Election_Title.setText(election.getElection_name() != null ? election.getElection_name() : " ");
            if (election.getPool_type()) {
                public_box.setChecked(true);
            } else {
                private_box.setChecked(true);
            }
            description.setText(election.getElection_describtion() != null ? election.getElection_describtion() : " ");
            starttext.setText(election.getElection_start());
            endtext.setText(election.getElection_expiration());
            update_activity = true;
            ArrayList<Candidate> candidatesList = new ArrayList<>(Arrays.asList(election.getCandidates()));
            createCandidateAdapter = new create_candidate_adapter(candidatesList);
        } else {
            createCandidateAdapter = new create_candidate_adapter(null);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(createCandidateAdapter);
        createCandidateAdapter.setOnImageSelectedListener(this);
        scrollView = findViewById(R.id.scroll);
        add_participant = findViewById(R.id.add_candidate);
        public_box.setOnClickListener(v -> {
            if (private_box.isChecked()) {
                private_box.setChecked(false);
            }
        });
        private_box.setOnClickListener(v -> {
            if (public_box.isChecked()) {
                public_box.setChecked(false);
            }
        });
        FloatingActionButton next_btn = findViewById(R.id.next);
        scrollView.setOnTouchListener((v, event) -> {
            return true; // Always consume the touch event
        });

        Post.setOnClickListener(
                v -> {
                    if (!firstItem) {
                        election.setElection_describtion(description.getText().toString());
                        if (createCandidateAdapter.localDataSet.size() > 1) {
                            createCandidateAdapter.localDataSet.remove(createCandidateAdapter.localDataSet.size() - 1);
                        }
                        election.setCandidates(createCandidateAdapter.localDataSet.toArray(new Candidate[0]));
                        election.setPool_type(public_box.isChecked());
                        election.setElectionid(election.getElection_name());
                        election.setOwnerid(sharedPreferences.getString("userid", "0"));
                        retrofitobj = Retrofit_Base_Class.getClient();
                        request_maker = new Request_Maker();
                        delaydialog = progress_dialog.delaydialogCircular(this);
                        delaydialog.show();
                        if(update_activity)
                        {
                            async_update_election(getBaseContext(), election,delaydialog, retrofitobj);
                        }
                        else {
                            async_create_election(getBaseContext(), election, delaydialog, retrofitobj);
                        }
                        //runRequestInBackground(getBaseContext(),election,delaydialog,retrofitobj);
                        //request_maker.create_meets(retrofitobj,getBaseContext(),election, delaydialog);
                    } else {
                        shakeEditText(Post);
                    }


                }
        );

        next_btn.setOnClickListener(v -> scrollView.post(new Runnable() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void run() {
                if (Election_Title.getText().length() > 0) {
                    election.setElection_name(Election_Title.getText().toString());
                    election.setElection_start(starttext.getText().toString());
                    election.setElection_expiration(endtext.getText().toString());
                    election.setElection_creation(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));
                    scrollView.fullScroll(View.FOCUS_RIGHT);
                    firstItem = false;
                } else {
                    shakeEditText(Election_Title);
                }

            }

        }));
        FloatingActionButton startdate_btn = findViewById(R.id.start_date_bttn);
        startdate_btn.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                StartDatePickerDialog();
            } else {
                older_StartDatePickerDialog();
            }
        });
        FloatingActionButton enddate_btn = findViewById(R.id.end_date_bttn);
        enddate_btn.setOnClickListener(c -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                EndDatePickerDialog();
            } else {
                older_EndDatePickerDialog();
            }
        });
    }

    private void openFileChooser(int position) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        selectedPosition = position;
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            createCandidateAdapter.image_uri = imageUri;
            try {
                createCandidateAdapter.updateImage(selectedPosition, convertToByteObjectArray(getBytesFromImageUri(getBaseContext(), imageUri)));
            } catch (IOException e) {
                //(throw new RuntimeException(e);
            }
            // Handle the chosen image URI (e.g., display it in an ImageView or upload it)
            Toast.makeText(this, "Image Selected: " + imageUri.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void StartDatePickerDialog() {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setListener((year, month, dayOfMonth) -> {
            LocalDateTime dateTimes = LocalDateTime.of(year, month, dayOfMonth, 0, 0, 0, 0);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            String formattedDate = dateTimes.format(formatter);
            //startdatetemp = DateTime.parseRfc3339(formattedDate);
            starttext.setText(formattedDate);
        });
        newFragment.show(getSupportFragmentManager(), "StartDate");
    }

    public void older_StartDatePickerDialog() {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setListener((year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth, 0, 0, 0);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            String formattedDate = formatter.format(calendar.getTime());

            //startdatetemp = DateTime.parseRfc3339(formattedDate);
            starttext.setText(formattedDate);
        });
        newFragment.show(getSupportFragmentManager(), "StartDate");
    }

    public void older_EndDatePickerDialog() {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setListener((year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth, 0, 0, 0);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            String formattedDate = formatter.format(calendar.getTime());
            //enddatetemp = DateTime.parseRfc3339(formattedDate);
            endtext.setText(formattedDate);
        });
        newFragment.show(getSupportFragmentManager(), "EndDate");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void EndDatePickerDialog() {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setListener((year, month, dayOfMonth) -> {
            LocalDateTime dateTimes = LocalDateTime.of(year, month, dayOfMonth, 0, 0, 0, 0);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            String formattedDate = dateTimes.format(formatter);
            //enddatetemp = DateTime.parseRfc3339(formattedDate);
            endtext.setText(formattedDate);
        });
        newFragment.show(getSupportFragmentManager(), "EndDate");
    }

    public void onBackPressed() {
        if (firstItem) {
            super.onBackPressed();
        } else {
            scrollView.fullScroll(View.FOCUS_LEFT);
            firstItem = true;
        }
    }

    @Override
    public void onImageSelected(int position) {
        openFileChooser(position);
    }

    /**
     * This in a edit textviex and shakes it
     *
     * @param editText material textview o be translated
     */
    private void shakeEditText(EditText editText) {
        // Calculate the desired translation distance
        int shakeDistance = 50;

        // Create the animation
        Animation shakeAnimation = new TranslateAnimation(0, shakeDistance, 0, 0);
        shakeAnimation.setDuration(100);
        shakeAnimation.setRepeatCount(7);
        shakeAnimation.setRepeatMode(Animation.REVERSE);
        editText.startAnimation(shakeAnimation);
    }

    /**
     * This in a Material textviex and shakes it
     *
     * @param editText material textview o be translated
     */
    private void shakeEditText(MaterialTextView editText) {
        // Calculate the desired translation distance
        int shakeDistance = 50;

        // Create the animation
        Animation shakeAnimation = new TranslateAnimation(0, shakeDistance, 0, 0);
        shakeAnimation.setDuration(100);
        shakeAnimation.setRepeatCount(7);
        shakeAnimation.setRepeatMode(Animation.REVERSE);
        editText.startAnimation(shakeAnimation);
    }


    /**
     * Calls the create_election function in Request_Maker in a new thread
     *
     * @param context     The context of the calling activity
     * @param election    The election to be posted
     * @param delaydialog A progress bar
     * @param retrofitobj Retrofit object
     */
    public void async_create_election(Context context, ContractElection election, Dialog delaydialog, Retrofit retrofitobj) {
        new Thread(() ->
        {
            request_maker.create_elections(retrofitobj, context, election, delaydialog);
        }).start();
    }
    /**
     * Calls the create_election function in Request_Maker in a new thread
     *
     * @param context     The context of the calling activity
     * @param election    The election to be posted
     * @param delaydialog A progress bar
     * @param retrofitobj Retrofit object
     */
    public void async_update_election(Context context, ContractElection election, Dialog delaydialog, Retrofit retrofitobj) {
        new Thread(() ->
        {
            request_maker.update(retrofitobj, context, election, election ,delaydialog);
        }).start();
    }

}