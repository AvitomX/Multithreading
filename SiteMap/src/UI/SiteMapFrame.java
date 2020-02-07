package UI;

import Multithreading.*;
import Multithreading.crawler.WebCrawler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class SiteMapFrame extends JFrame {
    private MainForm form = new MainForm();
    private Timer linksTimer;
    private int cores;
    private String site;
    private WebCrawler webCrawler;
    private long initTime;
    private long tempTime;

    {
        cores = Runtime.getRuntime().availableProcessors() + 1;
    }

    public SiteMapFrame() {
        setContentPane(form.getRootPanel());
        setSize(500, 400);
        setInitialState();

        linksTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTime(initTime);
                if (webCrawler.isDone()){
                    showTime(initTime);
                    linksTimer.stop();
                    form.getStateLabel().setText("Обработка завершена");
                    setStopState();
                }
                if (webCrawler.isPaused()){
                    tempTime = showTime(initTime);
                    setPauseState();
                    linksTimer.stop();
                }
            }
        });

        form.addActionListenerStartButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("Старт".equals(form.getStartButton().getText())) {
                    if (setSite()) {
                        startProcess();
                    } else
                        JOptionPane.showMessageDialog(
                                SiteMapFrame.this,
                                "Введите URL",
                                "Ошибка ввода",
                                JOptionPane.ERROR_MESSAGE);
                } else {
                    pauseOrContinueProcess(form.getStartButton());
                }
            }
        });

        form.addActionListenerStopButton(e -> stopProcess());

        form.addActionListenerSaveButton(e -> saveToFile());
    }

    private void saveToFile() {
        if (webCrawler != null) {
            writeToFile(chooseFile(), webCrawler.getRoot());
            form.getFileLabel().setText("Выгрузка завершена");
        } else {
            JOptionPane.showMessageDialog(
                    SiteMapFrame.this,
                    "Обработка ещё не завшена",
                    "Ошибка выгрузки",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private long showTime(long time) {
        form.getSiteNumberLabel().setText(String.valueOf(webCrawler.size()));
        long differTime = System.currentTimeMillis() - time;
        form.getTimeLabel().setText(String.format("%,d%s", differTime, " ms"));
        return differTime;
    }

    private void pauseOrContinueProcess(JButton button) {
        if ("Пауза".equals(button.getText())){
            if (webCrawler != null) {
                webCrawler.pause();
                setPauseState();
            }
        } else if ("Продолжить".equals(button.getText())){
            if (webCrawler != null) {
                initTime = System.currentTimeMillis() - tempTime;
                linksTimer.start();
                webCrawler.continueProcess();
                setContinueState();
            }
        }
    }

    private void stopProcess() {
        if (webCrawler != null) {
            webCrawler.stop();
            form.getStateLabel().setText("Завершение обработки...");
//            setStopState();
        }
    }

    private void startProcess() {
        setStartState();
        initTime = System.currentTimeMillis();
        linksTimer.start();
        webCrawler = new WebCrawler(cores, site);
    }

    private void setInitialState() {
        form.getStartButton().setText("Старт");
        form.getStopButton().setEnabled(false);
        form.getSaveButton().setEnabled(false);
        form.getFileLabel().setText("");
        form.getStateLabel().setText("");
    }

    private void setStartState() {
        form.getStartButton().setText("Пауза");
        form.getStopButton().setEnabled(true);
        form.getSaveButton().setEnabled(false);
        form.getFileLabel().setText("");
        form.getStateLabel().setText("Идет обработка...");
        tempTime = 0;
    }

    private void setContinueState() {
        form.getStartButton().setText("Пауза");
        form.getStopButton().setEnabled(true);
        form.getSaveButton().setEnabled(true);
        form.getFileLabel().setText("");
        form.getStateLabel().setText("Идет обработка...");
    }

    private void setPauseState() {
        form.getStartButton().setText("Продолжить");
        form.getStopButton().setEnabled(true);
        form.getSaveButton().setEnabled(true);
        form.getFileLabel().setText("");
        form.getStateLabel().setText("Обработка приостановлена");
    }

    private void setStopState() {
        form.getStartButton().setText("Старт");
        form.getStopButton().setEnabled(false);
        form.getSaveButton().setEnabled(true);
        form.getFileLabel().setText("");
    }

    private boolean setSite() {
        this.site = form.getPath().trim();
        return !this.site.isEmpty();
    }

    private void writeToFile(File file, SiteNode node) {
        ArrayList<String> nodesList = SiteNode.getAllChildren(node, "-");
        try (FileWriter writer = new FileWriter(file, false)) {
            String lineSeparator = System.getProperty("line.separator");
            for (String value : nodesList) {
                writer.append(String.valueOf(value)).append(lineSeparator);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public File chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        File selectedFile = new File("C:\\1.txt");
        fileChooser.setDialogTitle("Выбор файла");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(SiteMapFrame.this);
        if (result == JFileChooser.APPROVE_OPTION)
            selectedFile = fileChooser.getSelectedFile();
        return selectedFile;
    }
}
