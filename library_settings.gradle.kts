val mediaRootDir = file("media")

val modulePrefix = ":androidx-media-"
gradle.extra["androidxMediaModulePrefix"] = modulePrefix

if (!gradle.extra.has("androidxMediaSettingsDir")) {
    gradle.extra["androidxMediaSettingsDir"] = mediaRootDir.getCanonicalPath()
}

include(modulePrefix + "lib-common")
project(modulePrefix + "lib-common").projectDir = File(mediaRootDir, "libraries/common")

include(modulePrefix + "lib-container")
project(modulePrefix + "lib-container").projectDir = File(mediaRootDir, "libraries/container")

include(modulePrefix + "lib-exoplayer")
project(modulePrefix + "lib-exoplayer").projectDir = File(mediaRootDir, "libraries/exoplayer")

include(modulePrefix + "lib-database")
project(modulePrefix + "lib-database").projectDir = File(mediaRootDir, "libraries/database")

include(modulePrefix + "lib-datasource")
project(modulePrefix + "lib-datasource").projectDir = File(mediaRootDir, "libraries/datasource")

include(modulePrefix + "lib-decoder")
project(modulePrefix + "lib-decoder").projectDir = File(mediaRootDir, "libraries/decoder")
include(modulePrefix + "lib-decoder-ffmpeg")
project(modulePrefix + "lib-decoder-ffmpeg").projectDir = File(mediaRootDir, "libraries/decoder_ffmpeg")

include(modulePrefix + "lib-extractor")
project(modulePrefix + "lib-extractor").projectDir = File(mediaRootDir, "libraries/extractor")

include(modulePrefix + "test-utils-robolectric")
project(modulePrefix + "test-utils-robolectric").projectDir = File(mediaRootDir, "libraries/test_utils_robolectric")
include(modulePrefix + "test-data")
project(modulePrefix + "test-data").projectDir = File(mediaRootDir, "libraries/test_data")
include(modulePrefix + "test-utils")
project(modulePrefix + "test-utils").projectDir = File(mediaRootDir, "libraries/test_utils")
