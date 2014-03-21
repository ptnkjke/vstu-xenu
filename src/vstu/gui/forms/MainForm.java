package vstu.gui.forms;

import vstu.gui.listeners.FormWindowListener;
import vstu.gui.listeners.StartButtonListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Created by Lopatin on 17.03.14.
 */
public class MainForm extends JFrame {
    private JTextField urlTextField;
    private JButton startButton;
    private JTable tableJTable;
    private JPanel panel;
    private JPanel tablePanel;
    private JCheckBox ImgCheckBox;
    private JCheckBox CssCheckBox;
    private JCheckBox UrlCheckBox;
    private static final String TITLE = "VSTU XENU";

    private static MainForm instance;
    private DefaultTableModel tableModel;

    public MainForm() {
        instance = this;
        addWindowListener(new FormWindowListener());
        tableModel = new DefaultTableModel();

        startButton.addActionListener(new StartButtonListener());

        tableJTable = new JTable();
        JScrollPane jsp = new JScrollPane(tableJTable);   // Чтобы у таблицы была шапка, вот так таблицу оборачивают. 
        tablePanel.setLayout(new BorderLayout());         // С помощью редактора формы напрямую JScrollPane не поместить, и почему-то layout не инициализируется 
        tablePanel.add(jsp);

        tableModel.addColumn("Address");
        tableModel.addColumn("Status");
        tableModel.addColumn("Type");
        tableModel.addColumn("Size");
        tableModel.addColumn("Charset");
        tableModel.addColumn("Lvl");

        tableJTable.setModel(tableModel);

        setContentPane(panel);
        pack();
        setTitle(TITLE);
        setVisible(true);
    }

    public static MainForm getInstance() {
        return instance;
    }

    public JTextField getUrlTextField() {
        return this.urlTextField;
    }

    public DefaultTableModel getTableModel() {
        return this.tableModel;
    }

    public void SetButtonTitle(String s)
    {
        this.startButton.setText(s);
    }
}