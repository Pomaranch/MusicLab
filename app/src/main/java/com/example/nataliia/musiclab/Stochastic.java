package com.example.nataliia.musiclab;


import jm.JMC;
import jm.constants.ProgramChanges;
import jm.music.data.*;
import jm.midi.*;
import jm.util.*;
import jm.constants.*;

/**
 * Created by Nataliia on 10.05.2016.
 */
/*
public final class Stochastic {
    public static void main(String[] args) {
        Score stochScore = new Score("JMDemo - Stochastic");
        Part inst = new Part("Piano", ProgramChanges.PIANO, 0);
        Phrase phr = new Phrase();
        for(int i=0;i<32;i++){
            Note note = new Note((int)(Math.random()*127), Durations.CROTCHET);
            phr.addNote(note);
        }
        inst.addPhrase(phr);
        stochScore.addPart(inst);
        Write.midi(stochScore, "stochy.mid");
    }
}

*/