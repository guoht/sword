package net.guohaitao.sword;

import org.junit.Test;

/**
 * Created by guohaitao on 14-9-17.
 * Description: xxx
 */
public class PageBeanTest {

    @Test
    public void testPage() {
        Pagination pageBean = new Pagination(3, 5, 98);
        assert pageBean.getPrevPage() == 2;
        assert pageBean.getNextPage() == 4;
        assert pageBean.getPageMax() == 20;
        assert pageBean.getPageNo() == 3;
        assert pageBean.getPageSize() == 5;
        assert pageBean.getStart() == 10;
        assert pageBean.getTotal() == 98;
        assert pageBean.hasNext();
        assert pageBean.hasPrev();
    }
}
