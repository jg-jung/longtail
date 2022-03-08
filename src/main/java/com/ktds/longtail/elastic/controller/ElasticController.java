package com.ktds.longtail.elastic.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ktds.longtail.elastic.service.ElasticService;

import lombok.RequiredArgsConstructor;

/**
*
* Sample 클래스
*
* @author 개발자
* @since 2022. 03. 02
* @version 1.0
* @see
*
* << 개정이력(Modification Information) >>
*  
*  수정일        수정자           수정내용
*  -------    --------      ---------------------------
*   2022. 03. 02 개발자     		최초생성
* 
*/
@RestController
@RequestMapping("/api/elastic")
@RequiredArgsConstructor
public class ElasticController {

	private final ElasticService elasticService;
	Logger logger = LoggerFactory.getLogger(ElasticController.class);
	
	
	@GetMapping("/all")
    public Object getAll() {
		logger.info("[/sample/elastic/all] Controller Method");
		
		
		return elasticService.getAllDoc();
    }	
	
	/**
	 * 메소드 상세 설명 : elasticsearch에 저장된 도큐먼트 검색조건으로 쿼링 메소드
	 * 
	 * 범위검색(range), 조건, 페이징, 소팅 기능
	 *
	 */
	@GetMapping("/order/list")
    public Object getOrderListData(@RequestParam(value="pageStart", required = false) int pageStart, 	
    						 @RequestParam(value="pageCnt", required = false) int pageCnt, 	
    						 @RequestParam(value="orderType1", required = false) String orderType1, 											//주문상태1 (전체, 요금확인중, 배송요청, 배차완료, 상차완료, 하차완료, 주문쉬소)
    						 @RequestParam(value="orderType2", required = false) String orderType2, 											//주문상태2 (전체, 일반, 긴급)
    						 @RequestParam(value="freightOwnerType" , required = false) String freightOwnerType, 								//계약구분 (전체, 일반화주 계약화주)
    						 @RequestParam(value="freightType" , required = false) String freightType, 											//화물구분 (전체, 일반, 합짐, 혼짐, 복화)
    						 @RequestParam(value="searchType" , required = false) String searchType, 											//검색어 종류
    						 @RequestParam(value="searchText" , required = false) String searchText,											//검색어 ()    														//주문번호
    						 @RequestParam(value="freightRegistStartDatetime" , required = false) Long freightRegistStartDatetime, 				//화물등록일 시작
    						 @RequestParam(value="freightRegistEndDatetime" , required = false) Long freightRegistEndDatetime, 					//화물등록일 종료
    						 @RequestParam(value="loadingStartDateTime" , required = false) Long loadingStartDateTime, 		//상차일시 종료
    						 @RequestParam(value="loadingEndtDateTime" , required = false) Long loadingEndtDateTime, 			//상차일시 시작
    						 @RequestParam(value="unloadingStartDateTime" , required = false) Long unloadingStartDateTime, 	//하차일시 종료
    						 @RequestParam(value="unloadingEndDateTime" , required = false) Long unloadingEndDateTime) {		//하차일시 종료

		logger.info("[getOrderListData] Controller Method");
		return elasticService.getOrderListData(pageStart,
									 pageCnt,
									 orderType1, 					
									 orderType2, 					
									 freightOwnerType, 			
									 freightType, 				
									 searchType, 					
									 searchText,				
									 freightRegistStartDatetime, 	
									 freightRegistEndDatetime, 		
									 loadingStartDateTime, 
									 loadingEndtDateTime, 	
									 unloadingStartDateTime,
									 unloadingEndDateTime);
    }
	
	/**
	 * 메소드 상세 설명 : elasticsearch에 저장된 도큐먼트 검색조건으로 주문번호로 상세 페이지 사용
	 */
	@GetMapping("/order/detail")
    public Object getOrderDetailData(@RequestParam(value="orderNumber") String orderNumber) {
		
		logger.info("[getOrderDetailData] Controller Method");		
		return elasticService.getOrderDetailData(orderNumber);
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 * 
	 * */
	@GetMapping("/order/mask/list")
    public Object getqryMaskingData(@RequestParam(value="orderId") String orderId) {
		
		logger.info("[/sample/elastic/qry] Controller Method");
		
		return elasticService.getMaskingDoc(orderId);
    }
}
