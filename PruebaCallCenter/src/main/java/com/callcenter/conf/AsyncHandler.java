package com.callcenter.conf;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

public class AsyncHandler  implements AsyncUncaughtExceptionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(AsyncHandler.class);

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
		logger.error("Ocurrio un error en el metodo {} dispatchCall  con la llamada {}.",method, params,ex);
    }
}
