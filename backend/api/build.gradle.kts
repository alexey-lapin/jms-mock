plugins {
    java
    id("io.spring.dependency-management")
    id("org.springframework.boot")
}

dependencies {
    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")

    implementation("jakarta.validation:jakarta.validation-api")
    implementation("org.springframework:spring-web")
}

tasks {

    bootJar {
        enabled = false
    }

    jar {
        enabled = true
    }

}
