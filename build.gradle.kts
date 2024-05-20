import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.dsl.SpringBootExtension

plugins {
  id("org.springframework.boot") version "3.2.5"
  id("io.spring.dependency-management") version "1.1.5"
  kotlin("jvm") version "1.9.23"
  kotlin("plugin.spring") version "1.9.24"
  kotlin("plugin.jpa") version "1.9.24"
  id("org.sonarqube") version "5.0.0.4638"
  id("jacoco")
}

group = "io.github.srtigers98.birthdaydiscordbot"
version = "1.0.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
  mavenCentral()
}

val kordVersion = "0.13.1"
val mockitoVersion = "5.3.1"

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

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "17"
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
