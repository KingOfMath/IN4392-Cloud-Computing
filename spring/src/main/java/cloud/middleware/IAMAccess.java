package cloud.middleware;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IAMAccess {

    private Region region = Region.AWS_GLOBAL;
    private IamClient iam;
    private String username = "LyxGoodJob";
    private String roleName = "Lyxtestingcase-role-j5ttef8g";

    public IAMAccess(){
        iam = IamClient.builder()
                .region(region)
                .build();
        listAllUsers(iam);
        listKeys(iam,username);
        listCertificates(iam);
        attachIAMRolePolicy(iam,roleName);
    }

    public static void listAllUsers(IamClient iam ) {

        try {

            boolean done = false;
            String newMarker = null;

            while(!done) {
                ListUsersResponse response;

                if (newMarker == null) {
                    ListUsersRequest request = ListUsersRequest.builder().build();
                    response = iam.listUsers(request);
                } else {
                    ListUsersRequest request = ListUsersRequest.builder()
                            .marker(newMarker).build();
                    response = iam.listUsers(request);
                }

                for(
                        User user : response.users()) {
                    System.out.format("\n Retrieved user %s", user.userName());
                }

                if(!response.isTruncated()) {
                    done = true;
                } else {
                    newMarker = response.marker();
                }
            }
        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void listKeys( IamClient iam,String username ) {

        try {
            boolean done = false;
            String newMarker = null;

            while (!done) {
                ListAccessKeysResponse response;

                if (newMarker == null) {
                    ListAccessKeysRequest request = ListAccessKeysRequest.builder()
                            .userName(username).build();
                    response = iam.listAccessKeys(request);
                } else {
                    ListAccessKeysRequest request = ListAccessKeysRequest.builder()
                            .userName(username)
                            .marker(newMarker).build();
                    response = iam.listAccessKeys(request);
                }

                for (AccessKeyMetadata metadata :
                        response.accessKeyMetadata()) {
                    System.out.format("Retrieved access key %s",
                            metadata.accessKeyId());
                }

                if (!response.isTruncated()) {
                    done = true;
                } else {
                    newMarker = response.marker();
                }
            }
        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void listCertificates(IamClient iam) {
        try {
            boolean done = false;
            String newMarker = null;

            while (!done) {
                ListServerCertificatesResponse response;

                if (newMarker == null) {
                    ListServerCertificatesRequest request =
                            ListServerCertificatesRequest.builder().build();
                    response = iam.listServerCertificates(request);
                } else {
                    ListServerCertificatesRequest request =
                            ListServerCertificatesRequest.builder()
                                    .marker(newMarker).build();
                    response = iam.listServerCertificates(request);
                }

                for (ServerCertificateMetadata metadata :
                        response.serverCertificateMetadataList()) {
                    System.out.printf("Retrieved server certificate %s",
                            metadata.serverCertificateName());
                }

                if (!response.isTruncated()) {
                    done = true;
                } else {
                    newMarker = response.marker();
                }
            }
        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void attachIAMRolePolicy(IamClient iam, String roleName) {

        try {

            List<AttachedPolicy> matchingPolicies = new ArrayList<>();

            boolean done = false;
            String newMarker = null;

            while (!done) {

                ListAttachedRolePoliciesResponse response;

                if (newMarker == null) {
                    ListAttachedRolePoliciesRequest request =
                            ListAttachedRolePoliciesRequest.builder()
                                    .roleName(roleName)
                                    .build();
                    response = iam.listAttachedRolePolicies(request);
                } else {
                    ListAttachedRolePoliciesRequest request =
                            ListAttachedRolePoliciesRequest.builder()
                                    .roleName(roleName)
                                    .marker(newMarker).build();
                    response = iam.listAttachedRolePolicies(request);
                }

                matchingPolicies.addAll(
                        new ArrayList<>(response.attachedPolicies()));

                if (!response.isTruncated()) {
                    done = true;

                } else {
                    newMarker = response.marker();
                }
            }

            for (AttachedPolicy policy:matchingPolicies) {
                System.out.println(policy.policyName());
            }


        } catch (IamException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
}
