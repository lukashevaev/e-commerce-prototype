package com.bubusyaka.demo.configuration;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.interceptor.PerformanceMonitorInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@Aspect
public class MonitoringConfiguration {

   /* @Pointcut(
            "execution(public void com.bubusyaka.demo.configuration.OrderInitialization.sendOrders(..))"
    )
    public void monitor() { }

    @Bean
    public PerformanceMonitorInterceptor performanceMonitorInterceptor() {
        return new PerformanceMonitorInterceptor(true);
    }

    @Bean
    public Advisor performanceMonitorAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("com.bubusyaka.demo.configuration.MonitoringConfiguration.monitor()");
        return new DefaultPointcutAdvisor(pointcut, performanceMonitorInterceptor());
    }*/

    @Bean
    public TimedAspect timedAspect(MeterRegistry meterRegistry){
        return new TimedAspect(meterRegistry);
    }

}

