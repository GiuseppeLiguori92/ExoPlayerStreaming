package com.giuseppeliguori.exoplayerstreaming;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

public class MainActivity extends AppCompatActivity {

    String url = "http://techslides.com/demos/sample-videos/small.mp4";
    String url2 = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";

    String urlDash = "https://bitmovin-a.akamaihd.net/content/playhouse-vr/mpds/105560.mpd";

    String urlHsl = "https://abclive2-lh.akamaihd.net/i/abc_live11@423404/master.m3u8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        DASHPlayerManager playerManager = new DASHPlayerManager(getApplicationContext(),
//                (SimpleExoPlayerView) findViewById(R.id.player_view),
//                Uri.parse(urlDash));

        HLSPlayerManager playerManager = new HLSPlayerManager(getApplicationContext(),
                (SimpleExoPlayerView) findViewById(R.id.player_view),
                Uri.parse(urlHsl));
    }
}
