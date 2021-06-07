import com.github.gradle.node.npm.task.NpmTask

plugins {
    id("com.github.node-gradle.node")
}

tasks {
    register<NpmTask>("buildDocs") {
        dependsOn("npmInstall")
        inputs.files("package.json", "package-lock.json", "vue.config.js", "babel.config.js", ".env.production")
        outputs.dir("dist")
        npmCommand.set(listOf("run"))
        args.set(listOf("build"))
    }

    named<Delete>("clean") {
        delete.add("dist")
    }
}