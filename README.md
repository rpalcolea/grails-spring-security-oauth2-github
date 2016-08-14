Spring Security OAuth2 Github Plugin
====================================

[![Build Status](https://travis-ci.org/rpalcolea/grails-spring-security-oauth2-github.svg?branch=master)](https://travis-ci.org/rpalcolea/grails-spring-security-oauth2-github)
[ ![Download](https://api.bintray.com/packages/rpalcolea/plugins/spring-security-oauth2-github/images/download.svg) ](https://bintray.com/rpalcolea/plugins/spring-security-oauth2-github/_latestVersion)
[![Slack Signup](http://slack-signup.grails.org/badge.svg)](http://slack-signup.grails.org)

Add a Github OAuth2 provider to the [Spring Security OAuth2 Plugin](https://github.com/MatrixCrawler/grails-spring-security-oauth2) by @MatrixCrawler (Johannes Brunswicker).

Installation
------------
Add the following dependencies in `build.gradle`
```
dependencies {
...
    compile 'org.grails.plugins:spring-security-oauth2:1.+'
    compile 'org.grails.plugins:spring-security-oauth2-github:1.0.+'
...
}
```

Usage
-----
Add this to your application.yml
```
grails:
    plugin:
        springsecurity:
            oauth2:
                providers:
                    github:
                        api_key: 'github-client-id'             #needed
                        api_secret: 'github-client-secret'      #needed
                        successUri: "/oauth2/github/success"    #optional
                        failureUri: "/oauth2/github/failure"    #optional
                        callback: "/oauth2/github/callback"     #optional
```

You can replace the URIs with your own controller implementation.

In your view you can use the taglib exposed from this plugin and from OAuth plugin to create links and to know if the user is authenticated with a given provider:
```xml
<oauth2:connect provider="github" id="github-connect-link">Github</oauth2:connect>

Logged with Github?
<oauth2:ifLoggedInWith provider="github">yes</oauth2:ifLoggedInWith>
<oauth2:ifNotLoggedInWith provider="github">no</oauth2:ifNotLoggedInWith>
```
License
-------
Apache 2
