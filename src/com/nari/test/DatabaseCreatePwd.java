package com.nari.test;

import com.nari.utils.RsaDecryptTool;
public class DatabaseCreatePwd {
	static RsaDecryptTool rsaTool = new RsaDecryptTool();
	
	public static void main(String[] args) {
		

//		rsa();
		
	}
	
	/**
	 * oracle加密登录密码
	 */
	public static  void rsa(){
		//加密
		String a= rsaTool.encrypt("New_4&5*oa");
		System.out.println(a);
		//解密
//		String aa= rsaTool.decrypt("HgvEprKjt6cuHYbaFn/kUSO2ERn3TvzbC120m2m1/gpVBD7XIFzu0Hq+YWX54bTnh2KPi50r5lac2ooAZcl/jR2/psNHxgH55LXgPNU2fI+yiCIMIR6ZsqIV3Syy30qEdqgbpuKoWXnugi8mPq8YBpyqC4BTT7OFCNom7eQyMe8\=");
//		System.out.println(aa);
	}
}
