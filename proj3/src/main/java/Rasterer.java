import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    public Rasterer() {
        // YOUR CODE HERE
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
        // System.out.println(params);
        Map<String, Object> results = new HashMap<>();
//        System.out.println("Since you haven't implemented getMapRaster, nothing is displayed in "
//                           + "your browser.");
        double ullat = params.get("ullat");
        double ullon = params.get("ullon");
        double lrlat = params.get("lrlat");
        double lrlon = params.get("lrlon");
        double w = params.get("w");
        double h = params.get("h");
        boolean querySuccess = true;
        if (ullat < lrlat || ullon > lrlon || ullat <= MapServer.ROOT_LRLAT
                || lrlat >= MapServer.ROOT_ULLAT || ullon >= MapServer.ROOT_LRLON
                || lrlon <= MapServer.ROOT_ULLON) {
            querySuccess = false;
            results.put("query_success", querySuccess);
            return results;
        }

        int depth = 7;
        for (int i = 0; i < 8; i += 1) {
            double resLondpp = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON)
                    / (MapServer.TILE_SIZE * Math.pow(2, i));
            if (resLondpp <= (lrlon - ullon) / w) {
                depth = i;
                break;
            }
        }

        double lonPpic = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / Math.pow(2, depth);
        int left = 0;
        int right = (int) Math.pow(2, depth) - 1;
        for (int i = 0; i < Math.pow(2, depth); i += 1) {
            if (ullon >= MapServer.ROOT_ULLON + lonPpic * i
                    && ullon < MapServer.ROOT_ULLON + lonPpic * (i + 1)) {
                left = i;
            }
            if (lrlon > MapServer.ROOT_ULLON + lonPpic * i
                    && lrlon <= MapServer.ROOT_ULLON + lonPpic * (i + 1)) {
                right = i;
                break;
            }
        }
        double rasterUllon = MapServer.ROOT_ULLON + lonPpic * left;
        double rasterLrlon = MapServer.ROOT_ULLON + lonPpic * (right + 1);
        double latPpic = (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / Math.pow(2, depth);
        int upper = 0;
        int lower = (int) Math.pow(2, depth) - 1;
        for (int j = 0; j < Math.pow(2, depth); j += 1) {
            if (ullat <= MapServer.ROOT_ULLAT - latPpic * j
                    && ullat > MapServer.ROOT_ULLAT - latPpic * (j + 1)) {
                upper = j;
            }
            if (lrlat < MapServer.ROOT_ULLAT - latPpic * j
                    && lrlat >= MapServer.ROOT_ULLAT - latPpic * (j + 1)) {
                lower = j;
                break;
            }
        }
        double rasterUllat = MapServer.ROOT_ULLAT - latPpic * upper;
        double rasterLrlat = MapServer.ROOT_ULLAT - latPpic * (lower + 1);

        String[][] renderGrid = new String[lower - upper + 1][right - left + 1];
        for (int i = 0; i <= lower - upper; i += 1) {
            for (int j = 0; j <= right - left; j += 1) {
                renderGrid[i][j] = "d" + depth + "_x" + (j + left) + "_y" + (i + upper) + ".png";
            }
        }
        results.put("query_success", querySuccess);
        results.put("depth", depth);
        results.put("raster_ul_lon", rasterUllon);
        results.put("raster_lr_lon", rasterLrlon);
        results.put("raster_ul_lat", rasterUllat);
        results.put("raster_lr_lat", rasterLrlat);
        results.put("render_grid", renderGrid);

        return results;
    }

}
