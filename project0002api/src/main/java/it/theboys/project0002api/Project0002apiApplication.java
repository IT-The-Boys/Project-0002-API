package it.theboys.project0002api;

import it.theboys.project0002api.repository.BaseRepositoryHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(repositoryBaseClass = BaseRepositoryHelper.class)
public class Project0002apiApplication {

    public static void main(String[] args) {

        SpringApplication.run(Project0002apiApplication.class, args);
    }
}