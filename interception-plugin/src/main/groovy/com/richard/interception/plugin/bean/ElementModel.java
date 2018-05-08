package com.richard.interception.plugin.bean;

import org.objectweb.asm.Type;

import java.util.LinkedList;
import java.util.List;

public class ElementModel {

    private int processId;
    private int type;
    private String accessName;
    private Type[] desc;
    private String name;
    private String returnType;
    private boolean async;
    private boolean ui;
    private List<InterceptionModel.InterceptorsBean> interceptor = new LinkedList<>();
    private List<InterceptionModel.InterceptorsBean> subInterceptor = new LinkedList<>();

    public boolean isAsync() {
        return async;
    }

    public ElementModel setAsync(boolean async) {
        this.async = async;
        return this;
    }

    public boolean isUi() {
        return ui;
    }

    public ElementModel setUi(boolean ui) {
        this.ui = ui;
        return this;
    }

    public ElementModel setInterceptor(List<InterceptionModel.InterceptorsBean> interceptor) {
        this.interceptor = interceptor;
        return this;
    }

    public List<InterceptionModel.InterceptorsBean> getSubInterceptor() {
        return subInterceptor;
    }

    public ElementModel setSubInterceptor(List<InterceptionModel.InterceptorsBean> subInterceptor) {
        this.subInterceptor = subInterceptor;
        return this;
    }

    private String[] getAndSet;

    public String[] getGetAndSet() {
        return getAndSet;
    }

    public ElementModel setGetAndSet(String[] getAndSet) {
        this.getAndSet = getAndSet;
        return this;
    }

    public String getAccessName() {
        return accessName;
    }

    public ElementModel setAccessName(String accessName) {
        this.accessName = accessName;
        return this;
    }

    public Type[] getDesc() {
        return desc;
    }

    public ElementModel setDesc(Type[] desc) {
        this.desc = desc;
        return this;
    }

    public String getReturnType() {
        return returnType;
    }

    public ElementModel setReturnType(String returnType) {
        this.returnType = returnType;
        return this;
    }

    public String getName() {
        return name;
    }

    public ElementModel setName(String name) {
        this.name = name;
        return this;
    }

    public int getProcessId() {
        return processId;
    }

    public ElementModel setProcessId(int processId) {
        this.processId = processId;
        return this;
    }

    public int getType() {
        return type;
    }

    public ElementModel setType(int type) {
        this.type = type;
        return this;
    }

    public List<InterceptionModel.InterceptorsBean> getInterceptor() {
        return interceptor;
    }

}
