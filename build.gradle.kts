import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import io.gitlab.arturbosch.detekt.Detekt
import com.google.protobuf.gradle.*
import org.jmailen.gradle.kotlinter.tasks.LintTask

group = "com.dasrida.template.springboot"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

// gRPC dependencies
val grpcVersion = "1.42.0"
val grpcKotlinVersion = "1.2.0"
val protobufVersion = "3.19.1"

// gRPC directory configurations
val grpcGeneratedDir = ".grpc_generated"
val grpcOutputSubDir = "grpc"
val grpcKtOutputSubDir = "grpckt"

plugins {
    id("org.springframework.boot") version "2.5.6"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.jmailen.kotlinter") version "3.6.0"
    id("io.gitlab.arturbosch.detekt") version "1.19.0-RC1"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.5.0"
    id("org.jetbrains.dokka") version "1.4.32"
    id("com.google.protobuf") version "0.8.17"
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.spring") version "1.5.31"
    kotlin("plugin.jpa") version "1.5.31"
    kotlin("plugin.serialization") version "1.5.31"
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot/Reactor with Kotlin
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    implementation("org.springframework.boot:spring-boot-starter-security")
    testImplementation("org.springframework.security:spring-security-test")

    // kotlinx.serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")

    // gRPC with Kotlin
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-netty:$grpcVersion")
    implementation("com.google.protobuf:protobuf-kotlin:$protobufVersion")

    // gRPC
    implementation("io.grpc:grpc-stub:1.42.0")
}

sourceSets {
    main {
        // including gRPC generated source files(generated by generateProto task) to main sourceSet
        java {
            srcDir("$grpcGeneratedDir/main/$grpcOutputSubDir")
            srcDir("$grpcGeneratedDir/main/java")
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }

    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion:osx-x86_64"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:$grpcKotlinVersion:jdk7@jar"
        }
    }

    generateProtoTasks {
        ofSourceSet("main").forEach { task ->
            task.builtins {
                remove("java")
            }
            task.plugins {
                id("grpc") {
                    outputSubDir = grpcOutputSubDir
                }
                id ("grpckt") {
                    outputSubDir = grpcKtOutputSubDir
                }
                id ("java") {
                    outputSubDir = grpcOutputSubDir
                }
            }
        }
    }

    generatedFilesBaseDir = "$projectDir/$grpcGeneratedDir"
}

allOpen {
    annotation("com.dasrida.template.springboot.kotlin.AllOpen")
}

kotlinter {
    ignoreFailures = false
    indentSize = 4
    reporters = arrayOf("checkstyle", "plain")
    experimentalRules = false
    disabledRules = arrayOf("no-wildcard-imports")
}

detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    config = files("$projectDir/config/detekt.yml") // point to your custom config defining rules to run, overwriting default behavior
    baseline = file("$projectDir/config/baseline.xml") // a way of suppressing issues before introducing detekt
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// Kotlin DSL
tasks.withType<Detekt> {
    // Target version of the generated JVM bytecode. It is used for type resolution.
    jvmTarget = "11"
}

tasks.clean {
    delete("$projectDir/$grpcGeneratedDir")
}

tasks {
    "lintKotlinMain"(LintTask::class) {
        exclude("$projectDir/$grpcGeneratedDir/**")
    }
}