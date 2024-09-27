package com.example.dvote.fabric_gateway.retrofit;


import static com.example.dvote.fabric_gateway.activity.dashboard.setElection;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.dvote.fabric_gateway.activity.dashboard;
import com.example.dvote.fabric_gateway.activity.view_elections;
import com.example.dvote.fabric_gateway.models.ContractElection;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A general class to handle and make calls
 * using Retrofit also has a some utility functions
 * like a UserMeetings converter to Hashmaps
 */

public class Request_Maker {
    public void create_elections(Retrofit retrofitObject, Context con, ContractElection a, Dialog delaydialog) {
        Request_Route RR = retrofitObject.create(Request_Route.class);
        Call<result> resultCall = RR.store_election(a);
        resultCall.enqueue(new Callback<result>() {
            @Override
            public void onResponse(@NonNull Call<result> call, @NonNull Response<result> response) {
                delaydialog.cancel();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        // change the attribute Message in results to response in both front and backend 
                        Toast.makeText(con, response.body().code.equals("200") ? "Election Created" : "Election Exist", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(con, "Unseccessful " + response.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<result> call, @NonNull Throwable t) {
                delaydialog.cancel();
                //Toast.makeText(con, "Failed" + t.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(con, "Sorry try again, of problem persist contact us", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void update(Retrofit retrofitObject, Context con, ContractElection a, ContractElection oldE, Dialog delaydialog) {
        Request_Route RR = retrofitObject.create(Request_Route.class);
        Call<result> resultCall = RR.update_election(a);
        resultCall.enqueue(new Callback<result>() {
            @Override
            public void onResponse(@NonNull Call<result> call, @NonNull Response<result> response) {
                delaydialog.cancel();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        // change the attribute Message in results to response in both front and backend
                        Toast.makeText(con, response.body().code.equals("200") ? "ELection Updated" : "Election Exist", Toast.LENGTH_LONG).show();
                        dashboard.getElections().remove(oldE);
                        dashboard.getElections().add(a);
                    }
                } else {
                    Toast.makeText(con, "Unseccessful " + response.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<result> call, @NonNull Throwable t) {
                delaydialog.cancel();
                //Toast.makeText(con, "Failed" + t.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(con, "Sorry try again, of problem persist contact us", Toast.LENGTH_LONG).show();
            }
        });
    }



    public void get_elections(Retrofit retrofitObject, Context con, Dialog delaydialog, MutableLiveData<List<ContractElection>> election_list_observer, int view_value) {
        Request_Route RR = retrofitObject.create(Request_Route.class);
        Call<List<ContractElection>> resultCall = RR.get_election();
        resultCall.enqueue(new Callback<List<ContractElection>>() {
            @Override
            public void onResponse(@NonNull Call<List<ContractElection>> call, @NonNull Response<List<ContractElection>> response) {
                delaydialog.cancel();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().size() > 0) {
                        setElection(response.body());
                        election_list_observer.postValue(response.body());
                        if(view_value == 1)
                        {
                            // called from update election
                            Intent i = new Intent(con, view_elections.class);
                            i.putExtra("view_value", 1);
                            con.startActivity(i);
                        }
                        else {
                            Toast.makeText(con, "Fetched Elections", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(con, view_elections.class);
                            con.startActivity(i);
                        }
                    } else {
                        Toast.makeText(con, "No election where found", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(con, "The is a problem with the response" + response.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ContractElection>> call, @NonNull Throwable t) {
                delaydialog.cancel();
                //Toast.makeText(con, "Failed" + t.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(con, "Sorry try again, of problem persist contact us", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void get_block_chain_id(Retrofit retrofitObject, Context con, Dialog delaydialog) {
        Request_Route RR = retrofitObject.create(Request_Route.class);
        Call<result> resultCall = RR.get_identification();
        resultCall.enqueue(new Callback<result>() {
            @Override
            public void onResponse(@NonNull Call<result> call, @NonNull Response<result> response) {
                delaydialog.cancel();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Toast.makeText(con, ("Your Organisation is " + response.body().code + " UserId:" + response.body().message), Toast.LENGTH_LONG).show();
                    store_User(response.body().message, con);
                } else {
                    Toast.makeText(con, "The is a problem with the response" + response.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<result> call, @NonNull Throwable t) {
                delaydialog.cancel();
                //Toast.makeText(con, "Failed" + t.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(con, "Sorry try again, of problem persist contact us", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void store_User(String User, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userid", User);
        editor.apply(); // Apply changes asynchronously
    }

}
//http://zqktlwiuavvvqqt4ybvgvi7tyo4hjl5xgfuvpdf6otjiycgwqbym2qad.onion/wiki/The_Matrix
/*
            new Thread(()->
            {
                try {
                    getAttendees.execute();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }).start();
 */