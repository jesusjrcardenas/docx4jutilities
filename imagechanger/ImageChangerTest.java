package com.testing.demo.imagechanger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImageChangerTest {

    @Test
    public void testChangePNGtoJPG(){
        String filepath = "/Users/jesus/Consis/test/images/LiquidacionDeSiniestros.docx";
        String bufferpath = "/Users/jesus/Consis/test/images/"; //aqui guarda las imagenes para convertirlas
        ImageChanger imageChanger = new ImageChanger();
        try {
            imageChanger.saveDocxImg(filepath, bufferpath);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testChangePNGtoJPGinFolder(){
        String filepath = "/Users/jesus/Consis/test/images/";
        String bufferpath = "/Users/jesus/Consis/test/images/";  //aqui guarda las imagenes para convertirlas
        ImageChanger imageChanger = new ImageChanger();
        try {
            imageChanger.changeImagesInFolder(filepath, bufferpath);
        }catch (Exception e){
            e.printStackTrace();
        }


    }




}
