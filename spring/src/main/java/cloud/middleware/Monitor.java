package cloud.middleware;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class Monitor {
    public static void main(String[] args) {
        Monitor monitor = new Monitor();
        System.out.println(monitor.getPublicDNS());
    }

    private static Region region = Region.EU_WEST_2;
    private static Ec2Client ec2;

    public Monitor() {
        ec2 = Ec2Client.builder().region(region).build();
    }

    public void monitorS3Buckets() {
        Set<String> nameList = new HashSet<>();
        S3Client s3 = S3Client.builder().region(region).build();
        ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder().build();
        ListBucketsResponse listBucketsResponse = s3.listBuckets(listBucketsRequest);
        listBucketsResponse.buckets().stream().forEach(x -> nameList.add(x.name()));
        for (String bucketName : nameList) {
            try {
                ListObjectsRequest listObjects = ListObjectsRequest
                        .builder()
                        .bucket(bucketName)
                        .build();

                ListObjectsResponse res = s3.listObjects(listObjects);
                List<S3Object> objects = res.contents();

                System.out.print("\n Current bucket : " + bucketName);
                for (S3Object s3Object : objects) {
                    System.out.print("\n The name of the key : " + s3Object.key());
                    System.out.print("\n The object : " + s3Object.size() / 1024 + " KBs");
                    System.out.print("\n The eTag : " + s3Object.eTag());
                    System.out.print("\n The last modified time : " + s3Object.lastModified());
                    System.out.print("\n The owner : " + s3Object.owner());
                    System.out.println();
                }
            } catch (S3Exception e) {
                System.err.println(e.awsErrorDetails().errorMessage());
                System.exit(1);
            }
        }
    }

    public void monitorEC2Instances(String instanceId) {
        MonitorInstancesRequest request = MonitorInstancesRequest
                .builder()
                .instanceIds(instanceId)
                .build();
        ec2.monitorInstances(request);
    }

    public void unmonitorEC2Instances(String instanceId) {
        UnmonitorInstancesRequest request = UnmonitorInstancesRequest
                .builder()
                .instanceIds(instanceId)
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
                                        "and monitoring DNS %s",
                                instance.instanceId(),
                                instance.imageId(),
                                instance.instanceType(),
                                instance.state().name(),
                                instance.publicDnsName());
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

    public Set<String> getRunningInstancesIdByName(String name) {
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
                        if (instance.state().name().toString().equals("running")
                                && instance.tags().get(0).value().equals(name)) {
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

    public Set<Instance> getRunningInstancesByName(String name) {
        Set<Instance> runningSet = new HashSet<>();
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
                        if (instance.state().name().toString().equals("running")
                                && instance.tags().get(0).value().equals(name)) {
                            runningSet.add(instance);
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

    public Set<Instance> getRunningInstances() {
        Set<Instance> runningSet = new HashSet<>();
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
                    runningSet.addAll(reservation.instances());
                }
                nextToken = response.nextToken();
            } while (nextToken != null);

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return runningSet;
    }

    public Set<String> getPublicDNS(){
        Set<String> DNS = new HashSet<>();
        for (Instance instance : getRunningInstances()) {
            String keyName = instance.keyName();
            if (keyName == null) continue;
            if (keyName.equals("Liu")){
                DNS.add(instance.publicDnsName());
            }
        }
        return DNS;
    }

}
