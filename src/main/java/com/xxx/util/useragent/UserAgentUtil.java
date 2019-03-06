package com.xxx.util.useragent;

import com.xxx.util.useragent.bean.Browser;
import com.xxx.util.useragent.bean.Device;
import com.xxx.util.useragent.bean.OS;
import com.xxx.util.useragent.bean.UserAgent;
import com.xxx.util.useragent.helper.Constants;
import com.xxx.util.useragent.parser.CachingParser;
import com.xxx.util.useragent.parser.Parser;

import java.util.LinkedList;
import java.util.List;

public class UserAgentUtil {

	private static final Parser CACHING_PARSER = new CachingParser(Constants.CACHE_SIZE);

	public static UserAgent parse(String ua) {
		return CACHING_PARSER.parse(ua);
	}

	public static Browser browser(String ua) {
	    return CACHING_PARSER.parseBrowser(ua);
    }

    public static OS os(String ua) {
	    return CACHING_PARSER.parseOS(ua);
    }

    public static Device device(String ua) {
	    return CACHING_PARSER.parseDevice(ua);
    }

    @Deprecated
	public static UserAgent parse(String ua, boolean cache) {
		Parser parser;
		if (!cache)
			parser = Parser.build();
		else
			parser = CACHING_PARSER;
		return parser.parse(ua);
	}

	public static void main(String[] args) {
        performanceTest();
	}

	private static void performanceTest() {
        List<String> testData = new LinkedList<>();
        produce(testData);
        int times = 1;
        test(times, true, testData);
        test(times, false, testData);
    }

    private static void produce(List<String> testData) {
        String chrome = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36";
        String qq = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.25 Safari/537.36 Core/1.70.3638.400 QQBrowser/10.4.3273.400";
        String ie = "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko";
        String edge = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/18.17763";
        String wechat = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 MicroMessenger/6.5.2.501 NetType/WIFI WindowsWechat QBCore/3.43.1010.400 QQBrowser/9.0.2524.400";
        String qihu = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/18.17763";
        testData.add(chrome);
        testData.add(qq);
        testData.add(ie);
        testData.add(edge);
        testData.add(wechat);
        testData.add(qihu);
    }

	private static void test(int times, boolean cache, List<String> testData) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
           if (cache)
               testData.forEach(data -> System.out.println(UserAgentUtil.parse(data)));
           else
               testData.forEach(data -> parse(data, false));
        }
        long end = System.currentTimeMillis();
        System.out.println("cache cost:" + (end - start) / 1000.0 + "s");
    }

}
