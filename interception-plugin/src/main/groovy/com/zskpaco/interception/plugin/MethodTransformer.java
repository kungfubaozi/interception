package com.zskpaco.interception.plugin;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.GeneratorAdapter;

import static org.objectweb.asm.Opcodes.*;

import java.util.HashSet;

public class MethodTransformer extends GeneratorAdapter {

    private String owner;
    private boolean elementLoader = false;
    private HashSet<ClassHandler.Surround> surroundFieldSet;
    private String elementOwner;

    MethodTransformer(String owner, MethodVisitor mv, int access, String name, String desc,
                      HashSet<ClassHandler.Surround> surroundFieldSet, boolean elementLoader, String elementOwner) {
        super(ASM6, mv, access, name, desc);
        this.owner = owner;
        this.elementOwner = elementOwner;
        this.elementLoader = elementLoader;
        this.surroundFieldSet = surroundFieldSet;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        if (opcode == INVOKESTATIC && owner.equals("com/richard/interception/Assignment") && name.equals("visit")) {
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
            mv.visitVarInsn(ALOAD, 0);
            mv.visitTypeInsn(NEW, elementOwner);
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, elementOwner, "<init>", "()V", false);
            mv.visitFieldInsn(PUTFIELD, owner, "$_Element_Loader", "Lcom/richard/interception/internal/IElementLoader;");
            initLoader(mv, owner);
        }
    }

    private void initLoader(MethodVisitor mv, String owner) {
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, owner, "$_Element_Loader", "Lcom/richard/interception/internal/IElementLoader;");
        mv.visitVarInsn(ALOAD, 0);
        mv.visitInsn(ACONST_NULL);
        mv.visitMethodInsn(INVOKEINTERFACE, "com/richard/interception/internal/IElementLoader", "init", "(Ljava/lang/Object;Ljava/lang/Object;)V", true);

    }

    private void loadSurroundField(MethodVisitor mv, String owner, HashSet<ClassHandler.Surround> surroundFieldSet) {
        if (surroundFieldSet != null) {
            for (ClassHandler.Surround surround : surroundFieldSet) {
                String type = surround.desc.substring(1, surround.desc.length() - 1);
                mv.visitVarInsn(ALOAD, 0);
                mv.visitVarInsn(ALOAD, 0);
                mv.visitMethodInsn(INVOKESTATIC, type, "_init", "(Ljava/lang/Object;)" + surround.desc, false);
                mv.visitFieldInsn(PUTFIELD, owner, surround.name, surround.desc);
            }
        }
    }
}
