package dev.arbjerg.lavalink.protocol

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.JsonNode

data class Track(
    val encoded: String,
    val track: String,
    val info: TrackInfo,
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val pluginData: Map<String, JsonNode> = mutableMapOf()
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
