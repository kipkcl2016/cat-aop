/**
 *
 */
package com.yq.starter.cat.aop;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.mysql.cj.jdbc.JdbcPreparedStatement;
import com.yq.starter.cat.constant.Types;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.lang.reflect.Method;

import static java.util.Objects.isNull;

/**
 * 说明：
 * 1. 本plugin只针对用PreparedStatement执行sql的情况，pointcut表达式中如果埋到其它类中则无效
 * @author andersen
 *
 */
@Aspect
@Slf4j
public class MysqlAspectLogger extends DefaultAspectLogger {

    private Method preparedSqlMethod;

    public MysqlAspectLogger() {
        try {
            preparedSqlMethod = JdbcPreparedStatement.class.getMethod("getPreparedSql");
            if (isNull(preparedSqlMethod)) {
                effective = false;
                log.warn("未能从JdbcPreparedStatement类中获得getPreparedSql方法，请检查mysql驱动包是否正确");
            }
        } catch (Exception e) {
            effective = false;
        }
    }

    @Around("execution( * com.mysql.cj.jdbc.JdbcPreparedStatement+.execute())" +
            "|| execution( * com.mysql.cj.jdbc.JdbcPreparedStatement+.executeQuery())" +
            "|| execution( * com.mysql.cj.jdbc.JdbcPreparedStatement+.executeUpdate())")
    @Override
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        return super.doAround(pjp);
    }

    @Override
    public Transaction getTransaction(ProceedingJoinPoint pjp) throws Exception {
        if (pjp.getTarget() instanceof JdbcPreparedStatement) {
            return Cat.newTransaction(Types.SQL.name(), (String) preparedSqlMethod.invoke(pjp.getTarget()));
//            JdbcPreparedStatement ps = (JdbcPreparedStatement) pjp.getTarget();
//            if (ps.getConnection() instanceof ConnectionImpl) {
//                Cat.logEvent(SQL_DATABASE.name(), ((ConnectionImpl) ((JdbcPreparedStatement) pjp.getTarget()).getConnection()).getURL());
//            }
//            return transaction;
        } else {
            return null;
        }
    }
}
