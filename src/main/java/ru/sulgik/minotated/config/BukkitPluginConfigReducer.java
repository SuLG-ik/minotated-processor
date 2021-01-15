package ru.sulgik.minotated.config;

import ru.sulgik.minotated.BukkitPlugin;

public class BukkitPluginConfigReducer implements ConfigReducer {

    final BukkitPlugin annotation;

    public BukkitPluginConfigReducer(BukkitPlugin annotation) {
        this.annotation = annotation;
    }


    @Override
    public PluginConfig.Builder reduce(PluginConfig.Builder builder) {
        return builder.setName(annotation.name())
                .setVersion(annotation.version());
    }
}
