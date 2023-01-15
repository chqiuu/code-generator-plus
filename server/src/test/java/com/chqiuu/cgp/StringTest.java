package com.chqiuu.cgp;

import java.util.regex.Pattern;

public class StringTest {
    public static void main(String[] args) {
        // 正则表达式中\\\\匹配一个\
        boolean flg = Pattern.matches("\\\\", "\\");
        System.out.println(flg);

        char a = 'a';
        String b = "abc";
        String c = "你好\n\t棒";
        System.out.println(c);
        char d = '\'';
        String e = "\"";
        System.out.println(d);
        System.out.println(e);
        String f = "\\";
        System.out.println(f);
        String j = "\\\\";
        System.out.println(f + j);
        String h = "{\"name\":\"翎野君\"}";
        System.out.println(h);
        String g = "{\\\"age\\\":\\\"99\\\"}";
        System.out.println(g);
        String k = g.replaceAll("\\\\", "");
        System.out.println(k);
        g = "{\"2\":\"语文\",\"3\":\"数学\",\"4\":\"英语\",\"6\":\"物理\",\"7\":\"化学\",\"11\":\"生物\",\"9\":\"政治思品\",\"8\":\"历史\",\"10\":\"地理\",\"5\":\"科学\",\"20\":\"历史与社会\"}\n";
        k = g.replaceAll("\"", "\\\\\"").replaceAll("\r", " ").replaceAll("\n", " ");
        System.out.println(k);

        String layuiminiPath = "layuimini.add.html.ftl";layuiminiPath = "InputVO.java.ftl";
        System.out.println(layuiminiPath.matches("layuimini.*.html.ftl"));
    }
}
