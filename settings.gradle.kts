gradle.extra["androidxMediaModulePrefix"] = "androidx-media-"
val mediaRoot = file("media")
apply(from = File(mediaRoot, "core_settings.gradle"))

include(":media3-ffmpeg-decoder")
