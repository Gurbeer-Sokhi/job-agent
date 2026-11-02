package org.example;

import org.example.tools.FetchJobPageTool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;
import java.util.function.DoubleConsumer;

public class JsoupTest {
    public static void main(String[] args) throws IOException{
        FetchJobPageTool tool = new FetchJobPageTool();
        System.out.println(tool.execute(Map.of("url", "https://job-boards.eu.greenhouse.io/binance/jobs/5002679101")));
        System.out.println(tool.execute(Map.of("url", "https://this-site-does-not-exist.invalid")));
        System.out.println(tool.execute(Map.of("url", "not a url")));

    }
}
