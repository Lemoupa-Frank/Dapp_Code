package com.example.dvote.fabric_gateway.models;

/*
 * SPDX-License-Identifier: Apache-2.0
 */




import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public final  class Candidate implements Serializable {
    private  String candidateid;
    private  String organisationid;
    private  ArrayList<String> votes;
    private  Byte[] image;

    public Candidate(String candidateid, String organisationid, ArrayList<String> votes, Byte[] image) {
        this.candidateid = candidateid;
        this.organisationid = organisationid;
        this.votes = votes;
        this.image = image;
    }

    public Candidate() {

    }


    public String getCandidateid() {
        return candidateid;
    }

    public void setCandidateid(String candidateid) {
        this.candidateid = candidateid;
    }

    public String getOrganisationid() {
        return organisationid;
    }

    public void setOrganisationid(String organisationid) {
        this.organisationid = organisationid;
    }

    public ArrayList<String> getVotes() {
        return votes;
    }

    public void setVotes(ArrayList<String> votes) {
        this.votes = votes;
    }

    public Byte[] getImage() {
        return image;
    }

    public void setImage(Byte[] image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidate candidate = (Candidate) o;
        return Objects.equals(candidateid, candidate.candidateid) && Objects.equals(organisationid, candidate.organisationid) && Objects.equals(votes, candidate.votes) && Arrays.equals(image, candidate.image);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(candidateid, organisationid, votes);
        result = 31 * result + Arrays.hashCode(image);
        return result;
    }

    @NonNull
    @Override
    public String toString() {
        return "Candidate{" +
                "candidateid='" + candidateid + '\'' +
                ", organisationid='" + organisationid + '\'' +
                ", votes=" + votes +
                ", image=" + Arrays.toString(image) +
                '}';
    }
}

