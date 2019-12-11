package com.example.demo.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bing.convertvoice.ConvertApi;
import com.example.demo.tool.Face;
import com.example.demo.tool.Gender;
import com.example.demo.tool.HttpUtil;
import com.example.demo.tool.RobotGoThread;
import com.example.demo.tool.TTSService;
import com.example.demo.tool.Tool;

@RestController
public class HelloWorldController {
	private static final String serverURL1 = "http://vop.baidu.com/server_api";
    private static final String serverURL2 = "http://tsn.baidu.com/text2audio";
    private static String token = "";
    // 语音识别30天（2019/06/22）
    private static String token1 = "24.ffbda856d83fb7a26effdd5333d28764.2592000.1563776111.282335-16521110";
    // 语义识别30天（2019/06/22）
    private static String token3 = "24.ecd79b4aa63006eb170bab49c5124c98.2592000.1563775797.282335-16567128";
    
    
    private static String backCmd = "";
    
    //put your own params here
//    private static final String apiKey = "7k3ZCO9Fmokoc9VVSnpnd1kC";
//    private static final String secretKey = "vIxZDmHUMLixou6vQ0H283AeCA2UNToS";
    private static final String cuid = "sadadaw";
    private static String text = "";
    
//    private static final String apikey2 = "Uh48SfqWHbtDF3MU1QRonY1x";
//    private static final String secretKey2 ="b3reGcwZRS6Vzq8s4Hc2NB1AQ7aQ1M3b";
    
//    private static final String apikey3 = "rn1RResh8KA8Ro5D0SETjlRP";
//    private static final String secretKey3 ="OQb6IkmgWfEi1LkUQQT3bB2xu9XLUkhc";
    
    private static Set<String> searchUserNames = new HashSet<String>();
    
    public static void main(String[] args) {
    	HelloWorldController hello = new HelloWorldController();
    	hello.cmdReverse("AAACAAAAAAAAADAAACAAAAAACAAAAADAAAAAA");
    }
    
    /*
    private static void getToken(String api,String secret) throws Exception {
        String getTokenURL = "https://openapi.baidu.com/oauth/2.0/token?grant_type=client_credentials" + 
            "&client_id=" + api + "&client_secret=" + secret;
        HttpURLConnection conn = (HttpURLConnection) new URL(getTokenURL).openConnection();
        token = new JSONObject(printResponse(conn)).getString("access_token");
    }
    */
    
    public static String printResponse(HttpURLConnection conn) throws Exception {
        if (conn.getResponseCode() != 200) {
            // request error
            return "";
        }
        InputStream is = conn.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuffer response = new StringBuffer();
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();
        System.out.println(new JSONObject(response.toString()).toString(4));
        return response.toString();
    }
    
    /*
     * 获取文字转化成语音
     */
    @RequestMapping(value="/read", method=RequestMethod.GET)
    public void read(@RequestParam(value="text", required=false) String text,@RequestParam(value="type", required=false) String type,HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	if(!"1".equals(type)){
//    		getToken(apiKey,secretKey);
    		token = token1;
        	response.setHeader("Access-Control-Allow-Origin", "*");
            HttpURLConnection conn = (HttpURLConnection) new URL(serverURL2).openConnection();
            
            // add request header
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // send request
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes("tex="+URLEncoder.encode(text,"UTF-8")+"&lan="+URLEncoder.encode("zh","UTF-8")+"&ctp="+URLEncoder.encode("1","UTF-8")+"&tok="+URLEncoder.encode(token,"UTF-8")+"&cuid="+URLEncoder.encode(cuid,"UTF-8")+"&vol="+URLEncoder.encode("15","UTF-8"));
            wr.flush();
            wr.close();

            InputStream fis=conn.getInputStream();
            response.setHeader("Content-Type","audio/mp3");
            OutputStream os = response.getOutputStream();
            for(int i=0;i<=conn.getContentLength()/1024;i++){
            	byte[] bis = new byte[1024];
                fis.read(bis);
                os.write(bis);
            }    
    	}else{
    		response.setHeader("Access-Control-Allow-Origin", "*");
    		
    		String outputFormat = com.example.demo.tool.AudioOutputFormat.Riff16Khz16BitMonoPcm;
            String deviceLanguage = "ja-JP";
            String genderName = Gender.Female;
            String voiceName = "Microsoft Server Speech Text to Speech Voice (ja-JP, Ayumi, Apollo)";
    		
            try{
            	byte[] audioBuffer = TTSService.Synthesize(text, outputFormat, deviceLanguage, genderName, voiceName);
            	
            	response.setHeader("Content-Type","audio/wav");
                OutputStream os = response.getOutputStream();
                os.write(audioBuffer);
            	
                   
            }catch(Exception e){
            	e.printStackTrace();
            }
    	}
    }
    
