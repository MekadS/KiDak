/*
 * Copyright (C) 2008-2009 The Android Open Source Project
 * Copyright (C) 2021 Medonlakador Syiem
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mekads.kidak;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

import androidx.annotation.RequiresApi;

public class KiDak extends InputMethodService implements KeyboardView.OnKeyboardActionListener{
    private View candidates;
    private KeyboardView kv;
    private Keyboard keyboard;

    private  boolean isCaps = false;
    private boolean isCapsOnce = false;
    private  boolean isNumpad = false;
    private  boolean isEnglish = false;

    static final int KEYCODE_LANGUAGE_SWITCH = -101;
    static final int KEYCODE_SYMBOL1 = -201;
    static final int KEYCODE_SYMBOL2 = -202;
    static final int KEYCODE_CAPS = -102;
    //static final int KEYCODE_KEYBOARD_SWITCH = -2;

    //Press Ctrl+O

    @Override public void onText(CharSequence text) {       //Possible Solution to keyOutputText
        InputConnection ic = getCurrentInputConnection();
        ic.commitText(text, 0);
    }



    @Override
    public View onCreateInputView() {
        //For SpellChecking Candidates View

        kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard_view,null);
        keyboard = new Keyboard(this,R.xml.ngwerty);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        return kv;
    }

    @Override
    public void onPress(int i) {
        //BUG Commented
        /*InputConnection ic = getCurrentInputConnection();
        playClick(i);
        switch (i) {
            case Keyboard.KEYCODE_SHIFT:
                isCaps = !isCaps;
                keyboard.setShifted(isCaps);
                kv.invalidateAllKeys();
                System.out.println("Caps hold");
                break;
            case 32:
                System.out.println("Spacebar hold");
                break;
        }*/
        //ic.commitText(String.valueOf(i), 1);
    }

    @Override
    public void onRelease(int i) {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onKey(int i, int[] ints) {
        /*if(i==239 && isCaps) i=207;
        if(i==241 && isCaps) i=209;*/
        InputConnection ic = getCurrentInputConnection();
        playClick(i);
        switch (i) {
            case KEYCODE_SYMBOL1:
                isEnglish=true;
                keyboard = new Keyboard(this, R.xml.symbols);
                kv.setKeyboard(keyboard);
                kv.setOnKeyboardActionListener(this);
                break;
            case KEYCODE_SYMBOL2:
                isEnglish=true;
                keyboard = new Keyboard(this, R.xml.symbols_shift);
                kv.setKeyboard(keyboard);
                kv.setOnKeyboardActionListener(this);
                break;
            case Keyboard.KEYCODE_MODE_CHANGE:
                if (!isNumpad) {
                    keyboard = new Keyboard(this, R.xml.numpad);
                    kv.setKeyboard(keyboard);
                    kv.setOnKeyboardActionListener(this);
                    isNumpad = true;
                } else {
                    keyboard = new Keyboard(this, R.xml.ngwerty);
                    kv.setKeyboard(keyboard);
                    kv.setOnKeyboardActionListener(this);
                    isNumpad = false;
                }
                break;
            case KEYCODE_LANGUAGE_SWITCH:
                if (!isEnglish) {
                    keyboard = new Keyboard(this, R.xml.qwerty);
                    kv.setKeyboard(keyboard);
                    kv.setOnKeyboardActionListener(this);
                    isEnglish = true;
                } else {
                    keyboard = new Keyboard(this, R.xml.ngwerty);
                    kv.setKeyboard(keyboard);
                    kv.setOnKeyboardActionListener(this);
                    isEnglish = false;
                }
                break;
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1, 0);
                break;
            /*case KEYCODE_CAPS:
                if(!isCaps){
                    System.out.println("Caps");
                    isCaps = true;
                    isCapsOnce = false;
                    keyboard.setShifted(true);
                    kv.invalidateAllKeys();
                }
                break;*/
            case Keyboard.KEYCODE_SHIFT:
                System.out.println("ShiftOnce");
                if(isCaps){
                    isCaps = false;
                    keyboard.setShifted(false);
                    kv.invalidateAllKeys();
                }else{
                    isCaps = true;
                    isCapsOnce=true;
                    keyboard.setShifted(true);
                    kv.invalidateAllKeys();
                }
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            default:
                char code = (char) i;
                if (Character.isLetter(code) && isCaps)
                    code = Character.toUpperCase(code);

                ic.commitText(String.valueOf(code),1);

                if(isCapsOnce){
                    isCaps = false;
                    isCapsOnce=false;
                    keyboard.setShifted(false);
                    kv.invalidateAllKeys();
                    break;
                }
        }

    }

    private void playClick(int i) {

        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        switch(i)
        {
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default: am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

    /*@Override
    public void onText(CharSequence charSequence) {

    }*/

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}
