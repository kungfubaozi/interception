package com.zskpaco.interception.plugin.basic;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static com.zskpaco.interception.plugin.bean.TypeNames.*;

import static org.objectweb.asm.Opcodes.*;

public class _RunnerAsyncBytes {

    public static byte[] dump(String owner, String runnerOwner) {
        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;

        cw.visit(52, ACC_PUBLIC + ACC_SUPER, owner,
                "Landroid/os/AsyncTask<Ljava/lang/Void;Ljava/lang/Void;Ljava/lang/Object;>;",
                "android/os/AsyncTask", null);

        {
            fv = cw.visitField(ACC_PRIVATE + ACC_FINAL, "builder", L_INTERFACE_ELEMENT_BUILDER,
                    null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PRIVATE + ACC_FINAL, "buildLoader",
                    L_INTERFACE_ELEMENT_EXECUTION_LOADER, null, null);
            fv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>",
                    "(" + L_INTERFACE_ELEMENT_BUILDER + L_INTERFACE_ELEMENT_EXECUTION_LOADER + ")V",
                    null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(17, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "android/os/AsyncTask", "<init>", "()V", false);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(18, l1);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitFieldInsn(PUTFIELD, owner, "builder", L_INTERFACE_ELEMENT_BUILDER);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLineNumber(19, l2);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitFieldInsn(PUTFIELD, owner, "buildLoader", L_INTERFACE_ELEMENT_EXECUTION_LOADER);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitLineNumber(20, l3);
            mv.visitInsn(RETURN);
            Label l4 = new Label();
            mv.visitLabel(l4);
            mv.visitMaxs(2, 3);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PROTECTED + ACC_VARARGS, "doInBackground",
                    "([Ljava/lang/Void;)Ljava/lang/Object;", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(24, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, owner, "builder",
                    L_INTERFACE_ELEMENT_BUILDER);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, owner, "buildLoader", L_INTERFACE_ELEMENT_EXECUTION_LOADER);
            mv.visitMethodInsn(INVOKESTATIC, runnerOwner, "_current",
                    "(" + L_INTERFACE_ELEMENT_BUILDER + L_INTERFACE_ELEMENT_EXECUTION_LOADER + ")Ljava/lang/Object;",
                    false);
            mv.visitInsn(POP);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(25, l1);
            mv.visitInsn(ACONST_NULL);
            mv.visitInsn(ARETURN);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PROTECTED + ACC_BRIDGE + ACC_SYNTHETIC, "doInBackground",
                    "([Ljava/lang/Object;)Ljava/lang/Object;", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(12, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitTypeInsn(CHECKCAST, "[Ljava/lang/Void;");
            mv.visitMethodInsn(INVOKEVIRTUAL, owner, "doInBackground",
                    "([Ljava/lang/Void;)Ljava/lang/Object;", false);
            mv.visitInsn(ARETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }

}
