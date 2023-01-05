import org.sklrsn.utils.ShCommand

def call(Map config) {
    print(config.command)
    if (config.command?.trim()) {
        throw new RuntimeException('Incorrect usage of runShCommand. Please check the inputs')
    }

    def parsedCmd = ShCommand.parse(config.command)
    sh "${parsedCmd}"
}
