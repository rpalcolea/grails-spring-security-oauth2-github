package spring.security.oauth2.github

import grails.plugins.*
import grails.plugin.springsecurity.ReflectionUtils
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.oauth2.SpringSecurityOauth2BaseService
import grails.plugin.springsecurity.oauth2.exception.OAuth2Exception
import org.slf4j.LoggerFactory
import grails.plugin.springsecurity.oauth2.GithubOAuth2Service

class SpringSecurityOauth2GithubGrailsPlugin extends Plugin {

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "3.1.* > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]
    List loadAfter = ['spring-security-oauth2']

    def title = "Spring Security Oauth2 Github Provider"
    def author = "Roberto Perez Alcolea"
    def authorEmail = "roberto@perezalcolea.info"
    def description = '''\
This plugin provides the capability to authenticate via github oauth provider. Depends on grails-spring-security-oauth2.
'''
    def profiles = ['web']

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/spring-security-oauth2-github"

    def license = "APACHE"

    def issueManagement = [system: "Github", url: "https://github.com/rpalcolea/spring-security-oauth2-github/issues"]

    def scm = [url: "https://github.com/rpalcolea/spring-security-oauth2-github"]

    def log

    Closure doWithSpring() {
        { ->
            ReflectionUtils.application = grailsApplication
            if (grailsApplication.warDeployed) {
                SpringSecurityUtils.resetSecurityConfig()
            }
            SpringSecurityUtils.application = grailsApplication

            // Check if there is an SpringSecurity configuration
            def coreConf = SpringSecurityUtils.securityConfig
            boolean printStatusMessages = (coreConf.printStatusMessages instanceof Boolean) ? coreConf.printStatusMessages : true
            if (!coreConf || !coreConf.active) {
                if (printStatusMessages) {
                    println("ERROR: There is no SpringSecurity configuration or SpringSecurity is disabled")
                    println("ERROR: Stopping configuration of SpringSecurity Oauth2")
                }
                return
            }

            if (!hasProperty('log')) {
                log = LoggerFactory.getLogger(SpringSecurityOauth2GithubGrailsPlugin)
            }

            if (printStatusMessages) {
                println("Configuring Spring Security OAuth2 Github plugin...")
            }
            SpringSecurityUtils.loadSecondaryConfig('DefaultOAuth2GithubConfig')
            if (printStatusMessages) {
                println("... finished configuring Spring Security OAuth2 Github\n")
            }
        }
    }

    /**
     * Register GithubOAuth2Service
     */
    void doWithApplicationContext() {
        log.trace("doWithApplicationContext")
        SpringSecurityOauth2BaseService oAuth2BaseService = grailsApplication.mainContext.getBean('springSecurityOauth2BaseService') as SpringSecurityOauth2BaseService
        GithubOAuth2Service githubOAuth2Service = grailsApplication.mainContext.getBean('githubOAuth2Service') as GithubOAuth2Service
        try {
            oAuth2BaseService.registerProvider(githubOAuth2Service)
        } catch (OAuth2Exception exception) {
            log.error("There was an oAuth2Exception", exception)
            log.error("OAuth2 Github not loaded")
        }

    }

}
