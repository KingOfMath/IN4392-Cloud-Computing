package cloud.middleware;

import cloud.model.Machine;
import cloud.model.Task;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.Instance;
import software.amazon.awssdk.services.ec2.model.InstanceType;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class Scheduler {
    public static void main(String[] args) {
        Scheduler s = new Scheduler();
        s.schedule();

    }

    private static Region region = Region.EU_WEST_2;
    private static Ec2Client ec2;
    private Monitor monitor = new Monitor();
    private Set<Machine> machines = new HashSet<>();
    private String name = "Liu";

    public void schedule() {
        Task t1 = new Task(1,200);
        Task t2 = new Task(2,500);
        Task t3 = new Task(3,800);

    }



    public Scheduler() {
        ec2 = Ec2Client.builder().region(region).build();
        Set<Instance> instances = monitor.getRunningInstancesByName(name);
        for (Instance instance : instances) {
            InstanceType type = instance.instanceType();
            String id = instance.instanceId();
            machines.add(new Machine(id, getPriority(type), getLoad(type), .0));
        }
    }

    public Integer getPriority(InstanceType instance) {
        switch (instance) {
            case T2_NANO:
                return 1;
            case T2_MICRO:
                return 2;
            case T2_SMALL:
                return 3;
            case T2_MEDIUM:
                return 4;
            case T2_LARGE:
                return 5;
        }
        return 0;
    }

    public Double getLoad(InstanceType instance) {
        switch (instance) {
            case T2_NANO:
                return 0.5 * 1024 * 1024;
            case T2_MICRO:
                return 1.0 * 1024 * 1024;
            case T2_SMALL:
                return 2.0 * 1024 * 1024;
            case T2_MEDIUM:
                return 4.0 * 1024 * 1024;
            case T2_LARGE:
                return 8.0 * 1024 * 1024;
        }
        return .0;
    }

    public Monitor getMonitor() {
        return monitor;
    }

    public Set<Machine> getMachines() {
        return machines;
    }
}
