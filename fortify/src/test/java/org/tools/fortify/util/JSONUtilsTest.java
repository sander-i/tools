package org.tools.fortify.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.tools.bedrock.util.BaseUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JSONUtilsTest {

    @Test
    public void objToJson() {
    }

    @Test
    public void jsonToObj() {
    }

    @Test
    public void testJsonToObj() {
    }

    @Test
    public void objToJsonArray() {
    }

    @Test
    public void testJsonToObj1() {
    }

    @Test
    public void test() {
        String jsonStr = "[\n" +
                "    {\n" +
                "        \"market\": \"amazon;amazon_us_eufy\",\n" +
                "        \"PDT\": \"Smart Audio\",\n" +
                "        \"Sales\": \"lily.deng@anker.com;ailsa.li@anker.com\",\n" +
                "        \"PM\": \"savi.xiao@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon;amazon_us_eufy\",\n" +
                "        \"PDT\": \"Smart Projector\",\n" +
                "        \"Sales\": \"belinda.zhang@anker.com;ailsa.li@anker.com\",\n" +
                "        \"PM\": \"savi.xiao@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon;amazon_us_eufy\",\n" +
                "        \"PDT\": \"3D Print\",\n" +
                "        \"Sales\": \"lia.yang@anker.com\",\n" +
                "        \"PM\": \"dong.cheng@anker-in.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon;amazon_us_eufy\",\n" +
                "        \"PDT\": \"TWS\",\n" +
                "        \"Sales\": \"shawn.su@anker.com\",\n" +
                "        \"PM\": \"savi.xiao@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon;amazon_us_eufy\",\n" +
                "        \"PDT\": \"Headband\",\n" +
                "        \"Sales\": \"peirong.li@anker.com\",\n" +
                "        \"PM\": \"savi.xiao@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon;amazon_us_eufy\",\n" +
                "        \"PDT\": \"Wearable Device\",\n" +
                "        \"Sales\": \"mody.mo@anker.com\",\n" +
                "        \"PM\": \"savi.xiao@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon;amazon_us_eufy\",\n" +
                "        \"PDT\": \"Battery\",\n" +
                "        \"Sales\": \"peggy.zhu@anker.com\",\n" +
                "        \"PM\": \"rebeca.song@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon;amazon_us_eufy\",\n" +
                "        \"PDT\": \"Charger\",\n" +
                "        \"Sales\": \"tanya.tang@anker.com;corine.xing@anker.com;maureen.wang@anker.com\",\n" +
                "        \"PM\": \"rebeca.song@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon;amazon_us_eufy\",\n" +
                "        \"PDT\": \"Data New Accessories\",\n" +
                "        \"Sales\": \"joy.chen@anker.com\",\n" +
                "        \"PM\": \"rebeca.song@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon;amazon_us_eufy\",\n" +
                "        \"PDT\": \"Wireless\",\n" +
                "        \"Sales\": \"hinda.huang@anker.com\",\n" +
                "        \"PM\": \"rebeca.song@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon;amazon_us_eufy\",\n" +
                "        \"PDT\": \"Essential\",\n" +
                "        \"Sales\": \"tanya.tang@anker.com;hinda.huang@anker.com;joy.chen@anker.com;peggy.zhu@anker.com;corine.xing@anker.com;emma.xiao@anker.com\",\n" +
                "        \"PM\": \"rebeca.song@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon;amazon_us_eufy\",\n" +
                "        \"PDT\": \"PhoeniX\",\n" +
                "        \"Sales\": \"emily.long@anker.com\",\n" +
                "        \"PM\": \"dong.cheng@anker-in.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon;amazon_us_eufy\",\n" +
                "        \"PDT\": \"eufy S\",\n" +
                "        \"Sales\": \"elaine.tang@anker.com\",\n" +
                "        \"PM\": \"dong.cheng@anker-in.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon;amazon_us_eufy\",\n" +
                "        \"PDT\": \"Appliances others\",\n" +
                "        \"Sales\": \"Karina.li@anker.com\",\n" +
                "        \"PM\": \"dong.cheng@anker-in.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon;amazon_uk2\",\n" +
                "        \"PDT\": \"PPS\",\n" +
                "        \"Sales\": \"ronna.yu@anker.com;ellen.li@anker.com\",\n" +
                "        \"PM\": \"rebeca.song@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon;amazon_us_eufy\",\n" +
                "        \"PDT\": \"Edge Security\",\n" +
                "        \"Sales\": \"lia.yang@anker.com\",\n" +
                "        \"PM\": \"dong.cheng@anker-in.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon;amazon_us_eufy\",\n" +
                "        \"PDT\": \"Pro Security\",\n" +
                "        \"Sales\": \"forest.chen@anker.com\",\n" +
                "        \"PM\": \"dong.cheng@anker-in.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_au\",\n" +
                "        \"PDT\": \"\",\n" +
                "        \"Sales\": \"yong.liu@anker.com;joanne.huang@anker.com\",\n" +
                "        \"PM\": \"luisa.zhao@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_de2;amazon_de3;amazon_fr;amazon_it;amazon_nl;amazon_be\",\n" +
                "        \"PDT\": \"PPS\",\n" +
                "        \"Sales\": \"melissa.xiao@anker.com;ellen.li@anker.com\",\n" +
                "        \"PM\": \"rebeca.song@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_de;amazon_de2\",\n" +
                "        \"PDT\": \"Wireless;Battery;Charger;Data New Accessories;Essential\",\n" +
                "        \"Sales\": \"lea.feng@anker.com\",\n" +
                "        \"PM\": \"leona.liu@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_de;amazon_de2\",\n" +
                "        \"PDT\": \"3D Print\",\n" +
                "        \"Sales\": \"elvis.zhang@anker.com\",\n" +
                "        \"PM\": \"leona.liu@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_de;amazon_de2\",\n" +
                "        \"PDT\": \"Edge Security;Pro Security;Smart Projector;Smart Audio\",\n" +
                "        \"Sales\": \"elvis.zhang@anker.com;fiona.yi@anker.com\",\n" +
                "        \"PM\": \"leona.liu@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_de;amazon_de2\",\n" +
                "        \"PDT\": \"Appliances Others;PhoeniX;Vacuum Cleaner;eufy S\",\n" +
                "        \"Sales\": \"lucy.luo@anker.com\",\n" +
                "        \"PM\": \"leona.liu@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_de;amazon_de2\",\n" +
                "        \"PDT\": \"TWS;Wearable Device\",\n" +
                "        \"Sales\": \"zeta.liu@anker.com;merry.wen@anker.com\",\n" +
                "        \"PM\": \"leona.liu@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_de;amazon_de2\",\n" +
                "        \"PDT\": \"Headband\",\n" +
                "        \"Sales\": \"zeta.liu@anker.com;merry.wen@anker.com\",\n" +
                "        \"PM\": \"leona.liu@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_fr;amazon_it;amazon_es2\",\n" +
                "        \"PDT\": \"Appliances Others;PhoeniX;Vacuum Cleaner;Raccoon;shunk;eufy S\",\n" +
                "        \"Sales\": \"yuliya.ye@anker.com\",\n" +
                "        \"PM\": \"pauline.yu@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_fr;amazon_it;amazon_es2\",\n" +
                "        \"PDT\": \"Wireless;Data New Accessories\",\n" +
                "        \"Sales\": \"camila.zhang@anker-in.com\",\n" +
                "        \"PM\": \"pauline.yu@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_fr;amazon_it;amazon_es2\",\n" +
                "        \"PDT\": \"TWS;Headband;Wearable Device\",\n" +
                "        \"Sales\": \"celeste.zhou@anker.com\",\n" +
                "        \"PM\": \"pauline.yu@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_fr;amazon_it;amazon_es2\",\n" +
                "        \"PDT\": \"Battery;Essential\",\n" +
                "        \"Sales\": \"camila.zhang@anker-in.com;stella.liu@anker.com\",\n" +
                "        \"PM\": \"pauline.yu@anker.com\"\n" +
                "    },{\n" +
                "        \"market\": \"amazon_fr;amazon_it;amazon_es2\",\n" +
                "        \"PDT\": \"Charger\",\n" +
                "        \"Sales\": \"camila.zhang@anker-in.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_fr;amazon_it;amazon_es2\",\n" +
                "        \"PDT\": \"3D Print;Edge Security;Pro Security;Smart Projector;Smart Audio\",\n" +
                "        \"Sales\": \"regina.jiao@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_jp\",\n" +
                "        \"PDT\": \"Data New Accessories\",\n" +
                "        \"Sales\": \"bella.wei@anker.com;jasmine.chen@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_jp\",\n" +
                "        \"PDT\": \"Appliances Others;PhoeniX;Vacuum Cleaner;Raccoon;shunk;eufy S\",\n" +
                "        \"Sales\": \"homare.yamamoto@anker.com;jasmine.chen@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_jp\",\n" +
                "        \"PDT\": \"PPS;HES\",\n" +
                "        \"Sales\": \"jansea.jiang@anker.com;jasmine.chen@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_jp\",\n" +
                "        \"PDT\": \"Pro Security;Edge Security;3D Print\",\n" +
                "        \"Sales\": \"jasmine.chen@anker.com;eme.xiang@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_jp\",\n" +
                "        \"PDT\": \"Smart Projector;Smart Audio\",\n" +
                "        \"Sales\": \"kenya.enomoto@anker.com;jasmine.chen@anker.com;marina.sakaguchi@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_jp\",\n" +
                "        \"PDT\": \"Battery;Essential\",\n" +
                "        \"Sales\": \"jasmine.chen@anker.com;mia.zeng@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_jp\",\n" +
                "        \"PDT\": \"Headband;TWS;Wearable Device\",\n" +
                "        \"Sales\": \"jasmine.chen@anker.com;tsuru.tsuru@anker.com;miki.kurahashi@anker.com;natsuki.goto@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_jp\",\n" +
                "        \"PDT\": \"Wireless;Charger\",\n" +
                "        \"Sales\": \"yuno.yunose@anker.com;jasmine.chen@anker.com;kanako.aoki@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_jp_loona\",\n" +
                "        \"PDT\": \"\",\n" +
                "        \"Sales\": \"yuya.nakagawa@anker.com;jasmine.chen@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_mx_direct\",\n" +
                "        \"PDT\": \"\",\n" +
                "        \"Sales\": \"abner.shen@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_nl;amazon_be\",\n" +
                "        \"PDT\": \"Appliances Others;PhoeniX;Vacuum Cleaner;Wireless;Charger;Battery;Data New Accessories;Essential;TWS;Pro Security;Headband;Wearable Device;Edge Security;Smart Audio;3D Print;HES;Raccoon;Smart Projector\",\n" +
                "        \"Sales\": \"rita.peng@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_se;amazon_pl\",\n" +
                "        \"PDT\": \"PPS\",\n" +
                "        \"Sales\": \"ellen.li@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_se\",\n" +
                "        \"PDT\": \"Appliances Others;PhoeniX;Vacuum Cleaner;eufy S;TWS;Headband;Wearable Device;Pro Security;Edge Security;Smart Projector;Smart Audio;3D Print;Wireless;Battery;Charger;Data New Accessories;Essential\",\n" +
                "        \"Sales\": \"maycci.he@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_pl\",\n" +
                "        \"PDT\": \"Appliances Others;PhoeniX;Vacuum Cleaner;eufy S;TWS;Headband;Wearable Device;Pro Security;Edge Security;Smart Projector;Smart Audio;3D Print;Wireless;Battery;Charger;Data New Accessories;Essential\",\n" +
                "        \"Sales\": \"Yuri.yu@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_uk;amazon_uk2\",\n" +
                "        \"PDT\": \"Smart Projector;Smart Audio\",\n" +
                "        \"Sales\": \"chaya.lei@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_uk;amazon_uk2\",\n" +
                "        \"PDT\": \"Appliances Others;PhoeniX;eufy S\",\n" +
                "        \"Sales\": \"larine.hu@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_uk;amazon_uk2\",\n" +
                "        \"PDT\": \"Pro Security;Edge Security;3D Print\",\n" +
                "        \"Sales\": \"nadia.wang@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_uk;amazon_uk2\",\n" +
                "        \"PDT\": \"TWS;Wearable Device\",\n" +
                "        \"Sales\": \"serena.zhu@anker.com\"\n" +
                "    },{\n" +
                "        \"market\": \"amazon_uk;amazon_uk2\",\n" +
                "        \"PDT\": \"Headband\",\n" +
                "        \"Sales\": \"sue.zhang@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_uk;amazon_uk2\",\n" +
                "        \"PDT\": \"Battery\",\n" +
                "        \"Sales\": \"bella.wang@anker.com;hayley.wen@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_uk;amazon_uk2\",\n" +
                "        \"PDT\": \"Data New Accessories\",\n" +
                "        \"Sales\": \"lindsay.wu@anker-in.com;kary.xie@anker.com;hayley.wen@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_uk;amazon_uk2\",\n" +
                "        \"PDT\": \"Wireless\",\n" +
                "        \"Sales\": \"tiffany.fan@anker-in.com;hayley.wen@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_uk;amazon_uk2\",\n" +
                "        \"PDT\": \"Charger\",\n" +
                "        \"Sales\": \"tiffany.fan@anker-in.com;kary.xie@anker.com;hayley.wen@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_uk;amazon_uk2\",\n" +
                "        \"PDT\": \"Essential\",\n" +
                "        \"Sales\": \"bella.wang@anker.com;tiffany.fan@anker-in.com;kary.xie@anker.com;hayley.wen@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_ca\",\n" +
                "        \"PDT\": \"Smart Audio\",\n" +
                "        \"Sales\": \"hester.liu@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_ca\",\n" +
                "        \"PDT\": \"Smart Projector\",\n" +
                "        \"Sales\": \"hester.liu@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_ca\",\n" +
                "        \"PDT\": \"3D Print\",\n" +
                "        \"Sales\": \"hester.liu@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_ca\",\n" +
                "        \"PDT\": \"TWS\",\n" +
                "        \"Sales\": \"penny.pan@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_ca\",\n" +
                "        \"PDT\": \"Headband\",\n" +
                "        \"Sales\": \"penny.pan@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_ca\",\n" +
                "        \"PDT\": \"Wearable Device\",\n" +
                "        \"Sales\": \"penny.pan@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_ca\",\n" +
                "        \"PDT\": \"Battery\",\n" +
                "        \"Sales\": \"bella.li1@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_ca\",\n" +
                "        \"PDT\": \"Charger\",\n" +
                "        \"Sales\": \"pearl.ouyang@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_ca\",\n" +
                "        \"PDT\": \"Data New Accessories\",\n" +
                "        \"Sales\": \"pearl.ouyang@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_ca\",\n" +
                "        \"PDT\": \"Wireless\",\n" +
                "        \"Sales\": \"bella.li1@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_ca\",\n" +
                "        \"PDT\": \"Essential\",\n" +
                "        \"Sales\": \"pearl.ouyang@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_ca\",\n" +
                "        \"PDT\": \"Edge Security\",\n" +
                "        \"Sales\": \"yumi.tang@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_ca\",\n" +
                "        \"PDT\": \"Pro Security\",\n" +
                "        \"Sales\": \"yumi.tang@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_ca\",\n" +
                "        \"PDT\": \"PhoeniX;eufy S;Appliances Others\",\n" +
                "        \"Sales\": \"freya.tong@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_br;amazon_br_eufy\",\n" +
                "        \"PDT\": \"\",\n" +
                "        \"Sales\": \"sibyl.zhang@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_in3\",\n" +
                "        \"PDT\": \"Battery;Charger;Essential;Data New Accessories;Wireless;PPS\",\n" +
                "        \"Sales\": \"wells.wei@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_in3\",\n" +
                "        \"PDT\": \"TWS;Headband;Wearable Device;PhoeniX;eufy S;Appliances Others;Edge Security;Pro Security;Smart Projector;Smart Audio;3D Print\",\n" +
                "        \"Sales\": \"sylvia.wei@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_ae\",\n" +
                "        \"PDT\": \"Battery;Charger;Essential;Data New Accessories;Wireless;PPS\",\n" +
                "        \"Sales\": \"wells.wei@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_ae\",\n" +
                "        \"PDT\": \"TWS;Headband;Wearable Device;PhoeniX;eufy S;Appliances Others;Edge Security;Pro Security;Smart Projector;Smart Audio;3D Print\",\n" +
                "        \"Sales\": \"sylvia.wei@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_sa\",\n" +
                "        \"PDT\": \"Battery;Charger;Essential;Data New Accessories;Wireless;PPS\",\n" +
                "        \"Sales\": \"wells.wei@anker.com\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"market\": \"amazon_sa\",\n" +
                "        \"PDT\": \"TWS;Headband;Wearable Device;PhoeniX;eufy S;Appliances Others;Edge Security;Pro Security;Smart Projector;Smart Audio;3D Print\",\n" +
                "        \"Sales\": \"sylvia.wei@anker.com\"\n" +
                "    }\n" +
                "]";

        final JSONArray jsonArray = JSON.parseArray(jsonStr);

        List<String> list = new ArrayList<>();

        jsonArray.forEach(item -> {
            JSONObject jsonObject = (JSONObject) item;
            String market = jsonObject.getString("market");
            String pdt = jsonObject.getString("PDT");
            String Sales = jsonObject.getString("Sales").replace("@anker.com", "").replace("@anker-in.com", "").replace(";", "/");
            String[] markets = market.split(";");
            String[] pdts = pdt.split(";");
            if (pdt.contains(";")) {
                for (String p : pdts) {
                    if (market.contains(";")) {
                        for (String m : markets) {
                            list.add(buildSql2(m, p, Sales));
                        }
                    } else {
                        list.add(buildSql2(market, p, Sales));
                    }
                }
            } else {
                if (BaseUtils.isEmpty(pdt)) {
                    pdt = "ALL";
                }
                if (market.contains(";")) {
                    for (String m : markets) {
                        list.add(buildSql2(m, pdt, Sales));
                    }
                } else {
                    list.add(buildSql2(market, pdt, Sales));
                }
            }
        });

        for (String s : list) {
            System.out.println(s);
        }

    }

    public String buildSql(String market, String pdt, String sales) {
        return "INSERT INTO bs_config_param (config_type, business_id, config_key, config_value, is_encrypt, created_at, creator, updated_at, updator) VALUES(" +
                "'DEAL_APPLYOR', " +
                "(select id from sm_store where name = '" + market + "'), " +
                "(select number from bs_heavy_weight_team bhwt where bhwt.english_name = '" + pdt + "'), " +
                "'" + sales + "', " +
                "0, now(), 'sander', now(), 'sander');";
    }

    public String buildSql2(String market, String pdt, String sales) {
        return "select concat('INSERT INTO bs_config_param (config_type, business_id, config_key, config_value, is_encrypt, created_at, creator, updated_at, updator) VALUES(', \n" +
                "'\\'DEAL_APPLYOR\\', ',\n" +
                "(select id from sm_store where name = '" + market + "' and is_active = 1), ', ',\n" +
                ("ALL".equals(pdt)? "'\\'ALL\\'', \n" : "ifnull((select concat('\\'',number,'\\'') from bs_heavy_weight_team bhwt where bhwt.english_name = '"+pdt+"'),'\\'"+pdt+" 没有找到对应pdt code\\''),\n") +
                "', \\'', '" + sales + "\\'',\n" +
                "', 0', ', now()', ', \\'sander\\', now(), \\'sander\\');') UNION ALL";
    }

    @Test
    public void test1() {
        String jsonStr = "{\n" +
                "    \"min_discount_rate\": 0.2032,\n" +
                "    \"has_overlay\": true,\n" +
                "    \"lower_discount_rate_msku_list\": \"\",\n" +
                "    \"margin_item_volist\": [\n" +
                "        {\n" +
                "            \"value_type\": \"money_off\",\n" +
                "            \"code\": \"P13418\",\n" +
                "            \"ped_discount_amount\": \"N\",\n" +
                "            \"coupon_end_date\": \"\",\n" +
                "            \"duplicate_price\": 0.6,\n" +
                "            \"discount_amount\": 63.99,\n" +
                "            \"coupon_discount_amount\": \"N\",\n" +
                "            \"sale_cx_amount\": \"N\",\n" +
                "            \"cost_discount_rate\": \"21.25%\",\n" +
                "            \"business_price_amount\": \"\",\n" +
                "            \"sale_event_amount\": \"N\",\n" +
                "            \"discount_rate\": \"20.32%\",\n" +
                "            \"deal_discount_amount\": \"N\",\n" +
                "            \"coupon_start_date\": \"\",\n" +
                "            \"margin_msku\": \"68ANHUB-B14A-F0,B07WYPPR74\",\n" +
                "            \"deals_start_date\": \"\",\n" +
                "            \"ped_end_date\": \"\",\n" +
                "            \"has_business_price\": \"N\",\n" +
                "            \"profit\": 0,\n" +
                "            \"code_end_date\": \"2025-01-30\",\n" +
                "            \"deals_end_date\": \"\",\n" +
                "            \"discount_rate_value\": 0.2032,\n" +
                "            \"margin\": -8020,\n" +
                "            \"margin_discount\": 13,\n" +
                "            \"has_ped\": \"N\",\n" +
                "            \"code_start_date\": \"2025-01-25\",\n" +
                "            \"sale_event_start_date\": \"\",\n" +
                "            \"has_sale_event\": \"N\",\n" +
                "            \"ped_start_date\": \"\",\n" +
                "            \"code_discount_amount\": \"80.0%\",\n" +
                "            \"offer_price\": 63.99,\n" +
                "            \"has_sale_price\": \"N\",\n" +
                "            \"has_coupon\": \"N\",\n" +
                "            \"has_deals\": \"N\",\n" +
                "            \"sale_event_end_date\": \"\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"min_rate\": -8019.998\n" +
                "}";
        JSONObject newJsonObject = new JSONObject();

        JSONObject jsonObject = JSON.parseObject(jsonStr);

        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof JSONArray) {
                JSONArray newItemJsonArray = new JSONArray();
                JSONArray jsonArray = (JSONArray) value;
                jsonArray.forEach(item -> {
                    JSONObject newItemJsonObject = new JSONObject();
                    JSONObject itemJsonObject = (JSONObject) item;
                    itemJsonObject.forEach((k1, v1) -> newItemJsonObject.put("b_" + k1, v1));
                    newItemJsonArray.add(newItemJsonObject);
                });
                newJsonObject.put("b_" + key, newItemJsonArray);
            } else {
                if ("has_overlay".equals(key)) {
                    newJsonObject.put(key, value);
                    continue;
                }
                newJsonObject.put("b_" + key, value);
            }
        }

        jsonObject.forEach((k, v) -> {
            if (v instanceof JSONArray) {
                JSONArray newItemJsonArray = new JSONArray();
                JSONArray jsonArray = (JSONArray) v;
                jsonArray.forEach(item -> {
                    JSONObject newItemJsonObject = new JSONObject();
                    JSONObject itemJsonObject = (JSONObject) item;
                    itemJsonObject.forEach((k1, v1) -> newItemJsonObject.put("b_" + k1, v1));
                    newItemJsonArray.add(newItemJsonObject);
                });
                newJsonObject.put("b_" + k, newItemJsonArray);
            } else {
                if ("has_overlay".equals(k)) {
                    newJsonObject.put(k, v);
                    return;
                }
                newJsonObject.put("b_" + k, v);
            }
        });

        System.out.println(newJsonObject.toJSONString());
    }

}

