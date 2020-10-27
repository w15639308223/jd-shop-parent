package com.baidu.shop.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016102600766722";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC1mqPab6ynp0hgayoSosGcBdmzruj2fbyfdUGfTcPH5XswlATAn5BdrjhFhxEphPUWBHCIpHUsFxZBVOZ0PLUo6vR0/SL1wC6bYQzVgJYyu2OnUUDcxFz8203u88f36/pb1vgzIoESKgtG1zd5SNRu4DiNgYv/snOB5j6iGnc8euS/tF+KJNcbP/oq4E2R2+AqQjo8vt5DmFo79dGtJ2abZ/tG0mq6tjNSj8rNy/bUMwsChQIAzfehlf9Jszkh4eECwVGgTYm9onsuCIU2t6QMRuZF/ccZgjp0GrbdmkCmZpxgJIjTNAKRCRMwn6T/vpNZwFKloGUP5zI+X9Rj5BVNAgMBAAECggEBAIoCrFUbmz2J3nIP3thswfAdbhWkbjysz4TrKmSzR38JgbYoNvhgCul2tiL4g21nMo557z+Yo8dPAEXEDKL3Y8Fl1UN3AHN8iq7gcU1rvDlvuUr1b/KWdAhhbXeT4jsiJZBX7SWHKkmwlANRGUJdu7tLQQ8hDVDTv/5PCPhOXqEhg9MqRjwG7zhhVQsBESBWlOTPr8Ht6y85P2STMHNKxS5gMubQGeqavYB5TuvJl3J5lHrvKZj+5JCJZtJBZsnJ3xDZ3YD0Rp2n1uGzo5ikApFq8golMLH2DSvmuDh0jKuEdWmGMaAoPv9D/beghdhD60kU0OJz3qg5miMsWTauwi0CgYEA/cPv/8ils9Ch63cTd5VrTlX6TOyeX8F1XKof2jNS57/55sJjnILo9pi1xod3FhxifFvcohgyebTRQFL1BiJlR0Vp+k0EcSYO5jqRmP1ZXHPYP8QsAv3hbOAT87X+2YA94XESH6k1NzzgJAKCzyV3FInIDFkKYYjxyO2nbaMjCUMCgYEAtzQHjrqrdt9FFMAdeOXUhZQWzkjVufpa4VTHg0RrMGDP71bcdaQdUY63o1aOWPLRLz50lK+Q1r4fb2HcQgq8IdwYLFsAes2lvnbjQLmaq0ONUEb4Rq4DR88LhChMB5mzmBRNpNfji8uO3y0NdDQBUMNAuUaFocNVENdCLvfF9i8CgYEAwDL4Ao4Q2rlI+hNtHiSAaot6EWshpLrczftYNqaqwzZG6z0NvwvzsFoHSz9stcV33mKhH/Adwspttj9er2+7r5hmZRqSI7TEkiGIBwzT8s8W2W0WPInXg+PPmMWeKVQVCjvekzZQRO8JgJyaQJRKtbaJWrjRwP+8yQceyU5HCgsCgYB18ErFOarUddt1BlBy2hI8hB9CYWUluuPyl69w9ATFOi3ieFqoaSRusp/JyRkObhL4hQ5sAbIObIq0G5297kc6zQPK2jKu2+DNgp+9tT667lv0yBtg9Pkrq0BQrdh0NCCH0NpDOrBD+KFbTxeQLUKDiY8LNzkj8Cm9E7aJofyRBQKBgDrh+jKXMeOB93/+BK0RS5e9TrO6WcnftEF9T8TG0tyh0T4OXDREnBp718ZtDLrypOgfT1u9dHw+Ta/C3TCYu/jlLniFtIfk6jA/ev7NTiI3f+J9aVrzSPh3qIfII6Exp+lNgMBCh9Z2uokPSL/BWezrWp/jMQHaxOvkE4r8UFMs";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnX8sYg7UTExoXRuMRD+VkptY6MC/VN4slazpeZj2tsXm8JJHc55HY4EFmlMyeSsjXGO9ESaIcpsl9bCPPUGdY8vcwVHgWBILvMJDVrUe5kjuceDd+w+LzwQQyjHWUSKycvokeDhqOcDNxD4q3iFp25n38DMy5rgS8a/VNNeGguWQXegoye8utDKoWlkwhhwOmIo2pwNrVNnPf8y13fq7iFEHtzhA8InE10UsbK25IKIlsGRA4G95L+7HZQ/lT/osIDFhDL17mEQxgW/zdftTtKccGmEqGKuE08x4pUaR9lBQkOEz91hN1wajR6hUDJQdyE5t/ItyIADUOpZzCk4dZwIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://localhost:8900/pay/requestNotify";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://localhost:8900/pay/returnURL";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";


    // 支付宝网关
    public static String log_path = "D:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

