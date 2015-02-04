package org.poverty_action.github;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.json.JsonWriter;

import com.jcabi.github.JsonReadable;

class JsonFile {
	private final Path path;
	private final JsonReadable json;

	public JsonFile(Path path, JsonReadable json) {
		if (path == null || json == null)
			throw new NullPointerException();
		this.path = path;
		this.json = json;
	}

	public void write() throws IOException {
		if (!Files.deleteIfExists(path)) {
			Path parent = path.getParent();
			if (parent != null)
				Files.createDirectories(parent);
		}
		try (
			PrintWriter pw = new PrintWriter(path.toFile());
			JsonWriter  jw = PrettyJsonWriterFactory.get().createWriter(pw)) {
			jw.writeObject(json.json());
		}
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof JsonFile))
			return false;
		JsonFile that = (JsonFile) o;
		return this.path.equals(that.path) && this.json.equals(that.json);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result + path.hashCode();
		result = prime * result + json.hashCode();
		return result;
	}
}
