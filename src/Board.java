import javax.swing.JOptionPane;
import java.applet.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
public class Board extends Applet
        implements
        java.awt.event.MouseListener, java.awt.event.MouseMotionListener,
        java.lang.Runnable {
    AudioClip s;
    int[] board = new int[120];
    int[] graphboard = new int[120];
    Color brown = new Color(0x8B2500);
    Color white = new Color(0xFFFFCC);
    Color red = new Color(0xCC0000);
    Color green = new Color(0x009900);
    Color blue = new Color(0x000099);
    Image[] pieces = new Image[18];
    Applet parent;
    int code = 0, start = 21, alt = 21, end = 21, x = 0, y = 0;
    int[] movelist = new int[250];
    int movecounter = 0;
    int color = 1;
    Thread th = null;
    int deep = 0;
    int target = 4;
    float value = 0;
    float minimax[] = new float[10];
    float alphabeta[] = new float[10];
    boolean ababort = false;
    int move;
    float[] posvalues =
            {0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f,
                    0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f,
                    0.00f, 0.00f, 0.01f, 0.02f, 0.03f, 0.03f, 0.02f, 0.01f, 0.00f, 0.00f,
                    0.00f, 0.01f, 0.04f, 0.04f, 0.04f, 0.04f, 0.04f, 0.04f, 0.01f, 0.00f,
                    0.00f, 0.03f, 0.04f, 0.06f, 0.06f, 0.06f, 0.06f, 0.04f, 0.02f, 0.00f,
                    0.00f, 0.03f, 0.04f, 0.06f, 0.08f, 0.08f, 0.06f, 0.04f, 0.03f, 0.00f,
                    0.00f, 0.03f, 0.04f, 0.06f, 0.08f, 0.08f, 0.06f, 0.04f, 0.03f, 0.00f,
                    0.00f, 0.02f, 0.04f, 0.06f, 0.06f, 0.06f, 0.06f, 0.04f, 0.02f, 0.00f,
                    0.00f, 0.01f, 0.04f, 0.04f, 0.04f, 0.04f, 0.04f, 0.04f, 0.01f, 0.00f,
                    0.00f, 0.00f, 0.01f, 0.02f, 0.03f, 0.03f, 0.02f, 0.01f, 0.00f, 0.00f,
                    0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f,
                    0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f};

    public Board(java.applet.Applet ref) {
        super();
        alphabeta[0] = -3000.0f;
        newgame();
        MediaTracker controler = new MediaTracker(ref);
        pieces[1] = ref.getImage(ref.getCodeBase(), "wp.png");
        pieces[2] = ref.getImage(ref.getCodeBase(), "wn.png");
        pieces[3] = ref.getImage(ref.getCodeBase(), "wb.png");
        pieces[4] = ref.getImage(ref.getCodeBase(), "wr.png");
        pieces[5] = ref.getImage(ref.getCodeBase(), "wq.png");
        pieces[6] = ref.getImage(ref.getCodeBase(), "wk.png");
        pieces[11] = ref.getImage(ref.getCodeBase(), "bp.png");
        pieces[12] = ref.getImage(ref.getCodeBase(), "bn.png");
        pieces[13] = ref.getImage(ref.getCodeBase(), "bb.png");
        pieces[14] = ref.getImage(ref.getCodeBase(), "br.png");
        pieces[15] = ref.getImage(ref.getCodeBase(), "bq.png");
        pieces[16] = ref.getImage(ref.getCodeBase(), "bk.png");
        for (int i = 1; i < 7; i++) {
            controler.addImage(pieces[i], 0);
            controler.addImage(pieces[i + 10], 0);
        }
        try {
            controler.waitForAll();
        } catch (InterruptedException e) {
            System.out.println("Images not successfull loaded - Trying again ...");
            controler.checkID(0, true);
        }
        parent = ref;
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public float evaluation ( )
    {float value = 0;
        float figur = 0;
        for (int i = 21; i < 99; i++)
        {if ( board [i] != 0 )
        {switch (board [i] % 10)
        {case 1:figur = 1.0f;break;
            case 2:
            case 3:
                figur = 3.0f;
                break;
            case 4:figur = 4.5f;break;
            case 5:figur = 9.0f;break;
            case 6:figur = 0.0f;break;}
            figur += posvalues [i];
            if(board [i] % 100  / 10 == color)value += figur;
            else value -= figur;}
            if(i%10 == 8)i += 2;}
        return value;	}
    public void execute (int start, int end)
    {board [end] = board [start];
        board [start] = 0;
        if (board [end] % 10 == 6)
        {if(end == start + 2)
        {board [start + 1] = board [start + 3] % 100;
            board [start + 3] = 0;
            graphboard [start + 1] = board [start + 1];
            graphboard [start + 3] = 0;
            paintField (start + 3);
            paintField (start + 1);}
            if( end == start - 2)
            {board [start - 1] = board [start - 4] % 100;
                board [start - 4] = 0;
                graphboard [start - 1] = board [start - 1];
                graphboard [start - 4] = 0;
                paintField (start - 4);
                paintField (start - 1);}}
        if ( (board [end] % 10 == 1) && ((end < 29) || (end > 90)) )
            board [end] += 4;;
        graphboard [start] = board [start];
        graphboard [end] = board [end];
        paintField (end);
        paintField (start);
        if (color == 1)
        {color = 2;
            th = new Thread (this);
            th.setPriority (10);
            th.start ();}
        else
        {color = 1;
            movecounter = 0;
            deep = 0;
            target = 1;
            genmove ();
            if (movecounter == 0)
            {if (ischeck ())
            {infoBox("Chessmaster Wins!","Game Status");
                destroy();}
            else
            {infoBox("Game is a draw!","Game Status");
                destroy();}}}}

    public void genmove ()
    {deep++;ababort = false;
        if (deep % 2 != 0)
        {minimax [deep] = 2000.0f;
            alphabeta [deep] = 3000.0f;}
        else
        {minimax [deep] = -2000.0f;
            alphabeta [deep] = -3000.0f;}
        for (int i = 21; i < 99; i++)
        {if (board [i] % 100 / 10 == color)
            switch (board [i] % 10)
            {
                case 1:
                    if (color == 1)
                    {if (board [i-10] == 0)simulate ( i, i-10);
                        if (board [i- 9] % 100 / 10 == 2)simulate ( i, i-9 );
                        if (board [i-11] % 100 / 10 == 2)simulate ( i, i-11);
                        if ( (i>80) && ( ( board [i-10] == 0) && (board [i-20] == 0))) simulate ( i, i-20);}
                    else
                    {if (board [i+10] == 0)simulate ( i, i+10);
                        if (board [i+9] % 100 / 10 == 1)simulate (i, i+9);
                        if (board [i+11] % 100 / 10 == 1)simulate (i, i+11);
                        if ( (i<39) && ( (board [i+10] == 0) && (board [i+20] == 0)))simulate (i, i+20);}
                    break;
                case 2:
                    simulate (i, i+12);
                    simulate (i, i-12);
                    simulate (i, i+21);
                    simulate (i, i-21);
                    simulate (i, i+19);
                    simulate (i, i-19);
                    simulate (i, i+8 );
                    simulate (i, i-8 );
                    break;
                case 5:
                case 3:
                    multisimulate ( i,  -9);
                    multisimulate ( i, -11);
                    multisimulate ( i,  +9);
                    multisimulate ( i, +11);
                    if (board [i] % 10 == 3)break;
                case 4:
                    multisimulate (i, -10);
                    multisimulate (i, +10);
                    multisimulate (i,  -1);
                    multisimulate (i,  +1);
                    break;
                case 6:
                    if ((board [i] / 100 == 1) && (! ischeck ()))
                    {if (((board [i+1] == 0) && (board [i+2] == 0)) && (board [i+3] / 100 == 1))
                    {board [i+1] = board [i] % 100;
                        board [i] = 0;
                        if (! ischeck ())
                        {board [i] = board [i+1];
                            board [i + 1] = board [i + 3] % 100;
                            board [i + 3] = 0;
                            simulate (i, i+2);
                            board [i + 3] = board [i + 1] + 100;
                            board [i+1] = board [i];}
                        board [i] = board [i + 1] + 100;
                        board [i + 1] = 0;}
                        if (((board [i-1] == 0) && (board [i-2] == 0)) && ((board [i-3] == 0) && (board [i-4] / 100 == 1)))
                        {board [i-1] = board [i] % 100;
                            board [i] = 0;
                            if (! ischeck ())
                            {board [i] = board [i-1];
                                board [i - 1] = board [i - 4] % 100;
                                board [i - 4] = 0;
                                simulate (i, i-2);
                                board [i - 4] = board [i - 1] + 100;
                                board [i - 1] = board [i];}
                            board [i] = board [i - 1] + 100;
                            board [i - 1] = 0;}}
                    simulate (i, i+1);
                    simulate (i, i-1);
                    simulate (i, i+10);
                    simulate (i, i-10);
                    simulate (i, i+9);
                    simulate (i, i-9);
                    simulate (i, i+11);
                    simulate (i, i-11);	}
            if ( i%10 == 8)i += 2;}
        deep--;
        ababort = false;}

    public boolean ischeck () {
        int king = 0;
        for ( int i = 21; i < 99; i++)
        {if ((board [i] % 100 / 10 == color) && (board [i] % 10 == 6))
        {king = i;
            break;}
            if ( i % 10 == 8)i += 2;}
        if ((board [king-21] % 10 == 2) && (board [king-21] % 100 / 10 != color))return true;
        if ((board [king+21] % 10 == 2) && (board [king+21] % 100 / 10 != color))return true;
        if ((board [king-19] % 10 == 2) && (board [king-19] % 100 / 10 != color))return true;
        if ((board [king+19] % 10 == 2) && (board [king+19] % 100 / 10 != color))return true;
        if ((board [king- 8] % 10 == 2) && (board [king- 8] % 100 / 10 != color))return true;
        if ((board [king+ 8] % 10 == 2) && (board [king+ 8] % 100 / 10 != color))return true;
        if ((board [king-12] % 10 == 2) && (board [king-12] % 100 / 10 != color))return true;
        if ((board [king+12] % 10 == 2) && (board [king+12] % 100 / 10 != color))return true;
        int j = king;
        while (board [j - 9] != 99)
        {j -= 9;
            if (board [j] % 100 / 10 == color)break;
            if (board [j] == 0)
                continue;
            if ((board [j] % 10  == 3) || (board [j] % 10  == 5))return true;
            else break;}
        j = king;
        while (board [j+9] != 99)
        {j += 9;
            if (board [j] % 100 / 10 == color)break;
            if (board [j] == 0)continue;
            if ((board [j] % 10 == 3) || (board [j] % 10 == 5))return true;
            else break;}
        j = king;
        while (board [j-11] != 99)
        {j -= 11;
            if (board [j] % 100 / 10 == color)
                break;
            if (board [j] == 0)continue;
            if ( (board [j] % 10 == 3) || (board [j] % 10 == 5))return true;
            else break;}
        j = king;
        while (board [j+11] != 99)
        {j +=11;
            if (board [j] % 100 / 10 == color)break;
            if (board [j] == 0)continue;
            if ( (board [j] % 10 == 3) || (board [j] % 10 == 5))return true;
            else break;}
        j = king;
        while (board [j-10] != 99)
        {j -= 10;
            if (board [j] % 100 / 10 == color)break;
            if (board [j] == 0)continue;
            if ((board [j] % 10 == 4) || (board [j] % 10 == 5))return true;
            else break;}
        j = king;
        while (board [j+10] != 99)
        {j += 10;
            if (board [j] % 100 / 10 == color)break;
            if (board [j] == 0)	continue;
            if ((board [j] % 10 == 4) || (board [j] % 10 == 5))return true;
            else break;}
        j = king;
        while (board [j-1] != 99)
        {j -=1;
            if (board [j] % 100 / 10 == color)break;
            if (board [j] == 0)continue;
            if ((board [j] % 10 == 4) || (board [j] % 10 == 5))return true;
            else break;}
        j = king;
        while (board [j+1] != 99)
        {j +=1;
            if (board [j] % 100 / 10 == color)break;
            if (board [j] == 0)continue;
            if ((board [j] % 10 == 4) || (board [j] % 10 == 5))return true;
            else break;}
        if (color == 1)
        {if ((board [king-11] % 10 == 1) && (board [king-11] % 100 / 10 == 2))return true;
            if ((board [king- 9] % 10 == 1) && (board [king- 9] % 100 / 10 == 2))return true;}
        else
        {if ((board [king+11] % 10 == 1) && (board [king+11] % 100 / 10 == 1))return true;
            if ((board [king+ 9] % 10 == 1) && (board [king+ 9] % 100 / 10 == 1)) return true;}
        if ( board [king+ 1] % 10 == 6 )return true;
        if ( board [king- 1] % 10 == 6 )return true;
        if ( board [king+10] % 10 == 6 )return true;
        if ( board [king-10] % 10 == 6 )return true;
        if ( board [king+11] % 10 == 6 )return true;
        if ( board [king-11] % 10 == 6 )return true;
        if ( board [king+ 9] % 10 == 6 )return true;
        if ( board [king- 9] % 10 == 6 )return true;
        return false;}

    public boolean isvalid (int move)
    {for (int i = 0; i < movecounter; i++)
    {if (movelist [i] == move)return true;}
        return false;}

    public void mouseClicked(java.awt.event.MouseEvent e)
    {}
    public void mouseDragged(java.awt.event.MouseEvent e)
    {x = e.getX() / 80;
        if(x<0)x = 0;
        if (x > 7 )x = 7;
        y = e.getY() / 80;
        if (y < 0)y = 0;
        if (y > 7 )y = 7;
        end = 21 + y * 10 + x;
        if ( end != alt)
        {if	(alt != start)
            paintField (alt);
            if ( end != start)
            {Graphics g = getGraphics ();
                if ( (code != 1) && (isvalid (start * 100 + end) ))g.setColor (green);
                else g.setColor (red);
                g.fillRect (x * 80, y * 80, 80, 80);
                try
                {g.drawImage (pieces [graphboard [end] % 100 - 10], x * 80, y * 80, 80, 80, parent);}
                catch (ArrayIndexOutOfBoundsException ex) {}}
            alt = end;}}
    public void mouseEntered(java.awt.event.MouseEvent e)
    {}
    public void mouseExited(java.awt.event.MouseEvent e)
    {}
    public void mouseMoved(java.awt.event.MouseEvent e)
    {}
    public void mousePressed(java.awt.event.MouseEvent e)
    {x = e.getX() / 80;
        if (x < 0)x = 0;
        if (x > 7)x = 7;
        y = e.getY() / 80;
        if (y < 0)y = 0;
        if (y > 7)y = 7;
        start = 21 + y*10 + x;
        alt = start;
        end = start;
        Graphics g = getGraphics ();
        g.setColor (blue);
        g.fillRect (x * 80, y * 80, 80, 80);
        try
        {g.drawImage (pieces [graphboard [start] % 100 - 10], x * 80, y * 80, 80, 80, parent);}
        catch (ArrayIndexOutOfBoundsException ex) {}}
    public void mouseReleased(java.awt.event.MouseEvent e)
    {paintField (start);
        paintField (end);
        if ((code != 1) && (isvalid (start * 100 + end ) ))
        {execute (start, end);}}

    public void multisimulate (int start, int inc)
    {int to = start;
        while ((board [to + inc ] != 99) && (board [to + inc] % 100 / 10  != color))
        {to += inc;
            if (board [to] != 0)
            {simulate (start, to);
                return;}
            simulate (start, to);}
        simulate(start, to);}

    public void newgame() {
    }
}
}
