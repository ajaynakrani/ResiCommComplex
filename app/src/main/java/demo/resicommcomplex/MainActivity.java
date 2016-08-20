package demo.resicommcomplex;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by cabbage on 29/7/16.
 */
public class MainActivity extends AppCompatActivity {

    //X>455 && X<575 && Y<1185 && Y>390
    //String filename1 = "/sdcard/SiteData/shreehari.xml";
    String aDataRow = "";
    String aBuffer = "";
    int minX[] = new int[3];
    int minY[] = new int[3];
    int maxX[] = new int[3];
    int maxY[] = new int[3];
    int maxXxx = 1080, maxYxx = 1383;
    boolean mainFlag = false;
    int width, height;
    int flag = 1;
    private int REQUEST_CODE = 23;
    File myDirectory;
    File myFile;
    //int X, Y, minX = 455, minY = 390, maxX = 575, maxY = 1185;
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = getIntent();

        //final TextView textView = (TextView) findViewById(R.id.textview);
        ll = (LinearLayout) findViewById(R.id.ll);

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)/*||
                    ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED*/ {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE);
                Toast.makeText(getApplicationContext(), "Welcome To Shreehari Site", Toast.LENGTH_LONG).show();
            } else {
                checkFolderAndFile();
                Toast.makeText(getApplicationContext(), "Welcome To Shreehari Site", Toast.LENGTH_LONG).show();
            }

        } else {
            checkFolderAndFile();
            Toast.makeText(getApplicationContext(), "Welcome To Shreehari Site", Toast.LENGTH_LONG).show();
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
    /////////////////////////////////////////////

    ////////////////////////////This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == REQUEST_CODE){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                //Displaying a toast
                //Toast.makeText(this,"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
                checkFolderAndFile();

            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(this,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }
    ////////////////////////////////////////////////////////


    //////////////////////////folder and file check exist or not
    private void checkFolderAndFile() {

        myDirectory = new File(Environment.getExternalStorageDirectory(), "SiteData");
        myFile = new File(myDirectory + "/shreehari.xml");
        //System.out.println("======Path====="+myDirectory);
        ////////////////////////////////////////////

        String state;
        state = Environment.getExternalStorageState();

        //System.out.println("========State====="+state);

        if (Environment.MEDIA_MOUNTED.equals(state)) {

            // myDirectory = new File("/sdcard/SiteData/");
            //System.out.println("=====folder path=====" + myDirectory);

            if (myDirectory.exists() && myDirectory.isDirectory()) {

                if (myFile.exists()) {
                    mainFlag=true;
                    readXmlFile();
                } else {
                    CopyAssets();
                    readXmlFile();
                }

            } else {
                folderCreate();/////if not exist folder call this method
            }
            //Toast.makeText(getApplicationContext(), aBuffer, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "SDCARD Not Found !!!", Toast.LENGTH_LONG).show();
        }
    }

/////////////////////////////////////////////////

//////////////////////////////////Read Xml and store in string var
        private void readXmlFile () {
            FileInputStream fIn = null;
            try {
                fIn = new FileInputStream(myFile);
                BufferedReader myReader = new BufferedReader(
                        new InputStreamReader(fIn));

                while ((aDataRow = myReader.readLine()) != null) {
                    aBuffer += aDataRow + "\n";
                }
                myReader.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //System.out.println("=====data=====" + aBuffer);
        }
///////////////////////////////////////////////

    /////////////////////////////////Folder create in internal storage
        private void folderCreate () {
            //myDirectory.mkdirs();
            boolean success = myDirectory.mkdir();

            if (success) {

                //System.out.println("======Created folder=======");

                Toast.makeText(MainActivity.this, "SiteData Folder Created Automatically", Toast.LENGTH_LONG).show();

                CopyAssets();
                readXmlFile();

                Toast.makeText(getApplicationContext(), "Data File Copy In Storage !!!", Toast.LENGTH_LONG).show();
            } else {
                //System.out.println("======failed to Create Directory=======");

                Toast.makeText(MainActivity.this, "SiteData Folder Not Created", Toast.LENGTH_LONG).show();
            }
        }
    /////////////////////////////////////////

////////////////////////////////////////////back button event
        @Override
        public void onBackPressed () {
            finish();
        }
    //////////////////////////////////////////
////////////////////////////////Handle touch event
        public void trackTouch ( int touchX, int touchY, int minX[], int minY[], int maxX[],
        int maxY[]){
            ///////////////////

            if (mainFlag == true) {
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
                Toast.makeText(getApplicationContext(), "'Data File Not Found for Your Site!!!", Toast.LENGTH_LONG).show();
                //mainFlag = false;
            }

            /////////////////////////////
        }
//////////////////////////////////

    /////////////////////////Copy assets file
        private void CopyAssets () {
            AssetManager assetManager = getAssets();
            String[] files = null;
            try {
                files = assetManager.list("DataFile");
            } catch (IOException e) {
                Log.e("tag", e.getMessage());
            }

            for (String filename : files) {
                //System.out.println("File name => " + filename);
                InputStream in = null;
                OutputStream out = null;
                try {
                    in = assetManager.open("DataFile/" + filename);   // if files resides inside the "Files" directory itself
                    out = new FileOutputStream(myFile.toString());
                    copyFile(in, out);
                    in.close();
                    out.flush();
                    out.close();
                    mainFlag=true;
                } catch (Exception e) {
                    //Toast.makeText(getApplicationContext(), "Data File not Copy in Your Storage !!!", Toast.LENGTH_LONG).show();
                }
            }
        }
/////////////////////////////////////////////////////////

    //////////////////////////////////////Copy file in internal storage
        private void copyFile (InputStream in, OutputStream out)throws IOException {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
                /*Intent intent = new Intent(MainActivity.this, MainActivity.class);
                finish();
                startActivity(intent);*/
            }
        }
    }