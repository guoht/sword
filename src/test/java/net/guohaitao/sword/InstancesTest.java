package net.guohaitao.sword;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by guohaitao on 14-9-18.
 * Description: xxx
 */
public class InstancesTest {

    @Test
    public void test() {
        ArrayList arrayList1 = Instances.get(ArrayList.class);
        ArrayList arrayList2 = Instances.get(ArrayList.class);
        assert arrayList1.equals(arrayList2);
    }
}
