plugins {
    alias(libs.plugins.shadow)
    application
    `maven-publish`
}
description = "OpenRune Game Server Launcher"
application {
    apply(plugin = "maven-publish")
    mainClass.set("gg.rsmod.game.Launcher")
}

val lib = rootProject.project.libs
dependencies {
    runtimeOnly(project(":game:plugins"))

    with(lib) {
        implementation(projects.util)
        implementation("dev.openrune:filestore:1.0.0")
        implementation(projects.net)
        implementation(kotlin.scripting)
        implementation(kotlin.script.runtime)
        implementation(kotlinx.coroutines)
        implementation(reflection)
        implementation(commons)
        implementation(progress.bar)
        implementation(classgraph)
        implementation(fastutil)
        implementation(bouncycastle)
        implementation(jackson.module.kotlin)
        implementation(jackson.dataformat.yaml)
        implementation(kotlin.csv)
        testImplementation(junit)
    }
}

sourceSets {
    getByName("main").kotlin.srcDirs("src/main/kotlin")
    getByName("main").resources.srcDirs("src/main/resources")
}

tasks.register("install") {
    description = "Install OpenRune"
    val cacheList = listOf(
        "/cache/main_file_cache.dat2",
        "/cache/main_file_cache.idx0",
        "/cache/main_file_cache.idx1",
        "/cache/main_file_cache.idx2",
        "/cache/main_file_cache.idx3",
        "/cache/main_file_cache.idx4",
        "/cache/main_file_cache.idx5",
        "/cache/main_file_cache.idx7",
        "/cache/main_file_cache.idx8",
        "/cache/main_file_cache.idx9",
        "/cache/main_file_cache.idx10",
        "/cache/main_file_cache.idx11",
        "/cache/main_file_cache.idx12",
        "/cache/main_file_cache.idx13",
        "/cache/main_file_cache.idx14",
        "/cache/main_file_cache.idx15",
        "/cache/main_file_cache.idx16",
        "/cache/main_file_cache.idx17",
        "/cache/main_file_cache.idx18",
        "/cache/main_file_cache.idx19",
        "/cache/main_file_cache.idx20",
        "/cache/main_file_cache.idx255",
        "xteas.json"
    )
    val missingFiles = cacheList.filter { fileName ->
        val file = File("${rootProject.projectDir}/data/$fileName")
        !file.exists()
    }
    if (missingFiles.isNotEmpty()) {
        javaexec {
            workingDir = rootProject.projectDir
            classpath = sourceSets["main"].runtimeClasspath
            mainClass.set("gg.rsmod.game.service.cache.CacheService")
        }
    }
    doLast {
        copy {
            into("${rootProject.projectDir}/")
            from("${rootProject.projectDir}/game.example.yml") {
                rename("game.example.yml", "game.yml")
            }
            from("${rootProject.projectDir}/dev-settings.example.yml") {
                rename("dev-settings.example.yml", "dev-settings.yml")
            }
            file("${rootProject.projectDir}/first-launch").createNewFile()
        }
        javaexec {
            workingDir = rootProject.projectDir
            classpath = sourceSets["main"].runtimeClasspath
            mainClass.set("gg.rsmod.game.service.rsa.RsaService")
            args = listOf("16", "1024", "./data/rsa/key.pem") // radix, bitcount, rsa pem file
        }
    }
}

tasks.register("firstRun") {
    if(!file("${rootProject.projectDir}/data/rsa/key.pem").exists()) {
        dependsOn("install")
    }
}

tasks.named("run") {
    dependsOn("firstRun")
}

tasks.named("install") {
    dependsOn("build")
}

tasks.withType<AbstractCopyTask>{
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

tasks.withType<ProcessResources> {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}
