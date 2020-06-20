package cloud.middleware;


import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;
import software.amazon.awssdk.services.ec2.*;

import java.util.List;


/**
 * Creates an EC2 instance
 */


public class InstanceManager {

//    public static void main(String[] args) throws InterruptedException {
//
//        InstanceManager instanceManager = new InstanceManager();
//
//        String instanceId = instanceManager.createEC2Instance(2);
//        System.out.println("The instance ID is " + instanceId);
//
//        instanceManager.startInstance(instanceId);
//
//        Thread.sleep(15 * 60 * MS);
//
//        instanceManager.stopInstance(instanceId);
//
//        Thread.sleep(60 * MS);
//
//        instanceManager.terminateEC2(instanceId);
//
//    }

    private final static int MS = 1000;
    private String name = "Liu";
    private String amiId = "ami-055754bcf99180715";
    Region region = Region.EU_WEST_2;
    Ec2Client ec2;

    public InstanceManager() {
        ec2 = Ec2Client.builder()
                .region(region)
                .build();
    }

    // snippet-start:[ec2.java2.create_instance.main]
    public String createEC2Instance(Integer priority, Integer jobtype) {

        InstanceType inst;

        switch (priority) {
            case 1:
                inst = InstanceType.T2_NANO;
                break;
            case 2:
                inst = InstanceType.T2_MICRO;
                break;
            case 3:
                inst = InstanceType.T2_SMALL;
                break;
            case 4:
                inst = InstanceType.T2_MEDIUM;
                break;
            case 5:
                inst = InstanceType.T2_LARGE;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + priority);
        }


        switch (jobtype) {
            case 1:
                name = "Job1";
                amiId = "ami-055754bcf99180715";
                break;
            case 2:
                name = "Job2";
                amiId = "ami-0349394cd19506ac5";
                break;
            case 3:
                name = "Job3";
                amiId = "ami-020b9844fd7304617";
                break;
            case 4:
                name = "JobCommon";
                amiId = "ami-009b93e04a115e0b0";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + jobtype);
        }

        RunInstancesRequest runRequest = RunInstancesRequest.builder()
                .imageId(amiId)
                .instanceType(inst)
                .maxCount(1)
                .minCount(1)
                .securityGroups("default")
                .keyName("Liu")
                .build();

        RunInstancesResponse response = ec2.runInstances(runRequest);
        String instanceId = response.instances().get(0).instanceId();

        Tag tag = Tag.builder()
                .key("Name")
                .value(name)
                .build();

        CreateTagsRequest tagRequest = CreateTagsRequest.builder()
                .resources(instanceId)
                .tags(tag)
                .build();

        try {
            ec2.createTags(tagRequest);
            System.out.printf(
                    "Successfully started EC2 instance %s based on AMI %s",
                    instanceId, amiId);

            return instanceId;

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }

    public void startInstance(String instanceId) {

        StartInstancesRequest request = StartInstancesRequest.builder()
                .instanceIds(instanceId)
                .build();

        ec2.startInstances(request);
    }

    public void stopInstance(String instanceId) {

        StopInstancesRequest request = StopInstancesRequest.builder()
                .instanceIds(instanceId)
                .build();

        ec2.stopInstances(request);
    }

    public void terminateEC2(String instanceID) {

        try {
            TerminateInstancesRequest ti = TerminateInstancesRequest.builder()
                    .instanceIds(instanceID)
                    .build();

            TerminateInstancesResponse response = ec2.terminateInstances(ti);

            List<InstanceStateChange> list = response.terminatingInstances();

            for (int i = 0; i < list.size(); i++) {
                InstanceStateChange sc = (list.get(i));
                System.out.println("The ID of the terminated instance is " + sc.instanceId());
            }
        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

}