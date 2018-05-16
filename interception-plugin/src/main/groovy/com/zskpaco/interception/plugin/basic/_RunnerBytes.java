package com.zskpaco.interception.plugin.basic;

import org.objectweb.asm.*;


import static com.zskpaco.interception.plugin.bean.TypeNames.*;

import static org.objectweb.asm.Opcodes.*;

public class _RunnerBytes {

    public static byte[] dump(String owner, String instance, String uiOwner, String asyncOwner) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        FieldVisitor fv;
        MethodVisitor mv;

        cw.visit(52, ACC_PUBLIC + ACC_SUPER, owner, null, "java/lang/Object", null);

        cw.visitInnerClass("java/util/Map$Entry", "java/util/Map", "Entry",
                ACC_PUBLIC + ACC_STATIC + ACC_ABSTRACT + ACC_INTERFACE);

        {
            fv = cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, "HANDLER",
                    "Landroid/os/Handler;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, "BUILD_MAP", "Ljava/util/Map;",
                    "Ljava/util/Map<Ljava/lang/String;Ljava/util/Map<Ljava/lang/Integer;" + L_INTERFACE_ELEMENT_MULTIPART_BUILDER + ";>;>;",
                    null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, "FACTORY",
                    L_INTERFACE_ELEMENT_INSTANCE_LOADER, null, null);
            fv.visitEnd();
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
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "check", "(Ljava/lang/String;)Z", null,
                    null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitFieldInsn(GETSTATIC, owner, "BUILD_MAP", "Ljava/util/Map;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "containsKey",
                    "(Ljava/lang/Object;)Z", true);
            Label l1 = new Label();
            mv.visitJumpInsn(IFEQ, l1);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitInsn(ICONST_1);
            mv.visitInsn(IRETURN);
            mv.visitLabel(l1);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            mv.visitInsn(ICONST_0);
            mv.visitInsn(IRETURN);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitMaxs(2, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "getBuilderById",
                    "(Ljava/lang/String;I)" + L_INTERFACE_ELEMENT_MULTIPART_BUILDER, null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitFieldInsn(GETSTATIC, owner, "BUILD_MAP", "Ljava/util/Map;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "get",
                    "(Ljava/lang/Object;)Ljava/lang/Object;", true);
            mv.visitTypeInsn(CHECKCAST, "java/util/Map");
            mv.visitVarInsn(ILOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf",
                    "(I)Ljava/lang/Integer;", false);
            mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "get",
                    "(Ljava/lang/Object;)Ljava/lang/Object;", true);
            mv.visitTypeInsn(CHECKCAST, INTERFACE_ELEMENT_MULTIPART_BUILDER);
            mv.visitInsn(ARETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "add",
                    "(Ljava/lang/String;Ljava/util/Map;)V",
                    "(Ljava/lang/String;Ljava/util/Map<Ljava/lang/Integer;" + L_INTERFACE_ELEMENT_MULTIPART_BUILDER + ";>;)V",
                    null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitFieldInsn(GETSTATIC, owner, "BUILD_MAP", "Ljava/util/Map;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "put",
                    "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true);
            mv.visitInsn(POP);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitInsn(RETURN);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitMaxs(3, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "_process",
                    "(" + L_INTERFACE_ELEMENT_BUILDER + L_INTERFACE_ELEMENT_EXECUTION_LOADER + ")Ljava/lang/Object;",
                    null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitVarInsn(ALOAD, 0);
            Label l1 = new Label();
            mv.visitJumpInsn(IFNULL, l1);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ELEMENT_BUILDER, "async", "()Z", true);
            Label l3 = new Label();
            mv.visitJumpInsn(IFEQ, l3);
            Label l4 = new Label();
            mv.visitLabel(l4);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, owner, "_async",
                    "(" + L_INTERFACE_ELEMENT_BUILDER + L_INTERFACE_ELEMENT_EXECUTION_LOADER + ")V",
                    false);
            mv.visitJumpInsn(GOTO, l1);
            mv.visitLabel(l3);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ELEMENT_BUILDER, "ui", "()Z", true);
            Label l5 = new Label();
            mv.visitJumpInsn(IFEQ, l5);
            Label l6 = new Label();
            mv.visitLabel(l6);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, owner, "_ui",
                    "(" + L_INTERFACE_ELEMENT_BUILDER + L_INTERFACE_ELEMENT_EXECUTION_LOADER + ")V",
                    false);
            mv.visitJumpInsn(GOTO, l1);
            mv.visitLabel(l5);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, owner, "_current",
                    "(" + L_INTERFACE_ELEMENT_BUILDER + L_INTERFACE_ELEMENT_EXECUTION_LOADER + ")Ljava/lang/Object;",
                    false);
            mv.visitInsn(ARETURN);
            mv.visitLabel(l1);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            mv.visitInsn(ACONST_NULL);
            mv.visitInsn(ARETURN);
            Label l7 = new Label();
            mv.visitLabel(l7);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "_surround",
                    "(" + L_INTERFACE_ELEMENT_MULTIPART_BUILDER + L_INTERFACE_ELEMENT_EXECUTION_LOADER + ")V",
                    null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ELEMENT_MULTIPART_BUILDER, "generate",
                    "(" + L_INTERFACE_ELEMENT_EXECUTION_LOADER + ")" + L_INTERFACE_ELEMENT_CONTROLLER,
                    true);
            mv.visitVarInsn(ASTORE, 2);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ELEMENT_CONTROLLER, "annotations",
                    "()[Ljava/lang/Class;", true);
            mv.visitInsn(ICONST_0);
            mv.visitInsn(AALOAD);
            mv.visitVarInsn(ASTORE, 3);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitFieldInsn(GETSTATIC, owner, "FACTORY", L_INTERFACE_ELEMENT_INSTANCE_LOADER);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ELEMENT_INSTANCE_LOADER,
                    "getInterceptorInstance",
                    "(Ljava/lang/Class;)" + L_INTERFACE_ANNOTATION_INTERCEPTOR, true);
            mv.visitVarInsn(ASTORE, 4);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ELEMENT_CONTROLLER, "getElement",
                    "()" + L_INTERFACE_ELEMENT, true);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitInsn(ACONST_NULL);
            mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ELEMENT_CONTROLLER, "getAnnotationValue",
                    "(Ljava/lang/Class;)Ljava/util/Map;", true);
            mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ANNOTATION_INTERCEPTOR, "intercept",
                    "(" + L_INTERFACE_ELEMENT + "Ljava/util/Map;)Ljava/lang/Object;", true);
            mv.visitInsn(POP);
            Label l4 = new Label();
            mv.visitLabel(l4);
            mv.visitInsn(RETURN);
            Label l5 = new Label();
            mv.visitLabel(l5);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "_current",
                    "(" + L_INTERFACE_ELEMENT_BUILDER + L_INTERFACE_ELEMENT_EXECUTION_LOADER + ")Ljava/lang/Object;",
                    null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitInsn(ACONST_NULL);
            mv.visitVarInsn(ASTORE, 2);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitVarInsn(ALOAD, 0);
            Label l2 = new Label();
            mv.visitJumpInsn(IFNONNULL, l2);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitInsn(ARETURN);
            mv.visitLabel(l2);
            mv.visitFrame(Opcodes.F_APPEND, 1, new Object[]{"java/lang/Object"}, 0, null);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ELEMENT_BUILDER, "generate",
                    "(" + L_INTERFACE_ELEMENT_EXECUTION_LOADER + ")" + L_INTERFACE_ELEMENT_CONTROLLER,
                    true);
            mv.visitVarInsn(ASTORE, 3);
            Label l4 = new Label();
            mv.visitLabel(l4);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ELEMENT_CONTROLLER, "annotations",
                    "()[Ljava/lang/Class;", true);
            Label l5 = new Label();
            mv.visitJumpInsn(IFNULL, l5);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ELEMENT_CONTROLLER, "annotations",
                    "()[Ljava/lang/Class;", true);
            mv.visitInsn(ARRAYLENGTH);
            Label l6 = new Label();
            mv.visitJumpInsn(IFNE, l6);
            mv.visitLabel(l5);
            mv.visitFrame(Opcodes.F_APPEND, 1, new Object[]{INTERFACE_ELEMENT_CONTROLLER}, 0, null);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ELEMENT_CONTROLLER, "processed",
                    "()Ljava/lang/Object;", true);
            mv.visitVarInsn(ASTORE, 2);
            Label l7 = new Label();
            mv.visitJumpInsn(GOTO, l7);
            mv.visitLabel(l6);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            mv.visitInsn(ICONST_0);
            mv.visitVarInsn(ISTORE, 4);
            Label l8 = new Label();
            mv.visitLabel(l8);
            mv.visitFrame(Opcodes.F_APPEND, 1, new Object[]{Opcodes.INTEGER}, 0, null);
            mv.visitVarInsn(ILOAD, 4);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ELEMENT_CONTROLLER, "annotations",
                    "()[Ljava/lang/Class;", true);
            mv.visitInsn(ARRAYLENGTH);
            mv.visitJumpInsn(IF_ICMPGE, l7);
            Label l9 = new Label();
            mv.visitLabel(l9);
            mv.visitFieldInsn(GETSTATIC, owner, "FACTORY", L_INTERFACE_ELEMENT_INSTANCE_LOADER);
            mv.visitVarInsn(ALOAD, 3);
            Label l10 = new Label();
            mv.visitLabel(l10);
            mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ELEMENT_CONTROLLER, "annotations",
                    "()[Ljava/lang/Class;", true);
            mv.visitVarInsn(ILOAD, 4);
            mv.visitInsn(AALOAD);
            Label l11 = new Label();
            mv.visitLabel(l11);
            mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ELEMENT_INSTANCE_LOADER,
                    "getInterceptorInstance",
                    "(Ljava/lang/Class;)" + L_INTERFACE_ANNOTATION_INTERCEPTOR, true);
            mv.visitVarInsn(ASTORE, 5);
            Label l12 = new Label();
            mv.visitLabel(l12);
            mv.visitVarInsn(ALOAD, 5);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ELEMENT_CONTROLLER, "getElement",
                    "()" + L_INTERFACE_ELEMENT, true);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitVarInsn(ALOAD, 3);
            Label l13 = new Label();
            mv.visitLabel(l13);
            mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ELEMENT_CONTROLLER, "annotations",
                    "()[Ljava/lang/Class;", true);
            mv.visitVarInsn(ILOAD, 4);
            mv.visitInsn(AALOAD);
            mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ELEMENT_CONTROLLER, "getAnnotationValue",
                    "(Ljava/lang/Class;)Ljava/util/Map;", true);
            Label l14 = new Label();
            mv.visitLabel(l14);
            mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ANNOTATION_INTERCEPTOR, "intercept",
                    "(" + L_INTERFACE_ELEMENT + "Ljava/util/Map;)Ljava/lang/Object;", true);
            mv.visitVarInsn(ASTORE, 2);
            Label l15 = new Label();
            mv.visitLabel(l15);
            mv.visitIincInsn(4, 1);
            mv.visitJumpInsn(GOTO, l8);
            mv.visitLabel(l7);
            mv.visitFrame(Opcodes.F_CHOP, 1, null, 0, null);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitInsn(ARETURN);
            Label l16 = new Label();
            mv.visitLabel(l16);
            mv.visitMaxs(5, 6);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "_async",
                    "(" + L_INTERFACE_ELEMENT_BUILDER + L_INTERFACE_ELEMENT_EXECUTION_LOADER + ")V",
                    null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitTypeInsn(NEW, asyncOwner);
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESPECIAL, asyncOwner, "<init>",
                    "(" + L_INTERFACE_ELEMENT_BUILDER + L_INTERFACE_ELEMENT_EXECUTION_LOADER + ")V",
                    false);
            mv.visitInsn(ICONST_0);
            mv.visitTypeInsn(ANEWARRAY, "java/lang/Void");
            mv.visitMethodInsn(INVOKEVIRTUAL, asyncOwner, "execute",
                    "([Ljava/lang/Object;)Landroid/os/AsyncTask;", false);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitInsn(RETURN);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitMaxs(4, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "_ui",
                    "(" + L_INTERFACE_ELEMENT_BUILDER + L_INTERFACE_ELEMENT_EXECUTION_LOADER + ")V",
                    null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitFieldInsn(GETSTATIC, owner, "HANDLER", "Landroid/os/Handler;");
            mv.visitTypeInsn(NEW, uiOwner);
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESPECIAL, uiOwner, "<init>",
                    "(" + L_INTERFACE_ELEMENT_BUILDER + L_INTERFACE_ELEMENT_EXECUTION_LOADER + ")V",
                    false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "android/os/Handler", "post",
                    "(Ljava/lang/Runnable;)V", false);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitInsn(RETURN);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitMaxs(5, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitTypeInsn(NEW, "android/os/Handler");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESTATIC, "android/os/Looper", "getMainLooper",
                    "()Landroid/os/Looper;", false);
            mv.visitMethodInsn(INVOKESPECIAL, "android/os/Handler", "<init>",
                    "(Landroid/os/Looper;)V", false);
            mv.visitFieldInsn(PUTSTATIC, owner, "HANDLER", "Landroid/os/Handler;");
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitTypeInsn(NEW, "java/util/concurrent/ConcurrentHashMap");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/util/concurrent/ConcurrentHashMap", "<init>",
                    "()V", false);
            mv.visitFieldInsn(PUTSTATIC, owner, "BUILD_MAP", "Ljava/util/Map;");
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitTypeInsn(NEW, instance);
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, instance, "<init>", "()V", false);
            mv.visitFieldInsn(PUTSTATIC, owner, "FACTORY", L_INTERFACE_ELEMENT_INSTANCE_LOADER);
            mv.visitInsn(RETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }

}
