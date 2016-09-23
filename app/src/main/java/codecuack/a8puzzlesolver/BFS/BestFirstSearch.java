package codecuack.a8puzzlesolver.BFS;

import android.util.Log;

import java.util.ArrayList;
import codecuack.a8puzzlesolver.BFS.Block;

/**
 * Created by juancarlosroot on 9/12/16.
 */
import java.util.Queue;
import java.util.ArrayList;

public class BestFirstSearch
{
    public float in_fHeuristicModifier;
    String TAG = getClass().getName().toString();
    int mArray[][] =  {{8,3,5}, {4,1,6}, {2,7,0}};
    int sArray[][] = {{1,2,3}, {8,0,4}, {7,6,5}};
    int xArray[] = new int[sArray.length*sArray.length];
    int yArray[] = new int[sArray.length*sArray.length];
    ArrayList< Block > m_ClosedList;
    ArrayList< Block > m_OpenList;
    ArrayList< Block > m_PathList;
    ArrayList< Block > board;
    boolean isFinish = false;
    Block mActualBlock;
    int m_iNdimension;
    float m_fG_PrecedenceModifier;
    boolean m_bIsManhattan;
    long startTime;
    public BestFirstSearch(int in_iDimensions, boolean in_bIsManhattan, float in_fHeuristicModifier, float in_fG_precedenceModifier, int [][] mArray){
        this.m_OpenList = new ArrayList<Block>();
        this.m_ClosedList = new ArrayList<Block>();
        this.board = new ArrayList<Block>();
        this.m_PathList = new ArrayList<Block>();

        this.m_iNdimension = in_iDimensions;
        this.m_bIsManhattan = in_bIsManhattan;
        this.in_fHeuristicModifier = in_fHeuristicModifier;
        this.m_fG_PrecedenceModifier = in_fG_precedenceModifier;

        this.mActualBlock = new Block(mArray);
        createInitPositions();
    }

    public ArrayList<Block> getM_PathList() {
        return m_PathList;
    }

    public void setM_PathList(ArrayList<Block> m_PathList) {
        this.m_PathList = m_PathList;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }
    public long getTimeElapsed(){
        return System.currentTimeMillis() - startTime;
    }

    public boolean BestFS()
    {
        this.startTime = System.currentTimeMillis();
        boolean bIsFound = false;

        if(difference(mActualBlock) == 0)
        {
            bIsFound = true;
        }




        int c = 0;
        this.m_OpenList.add(mActualBlock);


        while( this.m_OpenList.size() > 0 && !bIsFound )
        {
            this.mActualBlock = this.m_OpenList.get(0);
            this.m_OpenList.remove(0);

            this.m_ClosedList.add(this.mActualBlock);

            int iActualX = this.mActualBlock.x;
            int iActualY = this.mActualBlock.y;

            int iPastX = (this.mActualBlock.parent != null?this.mActualBlock.parent.x:this.mActualBlock.x);
            int iPastY = (this.mActualBlock.parent != null?this.mActualBlock.parent.y:this.mActualBlock.y);

            int [][]iActualM = this.mActualBlock.mArray;

            Block TempNode =
                    new Block(iActualM);
            if( iActualX-1 > -1 && iPastX != iActualX-1 )
            {
                swapPositions(TempNode, -1, 0);
                if( AnalyzeNode( mActualBlock, TempNode) )
                {
                    bIsFound = true;
                    break;
                }
            }

            TempNode =
                    new Block(iActualM);
            if( iActualX+1 < m_iNdimension && iPastX != iActualX+1 ) //n is the size of the array.
            {
                swapPositions(TempNode, 1, 0);
                if( AnalyzeNode( mActualBlock, TempNode ) ) //Call the function to check if it is the final node, or to check //if it can have a better value on the open list.
                {
                    bIsFound = true;
                    break;
                }
            }

            TempNode =
                    new Block(iActualM);
            if( iActualY-1 > -1 && iPastY != iActualY-1)
            {
                swapPositions(TempNode, 0, -1);
                if( AnalyzeNode( mActualBlock, TempNode ) ) //Call the function to check if it is the final node, or to check //if it can have a better value on the open list.
                {
                    bIsFound = true;
                    break;
                }
                //TempNode.setMArray(swapPositions(TempNode, 0, 1));
            }

            TempNode =
                    new Block(iActualM);
            //Lower limit check
            if( iActualY+1 < m_iNdimension && iPastY != iActualY+1)
            {
                swapPositions(TempNode, 0, 1);
                if( AnalyzeNode( mActualBlock, TempNode ) ) //Call the function to check if it is the final node, or to check //if it can have a better value on the open list.
                {
                    bIsFound = true;
                    break;
                }
            }
            c++;
        }
        if( bIsFound == true )
        {
            MakeBacktrack();//Get the final route, stored inside the class's Backtrack ArrayList.
            setFinish(true);
            return true;
        }

        setFinish(bIsFound);
        return bIsFound;

    }

