import xyz.wagyourtail.jvmdg.gradle.task.DowngradeJar

plugins {
    // used for functional test only now
    kotlin("jvm")

    id("xyz.wagyourtail.jvmdowngrader") version "1.3.3"
    id("com.gtnewhorizons.gtnhconvention")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

kotlin {
    // compilerOptions {
    //     jvmTarget = JvmTarget.JVM_1_8
    // }
}

jvmdg {
    downgradeTo = JavaVersion.VERSION_1_8
}

tasks.assemble {
    // make sure to generate the downgraded jars when assembling (building)
    dependsOn(tasks.shadeDowngradedApi)
}

val patchedMcSet by sourceSets.named("patchedMc")
val mainSet by sourceSets.named("main")
val functionalTest: SourceSet by sourceSets.creating {
    java {
        srcDir("src/functionalTest/java")
        // compileClasspath += sourceSets.patchedMc.output + sourceSets.main.output
        compileClasspath += patchedMcSet.output + mainSet.output
    }
}

configurations.named(functionalTest.compileClasspathConfigurationName).configure {
    extendsFrom(configurations.named("compileClasspath").get())
}
configurations.named(functionalTest.runtimeClasspathConfigurationName).configure {
    extendsFrom(configurations.named("runtimeClasspath").get())
}
configurations.named(functionalTest.annotationProcessorConfigurationName).configure {
    extendsFrom(configurations.named("annotationProcessor").get())
}


tasks.register(functionalTest.jarTaskName, Jar::class.java) {
    from(functionalTest.output)
    archiveClassifier = "functionalTests"
    // we don't care about the version number here, keep it stable to avoid polluting the tmp directory
    archiveVersion = "1.0"
    destinationDirectory = File(layout.buildDirectory.asFile.get(), "tmp")
}

tasks.register("downgradeFunctionalTest", DowngradeJar::class.java) {
    inputFile = tasks.named<Jar>(functionalTest.jarTaskName, Jar::class.java).get().archiveFile
    downgradeTo = JavaVersion.VERSION_1_8

    archiveVersion = "1.0"
    destinationDirectory = File(layout.buildDirectory.asFile.get(), "tmp")
    archiveClassifier = "downgraded-functionalTest"
}

tasks.named("assemble").configure {
    dependsOn("downgradeFunctionalTest")
}

// Run tests in the default runServer/runClient configurations
tasks.named("runServer", JavaExec::class.java).configure {
    dependsOn(functionalTest.jarTaskName)
    classpath(configurations.named(functionalTest.runtimeClasspathConfigurationName), tasks.named("downgradeFunctionalTest"))
}

tasks.named("runClient", JavaExec::class.java).configure {
    dependsOn(functionalTest.jarTaskName)
    classpath(configurations.named(functionalTest.runtimeClasspathConfigurationName), tasks.named("downgradeFunctionalTest"))
}

apply(from = "late_dependencies.gradle")
