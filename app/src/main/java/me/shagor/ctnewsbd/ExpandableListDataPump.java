package me.shagor.ctnewsbd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();


        List<String> Politics = new ArrayList<String>();
        List<String> national = new ArrayList<String>();
        List<String> international = new ArrayList<String>();
        List<String> sport = new ArrayList<String>();
        List<String> entertainment = new ArrayList<String>();
        List<String> options = new ArrayList<String>();





        expandableListDetail.put("POLITICS", Politics);
        expandableListDetail.put("National ",national);
        expandableListDetail.put("INTERNATIONAL", international);
        expandableListDetail.put("SPORTS",sport);
        expandableListDetail.put("ENTERTAINMENT", entertainment);
        expandableListDetail.put("Opinion",options);




        return expandableListDetail;
    }
}