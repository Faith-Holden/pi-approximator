# pi-approximator
My solution for Chapter 12 Exercise 6 of “Introduction to Programming Using Java”.

NOTE: This is a javafx program. It requires the javafx library as a dependency. (See bottom of this README for javafx instructions).

Problem Description:
It is possible to get an estimate of the mathematical constant Pi by using a random process.
The idea is based on the fact that the area of a circle of radius 1 is equal to Pi, and the
area of a quarter of that circle is Pi/4. 
The area of the whole square is one, while the area of the part inside the circle is Pi/4. If
we choose a point in the square at random, the probability that it is inside the circle is
Pi/4. If we choose N points in the square at random, and if C of them are inside the circle,
we expect the fraction C/N of points that fall inside the circle to be about Pi/4. That is,
we expect 4*C/N to be close to Pi. If N is large, we can expect 4*C/N to be a good estimate
for Pi, and as N gets larger and larger, the estimate is likely to improve.
We can pick a random point in the square by choosing numbers x and y in the range
0 to 1 (using Math.random()). Since the equation of the circle is x*x+y*y=1, the point
lies inside the circle if x*x+y*y is less than 1. One trial consists of picking x and y and
testing whether x*x+y*y is less than 1. To get an estimate for Pi, you have to do many
trials, count the trials, and count the number of trials in which x*x+y*y is less than 1,
For this exercise, you should write a GUI program that does this computation and
displays the result. The computation should be done in a separate thread, and the results
should be displayed periodically. The program can use Labels to the display the results.
It should set the text on the labels after running each batch of, say, one million trials.
(Setting the text after each trial doesn’t make sense, since millions of trials can be done in
one second, and trying to change the display millions of times per second would be silly.
Your program should have a “Run”/”Pause” button that controls the computation.
When the program starts, clicking “Run” will start the computation and change the text
on the button to “Pause”. Clicking “Pause” will cause the computation to pause. The
thread that does the computation should be started at the beginning of the program, but
should immediately go into the paused state until the “Run” button is pressed. Use the
wait() method in the thread to make it wait until “Run” is pressed. Use the notify()
method when the “Run” button is pressed to wake up the thread. Use a boolean signal
variable, running, to control whether the computation thread is paused. (The wait()
and notify() methods are covered in Subsection 12.3.5.)
You might want to start with a version of the program with no control button. In that
version, the computation thread can run continually from the time it is started. Once that
is working, you can add the button and the control feature.
To get you started, here is the code from the thread in my solution that runs one batch
of trials and updates the display labels:
for (int i = 0; i < BATCH SIZE; i++) {
double x = Math.random();
double y = Math.random();
trialCount++;
if (x*x + y*y < 1)
inCircleCount++;
}
double estimateForPi = 4 * ((double)inCircleCount / trialCount);
Platform.runLater( () -> {
countLabel.setText( " Number of Trials: " + trialCount);
piEstimateLabel.setText( " Current Estimate: " + estimateForPi);
} );
The variables trialCount and inCircleCount are of type long in order to allow the
number of trials to be more than the two billion or so that would be possible with a
variable of type int.
(I was going to ask you to use multiple computation threads, one for each available
processor, but I ran into an issue when using the Math.random() method in several threads.
This method requires synchronization, which causes serious performance problems when
several threads are using it to generate large amounts of random numbers. A solution
to this problem is to have each thread use its own object of type java.util.Random to
generate its random numbers (see Subsection 5.3.1). My on-line solution to this exercise
discusses this problem further.)

Javafx setup instructions:
Download javafx from: https://gluonhq.com/products/javafx/ (I used javafx 12). Save it to a location of your choice.
Unpack the zip folder.
Open my project with your IDE of choice (I use intellij IDEA).
Add the javafx/lib folder as an external library for the project. For intellij, this means going to "project structure" -> "libraries" -> "add library" ->{javafx location}/lib
Add the following as a VM argument for the project: --module-path "{full path to your javafx/lib folder}" --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.graphics,javafx.media,javafx.swing,javafx.web
Build and run the project as normal.
