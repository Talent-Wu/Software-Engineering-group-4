package utils;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 1.顺序输入内容
 * 1）.收入支出
 * 例如：花了，开销这一类词归类到支出中
 * 2）.金额：这个ai检测数据直接统计即可（用不用ai无所谓
 * 3）.具体类型
 * 例如：零嘴，零食，分到零食这一类（就和一差不多有点像模糊输入识别）
 * 4）.公司名称：可以用ai判断一下除前三项的剩余内容，删去语气助词（可以预设，“的”“了”“公司”“在”…）
 * 例如：花了1000元钱买手机在华为公司
 */
public class AIHelper {
    private static final String API_KEY = "sk-88fe7e9dc728417d9585935cd167631b";

    public static String parseContent(String question) {
        String prompt = question + "。帮我分析前面这句话，它由四部分组成，第一部分是Income或Expenditure，" +
                "第二部分是金额，第三部分是收入或支出的具体分类（帮我归纳成9类：Pet，Phone，Monetary，Snacks，" +
                "Tobacco，Daily，Cosmetics，Horticulture，Meal），第四部分是公司名称（用英文）。" +
                "返回结果的四部分用#分割，例如Income#123#Pet#Huawei。不用解释，如果你不能归纳成指定的四部分，给我返回一个NO即可";
        String result = chat(prompt);
//        String result = "Expenditure#123#Meal#Huawei";
        return result;
    }

    public static String chat(String prompt) {
        try {
            URL url = new URL("https://api.deepseek.com/chat/completions");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer " + API_KEY);

            String data = "{\n" +
                    "        \"model\": \"deepseek-chat\",\n" +
                    "        \"messages\": [\n" +
                    "          {\"role\": \"system\", \"content\": \"You are a helpful assistant.\"},\n" +
                    "          {\"role\": \"user\", \"content\": \"" + prompt + "\"}\n" +
                    "        ],\n" +
                    "        \"stream\": false\n" +
                    "      }";
            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = data.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = con.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println("Response: " + response.toString());
            Map<String, String> stringStringMap = jsonToMap(response.toString());
            return stringStringMap.get("content");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Map<String, String> jsonToMap(String jsonString) {
        Map<String, String> map = new HashMap<>();
        Pattern pattern = Pattern.compile("\"(.*?)\":\"(.*?)\"");
        Matcher matcher = pattern.matcher(jsonString);

        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            map.put(key, value);
        }

        return map;
    }

    public static void main(String[] args) {
        String str = "{\"id\":\"267d74c3-d301-4a16-8b84-738f850efcaa\",\"object\":\"chat.completion\",\"created\":1746535082,\"model\":\"deepseek-chat\",\"choices\":[{\"index\":0,\"message\":{\"role\":\"assistant\",\"content\":\"截至2024年7月，唐纳德·特朗普（Donald Trump）的年龄为 **78岁**。他出生于 **1946年6月14日**，若到2024年6月将满78岁，并在11月的美国总统选举中作为共和党候选人参选。  \\n\\n如需更精确的当前年龄计算，可根据具体日期推算。\"},\"logprobs\":null,\"finish_reason\":\"stop\"}],\"usage\":{\"prompt_tokens\":12,\"completion_tokens\":74,\"total_tokens\":86,\"prompt_tokens_details\":{\"cached_tokens\":0},\"prompt_cache_hit_tokens\":0,\"prompt_cache_miss_tokens\":12},\"system_fingerprint\":\"fp_8802369eaa_prod0425fp8\"}";
        Map<String, String> stringStringMap = jsonToMap(str);
        System.out.println(stringStringMap.get("content"));
    }
}
