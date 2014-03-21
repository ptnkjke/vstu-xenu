package vstu.gui.listeners;

import vstu.gui.forms.MainForm;
import vstu.gui.logic.Parser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by danila on 20.03.2014.
 */
public class StartButtonListener implements ActionListener {
static Boolean StatusBtn=true;
    @Override
    public void actionPerformed(ActionEvent e) {
        MainForm.getInstance().getTableModel().getDataVector().clear();
        String url = MainForm.getInstance().getUrlTextField().getText();

        if(StatusBtn)
        {
            try{
                StatusBtn=false;
                Parser.startCheck(url);
                MainForm.getInstance().SetButtonTitle("Stop");


            }
            catch (Exception ex)
            {
                ex.fillInStackTrace();
            }
        }
        else
        {
            Parser.stop();
            Parser.checkedUrlList.clear();
            StatusBtn=true;
            MainForm.getInstance().SetButtonTitle("Start");

        }
    }
}
