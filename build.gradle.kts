import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
import org.springframework.boot.gradle.dsl.SpringBootExtension

plugins {
  id("org.springframework.boot") version "3.5.5"
  id("io.spring.dependency-management") version "1.1.7"
  kotlin("jvm") version "2.2.10"
  kotlin("plugin.spring") version "2.2.10"
  kotlin("plugin.jpa") version "2.2.10"
  id("org.sonarqube") version "6.2.0.5505"
  id("jacoco")
}

group = "io.github.srtigers98.birthdaydiscordbot"
version = "1.0.0-SNAPSHOT"

java {
  sourceCompatibility = JavaVersion.VERSION_17
}

kotlin {
  compilerOptions {
    apiVersion.set(KotlinVersion.KOTLIN_2_0)
    jvmTarget.set(JvmTarget.JVM_17)
  }
}

repositories {
  mavenCentral()
}

val kordVersion = "0.15.0"
val mockitoVersion = "6.0.0"

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.postgresql:postgresql")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("dev.kord:kord-core:$kordVersion")

  developmentOnly("org.springframework.boot:spring-boot-devtools")

  runtimeOnly("com.h2database:h2")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.mockito.kotlin:mockito-kotlin:$mockitoVersion")
}

extra["kotlin-coroutines.version"] = "1.7.3"

sonarqube {
  properties {
    property("sonar.projectKey", "SRTigers98_birthday-discord-bot")
    property("sonar.organization", "srtigers98")
    property("sonar.host.url", "https://sonarcloud.io")
  }
}

tasks.named("compileKotlin", KotlinCompilationTask::class.java) {
  compilerOptions {
    freeCompilerArgs.add("-Xjsr305=strict")
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
  finalizedBy("jacocoTestReport")
}

tasks.test {
  filter {
    excludeTestsMatching("*IntegrationTest*")
  }
}

tasks.register<Test>("itest") {
  group = JavaBasePlugin.VERIFICATION_GROUP
  description = "Run integration tests"

  filter {
    includeTestsMatching("*IntegrationTest*")
  }
}

tasks.jacocoTestReport {
  reports {
    xml.required.set(true)
  }
}

tasks.bootJar {
  archiveFileName.set("${archiveBaseName.get()}.${archiveExtension.get()}")
}

configure<SpringBootExtension> {
  buildInfo()
}
