package com.brtrip.auth.domain

import com.brtrip.auth.domain.dto.GoogleOAuth2UserInfo
import com.brtrip.auth.domain.dto.KakaoOAuth2UserInfo
import com.brtrip.auth.domain.dto.OAuth2UserInfo
import com.brtrip.user.domain.AuthProvider


class OAuth2UserInfoFactory {
    companion object {

        fun getOAuth2UserInfo(registrationId: String, attributes: Map<String, Any>): OAuth2UserInfo {
            return when {
                AuthProvider.GOOGLE.equalWith(registrationId) -> {
                    GoogleOAuth2UserInfo(attributes)
                }
                AuthProvider.KAKAO.equalWith(registrationId) -> {
                    KakaoOAuth2UserInfo(attributes)
                }
                else -> {
                    throw RuntimeException("$registrationId 로그인은 지원하지 않습니다.");
                }
            }
        }
    }
}
