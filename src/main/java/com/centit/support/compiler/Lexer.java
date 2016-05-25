package com.centit.support.compiler;

import com.centit.support.algorithm.StringBaseOpt;

public class Lexer {

	/**
	 * @param args
	 */
	private String curWord;
	
	private boolean isBack;
	private String formulaSen;
	private boolean canAcceptOpt;
	private int startPos ;	
	
	public static final int  LANG_TYPE_DEFAULT=0;
	/**
	 * java c++ 的注释方式 用 // 单行注释，/* 多行注释
	 */
	public static final int LANG_TYPE_JAVA=1;
	/**
	 * SQL 的注释方式 用 -- 单行注释，/* 多行注释
	 */
	public static final int LANG_TYPE_SQL=2;
	
	private int languageType;
	
	public Lexer()
	{
		languageType = LANG_TYPE_JAVA;
		setFormula(null);
	}
	
	public Lexer(String sFormula)
	{
		languageType = LANG_TYPE_JAVA;
		setFormula(sFormula);
	}
	public Lexer(int langType)
	{
		this.languageType = langType;
		setFormula(null);
	}
	
	public Lexer(String sFormula,int langType)
	{
		this.languageType = langType;
		setFormula(sFormula);
	}
	
	public void setPreword(String preWord)
	{
		curWord = preWord; 
		isBack = true;
	}
	
	public void setFormula(String sFormula)
	{
		formulaSen = sFormula;
		isBack  = false;
		curWord = "";
		startPos = 0;
		canAcceptOpt = false;
	}
	
	public boolean isCanAcceptOpt() {
		return canAcceptOpt;
	}

	public void setCanAcceptOpt(boolean canAcceptOpt) {
		this.canAcceptOpt = canAcceptOpt;
	}
	
	public int getCurrPos()
	{
		return startPos;
	}
	
	static public boolean isLabel(String sWord)
	{   
		if(sWord.length() < 1)
			return false;
		char c = sWord.charAt(0); 
		return ( c == '_' ||
				( c >= 'a' && c <= 'z') ||
				( c >= 'A' && c <= 'Z')
			 );
	}
	
	public String getARawWord(){
		int sl = formulaSen.length();

		while((startPos < sl ) && (formulaSen.charAt(startPos) == ' ' || formulaSen.charAt(startPos) == 9 || formulaSen.charAt(startPos) == 10|| formulaSen.charAt(startPos) == 13)) startPos++;
		if(startPos >= sl) return "";
			

		int bp = startPos;
		// 数字
		if( (formulaSen.charAt(startPos)>='0' && formulaSen.charAt(startPos)<='9') || 
			//m_Formula.charAt(m_iStart)== '.' ||
			( ! canAcceptOpt && (formulaSen.charAt(startPos)== '-' || formulaSen.charAt(startPos)== '+' ) ) ){
			startPos++;
			int nPoints = 0;
			while ( startPos < sl  && (
					( formulaSen.charAt(startPos)>='0' && formulaSen.charAt(startPos)<='9') ||
                     formulaSen.charAt(startPos)=='.'  ))
			{ 
				if( formulaSen.charAt(startPos)=='.' ){
					nPoints ++;
					if (nPoints>1)
						break;
				}
				startPos ++;
			}
			canAcceptOpt = true;			
		// 标识符	
		} else if (( formulaSen.charAt(startPos)>='a' && formulaSen.charAt(startPos)<='z') ||
			( formulaSen.charAt(startPos)>='A' && formulaSen.charAt(startPos)<='Z') ||
			formulaSen.charAt(startPos)=='_' ||
			formulaSen.charAt(startPos)=='@'  ){
			startPos++;
			while ( startPos < sl  && (
					( formulaSen.charAt(startPos)>='0' && formulaSen.charAt(startPos)<='9') ||
					( formulaSen.charAt(startPos)>='a' && formulaSen.charAt(startPos)<='z') ||
					( formulaSen.charAt(startPos)>='A' && formulaSen.charAt(startPos)<='Z') ||
					  formulaSen.charAt(startPos)=='_' || formulaSen.charAt(startPos)=='.' ||
					  formulaSen.charAt(startPos)=='@' ) ) 
				startPos ++;
			canAcceptOpt = true;
		}else {
			canAcceptOpt = false;
			switch(formulaSen.charAt(startPos)){
			case '+':
				++startPos;
				if((startPos<sl) && ((formulaSen.charAt(startPos) == '=') || 
									 (formulaSen.charAt(startPos) == '+')  ) ) startPos ++;
				break;
			case '-':
				++startPos;
				if((startPos<sl) && ((formulaSen.charAt(startPos) == '=') || 
									 (formulaSen.charAt(startPos) == '-')  ) ) startPos ++;
				break;
			case '*':
				++startPos;
				if((startPos<sl) && ((formulaSen.charAt(startPos) == '*') || 
									 (formulaSen.charAt(startPos) == '=') ||
									 (formulaSen.charAt(startPos) == '/')   ) ) startPos ++;
				break;	
			case '/':
				++startPos;
				if((startPos<sl) && ((formulaSen.charAt(startPos) == '=') || 
									 (formulaSen.charAt(startPos) == '/') ||
									 (formulaSen.charAt(startPos) == '*')   ) ) startPos ++;
				break;	
				
			case '<':
				++startPos;
				if((startPos<sl) && ((formulaSen.charAt(startPos) == '=') || 
									 (formulaSen.charAt(startPos) == '>') ||
									 (formulaSen.charAt(startPos) == '<')   ) ) startPos ++;
				break;	
			case '>':
				++startPos;
				if((startPos<sl) && ((formulaSen.charAt(startPos) == '=') || 
									 (formulaSen.charAt(startPos) == '>')  ) ) startPos ++;
				break;
			case '=':
			case '!':
				++startPos;
				if((startPos<sl) && (formulaSen.charAt(startPos) == '=')) startPos ++;
				break;
			case '|':
				++startPos;
				if((startPos<sl) && (formulaSen.charAt(startPos) == '|')) startPos ++;
				break;
			case '&':
				++startPos;
				if((startPos<sl) && (formulaSen.charAt(startPos) == '&')) startPos ++;
				break;
			case '\"': //字符串
			case '\'': //字符串
				canAcceptOpt = true;
				startPos ++;
				break;
			case '.':
				++startPos;
				while ( startPos < sl  && 
						( formulaSen.charAt(startPos)>='0' && formulaSen.charAt(startPos)<='9') )
				{ 
					startPos ++;
				}				
				break;
			case ')':
				canAcceptOpt = true;
				startPos ++;
				break;
			default: //"+-*/"
				startPos ++;
				break;
			}
		}
		
		String str = formulaSen.substring(bp, startPos );
		return str;	
	}	
	
