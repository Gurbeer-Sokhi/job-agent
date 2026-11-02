package org.example;

import com.google.genai.Client;
import com.google.genai.types.*;
import org.example.tools.FetchJobPageTool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ToolDemo {

    static final String MODEL = "gemini-3.6-flash";

    public static void main(String[] args) {
        Client client = Client.builder()
                .apiKey(System.getenv("GEMINI_API_KEY"))
                .build();

        FetchJobPageTool tool = new FetchJobPageTool();

        // 1. Tell the model the tool exists
        GenerateContentConfig config = GenerateContentConfig.builder()
                .tools(List.of(Tool.builder()
                        .functionDeclarations(List.of(tool.declaration()))
                        .build()))
                .build();

        List<Content> history = new ArrayList<>();
        String input = "What skills do Java backend jobs usually need?";
        history.add(Content.builder().role("user").parts(List.of(Part.fromText(input))).build());

        GenerateContentResponse response = client.models.generateContent(MODEL,history,config);

        List<FunctionCall> calls = response.functionCalls();

        System.out.println(calls);

        if (calls != null && !calls.isEmpty()) {
            FunctionCall call = calls.get(0);                 // the first (and here only) call

            String name = call.name().get();                  // "fetch_job_page"
            Map<String, Object> callArgs = call.args().get(); // {"url": "https://..."}

            System.out.println("Model wants: " + name + " with " + callArgs);

            String result = tool.execute(callArgs);


            // YOUR Java code runs here


        history.add(response.candidates().get().get(0).content().get());
        Content toolResult = Content.builder()
                .role("user")
                .parts(List.of(Part.fromFunctionResponse(name, Map.of("content", result))))
                .build();
        history.add(toolResult);

        // 7. Call 2: same config, so the model still knows about the tool
        GenerateContentResponse finalResponse = client.models.generateContent(MODEL, history, config);

        // 8. Print the final answer and the token count
        System.out.println("Final answer:\n" + finalResponse.text());
        System.out.println("Prompt tokens: " + finalResponse.usageMetadata().get().promptTokenCount().get());

    } else {
        // The model answered directly without using the tool
        System.out.println("No tool call. Answer:\n" + response.text());
    }


    }
}
