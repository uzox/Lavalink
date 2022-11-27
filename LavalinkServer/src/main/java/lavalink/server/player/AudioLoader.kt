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
package lavalink.server.player

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import dev.arbjerg.lavalink.api.AudioPlaylistJsonAppender
import dev.arbjerg.lavalink.api.AudioTrackJsonAppender
import dev.arbjerg.lavalink.protocol.*
import lavalink.server.util.toPlaylistInfo
import lavalink.server.util.toTrack
import org.slf4j.LoggerFactory
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.concurrent.atomic.AtomicBoolean

class AudioLoader(
    private val audioPlayerManager: AudioPlayerManager,
    private val trackModifiers: List<AudioTrackJsonAppender>,
    private val playlistModifiers: List<AudioPlaylistJsonAppender>
) : AudioLoadResultHandler {

    companion object {
        private val log = LoggerFactory.getLogger(AudioLoader::class.java)
        private val NO_MATCHES = LoadResult(ResultStatus.NO_MATCHES, emptyList(), null, null)
    }

    private val loadResult = CompletableFuture<LoadResult>()
    private val used = AtomicBoolean(false)

    fun load(identifier: String?): CompletionStage<LoadResult> {
        val isUsed = used.getAndSet(true)
        check(!isUsed) { "This loader can only be used once per instance" }
        log.trace("Loading item with identifier $identifier")
        audioPlayerManager.loadItem(identifier, this)
        return loadResult
    }

    override fun trackLoaded(audioTrack: AudioTrack) {
        log.info("Loaded track ${audioTrack.info.title}")

        val tracks = listOf(audioTrack.toTrack(audioPlayerManager, trackModifiers))
        loadResult.complete(LoadResult(ResultStatus.TRACK_LOADED, tracks, null))
    }

    override fun playlistLoaded(audioPlaylist: AudioPlaylist) {
        log.info("Loaded playlist ${audioPlaylist.name}")

        val status = if (audioPlaylist.isSearchResult) ResultStatus.SEARCH_RESULT else ResultStatus.PLAYLIST_LOADED
        val tracks = audioPlaylist.tracks.map { it.toTrack(audioPlayerManager, trackModifiers) }
        val playlistInfo = if (audioPlaylist.isSearchResult) null else audioPlaylist.toPlaylistInfo(playlistModifiers)
        loadResult.complete(LoadResult(status, tracks, playlistInfo))
    }

    override fun noMatches() {
        log.info("No matches found")

        loadResult.complete(NO_MATCHES)
    }

    override fun loadFailed(e: FriendlyException) {
        log.error("Load failed", e)

        loadResult.complete(LoadResult(e))
    }

}