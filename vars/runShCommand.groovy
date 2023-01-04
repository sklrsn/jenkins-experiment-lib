import org.sklrsn.utils.ShCommand

def call(Map config) {
    def cmd = config.command;
    ShCommand.echoCmd(cmd)
}
