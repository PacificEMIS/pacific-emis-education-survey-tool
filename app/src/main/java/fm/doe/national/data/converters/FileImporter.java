package fm.doe.national.data.converters;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileImporter {

    private DataImporter dataImporter;

    public FileImporter(DataImporter dataImporter) {
        this.dataImporter = dataImporter;
    }

    public void importFile(File file) {
        try {
            String data = new Scanner(file).useDelimiter("\\A").next();
            dataImporter.importData(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
