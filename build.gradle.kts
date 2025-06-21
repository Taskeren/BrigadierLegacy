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

jvmdg {
    downgradeTo = JavaVersion.VERSION_1_8
}

tasks.jar {
    archiveClassifier = "raw"
}

tasks.assemble {
    // make sure to generate the downgraded jars when assembling (building)
    dependsOn(tasks.shadeDowngradedApi)
}

tasks.runClient.configure {
    classpath = classpath - layout.files(tasks.jar) + tasks.downgradeJar.get().outputs.files
}
