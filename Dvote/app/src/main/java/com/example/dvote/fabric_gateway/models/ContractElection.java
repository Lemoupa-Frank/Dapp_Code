package com.example.dvote.fabric_gateway.models;




import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Arrays;

public final class ContractElection implements Serializable {
    private Candidate[] candidates;
    private String election_name;
    private String electionid;
    private String election_describtion;
    private Boolean pool_type;
    private String ownerid; //peer deploying the contract, check difference between peer  and user
    private String election_expiration;
    private String election_start;
    private String election_creation;

    public ContractElection(Candidate[] candidates, String election_name, String electionid, String election_describtion, Boolean pool_type, String ownerid, String election_expiration, String election_start, String election_creation) {
        this.candidates = candidates;
        this.election_name = election_name;
        this.electionid = electionid;
        this.election_describtion = election_describtion;
        this.pool_type = pool_type;
        this.ownerid = ownerid;
        this.election_expiration = election_expiration;
        this.election_start = election_start;
        this.election_creation = election_creation;
    }
    public ContractElection() {

    }

    public Candidate[] getCandidates() {
        return candidates;
    }

    public void setCandidates(Candidate[] candidates) {
        this.candidates = candidates;
    }

    public String getElection_name() {
        return election_name;
    }

    public void setElection_name(String election_name) {
        this.election_name = election_name;
    }

    public String getElectionid() {
        return electionid;
    }

    public void setElectionid(String electionid) {
        this.electionid = electionid;
    }

    public String getElection_describtion() {
        return election_describtion;
    }

    public void setElection_describtion(String election_describtion) {
        this.election_describtion = election_describtion;
    }

    public Boolean getPool_type() {
        return pool_type;
    }

    public void setPool_type(Boolean pool_type) {
        this.pool_type = pool_type;
    }

    public String getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    public String getElection_expiration() {
        return election_expiration;
    }

    public void setElection_expiration(String election_expiration) {
        this.election_expiration = election_expiration;
    }

    public String getElection_start() {
        return election_start;
    }

    public void setElection_start(String election_start) {
        this.election_start = election_start;
    }

    public String getElection_creation() {
        return election_creation;
    }

    public void setElection_creation(String election_creation) {
        this.election_creation = election_creation;
    }

    @NonNull
    @Override
    public String toString() {
        return "ContractElection{" +
                "candidates=" + Arrays.toString(candidates) +
                ", election_name='" + election_name + '\'' +
                ", electionid='" + electionid + '\'' +
                ", election_describtion='" + election_describtion + '\'' +
                ", pool_type=" + pool_type +
                ", ownerid='" + ownerid + '\'' +
                ", election_expiration='" + election_expiration + '\'' +
                ", election_start='" + election_start + '\'' +
                ", election_creation='" + election_creation + '\'' +
                '}';
    }
}

