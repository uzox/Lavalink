package dev.arbjerg.lavalink.protocol

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.JsonNode

data class PlaylistInfo(
    val name: String,
    val selectedTrack: Int,

    @JsonAnyGetter
    @JsonAnySetter
    @get:JsonIgnore
    val pluginFields: Map<String, JsonNode> = mutableMapOf()
)

