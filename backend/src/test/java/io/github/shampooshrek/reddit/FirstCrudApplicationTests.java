package io.github.shampooshrek.reddit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class FirstCrudApplicationTests {

  @Value("${spring.datasource.url}")
  private String datasourceUrl;

  void contextLoads() {
    System.out.println("Datasource URL: " + datasourceUrl);
  }
}
