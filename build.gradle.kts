val mainClassNameClient = "client.Client"
val mainClassNameServer = "server.Server"
val fileNameClient = "SP24-SE-INFO-I416-34844-Xavbeat03-Assignment1-Client"
val fileNameServer = "SP24-SE-INFO-I416-34844-Xavbeat03-Assignment1-Server"
val versionNumberClient = "0.0.1"
val versionNumberServer = "0.0.1"

val runClientArgs = "-Xms256M -Xmx512M -XX:ParallelGCThreads=2 -client -XX:CompileThreshold=1000".split(" ")
val runServerArgs = "-Xms1G -Xmx3G -XX:ParallelGCThreads=4 -XX:MaxMetaspaceSize=512M  -XX:+HeapDumpOnOutOfMemoryError -server -XX:CompileThreshold=10000 -Xlint:-deprecation".split(" ")
val compileArgs = listOf("-Werror")


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

// Java version 17
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<ProcessResources>{
    filteringCharset = "UTF-8"
}

sourceSets {
    getByName("main") {
        java {
            srcDir("src/main/java")
        }
        kotlin {
            srcDir("src/main/kotlin")
        }
    }
}

// Compile arguments for the JVM
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(compileArgs)
    options.release.set(17);
}

//Gradle tasks
tasks {
    // Builds the Client jar
    val client by creating(com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class) {
        from(sourceSets["main"].output){
            exclude("server/**")
        }
        archiveBaseName.set(fileNameClient)
        archiveVersion.set(versionNumberClient)
        archiveClassifier.set("")
        manifest {
            attributes["Main-Class"] = mainClassNameClient
        }
        configurations = listOf(project.configurations.runtimeClasspath.get())
    }

    // Should build the client jar and run it from the cli
    register("runClient") {
        dependsOn("client")
        group = "Execution"
        description = "Runs the client program"
        doLast {
            exec {
                commandLine("java", "-jar", "./build/libs/$fileNameClient-$versionNumberClient.jar", *runClientArgs.toTypedArray())
            }
        }
    }

    // Builds the Server jar
    val server by creating(com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class) {
        from(sourceSets["main"].output){
            exclude("client/**")
        }
        archiveBaseName.set(fileNameServer)
        archiveVersion.set(versionNumberServer)
        archiveClassifier.set("")
        manifest {
            attributes["Main-Class"] = mainClassNameServer
        }
        configurations = listOf(project.configurations.runtimeClasspath.get())
    }

    // Should build the Server jar and run it from the cli
    register("runServer") {
        dependsOn(server)
        group = "Execution"
        description = "Runs the server program"
        doLast {
            exec {
                commandLine("java", "-jar", "./build/libs/$fileNameServer-$versionNumberServer.jar", *runServerArgs.toTypedArray())
            }
        }
    }

    // Debugging task
    register("Debug") {
        doLast{
            println(sourceSets["main"].runtimeClasspath.asPath)
        }
    }

    // Disables ./gradlew build from producing its own jar
    named("jar"){
        enabled = false
    }


}

// reroutes ./gradlew build to the server and client tasks
tasks.build {
    dependsOn("server", "client")
}
