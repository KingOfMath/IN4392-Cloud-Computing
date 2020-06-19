package cloud.controller;

import cloud.middleware.InstanceManager;
import cloud.model.Task;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
public class InstanceController {

    private InstanceManager instanceManager = new InstanceManager();

    @GetMapping("/post")
    public String getPriority(@RequestParam("priority") Integer priority){
        String instanceId = instanceManager.createEC2Instance(priority);
        System.out.println("The instance ID is " + instanceId);
        return "created!";
    }

    @PostMapping("/post")
    public String createInstanceByPriority(@RequestParam("priority") Integer priority){
        String instanceId = instanceManager.createEC2Instance(priority);
        System.out.println("The instance ID is " + instanceId);
        return "created!";
    }
}
