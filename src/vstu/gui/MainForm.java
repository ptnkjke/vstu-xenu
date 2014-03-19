package vstu.gui;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Lopatin on 17.03.14.
 */
public class MainForm extends JFrame {
    private JTextField urlTextField;
    private JButton startButton;
    private JTable tableJTable;
    private JPanel panel;
    private JPanel tablePanel;
    Document doc;
    Elements links;
    DefaultTableModel dtm;
    int code;
    int bytes;
    String url_l;
    private static final String TITLE = "VSTU XENU";

    public MainForm() {
        startButton.addActionListener(new StartButtonListener());
        addWindowListener(new FormWindowListener());


        DefaultTableModel dtm = new DefaultTableModel();

        tableJTable = new JTable();
        JScrollPane jsp = new JScrollPane(tableJTable);   // Чтобы у таблицы была шапка, вот так таблицу оборачивают. 
        tablePanel.setLayout(new BorderLayout());         // С помощью редактора формы напрямую JScrollPane не поместить, и почему-то layout не инициализируется 
        tablePanel.add(jsp);

        dtm.addColumn("Address");
        dtm.addColumn("Status");
        dtm.addColumn("Type");
        dtm.addColumn("Size");
        dtm.addColumn("Title"); 
  
/*        dtm.addRow(new Object[]{"1", "1", "1", "1", "1"});*/

        tableJTable.setModel(dtm);

        setContentPane(panel);
        pack();
        setTitle(TITLE);
        setVisible(true);
    }


    /**
     * Слушатель для кнопки
     */
    private class StartButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            dtm = (DefaultTableModel) tableJTable.getModel();
            dtm.getDataVector().clear();
            String url = urlTextField.getText();

            // TODO: Запихнуть в отедльный поток
            try {
                    Connection.Response response = Jsoup.connect(url).execute();
                    doc = Jsoup.connect(url).get();
                    links = doc.select("a[href]");
                    ImagesLoad();




            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
        public void ImagesLoad() {

            Thread bacgraund = new Thread(new Runnable() {
                public void run() {
                    try {
                        for (Element el : links) {

                             url_l = el.attr("abs:href");
                            try {
                                Connection.Response t = Jsoup.connect(url_l).execute();
                                code = t.statusCode();
                                bytes = t.bodyAsBytes().length;
                                SetRssItems.run();


                            } catch (Exception exception) {
                                exception.printStackTrace();

                            }
                        }

                        }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                }});
            bacgraund.start();
        }
        Runnable SetRssItems = new Runnable() {
            public void run(){
                dtm.addRow(new Object[]{url_l, code, "", bytes, ""});
            }};


    }

    private class FormWindowListener implements WindowListener {

        @Override
        public void windowOpened(WindowEvent e) {

        }

        @Override
        public void windowClosing(WindowEvent e) {

        }

        @Override
        public void windowClosed(WindowEvent e) {
            System.exit(0);      // Чтобы приложение после закрытия окна не висело
        }

        @Override
        public void windowIconified(WindowEvent e) {

        }

        @Override
        public void windowDeiconified(WindowEvent e) {

        }

        @Override
        public void windowActivated(WindowEvent e) {

        }

        @Override
        public void windowDeactivated(WindowEvent e) {

        }
    }
} 