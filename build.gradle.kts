import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.0"
    kotlin("plugin.spring") version "1.5.0"
    kotlin("plugin.jpa") version "1.5.0"
    kotlin("plugin.serialization") version "1.5.0"
    id("org.jmailen.kotlinter") version "3.4.4"
    id("io.gitlab.arturbosch.detekt") version "1.17.1"
}

group = "com.dasrida.template.springboot"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_16

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

sourceSets {
    main {
        kotlin {
            
        }
    }
}

repositories {
    mavenCentral()
    jcenter {
        content {
            includeModule("org.jetbrains.kotlinx", "kotlinx-html-jvm")
        }
    }
    maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    // implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")
    // implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    // implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    // implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // https://github.com/Kotlin/kotlinx.serialization -> Dependency on the JSON library 참조
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    // implementation("org.springframework.kafka:spring-kafka")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    // testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.springframework.security:spring-security-test")
}

detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    config = files("$projectDir/config/detekt.yml") // point to your custom config defining rules to run, overwriting default behavior
    baseline = file("$projectDir/config/baseline.xml") // a way of suppressing issues before introducing detekt

    reports {
        html.enabled = true // observe findings in your browser with structure and code snippets
        xml.enabled = true // checkstyle like format mainly for integrations like Jenkins
        txt.enabled = true // similar to the console output, contains issue signature to manually edit baseline files
        sarif.enabled = true // standardized SARIF format (https://sarifweb.azurewebsites.net/) to support integrations with Github Code Scanning
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "16"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// Kotlin DSL
tasks.withType<io.gitlab.arturbosch.detekt.Detekt> {
    onlyIf { project.hasProperty("runDetekt") }
    // JDK 16은 아직 공식 지원 중은 아님
    // Target version of the generated JVM bytecode. It is used for type resolution.
    jvmTarget = "16"
}
