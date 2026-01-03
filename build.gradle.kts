plugins {
    id("java")
    id("com.diffplug.spotless") version "6.25.0"
    id("maven-publish")
}

group = "net.timelesspay"
version = "0.1.0-beta"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenCentral()
}

dependencies {
    // HTTP Client
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // JSON Processing
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.16.1")

    // Logging
    implementation("org.slf4j:slf4j-api:2.0.9")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Testing utilities
    testImplementation("org.mockito:mockito-core:5.11.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.11.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
}

tasks.test {
    useJUnitPlatform()
}

// Ensure check task runs spotlessCheck
tasks.check {
    dependsOn(tasks.named("spotlessCheck"))
}

spotless {
    java {
        target("src/**/*.java")
        googleJavaFormat("1.22.0")
        removeUnusedImports()
        trimTrailingWhitespace()
        endWithNewline()
    }

    kotlinGradle {
        target("*.gradle.kts")
        ktlint()
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            groupId = project.group.toString()
            artifactId = "sdk"
            version = project.version.toString()

            pom {
                name.set("TimelessPay SDK")
                description.set("Java SDK for TimelessPay Payment Gateway Service")
                url.set("https://github.com/your-org/timeless-sdk")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("timeless")
                        name.set("TimelessPay Team")
                    }
                }
            }
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/${System.getenv("GITHUB_REPOSITORY")}")
            credentials {
                username = System.getenv("USERNAME") ?: System.getenv("GITHUB_ACTOR")
                password = System.getenv("TOKEN") ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
