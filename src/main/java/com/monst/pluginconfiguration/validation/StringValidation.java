package com.monst.pluginconfiguration.validation;

public interface StringValidation {

    static Bound<String> lowercase() {
        return Bound.requiring(s -> s.chars().allMatch(Character::isLowerCase), String::toLowerCase);
    }

    static Bound<String> uppercase() {
        return Bound.requiring(s -> s.chars().allMatch(Character::isUpperCase), String::toUpperCase);
    }

    static Bound<String> noSpaces() {
        return Bound.requiring(s -> s.chars().noneMatch(Character::isSpaceChar), s -> s.replaceAll("\\s", ""));
    }

    static Bound<String> maxLength(int length) {
        return Bound.requiring(s -> s.length() <= length, s -> s.substring(0, length));
    }

}
