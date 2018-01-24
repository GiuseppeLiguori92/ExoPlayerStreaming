package com.giuseppeliguori.exoplayerstreaming;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Surface;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

/**
 * Created by giuseppeliguori on 23/01/2018.
 */

public class HLSPlayerManager {
    private static final String TAG = "PlayerManager";

    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer exoPlayer;

    public HLSPlayerManager(Context context, Uri uri) {
        this.simpleExoPlayerView = new SimpleExoPlayerView(context);
        init(context, uri);
    }

    public HLSPlayerManager(Context context, SimpleExoPlayerView simpleExoPlayerView, Uri uri) {
        this.simpleExoPlayerView = simpleExoPlayerView;
        init(context, uri);
    }

    private void init(Context context, Uri uri) {
        Handler mUiUpdateHandler = new Handler(Looper.getMainLooper());

        final BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter(mUiUpdateHandler, new BandwidthMeter.EventListener() {
            @Override
            public void onBandwidthSample(int elapsedMs, long bytes, long bitrate) {
                Log.d(TAG, "onBandwidthSample()1 called with: elapsedMs = [" + elapsedMs + "], bytes = [" + bytes + "], bitrate = [" + bitrate + "]");
            }
        });

        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

        simpleExoPlayerView.setPlayer(exoPlayer);

        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(
                        context,
                        Util.getUserAgent(context, context.getString(R.string.app_name)),
                        (TransferListener<? super DataSource>) bandwidthMeter);

        MediaSource mediaSource = new HlsMediaSource(uri, dataSourceFactory, null, null);

        Log.d(TAG, "init() called with: context = [" + context + "], uri = [" + uri + "]");
        exoPlayer.prepare(mediaSource);
        exoPlayer.setPlayWhenReady(true);

        exoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                Log.d(TAG, "onLoadingChanged() called with: isLoading = [" + isLoading + "]");
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Log.d(TAG, "onPlayerStateChanged() called with: playWhenReady = [" + playWhenReady + "], playbackState = [" + playbackState + "]");
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.d(TAG, "onPlayerError() called with: error = [" + error + "]");
            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                Log.d(TAG, "onPlaybackParametersChanged() called with: playbackParameters = [" + playbackParameters + "]");
            }

            @Override
            public void onSeekProcessed() {

            }
        });

        exoPlayer.addVideoListener(new SimpleExoPlayer.VideoListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                Log.d(TAG, "onVideoSizeChanged() called with: width = [" + width + "], height = [" + height + "], unappliedRotationDegrees = [" + unappliedRotationDegrees + "], pixelWidthHeightRatio = [" + pixelWidthHeightRatio + "]");
            }

            @Override
            public void onRenderedFirstFrame() {
                Log.d(TAG, "onRenderedFirstFrame() called");
            }
        });

        exoPlayer.addVideoDebugListener(new VideoRendererEventListener() {
            @Override
            public void onVideoEnabled(DecoderCounters counters) {
                Log.d(TAG, "onVideoEnabled() called with: counters = [" + counters + "]");
            }

            @Override
            public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
                Log.d(TAG, "onVideoDecoderInitialized() called with: decoderName = [" + decoderName + "], initializedTimestampMs = [" + initializedTimestampMs + "], initializationDurationMs = [" + initializationDurationMs + "]");
            }

            @Override
            public void onVideoInputFormatChanged(Format format) {
                Log.d(TAG, "onVideoInputFormatChanged() called with: format = [" + format.bitrate + "]");
            }

            @Override
            public void onDroppedFrames(int count, long elapsedMs) {
                Log.d(TAG, "onDroppedFrames() called with: count = [" + count + "], elapsedMs = [" + elapsedMs + "]");
            }

            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                Log.d(TAG, "onVideoSizeChanged() called with: width = [" + width + "], height = [" + height + "], unappliedRotationDegrees = [" + unappliedRotationDegrees + "], pixelWidthHeightRatio = [" + pixelWidthHeightRatio + "]");
            }

            @Override
            public void onRenderedFirstFrame(Surface surface) {
                Log.d(TAG, "onRenderedFirstFrame() called with: surface = [" + surface + "]");
            }

            @Override
            public void onVideoDisabled(DecoderCounters counters) {
                Log.d(TAG, "onVideoDisabled() called with: counters = [" + counters + "]");
            }
        });
    }
}
