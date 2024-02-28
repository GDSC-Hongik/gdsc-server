package com.gdschongik.gdsc.global.security;

import static com.gdschongik.gdsc.global.common.constant.SecurityConstant.*;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    private final DefaultOAuth2AuthorizationRequestResolver delegate;

    public CustomAuthorizationRequestResolver(
            ClientRegistrationRepository clientRegistrationRepository, String authorizationRequestBaseUri) {
        this.delegate = new DefaultOAuth2AuthorizationRequestResolver(
                clientRegistrationRepository, authorizationRequestBaseUri);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        return delegateResolve(request, null);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        return delegateResolve(request, clientRegistrationId);
    }

    private OAuth2AuthorizationRequest delegateResolve(
            HttpServletRequest request, @Nullable String clientRegistrationId) {
        OAuth2AuthorizationRequest resolved = clientRegistrationId == null
                ? delegate.resolve(request)
                : delegate.resolve(request, clientRegistrationId);

        return extractRedirectUri(request)
                .map(uri -> addRedirectUriToRequest(resolved, uri))
                .orElse(resolved);
    }

    private Optional<String> extractRedirectUri(HttpServletRequest request) {
        return request.getParameterMap().containsKey(REDIRECT_URI_PARAM)
                ? Optional.of(request.getParameterMap().get(REDIRECT_URI_PARAM)[0])
                : Optional.empty();
    }

    private OAuth2AuthorizationRequest addRedirectUriToRequest(
            OAuth2AuthorizationRequest resolved, String redirectUri) {
        return OAuth2AuthorizationRequest.from(resolved)
                .additionalParameters(Map.of(OAUTH_REDIRECT_URI_COOKIE_NAME, redirectUri))
                .build();
    }
}
