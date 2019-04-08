package com.owary.view;

import com.owary.MarkovModel;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

public class Texter implements Runnable{
    private JFrame jFrame;
    private JTextArea textArea1 = new JTextArea(5, 50);
    private JList<String> list1 = new JList<>();
    private MarkovModel markovModel = new MarkovModel();

    public Texter() {
        init();
        textArea1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    String text = textArea1.getText();
                    String[] lastTwo = getLastTwo(text);
                    List<String> strings1 = markovModel.nextWord(lastTwo);
                    System.out.println(Arrays.toString(lastTwo));
                    System.out.println(strings1);
                    String[] strings = strings1.toArray(new String[]{});
                    list1.setListData(strings);
                }
            }
        });
    }

    public String[] getLastTwo(String input){
        String[] sentences = input.split("\\.");
        String[] split = sentences[sentences.length-1].trim().split("\\s+");
        int length = split.length;
        if (length >= 2){
            return new String[]{split[length-2], split[length-1]};
        }
        return split;
    }

    @Override
    public void run() {
        jFrame.setVisible(true);
    }

    public void init() {
        jFrame = new JFrame();
        jFrame.setBounds(100, 100, 800, 500);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.getContentPane().setLayout(null);

        textArea1.setVisible(true);
        textArea1.setBounds(0, 0, 800, 200);
        jFrame.getContentPane().add(textArea1);

        list1.setBounds(0, 205, 800, 300);
        list1.setVisible(true);
        jFrame.getContentPane().add(list1);
    }
}
