plugins {
    id("java")
    id("application") // Add the application plugin for easier execution
}

group = "org.course"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2") // Jackson Databind
    implementation("com.fasterxml.jackson.core:jackson-core:2.18.2") // Jackson Core
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.18.2") // Jackson Annotations

    testImplementation(platform("org.junit:junit-bom:5.9.1")) // JUnit BOM for dependency management
    testImplementation("org.junit.jupiter:junit-jupiter") // JUnit Jupiter for testing
    testImplementation("org.mockito:mockito-core:5.15.2")
}

application {
    mainClass.set("org.course.Main") // Set the main class for the application plugin
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "org.course.Main" // Set the main class in the JAR manifest
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.test {
    useJUnitPlatform() // Use JUnit 5 for testing
}