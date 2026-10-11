// JDK 8 DNS resolver configuration replacement (sun.net.dns.ResolverConfigurationImpl).
// Own rewrite of the opaque prebuilt ResConfHack.jar blob the app used to ship;
// no public upstream source exists for the blob itself - see UPSTREAM.md. The
// class is derived from the OpenJDK 8 implementation (GPL-2.0 with Classpath
// exception, see LICENSE) with the resolv.conf location redirected to the
// launcher-provided file.
// Produces ResConfHack.jar, the artifact the launcher consumes.

plugins { `java-library` }

base { archivesName.set("ResConfHack") }

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(8)) }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    // The module replaces the rt.jar class sun.net.dns.ResolverConfigurationImpl
    // and therefore must compile against the internal sun.net.dns API, which
    // javac hides behind the symbol file. Same approach as the root build
    // script uses for the cacio `-8` modules: fork the toolchain's javac 8
    // binary, because the in-process compiler path does not honor
    // -XDignore.symbol.file.
    options.compilerArgs.add("-XDignore.symbol.file=true")
    val toolchainService =
        project.extensions.getByType(JavaToolchainService::class.java)
    val javac8 = toolchainService.compilerFor {
        languageVersion.set(JavaLanguageVersion.of(8))
    }.map {
        it.metadata.installationPath.asFile
            .resolve("bin/javac").absolutePath
    }
    options.fork(mapOf("executable" to javac8.get()))
}
