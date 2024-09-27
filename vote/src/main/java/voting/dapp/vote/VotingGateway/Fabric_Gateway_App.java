package voting.dapp.vote.VotingGateway;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import io.grpc.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.hyperledger.fabric.client.*;
import org.hyperledger.fabric.client.identity.Identities;
import org.hyperledger.fabric.client.identity.Identity;
import org.hyperledger.fabric.client.identity.Signer;
import org.hyperledger.fabric.client.identity.Signers;
import org.hyperledger.fabric.client.identity.X509Identity;
import voting.dapp.vote.model.Candidate;
import voting.dapp.vote.model.ContractElection;
import voting.dapp.vote.model.result;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;


public final class Fabric_Gateway_App {
    private static final String MSP_ID = System.getenv().getOrDefault("MSP_ID", "Org1MSP");
    private static final String CHANNEL_NAME = System.getenv().getOrDefault("CHANNEL_NAME", "mychannel");
    private static final String CHAINCODE_NAME = System.getenv().getOrDefault("CHAINCODE_NAME", "basic");

    // Path to crypto materials.
    private static final Path CRYPTO_PATH = Paths.get("/home/frank/fabric-samples/test-network/organizations/peerOrganizations/org1.example.com");
    // Path to user certificate.
    private static final Path CERT_DIR_PATH = CRYPTO_PATH.resolve(Paths.get("users/User1@org1.example.com/msp/signcerts"));
    // Path to user private key directory.
    private static final Path KEY_DIR_PATH = CRYPTO_PATH.resolve(Paths.get("users/User1@org1.example.com/msp/keystore"));
    // Path to peer tls certificate.
    private static final Path TLS_CERT_PATH = CRYPTO_PATH.resolve(Paths.get("/home/frank/fabric-samples/test-network/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt"));
    // Gateway peer end point.
    private static final String PEER_ENDPOINT = "localhost:7051";
    private static final String OVERRIDE_AUTH = "peer0.org1.example.com";