    /*
     * 获取语音转化成文字
     */
    public String convertListenToText(String speech, long len, HttpServletResponse response) throws Exception {
//    	getToken(apiKey,secretKey);
    	token = token1;
    	response.setHeader("Access-Control-Allow-Origin", "*");
        HttpURLConnection conn = (HttpURLConnection) new URL(serverURL1).openConnection();
        // construct params
        JSONObject params = new JSONObject();
        params.put("format", "wav");
        params.put("rate", 16000);
        params.put("channel", "1");
        params.put("token", token1);
        params.put("cuid", cuid);
        params.put("len", len);
        params.put("speech", speech);

        // add request header
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        conn.setDoInput(true);
        conn.setDoOutput(true);

        // send request
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(params.toString());
        wr.flush();
        wr.close();

        String result = printResponse(conn);
		String text = (new JSONObject(result)).get("result").toString();
		int l = text.length();
		text = text.substring(0, l - 3).substring(2);
        return text;
    }
    
    /*
     * 语义分析
     */
    public JSONObject analysisText(String bot_id) throws Exception {
//      getToken(apikey3,secretKey3);
      token = token3;
      // construct params
      JSONObject actionParams = new JSONObject();
      JSONObject mapRequest = new JSONObject();
      JSONObject mapQueryInfo = new JSONObject();

      actionParams.put("version", "2.0");
      actionParams.put("token", token);
      actionParams.put("bot_id", bot_id);
      actionParams.put("log_id", UUID.randomUUID().toString().replaceAll("-", ""));
      actionParams.put("request", mapRequest);
      actionParams.put("bot_session", "");
      mapRequest.put("user_id","98d0239a9d584fc1a4df2eb3c4ac09a4 ");
      mapRequest.put("query",text);
      mapRequest.put("query_info",mapQueryInfo);
      mapRequest.put("bernard_level",1);
      
      // 请求信息来源，可选值："ASR","KEYBOARD"。ASR为语音输入，KEYBOARD为键盘文本输入。针对ASR输入，UNIT平台内置了纠错机制，会尝试解决语音输入中的一些常见错误
      mapQueryInfo.put("source","KEYBOARD");
      mapQueryInfo.put("type","TEXT");
      String talkUrl = "https://aip.baidubce.com/rpc/2.0/unit/bot/chat";
              
      String result1 = HttpUtil.post(talkUrl, token, "application/json", actionParams.toString());

      JSONObject reulstJson =  new JSONObject(result1);
      
      return reulstJson;
    }
    
