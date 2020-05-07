package com.creasy.filter;

import com.creasy.pojo.MethodInvokeObj;
import com.creasy.service.IServiceProvider;
import org.apache.dubbo.rpc.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class TPMonitorFilter implements Filter, Runnable {

    private static ConcurrentLinkedQueue<MethodInvokeObj> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
    private static AtomicLong lastCountTime = new AtomicLong(System.currentTimeMillis());
    private static AtomicInteger count = new AtomicInteger(0);

    public TPMonitorFilter(){
        Executors.newSingleThreadScheduledExecutor()
                .scheduleWithFixedDelay(this, 1, 5, TimeUnit.SECONDS);
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        if( (System.currentTimeMillis() - lastCountTime.longValue()) > 60*1000 ){
            synchronized (TPMonitorFilter.class){
                lastCountTime.set(System.currentTimeMillis());
                int andSet = count.getAndSet(0);
                System.out.println("----->上一分钟共计进行了" + andSet + "次远程调用");
            }
        }
        count.addAndGet(1);
        long startTime = System.currentTimeMillis();
        Result result = invoker.invoke(invocation);
        long endTime = System.currentTimeMillis();
        if( invocation instanceof RpcInvocation ){
            InvokeMode invokeMode = ((RpcInvocation) invocation).getInvokeMode();
            StringBuilder sb = new StringBuilder();
            if( invokeMode == InvokeMode.SYNC ) {
                MethodInvokeObj methodInvokeObj = new MethodInvokeObj(invocation.getMethodName(), startTime, (int) (endTime - startTime), (String) result.getValue());
                concurrentLinkedQueue.add(methodInvokeObj);
                sb.append("同步调用:").append(invocation.getMethodName());
            } else {
                sb.append("异步调用:").append(invocation.getMethodName());
            }
            sb.append(":耗时：").append(endTime-startTime).append("ms");
            System.out.println(sb.toString());
        }

        return result;
    }

    @Override
    public void run() {
        //统计TP90、TP99
        Collection<MethodInvokeObj> methodInvokeObjs = Collections.unmodifiableCollection(concurrentLinkedQueue);
        long currentTime = System.currentTimeMillis();
        Method[] methods = IServiceProvider.class.getMethods();
        for (Method method : methods) {
            String name = method.getName();
            List<MethodInvokeObj> tp90Collect = methodInvokeObjs.stream()
                    .filter(t -> t.getInvokeTime() >= currentTime - 60 * 1000)//过去一分钟
                    .filter(t -> t.getMethodName().equals(name))
                    .sorted(Comparator.comparingInt(MethodInvokeObj::getInvokeDuration)).collect(Collectors.toList());
            if( tp90Collect.size() > 0 ){
                long skip = (long) (tp90Collect.size() * 0.9);
                if( skip >= tp90Collect.size() ){
                    skip = 0;
                }
                MethodInvokeObj methodInvokeObj = tp90Collect.stream().skip(skip).findFirst().get();
                System.out.println("----->" + name + ":TP90:" + methodInvokeObj.getInvokeDuration() + "ms." + new Date());
            }

            List<MethodInvokeObj> tp99Collect = methodInvokeObjs.stream()
                    .filter(t -> t.getInvokeTime() >= currentTime - 60 * 1000)//过去一分钟
                    .filter(t -> t.getMethodName().equals(name))
                    .sorted(Comparator.comparingInt(MethodInvokeObj::getInvokeDuration)).collect(Collectors.toList());
            if( tp99Collect.size() > 0 ){
                long skip = (long) (tp99Collect.size() * 0.9);
                if( skip >= tp99Collect.size() ){
                    skip = 0;
                }
                MethodInvokeObj methodInvokeObj = tp99Collect.stream().skip(skip).findFirst().get();
                System.out.println("----->" + name + ":TP99:" + methodInvokeObj.getInvokeDuration() + "ms." + new Date());
            }
        }
        List<MethodInvokeObj> collect = methodInvokeObjs.stream().filter(t -> t.getInvokeTime() < currentTime - 60 * 1000).collect(Collectors.toList());
        concurrentLinkedQueue.removeAll(collect);//移除一分钟之前的数据
    }
}
