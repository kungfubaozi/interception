package com.zskpaco.interception.plugin;

import com.zskpaco.interception.plugin.basic.ModuleRunnerWrite;
import com.zskpaco.interception.plugin.bean.ElementModel;
import com.zskpaco.interception.plugin.bean.InterceptionModel;
import com.zskpaco.interception.plugin.utils.VisitorUtils;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import static com.zskpaco.interception.plugin.bean.TypeNames.*;

import java.util.*;

public class ClassHandler extends ClassVisitor implements Opcodes {

    private static ElementModel EMPTY_ELEMENT_MODEL = new ElementModel();

    private String path;
    private ClassNode classNode;
    private InterceptionModel model;
    private boolean isSurround = false;
    private String name;
    private String runnerOwner;
    private HashSet<Element> methodSet = new HashSet<>();
    private HashSet<Surround> surroundFieldSet = new HashSet<>();
    private String elementOwner;
    private String loadName, loadRep;

    /**
     * 0:Type
     * 1:Variable
     * 2:Method
     */
    private Map<Integer, List<ElementModel>> modelMap = new LinkedHashMap<>();

    private MethodVisitor mv;

    public ClassHandler(ClassWriter cw, String runnerOwner, String path, ClassNode classNode,
                        InterceptionModel model) {
        super(ASM6, cw);
        this.path = path;
        this.runnerOwner = runnerOwner;
        this.classNode = classNode;
        this.model = model;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName,
                      String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        VisitorUtils.log(":interceptor:build %s", name.replace("/", "."));

        this.name = name;
        for (InterceptionModel.InterceptorsBean bean : model.getInterceptors()) {
            if (bean.getRelease() != null && bean.getRelease().equals(name)) {
                VisitorUtils.createInstance(cv, name, bean.isRs());
            } else if (bean.getDebug() != null && bean.getDebug().equals(name)) {
                VisitorUtils.createInstance(cv, name, bean.isDs());
            }
        }

        if (classNode.invisibleAnnotations != null) {
            ElementModel bean = getMatchModel(null, 0, classNode.invisibleAnnotations);
            if (!isSurround && bean != null && bean.getInterceptor().size() > 0) {
                List<ElementModel> models = new LinkedList<>();
                bean.setProcessId(0);
                bean.setName(name);
                bean.setReturnType(name);
                models.add(bean);
                modelMap.put(0, models);
            }
        }
        if (classNode.fields != null) {
            List<ElementModel> models = new LinkedList<>();
            for (Object node : classNode.fields) {
                FieldNode fieldNode = (FieldNode) node;
                int processId = (fieldNode.access + fieldNode.desc + fieldNode.name + fieldNode.signature).hashCode();
                if (fieldNode.invisibleAnnotations != null) {
                    ElementModel bean = getMatchModel(new Surround(fieldNode.name, fieldNode.desc),
                            1, fieldNode.invisibleAnnotations);
                    if (bean != null && bean != EMPTY_ELEMENT_MODEL) {
                        String accessName = "access$" + fieldNode.name + "$" + (processId < 0 ? Math.abs(
                                processId) : processId);

                        String[] getset = new String[2];

                        bean.setAccessName(accessName);
                        bean.setProcessId(processId);
                        bean.setName(fieldNode.name);
                        bean.setReturnType(fieldNode.desc);

                        //set
                        String set = "(L" + name + ";" + fieldNode.desc + ")Ljava/lang/Object;";
                        getset[0] = set;
                        mv = cv.visitMethod(ACC_PUBLIC + ACC_STATIC + ACC_SYNTHETIC, accessName,
                                set,
                                fieldNode.signature == null ? null : "(L" + name + ";" + fieldNode.signature + ")Ljava/lang/Object;",
                                null);
                        mv.visitVarInsn(ALOAD, 0);
                        VisitorUtils.visitVarLoadInsn(mv, fieldNode.desc, 1);
                        mv.visitFieldInsn(PUTFIELD, name, fieldNode.name, fieldNode.desc);
                        mv.visitInsn(ACONST_NULL);
                        mv.visitInsn(ARETURN);
                        mv.visitMaxs(0, 0);
                        mv.visitEnd();

                        //get
                        String get = "(L" + name + ";)" + fieldNode.desc;
                        getset[1] = get;
                        mv = cv.visitMethod(ACC_PUBLIC + ACC_STATIC + ACC_SYNTHETIC, accessName,
                                get,
                                fieldNode.signature == null ? null : "(L" + name + ";)" + fieldNode.signature,
                                null);
                        mv.visitVarInsn(ALOAD, 0);
                        mv.visitFieldInsn(GETFIELD, name, fieldNode.name, fieldNode.desc);
                        VisitorUtils.visitReturn(mv, fieldNode.desc);
                        mv.visitMaxs(0, 0);
                        mv.visitEnd();

                        bean.setGetAndSet(getset);

                        models.add(bean);

                    }
                }
            }
            if (models.size() > 0) {
                modelMap.put(1, models);
            }
        }
        if (classNode.methods != null && !isSurround) {
            List<ElementModel> models = new LinkedList<>();
            for (Object node : classNode.methods) {
                MethodNode methodNode = (MethodNode) node;
                int processId = (methodNode.access + methodNode.desc + methodNode.name + methodNode.signature).hashCode();
                if (methodNode.invisibleAnnotations != null && !isSurround) {
                    ElementModel bean = getMatchModel(null, 2, methodNode.invisibleAnnotations);
                    ElementModel variable = null;
                    if (methodNode.invisibleLocalVariableAnnotations != null) {
                        variable = getMatchModel(null, 3,
                                methodNode.invisibleLocalVariableAnnotations);
                    }
                    if (bean != null && bean.getInterceptor().size() > 0) {

                        if ((bean.isUi() || bean.isAsync()) && !methodNode.desc.endsWith("V")) {
                            throw new IllegalArgumentException(
                                    "The method of using @Async @UiThread can not contain the return value.");
                        }

                        if (variable != null && variable.getInterceptor() != null) {
                            bean.setSubInterceptor(variable.getInterceptor());
                        }

                        String accessName = ("access$" + methodNode.name + "$" + processId).replace(
                                "-", "x");

                        bean.setAccessName(accessName);
                        bean.setProcessId(processId);
                        bean.setName(methodNode.name);
                        bean.setReturnType(Type.getReturnType(methodNode.desc).getDescriptor());
                        bean.setDesc(Type.getArgumentTypes(methodNode.desc));
                        methodSet.add(new Element(methodNode.name + methodNode.desc, processId));

                        String desc = "(L" + name + ";" + methodNode.desc.substring(1);
                        boolean returnNull = false;
                        if (desc.endsWith("V")) {
                            returnNull = true;
                            desc = desc.substring(0, desc.length() - 1) + "Ljava/lang/Object;";
                        }
                        mv = cv.visitMethod(ACC_PUBLIC + ACC_STATIC + ACC_SYNTHETIC, accessName,
                                desc,
                                methodNode.signature == null ? null : "(L" + name + ";" + (returnNull ? methodNode.signature.substring(
                                        1,
                                        methodNode.signature.length() - 1) + "Ljava/lang/Object;" : methodNode.signature.substring(
                                        1)), null);
                        VisitorUtils.visitVarLoadInsn(mv, desc);
                        mv.visitMethodInsn(INVOKESPECIAL, name,
                                (methodNode.name + "$" + processId).replace("-", "x"),
                                methodNode.desc, false);
                        if (returnNull) {
                            mv.visitInsn(ACONST_NULL);
                            mv.visitInsn(ARETURN);
                        } else {
                            VisitorUtils.visitReturn(mv, bean.getReturnType());
                        }

                        mv.visitMaxs(0, 0);
                        mv.visitEnd();

                        models.add(bean);
                    }
                }
            }
            if (models.size() > 0) {
                modelMap.put(2, models);
            }
        }

        if (modelMap.size() > 0) {
            loadName = name.substring(name.lastIndexOf("/") + 1, name.length());
            loadRep = model.getModule() + "$" + loadName + (isSurround ? "$SurroundLoader" : "$ElementLoader");
            elementOwner = name.replace(loadName, loadRep);
        }

        if (isSurround) {
            mv = cv.visitMethod(ACC_PUBLIC + ACC_STATIC + ACC_SYNTHETIC, "_init",
                    "(Ljava/lang/Object;)L" + name + ";", null, null);
            mv.visitCode();
//            Label l0 = new Label();
//            mv.visitLabel(l0);
//            mv.visitTypeInsn(NEW, elementOwner);
//            mv.visitInsn(DUP);
//            mv.visitMethodInsn(INVOKESPECIAL, elementOwner, "<init>", "()V", false);
//            mv.visitVarInsn(ASTORE, 1);
//            Label l1 = new Label();
//            mv.visitLabel(l1);
//            mv.visitTypeInsn(NEW, name);
//            mv.visitInsn(DUP);
//            mv.visitMethodInsn(INVOKESPECIAL, name, "<init>", "()V", false);
//            mv.visitVarInsn(ASTORE, 2);
//            Label l2 = new Label();
//            mv.visitLabel(l2);
//            mv.visitVarInsn(ALOAD, 1);
//            mv.visitVarInsn(ALOAD, 2);
//            mv.visitVarInsn(ALOAD, 0);
//            mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ELEMENT_LOADER, "init",
//                    "(Ljava/lang/Object;Ljava/lang/Object;)V", true);
//            Label l3 = new Label();
//            mv.visitLabel(l3);
//            mv.visitVarInsn(ALOAD, 2);
//            mv.visitInsn(ARETURN);
//            Label l4 = new Label();
//            mv.visitLabel(l4);
//            mv.visitMaxs(0, 0);
//            mv.visitEnd();

            mv.visitFieldInsn(GETSTATIC, name, "$_Element_Loader", L_INTERFACE_ELEMENT_LOADER);
            Label l1 = new Label();
            mv.visitJumpInsn(IFNONNULL, l1);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitTypeInsn(NEW, elementOwner);
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, elementOwner, "<init>", "()V", false);
            mv.visitFieldInsn(PUTSTATIC, name, "$_Element_Loader", L_INTERFACE_ELEMENT_LOADER);
            mv.visitLabel(l1);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);

