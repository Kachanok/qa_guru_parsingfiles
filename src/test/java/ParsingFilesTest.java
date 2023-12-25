import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import models.Example;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.FileExtractor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Парсинг файлов")
public class ParsingFilesTest extends TestData {
    FileExtractor fileExtractor = new FileExtractor();
    private final ClassLoader cl = ParsingFilesTest.class.getClassLoader();
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

                assertThat(example.getTitle()).isEqualTo("Anno Dracula");
                assertThat(example.getAuthorName()).isEqualTo("Kim Newman");
                assertThat(example.getPageCount()).isEqualTo(409);
                assertThat(example.getMainCharacters()).contains("Charles Beauregard", "Penelope Churchward", "Count Dracula");


            }
        }
    }


    @Test
    @DisplayName("Распаковка архива и парсинг pdf файла")
    void parsePdfFileTest() throws Exception {
        byte[] fileContent = fileExtractor.extractZipFile(archiveName, pdfFile);
        PDF pdf = new PDF(fileContent);

        assertThat(pdf.creator).isEqualTo(creator);
        assertThat(pdf.numberOfPages).isEqualTo(pageCount);



    }

    @Test
    @DisplayName("Распаковка архива и парсинг xlsx файла")
    void parseXlsxFileTest() throws Exception {
        byte[] fileContent = fileExtractor.extractZipFile(archiveName, xlsFile);
        XLS xls = new XLS(fileContent);

        String title = xls.excel.getSheetAt(0).getRow(1).getCell(1).getStringCellValue();
        String section = xls.excel.getSheetAt(0).getRow(2).getCell(1).getStringCellValue();
        String textUrl = xls.excel.getSheetAt(0).getRow(3).getCell(1).getStringCellValue();

        assertThat(title).isEqualTo(title);
        assertThat(section).isEqualTo(section);
        assertThat(textUrl).isEqualTo(textUrl);


    }

    @Test
    @DisplayName("Распаковка архива и парсинг csv файла")
    void parseCsvFileTest() throws Exception {

        byte[] fileContent = fileExtractor.extractZipFile(archiveName, csvFile);
        CSVReader csvReader = new CSVReader(new InputStreamReader(
                new ByteArrayInputStream(fileContent), StandardCharsets.UTF_8));

        assertThat(csvReader.readAll().get(0)).isEqualTo(
                new String[]{csvFirstRow});
    }








}
