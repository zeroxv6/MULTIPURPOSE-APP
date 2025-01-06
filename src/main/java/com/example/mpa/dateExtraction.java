package com.example.mpa;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class dateExtraction {
    private static final String DIR_PATH = "C:\\ASSIGNMENTS";
    private static final String OUTPUT_FILE = "src/main/resources/data/data.txt";

    public static void main(String[] args) {
        File dir = new File(DIR_PATH);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("Directory not found: " + DIR_PATH);
            return;
        }

        for (File file : dir.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".pdf")) {
                process(file);
            } else if (file.isFile()) {
                file.delete();
                System.out.println("Deleted unnecessary file: " + file.getName());
            }
        }
    }

    private static void process(File pdfFile) {
        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);

            String submissionDate = extractData(text, "Submission Date\\s*[:]*\\s*(\\d{2}/\\d{2}/\\d{4})");
            String fullSubjectCode = extractData(text, "Subject Code\\s*[:]*\\s*(\\w+-\\d{3})");

            // Extract only the numeric part from the subject code
            String subjectCode = null;
            if (fullSubjectCode != null) {
                subjectCode = extractNumericCode(fullSubjectCode);
            }

            if (submissionDate != null && subjectCode != null) {
                String result = "Submission Date: " + submissionDate + " Subject Code: " + subjectCode;
                if (!isDuplicate(result)) {
                    saveData(result);
                    System.out.println("Processed file: " + pdfFile.getName());
                } else {
                    System.out.println("Duplicate entry found, skipping file: " + pdfFile.getName());
                }
            } else {
                System.out.println("Required data not found in file: " + pdfFile.getName());
            }
        } catch (IOException e) {
            System.err.println("An error occurred while processing file " + pdfFile.getName() + ": " + e.getMessage());
        }
    }

    private static String extractData(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    // New method to extract only the numeric part from subject code
    private static String extractNumericCode(String fullSubjectCode) {
        Pattern pattern = Pattern.compile("\\d{3}$");  // Matches exactly 3 digits at the end of string
        Matcher matcher = pattern.matcher(fullSubjectCode);
        if (matcher.find()) {
            return matcher.group();  // Returns only the numeric part (e.g., "304")
        }
        return null;
    }

    private static boolean isDuplicate(String result) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(OUTPUT_FILE));
            return lines.contains(result);
        } catch (IOException e) {
            System.err.println("An error occurred while checking for duplicates: " + e.getMessage());
            return false;
        }
    }

    private static void saveData(String result) {
        try (FileWriter writer = new FileWriter(OUTPUT_FILE, true)) {
            writer.write(result + System.lineSeparator());
        } catch (IOException e) {
            System.err.println("An error occurred while saving data: " + e.getMessage());
        }
    }
}