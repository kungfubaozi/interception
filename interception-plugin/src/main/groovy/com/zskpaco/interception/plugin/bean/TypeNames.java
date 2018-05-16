package com.zskpaco.interception.plugin.bean;

/**
 * Author: Richard paco
 * Date: 2018/5/8
 */
public interface TypeNames {

    String INTERFACE_ELEMENT = "com/zskpaco/interception/Element";
    String L_INTERFACE_ELEMENT = "L" + INTERFACE_ELEMENT + ";";

    String ASSIGNMENT = "com/zskpaco/interception/TypeVisitor";
    String L_ASSIGNMENT = "L" + INTERFACE_ELEMENT + ";";

    String ANNOTATION_SURROUND = "com/zskpaco/interception/Surround";
    String L_ANNOTATION_SURROUND = "L" + ANNOTATION_SURROUND + ";";

    String ANNOTATION_ASYNC = "com/zskpaco/interception/Async";
    String L_ANNOTATION_ASYNC = "L" + ANNOTATION_ASYNC + ";";

    String ANNOTATION_UI = "com/zskpaco/interception/UiThread";
    String L_ANNOTATION_UI = "L" + ANNOTATION_UI + ";";

    String INTERFACE_ELEMENT_BUILDER = "com/zskpaco/interception/internal/IElementBuilder";
    String L_INTERFACE_ELEMENT_BUILDER = "L" + INTERFACE_ELEMENT_BUILDER + ";";

    String INTERFACE_ELEMENT_BUILDER_IMPL = "com/zskpaco/interception/internal/ElementBuilderImpl";
    String L_INTERFACE_ELEMENT_BUILDER_IMPL = "L" + INTERFACE_ELEMENT_BUILDER_IMPL + ";";

    String INTERFACE_ELEMENT_EXECUTION_LOADER = "com/zskpaco/interception/internal/IElementExecutionLoader";
    String L_INTERFACE_ELEMENT_EXECUTION_LOADER = "L" + INTERFACE_ELEMENT_EXECUTION_LOADER + ";";

    String INTERFACE_ELEMENT_INSTANCE_LOADER = "com/zskpaco/interception/internal/IElementInstanceLoader";
    String L_INTERFACE_ELEMENT_INSTANCE_LOADER = "L" + INTERFACE_ELEMENT_INSTANCE_LOADER + ";";

    String INTERFACE_ELEMENT_LOADER = "com/zskpaco/interception/internal/IElementLoader";
    String L_INTERFACE_ELEMENT_LOADER = "L" + INTERFACE_ELEMENT_LOADER + ";";

    String INTERFACE_ELEMENT_MULTIPART_BUILDER = "com/zskpaco/interception/internal/IElementMultipartBuilder";
    String L_INTERFACE_ELEMENT_MULTIPART_BUILDER = "L" + INTERFACE_ELEMENT_MULTIPART_BUILDER + ";";

    String INTERFACE_ELEMENT_MULTIPART_BUILDER_IMPL = "com/zskpaco/interception/internal/ElementMultipartBuilderImpl";
    String L_INTERFACE_ELEMENT_MULTIPART_BUILDER_IMPL = "L" + INTERFACE_ELEMENT_MULTIPART_BUILDER_IMPL + ";";

    String INTERFACE_ELEMENT_CONTROLLER = "com/zskpaco/interception/internal/IElementController";
    String L_INTERFACE_ELEMENT_CONTROLLER = "L" + INTERFACE_ELEMENT_CONTROLLER + ";";

    String INTERFACE_ANNOTATION_INTERCEPTOR = "com/zskpaco/interception/AnnotationInterceptor";
    String L_INTERFACE_ANNOTATION_INTERCEPTOR = "L" + INTERFACE_ANNOTATION_INTERCEPTOR + ";";

}
