import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ValidateArchive {
    // Default values/constants
    private static final String OUTPUT_FILE_EXTENSION = "_links.csv";
    private static final String CSV_OUTPUT_NAME = "linkchecker-out.csv";
    private static final String SEMICOLON_DELIMITER = ";";
    private static final String VALID_LINK_RESULT = "TRUE";

    private static final int INDEX_OF_VALID_RESULT = 6;
    private static final int INDEX_OF_URL = 7;

    // Class variables
    private boolean logging, display;

    private Path outputDirectory = null, workingDirectory = null;
    private File outputFile = null;

    private String archiveLink;

    private ArrayList<String> validLinks, invalidLinks;

    /**
     * Constructor for the bulk conversion class
     */
    public ValidateArchive() {
        logging = false;
        display = false;
        workingDirectory = Paths.get(".");
        setOutputDirectory(workingDirectory);
        archiveLink = null;
        validLinks = new ArrayList<>();
        invalidLinks = new ArrayList<>();
    }

    /**
     * This function process the validation process for the specified
     * link
     */
    public void processLinkValidation() throws IOException, InterruptedException {
        // Create process to run conversion
        ProcessBuilder pb = new ProcessBuilder();
        createLinkValidationRunArguments(pb.command());

        System.out.println("Starting linkchecker run");

        // Output to console
        pb.inheritIO();

        // Start process
        Process process = pb.start();

        // Wait for process to end
        process.waitFor();

        // Look through CSV Output File
        parseCSVOutputFile();

        // Generate output CSV of Links
        generateCSVOfLinks();
    }

    /**
     * This function generates a CSV of all links and seperates
     * them between if they are valid or invalid
     */
    private void generateCSVOfLinks() throws IOException {
        List<String> fileContents = new ArrayList<>();

        // Add all valid links
        fileContents.add("Valid Links:");
        fileContents.addAll(validLinks);

        // Add all invalid links
        fileContents.add("Invalid Links:");
        fileContents.addAll(invalidLinks);

        // Find name of base website
        String[] archiveOutputFileNameList = archiveLink.split("http://");
        String archiveOutputFileName = archiveOutputFileNameList[archiveOutputFileNameList.length - 1];
        archiveOutputFileName = archiveOutputFileName.replace("/", "-");
        archiveOutputFileName = archiveOutputFileName.replace("\\", "-");

        // Create and write output file
        File outputCSVLinksFile = outputDirectory.resolve(archiveOutputFileName + OUTPUT_FILE_EXTENSION).toFile();
        outputCSVLinksFile.createNewFile();

        Files.write(outputCSVLinksFile.toPath(), fileContents);

        // Delete or move output csv file

        if (logging) {
            Files.move(workingDirectory.resolve(CSV_OUTPUT_NAME), outputDirectory.resolve(archiveOutputFileName + "_" + CSV_OUTPUT_NAME));
        }
        else {
            Files.delete(workingDirectory.resolve(CSV_OUTPUT_NAME));
        }
    }

    /**
     * This function parses through the output csv file that
     * linkchecker creates
     */
    private void parseCSVOutputFile() throws FileNotFoundException {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_OUTPUT_NAME))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(SEMICOLON_DELIMITER);

                // Checks if line actually contains information
                if (!values[0].contains("#") && !values[0].contains("urlname")) {
                    if (values[INDEX_OF_VALID_RESULT].equalsIgnoreCase(VALID_LINK_RESULT)) { // Adds url to proper list
                        validLinks.add(values[INDEX_OF_URL]);
                    }
                    else {
                        invalidLinks.add(values[INDEX_OF_URL]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds all run arguments to process builder commands
     */
    public void createLinkValidationRunArguments(List<String> arguments) {
        // Specify use of linkchecker
        arguments.add("linkchecker");

        // Specify to ignore robots on website
        arguments.add("--no-robots");

        // Specify creation of csv file
        arguments.add("-F");
        arguments.add("csv");

        // Follow extern
        arguments.add("--check-extern");

        // Ignore all URLs outside domain
        arguments.add("--ignore-url");

        int indexOfBaseURL = archiveLink.lastIndexOf("http");
        String baseURL = archiveLink.substring(indexOfBaseURL);
        arguments.add("!(.*" + baseURL + ".*)"); // Regex

        // Specify to run on verbose mode
        arguments.add("-v");

        // Specify archive link to check
        arguments.add(archiveLink);
    }

    /**
     * Validates information that has been received
     */
    public void validateInformation() {
        // Check if input and output directories are designated
        if (getOutputDirectory() == null) {
            throw new IllegalArgumentException("Invalid run arguments:\n" +
                    "Output directory was not specified");
        }

        // Checks if output file exists
        outputFile = outputDirectory.toFile();
        if (!outputFile.exists()) {
            throw new IllegalArgumentException("Invalid run arguments:\n" +
                    "Output directory of " + outputDirectory + "does not exist");
        }

        // Check if archive link has been specified
        if (archiveLink == null) {
            throw new IllegalArgumentException("Invalid run arguments:\n +" +
                    "Archive link not specified");
        }
    }

    /**
     * This function returns the output directory path
     */
    public Path getOutputDirectory() {
        return outputDirectory;
    }

    /**
     * This function sets the program to run in display mode
     */
    public void setDisplayOn() {
        display = true;
    }

    /**
     * This function sets the program to run in logging mode
     */
    public void setLoggingOn() {
        logging = true;
    }

    /**
     * Sets the archive link to the specified archive link
     * @param archiveLink Link to archive
     */
    public void setArchiveLink(String archiveLink) {
        this.archiveLink = archiveLink;
    }

    /**
     * This function sets the output directory path
     * @param setOutputDirectory Output Directory Path
     */
    public void setOutputDirectory(Path setOutputDirectory) {
        outputDirectory = setOutputDirectory;
    }
}
