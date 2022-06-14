import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")

}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }
    sourceSets {
        val ktor = "2.0.2"
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("com.squareup.retrofit2:retrofit:2.9.0")
                implementation("com.squareup.retrofit2:converter-gson:2.9.0")
                implementation("io.ktor:ktor-client-core:$ktor")
                implementation("io.ktor:ktor-client-cio:2.0.2")
                implementation("io.ktor:ktor-client-content-negotiation:2.0.2")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.0.2")
                implementation("org.jetbrains.compose.material:material-icons-extended-desktop:1.0.0")
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Compose-Desktop-First"
            packageVersion = "1.0.0"
        }
    }
}
