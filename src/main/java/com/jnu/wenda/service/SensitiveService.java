package com.jnu.wenda.service;

import com.mysql.cj.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Joe
 */
@Service
public class SensitiveService implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);

    private class TrieNode {
        //是否结尾
        private boolean end = false;

        //当前节点下所有子节点
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public void addSubNode(Character character, TrieNode node) {
            subNodes.put(character, node);
        }

        public TrieNode getSubNode(Character key) {
            return subNodes.get(key);
        }

        public boolean isKeyWordEnd() {
            return end;
        }

        public void setKeyWordEnd(boolean end) {
            this.end = end;
        }

    }

    private TrieNode root = new TrieNode();

    //初始化bean时会加载这个方法
    @Override
    public void afterPropertiesSet() {
        InputStream inputStream = null;
        InputStreamReader reader = null;
        BufferedReader stream = null;
        try {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            reader = new InputStreamReader(inputStream);
            stream = new BufferedReader(reader);
            String text = "";
            while ((text = stream.readLine()) != null) {
                addWord(text.trim());
            }

        } catch (Exception e) {
            logger.error("读取敏感词文件失败" + e.getMessage());
        } finally {
            try {
                stream.close();
                reader.close();
                inputStream.close();
            } catch (IOException e) {
                logger.error("关闭io流失败"+e.getMessage());
            }
        }
    }

    //增加敏感词
    private void addWord(String lineText) {
        TrieNode tmpNode = root;
        for (int i = 0; i < lineText.length(); i++) {
            Character c = lineText.charAt(i);
            TrieNode node = tmpNode.getSubNode(c);
            if (node == null) {
                node = new TrieNode();
                tmpNode.addSubNode(c, node);
            }
            tmpNode = node;
            if (i == lineText.length() - 1) {
                tmpNode.setKeyWordEnd(true);
            }

        }
    }

    public String filter(String str) {

        if (StringUtils.isNullOrEmpty(str)) {
            return str;
        }
        String text = str.trim();
        StringBuilder sb = new StringBuilder();
        String replacement = "***";
        TrieNode tempNode = root;
        int begin = 0;
        int pos = 0;

        while (pos < text.length()) {
            char c = text.charAt(pos);
            //如果非法字符，跳过
            if (isSymbol(c)) {
                if (tempNode == root) {
                    sb.append(c);
                    begin++;
                }
                ++pos;
                continue;
            }
            tempNode = tempNode.getSubNode(c);
            //如果没有，直接结束
            if (tempNode == null) {
                sb.append(text.charAt(begin));
                pos = begin + 1;
                begin = pos;
                tempNode = root;
            } else if (tempNode.isKeyWordEnd()) {
                //发现敏感词
                sb.append(replacement);
                pos += 1;
                begin = pos;
                tempNode = root;
            }else{
                pos++;
            }

        }

        sb.append(text.substring(begin));
        return sb.toString();

    }

    private boolean isSymbol(char c) {
        int ic = (int) c;
        //东亚文字及非字母即认为是非法
        return !Character.isLetterOrDigit(c) && (ic < 0x2E80 || ic > 0x9fff);
    }


}
