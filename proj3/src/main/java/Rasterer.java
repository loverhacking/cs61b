import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {


    private final double ROOT_ULLAT = MapServer.ROOT_ULLAT;
    private final double ROOT_ULLON = MapServer.ROOT_ULLON;
    private final double ROOT_LRLAT = MapServer.ROOT_LRLAT;
    private final double ROOT_LRLON = MapServer.ROOT_LRLON;
    private final int TILE_SIZE = MapServer.TILE_SIZE;

    private double[] LonDPP = new double[8];

    public Rasterer() {

        LonDPP[0] = calLonDPP(ROOT_LRLON, ROOT_ULLON, TILE_SIZE);
        for (int i = 0; i < 7; i++) {
            LonDPP[i + 1] = LonDPP[i] / 2;
        }

    }

    private double calLonDPP(double lrLon, double ulLon, int width) {
        return (lrLon - ulLon) / width;
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {

        double lrlon = params.get("lrlon");
        double ullon = params.get("ullon");
        double w = params.get("w");
        double h = params.get("h");
        double ullat = params.get("ullat");
        double lrlat = params.get("lrlat");

        // find the given query map LonDPP
        double queryLonDPP = calLonDPP(lrlon, ullon, (int) w);

        // find the best "depth"
        int bestIndex = 7;

        for (int i = LonDPP.length - 1; i >= 0; i--) {
            if (LonDPP[i] > queryLonDPP && i != 7) {
                bestIndex = i + 1;
                break;
            }
        }

        // the side length of pixels at given depth
        int lengthPixel = (int) Math.pow(2, bestIndex);


        double xLengthPerPixel = (ROOT_LRLON - ROOT_ULLON) / lengthPixel;
        double yLengthPerPixel = (ROOT_ULLAT - ROOT_LRLAT) / lengthPixel;

        // xstart: calculate the leftmost longitude
        int xstartNum = (int) Math.floor((ullon - ROOT_ULLON) / xLengthPerPixel);
        int xendNum = (int) Math.ceil((lrlon - ROOT_ULLON) / xLengthPerPixel);
        int numx = xendNum - xstartNum;

        double xstart = ROOT_ULLON + xstartNum * xLengthPerPixel;
        double xend = ROOT_ULLON + xendNum * xLengthPerPixel;

        // ystart: calculate the downmost latitude
        int ylownum = (int) Math.floor((lrlat - ROOT_LRLAT) / yLengthPerPixel);
        int yuppernum = (int) Math.ceil((ullat - ROOT_LRLAT) / yLengthPerPixel);
        int numy = yuppernum - ylownum;

        double ystart = ROOT_LRLAT + ylownum * yLengthPerPixel;
        double yend = ROOT_LRLAT + yuppernum * yLengthPerPixel;

        Map<String, Object> results = new HashMap<>();
        results.put("raster_ul_lon", xstart);
        results.put("raster_lr_lat", ystart);

        results.put("raster_lr_lon", xend);
        results.put("raster_ul_lat", yend);

        results.put("depth", bestIndex);

        String[][] renderGrid = new String[numy][numx];
        for (int i = 0; i < numy; i++) {
            for (int j = 0; j < numx; j++) {
                renderGrid[i][j] = "d" + bestIndex + "_x" + (xstartNum + j)
                        + "_y" + (lengthPixel - yuppernum + i) + ".png";
            }
        }
        results.put("render_grid", renderGrid);
        results.put("query_success", checkQuery(params));
        return results;
    }

    private boolean checkQuery(Map<String, Double> params) {
        double lrlon = params.get("lrlon");
        double ullon = params.get("ullon");

        double ullat = params.get("ullat");
        double lrlat = params.get("lrlat");

        if (lrlon < ullon || ullat < lrlat) {
            return false;
        }

        if (ullon < ROOT_ULLON || lrlon > ROOT_LRLON
                || ullat > ROOT_ULLAT || lrlat < ROOT_LRLAT) {
            return false;
        }
        return true;
    }
}
