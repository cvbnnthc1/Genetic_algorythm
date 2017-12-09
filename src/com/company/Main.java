package com.company;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws CloneNotSupportedException, IOException {
	// write your code here
       /* Rectangle[] rectangles = new Rectangle[5];*/
        FileInputStream fis = new FileInputStream("data.txt");
        Scanner scanner = new Scanner(fis);
       // System.out.println("Enter width of frame");
        int width = scanner.nextInt();
       // System.out.println("Enter height of frame");
        int height = scanner.nextInt();
        Rectangle frame = new Rectangle(0,0, width, height);
      //  System.out.println("Enter number of rectangles");
        int length = scanner.nextInt();
        Rectangle[] rectangles = new Rectangle[length];
        for(int i = 0; i < length; i++) {
         //   System.out.println("Enter width of current rectangle");
            width = scanner.nextInt();
         //   System.out.println("Enter height of current rectangle");
            height = scanner.nextInt();
            rectangles[i] = new Rectangle(0,0, width, height);
        }
        fis.close();
        System.out.println("Wait");

        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(frame, rectangles, 5000);
        geneticAlgorithm.Do();
    }
}
