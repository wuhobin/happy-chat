package com.wuhobin.utils.http;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.CookieGenerator;

import javax.net.ssl.*;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.*;
import java.net.Proxy.Type;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * 网络工具类。
 *
 * @author huxiaohui
 * @since 1.0,28 ,4 , 2023
 */
@Slf4j
public abstract class WebUtils {

    private static final String     DEFAULT_CHARSET = "UTF-8";
    private static final String     METHOD_POST     = "POST";
    private static final String     METHOD_GET      = "GET";

    private static SSLContext       ctx             = null;

    private static HostnameVerifier verifier        = null;

    private static SSLSocketFactory socketFactory   = null;

    private static class DefaultTrustManager implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] chain,
                                       String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain,
                                       String authType) throws CertificateException {
        }
    }

    static {

        try {
            ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[] { new DefaultTrustManager() },
                    new SecureRandom());

            ctx.getClientSessionContext().setSessionTimeout(15);
            ctx.getClientSessionContext().setSessionCacheSize(1000);

            socketFactory = ctx.getSocketFactory();
        } catch (Exception e) {

        }

        verifier = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return false;//默认认证不通过，进行证书校验。
            }
        };

    }

    private WebUtils() {
    }

    /**
     * 执行HTTP POST请求，可使用代理proxy。
     *
     * @param url 请求地址
     * @param jsonParams 请求参数(json字符串)
     * @param connectTimeout 连接超时时间
     * @param readTimeout 请求超时时间
     * @return 响应字符串
     * @throws IOException
     */
    public static String doPost(String url, String jsonParams,
                                int connectTimeout, int readTimeout) throws IOException {
        String ctype = "application/json;charset=" + DEFAULT_CHARSET;
        byte[] content = {};
        if (StringUtils.isNotEmpty(jsonParams)) {
            content = jsonParams.getBytes(DEFAULT_CHARSET);
        }
        return doPost(url, ctype, content, connectTimeout, readTimeout, null, -1);
    }

    /**
     * 执行HTTP POST请求，可使用代理proxy。
     *
     * @param url 请求地址
     * @param headers 请求头
     * @param jsonParams 请求参数(json字符串)
     * @param connectTimeout 连接超时时间
     * @param readTimeout 请求超时时间
     * @return 响应字符串
     * @throws IOException
     */
    public static String doPost(String url,Map<String,String> headers, String jsonParams,String charset,
                                int connectTimeout, int readTimeout) throws IOException {
        headers.put("Content-Type", "application/json;charset=UTF-8");
        byte[] content = {};
        if (StringUtils.isNotEmpty(charset)) {
            content = jsonParams.getBytes(charset);
        }else {
            content = jsonParams.getBytes(DEFAULT_CHARSET);
        }
        return doPost(url, headers, content, connectTimeout, readTimeout);
    }

    /**
     * 执行HTTP POST请求，可使用代理proxy。
     *
     * @param url 请求地址
     * @param jsonParams 请求参数(json字符串)
     * @param connectTimeout 连接超时时间
     * @param readTimeout 请求超时时间
     * @return 响应字符串
     * @throws IOException
     */
    public static String doPost(String url, String jsonParams,String charset,
                                int connectTimeout, int readTimeout) throws IOException {
        String ctype = "application/json;charset=" + charset;
        byte[] content = {};
        if (StringUtils.isNotEmpty(charset)) {
            content = jsonParams.getBytes(charset);
        }
        return doPost(url, ctype, content, connectTimeout, readTimeout, null, -1);
    }

    /**
     * 执行HTTP POST请求，可使用代理proxy。
     *
     * @param url 请求地址
     * @param params 请求参数
     * @param connectTimeout 连接超时时间
     * @param readTimeout 请求超时时间
     * @return 响应字符串
     * @throws IOException
     */
    public static String doPost(String url, Map<String, String> params,
                                int connectTimeout, int readTimeout) throws IOException {
        String ctype = "application/x-www-form-urlencoded;charset=" + DEFAULT_CHARSET;
        String query = buildQuery(params, DEFAULT_CHARSET);
        byte[] content = {};
        if (query != null) {
            content = query.getBytes(DEFAULT_CHARSET);
        }
        return doPost(url, ctype, content, connectTimeout, readTimeout, null, -1);
    }

    /**
     * 执行HTTP POST请求，可使用代理proxy。
     *
     * @param url 请求地址
     * @param params 请求参数
     * @param charset 字符集，如UTF-8, GBK, GB2312
     * @param connectTimeout 连接超时时间
     * @param readTimeout 请求超时时间
     * @return 响应字符串
     * @throws IOException
     */
    public static String doPost(String url, Map<String, String> params, String charset,
                                int connectTimeout, int readTimeout) throws IOException {
        String ctype = "application/x-www-form-urlencoded;charset=" + charset;
        String query = buildQuery(params, charset);
        byte[] content = {};
        if (query != null) {
            content = query.getBytes(charset);
        }
        return doPost(url, ctype, content, connectTimeout, readTimeout, null, -1);
    }

    /**
     * 执行HTTP POST请求，可使用代理proxy。
     *
     * @param url 请求地址
     * @param params 请求参数
     * @param charset 字符集，如UTF-8, GBK, GB2312
     * @param connectTimeout 连接超时时间
     * @param readTimeout 请求超时时间
     * @param proxyHost 代理host，传null表示不使用代理
     * @param proxyPort 代理端口，传0表示不使用代理
     * @return 响应字符串
     * @throws IOException
     */
    public static String doPost(String url, Map<String, String> params, String charset,
                                int connectTimeout, int readTimeout, String proxyHost,
                                int proxyPort) throws IOException {
        String ctype = "application/x-www-form-urlencoded;charset=" + charset;
        String query = buildQuery(params, charset);
        byte[] content = {};
        if (query != null) {
            content = query.getBytes(charset);
        }
        return doPost(url, ctype, content, connectTimeout, readTimeout, proxyHost, proxyPort);
    }

    /**
     * 执行HTTP POST请求。
     *
     * @param url 请求地址
     * @param ctype 请求类型
     * @param content 请求字节数组
     * @param connectTimeout 连接超时时间
     * @param readTimeout 请求超时时间
     * @param proxyHost 代理host，传null表示不使用代理
     * @param proxyPort 代理端口，传0表示不使用代理
     * @return 响应字符串
     * @throws IOException
     */
    public static String doPost(String url, String ctype, byte[] content, int connectTimeout,
                                int readTimeout, String proxyHost, int proxyPort) throws IOException {
        HttpURLConnection conn = null;
        OutputStream out = null;
        String rsp = null;
        try {
            try {
                conn = null;
                if (!StringUtils.isEmpty(proxyHost)) {
                    conn = getConnection(new URL(url), METHOD_POST, ctype, proxyHost, proxyPort);
                } else {
                    conn = getConnection(new URL(url), METHOD_POST, ctype);
                }
                conn.setConnectTimeout(connectTimeout);
                conn.setReadTimeout(readTimeout);
            } catch (IOException e) {
                log.error("url:{},ctype={},参数类型为bytes", url, ctype, e);
                throw e;
            }
            try {
                out = conn.getOutputStream();
                out.write(content);
                rsp = getResponseAsString(conn);
            } catch (IOException e) {
                log.error("url:{},ctype={},参数类型为bytes", url, ctype, e);
                throw e;
            }

        } finally {
            if (out != null) {
                out.close();
            }
            if (conn != null) {
                conn.disconnect();

            }
        }

        return rsp;
    }

    /**
     * 执行HTTP POST请求。
     *
     * @param url 请求地址
     * @param headers 请求头
     * @param content 请求字节数组
     * @param connectTimeout 连接超时时间
     * @param readTimeout 请求超时时间
     * @return 响应字符串
     * @throws IOException
     */
    public static String doPost(String url, Map<String,String> headers, byte[] content, int connectTimeout,
                                int readTimeout) throws IOException {
        HttpURLConnection conn = null;
        OutputStream out = null;
        String rsp = null;
        try {
            try {
                conn = getConnection(new URL(url), METHOD_POST, headers);
                conn.setConnectTimeout(connectTimeout);
                conn.setReadTimeout(readTimeout);
            } catch (IOException e) {
                log.error("url:{},headers={},参数类型为bytes", url, headers, e);
                throw e;
            }
            try {
                out = conn.getOutputStream();
                out.write(content);
                rsp = getResponseAsString(conn);
            } catch (IOException e) {
                log.error("url:{},headers={},参数类型为bytes", url, headers, e);
                throw e;
            }

        } finally {
            if (out != null) {
                out.close();
            }
            if (conn != null) {
                conn.disconnect();

            }
        }

        return rsp;
    }

    /**
     * 执行HTTP GET请求。
     *
     * @param url 请求地址
     * @param params 请求参数
     * @return 响应字符串
     * @throws IOException
     */
    public static String doGet(String url, Map<String, String> params) throws IOException {
        return doGet(url, params, DEFAULT_CHARSET);
    }

    /**
     * 执行HTTP GET请求。
     *
     * @param url 请求地址
     * @param params 请求参数
     * @return 响应字符串
     * @throws IOException
     */
    public static String doGet(String url, Map<String, String> params,int connectTimeout,
                               int readTimeout) throws IOException {
        return doGet(url, params, DEFAULT_CHARSET,connectTimeout,readTimeout);
    }

    /**
     * 执行HTTP GET请求。
     *
     * @param url 请求地址
     * @param params 请求参数
     * @param charset 字符集，如UTF-8, GBK, GB2312
     * @return 响应字符串
     * @throws IOException
     */
    public static String doGet(String url, Map<String, String> params,
                               String charset) throws IOException {
        HttpURLConnection conn = null;
        String rsp = null;

        try {
            String ctype = "application/x-www-form-urlencoded;charset=" + charset;
            String query = buildQuery(params, charset);
            try {
                conn = getConnection(buildGetUrl(url, query), METHOD_GET, ctype);
            } catch (IOException e) {
                log.error("url:{},params:{}", url, params, e);
                throw e;
            }

            try {
                rsp = getResponseAsString(conn);
            } catch (IOException e) {
                log.error("url:{},params:{}", url, params, e);
                throw e;
            }

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return rsp;
    }

    /**
     * 执行HTTP GET请求。
     *
     * @param url 请求地址
     * @param params 请求参数
     * @param charset 字符集，如UTF-8, GBK, GB2312
     * @return 响应字符串
     * @param connectTimeout 连接超时时间
     * @param readTimeout 请求超时时间
     * @throws IOException
     */
    public static String doGet(String url, Map<String, String> params,
                               String charset, int connectTimeout,
                               int readTimeout) throws IOException {
        HttpURLConnection conn = null;
        String rsp = null;

        try {
            String ctype = "application/x-www-form-urlencoded;charset=" + charset;
            String query = buildQuery(params, charset);
            try {
                conn = getConnection(buildGetUrl(url, query), METHOD_GET, ctype);
                conn.setConnectTimeout(connectTimeout);
                conn.setReadTimeout(readTimeout);
            } catch (IOException e) {
                log.error("url:{},params:{}", url, params, e);
                throw e;
            }

            try {
                rsp = getResponseAsString(conn);
            } catch (IOException e) {
                log.error("url:{},params:{}", url, params, e);
                throw e;
            }

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return rsp;
    }

    private static HttpURLConnection getConnection(URL url, String method, Map<String,String> headers) throws IOException {
        return getConnection(url, method, headers, null);
    }
    private static HttpURLConnection getConnection(URL url, String method, String ctype) throws IOException {
        return getConnection(url, method, ctype, null);
    }

    private static HttpURLConnection getConnection(URL url, String method, String ctype,
                                                   String proxyHost, int proxyPort) throws IOException {
        Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
        return getConnection(url, method, ctype, proxy);
    }

    private static HttpURLConnection getConnection(URL url, String method, String ctype, Proxy proxy) throws IOException {
        HttpURLConnection conn = null;
        if ("https".equals(url.getProtocol())) {
            HttpsURLConnection connHttps = null;
            if (proxy != null) {
                connHttps = (HttpsURLConnection) url.openConnection(proxy);
            } else {
                connHttps = (HttpsURLConnection) url.openConnection();
            }
            connHttps.setSSLSocketFactory(socketFactory);
            connHttps.setHostnameVerifier(verifier);
            conn = connHttps;
        } else {
            conn = null;
            if (proxy != null) {
                conn = (HttpURLConnection) url.openConnection(proxy);
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }
        }

        conn.setRequestMethod(method);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Accept", "*/*");
//        conn.setRequestProperty("User-Agent", "aop-sdk-java");
        conn.setRequestProperty("Content-Type", ctype);
        return conn;
    }

    private static HttpURLConnection getConnection(URL url, String method, Map<String,String> headers, Proxy proxy) throws IOException {
        HttpURLConnection conn = null;
        if ("https".equals(url.getProtocol())) {
            HttpsURLConnection connHttps = null;
            if (proxy != null) {
                connHttps = (HttpsURLConnection) url.openConnection(proxy);
            } else {
                connHttps = (HttpsURLConnection) url.openConnection();
            }
            connHttps.setSSLSocketFactory(socketFactory);
            connHttps.setHostnameVerifier(verifier);
            conn = connHttps;
        } else {
            conn = null;
            if (proxy != null) {
                conn = (HttpURLConnection) url.openConnection(proxy);
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }
        }

        conn.setRequestMethod(method);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Accept", "text/xml,text/javascript,text/html,*/*");
        if(MapUtils.isNotEmpty(headers)){
            for(String key : headers.keySet()){
                conn.setRequestProperty(key, headers.get(key));
            }
        }
        return conn;
    }

    private static URL buildGetUrl(String strUrl, String query) throws IOException {
        URL url = new URL(strUrl);
        if (StringUtils.isEmpty(query)) {
            return url;
        }

        if (StringUtils.isEmpty(url.getQuery())) {
            if (strUrl.endsWith("?")) {
                strUrl = strUrl + query;
            } else {
                strUrl = strUrl + "?" + query;
            }
        } else {
            if (strUrl.endsWith("&")) {
                strUrl = strUrl + query;
            } else {
                strUrl = strUrl + "&" + query;
            }
        }

        return new URL(strUrl);
    }

    public static String buildQuery(Map<String, String> params, String charset) throws IOException {
        if (params == null || params.isEmpty()) {
            return null;
        }

        StringBuilder query = new StringBuilder();
        Set<Entry<String, String>> entries = params.entrySet();
        boolean hasParam = false;

        for (Entry<String, String> entry : entries) {
            String name = entry.getKey();
            String value = entry.getValue();
            // 忽略参数名或参数值为空的参数
            if (StringUtils.isNoneEmpty(name, value)) {
                if (hasParam) {
                    query.append("&");
                } else {
                    hasParam = true;
                }

                query.append(name).append("=").append(URLEncoder.encode(value, charset));
            }
        }

        return query.toString();
    }

    protected static String getResponseAsString(HttpURLConnection conn) throws IOException {
        String charset = getResponseCharset(conn.getContentType());
        InputStream es = conn.getErrorStream();
        if (es == null) {
            return getStreamAsString(conn.getInputStream(), charset);
        } else {
            String msg = getStreamAsString(es, charset);
            if (StringUtils.isEmpty(msg)) {
                throw new IOException(conn.getResponseCode() + ":" + conn.getResponseMessage());
            } else {
                throw new IOException(msg);
            }
        }
    }

    private static String getStreamAsString(InputStream stream, String charset) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
            StringWriter writer = new StringWriter();

            char[] chars = new char[256];
            int count = 0;
            while ((count = reader.read(chars)) > 0) {
                writer.write(chars, 0, count);
            }

            return writer.toString();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    private static String getResponseCharset(String ctype) {
        String charset = DEFAULT_CHARSET;

        if (!StringUtils.isEmpty(ctype)) {
            String[] params = ctype.split(";");
            for (String param : params) {
                param = param.trim();
                if (param.startsWith("charset")) {
                    String[] pair = param.split("=", 2);
                    if (pair.length == 2) {
                        if (!StringUtils.isEmpty(pair[1])) {
                            charset = pair[1].trim();
                        }
                    }
                    break;
                }
            }
        }

        return charset;
    }

    /**
     * 使用默认的UTF-8字符集反编码请求参数值。
     *
     * @param value 参数值
     * @return 反编码后的参数值
     */
    public static String decode(String value) {
        return decode(value, DEFAULT_CHARSET);
    }

    /**
     * 使用默认的UTF-8字符集编码请求参数值。
     *
     * @param value 参数值
     * @return 编码后的参数值
     */
    public static String encode(String value) {
        return encode(value, DEFAULT_CHARSET);
    }

    /**
     * 使用指定的字符集反编码请求参数值。
     *
     * @param value 参数值
     * @param charset 字符集
     * @return 反编码后的参数值
     */
    public static String decode(String value, String charset) {
        String result = null;
        if (!StringUtils.isEmpty(value)) {
            try {
                result = URLDecoder.decode(value, charset);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    /**
     * 使用指定的字符集编码请求参数值。
     *
     * @param value 参数值
     * @param charset 字符集
     * @return 编码后的参数值
     */
    public static String encode(String value, String charset) {
        String result = null;
        if (!StringUtils.isEmpty(value)) {
            try {
                result = URLEncoder.encode(value, charset);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    /**
     * 获取客户端的真实IP，防止代理情况
     *
     * @param request
     * @return 获取的IP, 如果获取不到返回 null
     */
    public static String getClientRealIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (!Strings.isNullOrEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 如果是代理的情况， 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        return ip;
    }

    /**
     * 返回请求的全路径
     * <p>
     * 例如：http://42.121.122.150:8989/abc.jsp?wuid=818
     *
     * @param request
     * @return
     */
    public static String getHttpRequestFullURL(HttpServletRequest request) {
        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();

        if (queryString == null) {
            return requestURL.toString();
        } else {
            String fragment = Strings.nullToEmpty(request.getPathInfo());
            fragment = fragment.isEmpty()? "" : "#"+fragment;
            return requestURL.append('?').append(queryString).append(fragment).toString();
        }
    }

    public static String getBodyString(ServletRequest request)
    {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try (InputStream inputStream = request.getInputStream())
        {
            reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line = "";
            while ((line = reader.readLine()) != null)
            {
                sb.append(line);
            }
        }
        catch (IOException e)
        {
            log.warn("getBodyString出现问题！");
        }
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException e)
                {
                    log.error("关闭流异常",e);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 设置http cookie，默认设置成http only
     *
     * @param resp
     *            http response
     * @param cookieName
     *            cookie名
     * @param cookieValue
     *            cookie值
     * @param cookieDomain
     *            cookie所在的domain，此domain下的server才能读此cookie
     * @param cookiePath
     *            设置cookie可见路径，此URL及下属才能读取此cookie，传入null会使用默认值"/"，也就是全站可读
     * @param cookieMaxAge
     *            单位：秒，cookie的存活时间，-1或者null代表客户端关闭后立即删除，不持久化
     */
    public static void writeCookieValue(HttpServletResponse resp,
                                        String cookieName, String cookieValue, String cookieDomain,
                                        String cookiePath, Integer cookieMaxAge) {
        CookieGenerator cookieGenerator = new CookieGenerator();
        cookieGenerator.setCookieDomain(cookieDomain);
        if (cookiePath != null) {
            cookieGenerator.setCookiePath(cookiePath);
        }
        // 详情请查阅http only的解释，跟安全相关，仅在servlet3.0以上才支持
        cookieGenerator.setCookieHttpOnly(true);
        // cookie是否只在https环境下才发送，明显我们不是
        cookieGenerator.setCookieSecure(false);
        cookieGenerator.setCookieName(cookieName);
        cookieGenerator.setCookieMaxAge(cookieMaxAge);
        cookieGenerator.addCookie(resp, cookieValue);
    }
}
