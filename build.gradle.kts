import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

val querydslGeneratedDir = "$buildDir/generated"
val querydslKtOutputSubDirFull = "$querydslGeneratedDir/source/kaptKotlin"

group = "com.dasrida.template.springboot"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

plugins {
    id("org.springframework.boot") version "2.7.2"
    id("io.spring.dependency-management") version "1.0.12.RELEASE"
    kotlin("jvm") version "1.7.10" // https://plugins.gradle.org/plugin/org.jetbrains.kotlin.jvm
    kotlin("plugin.spring") version "1.7.10" // https://plugins.gradle.org/plugin/org.jetbrains.kotlin.plugin.spring
    kotlin("plugin.jpa") version "1.7.10" // https://plugins.gradle.org/plugin/org.jetbrains.kotlin.plugin.jpa
    kotlin("plugin.serialization") version "1.7.10" // https://plugins.gradle.org/plugin/org.jetbrains.kotlin.plugin.serialization
    id("org.jmailen.kotlinter") version "3.11.1" // https://github.com/jeremymailen/kotlinter-gradle/releases
    id("io.gitlab.arturbosch.detekt") version "1.21.0" // https://github.com/detekt/detekt/releases
    id("org.jetbrains.dokka") version "1.7.10" // https://plugins.gradle.org/plugin/org.jetbrains.dokka
    id("org.jetbrains.kotlin.plugin.allopen") version "1.7.10" // https://plugins.gradle.org/plugin/org.jetbrains.kotlin.plugin.allopen
}

repositories {
    mavenCentral()
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

    // https://github.com/Kotlin/kotlinx.serialization -> Dependency on the JSON library 참조
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0-RC")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}

sourceSets {
    main {
        java {
            srcDir(querydslKtOutputSubDirFull)
        }
    }
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    config = files("$projectDir/config/detekt.yml") // point to your custom config defining rules to run, overwriting default behavior
    baseline = file("$projectDir/config/baseline.xml") // a way of suppressing issues before introducing detekt
}

kotlinter {
    ignoreFailures = false
    reporters = arrayOf("checkstyle", "plain")
    experimentalRules = false
    disabledRules = emptyArray()
}

tasks {
    withType<Detekt>().configureEach {
        reports {
            html.required.set(true) // observe findings in your browser with structure and code snippets
            xml.required.set(true) // checkstyle like format mainly for integrations like Jenkins
            txt.required.set(true) // similar to the console output, contains issue signature to manually edit baseline files
            sarif.required.set(true) // standardized SARIF format (https://sarifweb.azurewebsites.net/) to support integrations with Github Code Scanning
            md.required.set(true) // simple Markdown format
        }
        jvmTarget = "17"
    }
    withType<DetektCreateBaselineTask>().configureEach {
        jvmTarget = "17"
    }

    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }

    withType<org.jmailen.gradle.kotlinter.tasks.LintTask> {
        setSource(source - fileTree(querydslGeneratedDir))
    }
}
