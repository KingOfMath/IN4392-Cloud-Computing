package cloud.middleware;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;
import software.amazon.awssdk.services.iam.IamClient;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FaultTolerancer {

    private Region region = Region.AWS_GLOBAL;
    private static Ec2Client ec2;
    private Monitor monitor = new Monitor();

    public FaultTolerancer(){
        ec2 = Ec2Client.builder().region(region).build();
    }

    public void terminateMachine(String groupName, String instanceID) throws Exception {
        Set<String> instances = monitor.getRunningInstancesIdByName(groupName);
        if(instances.contains(instanceID)){
            terminateEC2(ec2,instanceID);
        } else {
            throw new Exception("Current instanceId is not running!\n");
        }
    }


    public static void terminateEC2(Ec2Client ec2, String instanceID) {

        try{
            TerminateInstancesRequest ti = TerminateInstancesRequest.builder()
                    .instanceIds(instanceID)
                    .build();

            TerminateInstancesResponse response = ec2.terminateInstances(ti);

            List<InstanceStateChange> list = response.terminatingInstances();

            for (InstanceStateChange sc : list) {
                System.out.println("The ID of the terminated instance is " + sc.instanceId());
            }
        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
}
