package com.zskpaco.interception.plugin;

import com.zskpaco.interception.plugin.basic.ModuleRunnerWrite;
import com.zskpaco.interception.plugin.bean.ElementModel;
import com.zskpaco.interception.plugin.bean.InterceptionModel;
import com.zskpaco.interception.plugin.utils.VisitorUtils;

import org.objectweb.asm.*;

import static com.zskpaco.interception.plugin.bean.TypeNames.*;
import static org.objectweb.asm.Opcodes.*;

import java.util.*;

public class ElementVisitor {

    /**
     * 写入ElementLoader
     *
     * @param runnerOwner
     * @param isSurround
     * @param path
     * @param owner
     * @param modelMap
     */
    public static void visit(String runnerOwner, boolean isSurround, String path, String owner,
                             Map<Integer, List<ElementModel>> modelMap, String elementOwner,
                             String loadName, String name) {
        ClassWriter cw = new ClassWriter(1);
        FieldVisitor fv;
        MethodVisitor mv;
        List<ElementModel> types = modelMap.get(0);
        List<ElementModel> fields = modelMap.get(1);
        List<ElementModel> methods = modelMap.get(2);
        List<ElementModel> models = new LinkedList<>();
        List<ElementModel> includeTypeModels = new LinkedList<>();
        if (fields != null) {
            models.addAll(fields);
            includeTypeModels.addAll(fields);
        }
        if (methods != null) {
            models.addAll(methods);
            includeTypeModels.addAll(methods);
        }
        if (types != null) {
            includeTypeModels.addAll(types);
        }

        cw.visit(52, ACC_PUBLIC + ACC_SUPER, elementOwner,
                "Ljava/lang/Object;L" + INTERFACE_ELEMENT_LOADER + "<L" + owner + ";>;" + L_INTERFACE_ELEMENT_EXECUTION_LOADER,
                "java/lang/Object",
                new String[]{INTERFACE_ELEMENT_LOADER, INTERFACE_ELEMENT_EXECUTION_LOADER});

        {
            fv = cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, "HOST_CLASS",
                    "Ljava/lang/String;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PRIVATE, "_local", "L" + owner + ";", null, null);
            fv.visitEnd();
            if (isSurround) {
                fv = cw.visitField(ACC_PRIVATE, "_host", "Ljava/lang/Object;", null, null);
                fv.visitEnd();
            }
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv.visitInsn(RETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "init", "(L" + owner + ";Ljava/lang/Object;)V", null,
                    null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitFieldInsn(PUTFIELD, elementOwner, "_local", "L" + owner + ";");
            if (isSurround) {
                mv.visitVarInsn(ALOAD, 0);
                mv.visitVarInsn(ALOAD, 2);
                mv.visitFieldInsn(PUTFIELD, elementOwner, "_host", "Ljava/lang/Object;");
                mv.visitFieldInsn(GETSTATIC, elementOwner, "HOST_CLASS", "Ljava/lang/String;");
                mv.visitInsn(ICONST_0);
                mv.visitMethodInsn(INVOKESTATIC, runnerOwner, "getBuilderById",
                        "(Ljava/lang/String;I)" + L_INTERFACE_ELEMENT_MULTIPART_BUILDER, false);
                mv.visitVarInsn(ASTORE, 3);

                //if null
                mv.visitVarInsn(ALOAD, 3);
                Label l4 = new Label();
                mv.visitJumpInsn(IFNULL, l4);
                Label l5 = new Label();
                mv.visitLabel(l5);
                mv.visitVarInsn(ALOAD, 3);
                mv.visitVarInsn(ALOAD, 2);
                mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ELEMENT_MULTIPART_BUILDER, "setHost",
                        "(Ljava/lang/Object;)" + L_INTERFACE_ELEMENT_MULTIPART_BUILDER, true);
                mv.visitInsn(POP);

                mv.visitVarInsn(ALOAD, 3);
                mv.visitVarInsn(ALOAD, 0);
                mv.visitMethodInsn(INVOKESTATIC, runnerOwner, "_surround",
                        "(" + L_INTERFACE_ELEMENT_MULTIPART_BUILDER + L_INTERFACE_ELEMENT_EXECUTION_LOADER + ")V",
                        false);

                mv.visitLabel(l4);
                mv.visitFrame(Opcodes.F_APPEND, 1,
                        new Object[]{INTERFACE_ELEMENT_MULTIPART_BUILDER}, 0, null);
                mv.visitInsn(RETURN);

            } else {

                if (types != null) for (ElementModel model : types) {
                    buildInit(mv, elementOwner, runnerOwner, model.getProcessId());
                }

                if (fields != null) for (ElementModel model : fields) {
                    buildInit(mv, elementOwner, runnerOwner, model.getProcessId());
                }


                mv.visitInsn(RETURN);

            }
            mv.visitInsn(RETURN);
            Label l4 = new Label();
            mv.visitLabel(l4);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "startup", "(I[Ljava/lang/Object;)Ljava/lang/Object;",
                    null, null);
            mv.visitCode();
            mv.visitFieldInsn(GETSTATIC, elementOwner, "HOST_CLASS", "Ljava/lang/String;");
            mv.visitVarInsn(ILOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, runnerOwner, "getBuilderById",
                    "(Ljava/lang/String;I)" + L_INTERFACE_ELEMENT_MULTIPART_BUILDER, false);
            mv.visitTypeInsn(CHECKCAST, INTERFACE_ELEMENT_BUILDER);
            mv.visitVarInsn(ASTORE, 3);
            mv.visitVarInsn(ALOAD, 3);
            Label l2 = new Label();
            mv.visitJumpInsn(IFNULL, l2);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ELEMENT_BUILDER, "setArgs",
                    "([Ljava/lang/Object;)" + L_INTERFACE_ELEMENT_BUILDER, true);
            mv.visitVarInsn(ALOAD, 0);
            if (isSurround) {
                mv.visitFieldInsn(GETFIELD, elementOwner, "_host", "Ljava/lang/Object;");
            } else {
                mv.visitFieldInsn(GETFIELD, elementOwner, "_local", "L" + owner + ";");
            }
            mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ELEMENT_BUILDER, "setHost",
                    "(Ljava/lang/Object;)" + L_INTERFACE_ELEMENT_MULTIPART_BUILDER, true);
            mv.visitInsn(POP);
            mv.visitLabel(l2);
            mv.visitFrame(Opcodes.F_APPEND, 1, new Object[]{INTERFACE_ELEMENT_BUILDER}, 0, null);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESTATIC, runnerOwner, "_process",
                    "(" + L_INTERFACE_ELEMENT_BUILDER + L_INTERFACE_ELEMENT_EXECUTION_LOADER + ")Ljava/lang/Object;",
                    false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "init",
                    "(Ljava/lang/Object;Ljava/lang/Object;)V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitTypeInsn(CHECKCAST, owner);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitMethodInsn(INVOKEVIRTUAL, elementOwner, "init",
                    "(L" + owner + ";Ljava/lang/Object;)V", false);
            mv.visitInsn(RETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitMaxs(3, 3);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLdcInsn(Type.getType("L" + owner + ";"));
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getCanonicalName",
                    "()Ljava/lang/String;", false);
            mv.visitFieldInsn(PUTSTATIC, elementOwner, "HOST_CLASS", "Ljava/lang/String;");
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitMethodInsn(INVOKESTATIC, elementOwner, "_check_this_", "()V", false);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 0);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PRIVATE + ACC_STATIC + ACC_SYNTHETIC, "_check_this_", "()V",
                    null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(26, l0);
            mv.visitFieldInsn(GETSTATIC, elementOwner, "HOST_CLASS", "Ljava/lang/String;");
            mv.visitMethodInsn(INVOKESTATIC, runnerOwner, "check", "(Ljava/lang/String;)Z", false);
            Label l1 = new Label();
            mv.visitJumpInsn(IFNE, l1);
            mv.visitTypeInsn(NEW, "java/util/concurrent/ConcurrentHashMap");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/util/concurrent/ConcurrentHashMap", "<init>",
                    "()V", false);
            mv.visitVarInsn(ASTORE, 0);
            if (isSurround) {
                buildSurround(mv, fields);
            } else {
                buildNormal(mv, includeTypeModels);
            }
            mv.visitFieldInsn(GETSTATIC, elementOwner, "HOST_CLASS", "Ljava/lang/String;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESTATIC, runnerOwner, "add",
                    "(Ljava/lang/String;Ljava/util/Map;)V", false);
            mv.visitLabel(l1);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            mv.visitInsn(RETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "build", "(IZ[Ljava/lang/Object;)Ljava/lang/Object;",
                    null, null);
            mv.visitCode();
            buildMethod(runnerOwner, elementOwner, owner, mv, models);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        ModuleRunnerWrite.writeBytes(cw.toByteArray(), path.replace(loadName, name));
    }

    private static void buildInit(MethodVisitor mv, String elementOwner, String runnerOwner,
                                  int processId) {
        mv.visitFieldInsn(GETSTATIC, elementOwner, "HOST_CLASS", "Ljava/lang/String;");
        VisitorUtils.visitIntPushStack(mv, processId);
        mv.visitMethodInsn(INVOKESTATIC, runnerOwner, "getBuilderById",
                "(Ljava/lang/String;I)" + L_INTERFACE_ELEMENT_MULTIPART_BUILDER, false);
        mv.visitTypeInsn(CHECKCAST, INTERFACE_ELEMENT_BUILDER);
        mv.visitVarInsn(ASTORE, 3);

        mv.visitVarInsn(ALOAD, 3);
        Label l4 = new Label();
        mv.visitJumpInsn(IFNULL, l4);
        Label l5 = new Label();
        mv.visitLabel(l5);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ELEMENT_BUILDER, "setHost",
                "(Ljava/lang/Object;)" + L_INTERFACE_ELEMENT_MULTIPART_BUILDER, true);
        mv.visitInsn(POP);

        mv.visitVarInsn(ALOAD, 3);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESTATIC, runnerOwner, "_process",
                "(" + L_INTERFACE_ELEMENT_BUILDER + L_INTERFACE_ELEMENT_EXECUTION_LOADER + ")Ljava/lang/Object;",
                false);
        mv.visitInsn(POP);
        mv.visitLabel(l4);
        mv.visitFrame(Opcodes.F_APPEND, 1, new Object[]{INTERFACE_ELEMENT_BUILDER}, 0, null);
    }

    private static void buildNormal(MethodVisitor mv, List<ElementModel> models) {
        for (int i = 0; i < models.size(); i++) {
            ElementModel model = models.get(i);
            mv.visitTypeInsn(NEW, INTERFACE_ELEMENT_BUILDER_IMPL);
            mv.visitInsn(DUP);
            VisitorUtils.visitIntPushStack(mv, model.getType());
            mv.visitLdcInsn(model.getName());
            mv.visitLdcInsn(new Integer(model.getProcessId()));
            VisitorUtils.visitLdcInsn(mv, model.getType() == 0 ? "V" : model.getReturnType());
            if (model.getType() == 2) {
                VisitorUtils.visitIntPushStack(mv, model.getDesc().length);
                mv.visitTypeInsn(ANEWARRAY, "java/lang/Class");
                for (int j = 0; j < model.getDesc().length; j++) {
                    Type type = model.getDesc()[j];
                    mv.visitInsn(DUP);
                    VisitorUtils.visitIntPushStack(mv, j);
                    VisitorUtils.visitLdcInsn(mv, type.getDescriptor());
                    mv.visitInsn(AASTORE);
                }
            } else {
                mv.visitInsn(ICONST_0);
                mv.visitTypeInsn(ANEWARRAY, "java/lang/Class");
            }
            mv.visitMethodInsn(INVOKESPECIAL, INTERFACE_ELEMENT_BUILDER_IMPL, "<init>",
                    "(ILjava/lang/String;ILjava/lang/Class;[Ljava/lang/Class;)V", false);
            mv.visitVarInsn(ASTORE, 1);

            //设置线程类型
            mv.visitVarInsn(ALOAD, 1);
            mv.visitInsn(model.isAsync() ? ICONST_1 : ICONST_0);
            mv.visitInsn(model.isUi() ? ICONST_1 : ICONST_0);
            mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ELEMENT_BUILDER, "setType",
                    "(ZZ)" + L_INTERFACE_ELEMENT_MULTIPART_BUILDER, true);
            mv.visitInsn(POP);

            mv.visitVarInsn(ALOAD, 1);
            addModelAnnotation(mv, model);
            mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ELEMENT_BUILDER, "setAnnotation",
                    "([Ljava/lang/Class;)" + L_INTERFACE_ELEMENT_MULTIPART_BUILDER, true);
            mv.visitInsn(POP);

            //addAnnotationValue

            setInterceptorAnnotationValues(model.getInterceptor(), mv,
                    num -> mv.visitVarInsn(ALOAD, 1), num -> {
                        mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ELEMENT_BUILDER,
                                "addAnnotationValues",
                                "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/Object;)" + L_INTERFACE_ELEMENT_BUILDER,
                                true);
                        mv.visitInsn(POP);
                    });

            //addTypesAnnotationValue

            setInterceptorAnnotationValues(model.getSubInterceptor(), mv, num -> {
                mv.visitVarInsn(ALOAD, 1);
                VisitorUtils.visitIntPushStack(mv, num);
            }, num -> {
                mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ELEMENT_BUILDER,
                        "addTypesAnnotationValues",
                        "(ILjava/lang/Class;Ljava/lang/String;Ljava/lang/Object;)" + L_INTERFACE_ELEMENT_MULTIPART_BUILDER,
                        true);
                mv.visitInsn(POP);
            });


            mv.visitVarInsn(ALOAD, 0);
            mv.visitLdcInsn(new Integer(model.getProcessId()));
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf",
                    "(I)Ljava/lang/Integer;", false);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "put",
                    "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true);
            mv.visitInsn(POP);
        }
    }

    private static void buildSurround(MethodVisitor mv, List<ElementModel> models) {
        mv.visitTypeInsn(NEW, INTERFACE_ELEMENT_MULTIPART_BUILDER_IMPL);
        mv.visitInsn(DUP);
        {
            //初始化
            VisitorUtils.visitIntPushStack(mv, models.size());
            mv.visitIntInsn(NEWARRAY, T_INT);
            for (int i = 0; i < models.size(); i++) {
                ElementModel model = models.get(i);
                mv.visitInsn(DUP);
                VisitorUtils.visitIntPushStack(mv, i);
                mv.visitLdcInsn(new Integer(model.getProcessId()));
                mv.visitInsn(IASTORE);
            }
            VisitorUtils.visitIntPushStack(mv, models.size());
            mv.visitTypeInsn(ANEWARRAY, "java/lang/Class");
            for (int i = 0; i < models.size(); i++) {
                ElementModel model = models.get(i);
                mv.visitInsn(DUP);
                VisitorUtils.visitIntPushStack(mv, i);
                VisitorUtils.visitLdcInsn(mv, model.getReturnType());
                mv.visitInsn(AASTORE);
            }
        }
        {
            //setTypeNames
            mv.visitMethodInsn(INVOKESPECIAL, INTERFACE_ELEMENT_MULTIPART_BUILDER_IMPL, "<init>",
                    "([I[Ljava/lang/Class;)V", false);
            mv.visitVarInsn(ASTORE, 1);
            mv.visitVarInsn(ALOAD, 1);
            VisitorUtils.visitIntPushStack(mv, models.size());
            mv.visitTypeInsn(ANEWARRAY, "java/lang/String");
            for (int i = 0; i < models.size(); i++) {
                ElementModel model = models.get(i);
                mv.visitInsn(DUP);
                VisitorUtils.visitIntPushStack(mv, i);
                mv.visitLdcInsn(model.getName());
                mv.visitInsn(AASTORE);
            }
            mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ELEMENT_MULTIPART_BUILDER, "setTypeNames",
                    "([Ljava/lang/String;)" + L_INTERFACE_ELEMENT_MULTIPART_BUILDER, true);
            mv.visitInsn(POP);
        }
        {
            //addAnnotation
            for (int i = 0; i < models.size(); i++) {
                ElementModel model = models.get(i);
                mv.visitVarInsn(ALOAD, 1);
                VisitorUtils.visitIntPushStack(mv, i);
                addModelAnnotation(mv, model);
                mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ELEMENT_MULTIPART_BUILDER,
                        "addAnnotationToPosition",
                        "(I[Ljava/lang/Class;)" + L_INTERFACE_ELEMENT_MULTIPART_BUILDER, true);
                mv.visitInsn(POP);
            }
        }
        {
            //addTypesAnnotationValues
            for (int i = 0; i < models.size(); i++) {
                ElementModel model = models.get(i);
                int finalI = i;
                setInterceptorAnnotationValues(model.getInterceptor(), mv, num -> {
                    mv.visitVarInsn(ALOAD, 1);
                    VisitorUtils.visitIntPushStack(mv, finalI);
                }, num -> {
                    mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ELEMENT_MULTIPART_BUILDER,
                            "addTypesAnnotationValues",
                            "(ILjava/lang/Class;Ljava/lang/String;Ljava/lang/Object;)" + L_INTERFACE_ELEMENT_MULTIPART_BUILDER,
                            true);
                    mv.visitInsn(POP);
                });
            }
        }
        mv.visitVarInsn(ALOAD, 0);
        mv.visitInsn(ICONST_0);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;",
                false);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "put",
                "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true);
        mv.visitInsn(POP);
    }

    private static void addModelAnnotation(MethodVisitor mv, ElementModel model) {
        VisitorUtils.visitIntPushStack(mv, model.getInterceptor().size());
        mv.visitTypeInsn(ANEWARRAY, "java/lang/Class");
        for (int j = 0; j < model.getInterceptor().size(); j++) {
            InterceptionModel.InterceptorsBean bean = model.getInterceptor().get(j);
            mv.visitInsn(DUP);
            VisitorUtils.visitIntPushStack(mv, j);
            mv.visitLdcInsn(Type.getType("L" + bean.getAnnotation() + ";"));
            mv.visitInsn(AASTORE);
        }
    }

    private static void loadAnnotationValue(MethodVisitor mv, String key, Object value, String type,
                                            String annotation, boolean fromDefault) {
        if (value == null) {
            return;
        }
        mv.visitLdcInsn(Type.getType("L" + annotation + ";"));
        mv.visitLdcInsn(key);
        if (type.equals("boolean")) {
            VisitorUtils.log("boolean");
            boolean v;
            if (fromDefault) {
                v = value.toString().equals("true") ? true : false;
            } else {
                v = (boolean) value;
            }
            if (v) mv.visitInsn(ICONST_0);
            else mv.visitInsn(ICONST_1);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf",
                    "(Z)Ljava/lang/Boolean;", false);
        } else if (type.equals("int")) {
            VisitorUtils.log("int");

            int v = (int) value;
            VisitorUtils.visitIntPushStack(mv, v);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf",
                    "(I)Ljava/lang/Integer;", false);
        } else if (type.equals("float")) {
            VisitorUtils.log("float");

            String v = String.valueOf(value);
            if (v.endsWith("f")) {
                v = v.replace("f", "");
            }
            mv.visitLdcInsn(new Float(v));
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(I)Ljava/lang/Float;",
                    false);
        } else if (type.equals("long")) {
            VisitorUtils.log("long");

            String v = String.valueOf(value);
            if (v.endsWith("l")) {
                v = v.replace("l", "");
            }
            mv.visitLdcInsn(new Long(v));
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(I)Ljava/lang/Long;",
                    false);
        } else if (type.equals("double")) {
            VisitorUtils.log("double");

            String v = String.valueOf(value);
            mv.visitLdcInsn(new Double(v));
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(I)Ljava/lang/Double;",
                    false);
        } else if (type.equals("java.lang.Class")) {
            VisitorUtils.log("java.lang.Class");

            if (!fromDefault) {
                Type v = (Type) value;
                mv.visitLdcInsn(Type.getType(v.getDescriptor()));
                return;
            }
            String v = String.valueOf(value);
            if (v.startsWith("L") && v.endsWith(";")) {
                v = v.substring(1, v.length() - 1);
            }
            mv.visitLdcInsn(Type.getType("L" + v + ";"));
        } else if (type.equals("java.lang.String")) {
            VisitorUtils.log("java.lang.String");

            mv.visitLdcInsn(String.valueOf(value));
        } else if (type.endsWith("[]")) {
            type = type.replace("[]", "");
            if (type.equals("int")) {
                VisitorUtils.log("int[]");

                if (fromDefault) {
                    String[] v = value.toString().split(",");
                    VisitorUtils.visitIntPushStack(mv, v.length);
                    mv.visitIntInsn(NEWARRAY, T_INT);
                    for (int i = 0; i < v.length; i++) {
                        mv.visitInsn(DUP);
                        VisitorUtils.visitIntPushStack(mv, i);
                        VisitorUtils.visitIntPushStack(mv, Integer.parseInt(v[i]));
                        mv.visitInsn(IASTORE);
                    }
                } else {
                    int[] v = (int[]) value;
                    VisitorUtils.visitIntPushStack(mv, v.length);
                    mv.visitIntInsn(NEWARRAY, T_INT);
                    for (int i = 0; i < v.length; i++) {
                        mv.visitInsn(DUP);
                        VisitorUtils.visitIntPushStack(mv, i);
                        VisitorUtils.visitIntPushStack(mv, v[i]);
                        mv.visitInsn(IASTORE);
                    }
                }
            } else if (type.equals("java.lang.String")) {
                VisitorUtils.log("java.lang.String[]");

                String[] v;
                if (fromDefault) {
                    v = value.toString().split(",");
                } else {
                    v = (String[]) value;
                }
                VisitorUtils.visitIntPushStack(mv, v.length);
                mv.visitTypeInsn(ANEWARRAY, "java/lang/String");
                for (int i = 0; i < v.length; i++) {
                    mv.visitInsn(DUP);
                    VisitorUtils.visitIntPushStack(mv, i);
                    mv.visitLdcInsn(v[i]);
                    mv.visitInsn(AASTORE);
                }
            } else if (type.equals("java.lang.Class")) {
                VisitorUtils.log("java.lang.Class[]");

                if (fromDefault) {
                    String[] v = value.toString().split(",");
                    VisitorUtils.visitIntPushStack(mv, v.length);
                    mv.visitTypeInsn(ANEWARRAY, "java/lang/Class");
                    for (int i = 0; i < v.length; i++) {
                        String current = v[i].trim().replaceAll(".", "/").replace(".class", "");
                        mv.visitInsn(DUP);
                        VisitorUtils.visitIntPushStack(mv, i);
                        mv.visitLdcInsn(Type.getType("L" + current + ";"));
                        mv.visitInsn(AASTORE);
                    }
                } else {
                    ArrayList list = (ArrayList) value;
                    Type[] v = new Type[list.size()];
                    for (int i = 0; i < v.length; i++) {
                        v[i] = (Type) list.get(i);
                    }
                    VisitorUtils.visitIntPushStack(mv, v.length);
                    mv.visitTypeInsn(ANEWARRAY, "java/lang/Class");
                    for (int i = 0; i < v.length; i++) {
                        mv.visitInsn(DUP);
                        VisitorUtils.visitIntPushStack(mv, i);
                        mv.visitLdcInsn(Type.getType(v[i].getDescriptor()));
                        mv.visitInsn(AASTORE);
                    }
                }
            } else {

                //enum[]
                if (fromDefault) {

                    VisitorUtils.log("enum default[]");

                    String[] v = value.toString().contains(",") ? value.toString().split(
                            ",") : new String[]{value.toString()};
                    VisitorUtils.visitIntPushStack(mv, v.length);
                    String t = type.replace(".", "/");

                    mv.visitTypeInsn(ANEWARRAY, t);
                    for (int i = 0; i < v.length; i++) {
                        String e = v[i].replace(".", "/").trim();
                        String name = e.substring(e.lastIndexOf("/") + 1, e.length());
                        mv.visitInsn(DUP);
                        VisitorUtils.visitIntPushStack(mv, i);
                        mv.visitFieldInsn(GETSTATIC, t, name, "L" + t + ";");
                        mv.visitInsn(AASTORE);
                    }
                } else {
                    VisitorUtils.log("enum compiler[]");
                    if (value instanceof List) {
                        List<Object> list = (List<Object>) value;
                        VisitorUtils.visitIntPushStack(mv, list.size());
                        String t = type.replace(".", "/");
                        mv.visitTypeInsn(ANEWARRAY, t);
                        for (int j = 0; j < list.size(); j++) {
                            Object v = list.get(j);
                            if (v instanceof String[]) {
                                String[] array = (String[]) v;
                                if (array.length == 2) {
                                    mv.visitInsn(DUP);
                                    VisitorUtils.visitIntPushStack(mv, j);
                                    VisitorUtils.visitEnum(mv, array);
                                    mv.visitInsn(AASTORE);
                                } else {
                                    throw new IllegalArgumentException("Incorrect enumeration!");
                                }
                            }
                        }
                    }
                }
            }
        } else {
            VisitorUtils.log("enum");

            //enum
            if (fromDefault) {
                String v = (String) value;
                int i = v.lastIndexOf(".");
                String from = type.replace(".", "/");
                String ve = v.substring(i + 1, v.length());
                mv.visitFieldInsn(GETSTATIC, from, ve, "L" + from + ";");
            } else {
                String[] v = (String[]) value;
                VisitorUtils.visitEnum(mv, v);
            }
        }
    }

    private static void buildMethod(String runnerOwner, String elementOwner, String owner,
                                    MethodVisitor mv, List<ElementModel> models) {
        //switch case 需要排序
        Collections.sort(models,
                (o1, o2) -> Integer.valueOf(o1.getProcessId()).compareTo(o2.getProcessId()));
        mv.visitVarInsn(ILOAD, 1);
        int[] processIds = new int[models.size()];
        Label[] labels = new Label[models.size()];
        Label dflt = new Label();
        for (int i = 0; i < models.size(); i++) {
            ElementModel model = models.get(i);
            processIds[i] = model.getProcessId();
            labels[i] = new Label();
        }
        mv.visitLookupSwitchInsn(dflt, processIds, labels);
        for (int i = 0; i < models.size(); i++) {
            ElementModel model = models.get(i);
            boolean method = model.getType() == 2;
            mv.visitLabel(labels[i]);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            Label ifeq = new Label();
            if (!method) {
                mv.visitVarInsn(ILOAD, 2);
                mv.visitJumpInsn(IFEQ, ifeq);
            }
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, elementOwner, "_local", "L" + owner + ";");
            if (method) {
                StringBuilder desc = new StringBuilder();
                if (model.getDesc().length > 0) {
                    for (int j = 0; j < model.getDesc().length; j++) {
                        Type type = model.getDesc()[j];
                        desc.append(type.getDescriptor());
                        mv.visitVarInsn(ALOAD, 3);
                        VisitorUtils.visitIntPushStack(mv, j);
                        mv.visitInsn(AALOAD);
                        VisitorUtils.visitCheckCast(mv, type);
                    }
                }
                mv.visitMethodInsn(INVOKESTATIC, owner, model.getAccessName(),
                        "(L" + owner + ";" + desc.toString() + ")Ljava/lang/Object;", false);
                mv.visitInsn(ARETURN);
            } else {
                mv.visitVarInsn(ALOAD, 3);
                mv.visitInsn(ICONST_0);
                mv.visitInsn(AALOAD);
                VisitorUtils.visitCheckCast(mv, model.getReturnType());
                mv.visitMethodInsn(INVOKESTATIC, owner, model.getAccessName(),
                        model.getGetAndSet()[0], false);
                mv.visitInsn(ARETURN);
                mv.visitLabel(ifeq);
                mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                mv.visitVarInsn(ALOAD, 0);
                mv.visitFieldInsn(GETFIELD, elementOwner, "_local", "L" + owner + ";");
                mv.visitMethodInsn(INVOKESTATIC, owner, model.getAccessName(),
                        model.getGetAndSet()[1], false);
                mv.visitInsn(ARETURN);
            }
        }
        mv.visitLabel(dflt);
        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitInsn(ACONST_NULL);
        mv.visitInsn(ARETURN);
    }

    interface Operate {
        void doWhat(int num);
    }

    private static void setInterceptorAnnotationValues(
            List<InterceptionModel.InterceptorsBean> model, MethodVisitor mv, Operate doStart,
            Operate doEnd) {
        for (int j = 0; j < model.size(); j++) {
            InterceptionModel.InterceptorsBean bean = model.get(j);
            if (bean.getValues() != null && bean.getDefaultX() != null && bean.getDefaultX().size() > 0) {
                int size = bean.getValues().size();
                for (InterceptionModel.InterceptorsBean.DefaultBean defaultValue : bean.getDefaultX()) {
                    String key = defaultValue.getKey();
                    boolean fromDefault = true;
                    Object value = defaultValue.getValue();
                    String type = defaultValue.getVt();
                    for (int k = 0; k < size; k++) {
                        if (k % 2 == 0 && key.equals(bean.getValues().get(k).toString())) {
                            value = bean.getValues().get(k + 1);
                            fromDefault = false;
                            break;
                        }
                    }
                    doStart.doWhat(j);
                    loadAnnotationValue(mv, key, value, type, bean.getAnnotation(), fromDefault);
                    doEnd.doWhat(j);
                }
            }
        }
    }

}
