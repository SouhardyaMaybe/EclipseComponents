// CACAO CTC java agent for Java 17+ runtimes (CTCJavaAgent premain).
// Vendored from https://github.com/FCL-Team/caciocavallo17 (GPL-2.0).
// Produces cacio-agent.jar, the artifact the launcher consumes.

plugins { `java-library` }

base { archivesName.set("cacio-agent") }

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(17)) }
}

dependencies {
    // Upstream's agent pom depends on both shared and tta: the ctc.* classes
    // (CTCFontManager etc.) live in the tta module, not shared.
    api(project(":cacio-shared-17"))
    api(project(":cacio-tta-17"))
}

tasks.jar {
    manifest {
        attributes(
            "Manifest-Version" to "1.0",
            "PreMain-Class" to "com.github.caciocavallosilano.cacio.agent.CTCJavaAgent"
        )
    }
}
