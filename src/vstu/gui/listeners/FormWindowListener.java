package vstu.gui.listeners;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Created by danila on 20.03.2014.
 */
public class FormWindowListener implements WindowListener {

    @Override
    public void windowOpened(WindowEvent e) {
        // не требуется
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.exit(0);      // Чтобы приложение после закрытия окна не висело
    }

    @Override
    public void windowClosed(WindowEvent e) {
        // не требуется
    }

    @Override
    public void windowIconified(WindowEvent e) {
        // не требуется
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        // не требуется
    }

    @Override
    public void windowActivated(WindowEvent e) {
        // не требуется
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        // не требуется
    }
}