import sys
import constants
import validate_archive


# Main program to process run arguments
def main():
    try:
        validateArchive = validate_archive.ValidateArchive()

        args = sys.argv  # List of program arguments
        i = 1
        while i < len(args):
            arg = args[i]
            if arg == '-h' or arg == '--help':
                print(constants.HELP_STRING)
                return
            elif arg == '-a' or arg == '--archive_link':
                i += 1
                validateArchive.archiveLink = args[i]
            elif arg == '-o' or arg == '--outdir':
                i += 1
                validateArchive.outputDirectory = args[i]
            elif arg == '-l' or arg == '--log':
                i += 1
                validateArchive.logging = True

            i += 1

        validateArchive.validateInformation()
    except Exception as e:
        print(e)
        exit(1)


# Call to main function
if __name__ == "__main__":
    main()
