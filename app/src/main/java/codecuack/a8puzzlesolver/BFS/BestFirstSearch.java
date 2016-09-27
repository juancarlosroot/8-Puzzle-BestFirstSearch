package codecuack.a8puzzlesolver.BFS;

import android.util.Log;

import java.util.ArrayList;
import codecuack.a8puzzlesolver.BFS.Block;

/**
 * Created by juancarlosroot on 9/12/16.
 */
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.ArrayList;

public class BestFirstSearch
{
    public float in_fHeuristicModifier;
    String TAG = getClass().getName().toString();

    int sArray[][] = {{1,2,3}, {8,0,4}, {7,6,5}};
    int xArray[] = new int[sArray.length*sArray.length];
    int yArray[] = new int[sArray.length*sArray.length];
    ArrayList< Block > m_ClosedList;
    ArrayList< Block > m_PathList;
    PriorityQueue<Block> blockQueue;
    HashSet<Block> mHashSetAll;


    ArrayList< Block > board;
    boolean isFinish = false;
    Block mActualBlock;
    int m_iNdimension;
    float m_fG_PrecedenceModifier;
    boolean m_bIsManhattan;
    long startTime;
    public BestFirstSearch(int in_iDimensions, boolean in_bIsManhattan, float in_fHeuristicModifier, float in_fG_precedenceModifier, Integer [][] mArray){

        this.board = new ArrayList<Block>();
        this.m_PathList = new ArrayList<Block>();
        this.m_ClosedList = new ArrayList<Block>();
        this.blockQueue = new PriorityQueue<Block>(10, new Comparator<Block>() {
            @Override
            public int compare(Block lhs, Block rhs) {
                if (lhs.m_fFinalValue < rhs.m_fFinalValue) {
                    return -1;
                }
                if (lhs.m_fFinalValue > rhs.m_fFinalValue) {
                    return 1;
                }
                return 0;
            }
        });
        this.mHashSetAll = new HashSet<Block>();

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
        this.blockQueue.add(mActualBlock);

        while( this.blockQueue.size() > 0 && !bIsFound )
        {
            this.mActualBlock = blockQueue.poll();

            this.m_ClosedList.add(this.mActualBlock);

            int iActualX = this.mActualBlock.x;
            int iActualY = this.mActualBlock.y;

            int iPastX = (this.mActualBlock.parent != null?this.mActualBlock.parent.x:this.mActualBlock.x);
            int iPastY = (this.mActualBlock.parent != null?this.mActualBlock.parent.y:this.mActualBlock.y);

            Integer [][]iActualM = this.mActualBlock.mArray;

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
        while(back != null&& back.m_fG_Precedence != 0)
        {
            this.m_PathList.add(back);
            back =  back.parent;
            c++;
        }

    }

    private boolean AnalyzeNode(Block in_pActualNode, Block in_pNodeToCheck)
    {
        if(this.mHashSetAll.add(in_pNodeToCheck))
        {
            //Block temp = new Block(in_pActualNode.mArray);;
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

            //InsertByValue(in_pNodeToCheck, this.m_OpenList);
            this.blockQueue.add(in_pNodeToCheck);

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
        Integer [][] nArray = pActualNode.mArray;

        int m_iTempSwap = nArray[pActualNode.x + iActualX][pActualNode.y + iActualY];

        nArray[pActualNode.x + iActualX][pActualNode.y + iActualY] =
                nArray[pActualNode.x][pActualNode.y];

        nArray[pActualNode.x][pActualNode.y] =
                m_iTempSwap;

        pActualNode.setX(pActualNode.x + iActualX);
        pActualNode.setY(pActualNode.y + iActualY);

        pActualNode.setMArray(nArray);
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

    public PriorityQueue<Block> getBlockQueue() {
        return blockQueue;
    }
}
