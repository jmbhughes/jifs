
public class Image {
    BufferedImage img;
    float minX;
    float minY;
    float maxX;
    floag maxY;
    
    Image() {
        this.img = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
        this.minX = -1.0;
        this.maxX = 1.0;
        this.minY = -1.0;
        this.maxY = 1.0;
    }
    
    Image(int width, int height, float minX, float maxX, float minY, float minY) {
        this.img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    void setColor(float x, float y, Color color) {

    }
}
