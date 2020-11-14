plugins {
    id("org.jetbrains.intellij") version "0.6.3"
    kotlin("jvm") version "1.4.10"
}

group = "com.mobius.idea_plugins"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    /**
     * We targeting on the local version of Android Studio for getting proper API of plugins.
     */
//    version = "2020.1.4"
    localPath = "/Applications/Android Studio 4.0.app/Contents"
}
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
tasks.getByName<org.jetbrains.intellij.tasks.PatchPluginXmlTask>("patchPluginXml") {
    changeNotes("""
      Add change notes here.<br>
      <em>most HTML tags may be used</em>""")
}