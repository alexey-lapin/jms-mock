plugins {
    java
    id("io.spring.dependency-management")
    id("org.springframework.boot")
}

dependencies {
    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")

    implementation(parent!!.project("api"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
//    implementation("org.springframework.boot:spring-boot-starter-webflux")

    implementation("com.github.jknack:handlebars:4.2.0")
    implementation("com.ibm.mq:mq-jms-spring-boot-starter:2.4.5")
    implementation("com.jayway.jsonpath:json-path")
    implementation("io.projectreactor:reactor-core:3.4.5")
    implementation("org.zalando:logbook-spring-boot-starter:2.6.2")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.codehaus.groovy:groovy:3.0.8")
    runtimeOnly("org.liquibase:liquibase-core:4.3.4")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test:3.4.5")
}

tasks {
    val npmTask = rootProject.project("frontend").tasks["npmRunBuild"]

    register<Copy>("copyFrontend") {
        dependsOn(npmTask)
        from(npmTask.outputs.files)
        destinationDir = file("$buildDir/resources/main/public")
    }

    bootJar {
        dependsOn("copyFrontend")
        archiveFileName.set("${rootProject.name}-${archiveVersion.get()}.${archiveExtension.get()}")
    }
}
