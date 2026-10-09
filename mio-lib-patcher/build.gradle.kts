// MioLibPatcher java agent (com.mio.libpatcher) - bytecode transformers used
// on the Minecraft launch classpath.
// Vendored from https://github.com/ShirosakiMio/MioLibPatcher.
// NOTE: upstream ships WITHOUT an explicit license file - see UPSTREAM.md.
// Produces MioLibPatcher.jar, the artifact the launcher consumes.
//
// DRAFT: fat jar so the agent is self-contained (javassist bundled);
// iterate via CI runs.

plugins { `java-library` }

base { archivesName.set("MioLibPatcher") }

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(8)) }
}

dependencies {
    implementation("org.javassist:javassist:3.29.2-GA")
}

tasks.jar {
    from({
        configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes(
            "Manifest-Version" to "1.0",
            "Premain-Class" to "com.mio.libpatcher.MainAgent",
            "Agent-Class" to "com.mio.libpatcher.MainAgent",
            "Can-Redefine-Classes" to "true",
            "Can-Retransform-Classes" to "true"
        )
    }
}
