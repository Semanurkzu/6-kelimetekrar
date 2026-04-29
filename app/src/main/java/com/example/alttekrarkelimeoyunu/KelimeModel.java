package com.example.alttekrarkelimeoyunu;

import java.util.List;

public class KelimeModel {
    private String word;
    private List<Meaning> meanings;

    public static class Meaning {
        private List<Definition> definitions;

        public List<Definition> getDefinitions() {
            return definitions;
        }
    }

    public static class Definition {
        private String definition;
        private String example;

        public String getDefinition() {
            return definition;
        }

        public String getExample() {
            return example;
        }
    }

    public String getWord() {
        return word;
    }

    public List<Meaning> getMeanings() {
        return meanings;
    }
}