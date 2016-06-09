/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//-XX:-UseConcMarkSweepGC
package com.fegati.utilities;

import com.opencsv.CSVWriter;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.ArrayUtils;

/**
 *
 * @author the_fegati
 */
public class CSVPreprocessor {

    public static final int HEADER_INDEX = 0;
    public static final String DEFAULT_EMPTY_VALUE = "";
    public static final String DEFAULT_TITLE_PREFIX = "UNTITLED_";
    private static final Logger LOGGER = Logger.getLogger("CSVPreprocessor");

    /**
     * Preprocess the CSV File, remove all the unmeaningful column and add
     * titles to the untitled header's columns.
     *
     * @param filePath - The absolute CSV file path
     * @return The processed temporary CSV file
     * @throws IOException - Thrown while failed to access file
     *
     */
    public static File execute(String filePath) throws IOException {
        return execute(new File(filePath));
    }

    /**
     * Preprocess the CSV File, remove all the unmeaningful column and add
     * titles to the untitled header's columns.
     *
     * @param file - The original CSV file
     * @return The processed temporary CSV file
     * @throws IOException - Thrown while failed to access file
     */
    public static File execute(File file) throws IOException {

        if (file.exists()) {
            return preprocessCSVFile(file);
        }
        return file;
    }

    /**
     * Preprocess the CSV File, remove all the unmeaningful column and add
     * titles to the untitled header's columns.
     *
     * @param file - The original CSV file
     * @return The processed temporary CSV file
     * @throws IOException - Thrown while failed to access file
     */
    private static File preprocessCSVFile(File file) throws IOException {
        List<String[]> rows = readAllRowsFromCSVFile(file);
        /* Remove all the tailing empty string */
		//cleanTailingEmptyStrings(rows);

        /**
         * Remove unwanted columns
         */
        LOGGER.log(Level.INFO, "Whole file read");
        System.out.println(rows.size());
//                removeUnwantedColumns(rows, 2);
//                LOGGER.log(Level.INFO, "Deleted Column 2");
//                removeUnwantedColumns(rows, 4);
//                LOGGER.log(Level.INFO, "Deleted Column 4");
//                removeUnwantedColumns(rows, 5);
//                LOGGER.log(Level.INFO, "Deleted Column 5");
//                removeUnwantedColumns(rows, 6);
//                removeUnwantedColumns(rows, 10);
//                removeUnwantedColumns(rows, 12);
//                removeUnwantedColumns(rows, 13);
//                removeUnwantedColumns(rows, 14);
        List<String[]> cleanRows = removeUnwantedRows(rows);

        /* Get the highest column size */
        int columnSize = validateColumnSize(cleanRows);

        /* balance all the rows with the same column size */
		//balanceColumnsSizeOfTheRows(rows, columnSize);
        /* Add default title to the untitled header's columns */
		//titleHeader(rows);
        /* Create a temporary CSV file with the processed CSV contents */
        File processedFile = new File("/home/the_fegati/Downloads/FelixData/Batch1/preprocessed_labs.csv");
        writeRowToCSVFile(processedFile, cleanRows, columnSize);

        return processedFile;
    }

    /*
     * Return the highest column size in all the rows
     */
    private static int validateColumnSize(List<String[]> rows) {
        int maxSize = 0;

        for (String[] row : rows) {
            if (maxSize < row.length) {
                maxSize = row.length;
            }
        }
        return maxSize;
    }

    /*
     * Add default title to the untitled columns
     */
    private static void titleHeader(List<String[]> rows) {

        int numberOfUntitledColumn = 0;

        if (rows.size() > 0) {
            String[] header = rows.get(HEADER_INDEX);

            for (int i = 0; i < header.length; i++) {
                /* Add default title if the column is an empty string */
                if (header[i].equals(DEFAULT_EMPTY_VALUE)) {
                    header[i] = DEFAULT_TITLE_PREFIX + String.valueOf(numberOfUntitledColumn++);
                }
            }
            rows.set(HEADER_INDEX, header);
        }
    }

    /*
     * Loop through each row and remove the tailing emtpy strings if exist
     */
    private static void cleanTailingEmptyStrings(List<String[]> rows) {
        for (int i = 0; i < rows.size(); i++) {
            rows.set(i, removeTailingEmptyStringsInRow(rows.get(i)));
        }
    }

    /*
     * Remove the tailing empty strings from the given row. Example:
     * Given {"abc", "", "text",,""}
     * Result {"abc", "", "text"}
     */
    private static String[] removeTailingEmptyStringsInRow(String[] row) {
        int size = row.length;
        int cuttedIndex = size;

        for (int i = cuttedIndex - 1; i >= 0; i--) {
            if (row[i].equals(DEFAULT_EMPTY_VALUE)) {
                cuttedIndex = i;
            } else {
                break;
            }
        }

        if (cuttedIndex != size) {
            return ArrayUtilities.copyOf(row, cuttedIndex);
        }
        return row;
    }

