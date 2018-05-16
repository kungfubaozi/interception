package com.zskpaco.interception.plugin.utils;

import org.objectweb.asm.*;

import static org.objectweb.asm.Opcodes.*;

public class VisitorUtils {

    public static void log(String format, Object... args) {
        //System.out.println(String.format(format, args));
    }

    public static void createInstance(ClassVisitor cv, String name, boolean singleton) {
        if (singleton) {
            VisitorUtils.createSingleton(cv, name);
        }
        MethodVisitor mv = cv.visitMethod(ACC_PUBLIC + ACC_STATIC, "_get_instance", "()L" + name + ";", null, null);
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        if (singleton) {
            mv.visitMethodInsn(INVOKESTATIC, name, "_get_owner_singleton", "()L" + name + ";", false);
        } else {
            mv.visitTypeInsn(NEW, name);
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, name, "<init>", "()V", false);
        }
        mv.visitInsn(ARETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }


    private static void createSingleton(ClassVisitor cv, String name) {
        MethodVisitor mv;
        FieldVisitor fv;
        {
            fv = cv.visitField(ACC_STATIC, "_owner_singleton_instance", "L" + name + ";", null, null);
            fv.visitEnd();
        }
        {
            mv = cv.visitMethod(ACC_STATIC, "_get_owner_singleton", "()L" + name + ";", null, null);
            mv.visitCode();
            Label l0 = new Label();
            Label l1 = new Label();
            Label l2 = new Label();
            mv.visitTryCatchBlock(l0, l1, l2, null);
            Label l3 = new Label();
            mv.visitTryCatchBlock(l2, l3, l2, null);
            Label l4 = new Label();
            mv.visitLabel(l4);
            mv.visitFieldInsn(GETSTATIC, name, "_owner_singleton_instance", "L" + name + ";");
            Label l5 = new Label();
            mv.visitJumpInsn(IFNONNULL, l5);
            Label l6 = new Label();
            mv.visitLabel(l6);
            mv.visitLdcInsn(Type.getType("L" + name + ";"));
            mv.visitInsn(DUP);
            mv.visitVarInsn(ASTORE, 0);
            mv.visitInsn(MONITORENTER);
            mv.visitLabel(l0);
            mv.visitFieldInsn(GETSTATIC, name, "_owner_singleton_instance", "L" + name + ";");
            Label l7 = new Label();
            mv.visitJumpInsn(IFNONNULL, l7);
            Label l8 = new Label();
            mv.visitLabel(l8);
            mv.visitTypeInsn(NEW, name);
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, name, "<init>", "()V", false);
            mv.visitFieldInsn(PUTSTATIC, name, "_owner_singleton_instance", "L" + name + ";");
            mv.visitLabel(l7);
            mv.visitFrame(Opcodes.F_APPEND, 1, new Object[]{"java/lang/Object"}, 0, null);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitInsn(MONITOREXIT);
            mv.visitLabel(l1);
            mv.visitJumpInsn(GOTO, l5);
            mv.visitLabel(l2);
            mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[]{"java/lang/Throwable"});
            mv.visitVarInsn(ASTORE, 1);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitInsn(MONITOREXIT);
            mv.visitLabel(l3);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitInsn(ATHROW);
            mv.visitLabel(l5);
            mv.visitFrame(Opcodes.F_CHOP, 1, null, 0, null);
            mv.visitFieldInsn(GETSTATIC, name, "_owner_singleton_instance", "L" + name + ";");
            mv.visitInsn(ARETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
    }

    public static String getCombinationName(String desc) {
        StringBuilder builder = new StringBuilder();
        Type[] types = Type.getArgumentTypes(desc);
        for (Type type : types) {
            builder.append(type.getClassName());
        }
        return builder.toString();
    }

    public static void visitEnum(MethodVisitor mv, String[] v) {
        if (v.length == 2 && v[0].startsWith("L") && v[0].endsWith(";")) {
            if (v[0].contains("$")) {
                throw new IllegalArgumentException(
                        "does not support the enum of the internal class!");
            }
            mv.visitFieldInsn(GETSTATIC, v[0].substring(1, v[0].length() - 1), v[1], v[0]);
        }
    }