    private final Contract contract;
    private final String assetId = "Election" + Instant.now().toEpochMilli();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    final Logger logger = Logger.getLogger(Fabric_Gateway_App.class.getName());
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void main(final String[] args) throws Exception {
        // The gRPC client connection should be shared by all Gateway connections to
        // this endpoint.
        var channel = newGrpcConnection();

        var builder = Gateway.newInstance().identity(newIdentity()).signer(newSigner()).connection(channel)
                // Default timeouts for different gRPC calls
                .evaluateOptions(options -> options.withDeadlineAfter(5, TimeUnit.SECONDS))
                .endorseOptions(options -> options.withDeadlineAfter(15, TimeUnit.SECONDS))
                .submitOptions(options -> options.withDeadlineAfter(5, TimeUnit.SECONDS))
                .commitStatusOptions(options -> options.withDeadlineAfter(1, TimeUnit.MINUTES));

        try (var gateway = builder.connect()) {
            new Fabric_Gateway_App(gateway).run();
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    public static ManagedChannel newGrpcConnection() throws IOException {
        var credentials = TlsChannelCredentials.newBuilder()
                .trustManager(TLS_CERT_PATH.toFile())
                .build();
        return Grpc.newChannelBuilder(PEER_ENDPOINT, credentials)
                .overrideAuthority(OVERRIDE_AUTH)
                .build();
    }

    public static Identity newIdentity() throws IOException, CertificateException {
        try (var certReader = Files.newBufferedReader(getFirstFilePath(CERT_DIR_PATH))) {
            var certificate = Identities.readX509Certificate(certReader);
            return new X509Identity(MSP_ID, certificate);
        }
    }

    public static X509Identity newX509Identity() throws IOException, CertificateException {
        try (var certReader = Files.newInputStream(getFirstFilePath(CERT_DIR_PATH))) {
            var certificate = MyreadX509Certificate(certReader);
            return new X509Identity(MSP_ID, certificate);
        }
    }
    private static X509Certificate MyreadX509Certificate(InputStream inputStream) throws CertificateException, IOException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        return (X509Certificate) certificateFactory.generateCertificate(inputStream);
    }

    public static Signer newSigner() throws IOException, InvalidKeyException {
        try (var keyReader = Files.newBufferedReader(getFirstFilePath(KEY_DIR_PATH))) {
            var privateKey = Identities.readPrivateKey(keyReader);
            return Signers.newPrivateKeySigner(privateKey);
        }
    }

    private static Path getFirstFilePath(Path dirPath) throws IOException {
        try (var keyFiles = Files.list(dirPath)) {
            return keyFiles.findFirst().orElseThrow();
        }
    }

    public Fabric_Gateway_App(final Gateway gateway) {
        // Get a network instance representing the channel where the smart contract is
        // deployed.
        var network = gateway.getNetwork(CHANNEL_NAME);

        // Get the smart contract from the network.
        contract = network.getContract(CHAINCODE_NAME);
    }

    public static Gateway.Builder Gateway_builder(ManagedChannel channel) throws IOException, CertificateException, InvalidKeyException, InterruptedException {
        return Gateway.newInstance().identity(newIdentity()).signer(newSigner()).connection(channel)
                // Default timeouts for different gRPC calls
                .evaluateOptions(options -> options.withDeadlineAfter(5, TimeUnit.SECONDS))
                .endorseOptions(options -> options.withDeadlineAfter(15, TimeUnit.SECONDS))
                .submitOptions(options -> options.withDeadlineAfter(5, TimeUnit.SECONDS))
                .commitStatusOptions(options -> options.withDeadlineAfter(1, TimeUnit.MINUTES));
    }


    /**
     * This type of transaction would typically only be run once by an application
     * the first time it was started after its initial deployment. A new version of
     * the chaincode deployed later would likely not need to run an "init" function.
     */
    private void initLedger() {
        System.out.println("\n--> InitLedger, function creates the initial set of assets on the ledger");

        try {
            contract.submitTransaction("InitLedger");
            System.out.println("*** Transaction committed successfully");
        } catch (EndorseException | CommitException | SubmitException | CommitStatusException e) {
            System.out.println("initLedger caught " + e);
            logger.log(Level.SEVERE,
                    "Exception occurred",
                    e);
        }

        //System.out.println("Contract Name" + contract.getChaincodeName());
    }


    public void McreateElection() throws EndorseException, CommitException, SubmitException, CommitStatusException {
        System.out.println("\n createElection, creates new election");
        ArrayList<String> votes = new ArrayList<>();
        votes.add(OVERRIDE_AUTH);
        Candidate candidate = new Candidate("Prime45", "WHO", votes, null);
        Candidate[] candidates = new Candidate[1];
        candidates[0] = candidate;
        ContractElection contractElection = new ContractElection(candidates, "Leader45", "zeze", "The Leader", Boolean.FALSE, "unknown", "24", "25", "24");
        contractElection.setElectionid(assetId);
        Gson gson = new Gson();
        String Election_jsonData = gson.toJson(contractElection);
        System.out.println("String Elections\n" + Election_jsonData);
        byte[] results = contract.submitTransaction("CreateContract", Election_jsonData);
        System.out.println("*** Transaction committed successfully");
        System.out.println("result\n" + prettyJson(results));
    }

    public result createElection(ContractElection election) throws EndorseException, CommitException, SubmitException, CommitStatusException {
        System.out.println("\n createElection, creates new election");
        Gson gson = new Gson();
        String Election_jsonData = gson.toJson(election);
        System.out.println("String Elections\n" + Election_jsonData);
        byte[] results = contract.submitTransaction("CreateContract", Election_jsonData);
        System.out.println("*** Transaction committed successfully");
        System.out.println("result\n" + prettyJson(results));
        result data = gson.fromJson(prettyJson(results), result.class);
        return data;
    }

    public List<ContractElection> GetAllElections() throws GatewayException {
        System.out.println("\n Get All Election Contracts, function returns all the current Elections on the ledger");
        byte[] result;
        Gson gsone = new Gson();
        result = contract.evaluateTransaction("GetAllContracts");
        String jsonString = new String(result, StandardCharsets.UTF_8);
        System.out.println("JSON String from byte array: " + jsonString);// Gives exactly what was stored
        Type listType = new TypeToken<List<ContractElection>>() {
        }.getType();
        //System.out.println("*** Result \n " + contractList);
        return gsone.fromJson(jsonString, listType);
    }

    public result UpdateElections(ContractElection election) throws GatewayException, CommitException {
        System.out.println("\n UpdateElections, update election");
        Gson gson = new Gson();
        String Election_jsonData = gson.toJson(election);
        System.out.println("String Elections\n" + Election_jsonData);
        byte[] results = contract.submitTransaction("UpdateAsset", Election_jsonData);
        System.out.println("*** Transaction committed successfully");
        System.out.println("result\n" + prettyJson(results));
        return gson.fromJson(prettyJson(results), result.class);
    }


    private void readAssetById() {
        System.out.println("\n ReadAsset, function returns asset attributes");

        byte[] evaluateResult = new byte[0];
        try {
            evaluateResult = contract.evaluateTransaction("ReadAsset", assetId);
        } catch (GatewayException e) {
            System.out.println("readAssetById caught " + e);
        }

        System.out.println("*** Result:" + prettyJson(evaluateResult));
    }

    /**
     * submitTransaction() will throw an error containing details of any error
     * responses from the smart contract.
     */
    private void updateNonExistentAsset() {
        try {
            System.out.println("\n UpdateAsset asset70, asset70 does not exist and should return an error");

            contract.submitTransaction("UpdateAsset", "asset70", "blue", "5", "Tomoko", "300");

            System.out.println("******** FAILED to return an error");
        } catch (EndorseException | SubmitException | CommitStatusException e) {
            System.out.println("*** Successfully caught the error: ");
            e.printStackTrace(System.out);
            System.out.println("Transaction ID: " + e.getTransactionId());

            var details = e.getDetails();
            if (!details.isEmpty()) {
                System.out.println("Error Details:");
                for (var detail : details) {
                    System.out.println("- address: " + detail.getAddress() + ", mspId: " + detail.getMspId()
                            + ", message: " + detail.getMessage());
                }
            }
        } catch (CommitException e) {
            System.out.println("*** Successfully caught the error: " + e);
            e.printStackTrace(System.out);
            System.out.println("Transaction ID: " + e.getTransactionId());
            System.out.println("Status code: " + e.getCode());
        }
    }

    private String prettyJson(final byte[] json) {
        return prettyJson(new String(json, StandardCharsets.UTF_8));
    }

    private String prettyJson(final String json) {
        var parsedJson = JsonParser.parseString(json);
        return gson.toJson(parsedJson);
    }

    public static void Shut_Channel(ManagedChannel channel) throws InterruptedException {
        channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void run() throws GatewayException, CommitException {
        // Initialize a set of asset data on the ledger using the chaincode 'InitLedger' function.
        //initLedger();


        // Return all the current assets on the ledger.
        //getAllAssets();

        // Create a new asset on the ledger.
        //createAsset();

        // Create new election
        //createElection();

        // Get All Elections
        McreateElection();
        GetAllElections();

        // Update an existing asset asynchronously.
        //transferAssetAsync();

        // Get the asset details by assetID.
        //readAssetById();

        // Update an asset which does not exist.
        //updateNonExistentAsset();
    }
}
