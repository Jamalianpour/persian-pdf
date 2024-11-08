package io.github.jamalianpour;

/**
 * The type of text extraction to use
 */
public enum TextExtractorType {
    /**
     * Uses Apache Tika to extract text from the input stream directly.
     */
    NORMAL,
    /**
     * Uses Apache Tika to extract text from the input stream. Then,
     * it checks if the language of the extracted text is Persian or Arabic. If it is, it reverses the order of
     * Persian characters and the words in the text to correct the order of characters.
     */
    ADVANCED
}