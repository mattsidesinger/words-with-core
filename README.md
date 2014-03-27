# Words With Core

A Java implementation of the GADDAG data structure. Includes a command line utility to determine the highest scoring move for given a board. Classes are included to parse boards from a file.

This is the base (core) library to a larger project that integrates with Words With Friends.

## GaddagCommandLine

Given a path to a file that contains a board object and a rack, the highest score placement is determined.

```
USAGE: GaddagCommandLine [-b path-to-file] -r letters-in-rack
-b, --board: Path to a board file. If not included, assumes an empty board
-r, --rack: Letters in the rack (upper or lower cased), where a * signifies a blank tile. A rack must be between 1 and 7 characters long.
```

**Valid examples:**

* GaddagCommandLine -b /usr/board1.txt --rack RSTLNE
* GaddagCommandLine --board /usr/board2.txt -r ABDF*UX

The format of the board at the given file is parsed by the SimpleBoardParser is lenient mode.
