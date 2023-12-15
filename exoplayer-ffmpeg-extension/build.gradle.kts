import com.android.build.gradle.LibraryExtension

plugins {
    `maven-publish`
    signing
}

val exoplayerProject = project(":exoplayer-extension-ffmpeg")
val android = exoplayerProject.extensions.findByType(LibraryExtension::class.java)
    ?: error("Could not find android extension")

android.namespace = "com.google.android.exoplayer2.ext.ffmpeg"

val generateJavadoc by exoplayerProject.tasks.getting(Javadoc::class)
generateJavadoc.isFailOnError = false

val javadocJar by tasks.creating(Jar::class) {
    archiveClassifier.set("javadoc")
    from(generateJavadoc)
    dependsOn(generateJavadoc)
}

// Package sources from ExoPlayer FFmpeg extension project
val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    val main = android.sourceSets.getByName("main")
    from(main.java.srcDirs)
    @Suppress("deprecation")
    from(main.jni.srcDirs) {
        exclude("**/ffmpeg/")
    }
}

afterEvaluate {
    // Specify release artifacts and apply POM
    publishing.publications.create<MavenPublication>("default") {
        // Repackage release artifacts of extension
        from(exoplayerProject.components["release"])

        // Add JavaDocs and sources
        artifact(javadocJar)
        artifact(sourcesJar)

        pom {
            name.set("Jellyfin ExoPlayer libraries - $artifactId")
            description.set("ExoPlayer FFmpeg extension used in the Jellyfin project")
            url.set("https://github.com/jellyfin/jellyfin-exoplayer-ffmpeg-extension")

            scm {
                connection.set("scm:git:git://github.com/jellyfin/jellyfin-exoplayer-ffmpeg-extension.git")
                developerConnection.set("scm:git:ssh://github.com:jellyfin/jellyfin-exoplayer-ffmpeg-extension.git")
                url.set("https://github.com/jellyfin/jellyfin-exoplayer-ffmpeg-extension/tree/master")
            }

            licenses {
                license {
                    name.set("GNU General Public License v3.0")
                    url.set("https://www.gnu.org/licenses/gpl-3.0.txt")
                }
            }

            developers {
                developer {
                    id.set("Maxr1998")
                    name.set("Max Rumpf")
                    url.set("https://github.com/Maxr1998")
                    organization.set("Jellyfin")
                    organizationUrl.set("https://jellyfin.org")
                }

                developer {
                    id.set("nielsvanvelzen")
                    name.set("Niels van Velzen")
                    url.set("https://github.com/nielsvanvelzen")
                    organization.set("Jellyfin")
                    organizationUrl.set("https://jellyfin.org")
                }
            }
        }
    }

    // Add signing config
    configure<SigningExtension> {
        val signingKey = getProperty("signing.key")
        val signingPassword = getProperty("signing.password") ?: ""

        if (signingKey != null) {
            useInMemoryPgpKeys(signingKey, signingPassword)
            val publishing: PublishingExtension by project
            sign(publishing.publications)
        }
    }
}
