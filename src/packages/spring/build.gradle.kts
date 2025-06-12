plugins {
    kotlin("plugin.spring") version "1.9.25"
    id("org.jetbrains.kotlin.jvm")
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"

    `java-library`
}

group = "nl.klrnbk.daan.appiecal.packages"

version = "1.0.0"

repositories { mavenCentral() }

dependencies {
    testImplementation(kotlin("test"))
    api(project(":packages:exceptions"))
    api("org.jetbrains.kotlin:kotlin-reflect:2.1.21")
    api("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    api("org.springframework.boot:spring-boot-starter-web:3.5.0")
}

tasks.test { useJUnitPlatform() }

kotlin { jvmToolchain(23) }
