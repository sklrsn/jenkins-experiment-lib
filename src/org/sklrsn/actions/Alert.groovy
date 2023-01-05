package org.sklrsn.actions

import org.sklrsn.models.Delimiter
import org.sklrsn.models.Medium
import org.sklrsn.models.Status

class Alert {

    static String generate(String app, Map params) {
        switch (app) {
            case Medium.SLACK:
                return new Slack().generate(params)
            case Medium.GITLAB:
                return new Gitlab().generate(params)
        }
        return ''
    }

}

abstract class Report {

    abstract String generate(Map params)

    String report(String delimiter, Map params) {
        StringBuilder sb = new StringBuilder()
        switch (params.status) {
            case Status.SUCCESS:
                sb.append("${Status.SUCCESS} [${params.buildnumber}]").append(delimiter).append(params.buildurl)
                break

            case Status.FAILURE:
                sb.append("${Status.FAILURE} [${params.buildnumber}]").append(delimiter).append(params.buildurl)
                break

            case Status.ABORTED:
                sb.append("${Status.ABORTED} [${params.buildnumber}]").append(delimiter).append(params.buildurl)
                break

            case Status.NORMAL:
                sb.append("${Status.NORMAL} [${params.buildnumber}]").append(delimiter).append(params.buildurl)
                break

            case Status.UNSTABLE:
                sb.append("${Status.UNSTABLE} [${params.buildnumber}]").append(delimiter).append(params.buildurl)
                break
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