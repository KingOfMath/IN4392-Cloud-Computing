package cloud.middleware;

import software.amazon.awssdk.services.ec2.model.Instance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.*;

public class PostFile {
    public static void main(String[] args) throws IOException, InterruptedException {
        PostFile postFile = new PostFile();
        postFile.post();
    }

    private Monitor monitor = new Monitor();
    private String keyName = "\"Liu.pem\"";
    private String filePath = "src/main/resources/filelist/";
    private List<String> fileList = new ArrayList<String>(
            Arrays.asList("2.jpg", "3.jpg", "4.jpg")
    );

    public void post() throws IOException, InterruptedException {

        String fileName = null;

        for (Instance instance : monitor.getRunningInstances()) {
            String k = instance.keyName();
            if (k == null) continue;
            if (k.equals("Liu")) {
                String DNS = instance.publicDnsName();
                String type = instance.instanceType().toString();
                System.out.println(DNS);
                System.out.println(type);

                switch (type){
                    case "t2.nano":
                        fileName = fileList.get(0);
                        break;
                    case "t2.micro":
                        fileName = fileList.get(1);
                        break;
                    case "t2.small":
                        fileName = fileList.get(2);
                        break;
                }

                if (fileName == null) continue;
                System.out.println(fileName);

                String res = "scp -i " + keyName + " " + filePath + fileName + " ubuntu@" + DNS + ":/home/ubuntu";
                Thread.sleep(1000);
                String[] cmd = new String[]{"/bin/sh", "-c", res};
                Process process = Runtime.getRuntime().exec(cmd);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder stringBuffer = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line).append("\n");
                }
                System.out.println(stringBuffer.toString());
            }
        }
    }

}
