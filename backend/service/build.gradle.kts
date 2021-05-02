import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    java
    id("com.github.ben-manes.versions")
    id("io.spring.dependency-management")
    id("org.springframework.boot")
}

dependencies {
    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
//    implementation("org.springframework.boot:spring-boot-starter-webflux")

    implementation("com.ibm.mq:mq-jms-spring-boot-starter:2.4.5")
    implementation("io.projectreactor:reactor-core:3.4.5")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.liquibase:liquibase-core:4.3.4")

    runtimeOnly("org.codehaus.groovy:groovy:3.0.8")

//    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
//    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

}

tasks {

    bootJar {
        archiveFileName.set("${rootProject.name}.${archiveExtension.get()}")
    }

}
