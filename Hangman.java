// The "Hangman" class.
import java.awt.*;
import java.io.*;
import hsa.Console;
import hsa.Message;

public class Hangman
{
    static Console c;           // The output console
    int currentScore = 0; //Holds the score of the current game
    String currentLogin; // Login name for scoreboard
    final String scoresFile = "scores.txt"; //Stores the score file name
    final String wordsFile = "words.txt"; //Stores the words file name
    char choice;
    String gameWord;
    final int MAX_WORD_COUNT = 996;
    public Hangman ()
    {
	c = new Console ();
    }


    private void pauseProgram ()
    {
	c.println ("Press any key to continue...");
	c.getChar ();
    }


    public void splashScreen ()
    {
	final Font titleSize = new Font ("Courier", 1, 50);
	final Font nameSize = new Font ("Courier", 1, 30);
	drawBackground ();
	drawMan (6);
	c.setFont (titleSize);
	c.drawString ("Hangman!", 200, 50);
	c.setFont (nameSize);
	c.drawString ("By: Feng", 230, 100);
	c.setTextBackgroundColor (Color.red);
	c.setCursor (24, 1);
	pauseProgram ();
	c.setTextBackgroundColor (Color.white);
	for (int i = 0 ; i < 500 ; i++)
	{
	    c.setColor (Color.white);
	    c.drawLine (0, i, 640, i);
	    try
	    {
		Thread.sleep (2);
	    }
	    catch (InterruptedException e)
	    {
	    }
	}
    }


    private void drawBackground ()
    {
	title ();
	c.setColor (Color.red);
	c.fillRect (0, 400, 640, 100);
	c.setColor (Color.gray);
	c.fillRect (0, 0, 640, 400);
	c.setColor (Color.black);
	c.fillRect (110, 380, 150, 15); //base
	c.fillRect (170, 150, 15, 230); //upright bar
	c.fillRect (170, 150, 150, 15); //top
	for (int i = 0 ; i < 15 ; i++)
	    c.drawLine (185, 350 + i, 215, 380 + i); //support beam
	c.drawLine (319, 165, 319, 200); //string

    }


    private String wordGen ()
    {
	try
	{
	    BufferedReader br = new BufferedReader (new FileReader ("words.txt"));
	    int choice = (int) (Math.random () * MAX_WORD_COUNT);
	    for (int i = 0 ; i < choice - 1 ; i++)
	    {
		br.readLine ();
	    }
	    return br.readLine ();
	}
	catch (IOException e)
	{

	}
	return "";
    }


    public void title ()
    {
	c.clear ();
	c.print ("", 37);
	c.println ("Hangman");
	c.println ();
    }


    private boolean drawMan (int body)  //helper method to draw body parts
    {
	//chooses which body part to draw
	boolean end = false;
	switch (body)
	{
	    case 6:
		c.drawLine (340, 240, 319, 280); //his left arm
		end = true;
	    case 5:
		c.drawLine (300, 240, 319, 280); //his right arm
	    case 4:
		c.drawLine (319, 320, 340, 360); //his left leg
	    case 3:
		c.drawLine (319, 320, 300, 360); //his right leg
	    case 2:
		c.drawLine (319, 240, 319, 320); //body
	    case 1:
		c.fillOval (300, 200, 40, 40); //head
	}
	return end;
    }


    public void mainMenu ()
    {
	title ();
	c.println ("Enter a menu choice to get started!");
	c.println ();
	c.println ("1) Play hangman level 1");
	c.println ("2) Play hangman level 2");
	c.println ("3) Instructions");
	c.println ("4) High Scores");
	c.println ("5) Exit");
	choice = c.getChar ();
    }


    private String checkString (String checkString, char inputedChar, String screen)
    {
	String string = "";
	for (int i = 0 ; i < checkString.length () ; i++)
	{
	    if (checkString.charAt (i) == inputedChar)
	    {
		string += inputedChar;
	    }
	    else
	    {
		string += screen.charAt (i);
	    }
	}
	return string;
    }


