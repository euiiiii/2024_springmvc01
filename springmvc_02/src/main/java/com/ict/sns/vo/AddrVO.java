package com.ict.sns.vo;

public class AddrVO {
	
	// 원래는 로그인 한 상태에서 주소록을 사용하므로 사용자 id, 사용자 테이블의 idx가 필요
	private String postcode, address, detailAddress, extraAddress;

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDetailAddress() {
		return detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}

	public String getExtraAddress() {
		return extraAddress;
	}

	public void setExtraAddress(String extraAddress) {
		this.extraAddress = extraAddress;
	}
	
}