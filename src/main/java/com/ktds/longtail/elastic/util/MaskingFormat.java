package com.ktds.longtail.elastic.util;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MaskingFormat {

	/**아이디 마스킹 정책 :: 앞 3글자 표기 (나머지 *처리)*/
	public String maskingId(String userId){
		String maskingId = null;
		try {
			maskingId = userId.replaceAll("(?<=.{3})." , "*");
		} catch (Exception e) {
			maskingId = userId;
		}

		return maskingId;
	}
	
	/**이름 마스킹 정책 :: 뒤 1글자 숨김*/
	public String maskingName(String userName){
		String maskingName = null;
		
		try {
			if(userName != null) {
				// 한글만 (영어, 숫자 포함 이름은 제외) 
				String regex = "(^[가-힣]+)$";
				
				Pattern namePatterm = Pattern.compile( "(^[가-힣]+)$");
				
				Matcher matcher = namePatterm.matcher(userName);
				
				if(matcher.find()) {
					int length = userName.length()-1;
					maskingName = userName.replaceAll("(?<=.{"+length+"})." , "*");
				}else {
					int length = userName.length()-userName.length()/3;
					maskingName = userName.replaceAll("(?<=.{"+length+"})." , "*");
				}
			}
		} catch (Exception e) {
			maskingName = userName;
		}
		return maskingName;
	}
	
	/**외국인이름 마스킹 정책 :: 뒤 1/3글자 숨김*/
	public String maskingNameFo(String userName){
		String maskingName = null;
		
		try {
			int length = userName.length()-1;
			
			maskingName = userName.replaceAll("(?<=.{"+length+"})." , "*");
		} catch (Exception e) {
			maskingName = userName;
		}
		
		return maskingName;
	}
	
	/**운전면허번호 마스킹 정책 :: 가운데6글자 숨김*/
	public String maskingDriverLicence(String driverLicense){
		String rvt = "";
		try {
			String licenseNum1 = driverLicense.substring(0, 2);
			String licenseNum2 = driverLicense.substring(2, 4);
			String licenseNum3 = driverLicense.substring(4, 10);
			String licenseNum4 = driverLicense.substring(10, 12);
			
			String maskingDriverLicense = licenseNum3.replaceAll("(?<=.{0})." , "*");
			
			rvt = licenseNum1+"-"+licenseNum2+"-"+maskingDriverLicense+"-"+licenseNum4;
		} catch (Exception e) {
			rvt = driverLicense;
		}
		
		return rvt;
	}
	
	/**사업자번호 마스킹 정책 :: 뒤 5글자 숨김*/
	public String maskingCorporation(String corporationNum){
		String str2 = "";
		
		try {
			String maskingCorporationNum;
			String str = "";
			String[] strArray = corporationNum.split("-");
			for(int i =0 ; i<strArray.length;i++) {
				str += strArray[i];
			}
			int length = str.length() - 5;
			
			maskingCorporationNum = str.replaceAll("(?<=.{"+length+"})." , "*");
			int len = 0;
			for(int i =0 ; i<strArray.length;i++) {
				if(i == 0) {
					len = strArray[i].length();
					str2 += maskingCorporationNum.substring(0, len)+"-";
				}else if(i == strArray.length-1) {
					str2 += maskingCorporationNum.substring(len, len+strArray[i].length());
					len = strArray[i].length();
				}else {
					str2 += maskingCorporationNum.substring(len, len+strArray[i].length())+"-";
					len = len + strArray[i].length();
					
				}
			}
		} catch (Exception e) {
			str2 = corporationNum;
		}
		
		return str2;
	}
	
	/**주민번호/외국인번호 마스킹 정책 :: 	뒤 6글자 숨김*/
	public String maskingJumin(String userJumin){
		String rvt = "";
		try {
			String Jumin1 = userJumin.substring(0, 6);
			String Jumin2 = userJumin.substring(6, 13);
			String maskingJumin = Jumin2.replaceAll("(?<=.{1})." , "*");
			
			rvt = Jumin1+"-"+maskingJumin;
		} catch (Exception e) {
			rvt = userJumin;
		}
		
		return rvt;
	}
	
	/**이메일 마스킹 정책 :: 	뒤 3글자  숨김(예:hongkild***@kt.com)*/
	public String maskingEmail(String userEmail){
		String rvt = "";
		try {
			String maskingEmail = null;
			String[] emailArray = null;
			if(userEmail == null || userEmail == " ") {
				maskingEmail = "";
			}else {
				
				emailArray = userEmail.split("@");
				int length = emailArray[0].length()-3;
				if(length > -1) {
					
					maskingEmail = emailArray[0].replaceAll("(?<=.{"+length+"})." , "*");
				}else {
					maskingEmail = emailArray[0];
				}
				
			}
			rvt = maskingEmail+"@"+emailArray[1];
		} catch (Exception e) {
			rvt = userEmail;
		}
		
		
		return rvt;
	}
	
	/** 휴대폰번호/전화번호 마스킹 정책 :: 국번 뒤2글자 번호앞 1글자 숨김(예: 010-12**-*678)*/
	public String maskingPhone(String userPhone){
		 Pattern tellPattern = Pattern.compile( "^(01\\d{1}|02|0505|0502|0506|0\\d{1,2})-?(\\d{3,4})-?(\\d{4})");

		 String result = null;
		 
		 try {
			 if(userPhone != null) {
				 Matcher matcher = tellPattern.matcher(userPhone);
				 String str1;
				 String str2;
				 String str3;
				 
				 if(matcher.matches()) {
					 str1 = matcher.group(1);
					 str2 = matcher.group(2);
					 str3 = matcher.group(3);
				 }else{
					 str1 = userPhone.substring(0, 3);
					 str2 = userPhone.substring(3, 7);
					 str3 = userPhone.substring(7, 11);
				 }
				 
				 int length = str2.length()-2;
				 
				 String mas1 = str2.replaceAll("(?<=.{"+length+"})." , "*");
				 
				 String mas2 = "*"+str3.substring(1,4);
				 
				 result = str1+"-"+mas1+"-"+mas2;
			 }
		} catch (Exception e) {
			result = userPhone;
		}
		 
        return result;
	}

	/**계좌번호 마스킹 : 앞 6글자 표기(나머지 *처리, 예:123-45-6**-***)*/
	public String accountNoMasking(String accountNo) {
		String str2 = "";
		
		try {
			if(accountNo != null) {
				String accountArray[] = accountNo.split("-");
				String str = "";
				for(int i =0 ; i<accountArray.length;i++) {
					str += accountArray[i];
				}
				String maskingAccount = null;
				maskingAccount = str.replaceAll("(?<=.{6})." , "*");
				str2 = "";
				
				int length = 0;
				for(int i =0 ; i<accountArray.length;i++) {
					if(i == 0) {
						length = accountArray[i].length();
						str2 += maskingAccount.substring(0, length)+"-";
					}else if(i == accountArray.length-1) {
						str2 += maskingAccount.substring(length, length+accountArray[i].length());
						length = accountArray[i].length();
					}else {
						str2 += maskingAccount.substring(length, length+accountArray[i].length())+"-";
						length = length + accountArray[i].length();
						
					}
				}
			}
		} catch (Exception e) {
			str2 = accountNo;
		}
		
		return str2;
	}
	
	/**신용카드 마스킹 : 앞 6글자 표기, 뒤 1글자 표기(나머지 * 처리, 예:1234-56**-****-***5)*/
	public String cardMasking(String cardNo) { // 카드번호 16자리 또는 15자리 '-'포함/미포함 상관없음 
		
		String regex = "(\\d{4})-?(\\d{4})-?(\\d{4})-?(\\d{3,4})$";

		Pattern cardPattern = Pattern.compile(regex);
		String mas = null;
		
		try {
			if(cardNo != null) {
				Matcher matcher = cardPattern.matcher(cardNo);
				
				if (matcher.find()) {
					
					String str1 = matcher.group(1);
					String str2 = matcher.group(2).replaceAll("(?<=.{2})." , "*");
					String str3 = matcher.group(3).replaceAll("(?<=.{0}).","*");
					int length = matcher.group(4).length()-1;
					String str4 = matcher.group(4).replace("(?<=.{"+length+"})." , "*");
					String[] strArray = str4.split("");
					String str5 = "";
					for(int i=0;i<strArray.length;i++) {
						if(i==strArray.length-1) {
							str5 += strArray[i];
						}else {
							str5 += "*";
						}
						
					}
					
					String target = matcher.group(2) + matcher.group(3);
					char[] c = new char[length];
					Arrays.fill(c, '*');
					
					mas = str1+"-"+str2+"-"+str3+"-"+str5 ;
				}
			}
		} catch (Exception e) {
			mas = cardNo;
		}
		
		return mas;
	}
			
}


