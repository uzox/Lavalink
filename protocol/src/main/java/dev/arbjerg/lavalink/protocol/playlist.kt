package dev.arbjerg.lavalink.protocol

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.JsonNode

data class PlaylistInfo(
    val name: String,
    val selectedTrack: Int
)

data class Playlist(
    val info: Info,
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val pluginData: Map<String, JsonNode> = mutableMapOf()
) {
    data class Info(
        val name: String,
        val selectedTrack: Int
    )
}



