package com.testing.demo.imagechanger;

import org.docx4j.XmlUtils;
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
import java.util.ArrayList;
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


            Object o = XmlUtils.unwrap(entry.getValue());
            if (o instanceof org.docx4j.vml.CTRect){
                System.out.println("es un recta");
            }
            if (o instanceof org.docx4j.vml.CTImageData) {
                System.out.println("es una imagen");
            }
            if(o instanceof org.docx4j.vml.CTShapetype){
                System.out.println("es una imagen shape type");
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

    private java.util.List<String> getFiles(String documentsPath) {
        final File folder = new File(documentsPath);

        java.util.List<String> result = new ArrayList<>();

        search(".*\\.docx", folder, result);

        return result;
    }

    private static void search(final String pattern, final File folder, java.util.List<String> result) {
        for (final File f : folder.listFiles()) {

            if (f.isDirectory()) {
                search(pattern, f, result);
            }

            if (f.isFile()) {
                if (f.getName().matches(pattern)) {
                    result.add(f.getAbsolutePath());
                }
            }
        }
    }

    public void changeImagesInFolder(String documentsPath, String bufferpath) {
        //1. get files list
        java.util.List<String> filesPath = getFiles(documentsPath);

        for (String filepath : filesPath) {

            //2. open the file and change the image format
            ImageChanger imageChanger = new ImageChanger();
            try {
                imageChanger.saveDocxImg(filepath, bufferpath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}
