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
    implementation("org.springframework:spring-web:6.2.7")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")
    implementation("io.swagger.core.v3:swagger-annotations:2.2.31")
}

tasks.test { useJUnitPlatform() }

kotlin { jvmToolchain(23) }
