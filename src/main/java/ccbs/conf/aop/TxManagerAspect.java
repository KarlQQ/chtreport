package ccbs.conf.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Stack;

@Component
@Aspect
@Slf4j
public class TxManagerAspect implements ApplicationContextAware {
    private ApplicationContext appContext;

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = appContext;
    }

    @Pointcut("@annotation(ccbs.conf.aop.MyTxManager)")
    public void myTxManager() {
    }

    @Pointcut("within(ccbs.service..*)")
    public void service() {
    }

    @Around(value = "service() && myTxManager() && @annotation(annotation)")
    public Object twiceAsOld(ProceedingJoinPoint pjp, MyTxManager annotation) throws Throwable {
        Stack<DataSourceTransactionManager> dsTxManagerStack = new Stack<DataSourceTransactionManager>();
        Stack<TransactionStatus> txStatusStack = new Stack<TransactionStatus>();
        try {
            if (!openTransaction(dsTxManagerStack, txStatusStack, annotation)) {
                return null;
            }
            Object ret = pjp.proceed();
            commit(dsTxManagerStack, txStatusStack);
            return ret;
        } catch (Throwable e) {
            rollback(dsTxManagerStack, txStatusStack);
            log.error("[{}::{}] - ", pjp.getTarget().getClass().getSimpleName(), pjp.getSignature().getName());
            log.error("{}", e.getMessage());
            throw e;
        }
    }

    /**
     * open 交易
     *
     * @param dsTxManagerStack
     * @param txStatusStack
     * @param myTxManager
     * @return
     */
    private boolean openTransaction(Stack<DataSourceTransactionManager> dsTxManagerStack, Stack<TransactionStatus> txStatusStack, MyTxManager myTxManager) {
        String[] txManagerNames = myTxManager.value();
        if (ArrayUtils.isEmpty(myTxManager.value())) {
            return false;
        }

        for (String beanName : txManagerNames) {
            DataSourceTransactionManager dsTxManager = appContext.getBean(beanName, DataSourceTransactionManager.class);
            TransactionStatus txStatus = dsTxManager.getTransaction(new DefaultTransactionDefinition());
            txStatusStack.push(txStatus);
            dsTxManagerStack.push(dsTxManager);
        }
        return true;
    }

    /**
     * commit 交易
     *
     * @param dsTxManagerStack
     * @param txStatus
     */
    private void commit(Stack<DataSourceTransactionManager> dsTxManagerStack, Stack<TransactionStatus> txStatus) {
        while (!dsTxManagerStack.isEmpty()) {
            dsTxManagerStack.pop().commit(txStatus.pop());
        }
    }

    /**
     * rollback 交易
     *
     * @param dsTxManagerStack
     * @param txStatus
     */
    private void rollback(Stack<DataSourceTransactionManager> dsTxManagerStack, Stack<TransactionStatus> txStatus) {
        while (!dsTxManagerStack.isEmpty()) {
            dsTxManagerStack.pop().rollback(txStatus.pop());
        }
    }

}
