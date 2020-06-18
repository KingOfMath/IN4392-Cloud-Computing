package cloud.model;

import javax.persistence.*;

//@Entity
//@Table(name = "task")
public class Task {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(columnDefinition = "serial")
    private Integer taskId;
    private String fileName;
    private double load;

    public Task(Integer taskId, double load) {
        this.taskId = taskId;
        this.load = load;
    }

    public Task(Integer taskId, String fileName, double load) {
        this.taskId = taskId;
        this.fileName = fileName;
        this.load = load;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public double getLoad() {
        return load;
    }

    public void setLoad(double load) {
        this.load = load;
    }
}
