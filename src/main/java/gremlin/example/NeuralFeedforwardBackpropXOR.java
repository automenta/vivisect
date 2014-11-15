/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gremlin.example;

import automenta.vivisect.dimensionalize.FastOrganicLayout;
import automenta.vivisect.graph.GraphDisplays;
import automenta.vivisect.swing.NWindow;
import automenta.vivisect.swing.PCanvas;
import gremlin.BasicGrailScope;
import gremlin.display.NeuralDisplay;
import gremlin.NeuralTimestream;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.encog.engine.network.activation.ActivationFunction;
import org.jgrapht.Graphs;

/**
 *
 * @author me
 */
public class NeuralFeedforwardBackpropXOR {

    public static class Bitwise3 implements Runnable {

        private static final ActivationFunction ACTIVATION_FUNCTION = new HyperbolicTangentActivationFunction();
        
        private final FramedTransactionalGraph<?> graph;


        BackpropNeuron createNeuron(final FramedGraph<?> graph, final String layer) {
            final BackpropNeuron neuron = graph.addVertex(null, BackpropNeuron.class);
            neuron.asVertex().setProperty("layer", layer);
            return neuron;
        }

        public Bitwise3() {

            graph = Graphs.makeTinkerGraph();

            init();
        }

        protected void init() {
            

        //
            //Construct the Neural Graph
            //
            final List<BackpropNeuron> newInputNeurons = new ArrayList<BackpropNeuron>(2);
            newInputNeurons.add(createNeuron(graph, "input"));
            newInputNeurons.add(createNeuron(graph, "input"));
            newInputNeurons.add(createNeuron(graph, "input"));
            final List<BackpropNeuron> newHiddenNeurons = new ArrayList<BackpropNeuron>(4);
            newHiddenNeurons.add(createNeuron(graph, "hidden"));
            newHiddenNeurons.add(createNeuron(graph, "hidden"));
            newHiddenNeurons.add(createNeuron(graph, "hidden"));
            final BackpropNeuron newOutputNeuron = createNeuron(graph, "output");
            final BackpropNeuron biasNeuron = createNeuron(graph, "bias");
            biasNeuron.setSignal(1.0);

            //connect all input neurons to hidden neurons
            for (final BackpropNeuron inputNeuron : newInputNeurons) {
                for (final BackpropNeuron hiddenNeuron : newHiddenNeurons) {
                    graph.addEdge(null, inputNeuron.asVertex(), hiddenNeuron.asVertex(), "signals", BackpropSynapse.class);
                }
            }
            //connect all hidden neurons to the output neuron
            for (final BackpropNeuron hiddenNeuron : newHiddenNeurons) {
                graph.addEdge(null, hiddenNeuron.asVertex(), newOutputNeuron.asVertex(), "signals", BackpropSynapse.class);

                //create bias neuron
                graph.addEdge(null, biasNeuron.asVertex(), hiddenNeuron.asVertex(), "signals", BackpropSynapse.class);
            }
            //create bias neuron for output neuron
            graph.addEdge(null, biasNeuron.asVertex(), newOutputNeuron.asVertex(), "signals", BackpropSynapse.class);

        //
            //Construct the Action Triggers for the neural Graph
            //
            //First lets handle the output layer for propagation
            final PrioritySerialTrigger propagateOutputTrigger = createPrioritySerialTrigger(graph);
            //connect it to the output neuron with a priority of 0 (highest priority)
            final PrioritySerialTriggerEdge outputTriggerEdge = graph.addEdge(null, propagateOutputTrigger.asVertex(), newOutputNeuron.asVertex(), "triggers", PrioritySerialTriggerEdge.class);
            outputTriggerEdge.setTriggerPriority(1000);
            outputTriggerEdge.setTriggerAction("propagate");

            //now lets handle the hidden layer for propagation
            final PrioritySerialTrigger propagateHiddenTrigger = createPrioritySerialTrigger(graph);
            propagateHiddenTrigger.asVertex().setProperty("triggerPointer", "propagate");
            //connect it to each of the hidden neurons with a priority of 0 (highest priority)
            for (final BackpropNeuron hiddenNeuron : newHiddenNeurons) {
                final PrioritySerialTriggerEdge newEdge = graph.addEdge(null, propagateHiddenTrigger.asVertex(), hiddenNeuron.asVertex(), "triggers", PrioritySerialTriggerEdge.class);
                newEdge.setTriggerPriority(1000);
                newEdge.setTriggerAction("propagate");
            }

            //chain the prop[agation of the hidden layer to the propagation of the output layer, but make sure it has less of a priority than the other triggers
            final PrioritySerialTriggerEdge chainTriggerPropagateEdge = graph.addEdge(null, propagateHiddenTrigger.asVertex(), propagateOutputTrigger.asVertex(), "triggers", PrioritySerialTriggerEdge.class);
            chainTriggerPropagateEdge.setTriggerPriority(0);
            chainTriggerPropagateEdge.setTriggerAction("actionTrigger");

            //next lets handle the input layer for back propagation
            final PrioritySerialTrigger backpropInputTrigger = createPrioritySerialTrigger(graph);
            //connect it to each of the input neurons
            for (final BackpropNeuron inputNeuron : newInputNeurons) {
                final PrioritySerialTriggerEdge newEdge = graph.addEdge(null, backpropInputTrigger.asVertex(), inputNeuron.asVertex(), "triggers", PrioritySerialTriggerEdge.class);
                newEdge.setTriggerPriority(1000);
                newEdge.setTriggerAction("backpropagate");
            }
            //also connect it to all the bias neurons
            final PrioritySerialTriggerEdge biasTriggerBackpropEdge = graph.addEdge(null, backpropInputTrigger.asVertex(), biasNeuron.asVertex(), "triggers", PrioritySerialTriggerEdge.class);
            biasTriggerBackpropEdge.setTriggerPriority(1000);
            biasTriggerBackpropEdge.setTriggerAction("backpropagate");

            //create backpropagation trigger for the hidden layer
            final PrioritySerialTrigger backpropHiddenTrigger = createPrioritySerialTrigger(graph);
            backpropHiddenTrigger.asVertex().setProperty("triggerPointer", "backpropagate");
            //connect it to each of the hidden neurons with a priority of 0 (highest priority)
            for (final BackpropNeuron hiddenNeuron : newHiddenNeurons) {
                final PrioritySerialTriggerEdge newEdge = graph.addEdge(null, backpropHiddenTrigger.asVertex(), hiddenNeuron.asVertex(), "triggers", PrioritySerialTriggerEdge.class);
                newEdge.setTriggerPriority(1000);
                newEdge.setTriggerAction("backpropagate");
            }

            //chain the hidden layers back propagation to the input layers trigger
            final PrioritySerialTriggerEdge chainTriggerBackpropEdge = graph.addEdge(null, backpropHiddenTrigger.asVertex(), backpropInputTrigger.asVertex(), "triggers", PrioritySerialTriggerEdge.class);
            chainTriggerBackpropEdge.setTriggerPriority(0);
            chainTriggerBackpropEdge.setTriggerAction("actionTrigger");

            //commit everything
            graph.commit();

        }

