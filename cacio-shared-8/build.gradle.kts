// CACAO CTC "caciocavallo" shared classes for Java 8 runtimes
// (net.java.openjdk.cacio.ctc.* - AWT toolkit replacement used headless).
// Vendored from https://github.com/PojavLauncherTeam/caciocavallo (GPL-2.0).
// Produces cacio-shared-1.10-SNAPSHOT.jar, the artifact the launcher consumes.
//
// DRAFT: compiled with a JDK 8 toolchain so the pre-module `sun.*` internals
// stay visible; iterate via CI runs if flags need adjusting.

plugins { `java-library` }

base { archivesName.set("cacio-shared-1.10-SNAPSHOT") }

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(8)) }
}
