import com.android.build.gradle.LibraryExtension

plugins {
    id("io.github.gradle-nexus.publish-plugin") version "1.3.0"
}

buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.3.2")
    }
}

allprojects {
    group = "org.jellyfin.media3"
    version = createVersion()

    repositories {
        mavenCentral()
        google()
    }

    // Set minSdk to 21 and force a specific NDK version
    afterEvaluate {
        val android = extensions.findByType(LibraryExtension::class.java)
        if (android != null) {
            android.defaultConfig.minSdk = 21
            android.ndkVersion = "26.1.10909125"
        }
    }
}

// Add Sonatype publishing repository
nexusPublishing {
    repositories.sonatype {
        nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
        snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))

        username.set(getProperty("ossrh.username"))
        password.set(getProperty("ossrh.password"))
    }

    useStaging.set(project.provider { project.version.toString() != SNAPSHOT_VERSION })
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
}

tasks.create<Delete>("clean") {
    delete(rootProject.buildDir)
}
