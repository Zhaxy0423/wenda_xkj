package com.xkj.wenda.service;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class SensitiveService implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);
    private static final String DEFAULT_REPLACEMENT = "***";//敏感词替代符
    private TrieNode rootNode = new TrieNode();//根节点

    @Override
    public void afterPropertiesSet() throws Exception {
        try{
            /**读取敏感词文件*/
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWord.txt");
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String lineTxt;
            while((lineTxt=bufferedReader.readLine())!=null){
                addWord(lineTxt.trim());
            }
            reader.close();
        }catch (Exception e){
            logger.error("读取敏感词文件失败"+e.getMessage());
        }
    }
    /**增加关键词*/
    private void addWord(String lineTxt){
        TrieNode tempNode = rootNode;
        for(int i=0; i<lineTxt.length();i++){
            Character c = lineTxt.charAt(i);
            if(isSymbol(c)){
                continue;
            }
            /**获取根节点下的子节点。如不存在则创建一个node存放字符，
             * 并将该node作为关键词的根节点,
             * 如果存到关键词最后一个则将其设置为end=true
             * */
            TrieNode node = tempNode.getSubNode(c);
            if(node==null){
                node = new TrieNode();
                tempNode.addSubNode(c,node);
            }
            tempNode=node;
            if(i==lineTxt.length()-1){
                tempNode.setKeywordEnd(true);
            }
        }
    }

    private class  TrieNode{
        /**是不是关键词的结尾*/
        private boolean end = false;
        /**当前节点下的子节点*/
        private Map<Character,TrieNode> subNodes = new HashMap<Character, TrieNode>();
        /**添加节点*/
        public void addSubNode(Character key,TrieNode node){
            subNodes.put(key, node);
        }
        /**获取节点上的值*/
        TrieNode getSubNode(Character key){
            return subNodes.get(key);
        }

        boolean isKeywordEnd(){
            return end;
        }

        void  setKeywordEnd(boolean end){
            this.end=end;
        }
    }

    private boolean isSymbol(char c){
        int ic = (int) c;
        //东亚文字 0x2E80—0x9FFF
        return !CharUtils.isAsciiAlphanumeric(c)&&(ic < 0x2E80 || ic > 0x9FFF);
    }
    /**
     * 树的遍历实现
     * @param text
     * @return
     */
    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return text;
        }
        String replacement = DEFAULT_REPLACEMENT;
        StringBuilder result = new StringBuilder();
        TrieNode tempNode = rootNode;
        int begin = 0;//回滚数
        int position = 0;//当前位置

        while(position<text.length()){
            //遍历text字符串
            char c = text.charAt(position);
            if(isSymbol(c)){
                /**如果在根节点处开始遍历就出现空格，则将空格保存*/
                if(tempNode == rootNode){
                    result.append(c);
                    ++begin;
                }
                ++position;
                continue;
            }
            //依次访问树的子节点
            tempNode = tempNode.getSubNode(c);
            if(tempNode == null){
                //如果该节点下没有子节点则表示不存在敏感词
                result.append(text.charAt(begin));
                //下个字符测试
                position = begin + 1;
                begin = position;
                //回到初始树节点
                tempNode = rootNode;
            }else if(tempNode.isKeywordEnd()){
                //发现敏感词
                result.append(replacement);
                position = position + 1;
                begin = position;
                tempNode = rootNode;
            }else{
                ++position;
            }
        }
        result.append(text.substring(begin));
        return result.toString();
    }
       /*public static void main(String[] args) {
        SensitiveService sensitiveService = new SensitiveService();
        sensitiveService.addWord("色情");
        sensitiveService.addWord("赌博");
        System.out.println(sensitiveService.filter("你●好●色●情"));
       }*/
}
