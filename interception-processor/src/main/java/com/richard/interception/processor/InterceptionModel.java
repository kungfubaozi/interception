package com.richard.interception.processor;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Richard paco
 * Date: 2018/4/27
 */
public class InterceptionModel {

    private String module;
    private String root;
    private List<InterceptorsBean> interceptors = new ArrayList<>();

    public String getRoot() {
        return root;
    }

    public InterceptionModel setRoot(String root) {
        this.root = root;
        return this;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public List<InterceptorsBean> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<InterceptorsBean> interceptors) {
        this.interceptors = interceptors;
    }

    public static class InterceptorsBean {

        private String annotation;
        private String debug;
        private boolean ds;
        private String release;
        private boolean rs;
        private String processor;
        @SerializedName("default")
        private List<DefaultBean> defaultX = new ArrayList<>();

        public String getAnnotation() {
            return annotation;
        }

        public void setAnnotation(String annotation) {
            this.annotation = annotation;
        }

        public String getDebug() {
            return debug;
        }

        public void setDebug(String debug) {
            this.debug = debug;
        }

        public boolean isDs() {
            return ds;
        }

        public void setDs(boolean ds) {
            this.ds = ds;
        }

        public String getRelease() {
            return release;
        }

        public void setRelease(String release) {
            this.release = release;
        }

        public boolean isRs() {
            return rs;
        }

        public void setRs(boolean rs) {
            this.rs = rs;
        }

        public String getProcessor() {
            return processor;
        }

        public void setProcessor(String processor) {
            this.processor = processor;
        }

        public List<DefaultBean> getDefaultX() {
            return defaultX;
        }

        public void setDefaultX(List<DefaultBean> defaultX) {
            this.defaultX = defaultX;
        }

        public static class DefaultBean {

            private String key;
            private Object value;
            private String vt;

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public Object getValue() {
                return value;
            }

            public void setValue(Object value) {
                this.value = value;
            }

            public String getVt() {
                return vt;
            }

            public void setVt(String vt) {
                this.vt = vt;
            }
        }

    }
}
