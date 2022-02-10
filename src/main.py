import sys
import constants

archiveLink = ""
outputDirectory = ""
logging = False
def main():
    global outputDirectory, logging, archiveLink

    args = sys.argv  # List of program arguments
    i = 1
    while i < len(args):
        arg = args[i]
        if arg == '-h' or arg == '--help':
            print(constants.HELP_STRING)
            return
        elif arg == '-a' or arg == '--archive_link':
            i += 1
            archiveLink = args[i]
        elif arg == '-o' or arg == '--outdir':
            i += 1
            outputDirectory = args[i]
        elif arg == '-l' or arg == '--log':
            i += 1
            logging = True

        i += 1

    print(archiveLink)
    print(outputDirectory)
    print(logging)


if __name__ == "__main__":
    main()
