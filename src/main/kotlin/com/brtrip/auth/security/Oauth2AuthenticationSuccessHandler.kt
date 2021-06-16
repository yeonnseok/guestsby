package com.brtrip.auth.security

import com.brtrip.auth.domain.dto.TokenResponse
import com.brtrip.common.exceptions.BadRequestException
import com.brtrip.common.utils.CookieUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.security.web.savedrequest.HttpSessionRequestCache
import org.springframework.security.web.savedrequest.RequestCache
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class Oauth2AuthenticationSuccessHandler(
    private val tokenProvider: JwtTokenProvider,
    private val cookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository,
    @Value("\${app.authorized-redirect-uri:}")
    private val authorizedRedirectUri: String
) : SimpleUrlAuthenticationSuccessHandler() {

    private var requestCache: RequestCache = HttpSessionRequestCache()

    private val log = LoggerFactory.getLogger(javaClass)

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val savedRequest = requestCache.getRequest(request, response);

        if (savedRequest != null) {
            requestCache.removeRequest(request, response)
            clearAuthenticationAttributes(request, response)
        }

        val targetUrl = determineTargetUrl(request, response, authentication)

        if (response.isCommitted) {
            log.debug("Response has already been committed. Unable to redirect to $targetUrl")
            return
        }

        val jsonConverter = MappingJackson2HttpMessageConverter();
        val jsonMimeType = MediaType.APPLICATION_JSON;

        val token = TokenResponse(tokenProvider.createToken(authentication))
        if (jsonConverter.canWrite(token::class.java, jsonMimeType)) {
            jsonConverter.write(token, jsonMimeType, ServletServerHttpResponse(response))
        }
//        getRedirectStrategy().sendRedirect(request, response, targetUrl)
    }

    fun clearAuthenticationAttributes(request: HttpServletRequest, response: HttpServletResponse) {
        super.clearAuthenticationAttributes(request)
        cookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response)
    }

    override fun determineTargetUrl(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ): String {
        val redirectUri = CookieUtils.getCookie(
                request = request,
                name = cookieOAuth2AuthorizationRequestRepository.redirectUriParamCookieName
            )?.value

        if (redirectUri != null && !isAuthorizedRedirectUri(redirectUri)) {
            throw BadRequestException()
        }

//        val targetUrl = redirectUri ?: defaultTargetUrl

        val targetUrl = "http://localhost:8080"

        return UriComponentsBuilder.fromUriString(targetUrl)
            .queryParam("token", "123123r")
            .build().toUriString()
    }

    private fun isAuthorizedRedirectUri(uri: String): Boolean {
        val clientRedirectUri = URI.create(uri)
        val authorizedURI: URI = URI.create(authorizedRedirectUri)
        if (authorizedURI.getHost().equals(clientRedirectUri.getHost(), ignoreCase = true)
            && authorizedURI.getPort() == clientRedirectUri.getPort()
        ) {
            return true
        }
        return false
    }
}
