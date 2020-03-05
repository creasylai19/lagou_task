package com.lagou.edu.pojo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class BeanDefinition {

    private Object source;

    private String beanClass;

    private String beanName;

    private List<Method> transactionMethods;

    private List<Field> autowiredFields;

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    public String getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(String beanClass) {
        this.beanClass = beanClass;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public List<Method> getTransactionMethods() {
        return transactionMethods;
    }

    public void setTransactionMethods(List<Method> transactionMethods) {
        this.transactionMethods = transactionMethods;
    }

    public List<Field> getAutowiredFields() {
        return autowiredFields;
    }

    public void setAutowiredFields(List<Field> autowiredFields) {
        this.autowiredFields = autowiredFields;
    }
}
