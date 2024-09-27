package com.example.dvote.fabric_gateway.data;

import org.hyperledger.fabric.client.identity.X509Identity;

import java.security.Principal;
import java.security.cert.X509Certificate;

public class IdentityUtils {

    public static String getUserFromIdentity(X509Identity identity) {
        // Extract the X.509 certificate from the X509Identity
        X509Certificate certificate = identity.getCertificate();

        // Get the Subject Principal from the certificate
        Principal subjectPrincipal = certificate.getSubjectX500Principal();

        // Extract information from the Subject DN (Distinguished Name)
        String subjectDn = subjectPrincipal.getName();

        // Parse the DN to extract specific attributes like Common Name (CN)
        // Example: CN=UserName,OU=OrgUnit,O=Organization,L=Location,ST=State,C=Country

        return extractCommonName(subjectDn);
    }

    private static String extractCommonName(String dn) {
        // Extract the CN (Common Name) from the DN (Distinguished Name)
        String[] dnComponents = dn.split(",");
        for (String component : dnComponents) {
            if (component.trim().startsWith("CN=")) {
                return component.trim().substring(3);
            }
        }
        return null;
    }
}
