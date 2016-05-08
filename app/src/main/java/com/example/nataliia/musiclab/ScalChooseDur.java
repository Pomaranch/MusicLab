package com.example.nataliia.musiclab;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by Nataliia on 04.05.2016.
 */
public class ScalChooseDur extends Fragment implements View.OnClickListener {
    static int[] duration;
    Dialog d;
    View v;
    FloatingActionButton info;
    FloatingActionButton add;
    EditText durFrom;
    EditText durTo;
    TextView info_txt;
    TextView mistake;
    View.OnClickListener radioListener;
    RadioButton divRadio;
    RadioButton modRadio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.scal_frag_dur, null);

        info = (FloatingActionButton) v.findViewById(R.id.info_button_dur);
        add = (FloatingActionButton) v.findViewById(R.id.add_dur);
        durFrom = (EditText) v.findViewById(R.id.dur_from);
        durTo = (EditText) v.findViewById(R.id.dur_to);
        divRadio = (RadioButton) v.findViewById(R.id.div_dur);
        modRadio = (RadioButton) v.findViewById(R.id.mod_dur);

        info.setOnClickListener(this);
        add.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.info_button_dur:
                d = new Dialog(getActivity());
                d.setContentView(R.layout.info_dial);
                d.setTitle("Info");
                info_txt = (TextView) d.findViewById(R.id.info_text);
                info_txt.setText(getString(R.string.info_dur));
                d.show();
                break;
            case R.id.add_dur:
                if (durFrom.getText().toString().equals("") || durFrom.getText().toString().equals("") || (!divRadio.isChecked() && !modRadio.isChecked())) {
                    mistake = (TextView) v.findViewById(R.id.dur_mistake);
                    mistake.setVisibility(View.VISIBLE);
                } else {
                    int min, max;
                    min = Integer.parseInt(durFrom.getText().toString());
                    max = Integer.parseInt(durTo.getText().toString());
                    Dialog try_dial = new Dialog(getActivity());
                    try_dial.setContentView(R.layout.try_dialog);
                    try_dial.setTitle("Is everything OK?");
                    TextView try_text = (TextView) try_dial.findViewById(R.id.try_text);

                    if (divRadio.isChecked()) {
                        duration = MainActivity.div_oper(MainActivity.numer, max, min);

                    } else {
                        duration = MainActivity.mod_oper(MainActivity.numer, max, min);
                    }
                    String try_string = "";
                    for (int i = 0; i < duration.length; i++) {
                        try_string += Integer.toString(duration[i]) + ", ";
                    }
                    try_text.setText(try_string);
                    try_dial.show();
                }

        }
    }

}
