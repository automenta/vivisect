/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package toki;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author me
 */
public class ApproximateFunction {
    
    final int numFeatures;
    final int inputLength;
    double alpha, momentum;
    
    public ApproximateFunction(int inputLength, int numFeatures) {
        this(inputLength, numFeatures, 0.01, 0.2);
    }
    
    public ApproximateFunction(int inputLength, int numFeatures, double alpha, double momentum) {
        this.numFeatures = numFeatures;
        this.inputLength = inputLength;
        this.alpha = alpha;
        this.momentum = momentum;
    }
    
    public static class ENode {
        
    }
    
    public static interface Scalar {
        public double scalar();
    }
    
    public static class ScalarParameter extends ENode implements Scalar {
        
        private double value;

        public ScalarParameter(double initialValue) {
            this.value = initialValue;                    
        }

        public void set(double v) {
            this.value = v;
        }
        
        @Override
        public double scalar() {
            return value;
        }
        
    }
    
    public static class RandomScalarParameter extends ENode implements Scalar {
        
        private double min, max;

        public RandomScalarParameter(double min, double max) {
            this.min = min;
            this.max = max;
        }
                
        @Override public double scalar() {
            return Math.random() * ( max - min) + min;
        }
        
    }
        
    public static class InputNode extends ScalarParameter {
        
        public InputNode(double initialValue) {
            super(initialValue);
        }
        
    }
    
    public static class FunctionNode<X extends ENode> {
        public final X parameter;

        public FunctionNode(X parameter) {
            this.parameter = parameter;
        }
        
        
    }
    
    public interface VectorNodeBuilder {
            public ENode newNode(int index);
        }
    
    public static class VectorNode extends ENode {
        public final List<ENode> nodes;

        
        
        public VectorNode(int num, VectorNodeBuilder b) {
            nodes = new ArrayList(num);
            for (int i = 0; i < num; i++)
                nodes.add(b.newNode(i));
        }
        
        public VectorNode(List<ENode> nodes) {
            this.nodes = nodes;
        }        
    }
    
    public static class VectorSum extends FunctionNode<VectorNode> implements Scalar {

        public VectorSum(VectorNode summed) {
            super(summed);
        }

        @Override
        public double scalar() {
            double total = 0;
            for (ENode x : parameter.nodes) {
                if (x instanceof Scalar)
                    total += ((Scalar)x).scalar();
            }
            return total;
        }
        
    }

    abstract public static class BinaryNode<A extends ENode, B extends ENode> extends ENode implements Scalar {
        public final A a;
        public final B b;

        public BinaryNode(A a, B b) {
            this.a = a;
            this.b = b;
        }
        
    }
    public static class ExpFunction extends ENode implements Scalar {
        private Scalar x;
        
        public ExpFunction(Scalar a) {            
            this.x  = a;
        }

        @Override
        public double scalar() {
            return Math.exp(x.scalar());
        }
        
    }
    public static class SqrFunction extends ENode implements Scalar {
        private Scalar x;
        
        public SqrFunction(Scalar a) {            
            this.x  = a;
        }

        @Override
        public double scalar() {
            double s = x.scalar();
            return s*s;
        }
        
    }    
    
   public static class Add extends BinaryNode {

        public Add(Scalar a, Scalar b) {
            super((ENode)a, (ENode)b);
        }
        
        @Override
        public double scalar() {
            return ((Scalar)a).scalar() + ((Scalar)b).scalar();
        }        
        
    }    
    public static class Multiply extends BinaryNode {

        public Multiply(Scalar a, Scalar b) {
            super((ENode)a, (ENode)b);
        }
        
        @Override
        public double scalar() {
            return ((Scalar)a).scalar() * ((Scalar)b).scalar();
        }        
        
    }
    
    /*abstract public static class DotProduct extends ENode implements Scalar {
        private final VectorNode vector;
        private final Scalar scalar;
        
        public DotProduct(VectorNode a, Scalar b) {
            this.vector = a;
            this.scalar = b;
        }

        @Override
        public double scalar() {
            
        }            
    }
    */
    
/*    

    
    var str = "";
    str += "xs = [];\n";
    str += "for(i = 1..NUMFEATURES) {\n";
    str += "  ys = [];\n";
    str += "  for(j = 0..length(INPUTS) - 1){\n";
    str += "    f = INPUTS[j] * NEWPARAMETER(1.0) + NEWRANDOMPARAMETER(-1.0,0.0);\n";
    str += "    push(ys,f);\n";
    str += "  };\n";
    str += "  push(ys,NEWPARAMETER(0.0));\n";
    str += "  push(xs,NEWPARAMETER(1.0) * cos(NEWPARAMETER(PI * i) * sum(ys)));\n";
    str += "};\n";
    str += "push(xs,NEWPARAMETER(0.0));\n";
    str += "FUNCTION = sum(xs) * NEWPARAMETER(1.0);\n";
    this.fourierStr = str;
    */

}
