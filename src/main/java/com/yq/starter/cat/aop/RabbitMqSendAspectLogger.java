package com.yq.starter.cat.aop;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import static com.yq.starter.cat.constant.Types.RABBIT_MQ_SEND;

/**
 * @auth: YQ
 * @date： 6/28/2019
 **/
@Aspect
public class RabbitMqSendAspectLogger extends DefaultAspectLogger {

    @Around("execution(public void org.springframework.amqp.rabbit.core.RabbitTemplate.send(String,String, org.springframework.amqp.core.Message,org.springframework.amqp.rabbit.support.CorrelationData))")
    @Override
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        return super.doAround(pjp);
    }

    @Override
    Transaction getTransaction(ProceedingJoinPoint joinPoint) {
        String exchange = (String) joinPoint.getArgs()[0];
        String routingKey = (String) joinPoint.getArgs()[1];
        return Cat.newTransaction(RABBIT_MQ_SEND.name(), (StringUtils.isBlank(exchange) ? "" : exchange + " - ") + routingKey);
    }
}
