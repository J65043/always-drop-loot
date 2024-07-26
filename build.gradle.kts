import net.fabricmc.loom.task.RemapJarTask

val minecraftVersion: String by project
val parchmentVersion: String by project
val loaderVersion: String by project
val modId: String by project
val mavenGroup: String by project
val fabricVersion: String by project
val modMenuVersion: String by project

plugins {
  java
  `maven-publish`
  id("fabric-loom") version "1.7-SNAPSHOT"
  id("com.diffplug.spotless") version "5.17.0"
  id("org.jetbrains.changelog") version "1.3.1"
  id("com.github.jmongard.git-semver-plugin") version "0.4.2"
}

buildscript {
  dependencies {
    classpath("org.openjdk.nashorn:nashorn-core:15.3")
  }
}

group = mavenGroup
version = "1.0.0" // Change this as needed

sourceSets {
  create("testmod") {
    compileClasspath += main.get().compileClasspath
    runtimeClasspath += main.get().runtimeClasspath
  }
}

loom {
  runs {
    create("testmodClient") {
      client()
      name("Testmod Client")
      ideConfigGenerated(true)
      source(sourceSets.getByName("testmod"))
    }
    create("gametest") {
      server()
      name("Game Test")
      ideConfigGenerated(true)
      source(sourceSets.getByName("testmod"))
      vmArg("-Dfabric-api.gametest")
      runDir("build/gametest")
    }
  }
}

tasks.getByName("test").dependsOn("runGametest")

repositories {
  mavenCentral()
  // Loom adds the essential maven repositories automatically.

  maven(url = "https://maven.terraformersmc.com/")
  maven(url = "https://maven.parchmentmc.net/")

}

dependencies {
  minecraft("com.mojang:minecraft:$minecraftVersion")
  mappings(
    loom.layered {
      officialMojangMappings()
      parchment("org.parchmentmc.data:parchment-$minecraftVersion:$parchmentVersion@zip")
    })

  add("testmodImplementation", sourceSets.main.get().output)

  // remember to update the dependency list in fabric.mod.json
  // and the expansions in "processResources" below
  modImplementation("net.fabricmc:fabric-loader:$loaderVersion")
  modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricVersion")
  modRuntimeOnly("com.terraformersmc:modmenu:$modMenuVersion")
}

base { archivesName.set(modId) }

java {
  sourceCompatibility = JavaVersion.VERSION_21
  targetCompatibility = JavaVersion.VERSION_21
  withSourcesJar()
}

tasks.getByName<ProcessResources>("processResources") {
  filteringCharset = "UTF-8"
  filesMatching("fabric.mod.json") {
    expand(project.properties + mapOf("modVersion" to "${project.version}"))
  }
}

val javaCompile = tasks.withType<JavaCompile> {
  options.encoding = "UTF-8"
}

val jar = tasks.getByName<Jar>("jar") {
  from("LICENSE")
}

val remapJar = tasks.getByName<RemapJarTask>("remapJar")

spotless {
  java {
    importOrder()
    prettier(mapOf("prettier" to "2.4.1", "prettier-plugin-java" to "1.5.0"))
  }
  kotlinGradle { ktfmt() }
  freshmark {
    target("**/*.md")
    propertiesFile("gradle.properties")
    prettier()
  }
  format("misc") {
    target("**/*.json", "**/*.yml")
    prettier()
  }
}

changelog {
  version.set("1.0.0")
  groups.set(listOf("Changes"))
  unreleasedTerm.set("Current")
  header.set(provider { version.get() })
}

tasks.getByName("patchChangelog").finalizedBy(tasks.getByName("spotlessFreshmarkApply"))

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      artifact("remapJar") { builtBy(tasks.getByName("remapJar")) }
      artifact("sourcesJar") { builtBy(tasks.getByName("remapSourcesJar")) }
    }
  }
  repositories {
    // Add repositories to publish to here.
  }
}
