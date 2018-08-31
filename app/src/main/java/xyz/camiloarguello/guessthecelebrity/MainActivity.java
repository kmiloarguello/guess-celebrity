package xyz.camiloarguello.guessthecelebrity;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    ImageView downloaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloaded = findViewById(R.id.imageView);

        //https://camiloarguello.xyz/img/compa_juego.png

        /**
         * To download web code
         */
        /*
        String r = null;
        DownloadTask task = new DownloadTask();
        try {

            r = task.execute("https://camiloarguello.xyz").get();

        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();

        }

        Log.i("Content Resultado", r);
        */

    }

    public void downloadImage(View view){

        DownloadImage task = new DownloadImage();
        Bitmap myImage;

        try {
            myImage = task.execute("https://camiloarguello.xyz/img/compa_juego.png").get();

            downloaded.setImageBitmap(myImage);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
