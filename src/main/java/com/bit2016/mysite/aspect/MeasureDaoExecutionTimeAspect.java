package com.bit2016.mysite.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class MeasureDaoExecutionTimeAspect {
	
	@Around("execution( * *..repository.*.*(..) ) || execution( * *..service.*.*(..) ) || execution( * *..controller.*.*(..) )")
	public Object around(ProceedingJoinPoint pjp) throws Throwable{
		StopWatch stopWatch = new StopWatch();
		
		stopWatch.start();
		
		//dao method 실행
		Object result = pjp.proceed();
		
		stopWatch.stop();
		
		String className = pjp.getTarget().getClass().getName();
		String methodName = pjp.getSignature().getName();
		String taskName = className + "." + methodName;
		
		System.out.println("[ExecutionTime]["+taskName+"]"+":"+stopWatch.getLastTaskTimeMillis()+"millis");
		
		return result;
	}
}
