package com.ta.notifikasikecelakaan;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundTaskRegistrasi extends AsyncTask<String, String, String> {
    Context context;
    BackgroundTaskRegistrasi(Context ctx) {
        this.context=ctx;
    }
    @Override
    protected String doInBackground(String... strings) {
        String type = strings[0];
        String nama = strings[1];
        String telp = strings[2];
        String telp_kel = strings[3];
        String pass = strings[4];
        String regURL = "http://web.contohci/registrasiprosesmobile.php";
        if(type.equals("reg")) {
            try {
                URL url = new URL(regURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                String insert_data = URLEncoder.encode("nama", "UTF-8")+"="+URLEncoder.encode(nama, "UTF-8")+
                        "&"+URLEncoder.encode("telp", "UTF-8")+"="+URLEncoder.encode(telp, "UTF-8")+
                        "&"+URLEncoder.encode("telp_kel", "UTF-8")+"="+URLEncoder.encode(telp_kel, "UTF-8")+
                        "&"+URLEncoder.encode("pass", "UTF-8")+"="+URLEncoder.encode(pass, "UTF-8");
                bufferedWriter.write(insert_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "ISO-8859-1");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String result="";
                String line="";
                StringBuilder stringBuilder = new StringBuilder();
                while ((line=bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                result=stringBuilder.toString();
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        //super.onPostExecute(s);
        Toast.makeText(context,s, Toast.LENGTH_LONG).show();
    }
}
