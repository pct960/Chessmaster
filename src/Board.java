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
        java.lang.Runnable
{AudioClip s;
    int [] board = new int [120];
    int [] graphboard = new int [120];
    Color brown = new Color (0x8B2500);
    Color white = new Color (0xFFFFCC);
    Color red = new Color (0xCC0000);
    Color green = new Color (0x009900);
    Color blue = new Color (0x000099);
    Image [] pieces = new Image [18];
    Applet parent;
    int code=0,start=21,alt=21,end=21,x=0,y=0;
    int [] movelist = new int [250];
    int movecounter = 0;
    int color = 1;
    Thread th = null;
    int deep = 0;
    int target = 4;
    float value = 0;
    float minimax [] = new float [10];
    float alphabeta [] = new float [10];
    boolean ababort = false;
    int move;
    float [] posvalues =
            {	0.00f,	0.00f, 	0.00f, 	0.00f, 	0.00f, 	0.00f, 	0.00f, 	0.00f, 	0.00f, 	0.00f,
                    0.00f,	0.00f,	0.00f,	0.00f,	0.00f,	0.00f,	0.00f,	0.00f,	0.00f,	0.00f,
                    0.00f,	0.00f,	0.01f,	0.02f,	0.03f,	0.03f,	0.02f,	0.01f,	0.00f,	0.00f,
                    0.00f,	0.01f,	0.04f,	0.04f,	0.04f,	0.04f,	0.04f,	0.04f,	0.01f,	0.00f,
                    0.00f,	0.03f,	0.04f,	0.06f,	0.06f,	0.06f,	0.06f,	0.04f,	0.02f,	0.00f,
                    0.00f,	0.03f,	0.04f,	0.06f,	0.08f,	0.08f,	0.06f,	0.04f,	0.03f,	0.00f,
                    0.00f,	0.03f,	0.04f,	0.06f,	0.08f,	0.08f,	0.06f,	0.04f,	0.03f,	0.00f,
                    0.00f,	0.02f,	0.04f,	0.06f,	0.06f,	0.06f,	0.06f,	0.04f,	0.02f,	0.00f,
                    0.00f,	0.01f,	0.04f,	0.04f,	0.04f,	0.04f,	0.04f,	0.04f,	0.01f,	0.00f,
                    0.00f,	0.00f,	0.01f,	0.02f,	0.03f,	0.03f,	0.02f,	0.01f,	0.00f,	0.00f,
                    0.00f,	0.00f,	0.00f,	0.00f,	0.00f,	0.00f,	0.00f,	0.00f,	0.00f,	0.00f,
                    0.00f,	0.00f,	0.00f,	0.00f,	0.00f,	0.00f,	0.00f,	0.00f,	0.00f,	0.00f };