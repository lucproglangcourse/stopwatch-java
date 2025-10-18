plugins {
    java
    jacoco
    application
    id("com.diffplug.spotless") version "7.0.2"
}

group = "edu.luc.cs"
version = "0.3"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Apache Commons Collections
    implementation("org.apache.commons:commons-collections4:4.5.0")
    
    // JUnit Jupiter for testing
    testImplementation("org.junit.jupiter:junit-jupiter:5.14.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    
    // ArchUnit for architecture testing
    testImplementation("com.tngtech.archunit:archunit-junit5:1.3.0")
}

application {
    mainClass = "edu.luc.cs.stopwatch.Main"
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(listOf("-Xlint:all"))
    options.isDeprecation = true
}

tasks.test {
    useJUnitPlatform()
    
    // Enable assertions for tests
    jvmArgs("-ea")
    
    // Ensure JaCoCo report is generated after tests
    finalizedBy(tasks.jacocoTestReport)
}

jacoco {
    toolVersion = "0.8.14"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    
    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) {
                exclude(
                    "**/*Facade.class",
                    "**/common/*.class"
                )
            }
        })
    )
    
    reports {
        xml.required = true
        html.required = true
    }
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.jacocoTestReport)
    
    violationRules {
        rule {
            element = "BUNDLE"
            
            limit {
                counter = "INSTRUCTION"
                value = "COVEREDRATIO"
                minimum = "0.80".toBigDecimal()
            }
            
            limit {
                counter = "METHOD"
                value = "COVEREDRATIO"
                minimum = "1.00".toBigDecimal()
            }
            
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.90".toBigDecimal()
            }
            
            limit {
                counter = "CLASS"
                value = "COVEREDRATIO"
                minimum = "1.00".toBigDecimal()
            }
            
            limit {
                counter = "COMPLEXITY"
                value = "COVEREDRATIO"
                minimum = "1.00".toBigDecimal()
            }
        }
    }
    
    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) {
                exclude(
                    "**/*Facade.class",
                    "**/common/*.class"
                )
            }
        })
    )
}

tasks.check {
    dependsOn(tasks.jacocoTestCoverageVerification)
}

spotless {
    java {
        removeUnusedImports()
        trimTrailingWhitespace()
        endWithNewline()
    }
}

// Uncomment when Shadow plugin is enabled
/*
// Shadow plugin configuration for fat JAR (equivalent to maven-assembly-plugin)
tasks.shadowJar {
    archiveClassifier = "all"
    mergeServiceFiles()
}

// Make build create the fat JAR
tasks.build {
    dependsOn(tasks.shadowJar)
}
*/
