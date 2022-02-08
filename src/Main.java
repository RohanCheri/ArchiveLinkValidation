import java.nio.file.Path;

/**
 * Main program of ArchiveLinkValidation program
 * to separate invalid and valid links in a crawl
 *
 * This program is created for use at the
 * Stanford University Archives.
 *
 * @version 2/3/2021
 * @author Rohan Cherivirala and Avi Udash
 */
public class Main {
    private static final String HELP_STRING =
        "Program to output all valid and invalid links in an archive crawl\n" +
        "\n" +
        "Usage:\n" +
        "\tarchiveLinkValidation [OPTIONS...] -a Archive_Link\n" +
        "\n" +
        "Options:\n" +
        String.format("\t%-40s%s\n", "-h, --help", "Prints out help screen") +
        String.format("\t%-40s%s\n", "-a, --archive_link Archive_Link", "Link to home directory of web archive") +
        String.format("\t%-40s%s\n", "-o, --outdir Output_Directory", "Directory to write output CSV files (default is home directory)") +
        String.format("\t%-40s%s\n", "-l, --log", "Turns on logging mode (Stores CSV files created)") +
        String.format("\t%-40s%s\n", "-d, --display", "Turns on display mode (Displays conversion status to screen; Warning: may increase runtime)");

    public static void main(String[] args) {
        // Instantiate class for carrying out archive validation
        ValidateArchive validateArchive = new ValidateArchive();

        // Process run arguments
        try {
            for (int i = 0; i < args.length; i++ ) {
                switch (args[i]) { // Show help
                    case ("-h"), ("--help") -> {
                        System.out.println(HELP_STRING);
                        return;
                    }
                    case ("-a"), ("--archive_link") -> validateArchive.setArchiveLink(args[++i]);
                    // Output Directory
                    case ("-o"), ("--outdir") -> validateArchive.setOutputDirectory(Path.of(args[++i]));
                    // Logging mode
                    case ("-l"), ("--log") -> validateArchive.setLoggingOn();
                    // Display mode
                    case ("-d"), ("--display") -> validateArchive.setDisplayOn();
                    default -> // Invalid run argument
                            throw new IllegalArgumentException("Invalid run arguments:\n" +
                                    "Error occurred processing argument: " + args[i]);
                }
            }

            // Validates inputted information
            validateArchive.validateInformation();

            // Processes actual bulk conversion process
            validateArchive.processLinkValidation();
        }
        catch (Exception e) {
            e.printStackTrace(); // Display contents of error
            System.exit(1);
        }
    }
}
