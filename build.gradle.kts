import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm") version "1.7.0"
    id("org.springframework.boot") version "2.7.0"
    `java-library`
    `maven-publish`
}
apply(plugin = "io.spring.dependency-management")

group = "io.beautrace"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

publishing {
    publications {
        // https://docs.gradle.org/current/userguide/publishing_maven.html
        // To make maven-plugin work.
        // gradle -i publishToMavenLocal
        // It really feels like we shouldn't need to do this.
        // This is useless boilerplate just to make it go.
        // Whatever happened to "convention"!?!?
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    // Required for @ConfigurationProperties to work
    annotationProcessor("org.springframework.boot:spring-boot-autoconfigure-processor")

    testImplementation(kotlin("test"))
}

tasks.withType<BootJar> {
    enabled = false
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}