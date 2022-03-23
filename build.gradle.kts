import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.dsl.SpringBootExtension

plugins {
  id("org.springframework.boot") version "2.6.3"
  id("io.spring.dependency-management") version "1.0.11.RELEASE"
  kotlin("jvm") version "1.6.10"
  kotlin("plugin.spring") version "1.6.10"
  kotlin("plugin.jpa") version "1.6.10"
  id("org.sonarqube") version "3.3"
  id("jacoco")
}

group = "io.github.srtigers98.birthdaydiscordbot"
version = "1.0.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.postgresql:postgresql")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("dev.kord:kord-core:0.8.0-M10")

  developmentOnly("org.springframework.boot:spring-boot-devtools")

  runtimeOnly("com.h2database:h2")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
}

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

tasks.create<Test>("itest") {
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
