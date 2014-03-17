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

/**
 * Created by Lopatin on 17.03.14.
 */
public class MainForm extends JFrame {
    private JTextField urlTextField;
    private JButton startButton;
    private JTable tableJTable;
    private JPanel panel;
    private JPanel tablePanel;

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

            DefaultTableModel dtm = (DefaultTableModel) tableJTable.getModel();
            dtm.getDataVector().clear();
            String url = urlTextField.getText();

            // TODO: Запихнуть в отедльный поток
            try {
                Connection.Response response = Jsoup.connect(url).execute();
                Document doc = Jsoup.connect(url).get();

                Elements links = doc.select("a[href]");
                for (Element el : links) {
                    //System.out.println(el.attr("abs:href"));
                    String url_l = el.attr("abs:href");
                    try {
                        Connection.Response t = Jsoup.connect(url_l).execute();
                        int code = t.statusCode();
                        int bytes = t.bodyAsBytes().length;
                        dtm.addRow(new Object[]{url_l, code, "", bytes, ""});
                    } catch (Exception exception) {

                    }
                }


            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
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