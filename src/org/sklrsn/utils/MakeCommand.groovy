package org.sklrsn.utils

class MakeCommand {

    static String prepare(String path, String command) {
        return path?.trim() ? "make ${command}" : "cd ${path} && make ${command}"
    }

}
