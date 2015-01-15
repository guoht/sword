package net.guohaitao.sword;

/**
 * Created by i@guohaitao.net on 14-9-17.
 * Description: 经纬度相关工具方法
 */
public final class LBSs {

    //地球半径
    private static final double EARTH_RADIUS = 6378.137;

    private LBSs() {
    }

    /**
     * 两点距离
     *
     * @param lat1
     *         纬度1
     * @param lng1
     *         经度1
     * @param lat2
     *         纬度2
     * @param lng2
     *         经度2
     * @return
     */
    public static double distance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / (double) 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / (double) 2), 2)));
        s *= EARTH_RADIUS;
        s = Math.round(s * 10000) / (double) 10000;
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

}
