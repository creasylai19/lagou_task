package com.lagou.sqlSession;

import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;
import com.lagou.transaction.JdbcTransaction;
import com.lagou.transaction.Transaction;

import java.lang.reflect.*;
import java.sql.SQLException;
import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;
    private Executor executor;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
        Transaction transaction = new JdbcTransaction(configuration.getDataSource(), false);
        this.executor = new simpleExecutor(transaction);
    }

    @Override
    public <E> List<E> selectList(String statementid, Object... params) throws Exception {

        //将要去完成对simpleExecutor里的query方法的调用
//        simpleExecutor simpleExecutor = new simpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementid);
        List<Object> list = executor.query(configuration, mappedStatement, params);

        return (List<E>) list;
    }

    @Override
    public <T> T selectOne(String statementid, Object... params) throws Exception {
        List<Object> objects = selectList(statementid, params);
        if(objects.size()==1){
            return (T) objects.get(0);
        }else {
            throw new RuntimeException("查询结果为空或者返回结果过多");
        }
    }

    @Override
    public <T> T getMapper(Class<?> mapperClass) {
        // 使用JDK动态代理来为Dao接口生成代理对象，并返回

        Object proxyInstance = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 底层都还是去执行JDBC代码 //根据不同情况，来调用selctList或者selectOne
                // 准备参数 1：statmentid :sql语句的唯一标识：namespace.id= 接口全限定名.方法名
                // 方法名：findAll
                Object retObject = null;
                String methodName = method.getName();
                String className = method.getDeclaringClass().getName();

                String statementId = className+"."+methodName;

                MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
                switch (mappedStatement.getSqlCommandType()) {
                    case SELECT:
                        // 准备参数2：params:args
                        // 获取被调用方法的返回值类型
                        Type genericReturnType = method.getGenericReturnType();
                        // 判断是否进行了 泛型类型参数化
                        if(genericReturnType instanceof ParameterizedType){
                            retObject = selectList(statementId, args);
                        } else {
                            retObject = selectOne(statementId,args);
                        }
                        break;
                    case INSERT:
                        retObject = insert(statementId, args);
                        break;
                    case UPDATE:
                        retObject = update(statementId, args);
                        break;
                    case DELETE:
                        retObject = delete(statementId, args);
                        break;
                    default:
                        throw new RuntimeException("未实现的类型");
                }
                return retObject;

            }
        });

        return (T) proxyInstance;
    }

    @Override
    public int update(String statementId, Object... params) throws Exception {
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        return executor.update(configuration, mappedStatement, params);
    }

    @Override
    public int delete(String statementId, Object... params) throws Exception {
        return update(statementId, params);
    }

    @Override
    public int insert(String statementId, Object... params) throws Exception {
        return update(statementId, params);
    }

    @Override
    public void close() {
        executor.close();
    }

    @Override
    public void rollback()  throws SQLException {
        executor.rollback();
    }

    @Override
    public void commit()  throws SQLException {
        executor.commit();
    }


}
