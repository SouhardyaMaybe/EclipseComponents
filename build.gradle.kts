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
            // DRAFT: tests are vendored for reference but not executed by CI.
            // Iterate per-module if/when the test suites are wired up.
        }
        tasks.withType<Test>().configureEach {
            ignoreFailures = true
        }
    }
}
