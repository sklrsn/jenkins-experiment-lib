import org.sklrsn.utils.ShCommand

def call(Map config) {
    print(ShCommand.echoCmd(config.command))
}
