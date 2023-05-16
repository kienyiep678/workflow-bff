package dc.icdc.workflow;

import dc.icdc.workflow.helper.CustomRepositoryImpl;
import dc.icdc.lib.common.config.EnableCommonLib;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableProcessApplication
@EnableCommonLib
@EnableJpaRepositories(repositoryBaseClass = CustomRepositoryImpl.class)
public class Application {

  public static void main(String... args) {
    SpringApplication.run(Application.class, args);
  }

}