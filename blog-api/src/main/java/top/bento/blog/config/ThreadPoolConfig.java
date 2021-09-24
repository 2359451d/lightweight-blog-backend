package top.bento.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync // Enable multi-threads
public class ThreadPoolConfig {
    @Bean("taskExecutor")
    public Executor asyncServiceExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // set core threads number in the pool
        executor.setCorePoolSize(5);
        // set max pool size
        executor.setMaxPoolSize(20);
        // set queue size
        executor.setQueueCapacity(Integer.MAX_VALUE);
        // set thread keeping alive time
        executor.setKeepAliveSeconds(60);
        // set default thread name
        executor.setThreadNamePrefix("码神之路博客项目");
        // once all the tasks to be done shut down the pool
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }
}
