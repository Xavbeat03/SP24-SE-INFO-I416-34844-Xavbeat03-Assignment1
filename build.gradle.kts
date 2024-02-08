val mainClassNameClient = "client.Client"
val mainClassNameServer = "server.Server"
val fileNameClient = "SP24-SE-INFO-I416-34844-Xavbeat03-Assignment1-Client"
val fileNameServer = "SP24-SE-INFO-I416-34844-Xavbeat03-Assignment1-Server"
val versionNumberClient = "0.0.1"
val versionNumberServer = "0.0.1"

val runClientArgs = "-Xms256M -Xmx512M -XX:ParallelGCThreads=2 -client -XX:CompileThreshold=1000".split(" ")
val runServerArgs = "-Xms1G -Xmx3G -XX:ParallelGCThreads=4 -XX:MaxPermSize=256m -XX:MaxMetaspaceSize=256m  -XX:+HeapDumpOnOutOfMemoryError -server -XX:CompileThreshold=10000 -Xlint:-deprecation".split(" ")
val compileArgs = listOf("-Xlint:-deprecation")


plugins {
    kotlin("jvm")
    `java-library`
    java
    jacoco
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<ProcessResources>{
    filteringCharset = "UTF-8"
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(compileArgs)
    options.release.set(17);
}



tasks {
    val client by creating(com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class) {
        archiveBaseName.set(fileNameClient)
        archiveVersion.set(versionNumberClient)
        archiveClassifier.set("client")
        manifest {
            attributes["Main-Class"] = mainClassNameClient
        }
        configurations = listOf(project.configurations.runtimeClasspath.get())
    }

    register("runClient", JavaExec::class) {
        group = "Execution"
        description = "Runs the client"
        mainClass = mainClassNameClient
        classpath = sourceSets["main"].runtimeClasspath
        jvmArgs(runClientArgs)
        dependsOn(client)
    }

    val server by creating(com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class) {
        archiveBaseName.set(fileNameServer)
        archiveVersion.set(versionNumberServer)
        archiveClassifier.set("server")
        manifest {
            attributes["Main-Class"] = mainClassNameServer
        }

        configurations = listOf(project.configurations.runtimeClasspath.get())
    }

    register("runServer", JavaExec::class) {
        group = "Execution"
        description = "Runs the server"
        mainClass = mainClassNameServer
        classpath = sourceSets["main"].runtimeClasspath
        jvmArgs(runServerArgs)
        dependsOn(server)
    }
}

tasks.build {
    dependsOn("server", "client")
}