    /*
     * 计算机器人的指令
     */
    public String textToCmd(int textFlag, JSONObject job) {
    	String robotCode = "";
    	String singleRobotCode = "";
    	if(textFlag == 2) {
    		singleRobotCode = "C";
    	} else if(textFlag == 4) {
    		singleRobotCode = "F";
    	}
    	else {
    		if(job != null) {
    			JSONArray slots = job.getJSONArray("slots");
    			String direction = "A";
    			if(slots.length()>0){  
    				for(int i=0;i<slots.length();i++){
    					JSONObject action = slots.getJSONObject(i);
    					if("user_direction".equals(action.getString("name"))) {
    						if(action.getString("original_word").indexOf("前") != -1) {
    							direction = "A";
    						} else if(action.getString("original_word").indexOf("后") != -1) {
    							direction = "B";
    						}else if(action.getString("original_word").indexOf("左") != -1) {
    							direction = "C";
    						}else if(action.getString("original_word").indexOf("右") != -1) {
    							direction = "D";
    						}
    					} else if ("user_generic_unit".equals(action.getString("name"))) {
    						Integer directionLen =  (int) Float.parseFloat(action.getString("normalized_word").substring(0, action.getString("normalized_word").indexOf("|")));
    						int step = (int) Math.round(directionLen/0.3);
    						if (direction == "C" || direction == "D") {
    							robotCode += direction;
    							for (int i1 = 0 ; i1< step; i1++){
    								robotCode += "A";
    							}
    						} else {
    							
    							for (int i1 = 0 ; i1< step; i1++){
    								robotCode += direction;
    							}
    						}
    					}
    				}
    			}
    		}
    	}
        if (singleRobotCode != "") {
        	robotCode = singleRobotCode;
        } else {
        	robotCode = robotCode +"F";
        }
        
        return robotCode;
    }
    
    /*
     * 原路返回
     */
    public void cmdReverse(String robotCode) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("CC");
    	
