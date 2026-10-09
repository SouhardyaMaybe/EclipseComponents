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
    manifest {
        attributes(
            "Manifest-Version" to "1.0",
            "Premain-Class" to "com.movtery.optifine_renamer.MainAgent",
            "Agent-Class" to "com.movtery.optifine_renamer.MainAgent"
        )
    }
}
