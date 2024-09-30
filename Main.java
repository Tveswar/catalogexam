package json;



import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class test {

    public static void main(String[] args) throws IOException, ParseException {
        // Parse JSON input
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("input.json"); // Provide your input file path here
        JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

        // Extract keys (n and k values)
        JSONObject keys = (JSONObject) jsonObject.get("keys");
        int n = Integer.parseInt(String.valueOf(keys.get("n"))); // Convert to string, then parse
        int k = Integer.parseInt(String.valueOf(keys.get("k"))); // Convert to string, then parse

        // Extract x and y values (roots)
        List<Double> xValues = new ArrayList<Double>();
        List<Double> yValues = new ArrayList<Double>();

        for (int i = 1; i <= n; i++) {
            if (jsonObject.containsKey(String.valueOf(i))) {
                JSONObject root = (JSONObject) jsonObject.get(String.valueOf(i));
                
                // Extract base and value
                int base = Integer.parseInt((String) root.get("base")); // base is a String, so no issue
                
                // Handle both String and Long for the value
                String value = root.get("value").toString();

                // Decode the y value based on the base
                BigInteger decodedValue = new BigInteger(value, base);
                xValues.add((double) i); // x-values are the keys 1, 2, 3, ...
                yValues.add(decodedValue.doubleValue()); // decoded y-value
            }
        }

        // Ensure we have enough points to solve the polynomial
        if (xValues.size() < k) {
            System.out.println("Not enough points to solve the polynomial.");
            return;
        }

        // Solve the polynomial using Lagrange interpolation
        double secretC = lagrangeInterpolation(xValues, yValues);
        System.out.println("The secret constant term (c) is: " + (secretC));
    }

    // Lagrange interpolation method
    public static double lagrangeInterpolation(List<Double> x, List<Double> y) {
        double secret = 0.0;

        int n = x.size();
        for (int i = 0; i < n; i++) {
            double term = y.get(i);
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    term *= (0 - x.get(j)) / (x.get(i) - x.get(j)); // Lagrange basis polynomial
                }
            }
            secret += term;
        }

        return secret;
    }
}