package com.example.nataliia.musiclab;

import android.annotation.TargetApi;
import android.app.FragmentTransaction;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import jm.audio.Instrument;
import jm.constants.Pitches;

import jm.constants.Durations;

import jm.constants.ProgramChanges;
import jm.midi.MidiSynth;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Play;

public class MainActivity extends AppCompatActivity {

    private SoundPool mSoundPool;
    private int[] samples = {R.raw.a_min7, R.raw.a_sharp_min7, R.raw.b_min7,R.raw.c_min7low_pitch, R.raw.c_sharp_min7, R.raw.d_min7, R.raw.d_sharp_min7, R.raw.e_min7, R.raw.f_sharp_min7, R.raw.c_sharp_min7, R.raw.g_min7, R.raw.g_sharp_min7};
    private int[] sounds = new int[samples.length];
    private int[] a_samples = {R.raw.a_middle, R.raw.a_sharp_middle, R.raw.b_middle, R.raw.c_middle, R.raw.c_sharp_middle, R.raw.d_middle, R.raw.d_sharp_middle, R.raw.e_middle, R.raw.f_middle, R.raw.c_sharp_middle, R.raw.g_middle, R.raw.g_sharp_min7};
    private int[] a_sounds;

    static String numer = "";
    ConstChoose constFrag;
    ScalChoosePitch scalFrag1;
    ScalChooseDur scalFrag2;
    ToneGenerator tg;
    double[] pitch_const = {440.000, 415.305, 391.995, 369.994, 349.228, 329.628, 311.127, 293.665, 277.183, 261.626, 246.942, 233.082, 220.000, 207.652, 195.998, 184.997, 174.614, 164.814, 155.563, 146.832, 138.591, 130.813, 123.471, 116.541, 110.000, 103.826, 97.9989, 92.4986, 87.3071, 82.4069, 77.7817, 73.4162, 69.2957, 65.4064, 61.7354, 58.2705, 55.0000, 51.9130, 48.9995, 46.2493, 43.6536, 41.2035, 38.8909, 36.7081, 34.6479, 32.7032, 30.8677, 29.1353, 27.5000, 34.6479, 32.7032, 30.8677, 29.1353, 27.5000};

    int[] duration_const = {250, 500, 1000, 1500, 2000, 2500};

    private Timer mTimer;
    private TimerTask timerTask;



    FragmentTransaction fTrans;
    FloatingActionButton btnAdd;
    int frgmCount;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    public static String piSpigot(final int n) {
        // найденные цифры сразу же будем записывать в StringBuilder
        StringBuilder pi = new StringBuilder(n);
        int boxes = n * 10 / 3;    // размер массива
        int reminders[] = new int[boxes];
        // инициализируем масив двойками
        for (int i = 0; i < boxes; i++) {
            reminders[i] = 2;
        }
        int heldDigits = 0;    // счётчик временно недействительных цифр
        for (int i = 0; i < n; i++) {
            int carriedOver = 0;    // перенос на следующий шаг
            int sum = 0;
            for (int j = boxes - 1; j >= 0; j--) {
                reminders[j] *= 10;
                sum = reminders[j] + carriedOver;
                int quotient = sum / (j * 2 + 1);   // результат деления суммы на знаменатель
                reminders[j] = sum % (j * 2 + 1);   // остаток от деления суммы на знаменатель
                carriedOver = quotient * j;   // j - числитель
            }
            reminders[0] = sum % 10;
            int q = sum / 10;    // новая цифра числа Пи
            // регулировка недействительных цифр
            if (q == 9) {
                heldDigits++;
            } else if (q == 10) {
                q = 0;
                for (int k = 1; k <= heldDigits; k++) {
                    int replaced = Integer.parseInt(pi.substring(i - k, i - k + 1));
                    if (replaced == 9) {
                        replaced = 0;
                    } else {
                        replaced++;
                    }
                    pi.deleteCharAt(i - k);
                    pi.insert(i - k, replaced);
                }
                heldDigits = 1;
            } else {
                heldDigits = 1;
            }
            pi.append(q);    // сохраняем найденную цифру
        }
        if (pi.length() >= 2) {
            pi.insert(1, '.');    // добавляем в строчку точку после 3
        }
        return pi.toString();
    }


    public static int[] div_oper(String preInput, int input_max, int input_min) {
        String[] input = preInput.split("");
        int[] output = new int[preInput.length()];
        int[] minMax = {0, 9};
        for (int i = 1; i < preInput.length(); i++) {
            output[i] = input_min + (input_max - input_min) * (Integer.parseInt(input[i]) - minMax[0]) / (minMax[1] - minMax[0]);
        }
        return output;
    }

