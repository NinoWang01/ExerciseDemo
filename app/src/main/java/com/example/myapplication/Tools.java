package com.example.myapplication;

/**
 * Created by Nino on 19-3-5.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
/**
 *    "count": 30,
 "err": 0,
 "items": [
 {
 "format": "word",
 "image": null,
 "published_at": 1474411501,
 "tag": "",
 "user": {
 "avatar_updated_at": 1474096732,
 "uid": 8231441,
 "last_visited_at": 1365402245,
 "created_at": 1365402245,
 "state": "bonded",
 "last_device": "android_2.2.1",
 "role": "n",
 "login": "波杰克波波",
 "id": 8231441,
 "icon": "2016091707185249.JPEG"
 },
 "image_size": null,
 "id": 117574586,
 "hot_comment": {
 "status": "publish",
 "user_id": 31350726,
 "floor": 4,
 "ip": "112.96.115.114",
 "created_at": "2016-09-21 06:53:11",
 "comment_id": 365495582,
 "like_count": 71,
 "pos": 0,
 "content": "其中有 短 信是某情 趣 酒店消费记录……",
 "source": "ios",
 "score": null,
 "parent_id": 0,
 "anonymous": 0,
 "neg": 0,
 "article_id": 117574586,
 "user": {
 "avatar_updated_at": 1471581258,
 "uid": 31350726,
 "last_visited_at": 1457280749,
 "created_at": 1457280749,
 "state": "binding",
 "last_device": "android_9.0.0",
 "role": "n",
 "login": "愿时光温柔以待～",
 "id": 31350726,
 "icon": "2016081912341828.JPEG"
 }
 },
 "votes": {
 "down": -76,
 "up": 4393
 },
 "created_at": 1474407515,
 "content": "最近工作太忙总是加班到深夜，三天没回家了。这是我和女友相处两年以来第一次连续三天没见面。能看出来她十分想念我，期间给我发了60来条短信，全是银行卡扣款信息。",
 "state": "publish",
 "comments_count": 76,
 "allow_comment": true,
 "share_count": 30,
 "type": "promote"
 },
 *
 */
//解析json字符串
public class Tools {
    // 声明一个解析json字符串的静态方法
    public static List<Map<String, String>> jsonStringToList(String jsonString) {
        // 先创建集合
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        try {
            // 先获取jsonObject 和jsonArray对象
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            // 循环遍历jsonArray
            for (int i = 0; i < jsonArray.length(); i++) {
                // 在获取一次JSONObject的对象
                JSONObject jsonObject_item = jsonArray.getJSONObject(i);
                // 先创建map集合
                Map<String, String> map = new HashMap<String, String>();
                // 往map中添加值
                map.put("created_at", jsonObject_item.getString("created_at"));
                map.put("comments_count",
                        jsonObject_item.getString("comments_count"));
                map.put("content", jsonObject_item.getString("content"));

                // 把map添加到list中去
                list.add(map);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