    	for(int i = (robotCode.length() - 1); i >= 0; i--) {
    		String code = null;
    		if(i == 0) {
    			code = robotCode.substring(0, 1);
    		} else {
    			code = robotCode.substring(i - 1, i);
    		}
    		
    		if("A".equals(code)) {
    			sb.append("A");
    		} else if("C".equals(code)) {
    			sb.append("D");
    		} else if("D".equals(code)) {
    			sb.append("C");
    		}
    	}
    	sb.append("F");
    	backCmd = sb.toString();
    	System.out.println(backCmd);
    }
    
    @RequestMapping(value="/listen", method=RequestMethod.POST)
    public String listen(HttpServletRequest request,
            HttpServletResponse response,@RequestParam(value="len", required=false) long len,@RequestParam(value="speech", required=false) String speech) throws Exception {

    	// 获取文字
    	text = convertListenToText(speech, len, response);
        
    	if(!StringUtils.isEmpty(text) && (text.contains("返回") || text.contains("回来"))) {
    		RobotGoThread go = new RobotGoThread();
        	go.setParam(backCmd);
        	
        	Thread t = new Thread(go);
        	t.start();
        	
        	return text;
    	}
    	
    	
    	// 获取语义结果
    	// "62121":机器人行为  "62626":人名识别
    	String bot_id = "62121";
    	if(StringUtils.startsWithIgnoreCase(text, "寻找")) {
    		bot_id = "62626";
    		searchUserNames.clear();
    	}
    	JSONObject reulstJson = analysisText(bot_id);
        
        JSONObject qu_res = reulstJson.getJSONObject("result").getJSONObject("response").getJSONObject("qu_res");
        JSONArray candidates = qu_res.getJSONArray("candidates");
        
		if (bot_id == "62626") {
        	RobotGoThread go = new RobotGoThread();
        	go.setParam("C");
        	Thread t = new Thread(go);
        	t.start();
        	
			if (candidates.length() > 0) {
				for (int i = 0; i < candidates.length(); i++) {
					JSONArray slots = candidates.getJSONObject(i).getJSONArray("slots");
					if (slots.length() > 0) {
						for (int i1 = 0; i1 < slots.length(); i1++) {
							if ("user_name".equals(slots.getJSONObject(i1).getString("name"))) {
								searchUserNames.add(slots.getJSONObject(i1).getString("original_word"));
							}
						}
					}
				}
			}
			text = text + "|USER_SEARCH";
		} else {
        	// 1:机器人运行指令 4:停止 2:转圈  3:拍照指令 5:启动
            int textFlag = -1;
            JSONObject job = null;
            if(candidates.length()>0){  
      		  for(int i=0;i<candidates.length();i++){  
      			job = candidates.getJSONObject(i);
      			if(job.getString("intent").equals("ROBOT_WALK")) {
      				textFlag = 1;
      				break;
    			} else if (job.getString("intent").equals("CMD_UPDATE_USER_SWITCH")) {
    				JSONArray slots = job.getJSONArray("slots");
    				if (slots.length() > 0) {
    					for (int i1 = 0; i1 < slots.length(); i1++) {
    						JSONObject slot = slots.getJSONObject(i1);
    						if ("off".equals(slot.getString("normalized_word"))) {
    							textFlag = 4;
    							break;
    						} else if("on".equals(slot.getString("normalized_word"))) {
    							textFlag = 5;
    							text = text + "|ROBOT_ON";
    							break;
    						}
    					}
    				}
    			} else if(job.getString("intent").equals("ROBOT_TURN")) {
      				textFlag = 2;
      				break;
      			} else if(job.getString("intent").equals("CMD_UPDATE_USER_TAKE_PHOTO")) {
      				textFlag = 3;
      				text = text + "|CMD_UPDATE_USER_TAKE_PHOTO";
      				break;
      			} else {
      				JSONArray slots = job.getJSONArray("slots");
      				 if(slots.length()>0){  
      			  		  for(int i1=0;i1<slots.length();i1++){
      			  			JSONObject slot =  slots.getJSONObject(i1);
      			  		    if ("stop".equals(slot.getString("normalized_word"))) {
      			  		    textFlag = 4;
      		  				break;
      			  		    }
    		  			  }
    		  		  }
      			}
      		  } 
      		}
            
            if(textFlag == 1 || textFlag == 2 || textFlag == 4) {
            	String robotCode = textToCmd(textFlag, job);
            	RobotGoThread go = new RobotGoThread();
            	go.setParam(robotCode);
            	
            	Thread t = new Thread(go);
            	t.start();
            	
            	cmdReverse(robotCode);
            }
        }
        return text;
    }
    
    /*
    @RequestMapping(value="/searchPerson", method=RequestMethod.POST)
    public String searchPerson(HttpServletRequest request,
            HttpServletResponse response,@RequestParam(value="len", required=false) long len,@RequestParam(value="speech", required=false) String speech) throws Exception {
//    	getToken(apiKey,secretKey);
    	token = token1;
    	response.setHeader("Access-Control-Allow-Origin", "*");
    	
    	searchUserNames = new HashSet<String>();
        HttpURLConnection conn = (HttpURLConnection) new URL(serverURL1).openConnection();
        // construct params
        JSONObject params = new JSONObject();
        params.put("format", "wav");
        params.put("rate", 16000);
        params.put("channel", "1");
        params.put("token", token);
        params.put("cuid", cuid);
        params.put("len", len);
        params.put("speech", speech);

        // add request header
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        conn.setDoInput(true);
        conn.setDoOutput(true);

        // send request
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(params.toString());
        wr.flush();
        wr.close();

        String result = printResponse(conn);
        text=(new JSONObject(result)).get("result").toString();
        int l=text.length();
        text = text.substring(0,l-3).substring(2);
        
        //test
        
//        getToken(apikey3,secretKey3);
        token = token3;
        // construct params
        JSONObject actionParams = new JSONObject();
        JSONObject mapRequest = new JSONObject();
        JSONObject mapQueryInfo = new JSONObject();

        actionParams.put("version", "2.0");
        actionParams.put("token", token);
        actionParams.put("bot_id", "62626");
        actionParams.put("log_id", UUID.randomUUID().toString().replaceAll("-", ""));
        actionParams.put("request", mapRequest);
        actionParams.put("bot_session", "");
        mapRequest.put("user_id","98d0239a9d584fc1a4df2eb3c4ac09a4 ");
        mapRequest.put("query",text);
        mapRequest.put("query_info",mapQueryInfo);
        mapRequest.put("bernard_level",1);
        
        // 请求信息来源，可选值："ASR","KEYBOARD"。ASR为语音输入，KEYBOARD为键盘文本输入。针对ASR输入，UNIT平台内置了纠错机制，会尝试解决语音输入中的一些常见错误
        mapQueryInfo.put("source","KEYBOARD");
        mapQueryInfo.put("type","TEXT");
        String talkUrl = "https://aip.baidubce.com/rpc/2.0/unit/bot/chat";
        
        
        String result1 = HttpUtil.post(talkUrl, token, "application/json", actionParams.toString());

        JSONObject reulstJson =  new JSONObject(result1);
        JSONArray candidates = reulstJson.getJSONObject("result").getJSONObject("response").getJSONObject("qu_res").getJSONArray("candidates");
        if(candidates.length()>0){  
  		  for(int i=0;i<candidates.length();i++){
  			JSONArray slots = candidates.getJSONObject(i).getJSONArray("slots");
  			if(slots.length()>0){  
  	  		  for(int i1=0;i1<slots.length();i1++){
  	  			  if ("user_name".equals(slots.getJSONObject(i1).getString("name"))) {
  	  				searchUserNames.add(slots.getJSONObject(i1).getString("original_word"));
  	  			  }
  	  		  	}
			  }
  		  	}
		  }
        //test
        return text;
    }
     */
    
