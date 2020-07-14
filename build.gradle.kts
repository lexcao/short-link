plugins {
    kotlin("multiplatform") version "1.3.72"
}

group = "lexcao.github.io"
version = "1.0-SNAPSHOT"

val ktorVersion: String by project
val kotlinHtmlVersion: String by project

repositories {
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/kotlin/ktor")
    mavenLocal()
}

kotlin {

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        tasks.getByName<Jar>("jvmJar") {
            archiveVersion.set("")
            manifest {
                attributes["Main-Class"] = "io.ktor.server.netty.EngineMain"
            }
            doFirst {
                from(configurations["jvmCompileClasspath"].map { zipTree(it.absolutePath) })
            }
        }
    }

    js { browser() }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation("io.ktor:ktor-server-netty:$ktorVersion")
                implementation("io.ktor:ktor-server-core:$ktorVersion")
                implementation("io.ktor:ktor-html-builder:$ktorVersion")
                implementation("io.ktor:ktor-jackson:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:$kotlinHtmlVersion")
                implementation("io.github.microutils:kotlin-logging:1.7.9")
                implementation("ch.qos.logback:logback-classic:1.2.3")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
                implementation("org.jetbrains.kotlinx:kotlinx-html-js:$kotlinHtmlVersion")
            }
        }
    }
}

/*tasks.getByName<KotlinWebpack>("jsBrowserProductionWebpack") {
    outputFileName = "output.js"
}*/
/*tasks.getByName<Jar>("jvmJar") {

    *//*dependsOn(tasks.getByName("jsBrowserProductionWebpack"))
    val jsBrowserProductionWebpack =
        tasks.getByName<org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack>("jsBrowserProductionWebpack")
    from(File(jsBrowserProductionWebpack.destinationDirectory, jsBrowserProductionWebpack.outputFileName))
*//*
}*/
