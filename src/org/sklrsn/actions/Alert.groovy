package org.sklrsn.actions

import org.sklrsn.models.*

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
                for (stage in stages) {
                    switch (stage) {
                        case Stage.UNIT_TESTS:
                            sb.append('Unit - ').append(params.buildUrl).append(Artifacts.UNIT).append(delimiter)
                            break
                        case Stage.SMOKE_TESTS:
                            sb.append('Smoke - ').append(params.buildUrl).append(Artifacts.SMOKE).append(delimiter)
                            break
                        case Stage.BUILD_BINARIES:
                            sb.append('Failed to compile binaries').append(delimiter)
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

        if (params.console?.trim()) {
            sb.append(params.console).append(delimiter).append(delimiter)
            sb.append('more logs at ').append(params.buildUrl).append('consoleFull').append(delimiter)
        }

        if (params.changes?.trim()) {
            sb.append('changelog:').append(delimiter)
            sb.append(params.changes)
        }

        return sb.toString()
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
