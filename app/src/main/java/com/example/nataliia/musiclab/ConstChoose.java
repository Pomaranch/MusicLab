package com.example.nataliia.musiclab;

import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by Nataliia on 04.05.2016.
 */
public class ConstChoose extends Fragment{
    RadioButton pi;
    RadioButton fi;
    RadioButton e;
    RadioGroup rg;
    View v;
    EditText ed_precision;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.const_frag, null);

        ed_precision = (EditText) v.findViewById(R.id.ed_precision);
        pi = (RadioButton)v.findViewById(R.id.radio_pi);
        fi = (RadioButton)v.findViewById(R.id.radio_fi);
        e = (RadioButton)v.findViewById(R.id.radio_e);
        rg = (RadioGroup) v.findViewById(R.id.const_group);


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_pi:
                        MainActivity.numer = getString(R.string.pi);
                        break;
                    case R.id.radio_fi:
                        MainActivity.numer = getString(R.string.fi);
                        break;
                    case R.id.radio_e:
                        MainActivity.numer = getString(R.string.e);
                        break;
                    default:
                        break;
                }
            }
        });

        return v;
    }
}
