package grails.plugin.springsecurity.oauth2.github

import grails.plugin.springsecurity.ReflectionUtils
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.oauth2.github.GithubOAuth2Service
import grails.plugin.springsecurity.oauth2.SpringSecurityOauth2BaseService
import grails.plugin.springsecurity.oauth2.exception.OAuth2Exception
import grails.plugins.Plugin
import groovy.util.logging.Slf4j

@Slf4j
class SpringSecurityOauth2GithubGrailsPlugin extends Plugin {

    def grailsVersion = "3.1.* > *"
    def loadAfter = ['spring-security-oauth2']
    def title = "Spring Security Oauth2 Github Provider"
    def author = "Roberto Perez Alcolea"
    def authorEmail = "roberto@perezalcolea.info"
    def description = 'Provides the capability to authenticate via github oauth provider. Depends on grails-spring-security-oauth2.'
    def profiles = ['web']
    def documentation = "http://grails.org/plugin/spring-security-oauth2-github"
    def license = "APACHE"
    def issueManagement = [system: "Github", url: "https://github.com/rpalcolea/spring-security-oauth2-github/issues"]
    def scm = [url: "https://github.com/rpalcolea/spring-security-oauth2-github"]

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
        SpringSecurityOauth2BaseService oAuth2BaseService = applicationContext.getBean('springSecurityOauth2BaseService', SpringSecurityOauth2BaseService)
        GithubOAuth2Service githubOAuth2Service = applicationContext.getBean('githubOAuth2Service', GithubOAuth2Service)
        try {
            oAuth2BaseService.registerProvider(githubOAuth2Service)
        } catch (OAuth2Exception e) {
            log.error("There was an oAuth2Exception", e)
            log.error("OAuth2 Github not loaded")
        }
    }
}
