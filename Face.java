package com.example.demo.tool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.aip.face.AipFace;
import com.baidu.aip.face.FaceVerifyRequest;
import com.baidu.aip.util.Util;

public class Face {

	public static final String APP_ID = "16521110";
	public static final String API_KEY = "7k3ZCO9Fmokoc9VVSnpnd1kC";
	public static final String SECRET_KEY = "vIxZDmHUMLixou6vQ0H283AeCA2UNToS";
	public static final AipFace faceClient = new AipFace(APP_ID, API_KEY, SECRET_KEY);

	public static void main(String[] args) throws Exception {
		// 注册目录下的所有人脸
		String picPath = "D:\\svn_hackathon201710\\home2\\";
		facesetAddUser(picPath);

		// 删除注册的所有人脸
		//303bd66543a1151a27a6d9b2312d50da
		//6173e87d8ba00ee34f3898ee26f0ac58
		//d09b336da5f1cbcbe5d5ea14dc6722d5
				
//		facesetDeleteUser("uid10", "303bd66543a1151a27a6d9b2312d50da");
//		facesetDeleteUser("uid11", "6173e87d8ba00ee34f3898ee26f0ac58");
//		facesetDeleteUser("uid12", "d09b336da5f1cbcbe5d5ea14dc6722d5");
		
		
//		identifyUser(Util.readFileByBytes("D:\\svn_hackathon201710\\home2\\group1\\席楠楠.jpg"));
		identifyUsers(Util.readFileByBytes("D:\\svn_hackathon201710\\home2\\group1\\席楠楠.jpg"));

		
//		detect(Util.readFileByBytes("D:\\svn_hackathon201710\\home2\\xinn\\1.jpg"));
		
	}
	
	public static void queryUserInfo(String uid, String groupIdKey) {
	    // 传入可选参数调用接口
	    HashMap<String, String> options = new HashMap<String, String>();
	    
	    String userId = uid;
	    String groupId = groupIdKey;
	    
	    // 用户信息查询
	    JSONObject res = faceClient.getUser(userId, groupId, options);
	    System.out.println(res.toString(2));
	}

	public static void facesetAddUser(String path) {
		File dir = new File(path);
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				if(files[i].isDirectory()) {
					String groupName = files[i].getName();
					File[] subFiles = files[i].listFiles();
					
					for(int j = 0; j < subFiles.length; j++) {
						byte[] imageBytes = null;
						try {
							imageBytes = Util.readFileByBytes(subFiles[j].getAbsolutePath());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						String image = Base64Util.encode(imageBytes);
					    String imageType = "BASE64";
					    String groupId = groupName;
					    String userId = "uid_" + i + "_" + j;
					    
					    // 传入可选参数调用接口
					    HashMap<String, String> options = new HashMap<String, String>();
					    options.put("user_info", subFiles[j].getName().replace(".jpg", ""));
					    options.put("quality_control", "NORMAL");
					    options.put("liveness_control", "LOW");
					    options.put("action_type", "REPLACE");
					    
					    // 人脸注册
					    JSONObject res = faceClient.addUser(image, imageType, groupId, userId, options);
					    System.out.println(res.toString(2));
					}
					
				} else {
					return;
				}
				
				

			    

			}
		}
	}

	public static void facesetDeleteUser(String uid, String faceTokenKey) {
		// 从人脸库中彻底删除用户
		
	    HashMap<String, String> options = new HashMap<String, String>();
	    
	    String userId = uid;
	    String groupId = "group1";
	    String faceToken = faceTokenKey;
	    
	    // 人脸删除
	    JSONObject res = faceClient.faceDelete(userId, groupId, faceToken, options);
	    System.out.println(res.toString(2));
	}


	public static void detect(byte[] imageBytes) {
	    // 传入可选参数调用接口
	    HashMap<String, String> options = new HashMap<String, String>();
	    options.put("face_field", "user_info");
	    options.put("max_face_num", "2");
	    options.put("face_type", "LIVE");
	    options.put("liveness_control", "LOW");
	    
	    String image = Base64Util.encode(imageBytes);
	    String imageType = "BASE64";
	    
	    // 人脸检测
	    JSONObject res = faceClient.detect(image, imageType, options);
	    System.out.println(res.toString(2));
	}
	
	/**
	 * 人脸识别
	 * @param image 待识别的人脸图片的二进制
	 * @return 姓名
	 */
	public static String faceVerify(byte[] imageBytes) {
		
	    String image = Base64Util.encode(imageBytes);
	    FaceVerifyRequest req = new FaceVerifyRequest(image, "BASE64");
	    ArrayList<FaceVerifyRequest> list = new ArrayList<FaceVerifyRequest>();
	    list.add(req);
	    JSONObject res = faceClient.faceverify(list);
	    System.out.println(res.toString(2));
	    return null;
		
	}
	
	
	public static String identifyUser(byte[] imageBytes) {
	    // 传入可选参数调用接口
	    HashMap<String, String> options = new HashMap<String, String>();
	    options.put("max_face_num", "1");
	    options.put("match_threshold", "80");
	    options.put("quality_control", "NORMAL");
	    options.put("liveness_control", "LOW");
//	    options.put("user_id", "233451");
//	    options.put("max_user_num", "3");

	    String image = Base64Util.encode(imageBytes);
	    String imageType = "BASE64";
	    String groupIdList = "group1";

	    // 人脸搜索
	    JSONObject res = faceClient.search(image, imageType, groupIdList, options);
	    JSONObject obj = (JSONObject)res.getJSONObject("result").getJSONArray("user_list").get(0);
	    System.out.println(obj.getString("user_info"));
	    return obj.getString("user_info");
	}
	
	public static List<String> identifyUsers(byte[] imageBytes) {
	    // 传入可选参数调用接口
	    HashMap<String, String> options = new HashMap<String, String>();
	    options.put("max_face_num", "4");
	    options.put("match_threshold", "70");
	    options.put("quality_control", "NORMAL");
	    options.put("liveness_control", "LOW");
	    options.put("max_user_num", "4");

	    String image = Base64Util.encode(imageBytes);
	    String imageType = "BASE64";
	    String groupIdList = "group1";

	    // 人脸搜索 M:N 识别
	    JSONObject res = faceClient.multiSearch(image, imageType, groupIdList, options);
	    JSONArray face_list = res.getJSONObject("result").getJSONArray("face_list");	    
	    List<String> userList = new ArrayList<String>();
	    String[] dd = null;
	    if(face_list.length()>0){  
    		  for(int i=0;i<face_list.length();i++){
    			  JSONArray user_list = face_list.getJSONObject(i).getJSONArray("user_list");
    			  if(user_list.length()>0){  
    	    		  for(int i1=0;i1<user_list.length();i1++){
    	    			  userList.add(user_list.getJSONObject(i1).getString("user_info"));
    	    			  }
    	    		  }
    		  }
         }
	    return userList;
	}
}
