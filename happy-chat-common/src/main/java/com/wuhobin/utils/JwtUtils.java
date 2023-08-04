package com.wuhobin.utils;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wuhongbin
 * @create 2023/01/13 10:43
 * @desc jwt加解密
 **/
@Slf4j
public class JwtUtils {

    /**
     * 签名秘钥
     */
    private static final String BASE64_SECRET = "jwt_common_sign_key";

    /**
     * 用于JWT加密的密匙
     */
    private static final String DATA_KEY = "jwt_common_jwt_key";


    /**
     * 生成JWT字符串
     */
    public static String generateJWT(Long userId) {
        //签名算法，选择SHA-256
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //将BASE64SECRET常量字符串使用base64解码成字节数组
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(BASE64_SECRET);
        //使用HmacSHA256签名算法生成一个HS256的签名秘钥Key
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        //添加构成JWT的参数
        Map<String, Object> headMap = new HashMap<>();
        headMap.put("alg", SignatureAlgorithm.HS256.getValue());
        headMap.put("typ", "JWT");
        JwtBuilder builder = null;
        try {
            builder = Jwts.builder().setHeader(headMap)
                    .claim("userId", AESUtil.encrypt(String.valueOf(userId), DATA_KEY))
                    .claim("createTime", AESUtil.encrypt(DateUtil.getDateTimeStr(new Date()), DATA_KEY))
                    .signWith(signatureAlgorithm, signingKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.compact();
    }


    /**
     * 判断令牌是否过期
     * @param token
     * @return
     */
    public static Boolean isTokenExpired(String token) {
        try {
            Claims claims = parseJWT(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }


    /**
     *
     * 验证Token是否过期
     * @param token
     * @return true表示没有过期，false表示过期
     */
    public static boolean validateToken(String token) {
        return !isTokenExpired(token);
    }


    /**
     * 解析JWT
     */
    private static Claims parseJWT(String jsonWebToken) {
        Claims claims = null;
        try {
            if (StringUtils.isNotBlank(jsonWebToken)) {
                //解析jwt
                claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(BASE64_SECRET))
                        .parseClaimsJws(jsonWebToken).getBody();
                return claims;
            } else {
                log.info("token为空！重新获取用户信息！");
            }
        } catch (Exception e) {
            log.info("token为空！可能因为token已经超时或非法token", e);
        }
        return null;
    }



    public static Long getUserIdByToken(String jsonWebToken) {
        Claims claims = parseJWT(jsonWebToken);
        String decryptUserId = null;
        if (claims != null) {
            try {
                decryptUserId = AESUtil.decrypt((String) claims.get("userId"), DATA_KEY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (decryptUserId != null){
            return Long.valueOf(decryptUserId);
        }
        return null;
    }
}
