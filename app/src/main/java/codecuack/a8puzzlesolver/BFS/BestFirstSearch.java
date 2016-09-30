package codecuack.a8puzzlesolver.BFS;

import java.util.ArrayList;

/**
 * Created by juancarlosroot on 9/12/16.
 */
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

public class BestFirstSearch
{
    public float in_fHeuristicModifier;
    String TAG = getClass().getName().toString();

    int i_FinalArray[][] = {{1,2,3}, {8,0,4}, {7,6,5}};
    int i_XArray[] = new int[i_FinalArray.length* i_FinalArray.length];
    int i_YArray[] = new int[i_FinalArray.length* i_FinalArray.length];
    ArrayList< Block > m_ClosedList;
    ArrayList< Block > m_PathList;
    PriorityQueue<Block> m_OpenList;
    HashSet<Block> mHashSetAll;


    ArrayList< Block > board;
    boolean m_bFinish = false;
    Block m_ActualBlock;
    int m_iNdimension;
    float m_fG_PrecedenceModifier;
    boolean m_bIsManhattan;
    long l_StartTime;
    public BestFirstSearch(int in_iDimensions, boolean in_bIsManhattan, float in_fHeuristicModifier, float in_fG_precedenceModifier, Integer [][] mArray){

        this.board = new ArrayList<Block>();
        this.m_PathList = new ArrayList<Block>();
        this.m_ClosedList = new ArrayList<Block>();
        this.m_OpenList = new PriorityQueue<Block>(10, new Comparator<Block>() {
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

        this.m_ActualBlock = new Block(mArray);
        createInitPositions();
    }

    public ArrayList<Block> getM_PathList() {
        return m_PathList;
    }

    public boolean isM_bFinish() {
        return m_bFinish;
    }

    private void setM_bFinish(boolean m_bFinish) {
        this.m_bFinish = m_bFinish;
    }
    public long getTimeElapsed(){
        return System.currentTimeMillis() - l_StartTime;
    }

    public boolean BestFS()
    {
        this.l_StartTime = System.currentTimeMillis();
        boolean bIsFound = false;

        if(difference(m_ActualBlock) == 0)
        {
            bIsFound = true;
        }

        int c = 0;
        this.m_OpenList.add(m_ActualBlock);

        while( this.m_OpenList.size() > 0 && !bIsFound )
        {
            this.m_ActualBlock = m_OpenList.poll();

            //this.m_ClosedList.add(this.m_ActualBlock);

            int iActualX = this.m_ActualBlock.x;
            int iActualY = this.m_ActualBlock.y;

            int iPastX = (this.m_ActualBlock.parent != null?this.m_ActualBlock.parent.x:this.m_ActualBlock.x);
            int iPastY = (this.m_ActualBlock.parent != null?this.m_ActualBlock.parent.y:this.m_ActualBlock.y);

            Integer [][]iActualM = this.m_ActualBlock.mArray;

            Block TempNode =
                    new Block(iActualM);
            if( iActualX-1 > -1 && iPastX != iActualX-1 )
            {
                swapPositions(TempNode, -1, 0);
                if( AnalyzeNode(m_ActualBlock, TempNode) )
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
                if( AnalyzeNode(m_ActualBlock, TempNode ) ) //Call the function to check if it is the final node, or to check //if it can have a better value on the open list.
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
                if( AnalyzeNode(m_ActualBlock, TempNode ) ) //Call the function to check if it is the final node, or to check //if it can have a better value on the open list.
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
                if( AnalyzeNode(m_ActualBlock, TempNode ) ) //Call the function to check if it is the final node, or to check //if it can have a better value on the open list.
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
            setM_bFinish(true);
            return true;
        }

        setM_bFinish(bIsFound);
        return bIsFound;

    }

    private void MakeBacktrack()
    {
        Block back = this.m_ActualBlock.parent;
        int c = 0;
        System.out.println(this.m_ActualBlock.m_fG_Precedence);

        this.m_PathList.add(this.m_ActualBlock);
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
                this.m_ActualBlock = in_pNodeToCheck;
                return true;
            }

            in_pNodeToCheck.setM_fFinalValue(in_pNodeToCheck.m_fHeuristic + in_pNodeToCheck.m_fG_Precedence);
            //The sum of both H and G.

            //InsertByValue(in_pNodeToCheck, this.m_OpenList);
            this.m_OpenList.add(in_pNodeToCheck);

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
        for (int x = 0; x < i_FinalArray.length; x++) {
            for (int y = 0; y < i_FinalArray.length; y++) {
                if (this.m_bIsManhattan) {
                    if (in_pNodeToCheck.mArray[x][y] != i_FinalArray[x][y])
                        diff_c += (Math.abs(i_XArray[in_pNodeToCheck.mArray[x][y]] - x) + Math.abs(i_YArray[in_pNodeToCheck.mArray[x][y]] - y)) * in_fHeuristicModifier;
                }
                else {
                    diff_c += (in_pNodeToCheck.mArray[x][y] != i_FinalArray[x][y] ? 1 : 0);
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
        for (int x = 0; x < i_FinalArray.length; x++)
            for (int y = 0; y < i_FinalArray.length; y++)
            {
                this.i_XArray[this.i_FinalArray[x][y]] = x;
                this.i_YArray[this.i_FinalArray[x][y]] = y;
            }
    }

    public PriorityQueue<Block> getM_OpenList() {
        return m_OpenList;
    }

    public HashSet<Block> getmHashSetAll() {
        return mHashSetAll;
    }
}
