package cloud.model;

public class Machine {
    private String instanceId;
    private Double maxLoad;
    private Integer priority;
    private Double currentLoad;

    public Machine(String instanceId, Integer priority, Double maxLoad, Double currentLoad) {
        this.instanceId = instanceId;
        this.maxLoad = maxLoad;
        this.priority = priority;
        this.currentLoad = currentLoad;
    }

    public Double getCurrentLoad() {
        return currentLoad;
    }

    public void setCurrentLoad(Double currentLoad) {
        this.currentLoad = currentLoad;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public Double getMaxLoad() {
        return maxLoad;
    }

    public void setMaxLoad(Double maxLoad) {
        this.maxLoad = maxLoad;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
