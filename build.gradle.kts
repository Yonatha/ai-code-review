plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.17.2"
}

group = "com.yth.ai.codereview"
version = "1.2.1"

repositories {
    mavenCentral()
}
dependencies {
    implementation("io.github.openfeign:feign-gson:13.2.1")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2021.3")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf(/* Plugin Dependencies */))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }

    patchPluginXml {
        sinceBuild.set("0")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
