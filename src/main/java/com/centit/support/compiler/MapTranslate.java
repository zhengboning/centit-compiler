package com.centit.support.compiler;

import java.util.Map;

import com.centit.support.algorithm.ReflectionOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.algorithm.StringRegularOpt;

public class MapTranslate implements VariableTranslate {

	private Map<String,Object> varMap;
	//public
	public MapTranslate(){
		varMap = null;
	}
	public MapTranslate(Map<String,Object> varMap) {
		this.varMap = varMap;
	}
	
	@Override
	public String getVarValue(String varName) {
		if(varMap==null)
			return "\"\"";
		
		Object res = ReflectionOpt.attainExpressionValue(varMap, varName); 
			// varMap.get(varName);
		if(res==null)
			return "\"\"";
		return StringRegularOpt.quotedString(StringBaseOpt.objectToString(res));		
	}

	@Override
	public String getLabelValue(String labelName) {
		return getVarValue(labelName);
		/*if("''".equals(res))//res == null || 
			return labelName;
		return res;*/
	}

	public Map<String,Object> getVarMap() {
		return varMap;
	}

	public void setVarMap(Map<String,Object> varMap) {
		this.varMap = varMap;
	}

}

