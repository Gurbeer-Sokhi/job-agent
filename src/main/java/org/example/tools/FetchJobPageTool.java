package org.example.tools;

import com.google.genai.HttpApiClient;
import com.google.genai.types.FunctionDeclaration;
import com.google.genai.types.Schema;
import com.google.genai.types.Type;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.List;
import java.util.Map;

public class FetchJobPageTool {
    private static final int MAX_CHARS = 8000;
    public static final String NAME = "fetch_job_page";

    public FunctionDeclaration declaration() {

        Schema urlSchema = Schema.builder()
                .type(Type.Known.STRING)
                .description("Full http(s) URL of the job posting page")
                .build();

        Schema params = Schema.builder()
                .type(Type.Known.OBJECT)
                .properties(Map.of("url",urlSchema))
                .required(List.of("url"))
                .build();

        return FunctionDeclaration.builder()
                .name(NAME)
                .description("Fetches the job page, from the job posting board. Use when the user wants you to fetch job postings from particular URL; Requires url. Returns an error if the posting is not fetched successfully")
                .parameters(params)
                .build();

        }


    public String execute(Map<String, Object> args) {
        String url = (String) args.get("url");
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(10_000)
                    .get();
            Elements forms = doc.select("form");

            forms.remove();
            String res = doc.title() + "\n" + doc.body().text();
            if(res.length()> MAX_CHARS) res = res.substring(0,MAX_CHARS) + "\n...[truncated]";
            return res;

        } catch (Exception e) {
            return "[Error fetching] : " + url + ": " + e.getClass().getSimpleName() + " - " + e.getMessage();
        }


    }
}
