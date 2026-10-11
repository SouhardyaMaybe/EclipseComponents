// Fabric installer profile-name injection java agent
// (me.shadow.eclipse.miofabricagent). Own rewrite of the opaque prebuilt
// MioFabricAgent.jar blob the app used to ship; no public upstream source
// exists - see UPSTREAM.md for the provenance research and the decompiled
// behavior this module reproduces.
// Produces MioFabricAgent.jar, the artifact the launcher consumes.

plugins { `java-library` }

base { archivesName.set("MioFabricAgent") }

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(8)) }
}

dependencies {
    // Bytecode rewriting. The jar must be self-contained: the agent is
    // attached with -javaagent during the Fabric install and only its own
    // classes are guaranteed to be loadable, so ASM is merged into the jar
    // (same approach as the optifine-renamer module).
    implementation("org.ow2.asm:asm:9.7.1")
}

tasks.jar {
    // Plain merge into the `jar` task (not the Shadow plugin) because the
    // root build script pins `archiveFileName` for every Jar task and both
    // CI workflows invoke `gradle :mio-fabric-agent:jar`.
    from(provider {
        sourceSets.main.get().runtimeClasspath
            // `isFile` (not `!isDirectory`): this module has no resources, so
            // `build/resources/main` may not exist and zipTree would fail on it.
            .filter { it.isFile }
            .map { zipTree(it) }
    }) {
        // Our generated MANIFEST.MF stays authoritative; dependency
        // signature files would fail verification once re-packed.
        exclude("META-INF/MANIFEST.MF")
        exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA", "META-INF/*.EC")
        exclude("META-INF/INDEX.LIST")
        exclude("META-INF/maven/**")
    }
    manifest {
        attributes(
            "Manifest-Version" to "1.0",
            "Premain-Class" to "me.shadow.eclipse.miofabricagent.MioFabricAgent",
            "Agent-Class" to "me.shadow.eclipse.miofabricagent.MioFabricAgent",
            "Can-Redefine-Classes" to "true",
            "Can-Retransform-Classes" to "true"
        )
    }
}
