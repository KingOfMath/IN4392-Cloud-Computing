package middleware;

/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

// snippet-start:[ec2.java2.create_instance.import]
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;
import software.amazon.awssdk.services.ec2.*;

import java.util.List;

// snippet-end:[ec2.java2.create_instance.import]



/**
 * Creates an EC2 instance
 */


public class CreateInstance {

    private final static int MS = 1000;

    public static void main(String[] args) throws InterruptedException {
        final String USAGE =
                "To run this example, supply an instance name and AMI image id\n" +
                        "Both values can be obtained from the AWS Console\n" +
                        "Ex: CreateInstance <instance-name> <ami-image-id>\n";

//        if (args.length != 2) {
//            System.out.println(USAGE);
//            System.exit(1);
//        }

        String name = "Liu";
        String amiId = "ami-055754bcf99180715";

        Region region = Region.EU_WEST_2;
        Ec2Client ec2 = Ec2Client.builder()
                .region(region)
                .build();

        String instanceId = createEC2Instance(ec2, name, amiId,2);
        System.out.println("The instance ID is " + instanceId);

        startInstance(ec2, instanceId);
        
        Thread.sleep(15*60*MS);

        stopInstance(ec2,instanceId);

        Thread.sleep(60*MS);

        terminateEC2(ec2,instanceId);

    }

    // snippet-start:[ec2.java2.create_instance.main]
    public static String createEC2Instance(Ec2Client ec2, String name, String amiId, Integer priority) {

        InstanceType inst;

        switch (priority){
            case 1:
                inst = InstanceType.T2_NANO;
                break;
            case 2:
                inst = InstanceType.T2_MICRO;
                break;
            case 3:
                inst = InstanceType.T2_MEDIUM;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + priority);
        }

        RunInstancesRequest runRequest = RunInstancesRequest.builder()
                .imageId(amiId)
                .instanceType(inst)
                .maxCount(1)
                .minCount(1)
                .keyName("New")
                .securityGroups("launch-wizard-1")
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
        // snippet-end:[ec2.java2.create_instance.main]
        return "";
    }

    public static void startInstance(Ec2Client ec2, String instanceId) {

        StartInstancesRequest request = StartInstancesRequest.builder()
                .instanceIds(instanceId)
                .build();

        ec2.startInstances(request);
    }

    public static void stopInstance(Ec2Client ec2, String instanceId) {

        StopInstancesRequest request = StopInstancesRequest.builder()
                .instanceIds(instanceId)
                .build();

        ec2.stopInstances(request);
    }
    public static void terminateEC2( Ec2Client ec2, String instanceID) {

        try{
            TerminateInstancesRequest ti = TerminateInstancesRequest.builder()
                    .instanceIds(instanceID)
                    .build();

            TerminateInstancesResponse response = ec2.terminateInstances(ti);

            List<InstanceStateChange> list = response.terminatingInstances();

            for (int i = 0; i < list.size(); i++) {
                InstanceStateChange sc = (list.get(i));
                System.out.println("The ID of the terminated instance is "+sc.instanceId());
            }
        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

}