            mv.visitTypeInsn(NEW, name);
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, name, "<init>", "()V", false);
            mv.visitVarInsn(ASTORE, 1);

            mv.visitFieldInsn(GETSTATIC, name, "$_Element_Loader", L_INTERFACE_ELEMENT_LOADER);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ELEMENT_LOADER, "init",
                    "(Ljava/lang/Object;Ljava/lang/Object;)V", true);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
                                     String[] exceptions) {
        String newName = name;
        for (Element element : methodSet) {
            if (element.key.equals(name + descriptor)) {
                newName = (name + "$" + element.processId).replace("-", "x");
                mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
                mv.visitCode();
                //mv.visitVarInsn(ALOAD, 0);
                mv.visitFieldInsn(GETSTATIC, ClassHandler.this.name, "$_Element_Loader",
                        L_INTERFACE_ELEMENT_LOADER);
                mv.visitLdcInsn(new Integer(element.processId));

                Type[] types = Type.getArgumentTypes(descriptor);
                VisitorUtils.visitIntPushStack(mv, types.length);
                mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
                VisitorUtils.visitArgumentsLoadInsn2(mv, types);
                mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ELEMENT_LOADER, "startup",
                        "(I[Ljava/lang/Object;)Ljava/lang/Object;", true);
                String d = Type.getReturnType(descriptor).getDescriptor();
                VisitorUtils.visitCheckCast(mv, d);
                VisitorUtils.visitReturn(mv, d);

                mv.visitMaxs(0, 0);
                mv.visitEnd();
                break;
            }
        }
        MethodVisitor mv = cv.visitMethod(access, newName, descriptor, signature, exceptions);
        mv = new MethodTransformer(ClassHandler.this.name, mv, access, name, descriptor,
                surroundFieldSet, modelMap.size() > 0, elementOwner);
        return mv;
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        if (modelMap.size() > 0) {

            if (runnerOwner == null) {
                VisitorUtils.log("module runner create class");
                runnerOwner = ModuleRunnerWrite.write(
                        path.replace(classNode.name, "").replace(".class", ""), model);
            }

            ElementVisitor.visit(runnerOwner, isSurround, path, name, modelMap, elementOwner,
                    loadName, loadRep);

            FieldVisitor fv = cv.visitField(ACC_PRIVATE + ACC_STATIC + ACC_FINAL + ACC_SYNTHETIC,
                    "$_Element_Loader", L_INTERFACE_ELEMENT_LOADER, null, null);
            fv.visitEnd();


        }
    }

    /**
     * 获取匹配
     *
     * @param invisibleAnnotations
     * @return
     */
    ElementModel getMatchModel(Surround name, int type, List invisibleAnnotations) {
        ElementModel model = new ElementModel();
        model.setType(type);
        for (Object node : invisibleAnnotations) {
            AnnotationNode annotationNode = (AnnotationNode) node;
            if (annotationNode.desc.equals(
                    L_ANNOTATION_SURROUND) && !isSurround && type != 2 && type != 3) {
                if (type == 1) {
                    surroundFieldSet.add(name);
                    return EMPTY_ELEMENT_MODEL;
                } else {
                    isSurround = true;
                    return null;
                }
            }
            if (annotationNode.desc.equals(L_ANNOTATION_ASYNC)) {
                model.setAsync(true);
            }
            if (annotationNode.desc.equals(L_ANNOTATION_UI)) {
                model.setUi(true);
            }
            if (model.isAsync() && model.isUi()) {
                throw new IllegalArgumentException(
                        "@Async @UiThread cannot be added at the same time.");
            }
            for (InterceptionModel.InterceptorsBean bean : this.model.getInterceptors()) {
                if (annotationNode.desc.equals("L" + bean.getAnnotation() + ";")) {

                    InterceptionModel.InterceptorsBean nodeBean = new InterceptionModel.InterceptorsBean();
                    nodeBean.setAnnotation(bean.getAnnotation());
                    nodeBean.setDebug(bean.getDebug());
                    nodeBean.setDefaultX(bean.getDefaultX());
                    nodeBean.setDs(bean.isDs());
                    nodeBean.setProcessor(bean.getProcessor());
                    nodeBean.setRelease(bean.getRelease());
                    nodeBean.setRs(bean.isRs());
                    nodeBean.setValues(annotationNode.values);

                    model.getInterceptor().add(nodeBean);
                    break;
                }
            }
        }
        return model;
    }


    class Element {
        String key;
        int processId;

        public Element(String key, int processId) {
            this.key = key;
            this.processId = processId;
        }
    }

    class Surround {
        String name;
        String desc;

        public Surround(String name, String desc) {
            this.name = name;
            this.desc = desc;
        }
    }

}

