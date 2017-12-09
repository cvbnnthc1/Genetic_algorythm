package com.company;
/*
Вспомогательный класс, в котором хранится массив прямоугольников и значение его fitness-function
 */
public class Citizen implements Comparable<Citizen> {
    private Rectangle[] rectangles_;
    private int fitness_;
    public Citizen(Rectangle[] rectangles){
        rectangles_ = rectangles;
    }
    public void SetFitness(int fitness){
        fitness_ = fitness;
    }
    public Rectangle[] GetRectangles(){ return rectangles_; }
    public int GetFitness(){ return fitness_; };
    @Override
    public String toString() {
        return new String(String.valueOf(fitness_));
    }

    @Override
    public int compareTo(Citizen o) {
        if(fitness_ > o.GetFitness()) return 1;
        if(fitness_ < o.GetFitness()) return -1;
        return 0;
    }

    public Citizen clone() throws CloneNotSupportedException {
        Rectangle[] clone = new Rectangle[rectangles_.length];
        for ( int i = 0; i < rectangles_.length; i++){
            clone[i] = new Rectangle(rectangles_[i].GetX(), rectangles_[i].GetY(), rectangles_[i].GetWidth(), rectangles_[i].GetHeight());
        }
        Citizen res = new Citizen(clone);
        res.SetFitness(fitness_);
        return res;
    }
}
