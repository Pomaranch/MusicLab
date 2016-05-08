package com.example.nataliia.musiclab;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Nataliia on 04.05.2016.
 */
public class ScalChoosePitch extends Fragment{
    Dialog d;
    View v;
    FloatingActionButton info;

    TextView info_txt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.scal_frag_pitch, null);


        info = (FloatingActionButton) v.findViewById(R.id.info_button_pitch);

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d = new Dialog(getActivity());
                d.setContentView(R.layout.info_dial);
                d.setTitle("Info");
                info_txt = (TextView)d.findViewById(R.id.info_text);
                info_txt.setText(getString(R.string.info_pitch));
                d.show();

            }
        });
        return v;
    }
}
