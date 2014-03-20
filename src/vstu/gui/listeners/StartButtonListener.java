package vstu.gui.listeners;

import vstu.gui.forms.MainForm;
import vstu.gui.logic.Parser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by danila on 20.03.2014.
 */
public class StartButtonListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        MainForm.getInstance().getTableModel().getDataVector().clear();
        String url = MainForm.getInstance().getUrlTextField().getText();

        Parser.startCheck(url);
    }
}