    /**
     * remove rows that contain certain values uses simple string matching
     *
     * @param rows
     */

    private static List<String[]> removeUnwantedRows(List<String[]> rows) {
        for (int i = 0; i < rows.size(); i++) {
            if (removeIfUnwanted(rows.get(i))) {
                rows.remove(i);
//                        System.out.println(rows.remove(i));
            }
        }
        System.out.println(rows.size());
        return rows;
    }

    private static boolean removeIfUnwanted(String[] row) {
        String testName = row[5];
        if (testName.contains("Albumins") || testName.contains("Appearance")
                || testName.contains("ALKP") || testName.equals("ASC") || testName.equals("AST")
                || testName.equals("Bilirubin") || testName.equals("Bilirubin_urinalysis") || testName.equals("BilirubinTotals")
                || testName.equals("Blood") || testName.equals("Ca") || testName.equals("Cast_cells") || testName.equals("Cd4cd8Ratio")
                || testName.equals("cd8") || testName.equals("Cl") || testName.equals("Color") || testName.equals("CryptoccocalScreening")
                || testName.equals("Crystals") || testName.equals("Epithelial_Cells") || testName.equals("FREE_THYROXINE_T4")
                || testName.equals("FREE_TRI_IODOTHYRONINE_T3") || testName.equals("THYROTROPIN_TSH_S") || testName.equals("GAMMAGT")
                || testName.equals("Glucose_Urinalysis") || testName.equals("Isoniazid_H") || testName.equals("K")
                || testName.equals("LAC") || testName.equals("LacticAcidiosis") || testName.equals("LDL") || testName.equals("Na") || testName.equals("Nitrite")
                || testName.equals("Parasites_Blood_Slide") || testName.equals("Parasites_Urinalysis") || testName.equals("PCRResult")
                || testName.equals("PH") || testName.equals("Protein") || testName.equals("Pus_Cells") || testName.equals("RedBlood_Cells")
                || testName.equals("RH") || testName.equals("Rifampicin_R") || testName.equals("Serological") || testName.equals("SerumAmylase")
                || testName.equals("SG") || testName.equals("TotalProtein") || testName.equals("Tuberculosis") || testName.equals("Yeast_Cells")) {
            return true;
        } else {
            return false;
        }
    }

    public static void removeUnwantedColumns(List<String[]> rows, int index) {
        int i = 0;
        for (String[] row : rows) {
            rows.set(i, ArrayUtils.remove(row, index));
            i++;
            LOGGER.log(Level.INFO, "Removing column " + index + " of row " + i);
        }
    }

    /*
     * Read all the rows from CSV file
     */
    private static List<String[]> readAllRowsFromCSVFile(File file) throws IOException {
        TsvParserSettings settings = new TsvParserSettings();
        TsvParser parser = new TsvParser(settings);
        FileReader fileReader = new FileReader(file);
        List<String[]> rows = parser.parseAll(fileReader);

        return rows;
    }

    /*
     * Create the temporary CSV file
     */
//	private static File createTempCSVFile(File file) throws IOException {
//		String tempCSVFileName = "Preprocessed-" 
//			+ FileUtilities.extractFileName(file.getName()) 
//			+ "-";
//		
//		return FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
//					tempCSVFileName, "csv");
//	}
    /*
     * Write the rows into the CSV file
     */
    private static void writeRowToCSVFile(File file, List<String[]> cleanRows, int columnSize)
            throws IOException {
        CSVWriter csvWriter = new CSVWriter(new FileWriter(file));
        csvWriter.writeAll(cleanRows);
        csvWriter.close();
    }

    /*
     * Expend the rows if needed 
     */
    private static void balanceColumnsSizeOfTheRows(List<String[]> rows, int columnSize) {

        for (int i = 0; i < rows.size(); i++) {
            String[] row = rows.get(i);
            int remain = columnSize - row.length;

            /* Replace the expended row if needed */
            if (remain > 0) {
                rows.set(i, expendRowWithEmptyString(rows.get(i), remain));
            }
        }
    }

    /*
     * Add columns with the given size into a row 
     */
    private static String[] expendRowWithEmptyString(String[] row, int expendedSize) {
        if (expendedSize > 0) {
            int oldSize = row.length;

            /* Copy the array with additional size */
            row = ArrayUtilities.copyOf(row, oldSize + expendedSize);

            /* Fill null with DEFAULT_EMPTY_VALUE */
            for (int i = oldSize; i < row.length; i++) {
                row[i] = DEFAULT_EMPTY_VALUE;
            }
        }

        return row;
    }
}
