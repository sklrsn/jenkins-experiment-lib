import org.sklrsn.utils.ShCommand

def call(Map config) {
    if (config.command?.trim()) {
        throw new RuntimeException('Incorrect usage of runMakeCommand. Please check the inputs')
    }

    def parsedCmd = ShCommand.parse(config.command)
    sh "${parsedCmd}"
}
