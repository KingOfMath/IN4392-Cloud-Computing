package cloud.controller;

import cloud.middleware.InstanceManager;
import cloud.middleware.Monitor;
import cloud.model.Task;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.ec2.model.Instance;

@RestController
public class InstanceController {
    private InstanceManager instanceManager = new InstanceManager();
    private Monitor monitor = new Monitor();

    @GetMapping("/post")
    public String getPriority(@RequestParam("priority") Integer priority, @RequestParam("type") Integer type) {
        String instanceId = instanceManager.createEC2Instance(priority, type);
        String instanceDNS = null;
        System.out.println("The instance ID is " + instanceId);
        for (Instance instance : monitor.getRunningInstances()) {
            if (instance.instanceId().equals(instanceId))
                instanceDNS = instance.publicDnsName();
        }
        return "instanceId:" + instanceId +
                "\ninstanceDNS:" + instanceDNS;
    }

    @PostMapping("/post")
    public String createInstanceByPriority(@RequestParam("priority") Integer priority, @RequestParam("type") Integer type) throws InterruptedException {
        String instanceId = instanceManager.createEC2Instance(priority, type);
        String instanceDNS = null;
        System.out.println("The instance ID is " + instanceId);
        for (Instance instance : monitor.getRunningInstances()) {
            if (instance.instanceId().equals(instanceId))
                instanceDNS = instance.publicDnsName();
        }
//        Thread.sleep(5000);
        return "instanceId:" + instanceId +
                "\ninstanceDNS:" + instanceDNS;
    }
}
