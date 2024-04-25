import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.api.LibraryVariant
import com.android.build.gradle.tasks.BundleAar
import groovy.namespace.QName
import groovy.util.Node

plugins {
    `maven-publish`
    signing
}

val decoderProject = project(":androidx-media-lib-decoder-ffmpeg")
val decoderReleaseVersion = checkNotNull(decoderProject.ext["releaseVersion"]?.toString()) {
    "Couldn't read release version from androidx.media3 project"
}
val androidExtension = decoderProject.extensions.findByType(LibraryExtension::class.java)
    ?: error("Could not find android extension")

@Suppress("deprecation") // libraryVariants exposes deprecated type
val releaseVariant: LibraryVariant = androidExtension.libraryVariants.first { variant ->
    variant.name == "release"
}

val bundleReleaseAar by decoderProject.tasks.getting(BundleAar::class)

val generateJavadoc by tasks.registering(Javadoc::class) {
    group = "publishing"
    description = "Generates Javadoc for release sources."

    val javaCompile = releaseVariant.javaCompileProvider.get()
    source = javaCompile.source
    classpath = javaCompile.classpath + files(androidExtension.bootClasspath)

    setDestinationDir(File(buildDir, "/docs/javadoc"))

    isFailOnError = false
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")

    from(generateJavadoc)
    dependsOn(generateJavadoc)
}

// Package sources from decoder project
val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")

    val main = androidExtension.sourceSets.getByName("main")
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
        artifact(bundleReleaseAar)

        // Add JavaDocs and sources
        artifact(javadocJar)
        artifact(sourcesJar)

        pom {
            name.set("Jellyfin AndroidX Media3 libraries - $artifactId")
            description.set("AndroidX Media3 FFmpeg decoder used in the Jellyfin project")
            url.set("https://github.com/jellyfin/jellyfin-androidx-media")

            scm {
                connection.set("scm:git:git://github.com/jellyfin/jellyfin-androidx-media.git")
                developerConnection.set("scm:git:ssh://github.com:jellyfin/jellyfin-androidx-media.git")
                url.set("https://github.com/jellyfin/jellyfin-androidx-media/tree/master")
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

            withXml {
                // Make implicit dependency on androidx.media3 modules explicit by including them in the POM
                asNode().getOrCreateNode("dependencies").apply {
                    appendDependency("androidx.media3", "media3-decoder", decoderReleaseVersion)
                    appendDependency("androidx.media3", "media3-exoplayer", decoderReleaseVersion)
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

fun Node.getOrCreateNode(name: String): Node {
    return getAt(QName(name)).firstOrNull() as Node? ?: appendNode(name)
}

fun Node.appendDependency(group: String, id: String, version: String) {
    appendNode("dependency").apply {
        appendNode("groupId", group)
        appendNode("artifactId", id)
        appendNode("version", version)
        appendNode("scope", "compile")
        appendNode("type", "aar")
    }
}
