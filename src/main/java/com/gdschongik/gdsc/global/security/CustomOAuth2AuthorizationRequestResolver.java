package com.gdschongik.gdsc.global.security;

import static com.gdschongik.gdsc.global.common.constant.SecurityConstant.*;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.web.util.UriComponentsBuilder;

public class CustomOAuth2AuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    private final DefaultOAuth2AuthorizationRequestResolver delegate;

    public CustomOAuth2AuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
        this.delegate =
                new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        OAuth2AuthorizationRequest authorizationRequest = delegate.resolve(request);
        return authorizationRequest != null ? customizeAuthorizationRequest(request, authorizationRequest) : null;
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        OAuth2AuthorizationRequest authorizationRequest = delegate.resolve(request, clientRegistrationId);
        return authorizationRequest != null ? customizeAuthorizationRequest(request, authorizationRequest) : null;
    }

    private OAuth2AuthorizationRequest customizeAuthorizationRequest(
            HttpServletRequest request, OAuth2AuthorizationRequest authorizationRequest) {

        String referer = request.getHeader("Referer");

        if (referer == null || referer.isEmpty()) {
            return authorizationRequest;
        }

        String redirectUri = UriComponentsBuilder.fromHttpUrl(authorizationRequest.getRedirectUri())
                .queryParam(OAUTH_TARGET_URL_PARAM_NAME, referer)
                .toUriString();

        return OAuth2AuthorizationRequest.from(authorizationRequest)
                .redirectUri(redirectUri)
                .build();
    }
}
