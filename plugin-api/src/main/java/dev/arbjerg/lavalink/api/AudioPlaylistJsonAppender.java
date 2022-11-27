package dev.arbjerg.lavalink.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;

import java.util.Map;

public interface AudioPlaylistJsonAppender {

    /**
     * Adds custom fields to an {@link AudioPlaylist}'s JSON.
     *
     * @param playlist   the playlist that was loaded.
     * @return the new {@link JsonNode}
     */
    Map<String, JsonNode> appendFields(AudioPlaylist playlist);

}
