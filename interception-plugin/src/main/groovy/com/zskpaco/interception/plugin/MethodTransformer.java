package com.zskpaco.interception.plugin;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.GeneratorAdapter;

import static org.objectweb.asm.Opcodes.*;
import static com.zskpaco.interception.plugin.bean.TypeNames.*;

import java.util.HashSet;

public class MethodTransformer extends GeneratorAdapter {

    private String owner;
    private boolean elementLoader;
    private HashSet<ClassHandler.Surround> surroundFieldSet;
    private String elementOwner;
    private String name;

    MethodTransformer(String owner, MethodVisitor mv, int access, String name, String desc,
                      HashSet<ClassHandler.Surround> surroundFieldSet, boolean elementLoader,
                      String elementOwner) {
        super(ASM6, mv, access, name, desc);
        this.owner = owner;
        this.name = name;
        this.elementOwner = elementOwner;
        this.elementLoader = elementLoader;
        this.surroundFieldSet = surroundFieldSet;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor,
                                boolean isInterface) {
        if (opcode == INVOKESTATIC && owner.equals(ASSIGNMENT) && name.equals("justAllOfThis")) {
            init();
        } else {
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }
    }

    private void init() {
        //先初始化当前
        initLoader();
        loadSurroundField(mv, owner, surroundFieldSet);
    }

    private void initLoader() {
        if (elementLoader) {
//            mv.visitVarInsn(ALOAD, 0);
//            mv.visitTypeInsn(NEW, elementOwner);
//            mv.visitInsn(DUP);
//            mv.visitMethodInsn(INVOKESPECIAL, elementOwner, "<init>", "()V", false);
//            mv.visitFieldInsn(PUTFIELD, owner, "$_Element_Loader", L_INTERFACE_ELEMENT_LOADER);

            mv.visitFieldInsn(GETSTATIC, owner, "$_Element_Loader", L_INTERFACE_ELEMENT_LOADER);
            Label l1 = new Label();
            mv.visitJumpInsn(IFNONNULL, l1);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitTypeInsn(NEW, elementOwner);
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, elementOwner, "<init>", "()V", false);
            mv.visitFieldInsn(PUTSTATIC, owner, "$_Element_Loader", L_INTERFACE_ELEMENT_LOADER);
            mv.visitLabel(l1);
            if (name.equals("<clinit>")) {
                //不支持在静态初始化方法内初始化
                throw new IllegalArgumentException(
                        "Do not support initialization in the static initialization method");
            }
            if (name.equals("<init>")) {
                mv.visitFrame(F_FULL, 1, new Object[]{owner}, 0, new Object[]{});
            } else {
                mv.visitFrame(F_SAME, 0, null, 0, null);
            }

            initLoader(mv, owner);
        }
    }

    private void initLoader(MethodVisitor mv, String owner) {
        //mv.visitVarInsn(ALOAD, 0);

        mv.visitFieldInsn(GETSTATIC, owner, "$_Element_Loader", L_INTERFACE_ELEMENT_LOADER);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitInsn(ACONST_NULL);
        mv.visitMethodInsn(INVOKEINTERFACE, INTERFACE_ELEMENT_LOADER, "init",
                "(Ljava/lang/Object;Ljava/lang/Object;)V", true);

    }

    private void loadSurroundField(MethodVisitor mv, String owner,
                                   HashSet<ClassHandler.Surround> surroundFieldSet) {
        if (surroundFieldSet != null) {
            for (ClassHandler.Surround surround : surroundFieldSet) {
                String type = surround.desc.substring(1, surround.desc.length() - 1);
                mv.visitVarInsn(ALOAD, 0);
                mv.visitVarInsn(ALOAD, 0);
                mv.visitMethodInsn(INVOKESTATIC, type, "_init",
                        "(Ljava/lang/Object;)" + surround.desc, false);
                mv.visitFieldInsn(PUTFIELD, owner, surround.name, surround.desc);
            }
        }
    }
}
