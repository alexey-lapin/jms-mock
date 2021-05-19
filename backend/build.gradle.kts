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

    spotless {
        val headerFile = rootProject.project(":backend").file("src/spotless/mit-license.java")

        java {
            licenseHeaderFile(headerFile, "(package|import|open|module) ")
            removeUnusedImports()
            trimTrailingWhitespace()
            endWithNewline()
        }
    }

    tasks {
        withType<Test> {
            useJUnitPlatform()
        }
    }
}