    public static int[] mod_oper(String preInput, int input_max, int input_min) {
        String[] input = preInput.split("");
        int[] output = new int[preInput.length()];
        for (int i = 1; i < preInput.length(); i++) {
            output[i] = input_min + (Integer.parseInt(input[i]) % (input_max - input_min + 1));
        }
        return output;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frgmCount = 0;

        constFrag = new ConstChoose();
        scalFrag1 = new ScalChoosePitch();
        scalFrag2 = new ScalChooseDur();



       /* if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            mSoundPool =  new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        }else{
            createNewSoundPool();
        }*/
        mSoundPool =  new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        sounds = setSounds(samples);
        a_sounds = setSounds(a_samples);

        fTrans = getFragmentManager().beginTransaction();
        fTrans.add(R.id.frgmCont, constFrag);
        fTrans.commit();

        btnAdd = (FloatingActionButton) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fTrans = getFragmentManager().beginTransaction();
                frgmCount++;
                switch (frgmCount) {
                    case 1:
                        Log.d("OO",Integer.toString(sounds.length));
                        if (numer.equals("")) {
                            frgmCount--;
                            Toast.makeText(getApplicationContext(), "Оберіть один з варіантів", Toast.LENGTH_SHORT).show();
                        } else {
                            scalFrag1.setRetainInstance(true);
                            fTrans.replace(R.id.frgmCont, scalFrag1);
                        }
                        break;

                    case 2:
                        scalFrag2.setRetainInstance(true);
                        fTrans.replace(R.id.frgmCont, scalFrag2);
                        break;
                    case 3:
                        for(int i = 0; i < ScalChooseDur.duration.length; i++){
                            Log.d("OOO", Integer.toString(i));
                            try {
                                musicPlay(ScalChooseDur.duration[i], ScalChoosePitch.pitch[i]);
                            }catch (Exception e){
                                e.getCause();
                            }
                        }
                        btnAdd.setVisibility(View.GONE);


                        break;

                }
                fTrans.commit();

            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public int[] setSounds(int[] sampl) {
        int[] sound = new int[sampl.length];
        for (int i = 0; i < sampl.length; i++) {
            sound[i] = mSoundPool.load(getApplicationContext(), sampl[i], 1);
        }
        return sound;
    }

    private void resume(){
        mSoundPool.autoResume();
    }
    private void musicPlay(int i, int j){
        Thread timeThread;
        timeThread = new Thread();
        timeThread.start();

        mSoundPool.play(sounds[i], 1, 1, 1, 0, 1/2);
        mSoundPool.play(a_sounds[i], 1, 1, 1,0,1);
        try {
              timeThread.sleep(duration_const[j]);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void createNewSoundPool() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        mSoundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }


    public void player(int[] pit, int[] dur) {
       /* AudioTrack tone;
        Thread timeThread;
        timeThread = new Thread();
        timeThread.start();*/

        for (int i = 1; i < pitch_const.length; i++) {
            Log.d("LOG", Integer.toString(i));
           /* try {
              //  timeThread.sleep(duration_const[dur[i]]);
            }catch(InterruptedException e){
                e.printStackTrace();
            }*/
            try {
                //    tone = generateTone(pitch_const[pit[i]], duration_const[dur[i]]);
                //   tone.play();

            } catch (Exception e) {
                e.getCause();
                e.printStackTrace();
            }


        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.nataliia.musiclab/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.nataliia.musiclab/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
    /*  private static int[] min_max(String preInput){
        String[] input = preInput.split("");
        int[] min_max = new int[2];
        Toast.makeText(static this, )
      /*  for(int i = 0; i < preInput.length(); i++){
            if(Integer.parseInt(input[i]) > min_max[1]){
                min_max[1] = Integer.parseInt(input[i]);
            }else if(Integer.parseInt(input[i]) < min_max[0]){
                min_max[0] = Integer.parseInt(input[i]);
            }
            if(min_max[1] == 9 && min_max[0] == 0){
                break;
            }
        }
        return min_max;
    }*/
   /* private AudioTrack generateTone(double freqHz, int durationMs)
    {
        int count = (int)(44100.0 * 2.0 * (durationMs / 1000.0)) & ~1;
        short[] samples = new short[count];
        for(int i = 0; i < count; i += 2){
            short sample = (short)(Math.sin(2 * Math.PI * i / (44100.0 / freqHz)) * 0x7FFF);
            samples[i + 0] = sample;
            samples[i + 1] = sample;
        }
        AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
                count * (Short.SIZE / 8), AudioTrack.MODE_STATIC);
        track.write(samples, 0, count);
        return track;
    }*/
    /* public void jmPlayer(){
        Score stochScore = new Score("JMDemo - Stochastic");
        Part inst = new Part("Piano", ProgramChanges.PIANO, 0);
        Phrase phr = new Phrase();
        Instrument instrument = new RTPluckInst(44100);
        for(int i=0;i<32;i++){
            Note note = new Note(Pitches.A2, Durations.CROTCHET);
            phr.addNote(note);
        }
        inst.addPhrase(phr);
        stochScore.addPart(inst);
        //Write.midi(stochScore, "stochy.mid");
        Play.audio(inst,instrument);

      mediaPlayer = new MediaPlayer();
        try{
            mediaPlayer.setDataSource("stochy.mid");
        }catch (Exception e){
            e.printStackTrace();
        }
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try{
            mediaPlayer.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }
        mediaPlayer.start();
        }
*/

}

