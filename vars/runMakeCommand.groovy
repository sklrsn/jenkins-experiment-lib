import org.sklrsn.utils.MakeCommand

def call(Map config) {
    if (config.command?.trim()) {
        def command = MakeCommand.prepare(config.path, config.command)
        sh "${command}"

        return
    }

    throw new RuntimeException('Incorrect usage of runMakeCommand')
}
