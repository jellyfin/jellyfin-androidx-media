#!/bin/bash

# Search NDK path
CANDIDATES=("$ANDROID_NDK_ROOT" "$ANDROID_NDK_HOME" "$NDK_ROOT" "$NDK")

for candidate in ${CANDIDATES[@]}; do
    [[ -n "$candidate" && -d "$candidate" ]] && export ANDROID_NDK_ROOT="$candidate" && break
done

[[ -z "$ANDROID_NDK_ROOT" ]] && echo "No NDK found, quitting…" && exit 1

echo "Found NDK at $ANDROID_NDK_ROOT"

# Setup environment
export EXOPLAYER_ROOT="${PWD}/ExoPlayer"
export FFMPEG_EXT_PATH="${EXOPLAYER_ROOT}/extensions/ffmpeg/src/main"
export FFMPEG_PATH="${PWD}/ffmpeg"
export ENABLED_DECODERS=(alac pcm_mulaw pcm_alaw aac ac3 eac3 dca mlp truehd)

# Create softlink to ffmpeg
ln -sf "${FFMPEG_PATH}" "${FFMPEG_EXT_PATH}/jni/ffmpeg"

# Start build
cd "${FFMPEG_EXT_PATH}/jni"
./build_ffmpeg.sh "${FFMPEG_EXT_PATH}" "${ANDROID_NDK_ROOT}" "linux-x86_64" "${ENABLED_DECODERS[@]}"
