package com.example.meeting_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class TestTwo extends AppCompatActivity implements RecognitionListener {

    SpeechRecognizer sr;
    Intent recognizerIntent;
    int message;
    int previous_length = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        sr = SpeechRecognizer.createSpeechRecognizer(TestTwo.this);

        sr.setRecognitionListener(this);

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        sr.startListening(recognizerIntent);

    }



        public void restartRecognizer() {
            //sr = null;
            sr = SpeechRecognizer.createSpeechRecognizer(TestTwo.this);
            sr.setRecognitionListener(this);
            sr.startListening(recognizerIntent);

        }



        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int errorCode) {

            switch (errorCode) {

                case SpeechRecognizer.ERROR_AUDIO:

                    message = R.string.error_audio_error;
                    Log.i("ERROR","AUDIO ERROR");

                    break;

                case SpeechRecognizer.ERROR_CLIENT:

                    message = R.string.error_client;
                    Log.i("ERROR","CLIENT ERROR");

                    break;

                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:

                    message = R.string.error_permission;

                    Log.i("ERROR","PERMIT ERROR");

                    break;

                case SpeechRecognizer.ERROR_NETWORK:

                    message = R.string.error_network;

                    Log.i("ERROR","NETWORK ERROR");

                    break;

                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:

                    message = R.string.error_timeout;

                    Log.i("ERROR","TIMEOUT N ERROR");

                    break;

                case SpeechRecognizer.ERROR_NO_MATCH:

                    message = R.string.error_no_match;

                    Log.i("ERROR","NO MATCH ERROR");

                    break;

                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:

                    message = R.string.error_busy;

                    Log.i("ERROR","BUSY ERROR");

                    break;

                case SpeechRecognizer.ERROR_SERVER:

                    message = R.string.error_server;
                    Log.i("ERROR","SERVER ERROR");


                    break;

                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:

                    message = R.string.error_timeout;

                    Log.i("ERROR","TIMEOUT S ERROR");

                    break;

                default:

                    message = R.string.error_understand;
                    Log.i("ERROR","UNDERSTAND ERROR");


                    break;
            }
            Log.i("IMFO","INIT");

            sr.destroy();
            restartRecognizer();

        }

        @Override
        public void onEvent(int arg0, Bundle arg1) {
        }

        @Override
        public void onPartialResults(Bundle arg0) {
            ArrayList<String> matches = arg0.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            for(String s : matches) {
                if(s.compareTo("") != 0) {
                    if(s.length() > previous_length + 5) {
                        Toast.makeText(TestTwo.this, "P : " + s, Toast.LENGTH_SHORT).show();
                        previous_length = s.length();
                        break;
                    }
                }
            }
        }

        @Override
        public void onReadyForSpeech(Bundle arg0) {
        }

        @Override
        public void onResults(Bundle results) {

            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            for(String s : matches) {
                Toast.makeText(TestTwo.this, s , Toast.LENGTH_LONG).show();
                previous_length = 0;
                break;
            }
            Log.i("INFO","INSIDE");

            sr.destroy();
            restartRecognizer();
        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }


    }
