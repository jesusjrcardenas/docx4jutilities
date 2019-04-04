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
        String path = "/Users/jesus/Consis/test/images/";
        ImageChanger imageChanger = new ImageChanger();
        try {
            imageChanger.saveDocxImg(filepath, path);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
