// pro-grade Java SecurityManager sandbox.
// Vendored from https://github.com/pro-grade/pro-grade (Apache-2.0).
// Produces pro-grade.jar, the artifact name the launcher consumes.

plugins { `java-library` }

base { archivesName.set("pro-grade") }

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(8)) }
}
