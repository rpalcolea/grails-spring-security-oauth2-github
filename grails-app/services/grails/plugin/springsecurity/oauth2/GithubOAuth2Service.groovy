package grails.plugin.springsecurity.oauth2

import com.github.scribejava.apis.GitHubApi
import com.github.scribejava.core.builder.api.DefaultApi20
import com.github.scribejava.core.model.OAuth2AccessToken
import grails.converters.JSON
import grails.plugin.springsecurity.oauth2.exception.OAuth2Exception
import grails.plugin.springsecurity.oauth2.service.OAuth2AbstractProviderService
import grails.plugin.springsecurity.oauth2.token.OAuth2SpringToken
import grails.transaction.Transactional
import spring.security.oauth2.github.GithubOauth2SpringToken

@Transactional
class GithubOAuth2Service extends OAuth2AbstractProviderService {
    @Override
    String getProviderID() {
        return 'github'
    }

    @Override
    Class<? extends DefaultApi20> getApiClass() {
        GitHubApi.class
    }

    @Override
    String getProfileScope() {
        return 'https://api.github.com/user'
    }

    @Override
    String getScopes() {
        return 'https://api.github.com/user'
    }

    @Override
    String getScopeSeparator() {
        return " "
    }

    @Override
    OAuth2SpringToken createSpringAuthToken(OAuth2AccessToken accessToken) {
        def user
        def response = getResponse(accessToken)
        try {
            log.debug("JSON response body: " + accessToken.rawResponse)
            user = JSON.parse(response.body)
        } catch (Exception exception) {
            log.error("Error parsing response from " + getProviderID() + ". Response:\n" + response.body)
            throw new OAuth2Exception("Error parsing response from " + getProviderID(), exception)
        }
        if (!user?.email) {
            log.error("No user email from " + getProviderID() + ". Response was:\n" + response.body)
            throw new OAuth2Exception("No user id from " + getProviderID())
        }
        new GithubOauth2SpringToken(accessToken, user?.email, providerID)
    }
}
