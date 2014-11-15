/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gremlin.example;

import com.google.common.base.Predicate;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.gremlin.structure.Edge;
import com.tinkerpop.gremlin.structure.Graph;
import com.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import java.util.Random;
import org.apache.commons.math3.linear.RealVector;
import org.encog.neural.som.SOM;

/**
 *
 * @author me
 */
public class SOM1 {

    public static class SOMRun implements Runnable {

        final int TEST_ITERATIONS = 100;
        final int TRAIN_ITERATIONS = 100000;
        final double DRIFT_FACTOR = 400.0;
        final int OUTPUT_WIDTH = 10;
        final int OUTPUT_HEIGHT = 10;
        final int OUTPUT_DIMENSIONS = 2;
        final double LEARNING_RATE = 0.1;
        final int INPUT_DIMENSIONS = 3;

        private final Graph graph;
        private final SOM brain;
        final Random random = new Random();

        public SOMRun() {

            graph = TinkerGraph.open();
            brain = graph.addVertex(null, SomBrain.class);
            brain.reset(new SOMExponentialDecay(TRAIN_ITERATIONS, LEARNING_RATE), OUTPUT_DIMENSIONS, INPUT_DIMENSIONS);

        }

        public void run() {

        //initialize brain with 3d input and 2d output
            //create the output latice
            for (double x = 0; x < OUTPUT_WIDTH; x++) {
                for (double y = 0; y < OUTPUT_HEIGHT; y++) {
                    brain.addOutput(x, y);
                }
            }

            //run through RANDOM training data
            for (int iteration = 0; iteration < TRAIN_ITERATIONS; iteration++) {
                brain.setInput(random.nextDouble(), random.nextDouble(), random.nextDouble());
                brain.getBestMatchingUnit(true);
            }

            System.out.println("trained");
            
            //some static varibles for the blocksize
            final double blockSize = 0.0025;
            final double maxOffset = 1.0 - blockSize;
            //test the maximum distance of close colors in the color space
            double farthestDistanceClose = 0.0;
            String closeOutText = "";
            for (int iteration = 0; iteration < TEST_ITERATIONS; iteration++) {
                final StringBuilder outText = new StringBuilder(64);
                //find a mutual offset in the color space (leaving room for the
                //block)
                final double redOffset = random.nextDouble() * maxOffset;
                final double greenOffset = random.nextDouble() * maxOffset;
                final double blueOffset = random.nextDouble() * maxOffset;
                outText.append("close color offsets... red: ").append(redOffset).append(", green: ").append(greenOffset).append(", blue: ").append(blueOffset).append('\n');

                //get the location of a color within the block
                brain.setInput(
                        redOffset + (random.nextDouble() * blockSize),
                        greenOffset + (random.nextDouble() * blockSize),
                        blueOffset + (random.nextDouble() * blockSize)
                );

                double[] iRandom = brain.getInputVector().toArray();

                outText.append("close color1... red:").append(iRandom[0]).append(", green: ").append(iRandom[1]).append(", blue").append(iRandom[2]).append('\n');
                final RealVector color1 = brain.getBestMatchingUnit(true);

                //get the location of the other color within the block
                brain.setInput(
                        redOffset + (random.nextDouble() * blockSize),
                        greenOffset + (random.nextDouble() * blockSize),
                        blueOffset + (random.nextDouble() * blockSize)
                );

                double[] jRandom = brain.getInputVector().toArray();

                outText.append("close color2... red:").append(jRandom[0]).append(", green: ").append(jRandom[1]).append(", blue").append(jRandom[2]).append('\n');

                final RealVector color2 = brain.getBestMatchingUnit(true);

                //calculate the distance between these two points
                outText.append("close color1 point: ").append(color1).append('\n');
                outText.append("close color2 point: ").append(color2).append('\n');

                final double distance = color1.getDistance(color2);

                outText.append("close color distance: ").append(distance).append('\n');
                //store the distance if its greater than the current max
                if (farthestDistanceClose < distance) {
                    farthestDistanceClose = distance;
                    closeOutText = outText.toString();
                }
                
                System.out.println(outText.toString());
                try { Thread.sleep(20); } catch (InterruptedException ex) {                }
            }

            //test the maximum distance of far colors in the color space
            final double maxDrift = maxOffset / DRIFT_FACTOR;
            double closestDistanceFar = Double.POSITIVE_INFINITY;
            String farOutText = "";
            for (int iteration = 0; iteration < TEST_ITERATIONS; iteration++) {
                final StringBuilder outText = new StringBuilder(64);
                //get the location of a color within the block
                final boolean isRed1Positive = random.nextBoolean();
                final boolean isGreen1Positive = random.nextBoolean();
                final boolean isBlue1Positive = random.nextBoolean();
                brain.setInput(
                        (isRed1Positive ? random.nextDouble() * maxDrift : 1.0 - (random.nextDouble() * maxDrift)),
                        (isGreen1Positive ? random.nextDouble() * maxDrift : 1.0 - (random.nextDouble() * maxDrift)),
                        (isBlue1Positive ? random.nextDouble() * maxDrift : 1.0 - (random.nextDouble() * maxDrift))
                );

                double[] iRandom = brain.getInputVector().toArray();
                outText.append("far color1... red:").append(iRandom[0]).append(", green: ").append(iRandom[1]).append(", blue").append(iRandom[2]).append('\n');
                
                final RealVector color1 = brain.getBestMatchingUnit(true);

                //get the location of the other color within the block
                brain.setInput(
                        (isRed1Positive ? 1.0 - (random.nextDouble() * maxDrift) : random.nextDouble() * maxDrift),
                        (isGreen1Positive ? 1.0 - (random.nextDouble() * maxDrift) : random.nextDouble() * maxDrift),
                        (isBlue1Positive ? 1.0 - (random.nextDouble() * maxDrift) : random.nextDouble() * maxDrift)
                );

                double[] jRandom = brain.getInputVector().toArray();
                outText.append("far color2... red:").append(jRandom[0]).append(", green: ").append(jRandom[1]).append(", blue").append(jRandom[2]).append('\n');
                
                final RealVector color2 = brain.getBestMatchingUnit(true);

                //calculate the distance between these two points
                outText.append("far color1 point: ").append(color1).append('\n');
                outText.append("far color2 point: ").append(color2).append('\n');
                final double distance = color1.getDistance(color2);
                outText.append("far color distance: ").append(distance).append('\n');
                //store the distance if its greater than the current max
                if (closestDistanceFar > distance) {
                    closestDistanceFar = distance;
                    farOutText = outText.toString();
                }

                System.out.println(outText.toString());
                try { Thread.sleep(20); } catch (InterruptedException ex) {                }
            }

            System.out.println("colors map properly? far: " + closestDistanceFar + " -> close: " + farthestDistanceClose + "\n" + closeOutText + "\n" + farOutText + "\n" +  "---->" + (closestDistanceFar > farthestDistanceClose) );
        }
        

    }

    public static void main(String[] args) {

        SOMRun network = new SOMRun();

        Predicate<Edge> edgeFilter = new Predicate<Edge>() {
            @Override public boolean apply(final Edge t) {
                return (t.getProperty("weight")!=null);
            }
        };
        
        /*new NWindow("Self Organizing Map",
                new PCanvas(new BasicGrailScope(
                                network.graph,
                                null,
                                edgeFilter,
                                new GraphDisplays(
                                        //new FastOrganicLayout(),
                                        new SomDisplay()
                                )
                        ))).show(800, 700, true);
*/
        new Thread(network).start();
    }
}
