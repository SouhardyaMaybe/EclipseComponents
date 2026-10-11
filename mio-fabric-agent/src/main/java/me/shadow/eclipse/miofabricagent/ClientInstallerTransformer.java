package me.shadow.eclipse.miofabricagent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Rewrites {@code net.fabricmc.installer.client.ClientInstaller} so that every
 * store into local variable slot 4 inside a method named {@code install} is
 * immediately overwritten with the {@code profileName} system property.
 *
 * In the Fabric installer's {@code install} method that slot holds the
 * profile name, so the injection makes the installer create the profile under
 * the custom name the launcher passes with {@code -DprofileName}. The target
 * class is supplied by the Fabric installer jar on the install-time classpath;
 * this agent never links against Fabric classes itself.
 */
final class ClientInstallerTransformer implements ClassFileTransformer {

    /** Internal (slash-separated) name of the transformed class. */
    private static final String TARGET_CLASS = "net/fabricmc/installer/client/ClientInstaller";

    /** Only methods with this name are visited. */
    private static final String TARGET_METHOD = "install";

    /** System property that supplies the replacement profile name. */
    private static final String PROFILE_NAME_PROPERTY = "profileName";

    /** Local variable slot that holds the profile name in the target method. */
    private static final int PROFILE_NAME_SLOT = 4;

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer)
            throws IllegalClassFormatException {
        if (!TARGET_CLASS.equals(className)) {
            // Not our target: let the JVM load the class untouched.
            return null;
        }
        try {
            ClassReader reader = new ClassReader(classfileBuffer);
            ClassWriter writer = new ClassWriter(reader, 0);
            ClassVisitor visitor = new ClassVisitor(Opcodes.ASM9, writer) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String descriptor,
                                                 String signature, String[] exceptions) {
                    MethodVisitor delegate = super.visitMethod(access, name, descriptor, signature, exceptions);
                    if (!TARGET_METHOD.equals(name)) {
                        return delegate;
                    }
                    return new ProfileNameSlotVisitor(delegate);
                }
            };
            reader.accept(visitor, 0);
            System.out.println("[MioFabricAgent] profileName=" + System.getProperty(PROFILE_NAME_PROPERTY));
            return writer.toByteArray();
        } catch (Throwable t) {
            // Keep the installer usable even if the transform fails.
            t.printStackTrace();
            return classfileBuffer;
        }
    }

    /**
     * Forwards every instruction unchanged, but after each {@code astore} into
     * the profile-name slot re-reads the {@code profileName} system property
     * and stores that instead.
     */
    private static final class ProfileNameSlotVisitor extends MethodVisitor {

        ProfileNameSlotVisitor(MethodVisitor delegate) {
            super(Opcodes.ASM9, delegate);
        }

        @Override
        public void visitVarInsn(int opcode, int var) {
            super.visitVarInsn(opcode, var);
            if (opcode == Opcodes.ASTORE && var == PROFILE_NAME_SLOT) {
                visitLdcInsn(PROFILE_NAME_PROPERTY);
                visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "getProperty",
                        "(Ljava/lang/String;)Ljava/lang/String;", false);
                super.visitVarInsn(opcode, var);
            }
        }
    }
}
