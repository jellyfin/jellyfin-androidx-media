// Top-level build file where you can add configuration options common to all sub-projects/modules.

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

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
}

tasks.create<Delete>("clean") {
    delete(rootProject.buildDir)
}
