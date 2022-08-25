package demo.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("docker")
    public String docker() {
        System.out.println("docker程序部署成功");
        return "hello, this is used jks+k8s+docker";
    }
}
