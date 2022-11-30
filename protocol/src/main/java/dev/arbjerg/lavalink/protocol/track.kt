package dev.arbjerg.lavalink.protocol

import com.fasterxml.jackson.databind.node.ObjectNode

data class Track(
    val encoded: String,
    val track: String,
    val info: TrackInfo,
    val pluginInfo: ObjectNode
)

data class TrackInfo(
    val identifier: String,
    val isSeekable: Boolean,
    val author: String,
    val length: Long,
    val isStream: Boolean,
    val position: Long,
    val title: String,
    val uri: String,
    val sourceName: String,
)