    public boolean playGame (boolean mode)
    {
	drawBackground ();
	gameWord = wordGen ();
	String outputGame = "";
	int body = 0;
	for (int i = 0 ; i < gameWord.length () ; i++)
	    outputGame += "-";
	c.setTextBackgroundColor (Color.gray);
	c.println ("If you would like a hint, press 1. If you would like to guess the word, press 2.Or enter the character you would like to geuss");
	c.println (outputGame);
	while (true)
	{
	    if (outputGame.equals (gameWord))
		return true;
	    c.setCursor (5, 1);
	    char input = (c.getChar () + "").toLowerCase ().charAt (0);
	    if (input >= 97 && input <= 122)
	    {
		String temp = checkString (gameWord, input, outputGame);
		if (temp.equals (outputGame)) //if it has not changed
		{
		    body++;
		    currentScore += body;
		}
		else if (temp.equals (gameWord))
		{
		    currentScore -= gameWord.length ();
		    return true;
		}
		else
		    outputGame = temp;
		if (drawMan (body))
		    return false;
	    }
	    else if (input == '2')
	    {
		c.print ("Enter your word:");
		if (c.readLine ().equals (gameWord))
		{
		    currentScore -= 10;
		    return true;
		}
		else
		{
		    body++;
		    currentScore += 10;
		}
		c.setCursor (5, 1);
	    }
	    else if (input == '1')
	    {
		do
		{
		    String hold = checkString (gameWord, gameWord.charAt ((int) (Math.random () * gameWord.length ())), outputGame);
		    if (!hold.equals (outputGame))
		    {
			outputGame = hold;
			break;
		    }
		}
		while (true);
		currentScore += 10;
	    }
	    if (drawMan (body))
		return false;
	    c.println (outputGame);
	}
    }


    private void addSortFile (int score, String name)
    {
	final BufferedReader br;
	final PrintWriter pw;
	try
	{
	    br = new BufferedReader (new FileReader ("scores.lexd"));
	    String[] fileLines = new String [20];
	    for (int i = 0 ; i < 20 ; i++)
		fileLines [i] = br.readLine ();
	    br.close ();
	    pw = new PrintWriter (new FileWriter ("scores.lexd"));
	    for (int i = 0 ; i < 20 ; i += 2)
	    {
		if (fileLines [i] == null)
		{
		    pw.println (name);
		    pw.println (score);
		    break;
		}
		if (Integer.parseInt (fileLines [i + 1]) < score)
		{
		    pw.println (fileLines [i]);
		    pw.println (fileLines [i + 1]);
		}
		else
		{
		    pw.println (name);
		    pw.println (score);
		    pw.println (fileLines [i]);
		    pw.println (fileLines [i + 1]);
		    break;
		}

	    }
	    pw.close ();
	}
	catch (IOException e)
	{
	}
    }


    public void display (boolean win)
    {
	drawBackground ();
	c.println ("The word was: " + gameWord);
	if (win)
	{
	    c.println ("YOU WIN");
	    c.println ("Enter your name for a possible leaderboard score!");
	    addSortFile (currentScore, c.readLine ());
	}
	else
	    c.println ("YOU LOSE. Better Luck next time!");
	pauseProgram ();

    }


    public void instructions ()
    {
	title ();
	c.println ("Instructions:");
	c.println ("Menu Option 1: Begins a hangman game with hints turned on");
	c.println ("Menu Option 1: Begins a hangman game with hints turned off(cheat has been enabled for developers)");
	c.println ("------------");
	c.println ("Point System");
	c.println ("------------");
	c.println ("+1 point for every wrong guess");
	c.println ("+1 point for every 30 seconds");
	c.println ("ord guesses are the worth the size of the word, but only one body part");
	c.println ("The highscore menu allows you to check old scores");
	pauseProgram ();
    }


    public void displayScore ()
    {
	title ();
	try
	{
	    BufferedReader br = new BufferedReader (new FileReader ("scores.lexd"));
	    for (int i = 1 ; i <= 10 ; i++)
	    {
		String temp = br.readLine ();
		c.print (i + ". ");
		if (temp != null)
		    c.print (temp + "\t\t\t" + br.readLine ());
		c.println ();
	    }
	}
	catch (IOException e)
	{
	}
	pauseProgram ();
    }


    public void goodbye ()
    {
	title ();
	c.println ("Thanks for using my program!");
	c.println ("By: Feng");
	pauseProgram ();
	c.close ();
    }


    public static void main (String[] args)
    {
	Hangman hm = new Hangman ();
	hm.splashScreen ();
	while (true)
	{
	    hm.mainMenu ();
	    switch (hm.choice)
	    {
		case '1':
		    hm.display (hm.playGame (true));
		    continue;
		case '2':
		    hm.display (hm.playGame (false));
		    continue;
		case '3':
		    hm.instructions ();
		    continue;
		case '4':
		    hm.displayScore ();
		    continue;
		case '5':
		    break;
		default:
		    new Message ("Please enter a choice!");
		    continue;

	    }
	    break;
	}
	hm.goodbye ();
    }
} // Hangman class


