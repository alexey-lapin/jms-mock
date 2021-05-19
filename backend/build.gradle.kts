plugins {
    id("com.github.ben-manes.versions")
}

subprojects {

    apply(plugin = "java")

    repositories {
        mavenCentral()
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks {
        withType<Test> {
            useJUnitPlatform()
        }
    }

}