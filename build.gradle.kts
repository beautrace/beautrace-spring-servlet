import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm") version "1.7.0"
    id("org.springframework.boot") version "2.7.0"
    java
    `java-library`
    `maven-publish`
    signing
}
apply(plugin = "io.spring.dependency-management")

group = "com.beautrace"
version = "1.0.2"
java.sourceCompatibility = JavaVersion.VERSION_1_8

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

            pom {
                name.set("beautrace-spring-servlet")
                description.set("A library to trace execution of methods per each Spring Servlet's HTTP request.")
                url.set("https://github.com/beautrace/beautrace-spring-servlet")
                licenses {
                    license {
                        name.set("The MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        name.set("Dima Belousov")
                        email.set("belousovdmn@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com:beautrace/beautrace-spring-servlet.git")
                    developerConnection.set("scm:git:ssh://github.com:beautrace/beautrace-spring-servlet.git")
                    url.set("https://github.com/beautrace/beautrace-spring-servlet/tree/main")
                }
            }
        }
    }
    repositories {
        maven {
            name = "OSSRH"
            setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = System.getenv("OSSRH_USER") ?: return@credentials
                password = System.getenv("OSSRH_PASSWORD") ?: return@credentials
            }
        }
    }
}

signing {
    val key = System.getenv("SIGNING_KEY") ?: return@signing
    val password = System.getenv("SIGNING_PASSWORD") ?: return@signing
    val publishing: PublishingExtension by project
    useInMemoryPgpKeys(key, password)
    sign(publishing.publications)
}

configure<JavaPluginExtension> {
    withJavadocJar()
    withSourcesJar()
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

tasks.jar {
    // Remove `plain` postfix from jar file name
    // Required for maven builds to work
    archiveClassifier.set("")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}