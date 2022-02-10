class ValidateArchive:
    # Default constructor
    def __init__(self):
        self.logging = False
        self.archiveLink = ""
        self.outputDirectory = ""

    # Validates inputted information
    def validateInformation(self):
        # Check if archive link was specified
        if self.archiveLink == "":
            raise Exception("Invalid run arguments:\nArchive link was not specified")

    # Print out info about inputted command line arguments
    def printInfo(self):
        print("Archive Link: {0}".format(self.archiveLink))
        print("Output Directory: {0}".format(self.outputDirectory))
        print("Logging: {0}".format(self.logging))