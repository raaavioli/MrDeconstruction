package Misc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class BorderedBufferedImage {
    private BufferedImage image;

    //Dimensions
    private double x;
    private double y;
    private double height;
    private double width;
    private int cornerRadius;

    private Color borderColor;

    public BorderedBufferedImage(String resourcePath, double x, double y, double width, double height, int cornerRadius) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.cornerRadius = cornerRadius;

        borderColor = Color.DARK_GRAY;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(resourcePath));
            if (cornerRadius != 0) {
                image = makeRoundedCorner(image, cornerRadius);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BorderedBufferedImage(String resourcePath) {
        borderColor = Color.DARK_GRAY;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(resourcePath));
            if (cornerRadius != 0) {
                image = makeRoundedCorner(image, cornerRadius);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDimensions(double x, double y, double width, double height, int cornerRadius) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.cornerRadius = cornerRadius;
        makeRoundedCorner(image, cornerRadius);
    }


    private static BufferedImage makeRoundedCorner(BufferedImage image, int cornerRadius) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = output.createGraphics();

        // This is what we want, but it only does hard-clipping, i.e. aliasing
        // g2.setClip(new RoundRectangle2D ...)

        // so instead fake soft-clipping by first drawing the desired clip shape
        // in fully opaque white with antialiasing enabled...
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));

        // ... then compositing the image on top,
        // using the white shape from above as alpha source
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);

        g2.dispose();

        return output;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public void draw(Graphics2D g) {
        g.drawImage(image, (int) x, (int) y, (int) width, (int) height, null);
        g.setColor(borderColor);
        g.drawRoundRect((int) x - 1, (int) y - 1, (int) width + 1, (int) height + 1, cornerRadius, cornerRadius);
    }

}
