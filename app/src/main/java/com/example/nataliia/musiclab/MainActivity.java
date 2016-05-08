package com.example.nataliia.musiclab;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ConstChoose constFrag;
    ScalChoosePitch scalFrag1;
    ScalChooseDur scalFrag2;
    ToneGenerator tg;


    static String numer = "";

    Fragment temp;

    FragmentTransaction fTrans;
    FloatingActionButton btnAdd;
    int frgmCount;

    Dialog d;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frgmCount = 0;

        constFrag = new ConstChoose();
        scalFrag1 = new ScalChoosePitch();
        scalFrag2 = new ScalChooseDur();
        tg = new ToneGenerator(AudioManager.STREAM_MUSIC, (int)(ToneGenerator.MAX_VOLUME*0.5));


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
                        break;

                }
                fTrans.commit();

            }
        });
    }

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
}
