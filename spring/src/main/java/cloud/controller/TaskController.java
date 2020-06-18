package cloud.controller;

import cloud.model.Task;
import cloud.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

//@RestController
//public class TaskController {
//
//    @Autowired
//    private TaskRepository taskRepository;
//
//    @GetMapping("/task")
//    public Page<Task> getTasks(Pageable pageable) {
//        return taskRepository.findAll(pageable);
//    }
//
//    @GetMapping("/post")
//    public String getForm(Model model){
//        model.addAttribute("task", new Task());
//        return "post";
//    }
//
//    @PostMapping("/submit")
//    public String postTasks(@ModelAttribute Task task){
//        Task newTask = new Task();
//        newTask.setFileName(task.getFileName());
//        newTask.setPriority(task.getPriority());
//        newTask.setTaskId(task.getTaskId());
//        taskRepository.save(newTask);
//        return "";
//    }
//
//}
