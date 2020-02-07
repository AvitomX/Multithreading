package UI;

import javax.swing.*;
import java.awt.event.ActionListener;

public class MainForm {
    private JPanel rootPanel;
    private JPanel infoPanel;
    private JPanel sitePanel;
    private JTextField siteTextField;
    private JButton siteButton;
    private JButton saveButton;
    private JLabel infoSiteLabel;
    private JLabel siteNumberLabel;
    private JLabel infoTimeLabel;
    private JLabel timeLabel;
    private JButton startButton;
    private JButton stopButton;
    private JLabel fileLabel;
    private JLabel stateLabel;


    public MainForm() {
    }

    public JLabel getStateLabel() {
        return stateLabel;
    }

    public JLabel getTimeLabel() {
        return timeLabel;
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public JButton getStartButton() {
        return startButton;
    }

    public JButton getStopButton() {
        return stopButton;
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public void addActionListenerSaveButton(ActionListener listener) {
        saveButton.addActionListener(listener);
    }

    public void addActionListenerStopButton(ActionListener listener) {
        stopButton.addActionListener(listener);
    }

    public void addActionListenerStartButton(ActionListener listener) {
        startButton.addActionListener(listener);
    }

    public String getPath(){
        return siteTextField.getText().trim();
    }

    public JLabel getSiteNumberLabel() {
        return siteNumberLabel;
    }

    public JLabel getFileLabel() {
        return fileLabel;
    }
}
