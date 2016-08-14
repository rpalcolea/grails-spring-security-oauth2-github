package spring.security.oauth2.github

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration
import grails.plugins.metadata.PluginSource

@PluginSource
class Application extends GrailsAutoConfiguration {
    static void main(String[] args) {
        GrailsApp.run(Application, args)
    }
}
