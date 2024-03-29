package shagejack.industrimania.foundation.chemistry;

import java.util.Map;
import java.util.TreeMap;

public class ChemicalFormula {

    private String formula;
    private Map<String, Integer> elements;

    public ChemicalFormula(String formula) {
        this.formula = formula;
        this.elements = getElements(formula);
    }

    public Map<String, Integer> getElements(String formula) {
        Map<String, Integer> result = parse(formula);
        return result;
    }


    private Map<String, Integer> parse(String formula){
        int i = 0;
        int N = formula.length();
        Map<String, Integer> count = new TreeMap<>();
        while (i < N && formula.charAt(i) != ')') {
            if (formula.charAt(i) == '(') {
                i++;
                for (Map.Entry<String, Integer> entry : parse(formula).entrySet()) {
                    count.put(entry.getKey(), count.getOrDefault(entry.getKey(), 0) + entry.getValue());
                }
            } else {
                int start = i++;
                while (i < N && Character.isLowerCase(formula.charAt(i))) i++;
                String name = formula.substring(start, i);
                start = i;
                while (i < N && Character.isDigit(formula.charAt(i))) i++;
                int num = start < i ? Integer.parseInt(formula.substring(start, i)) : 1;
                count.put(name, count.getOrDefault(name, 0) + num);
            }
        }
        int start = ++i;
        while (i < N && Character.isDigit(formula.charAt(i))) i++;
        if (start < i) {
            int num = Integer.parseInt(formula.substring(start, i));
            count.replaceAll((k, v) -> count.get(k) * num);
        }
        return count;
    }

}
