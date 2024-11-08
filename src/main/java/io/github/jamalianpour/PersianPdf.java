package io.github.jamalianpour;

import org.apache.tika.exception.TikaException;
import org.apache.tika.langdetect.optimaize.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageDetector;
import org.apache.tika.language.detect.LanguageResult;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A Java library for extracting text from Persian or Arabic PDF
 */
public class PersianPdf {
    /**
     * Extracts text from the given input stream. The type parameter specifies the method to use:
     * <ul>
     *     <li>{@link TextExtractorType#NORMAL}: Uses Apache Tika to extract text from the input stream directly.
     *     <li>{@link TextExtractorType#ADVANCED}: Uses Apache Tika to extract text from the input stream. Then,
     *         it checks if the language of the extracted text is Persian or Arabic. If it is, it reverses the order of
     *         Persian characters and the words in the text to correct the order of characters.
     * </ul>
     *
     * @param stream the input stream to extract text from
     * @param type   the type of text extraction to use
     * @return the extracted text
     * @throws TikaException if there was an error using Apache Tika
     * @throws IOException   if there was an error reading from the input stream
     * @throws SAXException  if there was an error parsing the input stream
     */
    public String extractText(InputStream stream, TextExtractorType type) throws TikaException, IOException, SAXException {
        // Create an Apache Tika parser to extract text from the input stream
        AutoDetectParser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();

        // Parse the input stream using the parser
        parser.parse(stream, handler, metadata);

        // If the advanced method is specified, check if the language of the extracted text is Persian or Arabic
        if (type.equals(TextExtractorType.ADVANCED)) {
            // Create a language detector to detect the language of the extracted text
            LanguageDetector detector = new OptimaizeLangDetector().loadModels();
            // Get the extracted text
            String data = handler.toString();
            // Detect the language of the extracted text
            LanguageResult result = detector.detect(data);
            // If the language is Persian or Arabic, reverse the order of characters and the words in the text
            if (result.getLanguage().equals("fa") || result.getLanguage().equals("ar")) {
                data = reversePersianCharacters(data);
            }

            // Reverse the words in each line of the text
            return reverseWordsInLines(data);
        } else {
            // If the normal method is specified, return the extracted text as is
            return handler.toString();
        }
    }

    /**
     * Reverses the words in each line of the input string.
     * The line order is preserved, but the words in each line are reversed.
     * For example, "مالس" becomes "سلام".
     *
     * @param data the string to reverse
     * @return the reversed string
     */
    private String reverseWordsInLines(String data) {
        String[] lines = data.split("\n");
        StringBuilder buffer = new StringBuilder();
        for (String line : lines) {
            // Split the input text into an array of words
            String[] words = line.split(" ");

            // Convert array to list for easy reversal
            List<String> wordList = Arrays.asList(words);

            // Reverse the list
            Collections.reverse(wordList);

            // Join the reversed words back into a single string
            String output = String.join(" ", wordList);

            buffer.append(output).append("\n");
        }
        return buffer.toString();
    }

    /**
     * Reverse the order of Persian characters in a given string.
     * This is required because the order of Persian characters in a PDF file is
     * reversed when they are extracted as text.
     *
     * @param input the string to reverse
     * @return the reversed string
     */
    private String reversePersianCharacters(String input) {
        // Pattern to match Persian characters (Unicode range for Persian/Arabic)
        Pattern persianPattern = Pattern.compile("[\\u0600-\\u06FF]+");
        Matcher matcher = persianPattern.matcher(input);

        // StringBuffer to store the final result
        StringBuilder result = new StringBuilder();

        // Find Persian substrings and reverse them
        while (matcher.find()) {
            String persianSubStr = matcher.group();
            String reversedPersianSubStr = new StringBuilder(persianSubStr).reverse().toString();
            matcher.appendReplacement(result, reversedPersianSubStr);
        }

        // Append the rest of the string that doesn't match Persian characters
        matcher.appendTail(result);

        return result.toString();
    }
}