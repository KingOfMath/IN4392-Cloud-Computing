package cloud.middleware;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;

import java.security.Security;

public class securityGroup {

    private static Region region = Region.EU_WEST_2;
    private static Ec2Client ec2;
    private String name = "Liu";
    private String groupName = "default";
    private String groupId = "sg-6f28fd0a";

    public securityGroup(){
        ec2 = Ec2Client.builder().region(region).build();
    }

    public void createSecurityGroup(String groupName, String groupId){
        IpRange ipRange = IpRange.builder()
                .cidrIp("0.0.0.0/0").build();

        IpPermission ipPerm = IpPermission.builder()
                .ipProtocol("tcp")
                .toPort(22)
                .fromPort(22)
                .ipRanges(ipRange)
                .build();

        AuthorizeSecurityGroupIngressRequest authRequest =
                AuthorizeSecurityGroupIngressRequest.builder()
                        .groupName(groupName)
                        .groupId(groupId)
                        .ipPermissions(ipPerm)
                        .build();

        AuthorizeSecurityGroupIngressResponse authResponse =
                ec2.authorizeSecurityGroupIngress(authRequest);
    }

    public void describeSecurityGroup(String groupId) {
        try {
            DescribeSecurityGroupsRequest request =
                    DescribeSecurityGroupsRequest.builder()
                            .groupIds(groupId).build();

            DescribeSecurityGroupsResponse response =
                    ec2.describeSecurityGroups(request);

            for (SecurityGroup group : response.securityGroups()) {
                System.out.printf(
                        "Found security group with id %s, " +
                                "vpc id %s " +
                                "and description %s",
                        group.groupId(),
                        group.vpcId(),
                        group.description());
            }
        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public void deleteSecurityGroup(String groupId){
        try {
            DeleteSecurityGroupRequest request = DeleteSecurityGroupRequest.builder()
                    .groupId(groupId)
                    .build();
            DeleteSecurityGroupResponse response = ec2.deleteSecurityGroup(request);
            System.out.printf(
                    "Successfully deleted security group with id %s", groupId);

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
}
