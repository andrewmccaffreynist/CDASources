/*
 * RandomDistribution.java
 *
 * Created on June 14, 2006, 12:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package gov.nist.hl7.v3.misc;

import gov.nist.hl7.v3.misc.exception.InvalidProbabilitiesException;
import gov.nist.hl7.v3.misc.exception.InvalidRequestException;
import java.util.Random;

/**
 * A collection of static methods to do various tasks involving random
 * distributions.
 * @author mccaffrey
 */

public class RandomDistribution {
    
    static Random random = null;
    
    static private Random getRandom() {
        if(random == null)
            random = new Random();
        return random;
    }
    
    /**
     * Performs a bell-curve random distribution based on a given mean and standard
     * deviation.  Returns a double.
     * @param mean The mean needed for the calculation.
     * @param standardDeviation The standard deviation needed for the calculation.
     * @return A double which was generated using a random distribution on a bell curve.
     */
    public static double getRandomNormalDistributionDouble(double mean, double standardDeviation) {
        Random random = new Random();
        double init = random.nextGaussian();
        return mean + (init * standardDeviation);
    }
    
    public static double getRandomNormalDistributionDoubleSigDigits(double mean, double standardDeviation) {
        Double result = RandomDistribution.getRandomNormalDistributionDouble(mean,standardDeviation);
        String meanString = String.valueOf(mean);
        String standardDeviationString = String.valueOf(standardDeviation);
        int meanSigDigits = 0;
        int standardDeviationSigDigits = 0;
        if(meanString.indexOf('.') != -1)
            meanSigDigits = meanString.length() - meanString.indexOf('.') - 1;
        if(standardDeviationString.indexOf('.') != -1)
            standardDeviationSigDigits = standardDeviationString.length() - standardDeviationString.indexOf('.') - 1;
        int sigDigits = 0;
        if (meanSigDigits <= standardDeviationSigDigits)
            sigDigits = meanSigDigits;
        else
            sigDigits = standardDeviationSigDigits;
        
        String resultString = String.valueOf(result);
        if(resultString.indexOf('.') == -1)
            return result;
        if(resultString.indexOf('.') + sigDigits > resultString.length())
            return result;
        Double newResult = Double.parseDouble(resultString.substring(0,(resultString.indexOf('.') + sigDigits + 1)));
        return newResult;
        
    }
    
    /**
     * Performs a bell-curve random distribution based on a given mean and standard
     * deviation.  Returns an int.
     * @param mean The mean needed for the calculation.
     * @param standardDeviation The standard deviation needed for the calculation.
     * @return An int which was generated using a random distribution on a bell curve.
     */
    public static int getRandomNormalDistributionInt(double mean, double standardDeviation) {
        return new Double(RandomDistribution.getRandomNormalDistributionDouble(mean,standardDeviation)).intValue();
    }
    /**
     * Given two ints, this returns an int which is somewhere in between those two
     * values.
     * @param start The beginning of the interval.
     * @param end The end of the interval.
     * @return A random number along the interval described by the start and end inputs.
     */
    public static int getRandomUniformDistributionInt(int start, int end) {
        Random random = new Random();
        int randomNumber = random.nextInt(end - start + 1);
        return start + randomNumber;
    }
    
    public static int getProbabilityOrder(double[] probabilities) throws InvalidProbabilitiesException {
        
        if(!validProbabilities(probabilities)) throw new InvalidProbabilitiesException();
        double[] reworked = new double[probabilities.length];
        for(int i = 0; i < probabilities.length; i++) {
            if(i == 0)
                reworked[0] = probabilities[0];
            else
                reworked[i] = reworked[i - 1] + probabilities[i];
        }
        double selection = RandomDistribution.getRandom().nextDouble();
        for(int i = 0; i < probabilities.length; i++) {
            if(selection < reworked[i]) return i;
        }
        return -1;
        
    }
    
    /**
     *
     * @throws gov.nist.hl7.v3.misc.exception.InvalidProbabilitiesException Thrown if the probabilities don't add up to 1.0.
     * @throws gov.nist.hl7.v3.misc.exception.InvalidRequestException Thrown if something impossible has been requested (i.e. a call with allowRepeats
     * set to false while asking for more results than number of probabilities).
     * @return
     */
    public static int[] getProbabilityOrder(double[] probabilities, int size, boolean allowRepeats)
    throws InvalidProbabilitiesException, InvalidRequestException {
        if(!validProbabilities(probabilities)) throw new InvalidProbabilitiesException();
        if(size > probabilities.length && !allowRepeats) throw new InvalidRequestException();
        int[] results = getEmptyIntArray(size);
        
        for(int i = 0; i < size; i++) {
            int check = RandomDistribution.getProbabilityOrder(probabilities);
            if(allowRepeats)
                results[i] = check;
            else {
                if(!isResultAlreadyIncluded(check, results))
                    results[i] = check;
                else
                    i--;
            }
        }
        return results;
    }
    
    // In this algorithm, "empty" means full of -1.  This is so that we can still
    // do comparisons with a partially filled array, and therefore know the difference
    // between a position that has not been filled and a position that has been
    // filled with 0.
    private static int[] getEmptyIntArray(int size) {
        int[] empty = new int[size];
        for(int i = 0; i < empty.length; i++)
            empty[i] = -1;
        return empty;
    }
    private static boolean isResultAlreadyIncluded(int check, int[] results) {
        for(int i = 0; i < results.length; i++) {
            if(check == results[i]) return true;
        }
        return false;
    }
    private static boolean validProbabilities(double[] probabilities) {
        double total = 0;
        for(int i = 0; i < probabilities.length; i++) {
            total += probabilities[i];
        }
        if(1 == (float) total) return true;
        return false;
    }
    public static void main(String[] args) {
        try {
            /*
            double[] test = { .10,.20,.30,.40 } ;
            int[] result = { 0,0,0,0 };
            for(int i = 0; i < 1000000; i++) {
                result[getProbabilityOrder(test)] += 1;
            }
             
            for(int i = 0; i < result.length; i++) {
                System.out.println(i + " was returned " + result[i] + " times");
            }
             */
            for(int x = 0; x < 10; x++) {
                double[] test = { .10,.20,.30,.40 } ;
                int[] result = RandomDistribution.getProbabilityOrder(test,2,false);
                
                for(int i = 0; i < result.length; i++) {
                    System.out.println(i + " result = " + result[i]);
                }
                System.out.println("-----");
            }
            
            //  getRandomNormalDistributionDoubleSigDigits(5,5.55);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}