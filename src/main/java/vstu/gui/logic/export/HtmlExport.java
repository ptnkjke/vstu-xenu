package vstu.gui.logic.export;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import vstu.gui.data.ParserFilter;
import vstu.gui.forms.main.UrlData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

/**
 * Created by Lopatin on 30.03.2014.
 */
public class HtmlExport {

    public void createResultDoc(List<UrlData> datas, File outputFile) {
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty("resource.loader", "class");
        ve.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

        FileWriter writer = null;
        try {
            writer = new FileWriter(outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        VelocityContext vc = new VelocityContext();
        vc.put("datas", datas);
        vc.put("count", datas.size());
        vc.put("searchList", ParserFilter.searchList);
        vc.put("tagList", ParserFilter.tagList);

        Template t = ve.getTemplate("/vstu/gui/logic/export/html_template.vm");

        t.merge(vc, writer);

        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
