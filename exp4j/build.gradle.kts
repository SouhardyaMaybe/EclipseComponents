// net.objecthunter:exp4j - tiny math expression evaluator.
// Vendored from https://github.com/fasseg/exp4j (Apache-2.0).
// Produces exp4j-0.4.9-SNAPSHOT.jar, the artifact name the launcher consumes.

plugins { `java-library` }

base { archivesName.set("exp4j-0.4.9-SNAPSHOT") }

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(8)) }
}
