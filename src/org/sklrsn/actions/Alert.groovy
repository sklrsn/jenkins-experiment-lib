package org.sklrsn.actions


import org.sklrsn.models.Delimiter
import org.sklrsn.models.Medium
import org.sklrsn.models.Stage
import org.sklrsn.models.Status
import org.sklrsn.resources.Artifacts

class Alert {

    static String generate(Map params) {
        switch (params.medium) {
            case Medium.SLACK:
                return new Slack().generate(params)
            case Medium.GITLAB:
                return new Gitlab().generate(params)
            case Medium.CONSOLE:
                return new Console().generate(params)
        }
        return ''
    }

}

abstract class Report {

    abstract String generate(Map params)

    String report(String delimiter, Map params) {
        StringBuilder sb = new StringBuilder()
        // If BlueOcean is unavailable, fallback to classic page
        String buildUrl = params.displayUrl?.trim() ? params.displayUrl : params.buildUrl
        switch (params.status) {
            case Status.SUCCESS:
                sb.append("${Status.SUCCESS}:${params.jobname}-[${params.buildnumber}]").append(delimiter)
                sb.append("Pipeline - ${buildUrl}").append(delimiter)
                break

            case Status.FAILURE:
                sb.append("${Status.FAILURE}:${params.jobname}-[${params.buildnumber}]").append(delimiter)
                sb.append("Pipeline - ${buildUrl}").append(delimiter)

                Set<String> stages = params.stages
                println(stages)
                for (stage in stages) {
                    switch (stage) {
                        case Stage.UNIT_TESTS:
                            println("***********************")
                            println(params.console)
                            println(sb)
                            println(Stage.UNIT_TESTS)
                            println(delimiter)
                            println("***********************")
                           /* sb.append("${Stage.UNIT_TESTS} - ").append(params.buildUrl).append(Artifacts.UNIT).append(delimiter)
                            if (params.console && params.console.containsKey(Stage.UNIT_TESTS)) {
                                sb.append("Console:").append(delimiter)
                                sb.append(params.console.get(Stage.UNIT_TESTS)).append(delimiter)
                                sb.append('more logs at ').append(params.buildUrl).append('consoleFull').append(delimiter)
                            }*/
                            //appendConsoleLogs(params.console, sb, Stage.UNIT_TESTS, delimiter)
                            break
                        case Stage.SMOKE_TESTS:
                            sb.append("${Stage.SMOKE_TESTS} - ").append(params.buildUrl).append(Artifacts.SMOKE).append(delimiter)
                            if (params.console && params.console.containsKey(Stage.SMOKE_TESTS)) {
                                sb.append("Console:").append(delimiter)
                                sb.append(params.console.get(Stage.SMOKE_TESTS)).append(delimiter)
                                sb.append('more logs at ').append(params.buildUrl).append('consoleFull').append(delimiter)
                            }
                            break
                        case Stage.BUILD_BINARIES:
                            sb.append("${Stage.BUILD_BINARIES} - Failed to compile binaries").append(delimiter)
                            if (params.console && params.console.containsKey(Stage.BUILD_BINARIES)) {
                                sb.append("Console:").append(delimiter)
                                sb.append(params.console.get(Stage.BUILD_BINARIES)).append(delimiter)
                                sb.append('more logs at ').append(params.buildUrl).append('consoleFull').append(delimiter)
                            }
                            break
                    }
                }

                break

            case Status.ABORTED:
                sb.append("${Status.ABORTED}:${params.jobname}-[${params.buildnumber}]").append(delimiter)
                sb.append("Pipeline - ${buildUrl}").append(delimiter)
                break

            case Status.NORMAL:
                sb.append("${Status.NORMAL}:${params.jobname}-[${params.buildnumber}]").append(delimiter)
                sb.append("Pipeline - ${buildUrl}").append(delimiter)
                break

            case Status.UNSTABLE:
                sb.append("${Status.UNSTABLE}:${params.jobname}-[${params.buildnumber}]").append(delimiter)
                sb.append("Pipeline - ${buildUrl}").append(delimiter)
                break
        }


        if (params.changes?.trim()) {
            sb.append(delimiter)
            sb.append('changelog:').append(delimiter)
            sb.append(params.changes)
        }

        return sb.toString()
    }

    private appendConsoleLogs(Map params, StringBuilder sb, String stage, String delimiter) {
        if (params.containsKey("console") && params.console.containsKey(stage)) {
            sb.append("Console:").append(delimiter).append(params.console.get(stage)).append(delimiter)
            sb.append('more logs at ').append(params.buildUrl).append('consoleFull').append(delimiter)
        }
    }
}

class Slack extends Report {

    @Override
    String generate(Map params) {
        return this.report(Delimiter.EOL, params)
    }

}

class Gitlab extends Report {

    @Override
    String generate(Map params) {
        return this.report(Delimiter.BR, params)
    }

}

class Console extends Report {

    @Override
    String generate(Map params) {
        return this.report(Delimiter.EOL, params)
    }

}
