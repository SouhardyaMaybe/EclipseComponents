package me.shadow.eclipse.miofabricagent;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

/**
 * Java agent entry point for the Fabric installer profile-name injection.
 *
 * The launcher starts the Fabric installer with
 * {@code -DprofileName=<custom name> -javaagent:MioFabricAgent.jar}; the
 * agent registers {@link ClientInstallerTransformer}, which rewrites the
 * installer's {@code ClientInstaller.install} so the profile is created under
 * the launcher-supplied name.
 */
public final class MioFabricAgent {

    private MioFabricAgent() {
    }

    /**
     * Invoked by the JVM when the agent is loaded via {@code -javaagent}.
     */
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        System.out.println("[MioFabricAgent] Fabric installer profile-name injection active");
        instrumentation.addTransformer(new ClientInstallerTransformer(), true);
    }

    /**
     * Invoked only when the agent is attached to a running JVM; the launcher
     * never does this, the method exists to satisfy the Agent-Class contract.
     * The already-loaded-class scan compares against the simple class name,
     * matching the behavior of the original component.
     */
    public static void agentmain(String agentArgs, Instrumentation instrumentation)
            throws UnmodifiableClassException {
        instrumentation.addTransformer(new ClientInstallerTransformer(), true);
        for (Class<?> loadedClass : instrumentation.getAllLoadedClasses()) {
            if (loadedClass.getName().equals("ClientInstaller")) {
                instrumentation.retransformClasses(loadedClass);
                break;
            }
        }
    }
}
