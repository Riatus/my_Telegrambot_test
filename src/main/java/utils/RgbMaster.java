package utils;

import functions.ImageOperation;

import java.awt.image.BufferedImage;

public class RgbMaster {

    private BufferedImage image;
    private int widht;
    private  int height;
    private  boolean hasAlphaChannel;
    private int[] pixels;

    public RgbMaster(BufferedImage image){
        this.image = image;
        widht = image.getWidth();
        height = image.getHeight();
        hasAlphaChannel = image.getAlphaRaster() != null;
        pixels = new int[widht*height];
        image.getRGB(0,0,widht,height,pixels,0,widht);
    }
    public  BufferedImage getImage(){return image;}

    public  void changeImage(ImageOperation operation) throws Exception{
        for (int i = 0; i < pixels.length; i++) {
            float[] pixel =ImageUtils.rgbIntToArray(pixels[i]);
            float[] newPixel = operation.execute(pixel);
            pixels[i]=ImageUtils.arrayToRgbInt(newPixel);
        }
        image.setRGB(0,0,widht,height,pixels,0,widht);
    }

}
