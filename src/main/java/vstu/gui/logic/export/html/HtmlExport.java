package vstu.gui.logic.export.html;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import vstu.gui.data.ParserFilter;
import vstu.gui.forms.main.UrlData;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Lopatin on 30.03.2014.
 */
public class HtmlExport {

    public void createResultDoc(List<UrlData> datas, File outputFile) {
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty("resource.loader", "class");
        ve.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

        Writer writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(outputFile), Charset.forName("UTF-8").newEncoder());
        } catch (IOException e) {
            e.printStackTrace();
        }

        VelocityContext vc = new VelocityContext();
        vc.put("datas", datas);
        vc.put("count", datas.size());
        vc.put("searchList", ParserFilter.searchList);
        vc.put("tagList", ParserFilter.tagList);

        ResourceBundle translationBundle = ResourceBundle.getBundle("translation");
        vc.put("txt_brokenlinks", translationBundle.getString("htmlexport.txt_brokenlinks"));
        vc.put("txt_status", translationBundle.getString("htmlexport.txt_status"));
        vc.put("txt_parent", translationBundle.getString("htmlexport.txt_parent"));
        vc.put("txt_validlinks", translationBundle.getString("htmlexport.txt_validlinks"));
        vc.put("txt_tagsearch", translationBundle.getString("htmlexport.txt_tagsearch"));
        vc.put("txt_textlinks", translationBundle.getString("htmlexport.txt_textlinks"));
        vc.put("txt_statistic", translationBundle.getString("htmlexport.txt_statistic"));
        vc.put("txt_allcount", translationBundle.getString("htmlexport.txt_allcount"));
        vc.put("txt_brokencount", translationBundle.getString("htmlexport.txt_brokencount"));

        Template t = ve.getTemplate("/vstu/gui/logic/export/html/html_template.vm");

        t.merge(vc, writer);

        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
