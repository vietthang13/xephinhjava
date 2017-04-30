/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;
import java.util.Random;
 
public class Shape {
 
   //khởi tạo danh sách các khối gạch
    protected enum Tetrominoes { NoShape, ZShape, SShape, LineShape, 
               TShape, SquareShape, LShape, MirroredLShape };
 
    private Tetrominoes pieceShape;
    private int coords[][];//tọa độ khối gạch
    private int[][][] coordsTable; //mảng các tọa độ của các khối gạch
 
 
    public Shape() {
        //khởi tạo thông tin khối
        coords = new int[4][2];
        setShape(Tetrominoes.NoShape);//noshape: khối mặc định
    }
 
    public void setShape(Tetrominoes shape) {
       
         coordsTable = new int[][][] {
            { { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 } },
            { { 0,-1 },   { 0, 0 },   {-1, 0 },   {-1, 1 } },
            { { 0,-1 },   { 0, 0 },   { 1, 0 },   { 1, 1 } },
            { { 0,-1 },   { 0, 0 },   { 0, 1 },   { 0, 2 } },
            { {-1, 0 },   { 0, 0 },   { 1, 0 },   { 0, 1 } },
            { { 0, 0 },   { 1, 0 },   { 0, 1 },   { 1, 1 } },
            { {-1,-1 },   { 0,-1 },   { 0, 0 },   { 0, 1 } },
            { { 1,-1 },   { 0,-1 },   { 0, 0 },   { 0, 1 } }
        };
 
        for (int i = 0; i < 4 ; i++) {
             
            for (int j = 0; j < 2; ++j) {
                 
                coords[i][j] = coordsTable[shape.ordinal()][i][j];
            }
        }
         
        pieceShape = shape;
    }
    //thiết lập tọa độ x,y
    private void setX(int index, int x) { coords[index][0] = x; }
    private void setY(int index, int y) { coords[index][1] = y; }
    public int x(int index) { return coords[index][0]; }
    public int y(int index) { return coords[index][1]; }
    public Tetrominoes getShape()  { return pieceShape; }
 
    public void setRandomShape() {
         
        Random r = new Random();
        int x = Math.abs(r.nextInt()) % 7 + 1;
        Tetrominoes[] values = Tetrominoes.values(); 
        setShape(values[x]);
    }
 
    public int minX() {
         
      int m = coords[0][0];
       
      for (int i=0; i < 4; i++) {
           
          m = Math.min(m, coords[i][0]);
      }
       
      return m;
    }
 
 
    public int minY() {
         
      int m = coords[0][1];
       
      for (int i=0; i < 4; i++) {
           
          m = Math.min(m, coords[i][1]);
      }
       
      return m;
    }
 
    public Shape rotateLeft() {
         
        if (pieceShape == Tetrominoes.SquareShape)
            return this;
 
        Shape result = new Shape();
        result.pieceShape = pieceShape;
 
        for (int i = 0; i < 4; ++i) {
             
            result.setX(i, y(i));
            result.setY(i, -x(i));
        }
         
        return result;
    }
 
    public Shape rotateRight() {
         
        if (pieceShape == Tetrominoes.SquareShape)
            return this;
 
        Shape result = new Shape();
        result.pieceShape = pieceShape;
 
        for (int i = 0; i < 4; ++i) {
 
            result.setX(i, -y(i));
            result.setY(i, x(i));
        }
         
        return result;
    }
}