package com.company;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GeneticAlgorithm {
    GeneticAlgorithm(Rectangle frame, Rectangle[] rectangles, int number_of_iterations){
        frame_ = frame;
        rectangles_ = rectangles;
        number_of_iterations_ = number_of_iterations;
        population_ = new Citizen[50][100];
        Random rand = new Random(System.currentTimeMillis());
        for(int k = 0; k < population_.length; k++) {
            for (int i = 0; i < population_[k].length; i++) {
                Rectangle[] temp = new Rectangle[rectangles_.length];
                for (int j = 0; j < rectangles_.length; j++) {
                    temp[j] = new Rectangle(Math.abs(rand.nextInt() % (frame_.GetWidth())), Math.abs(rand.nextInt() % (frame_.GetHeight())), rectangles_[j].GetWidth(), rectangles_[j].GetHeight());
                }
                population_[k][i] = new Citizen(temp);
                population_[k][i].SetFitness(FitnessFunction(i, k));
            }
        }
    }
    public int FitnessFunction(int index, int number){
        int square = 0;
        boolean flag = false;                                                                                          //считается площадь вписанных прямоугольников, невписанные не считаются, если есть пересечения внутри контейнера - плохой случай, возвращаем 0
        ArrayList<Rectangle> temp_array = new ArrayList<>();
        for( int i = 0; i < population_[number][index].GetRectangles().length; i++){
            if(frame_.Cover(population_[number][index].GetRectangles()[i])){
                flag = true;
                square += population_[number][index].GetRectangles()[i].GetHeight()*population_[number][index].GetRectangles()[i].GetWidth();
                temp_array.add(population_[number][index].GetRectangles()[i]);
            }
        }
        if( !flag ) return 0;
        for( int i = 0; i < temp_array.size() - 1; i++){
            for( int j =  i + 1; j < temp_array.size(); j++){
                if( temp_array.get(i).Intersect(temp_array.get(j))) return 0;
            }
        }
        return square;
    }
    private void Body(int number) throws CloneNotSupportedException {                                                  //скрещивание и случайная мутация плохих особей
        Random rand = new Random(System.currentTimeMillis());
        for(int i = 0; i < 5; i++){
            Selection(number);
            for(int j = 0; j < 3*population_[number].length / 4; j++){
                if(Math.abs(rand.nextInt()%100) < 10) Mutation(j, number);
            }
        }

    }

    public void Do() throws CloneNotSupportedException {                                                             //основной цикл работы программы
        Random rand = new Random(System.currentTimeMillis());
        for(int j = 0; j < number_of_iterations_; j++) {
            GeneticThread[] threads = new GeneticThread[population_.length];
            for (int i = 0; i < population_.length; i++) {
                threads[i] = new GeneticThread(i);
                threads[i].start();
            }                                                                                                         //запускается эволюция на 5 итераций на независимых популяциях
            for(int i = 0; i < population_.length; i++){
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for(int i = 0; i < population_.length; i++){
                    int index = Math.abs(rand.nextInt()%(population_[(i + 1)%population_.length].length / 4)) + 3 * population_[(i + 1)%population_.length].length / 4;
                    population_[i%population_.length][0] = population_[(i + 1)%population_.length][index].clone();        //разные популяции обмениваются случайными хорошими особями, эти особи записываются вместо плохих
            }

        }

        Citizen[] temp = new Citizen[population_.length];                                                                     //находится более удачная особь из всех популяций и отрисовывается

        for(int i = 0; i < population_.length; i++){
            temp[i] = population_[i][population_[i].length - 1];
        }

        Arrays.sort(temp);

        System.out.println(temp[temp.length - 1].GetFitness());
        for(int i = 0; i < temp[temp.length - 1].GetRectangles().length; i++){
            if(frame_.Cover(temp[temp.length - 1].GetRectangles()[i])) System.out.println(temp[temp.length - 1].GetRectangles()[i]);
        }
        final_rects = temp[temp.length - 1].GetRectangles();
        System.out.println("Square");
        int square = 0;
        for(int i = 0; i < rectangles_.length; i++){
            square += rectangles_[i].GetHeight()*rectangles_[i].GetWidth();
        }
        System.out.println(square);
        Draw();
    }

    private void Selection(int number) throws CloneNotSupportedException {
        Arrays.sort(population_[number]);                                                                              //сортируются по значению fitness-function
        Random rand = new Random(System.currentTimeMillis());
            int index1 = Math.abs(rand.nextInt()%(rectangles_.length/4)) + (3*rectangles_.length/4 - 1);                //выбираются две хорошие особи из лучшей четверти, скрещиваются и записываются вместо самой плохой
            int index2 = Math.abs(rand.nextInt()%(rectangles_.length/4)) + (3*rectangles_.length/4 - 1);
            if(index1!=index2) {
                population_[number][0] = CrossOver(index1, index2, number);
                population_[number][0].SetFitness(FitnessFunction(0, number));
            }
            else {
                population_[number][0] = population_[number][index1].clone();
            }
        Arrays.sort(population_[number]);
    }

    private Citizen  CrossOver(int j, int k, int number){                                  //передаются прямоугольники с равной вероятностью от одного из родителей
        Rectangle[] temp = new Rectangle[rectangles_.length];
        Random rand = new Random(System.currentTimeMillis());
        for(int i = 0; i < rectangles_.length; i++){
            int tmp = Math.abs(rand.nextInt()%2);
            if(tmp == 0){
                temp[i] = population_[number][j].GetRectangles()[i];
            }
            else {
                temp[i] = population_[number][k].GetRectangles()[i];
            }
        }
        return new Citizen(temp);
         }

    private void Draw(){
        JFrame jf = new JFrame("Window");
        jf.setSize(frame_.GetWidth() + 100, frame_.GetHeight() + 100);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.add(new MyPanel());
    }


    public void Mutation(int index, int number){                                   //Случайно ихменяется высота или ширина у какого-то прямоугльника из набора
        Random rand = new Random(System.currentTimeMillis());
        if(Math.abs(rand.nextInt() % 2) == 0){
            population_[number][index].GetRectangles()[Math.abs(rand.nextInt() % rectangles_.length)].SetX(Math.abs(rand.nextInt()%(frame_.GetWidth())));
        }
        else population_[number][index].GetRectangles()[Math.abs(rand.nextInt() % rectangles_.length)].SetY(Math.abs(rand.nextInt()%(frame_.GetHeight())));
        population_[number][index].SetFitness(FitnessFunction(index, number));
    };
    private class GeneticThread extends Thread{
        private int number_;
        GeneticThread(int number){
            if(number < 0 || number >= population_.length) throw new ExceptionInInitializerError(); //Класс поток, который работает с определенной популяцией
            number_ = number;
        }

        @Override
        public void run() {
            try {
                Body(number_);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }

    private class MyPanel extends JPanel{
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            draw1(g);
        }
        private void draw1(Graphics g){
            Graphics2D g2d=(Graphics2D)g;
            g2d.setPaint(Color.red);
            g2d.drawRect(frame_.GetX(), frame_.GetY(), frame_.GetWidth(), frame_.GetHeight());
            g2d.setPaint(Color.blue);
            for(int i = 0; i < final_rects.length; i++){
                if(frame_.Cover(final_rects[i])){
                    g2d.drawRect(final_rects[i].GetX(), final_rects[i].GetY(), final_rects[i].GetWidth(), final_rects[i].GetHeight());
                }
            }

        }
    }

    private final Rectangle frame_;
    private final Rectangle[] rectangles_;
    private Citizen[][] population_;
    private int number_of_iterations_;
    private Rectangle[] final_rects;
}
