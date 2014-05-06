package vstu.gui.logic.export.sitemap;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import vstu.gui.forms.main.UrlData;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lopatin on 27.04.2014.
 */
public class SiteMapGen {

    public void createSiteMap(File output, List<UrlData> datas) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();

        // root element
        Document doc = documentBuilder.newDocument();
        Element root = doc.createElement("urlset");
        root.setAttribute("xmlns", "http://www.sitemaps.org/schemas/sitemap/0.9");

        List<String> contains = new ArrayList<>();

        for (UrlData urlData : datas) {
            if (!contains.contains(urlData.getAddress())) {
                Element url = doc.createElement("url");
                Element loc = doc.createElement("loc");

                // TODO: Требуется некоторая обработка предварительная для url

                loc.setTextContent(urlData.getAddress());

                url.appendChild(loc);

                root.appendChild(url);
            }
        }

        doc.appendChild(root);


        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        DOMSource source = new DOMSource(doc);

        StreamResult result = new StreamResult(output);


        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        transformer.transform(source, result);


    }
}
