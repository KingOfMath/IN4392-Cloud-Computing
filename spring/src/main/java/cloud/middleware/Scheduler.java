package cloud.middleware;

import cloud.model.Machine;
import cloud.model.Task;
import org.hibernate.validator.constraints.EAN;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.Instance;
import software.amazon.awssdk.services.ec2.model.InstanceType;

import java.util.*;

public class Scheduler {

    private static Region region = Region.EU_WEST_2;
    private static Ec2Client ec2;
    private Monitor monitor = new Monitor();
    private Set<Machine> machines = new HashSet<>();
    private String groupName = "Liu";
    private FaultTolerancer faultTolerancer = new FaultTolerancer();
    private double factor = 0.8;
    PriorityQueue<Machine> queue = new PriorityQueue<Machine>(new Comparator<Machine>() {
        public int compare(Machine m1, Machine m2) {
            return Double.compare(m1.getLeftLoad(), m2.getLeftLoad());
        }
    });
    List<Machine> arrayLists = new ArrayList<>();

    public Scheduler() {
        ec2 = Ec2Client.builder().region(region).build();
        Set<Instance> instances = monitor.getRunningInstancesByName(groupName);
        for (Instance instance : instances) {
            InstanceType type = instance.instanceType();
            String id = instance.instanceId();
            machines.add(new Machine(id, getPriority(type), getLoad(type), .0));
        }
    }

    public void executeListBySequence(Set<Task> tasks) throws Exception {
        arrayLists.addAll(machines);
        int size = arrayLists.size();
        int count = 0;
        for (Task task : tasks) {
            count %= size;
            Machine current = arrayLists.get(count);
            if (current == null) break;
            if (current.getCurrentLoad() >= factor * current.getMaxLoad()) {
                faultTolerancer.terminateMachine(groupName,current.getInstanceId());
                arrayLists.remove(current);
                continue;
            }
            current.setCurrentLoad(current.getCurrentLoad() + task.getLoad());
            count += 1;
        }
    }

    public void executeQueueByGreedy(Set<Task> tasks) {
        queue.addAll(machines);

        for (Task task : tasks) {
            Machine current = null;
            while (!queue.isEmpty() && current == null) {
                current = queue.poll();
                if (current.getCurrentLoad() >= factor * current.getMaxLoad()) current = null;
            }
            if (current == null) break;
            current.setCurrentLoad(current.getCurrentLoad() + task.getLoad());
            if (current.getCurrentLoad() < factor * current.getMaxLoad()) {
                queue.add(current);
            }
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
