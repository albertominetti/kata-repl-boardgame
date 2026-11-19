plugins {
    id("java")
    application
}

group = "com.minetti.kata"
version = "1.0-SNAPSHOT"

application {
    mainClass = "com.minetti.kata.Main"
}

tasks.named<JavaExec>("run") {
    // Let the REPL read commands from the terminal (or a pipe).
    standardInput = System.`in`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.opencsv:opencsv:5.12.0")

    testImplementation(platform("org.junit:junit-bom:5.12.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
