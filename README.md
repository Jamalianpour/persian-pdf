# Persian PDF Text Extractor

## Overview

This Java library provides a simple and efficient way to extract text from PDF files, specifically designed for Persian and Arabic language support. It uses Apache Tika under the hood to parse PDF files and extract text.

### Features

* Extract text from PDF files with Persian or Arabic language support
* Easy-to-use API with a single method for text extraction

## Usage

### Maven Dependency

Add the following dependency to your `pom.xml` file:
```xml
<dependency>
    <groupId>io.github.jamalianpour</groupId>
    <artifactId>persianpdf</artifactId>
    <version>0.0.1</version>
</dependency>
```
### Example Usage

```java
public class Example {
  public static void main(String[] args) {
    PersianPdf persianPdf = new PersianPdf();
    try (InputStream stream = new FileInputStream("path/to/example.pdf")) {
        String text = persianPdf.extractText(stream, TextExtractorType.ADVANCED);
        System.out.println(text);
    } catch (IOException | TikaException | SAXException e) {
        throw new RuntimeException(e);
    }
  }
}
```

**License**
-------

This library is licensed under the Apache License, Version 2.0.

**Contributing**
------------

Contributions are welcome! If you'd like to contribute to this library, please fork the repository and submit a pull request.

**Acknowledgments**
------------

This library uses Apache Tika under the hood. Thanks to the Apache Tika team for their hard work on this excellent library!