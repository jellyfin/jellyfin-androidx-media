// See https://github.com/google/ExoPlayer/issues/6339
if (gradle is ExtensionAware) {
    val extension = gradle as ExtensionAware
    extension.extra["exoplayerRoot"] = file("ExoPlayer").absolutePath
    extension.extra["exoplayerModulePrefix"] = "exoplayer-"
    apply(from = File(extension.extra["exoplayerRoot"].toString(), "core_settings.gradle"))
}

include(":jellyfin-exoplayer-ffmpeg-extension")
