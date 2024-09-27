package com.example.dvote.fabric_gateway.retrofit;


import com.example.dvote.fabric_gateway.models.ContractElection;

import org.hyperledger.fabric.client.identity.Identity;
import org.hyperledger.fabric.client.identity.X509Identity;

import java.time.LocalDate;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
// necessary imports to be used

//interface that will define all request to be used
public interface Request_Route
{

    @Headers("Content-Type: application/json")
    @POST("/VoteApplication/create_election")
    Call<result> store_election(@Body  ContractElection contractElection);

    @Headers("Content-Type: application/json")
    @POST("/VoteApplication/update_eletions")
    Call<result> update_election(@Body  ContractElection contractElection);

    @GET("/VoteApplication/view_eletions")
    Call<List<ContractElection>> get_election();

    @GET("/VoteApplication/identity")
    Call<result> get_identification();



}

//   /VoteApplication

/*
*    @PostMapping(value = "/create election")
    public ResponseEntity<result> create_election(@Parameter(description = "Election Body") @RequestBody ContractElection election)
 */

/*
    @GetMapping("/view eletions")
    public ResponseEntity<List<ContractElection>> GetAllElections()
 */