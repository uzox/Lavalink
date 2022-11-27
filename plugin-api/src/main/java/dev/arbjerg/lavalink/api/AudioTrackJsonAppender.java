package dev.arbjerg.lavalink.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.Map;

public interface AudioTrackJsonAppender {

    /**
     * Adds custom fields to an {@link AudioTrack}'s JSON.
     *
     * @param audioTrack the track that was loaded.
     * @return the new {@link JsonNode}
     */
    Map<String, JsonNode> appendFields(AudioTrack audioTrack);

}
