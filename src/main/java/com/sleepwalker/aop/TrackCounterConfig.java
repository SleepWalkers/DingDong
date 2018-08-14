package com.sleepwalker.aop;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.sleepwalker.bean.BlankDisc;
import com.sleepwalker.bean.CompactDisc;

@Configuration
@EnableAspectJAutoProxy
public class TrackCounterConfig {

    @Resource
    private BlankDisc    blankDisc;

    @Resource
    private TrackCounter trackCounter;

    public CompactDisc sgtPeppers() {
        blankDisc.setTitle("Test Title");
        blankDisc.setArtist("The Beatls");

        List<String> tracks = new ArrayList<>();
        tracks.add("Track1");
        tracks.add("Track2");
        tracks.add("Track3");
        tracks.add("Track4");
        tracks.add("Track5");

        blankDisc.setTracks(tracks);
        return blankDisc;
    }

    public TrackCounter trackCount() {
        return trackCounter;
    }
}
