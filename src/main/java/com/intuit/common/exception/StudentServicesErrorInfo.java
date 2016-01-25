/*
 *
 * =======================================================================
 *
 * Copyright (c) 2009-2015 Sony Network Entertainment International, LLC. All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Sony Network Entertainment International, LLC.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with
 * Sony Network Entertainment International, LLC.
 *
 * =======================================================================
 *
 * For more information, please see http://www.sony.com/SCA/outline/corporation.shtml
 *
 */

package com.intuit.common.exception;

/**
 * @author ksamii
 * @since 1/20/16.
 */
public class StudentServicesErrorInfo {
	private String id;
	private int code;
	private String details;

	public StudentServicesErrorInfo(ErrorCode error) {
		this.id = error.name();
		this.code = error.getErrorCode();
		this.details = error.getMessage();
	}

	public String getId() {
		return id;
	}

	public int getCode() {
		return code;
	}

	public String getDetails() {
		return details;
	}
}
