package middleware;

import com.amazonaws.services.ecs.AmazonECS;
import com.amazonaws.services.ecs.AmazonECSClientBuilder;
import com.amazonaws.services.ecs.model.UpdateServiceRequest;
import com.amazonaws.services.ecs.model.UpdateServiceResult;

import java.net.MalformedURLException;
import java.net.URL;

public class Client {

    private String webURL = "http://localhost:63342/cloudC/static/index.html?_ijt=ghnenfsafts4a0coml6hbnuidq";
    private AmazonECS ecsClient;
    private String serviceName;
    private String task;

    public Client() throws MalformedURLException {
        this.ecsClient = AmazonECSClientBuilder.standard().build();
        URL url = new URL(webURL);
        UpdateServiceRequest request = new UpdateServiceRequest()
                .withService("my-http-service")
                .withTaskDefinition("amazon-ecs-sample");
        UpdateServiceResult response = ecsClient.updateService(request);
    }

    public void run(){
        System.out.println("start client!");

    }

}
