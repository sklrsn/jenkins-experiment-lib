package org.sklrsn.utils

class MakeCommand {

    static String prepare(String path, String command) {
        return path?.trim() ? "cd ${path} && make ${command}" : "make ${command}"
    }

}
