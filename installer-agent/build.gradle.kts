// Forge/NeoForge installer agent (package git.artdeell.installer_agent).
// Vendored from the `forge_installer/` module of
// https://github.com/PojavLauncherTeam/PojavLauncher (LGPL-3.0).
// Produces forge_installer.jar, the artifact name the launcher consumes.
//
// DRAFT: matches the upstream build (fat jar + PreMain-Class manifest);
// iterate via CI runs if anything drifts.

plugins { `java-library` }

base { archivesName.set("forge_installer") }

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(8)) }
}

dependencies {
    implementation("org.json:json:20230618")
}

tasks.jar {
    // Bundle runtime dependencies so the agent jar is self-contained.
    from({
        configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes(
            "Manifest-Version" to "1.0",
            "PreMain-Class" to "git.artdeell.installer_agent.Agent"
        )
    }
}
