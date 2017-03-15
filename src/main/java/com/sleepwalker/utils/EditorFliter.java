package com.sleepwalker.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class EditorFliter {

    private static final Logger logger               = LoggerFactory.getLogger(EditorFliter.class);

    private static final String IMG_SRC_TAG          = "(<img src=\")";

    private static final String EXPRESSION_SRC_REGEX = "(/vendor/ueditor/)";

    private static final String IMG_SRC_REGEX        = "(http://file.showjoy.com/images/(.*?)\\/(.*?).(jpg|png|jpeg|gif)\")";

    private static final String ANY_WORD             = "(.*?)";

    private static final String IMG_SRC_TAG_END      = "(.*?/>)";

    private static final String TAG_END              = "(.*?>)";

    private static Pattern      p;

    private static Matcher      m;

    public static void main(String[] args) {
        String content = "12345687<a href=\"http://file.showjoy.com/images/3e/3e3b215fc5e5449b992a7237d3828c28.jpg\" target=\"_blank\" title=\"查看原图\"><img src=\"http://file.showjoy.com/images/3e/3e3b215fc5e5449b992a7237d3828c28.jpg.240x170.jpg\"/></a>";
        //        String content = "<img src=\"http://code.yunjee.net/view/Yunjee-Assets/trunk/vendor/ueditor/1.2.6/dialogs/emotion/images/jx2/j_0003.gif\"/>";
        System.out.println(isContainYunjeeImage(content));
        System.out.println(filterYunjeeImages(content));
    }

    /**
     * 过滤content中 云集图片服务器上图片的地址
     * @param textString
     * @return
     */
    public static List<String> filterYunjeeImages(String content) {
        List<String> imageSrcList = null;

        if (StringUtils.isNotBlank(content)) {
            imageSrcList = new ArrayList<String>();

            p = Pattern.compile(IMG_SRC_TAG + IMG_SRC_REGEX + IMG_SRC_TAG_END,
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            m = p.matcher(content);
            while (m.find())
                if (m.groupCount() >= 2)
                    imageSrcList.add(m.group(2).replaceAll("\"", ""));
        }
        return imageSrcList;
    }

    public static boolean isContainYunjeeImage(String content) {
        if (StringUtils.isNotBlank(content)) {
            p = Pattern.compile(IMG_SRC_TAG + IMG_SRC_REGEX + IMG_SRC_TAG_END,
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            m = p.matcher(content);
            if (m.find())
                return true;
        }
        return isContainYunjeeExpression(content);
    }

    public static boolean isContainYunjeeExpression(String content) {
        if (StringUtils.isNotBlank(content)) {
            p = Pattern.compile(IMG_SRC_TAG + ANY_WORD + EXPRESSION_SRC_REGEX + TAG_END,
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            m = p.matcher(content);
            if (m.find())
                return true;
        }
        return false;
    }

    public static String filterYunjeeImage(String content) {
        String imageSrcList = null;

        if (StringUtils.isNotBlank(content)) {
            p = Pattern.compile(IMG_SRC_TAG + IMG_SRC_REGEX + IMG_SRC_TAG_END,
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            m = p.matcher(content);
            if (m.find() && m.groupCount() >= 2)
                imageSrcList = m.group(2);

            if (StringUtils.isNotBlank(imageSrcList))
                imageSrcList = imageSrcList.replaceAll("\"", "");
        }

        return imageSrcList;
    }

    /**
     * 过滤所有html，style，script信息
     * @param inputString
     * @return
     */
    public static String textFilter(String inputString) {
        String htmlStr = inputString; // 含html标签的字符串      
        String textStr = "";
        java.util.regex.Pattern p_script;
        java.util.regex.Matcher m_script;
        java.util.regex.Pattern p_style;
        java.util.regex.Matcher m_style;
        java.util.regex.Pattern p_html;
        java.util.regex.Matcher m_html;

        java.util.regex.Pattern p_html1;
        java.util.regex.Matcher m_html1;

        try {
            String regEx_script = "<[//s]*?script[^>]*?>[//s//S]*?<[//s]*?///[//s]*?script[//s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[//s//S]*?<///script>      
            String regEx_style = "<[//s]*?style[^>]*?>[//s//S]*?<[//s]*?///[//s]*?style[//s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[//s//S]*?<///style>      
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式      
            String regEx_html1 = "<[^>]+";
            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签      

            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签      

            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签      

            p_html1 = Pattern.compile(regEx_html1, Pattern.CASE_INSENSITIVE);
            m_html1 = p_html1.matcher(htmlStr);
            htmlStr = m_html1.replaceAll(""); // 过滤html标签      

            // 增加过滤&nbsp by ben 20130129
            textStr = htmlStr.replace("&nbsp;", " ");

        } catch (Exception e) {
            System.err.println("Html2Text: " + e.getMessage());
        }
        logger.debug(textStr);
        return textStr;// 返回文本字符串      
    }

    /**
     * 允许的链接自动加地址
     * @param textString
     * @return
     */
    public static String linkFilter(String textString) {
        String htmlStr = textString;
        String urlStr = "";
        java.util.regex.Pattern p_url;
        java.util.regex.Matcher m_url;
        try {
            String regEx_url = "((?<![=\"|yunjee-link\"\\>])((ht){1}tp://)[-a-zA-Z0-9@:%_\\+.~#?&//=]+)";

            p_url = Pattern.compile(regEx_url, Pattern.CASE_INSENSITIVE);
            m_url = p_url.matcher(htmlStr);
            StringBuffer sb = new StringBuffer();
            boolean isMatched = false;
            while (m_url.find()) {
                isMatched = true;
                String urlString = m_url.group();
                m_url.appendReplacement(sb,
                    "<a href=\"" + urlString + "\" target=\"_blank\" class=\"yunjee-link\">"
                                            + urlString + "</a>");
            }
            m_url.appendTail(sb);
            if (isMatched) {
                urlStr = sb.toString();
            } else {
                urlStr = htmlStr;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        logger.debug(urlStr);
        return urlStr;
    }

}
