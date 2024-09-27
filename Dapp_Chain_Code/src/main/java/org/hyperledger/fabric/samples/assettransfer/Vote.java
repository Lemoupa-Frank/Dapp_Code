package org.hyperledger.fabric.samples.assettransfer;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.owlike.genson.Genson;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Contract(
        name = "Vote",
        info = @Info(
                title = "Voting Contract",
                description = "The Voting Transaction",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "frankmichel022@gmail.com",
                        name = "Frank Michel",
                        url = "https://hyperledger.example.com")))

@Default
public final class Vote implements ContractInterface {
    private final Genson genson = new Genson();

    private enum AssetTransferErrors {
        ASSET_NOT_FOUND,
        ASSET_ALREADY_EXISTS
    }
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String UpdateAsset(final Context ctx, String Election_Json) {
        ChaincodeStub stub = ctx.getStub();
        Gson gson = new Gson();
        try{
            ContractElection data = gson.fromJson(Election_Json, ContractElection.class);
            if (!ContractExists(ctx, data.getElectionid())) {
                String errorMessage = String.format("Election %s does not exists in ledger", data.getElectionid());
                return String.format("{\"code\": \"%s\",\"response\": \"%s\"}", "502", errorMessage);
            }

            stub.putStringState(data.getElectionid(), Election_Json);
            return String.format("{\"code\": \"%s\",\"response\": \"%s\"}", "200", "Election updated");
        }catch (Exception e){
            return String.format("{\"code\": \"%s\",\"response\": \"%s\"}", "501", e);
        }

    }


    /**
     * Creates a new Election
     * @param ctx the transaction context
     * @param Election_Json string format json of ContractElection
     * @return Election
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String CreateContract(final Context ctx, String Election_Json) {
       ChaincodeStub stub = ctx.getStub();
       Gson gson = new Gson();
        //ContractElection data = gson.fromJson(Election_Json, ContractElection.class);
        //ContractElection data = genson.deserialize(Election_Json, ContractElection.class);
        try{
            ContractElection data = gson.fromJson(Election_Json, ContractElection.class);
            if (ContractExists(ctx, data.getElectionid())) {
                String errorMessage = String.format("Election %s already exists in ledger", data.getElectionid());
                return String.format("{\"code\": \"%s\",\"response\": \"%s\"}", "502", errorMessage);
            }
            stub.putStringState(data.getElectionid(), Election_Json);
            return String.format("{\"code\": \"%s\",\"response\": \"%s\"}", "200", "Election Created");
        }catch(Exception e)
        {
            return String.format("{\"code\": \"%s\",\"response\": \"%s\"}", "501", e);
        }
    }




    /**
     * Retrieves all assets from the ledger.
     *
     * @param ctx the transaction context
     * @return array of contracts found on the ledger
     */
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllContracts(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        List<ContractElection> queryResults = new ArrayList<>();

        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");

        for (KeyValue result: results) {
            String Election = result.getStringValue();
            Gson gson = new Gson();
            ContractElection data = gson.fromJson(Election, ContractElection.class);
            queryResults.add(data);
        }

        return genson.serialize(queryResults);
    }



    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public boolean ContractExists(final Context ctx, final String assetID) {
        ChaincodeStub stub = ctx.getStub();
        String assetJSON = stub.getStringState(assetID);

        return (assetJSON != null && !assetJSON.isEmpty());
    }
}