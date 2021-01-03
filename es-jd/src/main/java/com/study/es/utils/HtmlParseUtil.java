package com.study.es.utils;

import com.study.es.entity.Content;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Program Name: es-study-api
 * Created by yanlp on 2021-01-03
 *
 * @author yanlp
 * @version 1.0
 */
public class HtmlParseUtil {

    /**
     * 从jd上爬取数据
     * @param keyword
     * @return
     * @throws Exception
     */
    public static List<Content> parse(String keyword) throws Exception {
        // 目前仅支持英文搜索,如果是中文的话,请更改new URL那里,有个字符集编码
        // 搜索url
        String url = "https://search.jd.com/Search?keyword=" + keyword;
        Document document = Jsoup.parse(new URL(url), 30000);
        Element element = document.getElementById("J_goodsList");
        Elements elements = element.getElementsByTag("li");
        List<Content> contentList = new ArrayList<>();
        for (Element el : elements) {
            String img = el.getElementsByTag("img").eq(0).attr("data-lazy-img");
            String price = el.getElementsByClass("p-price").text();
            String title = el.getElementsByClass("p-name").text();

            Content content = Content.builder()
                    .img(img)
                    .title(title)
                    .price(price)
                    .build();
            contentList.add(content);
        }
        return contentList;
    }

}
