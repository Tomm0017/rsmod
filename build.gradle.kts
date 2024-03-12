import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm) apply true
    alias(libs.plugins.kotlin.serialization)
    idea
}



allprojects {
    apply(plugin = "idea")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    group = "gg.rsmod"
    version = "0.0.5"
    java {
        toolchain { languageVersion.set(JavaLanguageVersion.of(17)) }
    }
    kotlin{
        jvmToolchain { languageVersion.set(JavaLanguageVersion.of(17)) }
    }
    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://repo.maven.apache.org/maven2")
        maven("https://jitpack.io")
        maven("https://repo.runelite.net/")
    }

    val lib = rootProject.project.libs
    dependencies {
        implementation(lib.kotlin.logging)
        implementation(lib.logback.classic)
        implementation(lib.fastutil)
        implementation(lib.kotlin.stdlib.jdk8)
        implementation(lib.jackson.dataformat.yaml)
        implementation(lib.jackson.dataformat.toml)
        implementation(lib.jackson.databind)
        implementation(lib.json)
        implementation(lib.jbcrypt)
        implementation(lib.gson)
        implementation(lib.cache)
        implementation(lib.netty.all)
        implementation(lib.kotlinx.serialization.core)
        testImplementation(lib.junit)
        testImplementation(lib.kotlin.test.junit)
    }

    idea {
        module {
            inheritOutputDirs = false
            outputDir = file("${project.buildDir}/classes/kotlin/main")
            testOutputDir = file("${project.buildDir}/classes/kotlin/test")
        }
    }

    tasks.compileJava {
        sourceCompatibility = JavaVersion.VERSION_17.toString()
        targetCompatibility = JavaVersion.VERSION_17.toString()
    }

    tasks.withType<KotlinCompile>().all {
        kotlinOptions {
            languageVersion = "1.7"
            jvmTarget = "17"
            freeCompilerArgs = listOf(
                "-Xallow-any-scripts-in-source-roots" ,
            )
        }
    }
}

tasks.register<Zip>("packageServer") {
    archiveFileName.set("rsmod.zip")
    destinationDirectory.set(file("."))

    from(".") {
        include("gradlew", "gradlew.bat", "build.gradle", "settings.gradle", "LICENSE", "README.md")

        include("dev-settings.example.yml")
        rename("dev-settings.example.yml", "dev-settings.yml")

        include("first-launch-template")
        rename("first-launch-template", "first-launch")
    }

    from("gradle/") {
        into("gradle")
    }

    from("data/") {
        into("data")
        exclude("cache", "rsa", "saves", "xteas")
    }

    from("game/") {
        into("game")
        exclude("build", "out", "plugins", "src/main/java", "src/test/java")
    }

    from("game/plugins") {
        into("game/plugins")
        include("src/main/kotlin/gg/rsmod/plugins/api/**", "src/main/kotlin/gg/rsmod/plugins/content/osrs.kts", "src/main/kotlin/gg/rsmod/plugins/service/**")
    }

    from("net/") {
        into("net")
        exclude("build", "out", "src/main/java", "src/test/java")
    }

    from("tools/") {
        into("tools")
        exclude("build", "out", "src/main/java", "src/test/java")
    }

}

tasks.register<Zip>("packageLibs") {
    archiveFileName.set("rsmod-libs.zip")
    destinationDirectory.set(file("."))

    from("game/build/libs/") {
        rename("game-${project.version}.jar", "game.jar")
    }

    from("game/plugins/build/libs/") {
        rename("plugins-${project.version}.jar", "plugins.jar")
    }
}

tasks.named<Jar>("jar") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
