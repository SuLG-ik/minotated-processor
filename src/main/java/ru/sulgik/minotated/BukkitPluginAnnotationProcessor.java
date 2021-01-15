package ru.sulgik.minotated;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import ru.sulgik.minotated.config.BukkitPluginConfigReducer;
import ru.sulgik.minotated.config.PluginConfig;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.Set;

public class BukkitPluginAnnotationProcessor extends AbstractProcessor {

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        final Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(BukkitPlugin.class);
        if (elements.size() > 1) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Only one class can be annotated as BukkitPlugin");
            return true;
        } else if (elements.isEmpty()) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "One JavaPlugin class must be annotated as BukkitPlugin");
        }

        for (final Element element : elements) {
            if (element.getKind() != ElementKind.CLASS) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Annotation must be annotated to Class", element);
                return true;
            }

            PluginConfig.Builder configBuilder = new PluginConfig.Builder();

            configBuilder.setMainClass(element.getSimpleName().toString());

            final BukkitPlugin pluginAnnotation = element.getAnnotation(BukkitPlugin.class);
            new BukkitPluginConfigReducer(pluginAnnotation).reduce(configBuilder);

            PluginConfig config;
            try {
                config = configBuilder.build();
            } catch (NullPointerException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Necessary parameters can't be null", element);
                e.printStackTrace();
                return true;
            }
            final ObjectMapper mapper = getYAMLMapper();
            try {
                final FileObject file = processingEnv.getFiler()
                        .getResource(StandardLocation.CLASS_OUTPUT, "", "config.yml");

                mapper.writeValue(file.openWriter(), config);
            } catch (IOException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Can't write config to resources", element);
                e.printStackTrace();
                return true;
            }
        }

        return true;
    }

    private ObjectMapper getYAMLMapper() {
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
        mapper.findAndRegisterModules();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}
