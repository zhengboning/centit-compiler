package com.centit.support.compiler;

import java.util.HashMap;
import java.util.Map;

public class FormulaTest {
	

	public static void testFormula() {
		Formula f = new Formula();
		f.setVariableTranslate(new MapTranslate());
		//System.out.println(f.m_preTreat.runPretreatment(" if(2>1,${userinfo.usercode},case(true,3<2,'hello',4*5<19,'helleo, weror','nihao,地球')) + ' !' "));
		String s = f.calculate("countnull(1,2,\"\",4,6,,3,,1)");
		System.out.println(s);
		//assertEquals("nihao,地球 !",s);
	}
	

	public static void testLexer() {
		Lexer l = new Lexer("hello jane , jan say!");
		System.out.println(l.findWord("jan",true,true));
		//assertEquals("nihao,地球 !",s);
		
		Formula f = new Formula();
		Map<String,Object> varMap = new HashMap<String,Object>();
		varMap.put("usercode", "000001");
		String s = f.calculate("today +  today() + ' '+'usercode' +usercode + ${otherValue} + ':' + ${usercode}",varMap);
		System.out.println(s);
	}
	
	public  static void  main(String[] args)   { 
		testFormula();
		//System.out.println(Double.valueOf("123123.4545").intValue());

	}
}
