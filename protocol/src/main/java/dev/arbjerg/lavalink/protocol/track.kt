package dev.arbjerg.lavalink.protocol

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.JsonNode

data class Track(
    val encoded: String,
    val track: String,
    val info: TrackInfo,
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

    @JsonAnyGetter
    @JsonAnySetter
    @get:JsonIgnore
    val pluginFields: Map<String, JsonNode> = mutableMapOf()
)
