package dev.arbjerg.lavalink.protocol

import com.fasterxml.jackson.databind.node.ObjectNode

data class PlaylistInfo(
    val name: String,
    val selectedTrack: Int
)

data class Playlist(
    val info: PlaylistInfo,
    val pluginInfo: ObjectNode,
    val tracks: List<Track>
)