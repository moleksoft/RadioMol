package com.example.petr.radiomol.data;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 1.8.17.
 */

public class XMLPullParserHandler {

    List<DataRadia> dataRadias;

    private DataRadia dataRadia;

    private String text;


    public List<DataRadia> getDataRadias(){
        return dataRadias;
    }


    public XMLPullParserHandler(){
        dataRadias = new ArrayList<DataRadia>();
    }





    public List<DataRadia> parse(InputStream is){

        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;

        try{
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);

            parser = factory.newPullParser();
            parser.setInput(is, null);
            int typ = parser.getEventType();

            while(typ != XmlPullParser.END_DOCUMENT)
            {
                String tagname = parser.getName();
                switch (typ) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("radio")) {
                            dataRadia = new DataRadia();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if(tagname.equalsIgnoreCase("radio"))
                        {
                            dataRadias.add(dataRadia);
                        }
                        else if(tagname.equalsIgnoreCase("stanice"))
                        {
                            dataRadia.setStanice(text);
                        }
                        else if(tagname.equalsIgnoreCase("vysilac"))
                        {
                            dataRadia.setVysilac(text);
                        }
                        else if(tagname.equalsIgnoreCase("web"))
                        {
                            dataRadia.setWww(text);
                        }
                        else if(tagname.equalsIgnoreCase("facebook"))
                        {
                            dataRadia.setFacebook(text);
                        }
                        else if(tagname.equalsIgnoreCase("call"))
                        {
                            dataRadia.setCall(text);
                        }
                        else if(tagname.equalsIgnoreCase("car"))
                        {
                            dataRadia.setCar(text);
                        }
                        else if(tagname.equalsIgnoreCase("speed32"))
                        {
                            dataRadia.setSpeed32(text);
                        }
                        else if(tagname.equalsIgnoreCase("speed64"))
                        {
                            dataRadia.setSpeed64(text);
                        }
                        else if(tagname.equalsIgnoreCase("speed128"))
                        {
                            dataRadia.setSpeed128(text);
                        }
                        break;
                    default:
                        break;
                }

                typ = parser.next();
            }
        }
        catch (Exception e){
        }

        return dataRadias;
    }
}
