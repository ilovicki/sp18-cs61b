import edu.princeton.cs.algs4.Picture;
public class SeamCarver {
    private Picture current;
    private int width;
    private int height;
    private int[][] pixels;
    private double[][] energies;
    public SeamCarver(Picture picture) {
        current = picture;
        width = picture.width();
        height = picture.height();
        pixels = new int[width][height];
        energies = new double[width][height];
        for (int i = 0; i < width; i += 1) {
            for (int j = 0; j < height; j += 1) {
                pixels[i][j] = picture.getRGB(i, j);

            }
        }
        calcEnergies();
    }

    // current picture
    public Picture picture() {
        return current;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    private double getEnergy(int x, int y) {
        if (x < 0 || x > width - 1 || y < 0 || y > height - 1) {
            throw new IndexOutOfBoundsException();
        }
        int left, right, below, above;
        if (x == 0) {
            left = pixels[width - 1][y];
        } else {
            left = pixels[x - 1][y];
        }

        if (x == width - 1) {
            right = pixels[0][y];
        } else {
            right = pixels[x + 1][y];
        }

        if (y == 0) {
            below = pixels[x][height - 1];
        } else {
            below = pixels[x][y - 1];
        }

        if (y == height - 1) {
            above = pixels[x][0];
        } else {
            above = pixels[x][y + 1];
        }
        int leftR = (left >> 16) & 0xFF;
        int leftG = (left >> 8) & 0xFF;
        int leftB = left & 0xFF;
        int rightR = (right >> 16) & 0xFF;
        int rightG = (right >> 8) & 0xFF;
        int rightB = right & 0xFF;
        int difXR = rightR - leftR;
        int difXG = rightG - leftG;
        int difXB = rightB - leftB;
        int difX2 = difXR * difXR + difXG * difXG + difXB * difXB;
        int belowR = (below >> 16) & 0xFF;
        int belowG = (below >> 8) & 0xFF;
        int belowB = below & 0xFF;
        int aboveR = (above >> 16) & 0xFF;
        int aboveG = (above >> 8) & 0xFF;
        int aboveB = above & 0xFF;
        int difYR = aboveR - belowR;
        int difYG = aboveG - belowG;
        int difYB = aboveB - belowB;
        int difY2 = difYR * difYR + difYG * difYG + difYB * difYB;
        int energy = difX2 + difY2;
        return energy;
    }

    private void calcEnergies() {
        for (int i = 0; i < width; i += 1) {
            for (int j = 0; j < height; j += 1) {
                energies[i][j] = getEnergy(i, j);
            }
        }
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        return energies[x][y];
    }



    private int[] calcMinCost(double[][] eners) {
        int W = eners.length;
        int H = eners[0].length;
        double[][] minCost = new double[W][H];
        for (int i = 0; i < W; i += 1) {
            for (int j = 0; j < H; j += 1) {
                if (i == 0) {
                    minCost[i][j] = eners[i][j];
                } else if (j == 0) {
                    minCost[i][j] = eners[i][j] + Math.min(minCost[i - 1][j],
                            minCost[i - 1][j + 1]);
                } else if (j == H - 1) {
                    minCost[i][j] = eners[i][j] + Math.min(minCost[i - 1][j],
                            minCost[i - 1][j - 1]);
                } else {
                    minCost[i][j] = eners[i][j] + Math.min(minCost[i - 1][j],
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
        int[] seam = new int[W];
        seam[W - 1] = end;
        for (int i = W - 2; i >= 0; i -= 1) {
            int last = seam[i + 1];
            double curCost = minCost[i + 1][last] - eners[i + 1][last];
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
        return calcMinCost(energies);

    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] energiesV = new double[height][width];
        for (int i = 0; i < width; i += 1) {
            for (int j = 0; j < height; j += 1) {
                energiesV[j][i] = energies[i][j];
            }
        }
        return calcMinCost(energiesV);
    }

    // remove horizontal seam from picture
    private void validateSeam(int[] seam, int len) {
        if (seam.length != len) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < len - 1; i += 1) {
            int dif = seam[i + 1] - seam[i];
            if (dif < -1 || dif > 1) {
                throw new IllegalArgumentException();
            }
        }
    }

    private void updatePixelsAndEnergies(int[] seam, boolean hori) {
        int[][] tempPixels = new int[width][height];
        for (int i = 0; i < width; i += 1) {
            for (int j = 0; j < height; j += 1) {
                if (hori) {
                    if (j < seam[i]) {
                        tempPixels[i][j] = pixels[i][j];
                    } else {
                        tempPixels[i][j] = pixels[i][j + 1];
                    }
                } else {
                    if (i < seam[j]) {
                        tempPixels[i][j] = pixels[i][j];
                    } else {
                        tempPixels[i][j] = pixels[i + 1][j];
                    }
                }

            }
        }
        pixels = tempPixels;

        double[][] tempEnergies = new double[width][height];
        for (int i = 0; i < width; i += 1) {
            for (int j = 0; j < height; j += 1) {
                if (hori) {
                    if (seam[i] - j > 1) {
                        tempEnergies[i][j] = energies[i][j];
                    }
                    if (seam[i] - j == 1 || seam[i] == j) {
                        tempEnergies[i][j] = energy(i, j);
                    } else {
                        tempEnergies[i][j] = energies[i][j + 1];
                    }
                } else {
                    if (seam[j] - i > 1) {
                        tempEnergies[i][j] = energies[i][j];
                    }
                    if (seam[j] - i == 1 || seam[j] == i) {
                        tempEnergies[i][j] = energy(i, j);
                    } else {
                        tempEnergies[i][j] = energies[i + 1][j];
                    }
                }

            }
        }
        energies = tempEnergies;
    }

    private void updatePic() {
        Picture pic = new Picture(width, height);
        for (int i = 0; i < width; i += 1) {
            for (int j = 0; j < height; j += 1) {
                pic.setRGB(i, j, pixels[i][j]);
            }
        }
        current = pic;
    }

    public void removeHorizontalSeam(int[] seam) {
        validateSeam(seam, width);
        height -= 1;
        updatePixelsAndEnergies(seam, true);
        updatePic();
    }

    // remove vertical seam from picture
    public void removeVerticalSeam(int[] seam) {
        validateSeam(seam, height);
        width -= 1;
        updatePixelsAndEnergies(seam, false);
        updatePic();
    }
}
