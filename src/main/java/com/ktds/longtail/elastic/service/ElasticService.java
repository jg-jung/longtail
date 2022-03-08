package com.ktds.longtail.elastic.service;

import java.io.IOException;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ktds.longtail.elastic.util.CheckString;
import com.ktds.longtail.elastic.util.RestApiUtil;

import com.google.gson.JsonObject;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ElasticService {
	CheckString  cs = new CheckString();
	Logger logger = LoggerFactory.getLogger(ElasticService.class);
	
	@Value("")
	String apiDestUrl;
	
	@Value("")
	String elasticDestUrl;
	
	public RestHighLevelClient createConnection() {
        return new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost("127.0.0.1", 9200, "http")
            )
        );
    }
	
	/**
	 * 메소드 상세 설명 : elasticsearch에 저장된 doc get
	 * 사용X TEST용
	 */
	public Object getAllDoc() {
		logger.info("[/sample/elastic/all] Service Method");
		
		Map<String, String> rtn = new HashMap<>();
		
		String index = "orders"; //데이터베이스(database) 개념
        //문서 타입
        String type ="_search"; 	//테이블(table) 개념
        //문서 ID
        //String docId = "5924066a-604b-4366-a42b-f5d8f970ce0a";
        String docId = "";
        //문서조회 요청
        GetRequest request = new GetRequest(index, type, docId);
        
        //문서조회 결과
        GetResponse response = null;
        try(RestHighLevelClient client = createConnection();){
            
            response = client.get(request, RequestOptions.DEFAULT);
            
        }catch (Exception e) {
            e.printStackTrace();            
        }

        Map<String, Object> sourceAsMap = null;
        if(response.isExists()) {
            long version = response.getVersion();
            sourceAsMap = response.getSourceAsMap();
            //sourceAsMap.putAll(apiRtn);
        }
        
		return sourceAsMap;
	}
	
	
	/**
	 * 메소드 상세 설명 : elasticsearch에 저장된 도큐먼트 검색조건으로 쿼링 메소드
	 * 
	 * 범위검색(range), 조건, 페이징, 소팅 기능
	 *
	 */
	public List<Map<String, Object>> getOrderListData(int pageStart,
											int pageCnt,
											String orderType1, 					//주문상태1 (전체, 요금확인중, 배송요청, 배차완료, 상차완료, 하차완료, 주문쉬소)
											String orderType2, 					//주문상태2 (전체, 일반, 긴급)
											String freightOwnerType, 			//계약구분 (전체, 일반화주 계약화주)
											String freightType, 				//화물구분 (전체, 일반, 합짐, 혼짐, 복화)
											String searchType, 					//검색어 종류
											String searchText, 					//검색어 ()											
											Long freightRegistStartDatetime, 	//화물등록일 시작
											Long freightRegistEndDatetime, 		//화물등록일 종료
											Long loadingStartDateTime, 			//상차일시 종료
											Long loadingEndtDateTime, 			//상차일시 시작
											Long unloadingStartDateTime,		//하차일시 종료
											Long unloadingEndDateTime) {		//하차일시 종료
		
		logger.info("[getOrderListData] Service Method");
		
		List<Map<String, Object>> arrList = new ArrayList<>();
		
		// 배차, 주문상태2 긴급, 일반 정보 담는 Map
		Map<String, Object> result = new HashMap<>();
		// Map<String, Object> result2 = new HashMap<>();
		
		// [Search Request 객체 선언 <index>]
		SearchRequest searchRequest = new SearchRequest("orders");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		
		BoolQueryBuilder orQuery = QueryBuilders.boolQuery();

		// 쿼리생성
		// 주문상태1
		if(!orderType1.equals("ALL")) {
			QueryBuilder qb1 = QueryBuilders.matchPhraseQuery("orderStatus", orderType1);
			orQuery.must(qb1);			
		}
		
		// 주문상태2 (일반, 긴급) 조건: 긴급!! 상차일시로부터 2시간 이내면 긴급 
		if(orderType2.equals("URGENTDELIVERY")) {
			java.util.Date date = new Date();
			
			Long time = date.getTime();
			
			QueryBuilder qb2 = QueryBuilders.rangeQuery("freight.loadingDateTime")
											.gte((Long)time)
											.lte((Long)time  + 7200000L); //2시간 이내인 것 만 추출
			orQuery.must(qb2);			
			result.put("orderType2", "URGENTDELIVERY");
		}
		
		// 배차지연여부
		/*
		if(freightType.equals("DELAYDISPATCH")) {
			
			currentTime = System.currentTimeMillis();

			QueryBuilder qb3 = QueryBuilders.rangeQuery("freight.loadingDateTime")
											.gte((Long)currentTime )
											.lte((Long)currentTime + 7200L); 
			
			orQuery.must(qb3);
			result.put("orderType2", "DELAYDISPATCH");
		}
		*/

		if(!freightOwnerType.equals("ALL")) {
			// 계약구분
			QueryBuilder qb3 = QueryBuilders.matchPhraseQuery("freightOwnerType", freightOwnerType);
			orQuery.must(qb3);
		}

		// [검색어]
		// 주문번호
		if(searchType.equals("ORDERNUM") & !cs.checkString(searchText)) {
			QueryBuilder qb4 = QueryBuilders.matchPhraseQuery("orderNumber", searchText);
			orQuery.must(qb4);
		}else if(searchType.equals("FREIGHTID") & !cs.checkString(searchText)) {
			QueryBuilder qb5 = QueryBuilders.matchPhraseQuery("freightOwnerId", searchText);
			orQuery.must(qb5);			
		}else if(searchType.equals("CAROWNERID") & !cs.checkString(searchText)) {
			QueryBuilder qb6 = QueryBuilders.matchPhraseQuery("carOwnerId", searchText);
			orQuery.must(qb6);			
		}
		
		/*
		//화주명
		if(searchType == "FREIGHTNAME" & !cs.checkString(searchText)) {
			QueryBuilder qb7 = QueryBuilders.matchPhraseQuery("orderStatus", searchText);
			orQuery.must(qb7);
		}
		
		//차주명
		if(searchType == "CAROWNERNAME" & !cs.checkString(searchText)) {
			QueryBuilder qb8 = QueryBuilders.matchPhraseQuery("shipment.carOwnerName", searchText);
			orQuery.must(qb8);			
		}
		
		//차량번호
		if(searchType == "CARNUMBER" & !cs.checkString(searchText)) {
			QueryBuilder qb9 = QueryBuilders.matchPhraseQuery("orderStatus", searchText);
			orQuery.must(qb9);			
		}
		*/
		//상차일시
		QueryBuilder qb10 = QueryBuilders.rangeQuery("freight.loadingDateTime")
										.gte((Long)loadingStartDateTime)
										.lte((Long)loadingEndtDateTime);
		orQuery.must(qb10);
		//하차일시
		QueryBuilder qb11 = QueryBuilders.rangeQuery("freight.unloadingDateTime")
										.gte((Long)unloadingStartDateTime)
										.lte((Long)unloadingEndDateTime);
		orQuery.must(qb11);
		
		//화물등록일
		QueryBuilder qb12 = QueryBuilders.rangeQuery("freight.createdAt")
										.gte((Long)freightRegistStartDatetime)
										.lte((Long)freightRegistEndDatetime);
		orQuery.must(qb12);
		
		sourceBuilder
				.sort(SortBuilders.fieldSort("freight.loadingDateTime"))
				.query(orQuery);
		
		// [paging]
		sourceBuilder.from(pageStart); 
		sourceBuilder.size(pageCnt);

		// 쿼리 Builder to Search Request 인입
		searchRequest.source(sourceBuilder);
		
		// 쿼리 실행
		try(RestHighLevelClient client = createConnection();){
			SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			
			for(SearchHit s:searchResponse.getHits().getHits())
			  {
				  Map<String, Object> sourceMap = s.getSourceAsMap();
				  arrList.add(sourceMap);
			  }

			for(int i=0; i<arrList.size(); i++) {
				//elasticsearch 에서 얻어온 화주, 차주 ID 디시젼 API호출용 파라메터
				Map<String, Object> member = new HashMap<>();
				member.put("resultCarOwnerId", arrList.get(i).get("carOwnerId").toString());
				member.put("resultFreightOwnerId", arrList.get(i).get("freightOwnerId").toString());	

				//디시젼 API로부터 데이터를 받았다고 가정 후 elasticsearch 데이터 안에 인입
				Map<String, Object> carApiRetMsg = new HashMap<>();				
				carApiRetMsg.put("carOwnerId", "id1");
				carApiRetMsg.put("carOwnerPhone", "010-1234-1234");
				carApiRetMsg.put("carOwnerName", "차주_1");

				Map<String, Object> freightApiRetMsg = new HashMap<>();				
				freightApiRetMsg.put("freightOwnerId", "id1");
				freightApiRetMsg.put("freightOwnerPhone", "010-5678-5678");
				freightApiRetMsg.put("freightOwnerName", "화주_1");
				
				arrList.get(i).put("carOwnerInfo", carApiRetMsg);
				arrList.get(i).put("freightOwnerInfo", freightApiRetMsg);
			}
			
		}catch (ElasticsearchException e) { 
			if (e.status() == RestStatus.NOT_FOUND) { 
				logger.error("인덱스를 찾을 수 없습니다."); 
			} 
		} 
		catch (IOException e) { 
			logger.error("[Elastic search fail");			
		}		
		
		return arrList;
	}
	

	/**
	 * 메소드 상세 설명 : elasticsearch에 저장된 도큐먼트 검색조건으로 주문번호로 상세 페이지 사용
	 */
	public List<Map<String, Object>> getOrderDetailData(String orderNumber) {					// 주문번호

		logger.info("[getOrderDetailData] Service Method");
		
		List<Map<String, Object>> arrList = new ArrayList<>();
		
		// [Search Request 객체 선언 <index>]
		SearchRequest searchRequest = new SearchRequest("orders");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		
		BoolQueryBuilder orQuery = QueryBuilders.boolQuery();

		// 쿼리생성
		// 주문상태1
		if(!orderNumber.isEmpty()) {
			QueryBuilder qb1 = QueryBuilders.matchPhraseQuery("orderNumber", orderNumber);
			orQuery.must(qb1);			
		}

		sourceBuilder
				.sort(SortBuilders.fieldSort("freight.loadingDateTime"))
				.query(orQuery);


		// 쿼리 Builder to Search Request 인입
		searchRequest.source(sourceBuilder);
		
		// 쿼리 실행
		try(RestHighLevelClient client = createConnection();){
			SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			
			for(SearchHit s:searchResponse.getHits().getHits())
			  {
				  Map<String, Object> sourceMap = s.getSourceAsMap();
				  arrList.add(sourceMap);
			  }

			for(int i=0; i<arrList.size(); i++) {
				//elasticsearch 에서 얻어온 화주, 차주 ID 디시젼 API호출용 파라메터
				Map<String, Object> member = new HashMap<>();
				member.put("resultCarOwnerId", arrList.get(i).get("carOwnerId").toString());
				member.put("resultFreightOwnerId", arrList.get(i).get("freightOwnerId").toString());	

				//디시젼 API로부터 데이터를 받았다고 가정 후 elasticsearch 데이터 안에 인입
				Map<String, Object> carApiRetMsg = new HashMap<>();				
				carApiRetMsg.put("carOwnerId", "id1");
				carApiRetMsg.put("carOwnerPhone", "010-1234-1234");
				carApiRetMsg.put("carOwnerName", "차주_1");

				Map<String, Object> freightApiRetMsg = new HashMap<>();				
				freightApiRetMsg.put("freightOwnerId", "id1");
				freightApiRetMsg.put("freightOwnerPhone", "010-5678-5678");
				freightApiRetMsg.put("freightOwnerName", "화주_1");
				
				arrList.get(i).put("carOwnerInfo", carApiRetMsg);
				arrList.get(i).put("freightOwnerInfo", freightApiRetMsg);
			}
			
		}catch (ElasticsearchException e) { 
			if (e.status() == RestStatus.NOT_FOUND) { 
				logger.error("인덱스를 찾을 수 없습니다."); 
			} 
		} 
		catch (IOException e) { 
			logger.error("[Elastic search fail");			
		}		
		
		return arrList;
	}
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 메소드 상세 설명 : elasticsearch에 저장된 도큐먼트 검색조건으로 주문번호로 상세 페이지
	 */
	public List<Map<String, Object>> getMaskingDoc(String orderNumber) {					// 주문번호

		logger.info("[/sample/elastic/qry] Service Method");
		
		// [Result Set 선언]
		List<Map<String, Object>> arrList = new ArrayList<>();
		
		// [Search Request 객체 선언 <index>]
		SearchRequest searchRequest = new SearchRequest("orders");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		
		// [match_phrase 쿼리]
		// select, check box에서 넘겨진 데이터를 검색
		QueryBuilder builder = QueryBuilders
				.matchPhraseQuery("orderNumber", orderNumber); //주문번호
		
		// 쿼리 Builder to Search Request 인입
		searchRequest.source(sourceBuilder);
		
		// 쿼리 실행
		try(RestHighLevelClient client = createConnection();){
			SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			
			for(SearchHit s:searchResponse.getHits().getHits())
			  {
				
				  Map<String, Object> sourceMap = s.getSourceAsMap();
				  arrList.add(sourceMap);
			  }
			return arrList;
		}catch (ElasticsearchException e) { 
			if (e.status() == RestStatus.NOT_FOUND) { 
				logger.error("인덱스를 찾을 수 없습니다."); 
				} 
			} 
		catch (IOException e) { 
			logger.error("[Elastic search fail");
			
		}
		return null;
	}
}
