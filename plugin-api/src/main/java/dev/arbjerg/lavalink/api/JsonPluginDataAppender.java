package dev.arbjerg.lavalink.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.Collections;
import java.util.Map;

public interface JsonPluginDataAppender {

	/**
	 * Adds custom fields to an {@link AudioTrack}'s JSON.
	 *
	 * @param audioTrack the track that was loaded.
	 * @return the new {@link JsonNode}
	 */
	default Map<String, JsonNode> addTrackPluginData(AudioTrack audioTrack) {
		return Collections.emptyMap();
	}

	/**
	 * Adds custom fields to an {@link AudioPlaylist}'s JSON.
	 *
	 * @param playlist the playlist that was loaded.
	 * @return the new {@link JsonNode}
	 */
	default Map<String, JsonNode> addPlaylistPluginData(AudioPlaylist playlist) {
		return Collections.emptyMap();
	}

}
