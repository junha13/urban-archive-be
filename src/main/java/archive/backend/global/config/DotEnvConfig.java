package archive.backend.global.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.Order;

@Configuration
public class DotEnvConfig {

    private final Dotenv dotenv;

    // DotEnv 로드 및 데이터 조회
    public DotEnvConfig() {
        this.dotenv = Dotenv.configure()
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();

        //dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
    }

    @Bean
    public Dotenv dotenv() {
        return this.dotenv;
    }
}