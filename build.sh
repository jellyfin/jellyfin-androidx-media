#!/bin/bash

# Ensure NDK is available
export ANDROID_NDK_PATH=$ANDROID_HOME/ndk/21.4.7075529

[[ -z "$ANDROID_NDK_PATH" ]] && echo "No NDK found, quitting…" && exit 1

# Setup environment
export EXOPLAYER_ROOT="${PWD}/ExoPlayer"
export FFMPEG_EXT_PATH="${EXOPLAYER_ROOT}/extensions/ffmpeg/src/main"
export FFMPEG_PATH="${PWD}/ffmpeg"
export ENABLED_DECODERS=(alac pcm_mulaw pcm_alaw mp3 aac ac3 eac3 dca mlp truehd)

# Create softlink to ffmpeg
ln -sf "${FFMPEG_PATH}" "${FFMPEG_EXT_PATH}/jni/ffmpeg"

# Start build
cd "${FFMPEG_EXT_PATH}/jni"
./build_ffmpeg.sh "${FFMPEG_EXT_PATH}" "${ANDROID_NDK_PATH}" "linux-x86_64" "${ENABLED_DECODERS[@]}"
