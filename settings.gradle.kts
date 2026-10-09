pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

// Auto-provisions the Java toolchains requested by the individual modules
// (JDK 8 for the legacy components, JDK 17 for the cacio 1.19.1 set).
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "EclipseComponents"

include(
    ":exp4j",
    ":java-sandbox",
    ":installer-agent",
    ":optifine-renamer",
    ":cacio-shared-8",
    ":cacio-androidnw-8",
    ":cacio-shared-17",
    ":cacio-tta-17",
    ":cacio-agent-17",
    ":mio-lib-patcher"
)
