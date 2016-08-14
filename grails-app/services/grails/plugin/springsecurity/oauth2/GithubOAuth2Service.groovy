package grails.plugin.springsecurity.oauth2

import com.github.scribejava.apis.GitHubApi
import com.github.scribejava.core.builder.api.DefaultApi20
import com.github.scribejava.core.model.OAuth2AccessToken
import grails.converters.JSON
import grails.plugin.springsecurity.oauth2.exception.OAuth2Exception
import grails.plugin.springsecurity.oauth2.service.OAuth2AbstractProviderService
import grails.plugin.springsecurity.oauth2.token.OAuth2SpringToken
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import spring.security.oauth2.github.GithubOauth2SpringToken

@CompileStatic
@Slf4j
class GithubOAuth2Service extends OAuth2AbstractProviderService {

    static transactional = false

    String getProviderID() {
        'github'
    }

    Class<? extends DefaultApi20> getApiClass() {
        GitHubApi
    }

    String getProfileScope() {
        'https://api.github.com/user'
    }

    String getScopes() {
        'https://api.github.com/user'
    }

    String getScopeSeparator() {
        " "
    }

    OAuth2SpringToken createSpringAuthToken(OAuth2AccessToken accessToken) {
        def user
        def response = getResponse(accessToken)
        try {
            log.debug("JSON response body: {}", accessToken.rawResponse)
            user = JSON.parse(response.body)
        } catch (Exception e) {
            log.error("Error parsing response from {}. Response:\n{}", providerID, response.body)
            throw new OAuth2Exception("Error parsing response from " + providerID, e)
        }
        if (user && !user['mail']) {
            log.error("No user email from {}. Response was:\n{}", providerID, response.body)
            throw new OAuth2Exception("No user id from " + providerID)
        }
        new GithubOauth2SpringToken(accessToken, (String) user['mail'], providerID)
    }
}
