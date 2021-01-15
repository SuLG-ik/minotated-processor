package ru.sulgik.minotated.config;


import ru.sulgik.minotated.Preconditions;

import java.util.Objects;

public class PluginConfig {

    final private String name;
    final private String version;
    final private String main;

    public PluginConfig(String name, String version, String main) {
        this.name = name;
        this.version = version;
        this.main = main;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PluginConfig that = (PluginConfig) o;
        return name.equals(that.name) && version.equals(that.version) && main.equals(that.main);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, version, main);
    }

    @Override
    public String toString() {
        return "PluginConfig{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", main='" + main + '\'' +
                '}';
    }

    public static class Builder {

        private String name;
        private String version;
        private String main;

        public Builder setName(String value) {
            name = value;
            return this;
        }


        public Builder setVersion(String value) {
            version = version;
            return this;
        }


        public Builder setMainClass(String value) {
            main = value;
            return this;
        }


        public PluginConfig build() throws NullPointerException {
            Preconditions.notNull(name, "Name must set");
            Preconditions.notNull(version, "Version must set");
            Preconditions.notNull(main, "Main must set");

            return new PluginConfig(
                    name,
                    version,
                    main
            );
        }

    }

}
