package codecuack.a8puzzlesolver;

import android.os.AsyncTask;
import android.support.annotation.IntegerRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import codecuack.a8puzzlesolver.BFS.BestFirstSearch;
import codecuack.a8puzzlesolver.BFS.Block;

public class MainActivity extends AppCompatActivity {
    TextView textView00, textView01, textView02, textView10, textView11, textView12, textView20, textView21, textView22;
    TextView textViewP, textViewQ, textViewMoves;
    Button resetButton;
    Button playButton;
    Button nextButton;
    Button prevButton;

    Integer mArray[][];
    ArrayList<Block> m_PathList;
    int lastMove;
    String TAG = getClass().getName().toString();
    RelativeLayout mRelative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRelative = (RelativeLayout) findViewById(R.id.relativeLayout);



        resetButton = (Button) findViewById(R.id.button_reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                resetTextViews();
                resetMatrixUI();
                nextButton.setVisibility(View.INVISIBLE);
                prevButton.setVisibility(View.INVISIBLE);
                playButton.setVisibility(View.VISIBLE);
                showAlert("Values erased");
            }
        });

        playButton = (Button) findViewById(R.id.button_start);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mArray = getMatrixUI();

                if (mArray == null)
                {
                    showAlert("Invalid input");
                    resetButton.setEnabled(true);
                }
                else
                {
                    showAlert("Calculating");
                    new BestFSTask().execute(mArray);
                    playButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        nextButton = (Button) findViewById(R.id.button_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lastMove >= 0 && lastMove < m_PathList.size()) {
                    lastMove--;
                }

                printArrayUI();

            }
        });

        prevButton = (Button) findViewById(R.id.button_prev);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lastMove >= 0 && lastMove < m_PathList.size()) {
                    lastMove++;
                }

                printArrayUI();

            }
        });

        initTextViews();
    }

    void printArrayUI()
    {
        if(lastMove >= 0 && lastMove < m_PathList.size()) {
            TextView textViewArray[] = {textView00, textView01, textView02, textView10, textView11, textView12, textView20, textView21, textView22};
            int tvC = 0;

            for (int i = 0; i < m_PathList.get(lastMove).getmArray().length; i++) {
                for (int c = 0; c < m_PathList.get(lastMove).getmArray().length; c++) {
                    textViewArray[tvC].setText(
                            Integer.toString(m_PathList.get(lastMove).getmArray()[i][c])
                    );
                    tvC++;
                }

            }
            textViewMoves.setText(Integer.toString(lastMove));
            prevButton.setVisibility(View.VISIBLE);
        }
        else
        {
            Log.i(TAG, " " + lastMove);
            if (lastMove < 0)
                lastMove =0;
            else if(lastMove >= m_PathList.size())
                lastMove = m_PathList.size()-1;
        }

    }
    void resetMatrixUI()
    {
        TextView textViewArray[] = {textView00, textView01, textView02, textView10, textView11, textView12, textView20, textView21, textView22};
        textViewP.setText("");
        textViewP.setText("");
        for (int i = 0; i < textViewArray.length; i++) {
            textViewArray[i].setText("");

        }
    }

    Integer[][] getMatrixUI(){
        Integer newArray[][] = new Integer[3][3];
        TextView textViewArray[] = {textView00, textView01, textView02, textView10, textView11, textView12, textView20, textView21, textView22};
        int []cs = new int[textViewArray.length];

        int tcv = 0;
        for (int i = 0; i< textViewArray.length;i++){
            cs[i] = 0;
            if (textViewArray[i].length() == 0 || textViewArray[i].length() > 1 )
                return null;
        }

        for (int i = 0; i< textViewArray.length;i++)
        {
            if ( 0 <= Integer.parseInt(textViewArray[i].getText().toString())
                    && Integer.parseInt(textViewArray[i].getText().toString()) < 9)
                cs[Integer.parseInt(textViewArray[i].getText().toString())]+=1;
            else
                return null;
        }

        for (int i = 0; i< textViewArray.length;i++)
        {
            if(cs[i] == 0)
                return null;
        }

        for (int i = 0; i < newArray.length; i++){
            for (int c = 0; c < newArray.length; c++){
                newArray[i][c] =
                        Integer.parseInt(
                                textViewArray[tcv].getText().toString().length() >= 1 ? textViewArray[tcv].getText().toString() : "0");
                tcv++;
            }

        }

        return newArray;
    }

    void initTextViews(){
        textView00 = (TextView) findViewById(R.id.editText00);
        textView01 = (TextView) findViewById(R.id.editText01);
        textView02 = (TextView) findViewById(R.id.editText02);
        textView10 = (TextView) findViewById(R.id.editText10);
        textView11 = (TextView) findViewById(R.id.editText11);
        textView12 = (TextView) findViewById(R.id.editText12);
        textView20 = (TextView) findViewById(R.id.editText20);
        textView21 = (TextView) findViewById(R.id.editText21);
        textView22 = (TextView) findViewById(R.id.editText22);
        textViewP  = (TextView) findViewById(R.id.textViewP);
        textViewQ  = (TextView) findViewById(R.id.textViewQ);
        textViewMoves  = (TextView) findViewById(R.id.textViewMoves);
        nextButton.setVisibility(View.INVISIBLE);
        prevButton.setVisibility(View.INVISIBLE);
        playButton.setVisibility(View.VISIBLE);
        resetButton.setEnabled(true);
    }

    void resetTextViews(){
        TextView textViewArray[] = {textView00, textView01, textView02, textView10, textView11, textView12, textView20, textView21, textView22};
        int tvc = 1;
        for (int i = 0; i < textViewArray.length; i++){
            textViewArray[i].setText("");
            if(i+1 != 5 )
                textViewArray[i].setText(Integer.toString(tvc++));
        }

        for (TextView tv:textViewArray){
            Random rand = new Random();
            int a = rand.nextInt((8 - 0) + 1) + 0;
            int b = rand.nextInt((8 - 0) + 1) + 0;
            while(a != 4 && b != 4){
                a = rand.nextInt((8 - 0) + 1) + 0;
                b = rand.nextInt((8 - 0) + 1) + 0;
            }

            String c = textViewArray[b].getText().toString();
            textViewArray[b].setText(textViewArray[a].getText().toString());
            textViewArray[a].setText(c);
            tv.setEnabled(false);
        }
    }


    private class BestFSTask extends AsyncTask<Integer [][], Integer, BestFirstSearch> {
        @Override
        protected BestFirstSearch doInBackground(Integer [][] ... params) {
            BestFirstSearch bfs = new BestFirstSearch(3, false, 1, 1, params[0]);
            bfs.BestFS();

            long mTStart = System.currentTimeMillis();

            while (!bfs.isFinish())
            {
                publishProgress((int) (System.currentTimeMillis() - (System.currentTimeMillis()) / 1000));
            }

            Log.e(TAG, "Tstart : " + mTStart);
            Log.i(TAG, "TEnd   : " + mTStart);
            return bfs;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(BestFirstSearch bfs) {
            super.onPostExecute(bfs);
            m_PathList = bfs.getM_PathList();
            lastMove = m_PathList.size() - 1;
            nextButton.setVisibility(View.VISIBLE);
            textViewMoves.setText(Integer.toString(lastMove));
            textViewQ.setText(Integer.toString(bfs.getM_ClosedList().size()));
            textViewP.setText(Integer.toString(bfs.getBlockQueue().size()));
            showAlert("Done");
        }
    }

    private void showAlert(String mText)
    {
        Snackbar.make(mRelative, mText, Snackbar.LENGTH_LONG).show();
    }

}
