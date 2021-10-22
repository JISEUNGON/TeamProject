package com.example.busapp;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class XMLParser {
    private XmlPullParser parser;
    private InputStream xmlStream;

    /**
     * @param stream R.raw.~로 넘겨 주시면 됩니다~
     * @throws XmlPullParserException
     */
    public XMLParser(InputStream stream) throws XmlPullParserException {
        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        parser = xmlPullParserFactory.newPullParser();
        xmlStream = stream;
    }

    /**
     * XML 속성(name) 기반으로 탐색합니다.
     * @param name XML 속성 값
     * @return 속성 값이 XML 파일에 없는 경우 NULL
     *         String[] 값들
     * @throws IOException Stream Error
     * @throws XmlPullParserException
     */
    public String[] getElementByName(String name) throws IOException, XmlPullParserException {
        xmlStream.reset(); // stream mark를 0으로
        parser.setInput(new InputStreamReader(xmlStream)); // parser가 stream을 읽도록

        ArrayList<String> items = new ArrayList<>(); // item이 들어갈 String
        int event = parser.getEventType();
        boolean tagOpened = false; // name을 속성 값으로 가지는 TAG가 열렸는지
        while(event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_TAG: // <xml>
                    String tag = parser.getAttributeValue(null, "name");
                    if (tag != null && tag.equals(name)) tagOpened = true;
                    break;
                case XmlPullParser.END_TAG: // </xml>
                    tag = parser.getName();
                    if (tag != null && tag.equals("string-array") && tagOpened) return items.toArray(new String[0]);
                    break;
                case XmlPullParser.TEXT: // else
                    if (tagOpened && parser.getText().replaceAll("\\W", "").length() > 0) items.add(parser.getText());
                    break;
            }
            event = parser.next();
        }
        return null;
    }
}
