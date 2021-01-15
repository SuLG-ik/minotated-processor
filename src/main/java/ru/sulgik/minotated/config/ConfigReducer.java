package ru.sulgik.minotated.config;

public interface ConfigReducer {

    PluginConfig.Builder reduce(PluginConfig.Builder builder);

}
