import org.sklrsn.actions.Alert
import org.sklrsn.models.Color
import org.sklrsn.models.Medium
import org.sklrsn.models.Status

def call(Map config) {
    if (config.medium?.trim() && config.jobname?.trim() && config.status?.trim() && config.buildnumber?.trim() && config.buildUrl?.trim()) {
        def message = Alert.generate(config)

        println("******************************")
        println(config.console)
        println("******************************")
        String color = Color.INFO
        if (config.color?.trim()) {
            color = config.color
        } else {
            switch (config.status) {
                case Status.SUCCESS:
                    color = Color.GOOD
                    break
                case Status.FAILURE:
                    color = Color.DANGER
                    break
                case Status.UNSTABLE:
                    color = Color.WARNING
                    break
                case Status.NORMAL:
                    color = Color.GOOD
                    break
                case Status.ABORTED:
                    color = Color.WARNING
                    break
            }
        }

        switch (config.medium) {
            case Medium.SLACK:
                def attachments =
                        [[
                                 text    : message,
                                 fallback: 'Please check the pipeline -> ${config.buildurl}',
                                 color   : color
                         ]]
                println("**********Slack Message *********")
                println(message)
                if (config.containsKey("slackChannels")) {
                    for (channel in config.slackChannels) {
                        slackSend(channel: channel, attachments: attachments)
                    }
                }
            case Medium.GITLAB:
                addGitLabMRComment comment: message
            case Medium.CONSOLE:
                println(message)
            default:
                throw new RuntimeException('Incorrect usage of buildAlert')
        }
        return
    }
    throw new RuntimeException('Incorrect usage of buildAlert')
}
