package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.StringReader;
import java.util.Scanner;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class HttpURLConnectionExample {

    public static String date;{}
    public static String codeval;{}

    public static void main(String[] args) throws Exception, XPathExpressionException {


        Scanner in = new Scanner(System.in);

        System.out.print("Введите дату (dd/mm/yyyy): ");
        date = in.nextLine();

        System.out.print("Введите код валюты: ");
        codeval = in.nextLine();

        HttpURLConnectionExample obj = new HttpURLConnectionExample();

        System.out.println("");
        obj.sendGet();

    }


    private void sendGet() throws Exception {

        String url = ("http://www.cbr.ru/scripts/XML_daily.asp?date_req=" + date);
        URL obj = new URL(url);

        HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();

        httpClient.setRequestMethod("GET");

        int responseCode = httpClient.getResponseCode();

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(httpClient.getInputStream()))) {

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }



            NodeList nl = null;
            try {
                DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(new StringReader(response.toString())));
                XPathFactory xPathfactory = XPathFactory.newInstance();
                XPath xpath = xPathfactory.newXPath();

                XPathExpression cod = xpath.compile("//ValCurs/Valute[CharCode='" + codeval +"']/Value/text()");

                nl = (NodeList) cod.evaluate(doc, XPathConstants.NODESET);

                for (int i = 0; i < nl.getLength(); i++) {
                    Node n = nl.item(i);
                    System.out.println("Курс валюты:" + n.getTextContent());
                }
                System.out.println();

            } catch (XPathExpressionException e1) {

                e1.printStackTrace();
            } catch (ParserConfigurationException e1) {

                e1.printStackTrace();
            } catch (SAXException e1) {

                e1.printStackTrace();
            } catch (IOException e1) {

                e1.printStackTrace();
            }

        }
    }
}