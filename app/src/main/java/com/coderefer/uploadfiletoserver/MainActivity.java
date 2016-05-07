package com.coderefer.uploadfiletoserver;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String selectedFilePath = "/storage/emulated/0/Download/Alan Walker - Faded.mp3";
    private String urlSong = "";
    Button bUpload;
    Button bReproduce;
    Button bStop;
    TextView tvFileName;
    ProgressDialog dialog;
    Audio audio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bUpload = (Button) findViewById(R.id.b_upload);
        bReproduce = (Button) findViewById(R.id.b_reproduce);
        bStop = (Button) findViewById(R.id.b_stop);
        tvFileName = (TextView) findViewById(R.id.tv_file_name);
        audio = new Audio();

    }

    public void Upload(View view) {
        //SELECTEDFILEPATH ES LA RUTA DONDE SE ENCUENTRA EL ARCHIVO DE VOZ
        if (selectedFilePath != null) {
            //ESTE DIALOG, SALE Y SOLO ESPERA QUE LA ACCION DEL UPLOADFILE TERMINE, Y A ESE METODO
            // LE MANDO EL DIALOG PARA QUE CUANDO COMPLETE TODO, ESTE LO DESAPARESCA =)
            dialog = ProgressDialog.show(MainActivity.this, "", "Uploading File..." + selectedFilePath, true);

            new Thread(new Runnable() {
                @Override
                public void run() {

                try {
                    //RETORNA LA URL DE LA CANCION QUE ES SUBIDA AL SERVIDOR, SI SUCEDE ALGUN ERROR DEBE BOTAR VACIO = ""
                    urlSong = audio.uploadFile(selectedFilePath, dialog);
                } catch (OutOfMemoryError e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Insufficient Memory!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.dismiss();
                }

                }
            }).start();
        } else {
            Toast.makeText(MainActivity.this, "Please choose a File First", Toast.LENGTH_SHORT).show();
        }
    }

    public void Reproduce(View view) {
        if (!urlSong.equals("")) {
            dialog = ProgressDialog.show(MainActivity.this, "", "Download Song...", true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    audio.reproduce(urlSong, dialog);
                }
            }).start();

        } else {
            Toast.makeText(this, "no hay archivo que reproducir", Toast.LENGTH_SHORT).show();
        }
    }

    public void Stop(View view) {
        audio.Stop();
    }
}
