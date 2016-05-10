package com.example.nataliia.musiclab;

import android.app.FragmentTransaction;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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

    static String numer = "";
    ConstChoose constFrag;
    ScalChoosePitch scalFrag1;
    ScalChooseDur scalFrag2;
    ToneGenerator tg;

    double[] pitch_const = {440.000, 415.305, 391.995, 369.994, 349.228, 329.628, 311.127, 293.665, 277.183, 261.626, 246.942, 233.082, 220.000, 207.652, 195.998, 184.997, 174.614, 164.814, 155.563, 146.832, 138.591, 130.813, 123.471, 116.541, 110.000, 103.826, 97.9989, 92.4986, 87.3071, 82.4069, 77.7817, 73.4162, 69.2957, 65.4064, 61.7354, 58.2705, 55.0000, 51.9130, 48.9995, 46.2493, 43.6536, 41.2035, 38.8909, 36.7081, 34.6479, 32.7032, 30.8677, 29.1353, 27.5000, 34.6479, 32.7032, 30.8677, 29.1353, 27.5000};

    int[] duration_const = {200,250,300,350,400,500};

    FragmentTransaction fTrans;
    FloatingActionButton btnAdd;
    int frgmCount;


    MediaPlayer mediaPlayer;
    AudioManager am;


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
        am = (AudioManager) getSystemService(AUDIO_SERVICE);

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
                        btnAdd.setVisibility(View.GONE);
                        player(ScalChoosePitch.pitch, ScalChooseDur.duration);
                        Log.d("LOG", Integer.toString(ScalChooseDur.duration.length));
                        Log.d("LOG", Integer.toString(ScalChoosePitch.pitch.length));

                        break;

                }
                fTrans.commit();

            }
        });
    }

    public ConstChoose getConstFrag() {
        return constFrag;
    }


    public void player(int[] pit, int[] dur){
        AudioTrack tone;
        Thread timeThread;
        timeThread = new Thread();
        timeThread.start();

        for(int i = 1; i < pitch_const.length; i++){
            Log.d("LOG", Integer.toString(i));
            try {
                timeThread.sleep(duration_const[dur[i]]);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            try {
                tone = generateTone(pitch_const[pit[i]], duration_const[dur[i]]);
                tone.play();

            }catch (Exception e){
                e.getCause();
                e.printStackTrace();
            }


        }
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
    private AudioTrack generateTone(double freqHz, int durationMs)
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
    }
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

