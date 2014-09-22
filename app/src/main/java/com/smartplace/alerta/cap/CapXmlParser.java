package com.smartplace.alerta.cap;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Roberto on 12/07/2014.
 */
public class CapXmlParser {
    // We don't use namespaces
    private static final String ns = null;

    public static CapFeed parseCapFeed(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }
    private static CapFeed readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {

        CapFeed capFeed = new CapFeed();

        parser.require(XmlPullParser.START_TAG, ns, "feed");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("title")) {
                capFeed.setTitle(readSimpleTag(parser, "title"));
            } else if (name.equals("id")) {
                capFeed.setId(readSimpleTag(parser, "id"));
            }else if (name.equals("rights")) {
                capFeed.setRights(readSimpleTag(parser, "rights"));
            }else if (name.equals("updated")) {
                capFeed.setUpdated(readSimpleTag(parser, "updated"));
            }else if (name.equals("entry")) {
                if(capFeed.getEntries()==null){
                    capFeed.setEntries(new ArrayList<CapEntry>());
                }
                capFeed.getEntries().add(readEntry(parser));
            }else {
                skip(parser);
            }

        }
        return capFeed;
    }
    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
    // Processes title tags in the feed.
    private static String readSimpleTag(XmlPullParser parser,String tag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, tag);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, tag);
        return title;
    }
    private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
   // to their respective "read" methods for processing. Otherwise, skips the tag.
    private static CapEntry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {

        CapEntry capEntry = new CapEntry();

        parser.require(XmlPullParser.START_TAG, ns, "entry");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("id")) {
                capEntry.setId(readSimpleTag(parser,"id"));
            } else if (name.equals("title")) {
                capEntry.setTitle(readSimpleTag(parser, "title"));
            } else if (name.equals("updated")) {
                capEntry.setUpdated(readSimpleTag(parser, "updated"));
            } else if (name.equals("content")) {
                capEntry.setContent(readEntryContent(parser));
            } else {
                skip(parser);
            }
        }
        return  capEntry;
    }
    private static CapEntryContent readEntryContent(XmlPullParser parser) throws XmlPullParserException, IOException {

        CapEntryContent capEntryContent = new CapEntryContent();

        parser.require(XmlPullParser.START_TAG, ns, "content");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("alert")) {
                capEntryContent.setAlert(readCapAlert(parser));
            } else {
                skip(parser);
            }
        }
        return  capEntryContent;
    }
    private static CapAlert readCapAlert(XmlPullParser parser) throws XmlPullParserException, IOException {

        CapAlert capAlert = new CapAlert();

        parser.require(XmlPullParser.START_TAG, ns, "alert");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("identifier")) {
                capAlert.setIdentifier(readSimpleTag(parser, "identifier"));
            }else if (name.equals("sender")) {
                capAlert.setSender(readSimpleTag(parser, "sender"));
            }else if (name.equals("sent")) {
                capAlert.setSent(readSimpleTag(parser, "sent"));
            }else if (name.equals("status")) {
                capAlert.setStatus(readSimpleTag(parser, "status"));
            }else if (name.equals("msgType")) {
                capAlert.setMsgType(readSimpleTag(parser, "msgType"));
            }else if (name.equals("source")) {
                capAlert.setSource(readSimpleTag(parser, "source"));
            }else if (name.equals("scope")) {
                capAlert.setScope(readSimpleTag(parser, "scope"));
            }else if (name.equals("restriction")) {
                capAlert.setRestriction(readSimpleTag(parser, "restriction"));
            }else if (name.equals("addresses")) {
                capAlert.setAddresses(readSimpleTag(parser, "addresses"));
            }else if (name.equals("code")) {
                if(capAlert.getCode()==null){
                    capAlert.setCode(new ArrayList<String>());
                }
                capAlert.getCode().add(readSimpleTag(parser, "code"));
            }else if (name.equals("note")) {
                capAlert.setNote(readSimpleTag(parser, "note"));
            }else if (name.equals("references")) {
                capAlert.setReferences(readSimpleTag(parser, "references"));
            }else if (name.equals("incidents")) {
                capAlert.setReferences(readSimpleTag(parser, "incidents"));
            } else if (name.equals("info")) {
                if(capAlert.getInfo()== null){
                    capAlert.setInfo(new ArrayList<CapAlertInfo>());
                }
                capAlert.getInfo().add(readCapAlertInfo(parser));
            } else {
                skip(parser);
            }
        }
        return  capAlert;
    }
    private static CapAlertInfo readCapAlertInfo(XmlPullParser parser) throws XmlPullParserException, IOException {

        CapAlertInfo capAlertInfo = new CapAlertInfo();

        parser.require(XmlPullParser.START_TAG, ns, "info");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("language")) {
                capAlertInfo.setLanguage(readSimpleTag(parser, "language"));
            }else if (name.equals("category")) {
                if(capAlertInfo.getCategory()==null){
                    capAlertInfo.setCategory(new ArrayList<String>());
                }
                capAlertInfo.getCategory().add(readSimpleTag(parser, "category"));
            }else if (name.equals("event")) {
                capAlertInfo.setEvent(readSimpleTag(parser, "event"));
            }else if (name.equals("responseType")) {
                if(capAlertInfo.getResponseType()==null){
                    capAlertInfo.setResponseType(new ArrayList<String>());
                }
                capAlertInfo.getResponseType().add(readSimpleTag(parser, "responseType"));
            }else if (name.equals("urgency")) {
                capAlertInfo.setUrgency(readSimpleTag(parser, "urgency"));
            }else if (name.equals("severity")) {
                capAlertInfo.setSeverity(readSimpleTag(parser, "severity"));
            }else if (name.equals("certainty")) {
                capAlertInfo.setCertainty(readSimpleTag(parser, "certainty"));
            }else if (name.equals("audience")) {
                capAlertInfo.setAudience(readSimpleTag(parser, "audience"));
            }else if (name.equals("eventCode")) {
                if(capAlertInfo.getEventCode() == null){
                    capAlertInfo.setEventCode(new ArrayList<String>());
                }
                capAlertInfo.getEventCode().add(readSimpleTag(parser, "eventCode"));
            }else if (name.equals("effective")) {
                capAlertInfo.setEffective(readSimpleTag(parser, "effective"));
            }else if (name.equals("onset")) {
                capAlertInfo.setOnset(readSimpleTag(parser, "onset"));
            }else if (name.equals("expires")) {
                capAlertInfo.setExpires(readSimpleTag(parser, "expires"));
            }else if (name.equals("senderName")) {
                capAlertInfo.setSenderName(readSimpleTag(parser, "senderName"));
            }else if (name.equals("headline")) {
                capAlertInfo.setHeadline(readSimpleTag(parser, "headline"));
            }else if (name.equals("description")) {
                capAlertInfo.setDescription(readSimpleTag(parser, "description"));
            }else if (name.equals("instruction")) {
                capAlertInfo.setInstruction(readSimpleTag(parser, "instruction"));
            }else if (name.equals("web")) {
                capAlertInfo.setWeb(readSimpleTag(parser, "web"));
            }else if (name.equals("contact")) {
                capAlertInfo.setContact(readSimpleTag(parser, "contact"));
            }  else if (name.equals("parameter")) {
                if(capAlertInfo.getParameter()== null){
                    capAlertInfo.setParameter(new ArrayList<CapParameter>());
                }
                capAlertInfo.getParameter().add(readCapParameter(parser));
            }else if (name.equals("area")) {
                if(capAlertInfo.getArea()== null){
                    capAlertInfo.setArea(new ArrayList<CapAlertArea>());
                }
                capAlertInfo.getArea().add(readCapAlertArea(parser));
            }else if (name.equals("resource")) {
                if(capAlertInfo.getResource()== null){
                    capAlertInfo.setResource(new ArrayList<CapAlertResource>());
                }
                capAlertInfo.getResource().add(readCapAlertResource(parser));
            }else {
                skip(parser);
            }
        }
        return  capAlertInfo;
    }
    private static CapParameter readCapParameter(XmlPullParser parser) throws XmlPullParserException, IOException {

        CapParameter capParameter = new CapParameter();

        parser.require(XmlPullParser.START_TAG, ns, "parameter");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("value")) {
                capParameter.setValue(readSimpleTag(parser, "value"));
            }else if (name.equals("valueName")) {
                capParameter.setValueName(readSimpleTag(parser, "valueName"));
            }else {
                skip(parser);
            }
        }
        return  capParameter;
    }
    private static CapAlertArea readCapAlertArea(XmlPullParser parser) throws XmlPullParserException, IOException {

        CapAlertArea capAlertArea = new CapAlertArea();

        parser.require(XmlPullParser.START_TAG, ns, "area");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("areaDesc")) {
                capAlertArea.setAreaDesc(readSimpleTag(parser, "areaDesc"));
            }else if (name.equals("circle")) {
                if(capAlertArea.getCircle() == null){
                    capAlertArea.setCircle(new ArrayList<String>());
                }
                capAlertArea.getCircle().add(readSimpleTag(parser, "circle"));
            }else if (name.equals("polygon")) {
                if(capAlertArea.getPolygon() == null){
                    capAlertArea.setPolygon(new ArrayList<String>());
                }
                capAlertArea.getPolygon().add(readSimpleTag(parser, "polygon"));
            }else if (name.equals("geocode")) {
                if(capAlertArea.getGeocode() == null){
                    capAlertArea.setGeocode(new ArrayList<String>());
                }
                capAlertArea.getGeocode().add(readSimpleTag(parser, "geocode"));
            }else if (name.equals("altitude")) {
                capAlertArea.setAltitude(readSimpleTag(parser, "altitude"));
            }else if (name.equals("ceiling")) {
                capAlertArea.setCeiling(readSimpleTag(parser, "ceiling"));
            }else {
                skip(parser);
            }
        }
        return  capAlertArea;
    }
    private static CapAlertResource readCapAlertResource(XmlPullParser parser) throws XmlPullParserException, IOException {

        CapAlertResource capAlertResource = new CapAlertResource();

        parser.require(XmlPullParser.START_TAG, ns, "resource");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("resourceDesc")) {
                capAlertResource.setResourceDesc(readSimpleTag(parser, "resourceDesc"));
            }else if (name.equals("mimeType")) {
                capAlertResource.setMimeType(readSimpleTag(parser, "mimeType"));
            }else if (name.equals("size")) {
                capAlertResource.setSize(readSimpleTag(parser, "size"));
            }else if (name.equals("uri")) {
                capAlertResource.setUri(readSimpleTag(parser, "uri"));
            }else if (name.equals("derefUri")) {
                capAlertResource.setDerefUri(readSimpleTag(parser, "derefUri"));
            }else if (name.equals("digest")) {
                capAlertResource.setDigest(readSimpleTag(parser, "digest"));
            }else {
                skip(parser);
            }
        }
        return  capAlertResource;
    }
}
