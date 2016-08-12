package demo.resicommcomplex;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by cabbage on 29/7/16.
 */
public class MainActivity extends AppCompatActivity {

    //X>455 && X<575 && Y<1185 && Y>390
    String filename = "/sdcard/SiteData/shreehari.xml";
    String aDataRow = "";
    String aBuffer = "";
    int minX[] = new int[3];
    int minY[] = new int[3];
    int maxX[] = new int[3];
    int maxY[] = new int[3];
    int maxXxx = 1080, maxYxx = 1383;
    int mainFlag = 1;
    int width, height;
    int flag = 1;
    //int X, Y, minX = 455, minY = 390, maxX = 575, maxY = 1185;
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = getIntent();

        //final TextView textView = (TextView) findViewById(R.id.textview);
        ll = (LinearLayout) findViewById(R.id.ll);
        ////////////////////////////////////////////////////


        ////////////////////////////////////////////
        File myDirectory;
        String state;
        state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            myDirectory = new File("/sdcard/SiteData/");

            if (myDirectory.exists()) {
                try {
                    File myFile = new File(filename);
                    //Log.e("======File Path====", myFile.getAbsolutePath().toString());
                    if (myFile.exists()) {
                        FileInputStream fIn = new FileInputStream(myFile);
                        BufferedReader myReader = new BufferedReader(
                                new InputStreamReader(fIn));

                        while ((aDataRow = myReader.readLine()) != null) {
                            aBuffer += aDataRow + "\n";
                        }
                        myReader.close();
                        //System.out.println("=====data=====" + aBuffer);

                    } else {
                        Toast.makeText(getApplicationContext(), "'shreehari.xml' File Not Found !!!", Toast.LENGTH_LONG).show();
                        mainFlag = 0;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                myDirectory.mkdirs();
                Toast.makeText(MainActivity.this, "SiteData Folder Created Automatically", Toast.LENGTH_LONG).show();
            }
            //Toast.makeText(getApplicationContext(), aBuffer, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "SDCARD Not Found !!!", Toast.LENGTH_LONG).show();
        }


        // this is the view on which you will listen for touch events
        ll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int touchX = (int) event.getX();
                int touchY = (int) event.getY();
                width = ll.getWidth();
                height = ll.getHeight();

                minX[0] = (width * 140) / maxXxx;
                minY[0] = (height * 340) / maxYxx;
                maxX[0] = (width * 390) / maxXxx;
                maxY[0] = (height * 1200) / maxYxx;

                minX[1] = (width * 425) / maxXxx;
                minY[1] = (height * 315) / maxYxx;
                maxX[1] = (width * 615) / maxXxx;
                maxY[1] = (height * 1200) / maxYxx;

                minX[2] = (width * 635) / maxXxx;
                minY[2] = (height * 45) / maxYxx;
                maxX[2] = (width * 900) / maxXxx;
                maxY[2] = (height * 1200) / maxYxx;

                if (flag == 1) {
                    flag = 0;
                    trackTouch(touchX, touchY, minX, minY, maxX, maxY);
                }

                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void trackTouch(int touchX, int touchY, int minX[], int minY[], int maxX[], int maxY[]) {
        ///////////////////

        if (mainFlag == 1) {
            if (touchX > minX[0] && touchY > minY[0] && touchX < maxX[0] && touchY < maxY[0]) {

//            System.out.println("MinX=" + minX[0] + "==MinY=" + minY[0] + "==MaxX=" + maxX[0] + "==MaxY=" + maxY[0]);
                //Toast.makeText(ResidencialAppartmentTrack.this, "Sai 1", Toast.LENGTH_SHORT).show();
                Intent iSend = new Intent(MainActivity.this, ResidentialCommercialAppartmentNumberFloor.class);
                iSend.putExtra("index", "1");
                iSend.putExtra("name", "shreehari");
                iSend.putExtra("data", aBuffer);
                finish();
                startActivity(iSend);
                //////////////////////////
            } else if (touchX > minX[1] && touchY > minY[1] && touchX < maxX[1] && touchY < maxY[1]) {
//            System.out.println("MinX=" + minX[1] + "==MinY=" + minY[1] + "==MaxX=" + maxX[1] + "==MaxY=" + maxY[1]);
                //Toast.makeText(ResidencialAppartmentTrack.this, "Sai 2", Toast.LENGTH_SHORT).show();
                Intent iSend = new Intent(MainActivity.this, ResidentialCommercialAppartmentNumberFloor.class);
                iSend.putExtra("index", "2");
                iSend.putExtra("name", "shreehari");
                iSend.putExtra("data", aBuffer);
                finish();
                startActivity(iSend);
                /////////////////////////////
            } else if (touchX > minX[2] && touchY > minY[2] && touchX < maxX[2] && touchY < maxY[2]) {
//            System.out.println("MinX="+minX[2]+"==MinY="+minY[2]+"==MaxX="+maxX[2]+"==MaxY="+maxY[2]);
                // Toast.makeText(ResidencialAppartmentTrack.this, "Sai 3", Toast.LENGTH_SHORT).show();
                Intent iSend = new Intent(MainActivity.this, ResidentialCommercialAppartmentNumberFloor.class);
                iSend.putExtra("index", "3");
                iSend.putExtra("name", "shreehari");
                iSend.putExtra("data", aBuffer);
                finish();
                startActivity(iSend);
                /////////////////////////////////////
            } else {
                Toast.makeText(MainActivity.this, "Select Any Building from 3 building To Get Info", Toast.LENGTH_SHORT).show();
                flag = 1;
            }
        } else {
            Toast.makeText(getApplicationContext(), "'shreehari.xml' File Not Found !!!", Toast.LENGTH_LONG).show();
        }

        /////////////////////////////
    }
}