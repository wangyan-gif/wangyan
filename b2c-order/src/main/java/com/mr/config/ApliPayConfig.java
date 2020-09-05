package com.mr.config;

public class ApliPayConfig {

    //应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static final String APP_ID = "2021000118642477";
    //应用私钥
    public static final String MERCHANT_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCjdhgzcN33hoTaG4a2P88J93HMGuD3acR6lMlfs2PI9Gh46Lvu6cA1y1zMXqRkWK2sc8Gs1rX14uw4JnAPqJcr/uLZSHGUl3XCw8ZXAFa/ufbvh/O2Mv2SPwlz3gcAg1Dj4JKd+bF3PcUHRzBK2Om8QXWm5vxC6GPhE/dQJwb1LEKTOmhVm0IpHYSz8NRP41edTXPrGlH9F/nI4/sYXBNnh4EkocqV9sB7hw5CLwWQS0lKIqg4Ycpof2jZoSiFB5vz2oqRKh8rvwGMmGBTYy0+waURgETtuqZcSD+WPsnGDpOH8zkQ/XQxcsrJLN19XH5IzX2jFiE5InJgArvYgIIzAgMBAAECggEAWl6HsqjLjnUILazaJRfR1qi+eSDDDwWiYkKvsgDzQb4do/rHK6Z/ZT4w56vLJs7/9T9p66wonO//uGtHuOBz8xbPQ2ERqhyyNiMWHDNLwmOFaZe33RsDpT7EcxJkvJvctJ8Lb9sDifDUZNmukTKqxjFKYiOBg1wHHAB10hJBfnk9d+HPlqRrOcAxvatX8rsslekPNzNW2zMr737piMaEjyAQ+x2O1IQJ8mJsnEOoWvOgQgHDG5xjeKBUHSIBhF4Gr4tb9F+zRJm6SJqQ8o1K98CLaE9kZ8+tSYROjcBcUkhRtJ6Y4fWTckSC6jkz1A7F2WpYbK0WcZkio7VcF4OhwQKBgQDqCC1CAzdRvI6ggfASOwyKWj/niuzjoFaxXJ1cWNhTAPU+4k4DTAhw49DAZ5eAMYHFwYkaYowBMKBMYjW2BA554d2BuQXOq5d907FdMLptXTOXA0AkYRfbIHYGRj172S+W+YVK6Eq5e9UXOQQaOI8Q+/JbsbR9SxGW5E91OwyyhwKBgQCyzhg7dXa/p/92uOQsavXlKgd2yA05zgQZ4mRmvAJ/o94rruvfWodj8992DVleJumnVJU410B0VuBvLhBlSd1qGYxbM9sBKopwxSJLrUUdOV6NSVr/4j3q/fjUZsrMzetWVs6a5O+f6vm+T84cbWzekwBtA7aErl2WfSuBT0fh9QKBgBg7lIQbVSUPgy03hyY8GwZQZgGRrwm61n8y+dBQGsYteTdzs2RAATe/PyRNFbDeb3OMoKeswJZ29II0QR+BuAYCzy7Ze7J0rhEHVHwzyXn9hpQCcCfULeYwrEfmCgPC71yQpa6l8ubV3bCz5KraZGX70JKSCgBXP5ECPwACh0QNAoGAQ2LuuQHk9ggffCPkQmUxNOOpGnf8nAtrekHbKuiyXF5qrbXnyS56Fg5j6j7Prm8l4d07u8dL0Eypmt0WasXyvzjPr9OUNxVMRYHWEgVEJeZoHwFFXWhrg3Oh4pK23YbBqbFfffrH4FITO0kiWY5X2G44W91pXQnF1aBo0/wIED0CgYEAm0WMFqg2sCZCXaJDru7Asmp+tUTXFhfDPn4gE59+Nuhk3c6FmvhBT3UxMd2ticNP//BRhjyQOhVA7dpGAsLfVXSvhiMSHz2oEU2r4oQVhyNRMTemWeq/i7poICxKZuFCfDQ6MQIlkk6OGW2XkGCRhiHNpdbH58hL48LQE3bGW+k=";
    //支付宝公钥
    public static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhaSHGD0xkL/pBMB3agBIU+Rpj2uFPnbfeMNacVHBVXH8lzTDVhVx1JWe+mglTfTS3tIYztMVxeGuEy3VUqVr/UfDxY6/g4+tNDGkC9rv72SrvVGa6b/gEvYX3YtBR/zlIbtp4wbkOSVZC0uaXCaotKkCVWE/R7XEgxK1MTlCVQ5lH01rAwSOlVZ2I3+6aDThS2so7kYGC8ATv6/nOZujVQN0t8EkdJtuUswvYYURv0ULwuqo151CuuVowmyh9jHXPY4WJih5l6vmZ0QBvzlaaw++9YoTTygFn0GiMH2OP+KbVwUfh1zR9zABw2tH4aWXksdckXIhvtvIaqOJ+p2o9QIDAQAB";
    //通知的回调地址 写一个确实支付成功改订单状态等，//必须为公网地址，
    public static final String NOTIFY_URL = "http://127.0.0.1:8089/paySuccess";
    //用户支付完成回调地址//可以写内网地址
    public static final String RETURN_URL = "http://127.0.0.1:8089/callback";
    // 签名方式
    public static String SIGN_TYPE = "RSA2";
    // 字符编码格式
    public static String CHARSET = "gbk";
    // 支付宝网关
    public static String GATEWAYURL = "https://openapi.alipaydev.com/gateway.do";

}