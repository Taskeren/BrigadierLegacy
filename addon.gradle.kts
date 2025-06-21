

val sourceSets = the<SourceSetContainer>()

/*
val patchedMc by sourceSets
val main by sourceSets

val functionalTestSet by sourceSets.creating {
    java {
        srcDir("src/functionalTest/java")
        // compileClasspath += sourceSets.patchedMc.output + sourceSets.main.output
        compileClasspath += patchedMc.output + main.output
    }
}

configurations.named(functionalTestSet.compileClasspathConfigurationName).configure {
    extendsFrom(configurations.named("compileClasspath").get())
}
configurations.named(functionalTestSet.runtimeClasspathConfigurationName).configure {
    extendsFrom(configurations.named("runtimeClasspath").get())
}
configurations.named(functionalTestSet.annotationProcessorConfigurationName).configure {
    extendsFrom(configurations.named("annotationProcessor").get())
}


tasks.register(functionalTestSet.jarTaskName, Jar::class.java) {
    from(functionalTestSet.output)
    archiveClassifier.set("functionalTests")
    // we don't care about the version number here, keep it stable to avoid polluting the tmp directory
    archiveVersion.set("1.0")
    destinationDirectory.set(File(layout.buildDirectory.asFile.get(), "tmp"))
}

*/
/*tasks.register("downgradeFunctionalTest", "xyz.wagyourtail.jvmdg.gradle.task.DowngradeJar") {
    this.property("inputFile", tasks.named(functionalTestSet.jarTaskName, Jar).get().archiveFile)
    this.property("downgradeTo", JavaVersion.VERSION_1_8)
    this.property("archiveClassifier", "downgraded-functionalTest")
}*//*


tasks.create("downgradeFunctionalTest", xyz.wagyourtail.jvmdg.gradle.task.DowngradeJar::class.java) {
    inputFile = tasks.named(functionalTestSet.jarTaskName, Jar).get().archiveFile
    downgradeTo = JavaVersion.VERSION_1_8
    archiveClassifier = "downgraded-functionalTest"
}

tasks.named("assemble").configure {
    dependsOn("downgradeFunctionalTest")
}

// Run tests in the default runServer/runClient configurations
tasks.named("runServer", JavaExec::class.java).configure {
    dependsOn(functionalTestSet.jarTaskName)
    classpath(configurations.named(functionalTestSet.runtimeClasspathConfigurationName), tasks.named("downgradeFunctionalTest"))
}

tasks.named("runClient", JavaExec::class.java).configure {
    dependsOn(functionalTestSet.jarTaskName)
    classpath(configurations.named(functionalTestSet.runtimeClasspathConfigurationName), tasks.named("downgradeFunctionalTest"))
}
*/
