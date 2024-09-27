/*
 * SPDX-License-Identifier: Apache-2.0
 */


package org.hyperledger.fabric.samples.assettransfer;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

@DataType()
public final  class Candidate implements Serializable {
    @Property()
    private  String candidateid;
    @Property()
    private  String organisationid;
    @Property()
    private  ArrayList<String> votes;
    @Property()
    private  Byte[] image;

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
        return Objects.equals(getCandidateid(), candidate.getCandidateid()) && Objects.equals(getOrganisationid(), candidate.getOrganisationid()) && Objects.equals(getVotes(), candidate.getVotes()) && Arrays.equals(getImage(), candidate.getImage());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getCandidateid(), getOrganisationid(), getVotes());
        result = 31 * result + Arrays.hashCode(getImage());
        return result;
    }
}
