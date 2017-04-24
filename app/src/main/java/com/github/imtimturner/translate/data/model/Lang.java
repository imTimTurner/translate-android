package com.github.imtimturner.translate.data.model;

/**
 * Описывает язык
 * */
public class Lang {

    private final String code;
    private String name;

    public Lang(String code) {
        if (code == null) {
            throw new IllegalArgumentException("Lang code cannot be null.");
        }
        if (code.length() < 1) {
            throw new IllegalArgumentException("Lang code cannot be empty.");
        }

        this.code = code;
    }

    public Lang(String code, String name) {
        this(code);
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Lang)) {
            return false;
        }
        Lang other = (Lang) obj;
        return code.equalsIgnoreCase(other.code);
    }

    @Override
    public int hashCode() {
        int result = 42;
        result = 37 * result + code.hashCode();
        result = 37 * result + (name == null ? 0 : name.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return name == null ? "" : name;
    }

}
