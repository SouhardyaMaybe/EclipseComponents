// OptiFine version renamer java agent (package com.movtery.optifine_renamer).
// Vendored from https://github.com/ZalithLauncher/OptiFineRenamer (MIT).
// Produces OptiFineRenamer.jar, the artifact name the launcher consumes.

plugins { `java-library` }

base { archivesName.set("OptiFineRenamer") }

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(8)) }
}

dependencies {
    implementation("org.ow2.asm:asm:9.7.1")
}

tasks.jar {
    // The launcher attaches this jar as a `-javaagent` on a minimal classpath,
    // so every class the agent touches has to travel inside the jar: merge the
    // runtime dependency jars (ASM) into the archive, exactly what the app's
    // prebuilt OptiFineRenamer.jar blob does (it bundles org.objectweb.asm).
    //
    // A plain merge into the existing `jar` task is used instead of the Shadow
    // plugin because the root build script pins `archiveFileName` for every
    // `Jar` task (so `jar` and `shadowJar` would both target
    // `build/libs/OptiFineRenamer.jar`) and both CI workflows invoke
    // `gradle :optifine-renamer:jar`. This keeps the change inside this module
    // and leaves the other nine modules byte-for-byte untouched.
    from(provider {
        sourceSets.main.get().runtimeClasspath
            .filter { !it.isDirectory }
            .map { zipTree(it) }
    }) {
        // Scoped to the merged dependency trees only: our generated
        // MANIFEST.MF (Premain-Class/Agent-Class) stays authoritative, and
        // dependency signature files would fail verification once re-packed.
        exclude("META-INF/MANIFEST.MF")
        exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA", "META-INF/*.EC")
        exclude("META-INF/INDEX.LIST")
        exclude("META-INF/maven/**")
    }
    manifest {
        attributes(
            "Manifest-Version" to "1.0",
            "Premain-Class" to "com.movtery.optifine_renamer.MainAgent",
            "Agent-Class" to "com.movtery.optifine_renamer.MainAgent"
        )
    }
}
