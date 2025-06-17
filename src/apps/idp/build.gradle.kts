plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "nl.klrnbk.daan.appiecal.apps"

version = "1.0.0"

repositories { mavenCentral() }

dependencies {
    testImplementation(kotlin("test"))
    implementation(project(":packages:spring"))
    implementation(project(":packages:security-idp"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.5.0")
    implementation("org.postgresql:postgresql:42.7.7")
}

tasks.test { useJUnitPlatform() }
tasks.register("prepareKotlinBuildScriptModel") {}
tasks.register("wrapper") {
    gradle.gradleVersion
}

kotlin { jvmToolchain(23) }
