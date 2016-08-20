package demo.resicommcomplex;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Created by cabbage on 29/7/16.
 */
public class ResidentialCommercialAppartmentNumberFloor extends AppCompatActivity {

    ListView listView;

    String expressionFloorData;
    String expressionFlatData;
    String titleData;
    Intent i;
    String filename ;
    String aDataRow = "";
    String index, name;
    String typeFloor;
    TextView title;
    File myFile,myDirectory;
    String exp;
    String aBuffer = "", newBuffer = "";
    GetData getDataClass;
    MyAdapterFloor myAdapterFloor;
    MyAdapterShopAndFlat myAdapterShopAndFlat;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resi_comm_app_floors);
        i = getIntent();
        index = i.getStringExtra("index");
        name = i.getStringExtra("name");
        aBuffer = i.getStringExtra("data");

        myDirectory = new File(Environment.getExternalStorageDirectory(), "SiteData");
        myFile = new File(myDirectory + "/shreehari.xml");
        filename=myFile.toString();

        title = (TextView) findViewById(R.id.title_residencial_appartment_foolrs_list_tv);

        listView = (ListView) findViewById(R.id.residencial_app_floor_listview);


        /////////////////////////////////
        titleData = name + " " + index;
        //System.out.println("title ========" + titleData);
        title.setText(titleData.toUpperCase());

        expressionFloorData = "/root/wing[@wingname='" + titleData + "']/floor";
        getDataClass = new GetData();

        getDataClass.getFloors(expressionFloorData, aBuffer);

        myAdapterFloor = new MyAdapterFloor();
        listView.setAdapter(myAdapterFloor);

    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(ResidentialCommercialAppartmentNumberFloor.this, MainActivity.class);
        finish();
        startActivity(setIntent);
    }

    // ///////////////////Get data about floors only and inflat on list view///////////////////////
    public class MyAdapterFloor extends BaseAdapter {

        class viewContainer {
            TextView floorNumber;
            MyGridView gridView;
            //GridView originalgridView;
        }

        public int getCount() {
            return getDataClass.floors.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View view, ViewGroup parent) {

            final viewContainer viewCont;

            if (view == null) {
                viewCont = new viewContainer();
                LayoutInflater li = ResidentialCommercialAppartmentNumberFloor.this.getLayoutInflater();
                view = li.inflate(R.layout.resi_comm_app_floor_item, null);

                viewCont.floorNumber = (TextView) view
                        .findViewById(R.id.floor_item_tv);

                viewCont.gridView = (MyGridView) view.findViewById(R.id.floor_item_gridview);

                view.setTag(viewCont);

            } else {
                viewCont = (viewContainer) view.getTag();
            }

            viewCont.floorNumber.setText(getDataClass.floors.get(position).getFloorNumber().toUpperCase());

            //System.out.println("===============Floor Data=====" + getDataClass.floors.get(position).getFloorNumber().toUpperCase());

            viewCont.floorNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewCont.floorNumber.setText(getDataClass.floors.get(position).getFloorNumber().toUpperCase());

                    //System.out.println("===============Floor Data=====" + getDataClass.floors.get(position).getFloorNumber().toUpperCase());

                    typeFloor = getDataClass.floors.get(position).getFloorType();

                    expressionFlatData = expressionFloorData;

                    expressionFlatData += "[@number='" + getDataClass.floors.get(position).getFloorNumber() + "']/flat";

                    getDataClass.getShopsAndFlats(expressionFlatData, aBuffer);

                    myAdapterShopAndFlat = new MyAdapterShopAndFlat();
                    viewCont.gridView.setAdapter(myAdapterShopAndFlat);
                }
            });

            typeFloor = getDataClass.floors.get(position).getFloorType();

            expressionFlatData = expressionFloorData;

            expressionFlatData += "[@number='" + getDataClass.floors.get(position).getFloorNumber() + "']/flat";

            getDataClass.getShopsAndFlats(expressionFlatData, aBuffer);

            myAdapterShopAndFlat = new MyAdapterShopAndFlat();
            viewCont.gridView.setAdapter(myAdapterShopAndFlat);

            ///////////////////////////////

            return view;
        }
    }
    //////////////////////get data about flat and shop per floors/////////////////

    public class MyAdapterShopAndFlat extends BaseAdapter {

        class viewContainer {
            TextView aNumber;
        }

        public int getCount() {
            //System.out.println("==========Size====" + getDataClass.flats.size());
            return getDataClass.flats.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View view, ViewGroup parent) {

            final viewContainer viewCont;

            if (view == null) {
                viewCont = new viewContainer();
                LayoutInflater li = ResidentialCommercialAppartmentNumberFloor.this.getLayoutInflater();
                view = li.inflate(R.layout.textview, null);

                viewCont.aNumber = (TextView) view
                        .findViewById(R.id.flats_number_tv_item);

                view.setTag(viewCont);

            } else {
                viewCont = (viewContainer) view.getTag();
            }

            viewCont.aNumber.setText(getDataClass.flats.get(position).getFlatnumber());

            //System.out.println("=============Position======="+position);
            //System.out.println("========Flat Data Test===="+getDataClass.flats.get(position).getFlatnumber());

            String Status = getDataClass.flats.get(position).getFlatstatus();

            if (typeFloor.equals("comm")) {
                if (Status.equals("available")) {
                    viewCont.aNumber.setBackgroundResource(R.drawable.shapeshopgreen);
                } else {
                    viewCont.aNumber.setBackgroundResource(R.drawable.shapeshopred);
                }
            }

            if (typeFloor.equals("resi")) {
                if (Status.equals("available")) {
                    viewCont.aNumber.setBackgroundResource(R.drawable.shapeflatgreen);
                } else {
                    viewCont.aNumber.setBackgroundResource(R.drawable.shapeflatred);
                }
            }

            viewCont.aNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int FlatNumber = Integer.parseInt(viewCont.aNumber.getText().toString());
                    exp = expressionFloorData + "[@number='" + String.valueOf(FlatNumber / 100) + "']/flat[@number='" + FlatNumber + "']";
                    //System.out.println("=========Expression=======" + exp);
                    updateFile();

                }
            });

            return view;
        }
    }

    ////////////////////////////
    //update file
    private void updateFile() {
        try {
            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;

            dBuilder = dbFactory.newDocumentBuilder();

            BufferedReader br = new BufferedReader(new StringReader(aBuffer));
            InputSource is = new InputSource(br);

            Document doc = dBuilder.parse(is);

            doc.getDocumentElement().normalize();

            XPath xPath = XPathFactory.newInstance().newXPath();

            //System.out.println("========Expression====" + expression1);

            NodeList nodeList = (NodeList) xPath.compile(exp).evaluate(doc, XPathConstants.NODESET);

            //System.out.println("===Node Length====" + nodeList.getLength());
            for (int i = 0; i < nodeList.getLength(); i++) {

                Node nNode = nodeList.item(i);
                /*System.out.println("\n===Current Element :"
                        + nNode.getNodeName());*/

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    eElement.setAttribute("status", "sold");

                }
            }

            String abc = toString(doc);

            writeToFile(abc);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    //////convert document object to string object
    public static String toString(Document doc) {
        try {
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            return sw.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error converting to String", ex);
        }
    }

    /////////////////Read file
    private void readFromFile() {
        try {

            //File myFile = new File(filename);
            //Log.e("======File Path====", myFile.getAbsolutePath().toString());
            if (myFile.exists()) {
                FileInputStream fIn = new FileInputStream(myFile);
                BufferedReader myReader = new BufferedReader(
                        new InputStreamReader(fIn));

                while ((aDataRow = myReader.readLine()) != null) {
                    newBuffer += aDataRow + "\n";
                }
                myReader.close();

                // System.out.println("=====data=====" + newBuffer);
                //System.out.println("==============================================================");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ////////////////////write file////
    private void writeToFile(String data) {
        try {

            //System.out.println("=============File Path to update===="+filename);

            FileOutputStream fileOutputStream = new FileOutputStream(filename);
            BufferedWriter myReader = new BufferedWriter(
                    new OutputStreamWriter(fileOutputStream));
            myReader.write(data);
            myReader.close();
            //System.out.println("=======================================================");
            readFromFile();
            //System.out.println("=================Data=====================");
            Toast.makeText(getApplicationContext(), "Data Updated", Toast.LENGTH_LONG).show();

            Intent iSend = new Intent(getApplicationContext(), ResidentialCommercialAppartmentNumberFloor.class);
            iSend.putExtra("index", index);
            iSend.putExtra("name", name);
            iSend.putExtra("data", newBuffer);
            finish();
            startActivity(iSend);

        } catch (IOException e) {

            Toast.makeText(getApplicationContext(), "Data Not Updated", Toast.LENGTH_LONG).show();
            Intent iSend = new Intent(getApplicationContext(), ResidentialCommercialAppartmentNumberFloor.class);
            iSend.putExtra("index", index);
            iSend.putExtra("name", name);
            iSend.putExtra("data", newBuffer);
            finish();
            startActivity(iSend);

        }

    }


}

