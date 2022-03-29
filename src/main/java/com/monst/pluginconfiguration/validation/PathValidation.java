package com.monst.pluginconfiguration.validation;

import java.nio.file.Files;
import java.nio.file.Path;

public interface PathValidation {

    static Bound<Path> isAbsolute() {
        return Bound.requiring(Path::isAbsolute, Path::toAbsolutePath);
    }

    static Bound<Path> startsWith(Path base) {
        return Bound.requiring(path -> path.startsWith(base), base::resolve);
    }

    static Bound<Path> endsWith(Path end) {
        return Bound.requiring(path -> path.endsWith(end), path -> path.resolve(end));
    }

    static Bound<Path> isDirectory() {
        return Bound.requiring(Files::isDirectory, Path::getParent);
    }

    static Bound<Path> isFile(String fileType) {
        return Bound.requiring(path -> path.toString().endsWith("." + fileType),
                path -> path.resolveSibling(path.getFileName() + "." + fileType));
    }

}
