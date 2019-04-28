package com.callcenter.conf;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConf implements AsyncConfigurer  {

	@Bean(name = "threadPoolTaskExecutor")
	public TaskExecutor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(30);
		executor.setQueueCapacity(100);
        executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setThreadNamePrefix("CallCenter-");
		executor.initialize();
		return executor;
	}
	
	
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncHandler();
    }
}
