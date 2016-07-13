package util.minterms

// Quine-McCluskey algorithm
// found on http://archive.today/z73yL#selection-195.0-195.25

class Term {

    private ArrayList<Value> varVals = []

    Term(Value[] varVals) {
        this.varVals = varVals
    }

    int getNumVars() {
        return varVals.size()
    }

    String toString() {
        String result = ""
        for (int i = 0; i < varVals.size(); i++) {
            if (varVals[i] == Value.DONTCARE)
                result += "X"
            else if (varVals[i] == Value.TRUE)
                result += "1"
            else if (varVals[i] == Value.FALSE)
                result += "0"
        }
        return result
    }

    // a simple resolution rule of propositional logic
    // (A AND B) OR (A AND not B)
    // can be simplified to
    // A, with B DONTCARE
    Term combine(Term term) {
        int diffVarNum = -1; /* The position where they differ */
        for (int i = 0; i < varVals.size(); i++) {
            if (this.varVals[i] != term.varVals[i]) {
                if (diffVarNum == -1) {
                    diffVarNum = i
                } else {
                    // They're different in at least two places
                    return null
                }
            }
        }
        if (diffVarNum == -1) {
            // They're identical
            return null
        }
        Value[] resultVars = (Value[]) varVals.clone()
        resultVars[diffVarNum] = Value.DONTCARE
        return new Term(resultVars)
    }

    // count the numbers of variables with a certain value
    int countValues(Value value) {
        int result = 0
        for (int i = 0; i < varVals.size(); i++) {
            if (varVals[i] == value) {
                result++
            }
        }
        return result
    }

    // check if two lists are equals
    boolean equals(Term o) {
        return varVals.equals(o.varVals)
    }

    boolean implies(Term term) {
        for (int i = 0; i < varVals.size(); i++) {
            if (varVals[i] != Value.DONTCARE && varVals[i] != term.varVals[i]) {
                return false
            }
        }
        return true
    }

    static Term read(String stringTerm) {
        ArrayList<Value> t = new ArrayList<Value>()
        for (c in stringTerm) {
            if (c == '0') {
                t.add(Value.FALSE)
            } else if (c == '1') {
                t.add(Value.TRUE)
            } else if (c == 'C') { // case conflict in the rule
                return null
            } else {
                throw new RuntimeException()
            }
        }
        if (t.size() > 0) {
            Value[] resultValues = new Value[t.size()]
            for (int i = 0; i < t.size(); i++) {
                resultValues[i] = t.get(i)
            }
            return new Term(resultValues)
        } else {
            return null
        }
    }
}

