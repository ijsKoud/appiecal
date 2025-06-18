plugins {
    kotlin("plugin.spring") version "1.9.25"
    id("org.jetbrains.kotlin.jvm")
    id("io.spring.dependency-management") version "1.1.7"

    `java-library`
}

group = "nl.klrnbk.daan.appiecal.packages"

version = "1.0.0"

repositories { mavenCentral() }

dependencies {
    testImplementation(kotlin("test"))
    api(project(":packages:common"))
    api("org.springframework.boot:spring-boot-starter-security:3.5.0")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")
    compileOnly("org.springdoc:springdoc-openapi-common:1.8.0")
    compileOnly("org.springframework:spring-web:6.2.7")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.19.0")
    implementation("com.auth0:java-jwt:4.4.0")
}

tasks.test { useJUnitPlatform() }

kotlin { jvmToolchain(23) }
