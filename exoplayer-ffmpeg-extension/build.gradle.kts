plugins {
    `maven-publish`
}

val exoplayerProject = project(":exoplayer-extension-ffmpeg")

group = "org.jellyfin.exoplayer"
if (exoplayerProject.ext.has("releaseVersion")) {
    version = exoplayerProject.ext.get("releaseVersion")!!
}

afterEvaluate {
    publishing.publications.create<MavenPublication>("default") {
        // Repackage release artifacts of extension
        from(exoplayerProject.components["release"])

        pom {
            name.set("$groupId:$artifactId")
            description.set("Jellyfin Android FFmpeg Extension")
            url.set("https://github.com/jellyfin/jellyfin-exoplayer-ffmpeg-extension")

            scm {
                connection.set("scm:git:git://github.com/jellyfin/jellyfin-exoplayer-ffmpeg-extension.git")
                developerConnection.set("scm:git:ssh://github.com:jellyfin/jellyfin-exoplayer-ffmpeg-extension.git")
                url.set("https://github.com/jellyfin/jellyfin-exoplayer-ffmpeg-extension/tree/master")
            }

            licenses {
                license {
                    name.set("GNU Lesser General Public License v3.0")
                    url.set("https://www.gnu.org/licenses/lgpl-3.0.txt")
                }
            }

            developers {
                developer {
                    id.set("Max Rumpf")
                    name.set("Maxr1998")
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
}
