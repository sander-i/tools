package org.tools.bedrock.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.tools.bedrock.util.spread.ImageUtils;

import java.util.Arrays;
import java.util.Objects;

public class ImageUtilsTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void imageUrLToBinary() {
        byte[] jpgs = ImageUtils.imageUrLToBinary("https://konachan.net/sample/d4b95992cb22792d712f1d3db12448e5/Konachan.com%20-%20354706%20sample.jpg?0.4660671193545778");
        System.out.println(Arrays.toString(jpgs));
    }

    @Test
    public void imageUrLToBinary2() {
        byte[] jpgs = ImageUtils.imageUrLToBinary2("https://konachan.net/sample/d4b95992cb22792d712f1d3db12448e5/Konachan.com%20-%20354706%20sample.jpg?0.4660671193545778");
        System.out.println(Arrays.toString(jpgs));
    }

    @Test
    public void imageToBinaryLoss() {
        String path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("static")).getPath().substring(1) + "/315805.jpg";
        byte[] jpgs = ImageUtils.imageToBinaryLoss(path);
        System.out.println(Arrays.toString(jpgs));
    }

    @Test
    public void binaryToImageLoss() {
        String path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("static")).getPath().substring(1) + "/315805.jpg";
        byte[] jpgs = ImageUtils.imageToBinary(path);
        ImageUtils.binaryToImageLoss(jpgs, "E:\\test\\bio\\zhh\\315805.jpeg");
    }

    @Test
    public void formatName() {
        String path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("static")).getPath().substring(1) + "/315805.jpg";
        String formatName = ImageUtils.formatName(path);
        System.out.println(formatName);
    }

    @Test
    public void imageToBinary() {
        String path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("static")).getPath().substring(1) + "/315805.jpg";
        byte[] jpgs = ImageUtils.imageToBinary(path);
        System.out.println(Arrays.toString(jpgs));
    }

    @Test
    public void binaryToImage() {
        String path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("static")).getPath().substring(1) + "/315805.jpg";
        byte[] jpgs = ImageUtils.imageToBinary(path);
        ImageUtils.binaryToImage(jpgs, "E:\\test\\bio\\zhh\\315805.jpeg");
    }

    @Test
    public void compressPicByScale() {
        String path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("static")).getPath().substring(1) + "/315805.jpg";
        byte[] jpgs = ImageUtils.imageToBinary(path);
        System.out.println(Arrays.toString(jpgs));
        jpgs = ImageUtils.compressPicByScale(jpgs, 0.99F);
        System.out.println(Arrays.toString(jpgs));
    }

    @Test
    public void testCompressPicByScale() {
        String path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("static")).getPath().substring(1) + "/315805.jpg";
        byte[] jpgs = ImageUtils.imageToBinary(path);
        jpgs = ImageUtils.compressPicByScale(jpgs, 0.9D);
        ImageUtils.binaryToImage(jpgs, "E:\\test\\bio\\zhh\\315805.jpeg");
    }

    @Test
    public void testCompressPicByScale1() {
        String path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("static")).getPath().substring(1) + "/315805.jpg";
        byte[] jpgs = ImageUtils.imageToBinary(path);
        jpgs = ImageUtils.compressPicByScale(jpgs, 640, 480);
        ImageUtils.binaryToImage(jpgs, "E:\\test\\bio\\zhh\\315805.jpeg");
    }

    @Test
    public void testCompressPicByScale2() {
        String path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("static")).getPath().substring(1) + "/315805.jpg";
        byte[] jpgs = ImageUtils.imageToBinary(path);
        jpgs = ImageUtils.compressPicByScale(jpgs, 0.9D, 0.95F);
        ImageUtils.binaryToImage(jpgs, "E:\\test\\bio\\zhh\\315805.jpeg");
    }

    @Test
    public void testCompressPicByScale3() {
        String path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("static")).getPath().substring(1) + "/315805.jpg";
        byte[] jpgs = ImageUtils.imageToBinary(path);
        jpgs = ImageUtils.compressPicByScale(jpgs, 1280, 960, 0.95F);
        ImageUtils.binaryToImage(jpgs, "E:\\test\\bio\\zhh\\315805.jpeg");
    }

    @Test
    public void testCompressPicByScale4() {
        String path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("static")).getPath().substring(1) + "/315805.jpg";
        byte[] jpgs = ImageUtils.imageToBinary(path);
        jpgs = ImageUtils.compressPicByScale(jpgs, 1280, 960, 0.95F, "jpeg");
        ImageUtils.binaryToImage(jpgs, "E:\\test\\bio\\zhh\\315805.jpeg");
    }

    @Test
    public void testCompressPicByScale5() {
        String path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("static")).getPath().substring(1) + "/315805.jpg";
        byte[] jpgs = ImageUtils.imageToBinary(path);
        jpgs = ImageUtils.compressPicByScale(jpgs, 0.9D, 0.95F, "jpeg");
        ImageUtils.binaryToImage(jpgs, "E:\\test\\bio\\zhh\\315805.jpeg");
    }
}