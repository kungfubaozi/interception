package com.zskpaco.interception.processor;

import com.google.auto.service.AutoService;
import com.google.gson.Gson;
import com.zskpaco.interception.AnnotationInterceptor;
import com.zskpaco.interception.Environment;
import com.zskpaco.interception.Interceptor;
import com.zskpaco.interception.Surround;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleTypeVisitor6;
import javax.lang.model.util.Types;

@AutoService(Processor.class)
public class InterceptionProcessor extends AbstractProcessor {

    private String moduleName;
    private String path;
    private File file;
    private Types typeUtils;
    private Elements elementUtils;
    private Gson gson;
    private String root;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        gson = new Gson();
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
        Map<String, String> module = processingEnvironment.getOptions();
        if (module.get("moduleName") == null) {
            throw new IllegalArgumentException(
                    "Please set module argument at gradle annotationProcessor option!");
        } else {
            moduleName = module.get("moduleName");
        }

        System.out.println("module=" + moduleName);

        path = System.getProperty("user.dir");

        file = new File(path + "/.plugin");
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(path + "/.plugin/module-" + moduleName + ".json");
        if (file.exists()) {
            try {
                FileInputStream inputStream = new FileInputStream(file);
                String jsonStr = getFileStr(inputStream);
                InterceptionModel interceptionModel = gson.fromJson(jsonStr,
                        InterceptionModel.class);
                this.root = interceptionModel.getRoot();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotationTypes = new HashSet<>();
        annotationTypes.add(Interceptor.class.getCanonicalName());
        annotationTypes.add(Surround.class.getCanonicalName());
        return annotationTypes;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (roundEnvironment.getRootElements().size() == 0) return false;
        else {
            InterceptionModel model = new InterceptionModel();
            model.setModule(moduleName);
            for (Element element : roundEnvironment.getElementsAnnotatedWith(Interceptor.class)) {
                if (root == null) {
                    root = elementUtils.getPackageOf(element).getQualifiedName().toString();
                }
                final InterceptionModel.InterceptorsBean bean = new InterceptionModel.InterceptorsBean();
                final TypeElement typeElement = (TypeElement) element;
                Interceptor interceptor = typeElement.getAnnotation(Interceptor.class);
                boolean isSingleton = interceptor.singleton();
                final boolean debug = interceptor.environment() == Environment.DEBUG;
                if (debug) {
                    bean.setDebug(typeElement.getQualifiedName().toString().replace(".", "/"));
                    bean.setDs(isSingleton);
                } else {
                    bean.setRelease(typeElement.getQualifiedName().toString().replace(".", "/"));
                    bean.setRs(isSingleton);
                }
                final TypeMirror[] annotationMirror = new TypeMirror[1];
                for (TypeMirror mirror : typeElement.getInterfaces()) {
                    if (mirror.toString().startsWith(
                            AnnotationInterceptor.class.getCanonicalName())) {
                        mirror.accept(new SimpleTypeVisitor6<Void, Void>() {
                            @Override
                            public Void visitDeclared(DeclaredType declaredType, Void aVoid) {
                                List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
                                if (!typeArguments.isEmpty()) {
                                    annotationMirror[0] = typeArguments.get(0);
                                    bean.setAnnotation(
                                            annotationMirror[0].toString().replace(".", "/"));
                                }
                                return null;
                            }
                        }, null);
                        if (bean.getAnnotation() == null) {
                            throw new IllegalArgumentException(
                                    "You must set a AnnotationInterceptor<@YourAnnotation> at use @Interceptor class;error with:" + ((TypeElement) element).getQualifiedName());
                        }
                        break;
                    }
                }

                TypeElement annotationTypeElement = (TypeElement) typeUtils.asElement(
                        annotationMirror[0]);
                for (Element part : annotationTypeElement.getEnclosedElements()) {
                    if (part.getKind() == ElementKind.METHOD) {
                        if (part instanceof ExecutableElement) {
                            ExecutableElement ex = (ExecutableElement) part;
                            Object value = ex.getDefaultValue();
                            InterceptionModel.InterceptorsBean.DefaultBean defaultBean = new InterceptionModel.InterceptorsBean.DefaultBean();
                            defaultBean.setKey(part.getSimpleName().toString());
                            String type = ex.getReturnType().toString();
                            if (type.startsWith("java.lang.Class") && type.endsWith("[]")) {
                                type = "java.lang.Class[]";
                            } else if (type.startsWith("java.lang.Class")) {
                                type = "java.lang.Class";
                            } else if (type.startsWith("char") || type.endsWith("char[]")) {
                                throw new IllegalArgumentException(
                                        "Interceptor Unsupported char type! ->" + annotationTypeElement.getQualifiedName().toString());
                            } else if (type.startsWith("byte") || type.endsWith("byte[]")) {
                                throw new IllegalArgumentException(
                                        "Interceptor Unsupported byte type! ->" + annotationTypeElement.getQualifiedName().toString());
                            } else if (type.startsWith("short") || type.endsWith("short[]")) {
                                throw new IllegalArgumentException(
                                        "Interceptor Unsupported short type! ->" + annotationTypeElement.getQualifiedName().toString());
                            } else if (type.equals("java.lang.String")) {
                                if (value != null) {
                                    value = value.toString().replaceAll("\"", "");
                                }
                            }
                            if (type.endsWith("[]")) {
                                if (value != null) {
                                    value = value.toString().substring(1,
                                            value.toString().length() - 1);
                                }
                            }
                            defaultBean.setValue(value == null ? null : value.toString());
                            defaultBean.setVt(type);
                            bean.getDefaultX().add(defaultBean);
                        }
                    }
                }

                model.getInterceptors().add(bean);
            }
            model.setRoot(root);
            //检查@Surround是否符合规范
            for (Element element : roundEnvironment.getElementsAnnotatedWith(Surround.class)) {
                if (element.getKind() == ElementKind.CLASS) {
                    TypeElement typeElement = (TypeElement) element;
                    //必须为静态内部类
                    for (Modifier modifier : typeElement.getModifiers()) {
                        if (modifier.equals(Modifier.ABSTRACT) || modifier.equals(
                                Modifier.PUBLIC)) {
                            throw new IllegalArgumentException(
                                    "@Surround class modifier must not container abstract/public");
                        }
                    }

                    DeclaredType tempMirror = null;
                    int size = typeElement.getEnclosedElements().size();
                    for (int i = 0; i < size; i++) {
                        Element e = typeElement.getEnclosedElements().get(i);
                        if (e.getKind() == ElementKind.METHOD) {
                            //不能包含方法
                            throw new IllegalArgumentException(
                                    "@Surround class must not container method!");
                        } else if (e.getKind() == ElementKind.FIELD) {
                            VariableElement variableElement = (VariableElement) e;
                            if (variableElement.getAnnotationMirrors() == null || variableElement.getAnnotationMirrors().size() == 0) {
                                throw new IllegalArgumentException(
                                        "@Surround field annotations can not be empty!");
                            }
                            if (variableElement.getAnnotationMirrors().size() > 1) {
                                throw new IllegalArgumentException(
                                        "@Surround field annotations can only add one!");
                            }
                            if (tempMirror == null) {
                                tempMirror = variableElement.getAnnotationMirrors().get(
                                        0).getAnnotationType();

                            } else {
                                if (i + 1 < size) {
                                    variableElement = (VariableElement) typeElement.getEnclosedElements().get(
                                            i + 1);
                                    if (!variableElement.getAnnotationMirrors().get(
                                            0).getAnnotationType().equals(tempMirror)) {
                                        throw new IllegalArgumentException(
                                                "Field annotations under @Surround must be consistent!");
                                    }
                                }
                            }
                        }
                    }
                }
            }
            FileWriter writer = null;
            try {
                writer = new FileWriter(file);
                writer.write(new Gson().toJson(model));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }

    private String getFileStr(InputStream inputStream) throws IOException {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            arrayOutputStream.write(buffer, 0, len);
        }
        return arrayOutputStream.toString();
    }
}
