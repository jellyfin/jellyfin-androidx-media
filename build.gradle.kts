plugins {
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.1")
    }
}

allprojects {
    group = "org.jellyfin.exoplayer"
    version = createVersion()

    repositories {
        mavenCentral()
        google()

        // Necessary for some ExoPlayer dependencies
        @Suppress("DEPRECATION", "JcenterRepositoryObsolete")
        jcenter {
            content {
                includeVersion("com.google.vr", "sdk-base", "1.190.0")
            }
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