    /**
     * 类型转换
     *
     * @param type
     */
    public static void visitCheckCast(MethodVisitor mv, String type) {
        Type t = Type.getType(type);
        int sort = t.getSort();
        switch (sort) {
            case 0:
                break;
            case 1:
                mv.visitTypeInsn(CHECKCAST, "java/lang/Boolean");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z",
                        false);
                break;
            case 2:
                mv.visitTypeInsn(CHECKCAST, "java/lang/Char");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Char", "charValue", "()C", false);
                break;
            case 3:
                mv.visitTypeInsn(CHECKCAST, "java/lang/Byte");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Byte", "byteValue", "()B", false);
                break;
            case 4:
                mv.visitTypeInsn(CHECKCAST, "java/lang/Short");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Short", "shortValue", "()S", false);
                break;
            case 5:
                mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
                break;
            case 6:
                mv.visitTypeInsn(CHECKCAST, "java/lang/Float");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F", false);
                break;
            case 7:
                mv.visitTypeInsn(CHECKCAST, "java/lang/Long");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Long", "longValue", "()L", false);
                break;
            case 8:
                mv.visitTypeInsn(CHECKCAST, "java/lang/Double");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D", false);
                break;
            case 9://数组
                mv.visitTypeInsn(CHECKCAST, t.getDescriptor());
                mv.visitTypeInsn(CHECKCAST, t.getDescriptor());
                break;
            default:
                mv.visitTypeInsn(CHECKCAST, t.getInternalName());
        }
    }

    public static void visitCheckCast(MethodVisitor mv, Type t) {
        int sort = t.getSort();
        switch (sort) {
            case 1:
                mv.visitTypeInsn(CHECKCAST, "java/lang/Boolean");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z",
                        false);
                break;
            case 2:
                mv.visitTypeInsn(CHECKCAST, "java/lang/Char");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Char", "charValue", "()C", false);
                break;
            case 3:
                mv.visitTypeInsn(CHECKCAST, "java/lang/Byte");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Byte", "byteValue", "()B", false);
                break;
            case 4:
                mv.visitTypeInsn(CHECKCAST, "java/lang/Short");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Short", "shortValue", "()S", false);
                break;
            case 5:
                mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
                break;
            case 6:
                mv.visitTypeInsn(CHECKCAST, "java/lang/Float");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F", false);
                break;
            case 7:
                mv.visitTypeInsn(CHECKCAST, "java/lang/Long");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Long", "longValue", "()L", false);
                break;
            case 8:
                mv.visitTypeInsn(CHECKCAST, "java/lang/Double");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D", false);
                break;
            case 9://数组
                mv.visitTypeInsn(CHECKCAST, t.getDescriptor());
                mv.visitTypeInsn(CHECKCAST, t.getDescriptor());
                break;
            default:
                mv.visitTypeInsn(CHECKCAST, t.getInternalName());
        }
    }

    /**
     * 返回
     *
     * @param type
     */
    public static void visitReturn(MethodVisitor mv, String type) {
        Type t = Type.getType(type);
//        Type returnType = Type.getReturnType(desc);
//        if (returnType.getSort() == Type.VOID) {
//            mv.visitInsn(RETURN);
//            return;
//        }
        int sort = t.getSort();
        switch (sort) {
            case 0:
                mv.visitInsn(RETURN);
                break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                mv.visitInsn(Opcodes.IRETURN);
                break;
            case 6:
                mv.visitInsn(Opcodes.FRETURN);
                break;
            case 7:
                mv.visitInsn(Opcodes.LRETURN);
                break;
            case 8:
                mv.visitInsn(Opcodes.DRETURN);
                break;
            default:
                mv.visitInsn(Opcodes.ARETURN);
        }
    }

    /**
     * mv.visitInsn()
     * mv.visitIntInsn()
     *
     * @param size
     */
    public static void visitSizeInsn(MethodVisitor mv, int size) {
        if (size > 5) mv.visitIntInsn(Opcodes.BIPUSH, size);
        else mv.visitInsn(size + 3);
    }

    public static void visitVarLoadInsn(MethodVisitor mv, String methodDesc) {
        Type[] types = Type.getArgumentTypes(methodDesc);
        visitArgumentsLoadInsn(mv, types);
    }

    /**
     * mv.visitVarInsn(ALOAD/DLOAD/ILOAD....,)
     *
     * @param type
     */
    public static void visitVarLoadInsn(MethodVisitor mv, String type, int var) {
        Type t = Type.getType(type);
        int sort = t.getSort();
        switch (sort) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                mv.visitVarInsn(Opcodes.ILOAD, var);
                break;
            case 6:
                mv.visitVarInsn(Opcodes.FLOAD, var);
                break;
            case 7:
                mv.visitVarInsn(Opcodes.LLOAD, var);
                break;
            case 8:
                mv.visitVarInsn(Opcodes.DLOAD, var);
                break;
            default:
                mv.visitVarInsn(Opcodes.ALOAD, var);
        }
    }

    public static void visitArgumentsLoadInsn(MethodVisitor mv, Type[] types) {
        int offset = 0;
        for (int i = 0; i < types.length; i++) {
            int sort = types[i].getSort();
            switch (sort) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    mv.visitVarInsn(Opcodes.ILOAD, i + offset);
                    break;
                case 6:
                    mv.visitVarInsn(Opcodes.FLOAD, i + offset);
                    break;
                case 7:
                    mv.visitVarInsn(Opcodes.LLOAD, i + offset);
                    break;
                case 8:
                    mv.visitVarInsn(Opcodes.DLOAD, i + offset);
                    offset = offset + 1;
                    break;
                default:
                    mv.visitVarInsn(Opcodes.ALOAD, i + offset);
            }
        }
    }

    public static void visitArgumentsLoadInsn2(MethodVisitor mv, Type[] types) {
        int offset = 1;
        for (int i = 0; i < types.length; i++) {
            mv.visitInsn(DUP);
            visitIntPushStack(mv, i);
            int sort = types[i].getSort();
            switch (sort) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    mv.visitVarInsn(Opcodes.ILOAD, i + offset);
                    break;
                case 6:
                    mv.visitVarInsn(Opcodes.FLOAD, i + offset);
                    break;
                case 7:
                    mv.visitVarInsn(Opcodes.LLOAD, i + offset);
                    break;
                case 8:
                    mv.visitVarInsn(Opcodes.DLOAD, i + offset);
                    offset = offset + 1;
                    break;
                default:
                    mv.visitVarInsn(Opcodes.ALOAD, i + offset);
            }
            visitValueOf(mv, types[i].getDescriptor());
            mv.visitInsn(AASTORE);
        }
    }

    public static void visitIntPushStack(MethodVisitor mv, int i) {
        if (i == -1) {
            mv.visitInsn(ICONST_M1);
        } else if (i >= 0 && i <= 5) {
            mv.visitInsn(i + 3);
        } else if (i >= -127 && i <= 128) {
            mv.visitIntInsn(BIPUSH, i);
        } else if (i >= -32768 && i <= 32767) {
            mv.visitIntInsn(SIPUSH, i);
        } else if (i >= -2147483648 && i <= 2147483647) {
            mv.visitLdcInsn(new Integer(i));
        } else {
            throw new IllegalArgumentException("value out of stack!");
        }
    }

    /**
     * I:mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false)
     * .....
     *
     * @param type
     */
    public static void visitValueOf(MethodVisitor mv, String type) {
        Type t = Type.getType(type);
        int sort = t.getSort();
        switch (sort) {
            case 1:
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Boolean", "valueOf",
                        "(Z)Ljava/lang/Boolean;", false);
                break;
            case 2:
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Char", "valueOf",
                        "(C)Ljava/lang/Char;", false);
                break;
            case 3:
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Byte", "valueOf",
                        "(B)Ljava/lang/Byte;", false);
                break;
            case 4:
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Short", "valueOf",
                        "(S)Ljava/lang/Short;", false);
                break;
            case 5:
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf",
                        "(I)Ljava/lang/Integer;", false);
                break;
            case 6:
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Float", "valueOf",
                        "(F)Ljava/lang/Float;", false);
                break;
            case 7:
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Long", "valueOf",
                        "(L)Ljava/lang/Long;", false);
                break;
            case 8:
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Double", "valueOf",
                        "(D)Ljava/lang/Double;", false);
                break;
            default:

        }
    }

    public static void visitLdcInsn(MethodVisitor mv, String desc) {
        Type t = Type.getType(desc);
        int sort = t.getSort();
        switch (sort) {
            case 0:
                mv.visitFieldInsn(GETSTATIC, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
                break;
            case 1:
                mv.visitFieldInsn(GETSTATIC, "java/lang/Boolean", "TYPE", "Ljava/lang/Class;");
                break;
            case 2:
                mv.visitFieldInsn(GETSTATIC, "java/lang/Char", "TYPE", "Ljava/lang/Class;");
                break;
            case 3:
                mv.visitFieldInsn(GETSTATIC, "java/lang/Byte", "TYPE", "Ljava/lang/Class;");
                break;
            case 4:
                mv.visitFieldInsn(GETSTATIC, "java/lang/Short", "TYPE", "Ljava/lang/Class;");
                break;
            case 5:
                mv.visitFieldInsn(GETSTATIC, "java/lang/Integer", "TYPE", "Ljava/lang/Class;");
                break;
            case 6:
                mv.visitFieldInsn(GETSTATIC, "java/lang/Float", "TYPE", "Ljava/lang/Class;");
                break;
            case 7:
                mv.visitFieldInsn(GETSTATIC, "java/lang/Long", "TYPE", "Ljava/lang/Class;");
                break;
            case 8:
                mv.visitFieldInsn(GETSTATIC, "java/lang/Double", "TYPE", "Ljava/lang/Class;");
                break;
            default:
                mv.visitLdcInsn(Type.getType(t.getDescriptor()));
                break;
        }
    }

}
