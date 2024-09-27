package voting.dapp.vote.controller;


import io.grpc.ManagedChannel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.hyperledger.fabric.client.*;
import org.hyperledger.fabric.client.identity.Identity;
import org.hyperledger.fabric.client.identity.Signer;
import org.hyperledger.fabric.client.identity.X509Identity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import voting.dapp.vote.VotingGateway.Fabric_Gateway_App;
import voting.dapp.vote.model.ContractElection;
import voting.dapp.vote.model.result;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.cert.CertificateException;
import java.util.List;

import static voting.dapp.vote.VotingGateway.Fabric_Gateway_App.*;
import static voting.dapp.vote.model.IdentityUtils.getUserFromIdentity;

@RestController
@RequestMapping("/VoteApplication")
public class vote_controller {
    @Operation(summary = "Home Page")
    @GetMapping("/")
    public String index() {
        return "Greetings from Dev Team!";
    }

    @Operation(summary = "Submit An Election ")
    @PostMapping(value = "/create_election")
    public ResponseEntity<result> create_election(@Parameter(description = "Election Body") @RequestBody ContractElection election) {
        try {
            ManagedChannel channel = newGrpcConnection();
            Gateway.Builder builder = (Fabric_Gateway_App.Gateway_builder(channel));
            var gateway = builder.connect();
            result results = new Fabric_Gateway_App(gateway).createElection(election);
            Fabric_Gateway_App.Shut_Channel(channel);
            return ResponseEntity.ok(results);
        } catch (EndorseException | CommitException | SubmitException | CommitStatusException | IOException |
                 CertificateException | InvalidKeyException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new result(e.getMessage(), "500"));
        }
    }

    @Operation(summary = "View All Elections Contract")
    @GetMapping("/view_eletions")
    public ResponseEntity<List<ContractElection>> GetAllElections() {
        try {
            ManagedChannel channel = newGrpcConnection();
            Gateway.Builder builder = (Fabric_Gateway_App.Gateway_builder(channel));
            var gateway = builder.connect();
            List<ContractElection> results = new Fabric_Gateway_App(gateway).GetAllElections();
            return ResponseEntity.ok( results);
        } catch (IOException | CertificateException | InvalidKeyException | InterruptedException | GatewayException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @Operation(summary = "Update an existing Elections Contract")
    @PostMapping("/update_eletions")
    public ResponseEntity<result> updateElection(@Parameter(description = "Election Body") @RequestBody ContractElection election) {
        try {
            ManagedChannel channel = newGrpcConnection();
            Gateway.Builder builder = (Fabric_Gateway_App.Gateway_builder(channel));
            var gateway = builder.connect();
            result results = new Fabric_Gateway_App(gateway).UpdateElections(election);
            Fabric_Gateway_App.Shut_Channel(channel);
            return ResponseEntity.ok(results);
        } catch (CommitException | IOException | CertificateException | InvalidKeyException | InterruptedException |
                 GatewayException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new result(e.getMessage(), "500"));
        }
    }

    @Operation(summary = "Request Identification from blockchain")
    @GetMapping("/identity")
    public result newIdentity() throws IOException, CertificateException {
        System.out.println(getUserFromIdentity(newX509Identity()));
        return new result(getUserFromIdentity(newX509Identity()), newX509Identity().getMspId());
    }

}
