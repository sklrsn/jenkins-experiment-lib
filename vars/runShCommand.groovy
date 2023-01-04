import org.sklrsn.utils.ShCommand

def call(Map config) {
    def command = ShCommand.echoCmd(config.command)
    sh ${command}
}
