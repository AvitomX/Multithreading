package Multithreading.crawler;

import Multithreading.SiteNode;

import java.util.HashSet;

public class WebCrawler implements NodeHandler {
    private HashSet<String> processedLinks;
    private MyPool pool;
    private SiteNode node;
    private boolean isPaused;

    {
        processedLinks = new HashSet<>();
        isPaused = false;
    }

    public WebCrawler(int cores, String url) {
        pool = new MyPool(cores);
        node = new SiteNode(url);
        processedLinks.clear();
        processedLinks.add(url);
        pool.startPool(new LinksFinderTask(node, this));
    }

    @Override
    public boolean isProcessedLink(String link) {
        synchronized (processedLinks) {
            if (processedLinks.contains(link)) {
                return true;
            } else {
                processedLinks.add(link);
                return false;
            }
        }
    }

    @Override
    public int size() {
        return processedLinks.size();
    }

    public boolean isDone(){
        return pool.isTerminated();
    }

    public void stop() {
        LinksFinderTask.setFinishedTask(true);
        continueProcess();
    }

    public void pause() {
        isPaused = true;
        LinksFinderTask.setPause(true);
    }

    public void continueProcess() {
        isPaused = false;
        LinksFinderTask.continueProcess();
    }

    public SiteNode getRoot(){
        return node;
    }

    public boolean isPaused() {
        return isPaused;
    }
}
