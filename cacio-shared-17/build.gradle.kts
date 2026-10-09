// CACAO CTC shared classes for Java 17+ runtimes
// (com.github.caciocavallosilano.cacio.ctc.*).
// Vendored from https://github.com/FCL-Team/caciocavallo17 (GPL-2.0).
// Produces cacio-shared-1.19.1-SNAPSHOT.jar, the artifact the launcher consumes.
//
// DRAFT: compiles against JDK internals, so a set of --add-exports flags is
// required. The list below covers the imports currently vendored; extend it
// via CI feedback if javac reports additional encapsulation errors.

plugins { `java-library` }

base { archivesName.set("cacio-shared-1.19.1-SNAPSHOT") }

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(17)) }
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.addAll(
        listOf(
            "--add-exports=java.desktop/sun.awt=ALL-UNNAMED",
            "--add-exports=java.desktop/sun.awt.image=ALL-UNNAMED",
            "--add-exports=java.desktop/sun.awt.event=ALL-UNNAMED",
            "--add-exports=java.desktop/sun.awt.datatransfer=ALL-UNNAMED",
            "--add-exports=java.desktop/sun.font=ALL-UNNAMED",
            "--add-exports=java.desktop/sun.java2d=ALL-UNNAMED",
            "--add-exports=java.desktop/sun.java2d.pipe=ALL-UNNAMED",
            "--add-exports=java.desktop/sun.swing=ALL-UNNAMED"
        )
    )
}
