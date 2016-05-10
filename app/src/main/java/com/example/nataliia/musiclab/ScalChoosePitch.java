package com.example.nataliia.musiclab;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Nataliia on 04.05.2016.
 */
public class ScalChoosePitch extends Fragment implements View.OnClickListener{
    static int[] pitch;
    Dialog d;
    View v;
    FloatingActionButton info;
    FloatingActionButton add;
    EditText pitchFrom;
    EditText pitchTo;
    TextView info_txt;
    TextView mistake;
    View.OnClickListener radioListener;
    RadioButton divRadio;
    RadioButton modRadio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.scal_frag_pitch, null);

        info = (FloatingActionButton) v.findViewById(R.id.info_button_pitch);
        add = (FloatingActionButton) v.findViewById(R.id.add_pitch);
        pitchFrom = (EditText) v.findViewById(R.id.pitch_from);
        pitchTo = (EditText) v.findViewById(R.id.pitch_to);
        divRadio = (RadioButton) v.findViewById(R.id.div_pitch);
        modRadio = (RadioButton) v.findViewById(R.id.mod_pitch);

        info.setOnClickListener(this);
        add.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.info_button_pitch:
                d = new Dialog(getActivity());
                d.setContentView(R.layout.info_dial);
                d.setTitle("Info");
                info_txt = (TextView) d.findViewById(R.id.info_text);
                info_txt.setText(getString(R.string.info_pitch));
                d.show();
                break;
            case R.id.add_pitch:
                if (pitchFrom.getText().toString().equals("") || pitchFrom.getText().toString().equals("") || (!divRadio.isChecked() && !modRadio.isChecked())) {
                    mistake = (TextView) v.findViewById(R.id.pitch_mistake);
                    mistake.setVisibility(View.VISIBLE);
                } else {
                    int min, max;
                    if(Integer.parseInt(pitchFrom.getText().toString()) < Integer.parseInt(pitchTo.getText().toString())){
                        min = Integer.parseInt(pitchFrom.getText().toString());
                        max = Integer.parseInt(pitchTo.getText().toString());
                    }else if(pitchFrom.getText().toString().equals(pitchTo.getText().toString())){
                        Toast.makeText(getActivity(), "Значення повинні відрізнятися", Toast.LENGTH_SHORT).show();
                        break;
                    }else{
                        max = Integer.parseInt(pitchFrom.getText().toString());
                        min = Integer.parseInt(pitchTo.getText().toString());
                    }
                    Dialog try_dial = new Dialog(getActivity());
                    try_dial.setContentView(R.layout.try_dialog);
                    try_dial.setTitle("Is everything OK?");
                    TextView try_text = (TextView) try_dial.findViewById(R.id.try_text);

                    if (divRadio.isChecked()) {
                        pitch = MainActivity.div_oper(MainActivity.numer, max, min);

                    } else {
                        pitch = MainActivity.mod_oper(MainActivity.numer, max, min);
                    }
                    String try_string = "";
                    for (int i = 1; i < pitch.length; i++) {
                        try_string += Integer.toString(pitch[i]) + ", ";
                    }
                    try_text.setText(try_string);
                    try_dial.show();
                }

        }
    }

}
