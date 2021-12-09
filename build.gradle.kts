plugins {
    java
}

group = "ir.arg"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    implementation("org.jetbrains:annotations:23.0.0")
    implementation("org.json:json:20211205")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}