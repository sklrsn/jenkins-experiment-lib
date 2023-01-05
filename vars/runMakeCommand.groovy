import org.sklrsn.utils.MakeCommand

def call(Map config) {
    print(config.command)
    if (config.command?.trim()) {
        throw new RuntimeException('Incorrect usage of runMakeCommand. Please check the inputs')
    }
    def command = MakeCommand.prepare(config.path, config.command)
    sh "${command}"
}
