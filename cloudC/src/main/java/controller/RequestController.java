package controller;

import model.Task;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class RequestController {
//    @RequestMapping(value="/py", method= RequestMethod.GET)
//    public String pythonForm(Model model) {
//        model.addAttribute("pythonFile", new Task());
//        return "py";
//    }
//
//    @RequestMapping(value="/py", method=RequestMethod.POST)
//    public String getPythonRequest(@ModelAttribute Task task, Model model) {
//        model.addAttribute("pythonFile", task);
//        return "requests";
//    }

    @RequestMapping(value="/sayhello", method=RequestMethod.GET)
    public String sayHelloForm(Model model) {
        model.addAttribute("helloMessage", new Task());
        return "sayhello";
    }

    @RequestMapping(value="/sayhello", method=RequestMethod.POST)
    public String sayHello(@ModelAttribute Task helloMessage, Model model) {
        model.addAttribute("helloMessage", helloMessage);
        return "message";
    }

}
