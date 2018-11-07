package si.um.feri.guessemotion.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

import si.um.feri.guessemotion.game.data.enums.GamePlayMode;
import si.um.feri.guessemotion.util.Constants;

/**
 * Created by:
 *
 * Diana Sellárová,
 * Marek Urban,
 * Stanislav Blaško
 *
 * as assignment for Mobile communication services subject on Erasmus+ study.
 */

public class MediaPlayerService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener,
        AudioManager.OnAudioFocusChangeListener {

    private static final String TAG = "MediaPlayerService";

    // Binder given to clients
    private final IBinder iBinder = new LocalBinder();
    private MediaPlayer mediaPlayer;
    //path to the audio file
    private String mediaFile;
    //Used to pause/resume MediaPlayer
    private int resumePosition;
    private AudioManager audioManager;
    // Registered callbacks
    private MediaPlayerServiceCallback serviceCallbacks;
    // position of actual level so we know how much from record should be played
    private int actualLevel;
    // gameplay mode
    private GamePlayMode gamePlayMode;

    //The system calls this method when an activity, requests the service be started
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            //An audio file is passed to the service through putExtra();
            mediaFile = intent.getExtras().getString(Constants.EXTRA_MEDIA_FILE_PATH);
            actualLevel = intent.getExtras().getInt(Constants.EXTRA_MEDIA_ACTUAL_LEVEL, 0);
            gamePlayMode = intent.getExtras().getInt(Constants.EXTRA_GAME_PLAY_MODE, 0) == 0 ? GamePlayMode.RANKED : GamePlayMode.PRACTICE;
        } catch (NullPointerException e) {
            stopSelf();
        }

        //Request audio focus
        if (!requestAudioFocus()) {
            //Could not gain focus
            stopSelf();
        }

        if (mediaFile != null && !mediaFile.isEmpty())
            initMediaPlayer();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            stopMedia();
            mediaPlayer.release();
        }
        removeAudioFocus();
    }

    private void initMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            //Set up MediaPlayer event listeners
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnSeekCompleteListener(this);
            mediaPlayer.setOnInfoListener(this);
        }
        //Reset so that the MediaPlayer is not pointing to another data source
        mediaPlayer.reset();

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            // Set the data source to the mediaFile location and get if from assets folder
            AssetFileDescriptor afd = getAssets().openFd(mediaFile);
            mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
            stopSelf();
        }
        mediaPlayer.prepareAsync();
    }

    private void playMedia() {
        if (!mediaPlayer.isPlaying()) {
            seekByLevel();
            mediaPlayer.start();
            Log.d(TAG,"Starting playing media..");
        }
    }

    private void stopMedia() {
        if (mediaPlayer == null) return;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    public void pauseMedia() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            resumePosition = mediaPlayer.getCurrentPosition();
        }
    }

    public void resumeMedia() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(resumePosition);
            mediaPlayer.start();
        }
    }

    private void seekByLevel() {
        if (gamePlayMode == GamePlayMode.PRACTICE) return;
        int seekOn;
        switch (actualLevel) {
            case 1:
                // do not seek
                break;
            case 2:
                System.out.println("Duration -> " + mediaPlayer.getDuration());
                seekOn = (int)(mediaPlayer.getDuration() * 0.5);
                System.out.println("Seek -> " + seekOn);
                mediaPlayer.seekTo(mediaPlayer.getDuration() - seekOn);
                break;
            case 3:
                System.out.println("Duration -> " + mediaPlayer.getDuration());
                seekOn = (int) (mediaPlayer.getDuration() * 0.25);
                System.out.println("Seek -> " + seekOn);
                mediaPlayer.seekTo(mediaPlayer.getDuration() - seekOn);
                break;
            case 4:
                seekOn = (int) (mediaPlayer.getDuration() * 0.10);
                System.out.println("Seek -> " + seekOn);
                mediaPlayer.seekTo(mediaPlayer.getDuration() - seekOn);
                break;
        }
    }

    public void playNextRecord(String mediaFile, int actualLevel) {
        this.mediaFile = mediaFile;
        this.actualLevel = actualLevel;
        initMediaPlayer();
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        //Invoked indicating buffering status of
        //a media resource being streamed over the network.
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(TAG,"onCompletion fired!");
        //Invoked when playback of a media source has completed.
        stopMedia();
        //stop the service
        stopSelf();
        if (serviceCallbacks != null) {
            serviceCallbacks.onFinishRecord();
        }
    }

    //Handle errors
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        //Invoked when there has been an error during an asynchronous operation.
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Log.d(TAG, "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.d(TAG, "MEDIA ERROR SERVER DIED " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.d(TAG, "MEDIA ERROR UNKNOWN " + extra);
                break;
        }
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        //Invoked to communicate some info.
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //Invoked when the media source is ready for playback.
        playMedia();
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        //Invoked indicating the completion of a seek operation.
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        //Invoked when the audio focus of the system is updated.
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                // TODO: 8.1.18 have a better look on this method
                if (mediaPlayer == null) initMediaPlayer();
                //else if (!mediaPlayer.isPlaying()) mediaPlayer.start();
                mediaPlayer.setVolume(1.0f, 1.0f);
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if (mediaPlayer.isPlaying()) mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (mediaPlayer.isPlaying()) mediaPlayer.pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if (mediaPlayer.isPlaying()) mediaPlayer.setVolume(0.1f, 0.1f);
                break;
        }
    }

    private boolean requestAudioFocus() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (audioManager == null) return false;
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            //Focus gained
            return true;
        }
        //Could not gain focus
        return false;
    }

    private boolean removeAudioFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == audioManager.abandonAudioFocus(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    public class LocalBinder extends Binder {
        public MediaPlayerService getService() {
            return MediaPlayerService.this;
        }
    }

    public void setCallbacks(MediaPlayerServiceCallback callbacks) {
        serviceCallbacks = callbacks;
    }

}
