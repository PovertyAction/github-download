github-download
===============

`github-download` downloads commit comments and select issues metadata, saving the raw JSON and writing summary `.csv` files.

Installing
----------

Download the `.jar` file [here](/target/github-download-1.0-SNAPSHOT-jar-with-dependencies.jar). It includes all dependencies. You must have the [Java Runtime Environment](http://java.com/en/download/manual.jsp) version 7 or above.

Usage
-----

`github-download` can be run from the command line. It has three required flags:

`-repo`. The full repository name, e.g., `PovertyAction/github-download`.

`-to`. The directory in which to save the metadata. It will be created if it does not exist already.

`-token`. The name of a text file that contains solely a GitHub [OAuth token](https://help.github.com/articles/creating-an-access-token-for-command-line-use/). GitHub will supply you a token, which is a single string. You must copy it to a text file, then specify the name of that file to `-token`.

All together:

```
java -jar github-download.jar -repo PovertyAction/github-download -token token.txt -to metadata
```

If the name of the `.jar` file is not `github-download.jar`, use the actual filename in the command above, or rename the file as `github-download.jar`. If the file is not in the current working directory, you will have to specify its path.

Next, specify the metadata to download:

`-issues`. Download select issues metadata.

`-cc`. Download commit comments, including in-line notes.

To download all supported metadata:

```
java -jar github-download.jar -repo PovertyAction/github-download -token token.txt -to metadata -issues -cc
```

You may see the following warning message, which is safe to ignore:

```
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
```
