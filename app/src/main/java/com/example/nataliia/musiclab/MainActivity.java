package com.example.nataliia.musiclab;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static String numer = "";
    ConstChoose constFrag;
    ScalChoosePitch scalFrag1;
    ScalChooseDur scalFrag2;
    int[] duration_const = {250, 500, 1000, 1500, 2000, 2500};
    FragmentTransaction fTrans;
    FloatingActionButton btnAdd;
    int frgmCount;
    private SoundPool mSoundPool;
    private int[] samples = {R.raw.a_min7, R.raw.a_sharp_min7, R.raw.b_min7,R.raw.c_min7low_pitch, R.raw.c_sharp_min7, R.raw.d_min7, R.raw.d_sharp_min7, R.raw.e_min7, R.raw.f_sharp_min7, R.raw.c_sharp_min7, R.raw.g_min7, R.raw.g_sharp_min7};
    private int[] sounds = new int[samples.length];
    private int[] a_samples = {R.raw.a_middle, R.raw.a_sharp_middle, R.raw.b_middle, R.raw.c_middle, R.raw.c_sharp_middle, R.raw.d_middle, R.raw.d_sharp_middle, R.raw.e_middle, R.raw.f_middle, R.raw.c_sharp_middle, R.raw.g_middle, R.raw.g_sharp_min7};
    private int[] a_sounds = new int[a_samples.length];

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

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            mSoundPool =  new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        }else{
            createNewSoundPool();
        }
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
                            String precision = "";
                            Fragment temp  = getFragmentManager().findFragmentById(R.id.frgmCont);
                            try {
                                precision = ((EditText) temp.getView().findViewById(R.id.ed_precision)).getText().toString();
                            }catch (NullPointerException e){
                                e.printStackTrace();
                            }
                            if(precision.equals("")){
                                numer = numer.substring(0, 100);
                            }else{
                                numer = numer.substring(0, Integer.parseInt(precision));
                            }

                            scalFrag1.setRetainInstance(false);
                            fTrans.replace(R.id.frgmCont, scalFrag1);
                        }
                        break;
                    case 2:
                        if(ScalChoosePitch.pitch == null){
                            Toast.makeText(getApplication(), R.string.no_add, Toast.LENGTH_SHORT).show();
                            frgmCount--;
                        }else{
                            scalFrag2.setRetainInstance(false);
                            fTrans.replace(R.id.frgmCont, scalFrag2);
                        }
                        break;
                    case 3:
                        if(ScalChooseDur.duration == null){
                            Toast.makeText(getApplication(), R.string.no_add, Toast.LENGTH_SHORT).show();
                            frgmCount--;
                        }else{
                          //  btnAdd.setBackgroundResource(R.drawable.ic_replay_white_48dp);
                            musicPlay();



                        }
                        break;

                    case 4:
                        constFrag = new ConstChoose();
                        scalFrag1 = new ScalChoosePitch();
                        scalFrag2 = new ScalChooseDur();
                        fTrans.add(R.id.frgmCont, constFrag);
                        frgmCount = 0;

                        break;
                    default:
                        break;
                }
                fTrans.commit();
            }
        });
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

    private void musicPlay() {
        new Thread() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < ScalChooseDur.duration.length; i++) {
                        mSoundPool.play(sounds[ScalChoosePitch.pitch[i]], 1, 1, 1, 0, 1);
                        mSoundPool.play(a_sounds[ScalChoosePitch.pitch[i]], 1, 1, 1, 0, 1);

                        Thread.sleep(duration_const[ScalChooseDur.duration[i]]);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        }.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void createNewSoundPool() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        mSoundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }

    @Override
    public void onStop() {
        mSoundPool.release();
        super.onStop();
    }

}

