package com.yq.starter.cat.aop;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import static com.yq.starter.cat.constant.Types.RABBIT_MQ_RECEIVE;

/**
 * @auth: YQ
 * @date： 6/28/2019
 **/
@Aspect
public class RabbitMqReceiveAspectLogger extends DefaultAspectLogger {

    @Around("execution(public void org.springframework.amqp.rabbit.listener.adapter.MessagingMessageListenerAdapter.onMessage(org.springframework.amqp.core.Message, com.rabbitmq.client.Channel))")
    @Override
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        return super.doAround(pjp);
    }

    @Override
    Transaction getTransaction(ProceedingJoinPoint joinPoint) {
        MessageProperties properties = ((Message) joinPoint.getArgs()[0]).getMessageProperties();
        return Cat.newTransaction(RABBIT_MQ_RECEIVE.name(), (StringUtils.isBlank(properties.getReceivedExchange()) ? "" : properties.getReceivedExchange() + " - ") + properties.getReceivedRoutingKey());
    }
}
