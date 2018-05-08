package com.zskpaco.interception.internal;

import com.zskpaco.interception.Element;
import com.zskpaco.interception.SurroundElement;
import com.zskpaco.interception.VariableElement;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: Richard paco
 * Date: 2018/4/27
 */
public class ElementMultipleBuilderImpl implements IElementMultipartBuilder {

    private int[] codes;
    private Class<?>[] types;
    private String[] names;
    private Object host;
    private Map<Integer, Map<String, Object>> typesAnnotationValues = new ConcurrentHashMap();

    private Class<? extends Annotation> variableAnnotation;
    private VariableElement[] variableElements;
    private Map<String, Object> variableAnnotationValues = new ConcurrentHashMap<>();

    public ElementMultipleBuilderImpl(int[] codes, Class<?>[] types) {
        this.codes = codes;
        this.types = types;
    }

    @Override
    public IElementMultipartBuilder setTypeNames(String[] names) {
        this.names = names;
        return this;
    }

    @Override
    public IElementMultipartBuilder addAnnotationToPosition(int position,
                                                            Class<? extends Annotation>[] annotations) {
        variableAnnotation = annotations[0];
        Map<String, Object> map = typesAnnotationValues.get(
                position);
        if (map == null) {
            map = new ConcurrentHashMap<>();
            typesAnnotationValues.put(position, map);
        }
        return this;
    }

    @Override
    public IElementMultipartBuilder addTypesAnnotationValues(int position,
                                                             Class<? extends Annotation> annotation,
                                                             String key, Object value) {
        typesAnnotationValues.get(position).put(key, value);
        return this;
    }

    @Override
    public IElementMultipartBuilder setHost(Object host) {
        this.host = host;
        return this;
    }

    private void buildVariables(IElementExecutionLoader buildLoader) {
        if (variableElements == null) {
            variableElements = new VariableElement[codes.length];
            for (int i = 0; i < codes.length; i++) {
                Class type = types[i];
                int code = codes[i];
                String name = names[i];

                VariableElement variableElement = new VariableElement() {
                    @Override
                    public Class<?> getVariableType() {
                        return type;
                    }

                    @Override
                    public Object getValue() {
                        return buildLoader.build(code, false, null);
                    }

                    @Override
                    public void setValue(Object value) {
                        buildLoader.build(code, true, new Object[]{value});
                    }

                    @Override
                    public Object getHost() {
                        return host;
                    }

                    @Override
                    public String getSimpleName() {
                        return name;
                    }
                };
                variableAnnotationValues.put(name, typesAnnotationValues.get(i));
                variableElements[i] = variableElement;
            }
        }
        typesAnnotationValues = null;
    }

    @Override
    public IElementController generate(IElementExecutionLoader buildLoader) {
        buildVariables(buildLoader);
        return new IElementController() {
            @Override
            public Map<String, Object> getAnnotationValue(Class<? extends Annotation> annotation) {
                return variableAnnotationValues;
            }

            @Override
            public Object processed() {
                return null;
            }

            @Override
            public Element getElement() {
                return new SurroundElement() {
                    @Override
                    public VariableElement[] getVariables() {
                        return variableElements;
                    }

                    @Override
                    public Object getHost() {
                        return host;
                    }

                    @Override
                    public String getSimpleName() {
                        return host.getClass().getSimpleName();
                    }
                };
            }

            @Override
            public Class<? extends Annotation>[] annotations() {
                return new Class[]{variableAnnotation};
            }
        };
    }

}
