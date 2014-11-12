/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package toki.example;

import toki.ApproximateFunction.Add;
import toki.ApproximateFunction.ENode;
import toki.ApproximateFunction.ExpFunction;
import toki.ApproximateFunction.InputNode;
import toki.ApproximateFunction.Multiply;
import toki.ApproximateFunction.RandomScalarParameter;
import toki.ApproximateFunction.ScalarParameter;
import toki.ApproximateFunction.SqrFunction;
import toki.ApproximateFunction.VectorNode;
import toki.ApproximateFunction.VectorNodeBuilder;
import toki.ApproximateFunction.VectorSum;

/**
 *
 * @author me
 */
public class FunctionApproximationRBF  {

    public ENode make(final int numFeatures, InputNode[] inputs) {
        return new Multiply(
            new VectorSum( 
                    new VectorNode(numFeatures+1, new VectorNodeBuilder() {
                        public ENode newNode(int i) {
                            if (i == numFeatures) {
                                return new Multiply(
                                       new ScalarParameter(1.0), 
                                       new ExpFunction(
                                               new Multiply(
                                                new ScalarParameter(-1.0),
                                                new VectorSum(
                                                     new VectorNode(inputs.length, new VectorNodeBuilder() {

                                    @Override public ENode newNode(int index) {
                                        return new SqrFunction(
                                                new Add(
                                                    new Multiply(
                                                            inputs[index],
                                                            new ScalarParameter(10.0)
                                                    ),
                                                    new RandomScalarParameter(-10,0.0)
                                                )                                        
                                        );
                                    }
 //str += "    f = INPUTS[j] * NEWPARAMETER(10.0) + NEWRANDOMPARAMETER(-10.0,0.0);\n";
 //str += "    push(ys,f * f);\n";


                                                     })                                                    
                                                )
                                               )
                                       )
                                );
                            }
                            else {
                                return new ScalarParameter(0);
                            }
                        }
                    })
            ),
            new ScalarParameter(1.0)
        );
    }
 
    
    /*
        var str = "";
    str += "xs = [];\n";
    str += "for(i = 1..NUMFEATURES) {\n";
    str += "  ys = [];\n";
    str += "  for(j = 0..length(INPUTS) - 1){\n";
    str += "    f = INPUTS[j] * NEWPARAMETER(10.0) + NEWRANDOMPARAMETER(-10.0,0.0);\n";
    str += "    push(ys,f * f);\n";
    str += "  };\n";
    str += "  push(ys,NEWPARAMETER(0.0));\n";
    str += "  param = NEWPARAMETER(-1.0);\n";
    str += "  param <= 0.0;\n";
    str += "  push(xs,NEWPARAMETER(1.0) * exp(param * sum(ys)));\n";
    str += "};\n";
    str += "push(xs,NEWPARAMETER(0.0));\n";
    str += "FUNCTION = sum(xs) * NEWPARAMETER(1.0);\n";
    this.rbfStr = str;
    */
}
