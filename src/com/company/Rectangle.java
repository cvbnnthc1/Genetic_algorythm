package com.company;

public class Rectangle  {
    private int x_;
    private int y_;
    private int height_;
    private int width_;
    Rectangle(int x, int y, int width, int height){
        if(x < 0 || y < 0 || height <= 0 || width <= 0) throw new ExceptionInInitializerError();
        else{
            x_ = x;
            y_ = y;
            height_ = height;
            width_ = width;
        }
    }
    public int GetX(){ return x_; }
    public int GetY(){ return y_; }
    public void SetX(int x){ x_ = x; }
    public void SetY(int y){ y_ = y; }
    public int GetHeight(){ return height_; }
    public int GetWidth(){ return width_; }
    public boolean Cover(Rectangle that){
        return (x_ <= that.GetX() && y_ <= that.GetY() && x_ + width_ >= that.GetX() + that.GetWidth() && y_ + height_ >= that.GetY() + that.GetHeight());
    }
    public boolean Intersect(Rectangle that){
        return !(x_ >= that.GetX() + that.GetWidth() || x_ + width_ <= that.GetX() || y_ >= that.GetY() + that.GetHeight() || y_ + height_ <= that.GetY());
    }

    @Override
    public String toString() {
        return new String("x " + String.valueOf(x_) + " y " + String.valueOf(y_) + " width " + String.valueOf(width_) + " height " + String.valueOf(height_));

    }
}
