// CACAO CTC Android native-window backend for Java 8 runtimes
// (cacio-androidnw: net.java.openjdk.cacio.ctc.CTCAndroidInput etc.).
// Vendored from https://github.com/PojavLauncherTeam/caciocavallo (GPL-2.0).
// Produces cacio-androidnw-1.10-SNAPSHOT.jar, the artifact the launcher consumes.
//
// `lib/xml-libs-pack.jar` is the upstream system-scoped dependency, vendored
// as-is (JDK xml classes needed at compile time only).

plugins { `java-library` }

base { archivesName.set("cacio-androidnw-1.10-SNAPSHOT") }

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(8)) }
}

dependencies {
    api(project(":cacio-shared-8"))
    compileOnly(files("lib/xml-libs-pack.jar"))
}
