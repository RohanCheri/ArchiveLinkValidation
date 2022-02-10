HELP_STRING = """Program to output all valid and invalid links in an archive crawl
Usage:
    archiveLinkValidation [OPTIONS...] -a Archive_Link
Options:
    {0:40s}{1}
    {2:40s}{3}
    {4:40s}{5}
    {6:40s}{7}""".format("-h, --help", "Prints out help screen",
                         "-a, --archive_link Archive_Link", "Link to home directory of web archive",
                         "-o, --outdir Output_Directory", "Directory to write output CSV files (default is home directory)",
                         "-l, --log", "Turns on logging mode (Stores CSV files created)")
