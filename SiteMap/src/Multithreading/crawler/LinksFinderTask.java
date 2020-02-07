package Multithreading.crawler;

import Multithreading.SiteNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;

public class LinksFinderTask extends RecursiveAction {
    private static final int SLEEP_TIME = 10;
    private static Random random = new Random();
    private static AtomicBoolean finishedTask = new AtomicBoolean(false);
    private static AtomicBoolean isPaused = new AtomicBoolean(false);

    private SiteNode node;
    private NodeHandler handler;

    public LinksFinderTask(SiteNode node, NodeHandler handler) {
        this.node = node;
        this.handler = handler;
    }

    @Override
    protected void compute() {
        if (!finishedTask.get()) {
            //засыпаем на случайное время, чтобы нас не заблокировали
            try {
                Thread.sleep(random.nextInt(SLEEP_TIME));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                Document document = Jsoup.connect(node.getName()).get();
                Elements elements = document.select("a");
                List<RecursiveAction> actions = new ArrayList<>();
                for (Element element : elements) {
                    String newLink = element.attr("abs:href");
                    if (checkRelation(newLink, node.getName()) & !newLink.contains("#")) {
                        if (!handler.isProcessedLink(newLink)) {
                            SiteNode childNode = new SiteNode(newLink);
                            node.addChild(childNode);
                            actions.add(new LinksFinderTask(childNode, handler));
                        }
                    }
                }
                pauseProcess();
                invokeAll(actions);
            } catch (IOException e1) {
                System.out.println("Connecting error: " + node.getName());
            }
        }
    }

    static void continueProcess() {
        synchronized (isPaused) {
            isPaused.set(false);
            isPaused.notifyAll();
        }
    }

    private void pauseProcess() {
        synchronized (isPaused) {
            while (isPaused.get()) {
                try {
                    isPaused.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private boolean checkRelation(String childNode, String parentNode) {
        if (childNode == null)
            return false;
        try {
            URI childUri = new URI(childNode);
            URI parentUri = new URI(parentNode);
            URI resultURI = parentUri.relativize(childUri);
            return !resultURI.equals(childUri);
        } catch (URISyntaxException e) {
            return false;
        }
    }

    static void setFinishedTask(boolean value) {
        LinksFinderTask.finishedTask.set(value);
    }

    static void setPause(boolean value) {
        LinksFinderTask.isPaused.set(value);
    }
}
