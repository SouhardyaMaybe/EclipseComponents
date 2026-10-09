// CACAO CTC "TTA" (Toolkit Test Adapter / Swing layer) for Java 17+ runtimes.
// Vendored from https://github.com/FCL-Team/caciocavallo17 (GPL-2.0).
// Produces cacio-tta-1.19.1-SNAPSHOT.jar, the artifact the launcher consumes.
//
// DRAFT: jide-oss and assertj-swing-junit follow upstream compile scope;
// iterate via CI runs if scopes need adjusting.

plugins { `java-library` }

base { archivesName.set("cacio-tta-1.19.1-SNAPSHOT") }

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(17)) }
}

dependencies {
    api(project(":cacio-shared-17"))
    implementation("com.jidesoft:jide-oss:3.6.18")
    implementation("org.assertj:assertj-swing-junit:3.17.1")
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.addAll(
        listOf(
            "--add-exports=java.desktop/sun.awt=ALL-UNNAMED",
            "--add-exports=java.desktop/sun.awt.image=ALL-UNNAMED",
            "--add-exports=java.desktop/sun.font=ALL-UNNAMED",
            "--add-exports=java.desktop/sun.java2d=ALL-UNNAMED",
            "--add-exports=java.desktop/sun.swing=ALL-UNNAMED"
        )
    )
}
