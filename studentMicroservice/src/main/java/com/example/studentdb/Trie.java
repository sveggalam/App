package com.example.studentdb;

import com.example.studentdb.model.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trie {
    public TrieNode root;
    public Trie() {
        root = new TrieNode();
    }

    public void insert(String word,Student student) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            c = Character.toLowerCase(c);
            if (!node.children.containsKey(c)) {
                node.children.put(c, new TrieNode());
            }
            // Optional: associate a student with the node
            node.Ids.add(student.getId());
            node = node.children.get(c);
        }
        node.isEndOfWord = true;
    }

    public List<Integer> search(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            c = Character.toLowerCase(c);
            if (!node.children.containsKey(c)) {
                return List.of(); // Return empty list if not found
            }
            node = node.children.get(c);
        }
        return node.isEndOfWord ? node.Ids : List.of(); // Return associated student IDs if found
    }

    public boolean startsWith(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            c = Character.toLowerCase(c);
            if (!node.children.containsKey(c)) {
                return false;
            }
            node = node.children.get(c);
        }
        return true;
    }
    
    private static class TrieNode {
        Map<Character, TrieNode> children;
        boolean isEndOfWord;
        List<Integer>Ids = new ArrayList<>();// Optional: to store associated student data
        public TrieNode() {
            children = new HashMap<>();
            isEndOfWord = false;
        }
    }
}
// dont do anything