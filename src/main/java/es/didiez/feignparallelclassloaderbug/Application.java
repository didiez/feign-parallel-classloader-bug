package es.didiez.feignparallelclassloaderbug;

import static java.lang.System.out;
import java.util.stream.LongStream;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Bean
    CommandLineRunner run(FakeClient fakeClient){
        return args -> {
            LongStream.range(1, 100).boxed()                    
                    .parallel()
                    .map(id -> fakeClient.getAlbum(id))
                    .forEach(out::println);
        };
    }

}
