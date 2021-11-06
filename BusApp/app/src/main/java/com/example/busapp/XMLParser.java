package com.example.busapp;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * TODO
 *      1. 더 Generic한 Parser
 *      2. parser.getNextText() 활용
 */
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
        if(xmlStream.markSupported()) xmlStream.reset(); // stream mark를 0으로
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

    public String[] getElementByName(String name, boolean recursive) {
        try{
            if(xmlStream.markSupported()) xmlStream.reset();
            parser.setInput(new InputStreamReader(xmlStream));
            if(!recursive) {
                ArrayList<String> result = new ArrayList<>();
                while(parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                    if(parser.getEventType() == XmlPullParser.START_TAG) {
                        if(name.equals(parser.getName())) {
                            result.add(parser.nextText());
                        }
                    }
                    parser.next();
                }
                return result.toArray(new String[0]);
            } else {
                ArrayList<String> items = new ArrayList<>();

                boolean tagStarted = false; // 탐색 태그 오픈되었는지 확인
                String tag = null; // 탐색된 태그의 이름 <string-arrray>의 경우 string-array
                while(parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                    // START_TAG 와 END_TAG 에 대해서만 동작
                    if(parser.getEventType() != XmlPullParser.START_TAG && parser.getEventType() != XmlPullParser.END_TAG) {
                        parser.next(); continue;
                    }

                    String current_tag = null;
                    String current_tagName = null;
                    if(parser.getEventType() == XmlPullParser.START_TAG) {
                        current_tagName = parser.getAttributeValue(null, "name");
                        current_tag = parser.getName();
                    } else {
                        current_tagName = current_tag = parser.getName();
                    }
                    if (current_tagName == null) { // TAG의 name 속성이 없는 경우
                        if(tagStarted) { // 이미 TAG를 찾았을 수도 있음 (Recursive)
                            String text = parser.nextText();
                            items.add(text);
                        }
                    } else { // Tag의 name 속성이 있는 경우
                        if (current_tagName.equals(name) || (tag != null && current_tag.equals(tag))) {
                            tag = current_tag;
                            if(tagStarted) return items.toArray(new String[0]);
                            else tagStarted = true;
                        }
                    }
                    parser.next();
                }
                return items.toArray(new String[0]);
            }
        } catch (Exception e) {
            Log.e("[getElementByName]", e.toString());
        }
        return null;
    }

}
