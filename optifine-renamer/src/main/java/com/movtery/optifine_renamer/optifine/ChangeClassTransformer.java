package com.movtery.optifine_renamer.optifine;

import com.movtery.optifine_renamer.Print;
import org.objectweb.asm.*;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.Objects;

public class ChangeClassTransformer implements ClassFileTransformer {
    private final String versionName;

    /**
     * 修改optifine.Installer类中的doInstall方法，将其mcVerOf变量修改为自定义的版本名，
     * OptiFine在安装的时候所创建的版本文件夹以及文件，都将以这个自定义的版本名进行命名
     * @param versionName 自定义的版本名称
     */
    public ChangeClassTransformer(String versionName) {
        this.versionName = versionName;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classFileBuffer) {
        if (Objects.equals("optifine/Installer", className)) {
            ClassReader classReader = new ClassReader(classFileBuffer);
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM9, classWriter) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                    if ("doInstall".equals(name) && "(Ljava/io/File;)V".equals(descriptor)) {
                        return new MethodVisitor(Opcodes.ASM9, super.visitMethod(access, name, descriptor, signature, exceptions)) {
                            private boolean modified = false;

                            @Override
                            public void visitVarInsn(int opcode, int varIndex) {
                                super.visitVarInsn(opcode, varIndex);
                                //String mcVerOf =...
                                //索引貌似一直都是7，如果之后有特殊情况，可能需要进行额外处理，能用就行（
                                if (!modified && opcode == Opcodes.ASTORE && varIndex == 7) {
                                    super.visitLdcInsn(versionName);
                                    super.visitVarInsn(Opcodes.ASTORE, 7);
                                    modified = true;
                                    Print.printLog("Modified mcVerOf to " + versionName);
                                }
                            }
                        };
                    }
                    return super.visitMethod(access, name, descriptor, signature, exceptions);
                }
            };
            classReader.accept(classVisitor, 0);
            return classWriter.toByteArray();
        }
        return classFileBuffer;
    }
}
