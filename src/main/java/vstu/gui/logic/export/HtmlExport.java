package vstu.gui.logic.export;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;

/**
 * Created by Lopatin on 30.03.2014.
 */
public class HtmlExport {

    public void createResultDoc() {
        VelocityEngine ve = new VelocityEngine();

        StringWriter writer = new StringWriter();

        VelocityContext vc = new VelocityContext();

        Template t = ve.getTemplate("html_template.vm");

        t.merge(vc, writer );
    }
}
