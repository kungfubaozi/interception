package com.richard.interception.plugin.basic;

import com.richard.interception.plugin.bean.InterceptionModel;
import com.richard.interception.plugin.Transformer;
import org.objectweb.asm.*;

import java.util.List;

import static org.objectweb.asm.Opcodes.*;

public class _RunnerInstanceFactory {

    public static byte[] dump(String owner, InterceptionModel model) {

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        MethodVisitor mv;
        cw.visit(52, ACC_PUBLIC + ACC_SUPER, owner, null, "java/lang/Object", new String[]{"com/richard/interception/internal/IElementInstanceLoader"});
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
            mv = cw.visitMethod(ACC_PUBLIC, "getInterceptorInstance", "(Ljava/lang/Class;)Lcom/richard/interception/AnnotationInterceptor;", "(Ljava/lang/Class<+Ljava/lang/annotation/Annotation;>;)Lcom/richard/interception/AnnotationInterceptor;", null);
            mv.visitCode();
            List<InterceptionModel.InterceptorsBean> beans = model.getInterceptors();
            for (int i = 0; i < beans.size(); i++) {
                InterceptionModel.InterceptorsBean bean = beans.get(i);

                String interceptor = Transformer.config.getDebug() ? bean.getDebug() : bean.getRelease();
                if (interceptor == null) {
                    throw new IllegalArgumentException("No corresponding interceptor class!");
                }

                mv.visitVarInsn(ALOAD, 1);
                mv.visitLdcInsn(Type.getType("L" + bean.getAnnotation() + ";"));
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "equals", "(Ljava/lang/Object;)Z", false);
                Label l1 = new Label();
                mv.visitJumpInsn(IFEQ, l1);
                Label l2 = new Label();
                mv.visitLabel(l2);
                mv.visitMethodInsn(INVOKESTATIC, interceptor, "_get_instance", "()L" + interceptor + ";", false);
                mv.visitInsn(ARETURN);
                mv.visitLabel(l1);
                mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            }
            mv.visitInsn(ACONST_NULL);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }

        return cw.toByteArray();
    }

}
