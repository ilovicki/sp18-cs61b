import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {
    private Picture picture;
    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x > width() - 1 || y < 0 || y > height() - 1) {
            throw new IndexOutOfBoundsException();
        }
        Color left, right, below, above;
        if (x == 0) {
            left = picture.get(width() - 1, y);
        } else {
            left = picture.get(x - 1, y);
        }

        if (x == width() - 1) {
            right = picture.get(0, y);
        } else {
            right = picture.get(x + 1, y);
        }

        if (y == 0) {
            below = picture.get(x, height() - 1);
        } else {
            below = picture.get(x, y - 1);
        }

        if (y == height() - 1) {
            above = picture.get(x, 0);
        } else {
            above = picture.get(x, y + 1);
        }
        int difXR = right.getRed() - left.getRed();
        int difXG = right.getGreen() - left.getGreen();
        int difXB = right.getBlue() - left.getBlue();
        int difX2 = difXR * difXR + difXG * difXG + difXB * difXB;

        int difYR = above.getRed() - below.getRed();
        int difYG = above.getGreen() - below.getGreen();
        int difYB = above.getBlue() - below.getBlue();
        int difY2 = difYR * difYR + difYG * difYG + difYB * difYB;
        int energy = difX2 + difY2;
        return energy;
    }

    private int[] calcMinCost(double[][] energies) {
        int W = energies.length;
        int H = energies[0].length;
        double[][] minCost = new double[W][H];
        int[] seam = new int[W];
        if (H == 1) {
            return seam;
        }
        for (int i = 0; i < W; i += 1) {
            for (int j = 0; j < H; j += 1) {
                if (i == 0) {
                    minCost[i][j] = energies[i][j];
                } else if (j == 0) {
                    minCost[i][j] = energies[i][j] + Math.min(minCost[i - 1][j],
                            minCost[i - 1][j + 1]);
                } else if (j == H - 1) {
                    minCost[i][j] = energies[i][j] + Math.min(minCost[i - 1][j],
                            minCost[i - 1][j - 1]);
                } else {
                    minCost[i][j] = energies[i][j] + Math.min(minCost[i - 1][j],
                            Math.min(minCost[i - 1][j - 1], minCost[i - 1][j + 1]));
                }
            }
        }

        double finalCost = Double.MAX_VALUE;
        int end = -1;
        for (int j = 0; j < H; j += 1) {
            if (minCost[W - 1][j] < finalCost) {
                finalCost = minCost[W - 1][j];
                end = j;
            }
        }

        seam[W - 1] = end;
        for (int i = W - 2; i >= 0; i -= 1) {
            int last = seam[i + 1];
            double curCost = minCost[i + 1][last] - energies[i + 1][last];
            if (minCost[i][last] == curCost) {
                seam[i] = last;
            } else if (last > 0 && minCost[i][last - 1] == curCost) {
                seam[i] = last - 1;
            } else {
                seam[i] = last + 1;
            }
        }
        return seam;
    }




    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        double[][] energies = SCUtility.toEnergyMatrix(this);
        return calcMinCost(energies);

    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] energies = SCUtility.toEnergyMatrix(this);
        int w = energies.length;
        int h = energies[0].length;
        double[][] transposed = new double[h][w];
        for (int i = 0; i < w; i += 1) {
            for (int j = 0; j < h; j += 1) {
                transposed[j][i] = energies[i][j];
            }
        }
        return calcMinCost(transposed);
    }

    // remove horizontal seam from picture

    public void removeHorizontalSeam(int[] seam) {
        picture = SeamRemover.removeHorizontalSeam(picture, findHorizontalSeam());

    }

    // remove vertical seam from picture
    public void removeVerticalSeam(int[] seam) {
        picture = SeamRemover.removeVerticalSeam(picture, findVerticalSeam());
    }
}