//	private static String JP2CN(String input)
//	{
//		TransApi transApi = new TransApi("20171201000101441", "BNnjvjHitGJhj438Skk1");
//		String result = transApi.getTransResult(input, "jp", "zh");
//		String array = (new JSONObject(result)).get("trans_result").toString();
//		String array0 = (new JSONArray(array)).get(0).toString();
//		String dst = (new JSONObject(array0)).get("dst").toString();
//		try {
//			String dst2 = URLDecoder.decode(dst, "utf-8");
//			return dst2;
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//			return "";
//		}
//	}
    
	public static void fileWrite(String speech) {

		  FileOutputStream fop = null;
		  File file;

		  try {

		   file = new File("D:/1.wav");
		   fop = new FileOutputStream(file);

		   if (!file.exists()) {
		    file.createNewFile();
		   }

		   // get the content in bytes
		   byte[] contentInBytes = speech.getBytes();

		   fop.write(contentInBytes);
		   fop.flush();
		   fop.close();
		  } catch (IOException e) {
		   e.printStackTrace();
		  } finally {
		   try {
		    if (fop != null) {
		     fop.close();
		    }
		   } catch (IOException e) {
		    e.printStackTrace();
		   }
		  }
		 }
	
	public static String ConvertJPVoice2Text(String speech)
	{
		fileWrite(speech);
		ConvertApi convertApi = new ConvertApi("108a5a3ab51a4dc28baf89a502ed71b1");
		String result = convertApi.convertVoice2Text(speech, "ja-jp");
		String array = (new JSONObject(result)).get("NBest").toString();
		String array0 = (new JSONArray(array)).get(0).toString();
		String display = (new JSONObject(array0)).get("Display").toString();
		return display;
	}
	
	public static String ConvertJPVoice2Text(InputStream input)
	{
		ConvertApi convertApi = new ConvertApi("108a5a3ab51a4dc28baf89a502ed71b1");
		String result = convertApi.convertVoice2Text(input, "ja-jp");
		String array = (new JSONObject(result)).get("NBest").toString();
		String array0 = (new JSONArray(array)).get(0).toString();
		String display = (new JSONObject(array0)).get("Display").toString();
		return display;
	}
    
    @RequestMapping(value="/listen1", method=RequestMethod.POST)
    public String listen1(HttpServletRequest request,
            HttpServletResponse response,@RequestParam(value="len", required=false) long len,@RequestParam(value="speech", required=false) String speech) throws Exception {
    	response.setHeader("Access-Control-Allow-Origin", "*");
    	return ConvertJPVoice2Text(speech);
    }
    
    @RequestMapping(value="/face", method=RequestMethod.POST)
    public String face(HttpServletResponse response, @RequestParam(value="face", required=false) String face) throws Exception {
    	response.setHeader("Access-Control-Allow-Origin", "*");
    	//Tool.decodeBase64ToImage(face, "c:\\doc\\svn_hackathon201710\\tmp\\", "test.png");
    	String name = Face.identifyUser(Tool.decodeBase64ToImage(face));
    	if(name != null) {
    		return name;
    	} else {
    		return "none";
    	}
    }
    
    @RequestMapping(value="/faceUsers", method=RequestMethod.POST)
    public String faceUsers(HttpServletResponse response, @RequestParam(value="face", required=false) String face) throws Exception {
    	response.setHeader("Access-Control-Allow-Origin", "*");
    	//Tool.decodeBase64ToImage(face, "c:\\doc\\svn_hackathon201710\\tmp\\", "test.png");
    	List<String> names = Face.identifyUsers(Tool.decodeBase64ToImage(face));
    	
    	if(names.isEmpty() || names.size() == 0 || names == null) {
    		return "找不到，请重新规划路径。";
    	} else {
    		String result = "";
        	for(int i=0;i<names.size();i++){
        		if(searchUserNames.contains(names.get(i))) {
        			if (i != names.size() - 1) {
        				result += names.get(i) + "和";
        			} else {
        				result += names.get(i);
        			}
        		}
        	}
        	if(result == "") {
        		result="找不到，请重新规划路径。";
        	} else {
        		result=result+",已找到。";
        		
            	RobotGoThread go = new RobotGoThread();
            	go.setParam("F");
            	Thread t = new Thread(go);
            	t.start();
        	}
    		return result;
    	}
    }
 
    /*
    @RequestMapping(value="/action", method=RequestMethod.GET)
    public String action(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value="text", required=false) String text,
    		@RequestParam(value="type", required=false) String type) throws Exception {
    	if("1".equals(type)) {
    		text = JP2CN(text);
    	}
    	
    	getToken(apikey2,secretKey2);
    	response.setHeader("Access-Control-Allow-Origin", "*");
    	
    	// 请求URL
        String talkUrl = "https://aip.baidubce.com/rpc/2.0/solution/v1/unit_utterance";
        try {
            // 请求参数
            String params = "{\"scene_id\":14002,\"session_id\":\"\",\"query\":\"" + text + "\"}";
            String result = HttpUtil.post(talkUrl, token, "application/json", params);
            result = (new JSONObject(result)).get("result").toString();
            String action_list = (new JSONObject(result)).get("action_list").toString();
            String action = (new JSONArray(action_list)).get(0).toString();
            String action_id = (new JSONObject(action)).get("action_id").toString();
            String schema = (new JSONObject(result)).get("schema").toString();
            String slots = (new JSONObject(schema)).get("bot_merged_slots").toString();
            System.out.println(slots);
            JSONArray slotsArray = (new JSONArray(slots));
            
            if("weather_satisfy".equals(action_id)){
            	String city = "南京";
            	String time = "今天";
            	for(int i=0;i<slotsArray.length();i++){
            		if("user_time".equals((new JSONObject(slotsArray.get(i).toString())).get("type").toString())){
            			time = (new JSONObject(slotsArray.get(i).toString())).get("original_word").toString();
            		}
            		if("user_loc".equals((new JSONObject(slotsArray.get(i).toString())).get("type"))){
            			city = (new JSONObject(slotsArray.get(i).toString())).get("original_word").toString();
            		}
            		
            	}
            	System.out.println(city+time);
        		return APIController.weather(city, time);
            }
            if("news_satisfy".equals(action_id)){
            	String kind = "top";
            	for(int i=0;i<slotsArray.length();i++){
            		if("user_topic".equals((new JSONObject(slotsArray.get(i).toString())).get("type").toString())){
            			kind = (new JSONObject(slotsArray.get(i).toString())).get("original_word").toString();
            		}            		
            	}
            	System.out.println(kind);
            	return NewsController.news(kind);
            }
            if("movie_satisfy".equals(action_id)){
            	return MovieController.movieList();
            }
            if("salary_satisfy".equals(action_id)){
            	return "因为数据和网络问题，该功能尚在开发中，敬请期待~";
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        if("1".equals(type)) {
        	return "わかりません。";
        } else {
        	return "我没能明白，换个问题吧。";
        }
    }
    */
}