package com.openai.board.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.openai.board.dto.TagDto;

public class HashTagProccess {
	public List<TagDto> addTagWord(String content, int bnum) 
			throws Exception {
		String openApiURL = "http://aiopen.etri.re.kr:8000/WiseNLU";  
		String accessKey = "57d751ca-cfea-483c-bce6-4f75ffa84743"; // 諛쒓툒諛쏆� API Key
		String analysisCode = "ner";        // �뼵�뼱 遺꾩꽍 肄붾뱶
		String text = content;           // 遺꾩꽍�븷 �뀓�뒪�듃 �뜲�씠�꽣
		Gson gson = new Gson();

		Map<String, Object> request = new HashMap<>();
		Map<String, String> argument = new HashMap<>();

		argument.put("analysis_code", analysisCode);
		argument.put("text", text);

		request.put("access_key", accessKey);
		request.put("argument", argument);

		URL url;
		Integer responseCode = null;
		String responBodyJson = null;
		Map<String, Object> responeBody = null;

		url = new URL(openApiURL);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setDoOutput(true);

		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(gson.toJson(request).getBytes("UTF-8"));
		wr.flush();
		wr.close();

		responseCode = con.getResponseCode();
		InputStream is = con.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuffer sb = new StringBuffer();

		String inputLine = "";
		while ((inputLine = br.readLine()) != null) {
			sb.append(inputLine);
		}
		responBodyJson = sb.toString();

		// http �슂泥� �삤瑜� �떆 泥섎━
		if ( responseCode != 200 ) {
			// �삤瑜� �궡�슜 異쒕젰
			System.out.println("[error] " + responBodyJson);
			return null;
		}

		responeBody = gson.fromJson(responBodyJson, Map.class);
		Integer result = ((Double) responeBody.get("result")).intValue();
		Map<String, Object> returnObject;
		List<Map> sentences;

		// 遺꾩꽍 �슂泥� �삤瑜� �떆 泥섎━
		if ( result != 0 ) {
			// �삤瑜� �궡�슜 異쒕젰
			System.out.println("[error] " + responeBody.get("result"));
			return null;
		}

		// 遺꾩꽍 寃곌낵 �솢�슜
		returnObject = (Map<String, Object>) responeBody.get("return_object");
		sentences = (List<Map>) returnObject.get("sentence");

		Map<String, TagDto> wordMap = new HashMap<String, TagDto>();
		List<TagDto> words = null;

		for( Map<String, Object> sentence : sentences ) {
			// �삎�깭�냼 遺꾩꽍湲� 寃곌낵 �닔吏� 諛� �젙�젹
			List<Map<String, Object>> analysisResult = 
					(List<Map<String, Object>>) sentence.get("morp");

			for( Map<String, Object> wInfo : analysisResult ) {
				String word = (String) wInfo.get("lemma");
				TagDto tag = wordMap.get(word);
				if ( tag == null ) {
					tag = new TagDto();
					//媛� �뜲�씠�꽣 setting
					tag.setT_bnum(bnum);
					tag.setT_word(word);
					tag.setT_type((String)wInfo.get("type"));
					tag.setT_count(1);
					wordMap.put(word, tag);
				} else {
					tag.setT_count(tag.getT_count() + 1);
				}
			}
		}

		if ( 0 < wordMap.size() ) {//移댁슫�듃濡� �젙�젹
			words = new ArrayList<TagDto>(wordMap.values());
			words.sort((word1, word2) -> {//�엺�떎�떇(�옄諛붿뒪�겕由쏀듃�쓽 �솕�궡�몴 �븿�닔)
				return word2.getT_count() - word1.getT_count();
			});
		}
		
		return words;
	}//method end
}//class end




