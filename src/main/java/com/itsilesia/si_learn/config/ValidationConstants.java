package com.itsilesia.si_learn.config;

public final class ValidationConstants {
	private ValidationConstants() {
	}

	//common
	public static final int languageCodeLenMax = 6;
	//fixme: this could be very restrictive for TaskContent
	public static final int translationDictItemContentMaxLen = 4 << 10;

	//user
	public static final int passwordLenMax = 100;
	public static final int passwordLenMin = 8;
	public static final int nameLenMax = 100;
	public static final int surnameLenMax = 100;

	//courses, user-courses
	public static final int gradeMinScore = 0;
	public static final int gradeMaxScore = 1;
	/// (max 100KB)
	public static final int codeLenMax = 4 << 10;
	/// (max 100KB)
	public static final int codeInputLenMax = 4 << 10;
	///(max 999 chars)
	public static final int expectedOutputLenMax = 999;
	public static final int timeoutSecMin = 0;
	public static final int timeoutSecMax = 15;
	//course specific
	public static final int titleLenMin = 1;
	public static final int titleLenMax = 255;
	public static final int descriptionLenMin = 0;
	public static final int descriptionLenMax = 2 << 10;

}
