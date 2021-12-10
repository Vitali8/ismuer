package com.upfordown.ismuer.liquibase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upfordown.ismuer.liquibase.exception.NoFileException;
import com.upfordown.ismuer.liquibase.exception.NotSupportedFileException;
import com.upfordown.ismuer.liquibase.exception.ScriptConversionException;
import com.upfordown.ismuer.liquibase.model.CqlScript;
import com.upfordown.ismuer.liquibase.model.CqlScriptLocation;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ScriptRepository {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final String changeLog;

    private List<CqlScript> scripts;

    public ScriptRepository(final String changeLog) {
        this.changeLog = changeLog;
    }

    public List<CqlScript> getScripts(final String defaultKeyspace) throws IOException {
        if (scripts != null) {
            return scripts;
        }

        final List<CqlScriptLocation> locations = OBJECT_MAPPER.readValue(readResource(changeLog),
                OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, CqlScriptLocation.class));

        scripts = locations.stream().sequential()
                .map(this::convertToCqlScript)
                .peek(script -> {
                    if (script.getKeyspace() == null) {
                        if (defaultKeyspace == null) {
                            throw new ScriptConversionException(String.format("No keyspace for script with order '%s'. " +
                                    "No default keyspace was set", script.getOrder()));
                        }
                        script.setKeyspace(defaultKeyspace);
                    }
                })
                .sorted(Comparator.comparingInt(CqlScript::getOrder))
                .collect(Collectors.toList());

        return scripts;
    }

    private CqlScript convertToCqlScript(final CqlScriptLocation location) {
        if (!location.getFile().endsWith(".cql")) {
            throw new NotSupportedFileException(String.format("File '%s' is not cql script", location.getFile()));
        }
        if (location.getOrder() == null) {
            throw new NotSupportedFileException("Orders for all scripts should be set");
        }
        final CqlScript script = new CqlScript();

        script.setOrder(location.getOrder());
        script.setLocation(location.getFile());
        script.setConsistencyLevel(location.getConsistencyLevel());
        script.setKeyspace(location.getKeyspace());

        final String scriptLocation = location.getRelativeToChangelogFile() ? getAsRelativePath(location.getFile()) :
                location.getFile();
        try {
            script.setContent(readResource(scriptLocation));
        } catch (Exception ex) {
            throw new ScriptConversionException(String.format("Can't read file '%s'", location.getFile()), ex);
        }

        return script;
    }

    private String getAsRelativePath(final String location) {
        final Path parent = Paths.get(changeLog).getParent();
        return (parent != null ? parent : Paths.get("/")).resolve(location).toString();
    }

    private String readResource(final String location) {
        final ClassPathResource resource = new ClassPathResource(location);
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new NoFileException("Can't find resource: " + location);
        }
    }
}