        @Override
        public void run() {

            final int maxCycles = 10000;
            final int completionPeriod = 50;
            final double maxError = 0.75;
            for (int cycle = maxCycles; cycle >= 0; cycle--) {
                int finished = 0;
                for (int in1 = -1; in1 <= 1; in1 += 2) {
                    for (int in2 = -1; in2 <= 1; in2 += 2) {
                        for (int in3 = -1; in3 <= 1; in3 += 2) {
                            boolean bi = in1 >= 0;
                            boolean bj = in2 >= 0;
                            boolean bk = in3 >= 0;
                            boolean expect = bi ^ bj ^ bk;
                            double expectD = expect ? 1.0 : -1.0;

                            train(graph, in1, in2, in3, expectD);

                            /*if (cycle % completionPeriod == 0 && calculateError(graph, in1, in2, in3, expectD) < maxError) {
                             finished++;
                             }*/
                        }
                    }
                }
                if (finished == 8) {
                    break;
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {

                }
            }

        }

        private PrioritySerialTrigger createPrioritySerialTrigger(final FramedGraph<?> graph) {
            return graph.addVertex(null, PrioritySerialTrigger.class);
        }

        private static void train(final FramedTransactionalGraph<?> graph, final double input1, final double input2, final double input3, final double expected) {
            propagate(graph, input1, input2, input3);

            final Iterator<BackpropNeuron> outputNeurons = graph.getVertices("layer", "output", BackpropNeuron.class).iterator();
            final BackpropNeuron outputNeuron = outputNeurons.next();
            outputNeuron.setDeltaTrain((expected - outputNeuron.getSignal()) * ACTIVATION_FUNCTION.activateDerivative(outputNeuron.getActivity()));
            graph.commit();

            final Iterator<PrioritySerialTrigger> backpropTriggers = graph.getVertices("triggerPointer", "backpropagate", PrioritySerialTrigger.class).iterator();
            final PrioritySerialTrigger backpropTrigger = backpropTriggers.next();
            backpropTrigger.trigger();
            graph.commit();
        }

        private static double propagate(final FramedTransactionalGraph<?> graph, final double input1, final double input2, final double input3) {
            final Iterator<BackpropNeuron> inputNeurons = graph.getVertices("layer", "input", BackpropNeuron.class).iterator();
            inputNeurons.next().setSignal(input1);
            inputNeurons.next().setSignal(input2);
            inputNeurons.next().setSignal(input3);
            graph.commit();

            final Iterator<PrioritySerialTrigger> propagateTriggers = graph.getVertices("triggerPointer", "propagate", PrioritySerialTrigger.class).iterator();
            final PrioritySerialTrigger propagateTrigger = propagateTriggers.next();
            try {
                propagateTrigger.trigger();
            } catch (final UndeclaredThrowableException caught) {
                caught.getUndeclaredThrowable().printStackTrace();
                throw caught;
            }
            graph.commit();

            final Iterator<BackpropNeuron> outputNeurons = graph.getVertices("layer", "output", BackpropNeuron.class).iterator();
            final BackpropNeuron outputNeuron = outputNeurons.next();
            return outputNeuron.getSignal();
        }

    }

    public static void main(String[] args) {

        Bitwise3 network = new Bitwise3();

        new NWindow("Feedforward Bitwise XOR Neural Network",
                new PCanvas(new BasicGrailScope(
                                network.graph,
                                new GraphDisplays(
                                        new FastOrganicLayout(),
                                        new NeuralDisplay(),
                                        new NeuralTimestream()
                                )
                        ))).show(800, 700, true);

        new Thread(network).start();
    }
}
