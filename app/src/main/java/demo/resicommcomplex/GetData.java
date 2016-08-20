package demo.resicommcomplex;

import android.annotation.TargetApi;
import android.os.Build;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Created by cabbage on 5/8/16.
 */
public class GetData {

    public ArrayList<FloorDataModelClass> floors = new ArrayList<FloorDataModelClass>();
    public ArrayList<FlatDataModelClass> flats = new ArrayList<FlatDataModelClass>();

    @TargetApi(Build.VERSION_CODES.FROYO)
    public void getFloors(String expression, String aBuffer) {

        try {
            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();

            DocumentBuilder dBuilder;

            dBuilder = dbFactory.newDocumentBuilder();

            BufferedReader br = new BufferedReader(new StringReader(aBuffer));
            InputSource is = new InputSource(br);

            //System.out.println("==========IS=========" + is);

            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();

            XPath xPath = XPathFactory.newInstance().newXPath();

            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {

                Node nNode = nodeList.item(i);
                //System.out.println("\nCurrent Element :"
                //      + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    FloorDataModelClass obj = new FloorDataModelClass();

                    //floors.add(i, eElement.getAttribute("fl").toUpperCase());
                    obj.setFloorNumber(eElement.getAttribute("number"));
                    obj.setFloorType(eElement.getAttribute("type"));

                    floors.add(obj);

                    //System.out.println("========Current floor Number : " + floors.get(i).getFloorNumber());
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        Collections.reverse(floors);
        /*for(int j=0;j<floors.size();j++)
            System.out.println("Current floor number: "
                    + floors.get(j));*/
    }

    ///////////////////////////////////////////////////////////////////////////
    @TargetApi(Build.VERSION_CODES.FROYO)
    public void getShopsAndFlats(String expression1, String aBuffer1) {
        flats.clear();
        //System.out.println("====data======"+aBuffer1);
        try {
            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;

            dBuilder = dbFactory.newDocumentBuilder();

            BufferedReader br = new BufferedReader(new StringReader(aBuffer1));
            InputSource is = new InputSource(br);

            Document doc = dBuilder.parse(is);

            doc.getDocumentElement().normalize();

            XPath xPath = XPathFactory.newInstance().newXPath();

            //System.out.println("========Expression====" + expression1);

            NodeList nodeList = (NodeList) xPath.compile(expression1).evaluate(doc, XPathConstants.NODESET);


            //System.out.println("===Node Length====" + nodeList.getLength());
            for (int i = 0; i < nodeList.getLength(); i++) {

                Node nNode = nodeList.item(i);
                /*System.out.println("\n===Current Element :"
                        + nNode.getNodeName());*/

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    FlatDataModelClass obj = new FlatDataModelClass();

                    obj.setFlatnumber(eElement.getAttribute("number"));
                    obj.setFlatstatus(eElement.getAttribute("status"));
                   /* obj.setNumber(eElement.getElementsByTagName("number").item(0).getTextContent());
                    obj.setStatus(eElement.getElementsByTagName("status").item(0).getTextContent());*/

                    flats.add(obj);
                    //System.out.println("====Current Shop or Flat Number : " + obj.getFlatnumber());
                }
                //System.out.println("==========Object Size====="+flats.size());
            }
            //System.out.println("==========new Size=========" + flats.size());

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
    //////////////////////////////////////////////////////////////
}