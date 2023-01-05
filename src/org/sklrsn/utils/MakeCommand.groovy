package org.sklrsn.utils

class MakeCommand {

    static String prepare(String path, String command) {
        return "cd ${path} && make ${command}"
    }

}
