package com.richard.interception.internal;

import com.richard.interception.Element;
import com.richard.interception.ExecutableElement;
import com.richard.interception.ParameterElement;
import com.richard.interception.TypeElement;
import com.richard.interception.VariableElement;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: Richard paco
 * Date: 2018/4/23
 */
public class ElementBuilderImpl implements IElementBuilder {

    private int threadType = 0; //0 current,1 async,2 ui
    private String name;
    private int code;
    private int elementType;
    private Class[] types;
    private Object[] args = new Object[0];
    private String[] typeNames;
    private Class returnType;
    private Class<?>[] thrownTypes;
    private Object host;
    private Class<? extends Annotation>[] executionAnnotations;
    private Map<Class<? extends Annotation>, Map<String, Object>> annotationValues;
    private Map<Integer, Map<Class<? extends Annotation>, Map<String, Object>>> typesAnnotationValues;

    public ElementBuilderImpl(int elementType, String name, int code, Class returnType,
                              Class[] types) {
        this.elementType = elementType;
        this.name = name;
        this.returnType = returnType;
        this.code = code;
        this.types = types;
    }

    @Override
    public IElementBuilder setArgs(Object[] args) {
        this.args = args;
        return this;
    }

    @Override
    public IElementMultipartBuilder setType(boolean async, boolean ui) {
        if (async) {
            threadType = 1;
        } else if (ui) {
            threadType = 2;
        }
        return this;
    }

    @Override
    public boolean async() {
        return threadType == 1;
    }

    @Override
    public boolean ui() {
        return threadType == 2;
    }

    @Override
    public IElementBuilder setTypeNames(String[] names) {
        this.typeNames = names;
        return this;
    }

    @Override
    public IElementBuilder addAnnotationToPosition(int position,
                                                   Class<? extends Annotation>[] annotations) {
        return this;
    }


    @Override
    public IElementBuilder setAnnotation(Class<? extends Annotation>[] annotation) {
        executionAnnotations = annotation;
        return this;
    }

    @Override
    public IElementBuilder addAnnotationValues(Class<? extends Annotation> annotation, String key,
                                               Object value) {
        if (annotationValues == null) {
            annotationValues = new ConcurrentHashMap<>();
        }
        Map<String, Object> values = annotationValues.get(annotation);
        if (values == null) {
            values = new ConcurrentHashMap<>();
            values.put(key, value);
            annotationValues.put(annotation, values);
        } else {
            values.put(key, value);
        }
        return this;
    }

    @Override
    public IElementBuilder addTypesAnnotationValues(int position,
                                                    Class<? extends Annotation> annotation,
                                                    String key, Object value) {
        if (typesAnnotationValues == null) {
            typesAnnotationValues = new ConcurrentHashMap<>();
        }
        Map<Class<? extends Annotation>, Map<String, Object>> types = typesAnnotationValues.get(
                position);
        if (types == null) {
            Map<String, Object> values = new ConcurrentHashMap<>();
            types = new ConcurrentHashMap<>();
            values.put(key, value);
            types.put(annotation, values);
        } else {
            Map<String, Object> values = types.get(annotation);
            values.put(key, value);
        }
        return this;
    }

    @Override
    public IElementBuilder setThrownTypes(Class<?>[] thrownTypes) {
        this.thrownTypes = thrownTypes;
        return this;
    }

    @Override
    public IElementBuilder setHost(Object host) {
        this.host = host;
        return this;
    }

    @Override
    public IElementController generate(IElementExecutionLoader buildLoader) {
        return new Builder(elementType, this, buildLoader).get();
    }

    private static final class Builder {

        private Element element;
        private ElementBuilderImpl builder;
        private IElementExecutionLoader loader;

        private Builder(int type, ElementBuilderImpl builder, IElementExecutionLoader loader) {
            this.builder = builder;
            this.loader = loader;
            switch (type) {
                case 0://type
                    element = new TypeElement() {
                        @Override
                        public String getPackageName() {
                            return builder.getClass().getPackage().getName();
                        }

                        @Override
                        public Object getHost() {
                            return builder.host;
                        }

                        @Override
                        public String getSimpleName() {
                            return builder.name;
                        }
                    };
                    break;

                case 1://field
                    element = new VariableElement() {
                        @Override
                        public Class<?> getVariableType() {
                            return builder.returnType;
                        }

                        @Override
                        public Object getValue() {
                            return loader.build(builder.code, false, new Object[0]);
                        }

                        @Override
                        public void setValue(Object value) {
                            verifyValueType(builder.returnType, value);
                            loader.build(builder.code, true, new Object[]{value});
                        }

                        @Override
                        public Object getHost() {
                            return builder.host;
                        }

                        @Override
                        public String getSimpleName() {
                            return builder.name;
                        }
                    };
                    break;

                case 2://method
                    element = new ExecutableElement() {
                        @Override
                        public ParameterElement[] getParameters() {
                            return buildParameters();
                        }

                        @Override
                        public Class<?> getReturnType() {
                            return builder.returnType;
                        }

                        @Override
                        public Class<?>[] getThrownTypes() {
                            return builder.thrownTypes;
                        }

                        @Override
                        public Object proceed() {
                            return loader.build(builder.code, true, builder.args);
                        }

                        @Override
                        public Object getHost() {
                            return builder.host;
                        }

                        @Override
                        public String getSimpleName() {
                            return builder.name;
                        }
                    };
                    break;
            }
        }

        private ParameterElement[] buildParameters() {
            if (builder.types == null) {
                return new ParameterElement[0];
            } else {
                ParameterElement[] elements = new ParameterElement[builder.types.length];
                for (int i = 0; i < builder.types.length; i++) {
                    int finalI = i;
                    ParameterElement parameter = new ParameterElement() {
                        @Override
                        public Class<?> getVariableType() {
                            return builder.types[finalI];
                        }

                        @Override
                        public Object getValue() {
                            return builder.args[finalI];
                        }

                        @Override
                        public Map<String, Object> getAnnotation(
                                Class<? extends Annotation> annotation) {
                            Map<Class<? extends Annotation>, Map<String, Object>> valuesMap = builder.typesAnnotationValues.get(
                                    finalI);
                            if (valuesMap != null) {
                                return valuesMap.get(annotation);
                            }
                            return null;
                        }

                        @Override
                        public Object getHost() {
                            return builder.host;
                        }

                        @Override
                        public String getSimpleName() {
                            return builder.typeNames[finalI];
                        }
                    };
                    elements[i] = parameter;
                }
                return elements;
            }
        }

        private void verifyValueType(Class<?> clazz, Object value) {
            if (!clazz.equals(value.getClass()) && !clazz.isAssignableFrom(value.getClass())) {
                throw new IllegalArgumentException("type not equals!");
            }
        }

        private IElementController get() {
            return new IElementController() {
                @Override
                public Map<String, Object> getAnnotationValue(
                        Class<? extends Annotation> annotation) {
                    Map<String, Object> values = new LinkedHashMap<>();
                    if (builder.annotationValues != null && builder.annotationValues.containsKey(
                            annotation)) {
                        Map<String, Object> annotationValues = builder.annotationValues.get(
                                annotation);
                        if (annotationValues != null) return annotationValues;
                    }
                    return values;
                }

                @Override
                public Object processed() {
                    return loader.build(builder.code, true, builder.args);
                }

                @Override
                public Element getElement() {
                    return element;
                }

                @Override
                public Class<? extends Annotation>[] annotations() {
                    return builder.executionAnnotations;
                }
            };
        }
    }

}
