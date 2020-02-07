package Multithreading.crawler;

public interface NodeHandler {

    boolean isProcessedLink(String link);

    int size();
}
