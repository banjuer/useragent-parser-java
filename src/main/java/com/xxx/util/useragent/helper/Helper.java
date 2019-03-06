package com.xxx.util.useragent.helper;

import java.util.Arrays;
import java.util.List;

public class Helper {

	public static String version(String major, String minor, String patch) {
		return join(Arrays.asList(major, minor, patch), Constants.VERSION_SPLIT);
	}

	private static String join(List<String> strs, String join) {
		StringBuilder ret = new StringBuilder();
		if (strs == null || strs.size() == 0)
		    return ret.toString();
		String cur;
        for (String str : strs) {
            cur = str;
            if (!isEmpty(cur)) {
                if (ret.length() == 0)
                    ret.append(cur);
                else
                    ret.append(join).append(cur);
            }
        }
		return ret.toString();
	}

	public static boolean isEmpty(String string) {
		if (string == null || string.length() == 0)
			return true;
		return false;
	}

}
