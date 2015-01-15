package net.guohaitao.sword;

import org.junit.Test;

/**
 * Created by guohaitao on 15-1-5.
 * Description: xxx
 */
public class StringsTest {

    @Test
    public void test() {
        String testA = Strings.trimHtml(" 集合[Collections]</h2>\n" +
                "<p>Guava对JDK集合的扩展，这是Guava最成熟和为人所知的部分</p>\n" +
                "<p>2.1 <a title=\"[Google Guava] 不可变集合: 防御性编程、常量集合和性能提升\" href=\"http://ifeve.com/google-guava-immutablecollections/\">不可变集合</a>: 用不变的集合进行防御性编程和性能提升。</p>\n" +
                "<p>2.2 <a href=\"http://ifeve.com/google-guava-newcollectiontypes/\">新集合类型</a>: multisets, multimaps, tables, bidirectional maps等</p>\n" +
                "<p>2.3 <a title=\"[Google Guava] 2.3-强大的集合工具类：java.util.Collections中未包含的集合工具\" href=\"http://ifeve.com/google-guava-collectionutilities/\" target=\"_blank\">强大的集合工具类</a>: 提供java.util.Collections中没有的集合工具</p>\n" +
                "<p>2.4 <a title=\"[Google Guava] 2.4-集合扩展工具类\" href=\"http://ifeve.com/google-guava-collectionhelpersexplained/\" target=\"_blank\">扩展工具类</a>：让实现和扩展集合类变得更容易，比如创建<tt>Collection<span style=\"font-family: Georgia;\">的装饰器，或实现迭代器</span></tt></p>\n" +
                "<h2>3. <a href=\"http://ifeve.com/google-guava-cachesexplained\">缓存</a>[Caches]</h2>");
        String testB = Strings.trimHtml("aaaaaa");
        assert !testA.contains("<");
        assert testB.equals("aaaaaa");
    }
}