    private void MakeBacktrack()
    {
        Block back = this.mActualBlock.parent;
        int c = 0;
        System.out.println(this.mActualBlock.m_fG_Precedence);

        this.m_PathList.add(this.mActualBlock);
        while(back.m_fG_Precedence != 0)
        {
            this.m_PathList.add(back);
            back =  back.parent;
            c++;
        }

    }

    private boolean AnalyzeNode(Block in_pActualNode, Block in_pNodeToCheck)
    {
        if( ! ( this.m_ClosedList.contains(in_pNodeToCheck) || this.m_OpenList.contains(in_pNodeToCheck)) )
        {
            Block temp = new Block(in_pActualNode.mArray);;
            //in_pActualNode.printM();

            in_pNodeToCheck.setParent( in_pActualNode);

            in_pNodeToCheck.setM_fG_Precedence(in_pActualNode.m_fG_Precedence + m_fG_PrecedenceModifier);

            in_pNodeToCheck.setM_fHeuristic(difference(in_pNodeToCheck));

            if( in_pNodeToCheck.m_fHeuristic == 0 )
            {
                this.mActualBlock = in_pNodeToCheck;
                return true;
            }

            in_pNodeToCheck.setM_fFinalValue(in_pNodeToCheck.m_fHeuristic + in_pNodeToCheck.m_fG_Precedence);
            //The sum of both H and G.

            InsertByValue(in_pNodeToCheck, this.m_OpenList);

            return false;
        }

        return false;
    }
    void InsertByValue( Block in_NodeToInsert, ArrayList< Block >  in_DestinationList )
    {
        for( int i = 0 ; i < in_DestinationList.size(); i++ )
        {
            //mPrint("aaaa " + i);
            if(in_DestinationList.get(i).m_fFinalValue > in_NodeToInsert.m_fFinalValue ) //Check the Final f(u) value, to see where to put it.
            {
                //Insert it BEFORE the DestinationList[i] element.
                in_DestinationList.add(i, in_NodeToInsert);
                return;//Return to exit the function.
            }
        }
        //In case it was the highest cost, place it on the back of the list.
        in_DestinationList.add(in_NodeToInsert);
    }

    boolean CustomContains(Block in_NodeToInsert, ArrayList< Block >  in_DestinationList)
    {
        int c = 0;
        for( int i = 0 ; i < in_DestinationList.size(); i++ )
        {
            c = 0;
            for(int x = 0 ; x < m_iNdimension ; x++){
                for(int y = 0 ; y < m_iNdimension ; y++){
                    if(in_DestinationList.get(i).mArray[x][y] == in_NodeToInsert.mArray[x][y] )
                    {
                        c++;
                    }
                }
                if(c == 9)
                    return true;
            }

        }
        return false;
    }

    private int difference(Block in_pNodeToCheck){
        int diff_c = 0;
        for (int x = 0; x < sArray.length; x++) {
            for (int y = 0; y < sArray.length; y++) {
                if (this.m_bIsManhattan) {
                    if (in_pNodeToCheck.mArray[x][y] != sArray[x][y])
                        diff_c += (Math.abs(xArray[in_pNodeToCheck.mArray[x][y]] - x) + Math.abs(yArray[in_pNodeToCheck.mArray[x][y]] - y)) * in_fHeuristicModifier;
                }
                else {
                    diff_c += (in_pNodeToCheck.mArray[x][y] != sArray[x][y] ? 1 : 0);
                }
            }
        }
        return diff_c;
    }

    private void swapPositions(Block pActualNode, int iActualX, int iActualY) {
        int [][] nArray = pActualNode.mArray;

        int m_iTempSwap = nArray[pActualNode.x + iActualX][pActualNode.y + iActualY];

        nArray[pActualNode.x + iActualX][pActualNode.y + iActualY] =
                nArray[pActualNode.x][pActualNode.y];

        nArray[pActualNode.x][pActualNode.y] =
                m_iTempSwap;

        pActualNode.setX(pActualNode.x + iActualX);
        pActualNode.setY(pActualNode.y + iActualY);

        pActualNode.setMArray(nArray);
    }

    private void mPrint(String text)
    {
        System.out.println(text);
    }

    private void createInitPositions()
    {
        int c = 0;
        for (int x = 0; x < sArray.length; x++)
            for (int y = 0; y < sArray.length; y++)
            {
                this.xArray[this.sArray[x][y]] = x;
                this.yArray[this.sArray[x][y]] = y;
            }
    }

    public ArrayList<Block> getM_ClosedList() {
        return m_ClosedList;
    }

    public ArrayList<Block> getM_OpenList() {
        return m_OpenList;
    }
}