	public String getARegularWord()
	{
		String s = getARawWord();
		int sl = formulaSen.length();
		if("\"".equals(s)){
			int bp = startPos-1;
			while(startPos < sl && formulaSen.charAt(startPos) != '\"') {
				 if (this.languageType == LANG_TYPE_JAVA && formulaSen.charAt(startPos)=='\\')
						startPos++;
					 startPos++;
				}
			if(startPos >= sl)//没有找到配对的\"
				return null;			
			startPos ++;
			canAcceptOpt = true;
			s = formulaSen.substring(bp, startPos );
		}else if("\'".equals(s)){
			int bp = startPos-1;
			while(startPos < sl && formulaSen.charAt(startPos) != '\'') {
				 if(this.languageType == LANG_TYPE_JAVA &&  formulaSen.charAt(startPos)=='\\')
						startPos++;
					 startPos++;
				}
			if(startPos >= sl)//没有找到配对的\'
				return null;			
			startPos ++;
			canAcceptOpt = true;
			s = formulaSen.substring(bp, startPos );
		}
		return s;
	}
	
	/**
	 * 过滤掉 注释
	 * 系统支持两种注释， c++(java)  // /*
	 * 				sql 	  -- /*
	 * @return
	 */
	public String getAWord( )
	{
		if (isBack) {
			isBack = false;
			return curWord;
		}
		
		while(true){
			curWord =  getARegularWord();
			if(curWord==null || "".equals(curWord))
				break;
			else if((this.languageType == LANG_TYPE_JAVA && "//".equals(curWord)) ||
					(this.languageType == LANG_TYPE_SQL && "--".equals(curWord)) )
				this.seekToLineEnd();
			else if("/*".equals(curWord))
				this.seekToAnnotateEnd();
			else
				break;			
		}
		return curWord;
	}
	
	public String getAWord(boolean bAcceptOpt )
	{
		canAcceptOpt = bAcceptOpt;
		return getAWord( );
	}
	
	
	public String getARawWord(boolean bAcceptOpt )
	{
		canAcceptOpt = bAcceptOpt;
		return getARawWord( );
	}
	
	public void seekToLineEnd(){
		int sl = formulaSen.length();
		while( (startPos < sl ) && (formulaSen.charAt(startPos) != 10 ))
			startPos ++;
	}
	
