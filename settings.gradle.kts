pluginManagement {
    plugins {
        id("com.diffplug.spotless") version "5.12.1"
        id("com.github.ben-manes.versions") version "0.38.0"
        id("com.github.node-gradle.node") version "3.0.1"
        id("io.spring.dependency-management") version "1.0.11.RELEASE"
        id("org.springframework.boot") version "2.4.5"
    }
}
rootProject.name = "jms-mock"
include(":frontend")
include(":backend")
include(":backend:api")
include(":backend:service")