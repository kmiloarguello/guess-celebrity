package xyz.camiloarguello.guessthecelebrity;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ImageView downloadedImage;
    String r;
    ArrayList<String> djsURls = new ArrayList<>();
    ArrayList<String> djsNames = new ArrayList<>();
    int chosenDJ = 0;
    int locationCorrectAnswer = 0; // between 0 and 3
    String[] answers = new String[4];
    Button buttonRta1;
    Button buttonRta2;
    Button buttonRta3;
    Button buttonRta4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloadedImage = findViewById(R.id.imageView);
        buttonRta1 = findViewById(R.id.rta1);
        buttonRta2 = findViewById(R.id.rta2);
        buttonRta3 = findViewById(R.id.rta3);
        buttonRta4 = findViewById(R.id.rta4);

        DownloadTask task = new DownloadTask();

        try {
            // Request the html for the
            r = task.execute("https://djmag.com/top100dj?year=2015").get();

            // Split html code received with 2 parts before and after of following section
            String[] splittedBySection = r.split("<section class=\"top100dj--newlayout\">");
            Pattern p = Pattern.compile("<img src=\"(.*?)\"");
            Matcher m = p.matcher(splittedBySection[1]);

            while(m.find()) {

                // Adding each image url to arraylist
                djsURls.add(m.group(1));

            }
            // For ALT
            p = Pattern.compile("alt=\"(.*?)\"");

            m = p.matcher(splittedBySection[1]);


            while(m.find()) {
                // Adding names to arraylist
                djsNames.add(m.group(1));
            }

            createNewQuestion();

        } catch (InterruptedException e) {

            e.printStackTrace();
            Log.e("HTML-->", "Process failed or interrupted by unknown causes. :(");

        } catch (ExecutionException e) {

            e.printStackTrace();
            Log.e("HTML-->", "Can't get the HTML content. :(");

        }

    }

    public void chooseDj(View view){
        // if element clicked matchs with the number of location
        // The tag must be an integer from 0 to 3
        if(view.getTag().toString().equals(Integer.toString(locationCorrectAnswer))){
            Toast.makeText(getApplicationContext(),"Correct!", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(), "Wrong! It was " + djsNames.get(chosenDJ), Toast.LENGTH_SHORT).show();
        }
        createNewQuestion();
    }

    public void createNewQuestion(){
        // Create random number to show random dj at once time
        Random random = new Random();
        chosenDJ = random.nextInt(djsURls.size());

        // Using downloadImage class to download the image,
        // which? the urls from Arraylist and
        // what Position? The chosen by random
        DownloadImage imageTask = new DownloadImage();
        Bitmap djImage;

        try {
            djImage = imageTask.execute(djsURls.get(chosenDJ)).get();

            // Update the image on View
            downloadedImage.setImageBitmap(djImage);

            int incorrectAnswerLocation;

            // For 4 possibles answers
            locationCorrectAnswer = random.nextInt(4);
            for (int i=0;i<4;i++){
                if(i == locationCorrectAnswer){
                    answers[i] = djsNames.get(chosenDJ); // Set the real dj name
                }else{
                    // Generate other random number of DJS
                    incorrectAnswerLocation = random.nextInt(djsURls.size());

                    while (incorrectAnswerLocation == chosenDJ){
                        incorrectAnswerLocation = random.nextInt(djsURls.size());
                    }
                    // get the name with location of incorrect answer
                    answers[i] = djsNames.get(incorrectAnswerLocation);
                }
            }

            buttonRta1.setText(answers[0]);
            buttonRta2.setText(answers[1]);
            buttonRta3.setText(answers[2]);
            buttonRta4.setText(answers[4]);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("IMAGE-->", "Can't get the image content. :(");
        }
    }

}
