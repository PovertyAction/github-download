package org.poverty_action.github;

import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

class PrettyJsonWriterFactory {
	private static final JsonWriterFactory factory;
	static {
		Map<String, Boolean> config = new HashMap<>(1);
		config.put(JsonGenerator.PRETTY_PRINTING, true);
		factory = Json.createWriterFactory(config);
	}

	private PrettyJsonWriterFactory() { }

	public static JsonWriterFactory get() { return factory; }
}
