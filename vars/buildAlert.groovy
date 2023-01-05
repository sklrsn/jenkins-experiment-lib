import org.sklrsn.actions.Alert
import org.sklrsn.models.Color
import org.sklrsn.models.Medium
import org.sklrsn.models.Status

def call(Map config) {
    if (config.medium?.trim() && config.jobname?.trim() && config.status?.trim() && config.buildnumber?.trim() && config.buildurl?.trim()) {
        def message = Alert.generate(config)

        String channel = config.channel?.trim() ? config.channel.trim() : '#build'
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
                def attachments = [
                        [
                                text    : message,
                                fallback: '',
                                color   : color
                        ]
                ]
                slackSend(channel: channel, attachments: attachments)
                //slackSend(color: color, channel: channel, message: message)
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
