package com.example.dvote.fabric_gateway.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.viewpager2.widget.ViewPager2;

import com.example.dvote.R;
import com.example.dvote.databinding.ActivityDashboardBinding;
import com.example.dvote.fabric_gateway.adapters.ImagePagerAdapter;
import com.example.dvote.fabric_gateway.custom_dialog.progress_dialog;
import com.example.dvote.fabric_gateway.models.ContractElection;
import com.example.dvote.fabric_gateway.retrofit.Request_Maker;
import com.example.dvote.fabric_gateway.retrofit.Retrofit_Base_Class;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.material.navigation.NavigationView;

import java.util.Arrays;
import java.util.List;

import retrofit2.Retrofit;

public class dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActivityDashboardBinding binding;
    private Handler handler;
    Dialog delaydialog;
    private int currentPage = 0;
    Request_Maker request_maker;
    Retrofit retrofitobj;
    SharedPreferences sharedPreferences;

    Rect rect;
    MutableLiveData<List<ContractElection>> election_item_listener = new MutableLiveData<>();

    public static List<ContractElection> elections;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_dashboard);
        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        setNavigationBarColor(ResourcesCompat.getColor(this.getResources(), R.color.white, null));
        ViewPager2 viewPager = findViewById(R.id.pager);
        List<Integer> images = Arrays.asList(
                R.drawable.vote1,
                R.drawable.vote2,
                R.drawable.vote3,
                R.drawable.vote4,
                R.drawable.vote6
        );
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ImageButton buttonOpenDrawer = findViewById(R.id.button_open_drawer);
        CardView cardView_creat_election = findViewById(R.id.card_create_election);
        CardView lastelection = findViewById(R.id.my_election);
        CardView election_list = findViewById(R.id.view_e);
        CardView update_election = findViewById(R.id.update_election_card);
        if (!dashboardTour()) {
            startTour();
        }
        update_election.setOnClickListener(v->{
            if (!sharedPreferences.getString("userid", "0").equals("0"))
            {
                delaydialog = progress_dialog.delaydialogCircular(this);
                delaydialog.show();
                retrofitobj = Retrofit_Base_Class.getClient();
                request_maker = new Request_Maker();
                async_get_elections(dashboard.this, delaydialog, retrofitobj, election_item_listener, 1 );
            }
            else {
                Toast.makeText(this,  "Request Blockchain ID",Toast.LENGTH_LONG).show();
            }
        });
        cardView_creat_election.setOnClickListener(v -> {
            if (!sharedPreferences.getString("userid", "0").equals("0"))
            {
                Intent i = new Intent(dashboard.this, create_election.class);
                startActivity(i);

            }
            else {
                Toast.makeText(this,  "Request Blockchain ID First",Toast.LENGTH_LONG).show();
            }

        });
        lastelection.setOnClickListener(v -> {
            if (!sharedPreferences.getString("userid", "0").equals("0"))
            {
                Intent i = new Intent(dashboard.this, my_election.class);
                startActivity(i);

            }
            else {
                Toast.makeText(this,  "Request Blockchain ID first",Toast.LENGTH_LONG).show();
            }
        });

        election_list.setOnClickListener(v -> {
            // Retrieve the stored PIN
            if (!sharedPreferences.getString("userid", "0").equals("0"))
            {
                delaydialog = progress_dialog.delaydialogCircular(this);
                delaydialog.show();
                retrofitobj = Retrofit_Base_Class.getClient();
                request_maker = new Request_Maker();
                async_get_elections(dashboard.this, delaydialog, retrofitobj, election_item_listener, 0);
            }
            else {
                Toast.makeText(this,  "Request Blockchain ID",Toast.LENGTH_LONG).show();
            }

        });

        buttonOpenDrawer.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        final ImagePagerAdapter pagerAdapter = new ImagePagerAdapter(this, images);
        viewPager.setAdapter(pagerAdapter);
        screen_slide_runner(pagerAdapter, viewPager);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }*/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.requestid) {
            if(sharedPreferences.getString("userid", "a").equals("a"))
            {
                retrofitobj = Retrofit_Base_Class.getClient();
                request_maker = new Request_Maker();
                delaydialog = progress_dialog.delaydialogCircular(this);
                delaydialog.show();
                async_get_blockchain_id(getBaseContext(), delaydialog, retrofitobj);
            }
            else{
                Toast.makeText(this,"You already have a blockchain ID",Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.action_about) {
            Toast.makeText(this, "About", Toast.LENGTH_LONG).show();
            Intent i = new Intent(dashboard.this, AboutActivity.class);
            startActivity(i);
        } else if (id == R.id.language) {
            Toast.makeText(this, "last election", Toast.LENGTH_LONG).show();
        }else if (id == R.id.logout) {
            finishAffinity();
        }

        //drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void screen_slide_runner(ImagePagerAdapter pagerAdapter, ViewPager2 viewPager) {
        handler = new Handler();
        // Change page every 5 seconds
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (pagerAdapter.getItemCount() == 0) return;
                currentPage = (currentPage + 1) % pagerAdapter.getItemCount();
                viewPager.setCurrentItem(currentPage, true);
                handler.postDelayed(this, 3000); // Change page every 5 seconds
            }
        };
        handler.postDelayed(runnable, 3000); // Initial delay of 5 seconds
    }

    public void async_get_elections(Context context, Dialog delaydialog, Retrofit retrofitobj, MutableLiveData<List<ContractElection>> election_item_listener, int view_value) {
        new Thread(() ->
        {
            request_maker.get_elections(retrofitobj, context, delaydialog, election_item_listener, view_value);
        }).start();

    }

    public static List<ContractElection> getElections() {
        return elections;
    }

    public static void setElection(List<ContractElection> new_elections) {
        elections = new_elections;
    }

    private void startTour() {
        new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(findViewById(R.id.card_create_election), "Submit a new Election", "Here you can create and deploy an election to the blockchain.")
                                .outerCircleColor(R.color.purple_700)
                                .targetCircleColor(R.color.black)
                                .titleTextSize(24)
                                .titleTextColor(R.color.white)
                                .descriptionTextSize(14)
                                .descriptionTextColor(R.color.primary_blue)
                                .textColor(R.color.primary_blue)
                                .drawShadow(true)
                                .cancelable(false)
                                .tintTarget(true)
                                .transparentTarget(true)
                                .targetRadius(60),
                        TapTarget.forView(findViewById(R.id.my_election), "Last Election Visited", "Take a look at your last election")
                                .outerCircleColor(R.color.purple_700)
                                .targetCircleColor(R.color.black)
                                .titleTextSize(24)
                                .titleTextColor(R.color.white)
                                .descriptionTextSize(14)
                                .descriptionTextColor(R.color.primary_blue)
                                .textColor(R.color.primary_blue)
                                .drawShadow(true)
                                .cancelable(false)
                                .tintTarget(true)
                                .transparentTarget(true)
                                .targetRadius(60),
                        TapTarget.forView(findViewById(R.id.update_election_card), "Update an Existing Election", "Update an Election you created")
                                .outerCircleColor(R.color.purple_700)
                                .targetCircleColor(R.color.black)
                                .titleTextSize(24)
                                .titleTextColor(R.color.white)
                                .descriptionTextSize(14)
                                .descriptionTextColor(R.color.primary_blue)
                                .textColor(R.color.primary_blue)
                                .drawShadow(true)
                                .cancelable(false)
                                .tintTarget(true)
                                .transparentTarget(true)
                                .targetRadius(60),
                        TapTarget.forView(findViewById(R.id.view_e), "View all public Elections", "Take a look at what people are voting for")
                                .outerCircleColor(R.color.purple_700)
                                .targetCircleColor(R.color.black)
                                .titleTextSize(24)
                                .titleTextColor(R.color.white)
                                .descriptionTextSize(14)
                                .descriptionTextColor(R.color.primary_blue)
                                .textColor(R.color.primary_blue)
                                .drawShadow(true)
                                .cancelable(false)
                                .tintTarget(true)
                                .transparentTarget(true)
                                .targetRadius(60),
                        TapTarget.forView(findViewById(R.id.button_open_drawer), "Access Menu buttons", "You can Find additional functions here")
                                .outerCircleColor(R.color.purple_700)
                                .targetCircleColor(R.color.black)
                                .titleTextSize(24)
                                .titleTextColor(R.color.white)
                                .descriptionTextSize(14)
                                .descriptionTextColor(R.color.primary_blue)
                                .textColor(R.color.primary_blue)
                                .drawShadow(true)
                                .cancelable(false)
                                .tintTarget(true)
                                .transparentTarget(true)
                                .targetRadius(60)
                )
                .listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {
                        store_sequece();
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                        // A step finished
                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        // Tour canceled
                    }
                }).start();
    }


    private void MenuTour() {
        new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(findViewById(R.id.requestid), "Submit a new Election", "Here you can create and deploy an election to the blockchain.")
                                .outerCircleColor(R.color.purple_700)
                                .targetCircleColor(R.color.black)
                                .titleTextSize(24)
                                .titleTextColor(R.color.white)
                                .descriptionTextSize(14)
                                .descriptionTextColor(R.color.primary_blue)
                                .textColor(R.color.primary_blue)
                                .drawShadow(true)
                                .cancelable(false)
                                .tintTarget(true)
                                .transparentTarget(true)
                                .targetRadius(60),
                        TapTarget.forView(findViewById(R.id.action_about), "Last Election Visited", "Take a look at your last election")
                                .outerCircleColor(R.color.purple_700)
                                .targetCircleColor(R.color.black)
                                .titleTextSize(24)
                                .titleTextColor(R.color.white)
                                .descriptionTextSize(14)
                                .descriptionTextColor(R.color.primary_blue)
                                .textColor(R.color.primary_blue)
                                .drawShadow(true)
                                .cancelable(false)
                                .tintTarget(true)
                                .transparentTarget(true)
                                .targetRadius(60),
                        TapTarget.forView(findViewById(R.id.language), "Update an Existing Election", "Update an Election you created")
                                .outerCircleColor(R.color.purple_700)
                                .targetCircleColor(R.color.black)
                                .titleTextSize(24)
                                .titleTextColor(R.color.white)
                                .descriptionTextSize(14)
                                .descriptionTextColor(R.color.primary_blue)
                                .textColor(R.color.primary_blue)
                                .drawShadow(true)
                                .cancelable(false)
                                .tintTarget(true)
                                .transparentTarget(true)
                                .targetRadius(60),
                        TapTarget.forView(findViewById(R.id.logout), "View all public Elections", "Take a look at what people are voting for")
                                .outerCircleColor(R.color.purple_700)
                                .targetCircleColor(R.color.black)
                                .titleTextSize(24)
                                .titleTextColor(R.color.white)
                                .descriptionTextSize(14)
                                .descriptionTextColor(R.color.primary_blue)
                                .textColor(R.color.primary_blue)
                                .drawShadow(true)
                                .cancelable(false)
                                .tintTarget(true)
                                .transparentTarget(true)
                                .targetRadius(60)
                )
                .listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {
                        store_sequece();
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                        // A step finished
                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        // Tour canceled
                    }
                }).start();
    }




    /**
     * If Onbaording has been done on this screen
     * this function store that in in sharedprefereces
     * so onbaording is done just once, by storing a
     * a value true with key "Onboarding".
     */
    private void store_sequece() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("OnboardingDashboard", true);
        editor.apply(); // Apply changes asynchronously
    }

    /**
     * Funtion checks if onboarding was done in this activity if
     * returns true
     *
     * @return boolean true if there was a prior onboarding
     */
    private boolean dashboardTour() {
        // Get the SharedPreferences object
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        // Retrieve the stored PIN
        return sharedPreferences.getBoolean("OnboardingDashboard", false);

    }

    /**
     * Set Color of Status bar and naviation bar
     *
     * @param color value of color to be set
     */
    public void setNavigationBarColor(int color) {
        //int statusBarColor = ((ColorDrawable) CL.getBackground()).getColor();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
        window.setNavigationBarColor(color);
    }



    /**
     * Calls the get_blockchain_id function in Request_Maker in a new thread
     *
     * @param context     The context of the calling activity
     * @param delaydialog A progress bar
     * @param retrofitobj Retrofit object
     */
    public void async_get_blockchain_id(Context context, Dialog delaydialog, Retrofit retrofitobj) {
        new Thread(() ->
        {
            request_maker.get_block_chain_id(retrofitobj, context, delaydialog);
        }).start();
    }

}