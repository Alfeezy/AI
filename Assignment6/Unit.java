/***************************************************
 Assignment 6: ANNs
 CIS 421 Artificial Intelligence, Spring 2018
 L. Grabowski, updated 09 April 2018
 Code adapted from J. Feldman
 Unit.java
 **************************************************/
  /*
  Name: Bastien Gliech
  CIS 421 Artificial Intelligence
  Assignment: 6 - ANN
  Due April 27, 2018
*/
  
import java.util.*;

/**
 * Unit is the basic unit of a neural network.
 */
public class Unit {

  // Unit attributes

  // The following Unit attributes are suggested starting points
  // for the assignment. You may wish to modify them as necessary
  // for your particular implementation.

  Vector<Unit> in; // input Units for this Unit

  Vector<Unit> out; // output Units for this Unit

  double[] inWeights; // current (input) weights for this Unit

  // I.E. inWeights[1] is the weight from the in[1] unit.

  double activation; // this unit's activation level

  double error; // this unit's error

  double delta; // this unit's delta

  double[] weightChange; // weight changes for each weight
  
  double[] weightChangeMomentum;

  Net net; // network this unit belongs to

  /**
   * Constructor for Unit class. Takes an index number and Net to which Unit
   * belongs.
   * 
   */
  public Unit() {
    in = new Vector<Unit>();
    out = new Vector<Unit>();
  }

  public void addIncomingUnit(Unit inUnit) {
    this.in.add(inUnit);
  }

  public void setOutgoingUnit(Unit outUnit) {
    this.out.add(outUnit);
  }

  /**
   * initalize() Randomize all incoming weights between the network's minimum
   * and maximum weights, including bias weights.
   *
   * @param:
   *     double min: minimum value of random weight initialization
   *     double max: maximum value of random weight initialization
   * @pre: max is greater than min
   */
  public void initialize() {

    // instanciates weight-related arrays
    this.inWeights = new double[this.in.size()];
    this.weightChange = new double[this.in.size()];
    this.weightChangeMomentum = new double[this.in.size()];

    // random weights
    for (int i = 0; i < in.size(); i++){

      // random input weights
      this.inWeights[0] = getRandom(net.MIN_WEIGHT, net.MAX_WEIGHT);
      System.out.println(this.inWeights[0]);
    }
  }

  /**
   * computeActivation() Compute output activation of this unit. Apply sigmoid
   * function to weighted sum of inputs.
   *
   * @pre: in and inWeights are instanciated
   */
  public double computeActivation() {
    double sum = 0.0;   // finds the sum of all weights and bias
    for (int i = 0; i < this.inWeights.length; i++){
      sum += (this.inWeights[i] * this.in.get(i).activation);
    }

    // sigmoid squishification function
    this.activation =  1 / (1 + Math.exp(-1*sum));
    return this.activation;
  }

  /**
   * computeError(targets)
   * 
   * Computes error and delta for the output node. (not squared)
   * 
   * @param target
   *          is the current true output
   */
  public void computeError(int target) {
    this.error = target - this.activation;
  }

  /**
   * computeWeightChange()
   * 
   * Calculate the current weight change, including a momentum factor. May want
   * to use computeWeightChangeMomentum to calculate the momentum factor.
   */
  public void computeWeightChange() {

    // computes momentum from last term
    computeWeightChangeMomentum();

    // computes delta (sorry for this., I'm used to JS after the hackathon)
    this.delta = this.error * this.activation * (1 - this.activation);

    // computes all weight changes
    for (int i = 0; i < this.weightChange.length; i++){
      this.weightChange[i] = this.net.learningRate * this.in.get(i).activation 
                             * this.delta + this.weightChangeMomentum[i];
    }
  }

  /**
   * Update changes to weights for this pattern.
   */
  public void updateWeights() {

    // adds weightChange for each weight
    for (int i = 0; i < this.weightChange.length; i++){
     this.inWeights[i] += this.weightChange[i];
    }
  }

  /**
   * Calculate momentum factor for weight change. Store in this.weightChangeMomentum.
   */
  public void computeWeightChangeMomentum() {
    for (int i = 0; i < weightChange.length; i++){
      weightChangeMomentum[i] = weightChange[i] * Net.momentum;
    }
  }

  /**
   * Return a random number between min and max.
   * my boy randy
   */
  Random randy = new Random();

  public double getRandom(double min, double max) {
    return (randy.nextDouble() * (max - min)) + min;
  }
}