	/**
	 * 将解释位置滑动到注释结束位置 '*'+'/'
	 */
	public void seekToAnnotateEnd(){
		int sl = formulaSen.length();
		while( (startPos < sl-1 ) && (formulaSen.charAt(startPos) != '*' || formulaSen.charAt(startPos+1) != '/' ))
			startPos ++;
		if(startPos < sl-1 && formulaSen.charAt(startPos) == '*' && formulaSen.charAt(startPos+1) == '/')
			startPos += 2;
		else 
			startPos = sl;
	}
	
	/**
	 * 移动到下一个），自动跳过之间的（）括号对
	 */
	public boolean seekToRightBracket()
	{
		int nBracket = 1;
		while(true){
			String sWord = getAWord(false);
			if (sWord==null || sWord.equals(""))
				return false;
			if (sWord.equals("("))
				nBracket ++;
			else if (sWord.equals(")"))
				nBracket --;
			if (nBracket==0)
				return true;
		}
	}	
	
	/**
	 * 移动到下一个]，自动跳过之间的[]括号对
	 */
	public boolean seekToRightSquareBracket()
	{
		int nBracket = 1;
		while(true){
			String sWord = getAWord(false);
			if (sWord==null || sWord.equals(""))
				return false;
			if (sWord.equals("["))
				nBracket ++;
			else if (sWord.equals("]"))
				nBracket --;
			if (nBracket==0)
				return true;
		}
	}
	
	/**
	 * 移动到下一个}，自动跳过之间的{}括号对
	 */
	public boolean seekToRightBrace()
	{
		int nBracket = 1;
		while(true){
			String sWord = getAWord(false);
			if (sWord==null || sWord.equals(""))
				return false;
			if (sWord.equals("{"))
				nBracket ++;
			else if (sWord.equals("}"))
				nBracket --;
			if (nBracket==0)
				return true;
		}
	}
	
	
	public void skipAOperand()
	{
		int nBracket = 0;
		String sWord;
		while(true){
			sWord = getAWord();
			if (sWord==null || sWord.equals(""))
				return;
			if (sWord.equals("("))
				nBracket ++;
			else if (sWord.equals(")")){
				nBracket--;
				if(nBracket<0) {
					setPreword(")");
					return;
				}
			}
	
			if(sWord.equals(",")){
				if(nBracket==0) {
					setPreword(",");
					return;
				}
			}
		}
	}
	
	public String getStringUntil(String szBreak)
	{
		int bp = startPos;
		int ep = startPos;
		while(true){
			ep = startPos;
			String sWord = getAWord(false);
			if (sWord == null || sWord.equals("") || sWord.equals(szBreak))
				break;
		}
		String str = formulaSen.substring(bp, ep );
		return str;	
	}
	
	public void resetToBegin()
	{
		isBack  = false;
		curWord = "";
		startPos = 0;
		canAcceptOpt = false;
	}	
	
	public boolean setPosition(int newPos)
	{
		if(formulaSen==null|| formulaSen.length()<=newPos)
			return false;
		isBack  = false;
		curWord = "";
		startPos = newPos;
		canAcceptOpt = false;
		return true;
	}	
	
	public boolean seekTo(char cSplit){
		int sl = formulaSen.length();
		while((startPos < sl ) && (formulaSen.charAt(startPos) != cSplit))
			startPos ++;
		if(startPos < sl){
			startPos ++;
			return true;
		}
		return false;
	}
	
	public boolean seekTo(String aword,final boolean skipAnnotate){
		while(true){
			curWord = skipAnnotate?this.getAWord():this.getARegularWord();
			if(curWord==null || "".equals(curWord))
				return false;
			if(curWord.equals(aword))
				return true;			
		}
	}
	
	public String getBuffer(int bp, int ep){
		if(ep-bp < 1)
			return null;			 
		return formulaSen.substring(bp, ep );
	}
	/**
	 * 
	 * @param aword
	 * @param caseSensitives 
	 * @param skipAnnotate
	 * @return 发现点
	 */
	public int findWord(String aword,final boolean caseSensitives,final boolean skipAnnotate)
	{
		String cWord=skipAnnotate?this.getAWord():this.getARegularWord();
		while(!StringBaseOpt.isNvl(cWord)){
			if(cWord.equals(aword) || (!caseSensitives && cWord.equalsIgnoreCase(aword)) )
				return this.getCurrPos() - cWord.length();
			cWord=skipAnnotate?this.getAWord():this.getARegularWord();
		}	
		return -1;
	}

	public int getNoteType() {
		return languageType;
	}

	public void setNoteType(int noteType) {
		this.languageType = noteType;
	}

}
