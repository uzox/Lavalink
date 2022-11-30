/*
 * Copyright (c) 2021 Freya Arbjerg and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package lavalink.server.util

import com.fasterxml.jackson.databind.JsonNode
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import dev.arbjerg.lavalink.api.JsonPluginDataAppender
import dev.arbjerg.lavalink.protocol.*
import lavalink.server.io.SocketContext
import lavalink.server.io.SocketServer
import lavalink.server.player.LavalinkPlayer
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

fun LavalinkPlayer.toPlayer(context: SocketContext, dataAppender: List<JsonPluginDataAppender>): Player {
    val connection = context.getMediaConnection(this).gatewayConnection
    val voiceServerInfo = context.koe.getConnection(guildId)?.voiceServerInfo

    return Player(
        guildId.toString(),
        track?.toTrack(context.audioPlayerManager, dataAppender),
        audioPlayer.volume,
        audioPlayer.isPaused,
        VoiceState(
            voiceServerInfo?.token ?: "",
            voiceServerInfo?.endpoint ?: "",
            voiceServerInfo?.sessionId ?: "",
            connection?.isOpen ?: false,
            connection?.ping ?: -1
        ),
        filters.toFilters(),
    )
}

fun AudioTrack.toTrack(audioPlayerManager: AudioPlayerManager, dataAppender: List<JsonPluginDataAppender>): Track {
    return this.toTrack(encodeTrack(audioPlayerManager, this), dataAppender)
}

fun AudioTrack.toTrack(encoded: String, dataAppender: List<JsonPluginDataAppender>): Track {
    val pluginData = mutableMapOf<String, JsonNode>()
    dataAppender.forEach { pluginData.putAll(it.addTrackPluginData(this)) }

    return Track(encoded, encoded, this.toInfo(), pluginData)
}

fun AudioTrack.toInfo(): TrackInfo {
    return TrackInfo(
        this.identifier,
        this.isSeekable,
        this.info.author,
        this.info.length,
        this.info.isStream,
        this.position,
        this.info.title,
        this.info.uri,
        this.sourceManager.sourceName,
    )
}

fun AudioPlaylist.toPlaylist(dataAppender: List<JsonPluginDataAppender>): Playlist {
    val pluginData = mutableMapOf<String, JsonNode>()
    dataAppender.forEach { pluginData.putAll(it.addPlaylistPluginData(this)) }

    return Playlist(
        Playlist.Info(this.name, this.tracks.indexOf(this.selectedTrack)),
        pluginData
    )
}

fun getRootCause(throwable: Throwable?): Throwable {
    var rootCause = throwable
    while (rootCause!!.cause != null) {
        rootCause = rootCause.cause
    }
    return rootCause
}

fun socketContext(socketServer: SocketServer, sessionId: String) =
    socketServer.contextMap[sessionId] ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found")

fun existingPlayer(socketContext: SocketContext, guildId: Long) =
    socketContext.players[guildId] ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found")
