import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
//"time_id","ping_ms","temperature_c","humidity_p"
public class FileSplitter {

    public static void main(String[] args) throws InterruptedException {
        // Specify the source file and the number of lines per split
        String sourceFilePath = "../input/data.csv";
        int linesPerSplit = 10;

        try {
            splitFile(sourceFilePath, linesPerSplit);
            System.out.println("File splitting completed successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void splitFile(String sourceFilePath, int linesPerSplit) throws IOException, InterruptedException {
        try (BufferedReader reader = new BufferedReader(new FileReader(sourceFilePath))) {
            String line;
            int fileCount = 1;
            int lineCount = 0;

            // Create the first output file
            FileWriter writer = new FileWriter(getOutputFileName(sourceFilePath, fileCount));
            while ((line = reader.readLine()) != null) {
        		if(fileCount == 3){
        			break;
        		}
            	writer.write(line + "\n");
                lineCount++;

                // Check if the specified number of lines per split is reached
                if (lineCount == linesPerSplit) {
                    // Close the current output file and create a new one
                    writer.close();
                    fileCount++;
                    lineCount = 0;
                    writer = new FileWriter(getOutputFileName(sourceFilePath, fileCount));
                    System.out.println("File splitting for part completed:" + fileCount);

                    Thread.sleep(10000);
                }
            }

            // Close the last output file
            writer.close();
        }
    }

    private static String getOutputFileName(String sourceFilePath, int fileCount) {
        // Generate the name for the output file based on the source file name and count
        int dotIndex = sourceFilePath.lastIndexOf('.');
        String extension = (dotIndex == -1) ? "" : sourceFilePath.substring(dotIndex);
        String outputFile = sourceFilePath.replace(extension, "_part" + fileCount + extension);
        return "../input/parts/data_part" + fileCount  + extension;
    }
}
