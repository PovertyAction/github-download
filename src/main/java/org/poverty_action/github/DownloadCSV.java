package org.poverty_action.github;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

abstract class DownloadCSV<Record, Subrecord> implements Closeable {
	private final CSVPrinter printer;

	protected DownloadCSV(File file) throws IOException {
		Files.deleteIfExists(file.toPath());
		printer = new CSVPrinter(new PrintWriter(file), CSVFormat.DEFAULT);
	}

	protected CSVPrinter printer() {
		return printer;
	}

	public abstract void printHeader(Record record) throws IOException;
	public abstract void printSubrecord(Subrecord subrecord) throws IOException;

	public void printFooter() throws IOException {
		printer.println();
	}

	@Override
	public void close() throws IOException {
		printer.close();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof DownloadCSV))
			return false;
		return printer.equals(((DownloadCSV<?, ?>) o).printer);
	}

	@Override
	public int hashCode() {
		return printer.hashCode();
	}
}
