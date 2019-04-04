package com.testing.demo.imagechanger;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.Part;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPart;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class ImageChanger {

    public void saveDocxImg(String filePath, String savePath) throws Exception {
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage
                .load(new File(filePath));
        for (Map.Entry<PartName, Part> entry : wordMLPackage.getParts().getParts()
                .entrySet()) {
            if (entry.getValue() instanceof BinaryPartAbstractImage) {
                BinaryPartAbstractImage binImg = (BinaryPartAbstractImage) entry
                        .getValue();

                String imgContentType = binImg.getContentType();
                PartName pt = binImg.getPartName();
                String fileName = null;
                if (pt.getName().indexOf("word/media/") != -1) {
                    fileName = pt.getName().substring(
                            pt.getName().indexOf("word/media/")
                                    + "word/media/".length());
                }
                System.out.println(String.format("mimetype=%s,filePath=%s",
                        imgContentType, pt.getName()));
                if (fileName.contains(".png")) {
                    FileOutputStream fos = new FileOutputStream(savePath + fileName);
                    ((BinaryPart) entry.getValue()).writeDataToOutputStream(fos);
                    fos.close();

                    String routePNG = savePath + fileName;
                    String routeJPG = routePNG.replaceAll(".png", ".jpg");

                    changeImagePNGtoJPG(routePNG, routeJPG);

                    BinaryPart binaryPart = (BinaryPart) entry.getValue();
                    binaryPart.setBinaryData(fileToBytes(new File(routePNG)));

                    File file = new File(filePath);
                    file.setWritable(true);
                    wordMLPackage.save(file);
                }


            }
        }
    }

    public void changeImagePNGtoJPG(String urlImagePNGinput, String urlImageJPGoutput) {
        try {
            File input = new File(urlImagePNGinput);
            File output = new File(urlImageJPGoutput);

            BufferedImage image = ImageIO.read(input);
            BufferedImage result = new BufferedImage(
                    image.getWidth(),
                    image.getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            result.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
            ImageIO.write(result, "jpg", output);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] fileToBytes(File file) throws FileNotFoundException,
            IOException {
        byte[] bytes;

        if (file.exists() && file.isFile()) {
            java.io.InputStream is = new java.io.FileInputStream(file);
            long length = file.length();
            bytes = new byte[(int) length];
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length
                    && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
            if (offset < bytes.length) {
            }
            is.close();
        } else {
            bytes = new byte[0];
        }
        return bytes;
    }


}
