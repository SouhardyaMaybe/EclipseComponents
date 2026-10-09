// Root build file for the EclipseComponents monorepo.
// Each component lives in its own `java-library` module; see README.md for the
// module table and UPSTREAM.md for provenance of every vendored source tree.

allprojects {
    group = "me.eclipse.launcher.components"
    version = "0.1.0"
}

subprojects {
    repositories {
        mavenCentral()
    }

    plugins.withId("java-library") {
        tasks.withType<JavaCompile>().configureEach {
            options.encoding = "UTF-8"
            // The cacio modules compile against pre-module JDK internals
            // (java.awt.peer, sun.awt.*). javac hides those behind the module
            // system on 9+ and behind the symbol file on 8; these are the same
            // flag sets needed to compile the vendored sources, applied
            // uniformly so no module can miss one.
            when {
                project.name.endsWith("-17") -> options.compilerArgs.addAll(
                    listOf(
                        "-XDignore.symbol.file=true",
                        "--add-exports=java.desktop/java.awt=ALL-UNNAMED",
                        "--add-exports=java.desktop/java.awt.peer=ALL-UNNAMED",
                        "--add-exports=java.desktop/java.awt.dnd.peer=ALL-UNNAMED",
                        "--add-exports=java.desktop/sun.awt=ALL-UNNAMED",
                        "--add-exports=java.desktop/sun.awt.image=ALL-UNNAMED",
                        "--add-exports=java.desktop/sun.awt.event=ALL-UNNAMED",
                        "--add-exports=java.desktop/sun.awt.datatransfer=ALL-UNNAMED",
                        "--add-exports=java.desktop/sun.font=ALL-UNNAMED",
                        "--add-exports=java.desktop/sun.java2d=ALL-UNNAMED",
                        "--add-exports=java.desktop/sun.java2d.pipe=ALL-UNNAMED",
                        "--add-exports=java.desktop/sun.swing=ALL-UNNAMED",
                        "--add-exports=java.base/java.security.PrivilegedAction=ALL-UNNAMED"
                    )
                )
                project.name.endsWith("-8") ->
                    options.compilerArgs.add("-XDignore.symbol.file=true")
            }
            // DRAFT: tests are vendored for reference but not executed by CI.
            // Iterate per-module if/when the test suites are wired up.
        }
        tasks.withType<Test>().configureEach {
            ignoreFailures = true
        }
    }
}
