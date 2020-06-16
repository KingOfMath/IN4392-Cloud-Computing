package cloud.middleware;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;

import java.util.HashSet;
import java.util.Set;

public class Monitor {

    public static void main(String[] args) {
        Monitor m = new Monitor();
        m.monitor();
    }

    private static Region region = Region.EU_WEST_2;
    private static Ec2Client ec2;
    private String instanceId = "i-0f0cd6d21c8507da4";

    private Monitor() {
        ec2 = Ec2Client.builder().region(region).build();
    }

    private void monitor() {
        describeEC2Instances();
        monitorEC2Instances();
//        unmonitorEC2Instances();
        for (String s: getRunningInstancesId()){
            System.out.println(s);
        }
    }

    private void monitorEC2Instances() {
        MonitorInstancesRequest request = MonitorInstancesRequest
                .builder()
                .instanceIds(this.instanceId)
                .build();
        ec2.monitorInstances(request);
    }

    private void unmonitorEC2Instances() {
        UnmonitorInstancesRequest request = UnmonitorInstancesRequest
                .builder()
                .instanceIds(this.instanceId)
                .build();
        ec2.unmonitorInstances(request);
    }

    private void describeEC2Instances() {

        boolean done = false;
        String nextToken = null;
        Integer maxResult = 6;

        try {

            do {
                DescribeInstancesRequest request = DescribeInstancesRequest
                        .builder()
                        .maxResults(maxResult)
                        .nextToken(nextToken)
                        .build();
                DescribeInstancesResponse response = ec2.describeInstances(request);

                for (Reservation reservation : response.reservations()) {
                    for (Instance instance : reservation.instances()) {
                        System.out.printf(
                                "Found reservation with id %s, " +
                                        "AMI %s, " +
                                        "type %s, " +
                                        "state %s " +
                                        "and monitoring state %s",
                                instance.instanceId(),
                                instance.imageId(),
                                instance.instanceType(),
                                instance.state().name(),
                                instance.monitoring().state());
                        System.out.println("");
                    }
                }
                nextToken = response.nextToken();
            } while (nextToken != null);

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    private Set<String> getRunningInstancesId(){
        Set<String> runningSet = new HashSet<>();
        String nextToken = null;
        try {

            do {
                DescribeInstancesRequest request = DescribeInstancesRequest
                        .builder()
                        .maxResults(6)
                        .nextToken(nextToken)
                        .build();
                DescribeInstancesResponse response = ec2.describeInstances(request);

                for (Reservation reservation : response.reservations()) {
                    for (Instance instance : reservation.instances()) {
                        if (instance.state().name().toString().equals("running")){
                            runningSet.add(instance.instanceId());
                        }
                    }
                }
                nextToken = response.nextToken();
            } while (nextToken != null);

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return runningSet;
    }

}
