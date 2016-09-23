package codecuack.a8puzzlesolver.BFS;

/**
 * Created by juancarlosroot on 9/19/16.
 */
public class Block{
    float m_fHeuristic;
    float m_fG_Precedence;
    float m_fFinalValue;
    int mArray[][];
    int x;
    int y;
    Block parent;
    int sArray[][] = {{1,2,3}, {8,0,4}, {7,6,5}};

    public Block(int [][] mArray)
    {
        this.parent = null;
        this.m_fHeuristic = 0.0f;
        this.m_fG_Precedence = 0.0f;
        setMArray(mArray);
        nullPosition();
    }

    public Block getParent() {
        return parent;
    }

    public void setParent(Block m_pParent) {
        this.parent = m_pParent;
    }

    public void setM_fHeuristic(float m_fHeuristic) {
        this.m_fHeuristic = m_fHeuristic;
    }

    public void setM_fG_Precedence(float m_fG_Precedence) {
        this.m_fG_Precedence = m_fG_Precedence;
    }

    public void setM_fFinalValue(float m_fFinalValue) {
        this.m_fFinalValue = m_fFinalValue;
    }

    public void setMArray(int [][] mArray) {
        this.mArray = new int [mArray.length][mArray.length];
        for (int x = 0; x < sArray.length; x++) {
            for (int y = 0; y < sArray.length; y++)
                this.mArray[x][y] = mArray[x][y];
        }
    }

    public int[][] getmArray() {
        return mArray;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void printM(){
        for (int x = 0; x < sArray.length; x++) {
            System.out.println(" ");
            for (int y = 0; y < sArray.length; y++)
                System.out.print(this.mArray[x][y]);
        }
        System.out.println(" ");
    }

    void nullPosition()
    {
        for (int x = 0; x < 3; x++)
            for (int y = 0; y < 3; y++)
                if (this.mArray[x][y] == 0)
                {
                    this.x = x;
                    this.y = y;
                }

    }
}
