import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import models.Example;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Парсинг файлов")
public class ParsingFilesTest extends TestData {
    private ClassLoader cl = ParsingFilesTest.class.getClassLoader();
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Парсинг json файла")
    void parseJsonTest() throws Exception {
        try (
                InputStream resource = cl.getResourceAsStream("example.json")
        ) {
            assert resource != null;
            try (InputStreamReader reader = new InputStreamReader(resource)
            ) {
                Example example = objectMapper.readValue(reader, Example.class);

                assertThat(example.getBookTitle()).isEqualTo("Anno Dracula");
                assertThat(example.getAuthorName()).isEqualTo("Kim Newman");
                assertThat(example.getPageCount()).isEqualTo(409);
                assertThat(example.getMainCharacters()).contains("Charles Beauregard", "Penelope Churchward", "Count Dracula");


            }
        }
    }


    @Test
    @DisplayName("Распаковка архива и парсинг pdf файла")
    void parsePdfFileTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("files.zip"))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals("pdf.pdf")) {
                    PDF pdf = new PDF(zis);


                    assertThat(pdf.creator).isEqualTo(creator);
                    assertThat(pdf.numberOfPages).isEqualTo(pageCount);


                }
            }
        }
    }

    @Test
    @DisplayName("Распаковка архива и парсинг xlsx файла")
    void parseXlsxFileTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("files.zip"))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals("xlstest.xlsx")) {
                    XLS xls = new XLS(zis);

                    Assertions.assertEquals(xls.excel.getSheetAt(0).getRow(0).getCell(0).getNumericCellValue(), 5698235.0);
                    break;

                }
            }
        }
    }

    @Test
    @DisplayName("Распаковка архива и парсинг csv файла")
    void parseCsvFileTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("files.zip"))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals("csv.csv")) {
                    CSVReader reader = new CSVReader(new InputStreamReader(zis));
                    List<String[]> data = reader.readAll();
                    Assertions.assertEquals(2, data.size());

                    Assertions.assertArrayEquals(
                            new String[]{"GitHub", "https://github.com/"},
                            data.get(0));
                    Assertions.assertArrayEquals(
                            new String[]{"StackOverFlow", "https://stackoverflow.com/"},
                            data.get(1));


                }

            }
        }
    }
}