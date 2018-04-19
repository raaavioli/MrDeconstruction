package Unit;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Animation {
    private ArrayList<BufferedImage> images;
    private int currentImage;

    private long delay;
    private long startTime;

    private boolean playedOnce;

    public Animation(long delay) {
        playedOnce = false;
        this.delay = delay;
        images = new ArrayList<>();
    }

    public void update() {
        long elapsedTime = (System.nanoTime() - startTime) / 1000000;

        if (elapsedTime > delay) {
            startTime = System.nanoTime();
            currentImage++;
        }

        if (currentImage == images.size()) {
            playedOnce = true;
            currentImage = 0;
        }
    }

    public boolean isPlayedOnce() {
        return playedOnce;
    }

    public void setPlayedOnce(boolean playedOnce) {
        this.playedOnce = playedOnce;
    }

    public BufferedImage getImage() {
        return images.get(currentImage);
    }

    public void addImage(BufferedImage image) {
        images.add(image);
    }
}
