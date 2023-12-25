package utils;

import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileExtractor {
    private ClassLoader cl = FileExtractor.class.getClassLoader();

    public byte[] extractZipFile(String archiveName, String fileName) throws Exception {

        byte[] file = null;
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream(archiveName)
        )) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals(fileName)) {
                    file = zis.readAllBytes();
                }
            }
            return file;
        }
    }
}
