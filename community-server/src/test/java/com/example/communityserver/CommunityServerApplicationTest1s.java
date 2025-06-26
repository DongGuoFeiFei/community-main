package com.example.communityserver;

import com.example.communityserver.utils.markdown.MarkDownToHtmlUtils;
import com.example.communityserver.utils.markdown.MarkDownUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CommunityServerApplicationTest1s {

  @Test
    void  test(){
      String md= "**鸿蒙之初，天地未形，阴阳未判。**\n" +
              "\n" +
              "宇宙如一枚玄卵，悬于虚无之中，内蕴混沌之气，无光无暗，无上无下，无生无死。唯有先天一炁，流转不息，似有若无，似动若静。\n" +
              "\n" +
              "**忽有一日，混沌震颤，玄卵裂变。**\n" +
              "\n" +
              "自那无始无终的黑暗之中，一尊巨人缓缓苏醒。他名**盘古**，乃先天之灵，混沌之子。双目如日月未升之光，呼吸如风云未起之息，筋骨如山川未成之脉。他睁开双眼，所见唯有茫茫混沌，无天无地，无昼无夜。\n" +
              "\n" +
              "盘古怒喝一声，声震鸿蒙，混沌为之激荡。他双臂撑天，双足踏地，以无上神力，硬生生将混沌撕裂！\n" +
              "\n" +
              "**清者上升为天，浊者下沉为地。**\n" +
              "\n" +
              "天日高一丈，地日厚一丈，盘古亦日长一丈。如此一万八千年，天极高，地极深，盘古亦顶天立地，屹立其间。\n" +
              "\n" +
              "**然天地初分，尚未稳固。**\n" +
              "\n" +
              "盘古恐天地复合，混沌再临，遂以自身为柱，撑持乾坤。又吐纳元气，化作雷霆风雨；双目开阖，化为日月星辰；筋脉延展，化作江河湖海；毛发飘散，化作草木生灵。\n" +
              "\n" +
              "**及至力竭，盘古轰然倒下。**\n" +
              "\n" +
              "其首化昆仑，其躯化五岳，其血化四海，其息化风云，其声化雷霆，其骨化金石，其髓化玉脉，其魂化三清，其魄化诸神。\n" +
              "\n" +
              "**自此，天地始成，万物始生。**\n" +
              "\n" +
              "阴阳轮转，四时更替，日月照耀，山川巍峨，江河奔涌，草木繁茂，生灵繁衍。\n" +
              "\n" +
              "**而盘古之意志，永镇乾坤，万古不灭。**\n";
      String md2= "![wallhaven-zyrr2w](http://127.0.0.1:8080/uploads/202505/8cac651f7653483f8c1cd92789e42918.jpg)朋友，你的游戏，由我来分享\uD83C\uDF89\uFE0F！\n";


    String plainText = MarkDownUtils.toPlainText(md2);
    String plainText2 = MarkDownUtils.toHtml(md2);
    String plainText22 = MarkDownUtils.toHtml(md);
    String plainText222 = MarkDownUtils.toPlainText(md);
    System.out.println(plainText2);
    System.out.println(plainText222);
    System.out.println(plainText22);
    System.out.println(plainText);


  }
}
