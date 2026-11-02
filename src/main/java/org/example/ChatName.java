package org.example;

import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class ChatName {

    static final String MODEL = "gemini-3.5-flash";
    public static void main() {
        Client client = Client.builder()
                .apiKey(System.getenv("GEMINI_API_KEY"))
                .build();

        List<Content> history = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        while(true){
            System.out.print("You: ");
            String input = scanner.nextLine();
            if ("exit".equalsIgnoreCase(input.trim())) break;

            history.add(Content.builder().role("user").parts(List.of(Part.fromText(input))).build());

            GenerateContentResponse response = client.models.generateContent(
                    MODEL, history, null);

            history.add(response.candidates().get().get(0).content().get());

            System.out.println("Reply text: " + response.text());
            System.out.println("prompt Token Count: " + response.usageMetadata().get().promptTokenCount().get());


        }

    }
}
