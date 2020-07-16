import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

plugins {

    val kotlinVersion = "1.3.72"

    application
    kotlin("multiplatform") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
}

group = "lexcao.github.io"
version = "1.0-SNAPSHOT"

val ktorVersion: String by project
val kotlinHtmlVersion: String by project
val serializationVersion: String by project

repositories {
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/kotlin/ktor")
    mavenLocal()
}

kotlin {

    jvm {
        withJava()
    }

    js {
        browser {
            // https://kotlinlang.org/docs/reference/javascript-dce.html#known-issue-dce-and-ktor
            dceTask {
                keep("ktor-ktor-io.\$\$importsForInline\$\$.ktor-ktor-io.io.ktor.utils.io")
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:0.20.0")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation("io.ktor:ktor-server-netty:$ktorVersion")
                implementation("io.ktor:ktor-server-core:$ktorVersion")
                implementation("io.ktor:ktor-serialization:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializationVersion")

                implementation("ch.qos.logback:logback-classic:1.2.3")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
                implementation("io.ktor:ktor-client-js:$ktorVersion")
                implementation("io.ktor:ktor-client-json-js:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization-js:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-html-js:$kotlinHtmlVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:$serializationVersion")

                // fix stuff
                implementation(npm("text-encoding"))
                implementation(npm("abort-controller"))
                implementation(npm("bufferutil"))
                implementation(npm("utf-8-validate"))
                implementation(npm("fs"))

                // react
                implementation("org.jetbrains:kotlin-react:16.13.1-pre.105-kotlin-1.3.72")
                implementation("org.jetbrains:kotlin-react-dom:16.13.1-pre.105-kotlin-1.3.72")
                implementation(npm("react", "16.13.1"))
                implementation(npm("react-dom", "16.13.1"))

                // kotlin styled
                implementation("org.jetbrains:kotlin-styled:1.0.0-pre.110-kotlin-1.3.72")
                implementation(npm("styled-components"))
                implementation(npm("inline-style-prefixer"))
            }
        }
    }
}

application {
    mainClassName = "io.github.lexcao.shortlink.ApplicationKt"
}

distributions {
    main {
        contents {
            from("$buildDir/libs") {
                rename("${rootProject.name}-jvm", rootProject.name)
                into("lib")
            }
        }
    }
}

tasks.getByName<Jar>("jvmJar") {
    val webpack = tasks.getByName<KotlinWebpack>(getWebpackTask())
    dependsOn(webpack)
    from(File(webpack.destinationDirectory, webpack.outputFileName!!))
}

fun getWebpackTask(): String {
    val production: String? by project
    return if (production?.toBoolean() == true) {
        System.err.println("Production is enabled.")
        "jsBrowserProductionWebpack"
    } else {
        "jsBrowserDevelopmentWebpack"
    }
}

// Alias "installDist" as "stage" for Heroku
tasks.create("stage") {
    dependsOn(tasks.getByName("installDist"))
}

tasks.getByName<JavaExec>("run") {
    // so that the JS artifacts generated by `jvmJar` can be found and served
    classpath(tasks.getByName<Jar>("jvmJar"))
}

