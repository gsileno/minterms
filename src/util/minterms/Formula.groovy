package util.minterms

// Quine-McCluskey algorithm
// found on http://archive.today/z73yL#selection-195.0-195.25

class Formula {
    ArrayList<Term> termList = new ArrayList<Term>()
    private ArrayList<Term> originalTermList

    Formula(List<Term> termList) {
        this.termList = termList
    }

    Integer size() {
        return termList.size()
    }

    String toString() {
        String result = ""
        result += termList.size() + " terms, " + termList.get(0).getNumVars() + " variables\n"
        for (int i = 0; i < termList.size(); i++) {
            result += termList.get(i).toString() + "\n"
        }
        return result
    }

    void reduceToPrimeImplicants() {
        originalTermList = new ArrayList<Term>(termList)

        if (termList.size() == 0) return

        // create a double term table
        // where to count the number of dontcares and true in the formula
        int numVars = termList.get(0).getNumVars()
        ArrayList<Term>[][] table = new ArrayList[numVars + 1][numVars + 1]
        for (int dontKnows = 0; dontKnows <= numVars; dontKnows++) {
            for (int ones = 0; ones <= numVars; ones++) {
                table[dontKnows][ones] = new ArrayList<Term>()
            }
        }
        for (int i = 0; i < termList.size(); i++) {
            int dontCares = termList.get(i).countValues(Value.DONTCARE)
            int ones = termList.get(i).countValues(Value.TRUE)
            table[dontCares][ones].add(termList.get(i))
        }

        // generate new terms with combine() while updating prime implicant list
        for (int dontKnows = 0; dontKnows <= numVars - 1; dontKnows++) {
            for (int ones = 0; ones <= numVars - 1; ones++) {
                ArrayList<Term> left = table[dontKnows][ones]
                ArrayList<Term> right = table[dontKnows][ones + 1]
                ArrayList<Term> out = table[dontKnows + 1][ones]

                for (int leftIdx = 0; leftIdx < left.size(); leftIdx++) {
                    for (int rightIdx = 0; rightIdx < right.size(); rightIdx++) {
                        Term combined = left.get(leftIdx).combine(right.get(rightIdx))
                        if (combined != null) {

                            if (out.find() { it.equals(combined) } == null) {
                                out.add(combined)
                            }

                            // update prime implicant list
                            termList.remove(left.get(leftIdx))
                            // print "Remove "+left.get(leftIdx).toString()+" "
                            // println termList.toString()

                            termList.remove(right.get(rightIdx))
                            // print "Remove "+right.get(rightIdx).toString()+" "
                            // println termList.toString()

                            if (termList.find() { it.equals(combined) } == null) {
                                // if (!termList.contains(combined)) {
                                termList.add(combined)
                                // print "Add "+combined.toString()+" "
                                // println termList.toString()
                            }

                        }
                    }
                }
            }
        }
    }


    static private int extractEssentialImplicant(boolean[][] table) {
        for (int term = 0; term < table[0].length; term++) {
            int lastImplFound = -1
            for (int impl = 0; impl < table.length; impl++) {
                if (table[impl][term]) {
                    if (lastImplFound == -1) {
                        lastImplFound = impl
                    } else {
                        // This term has multiple implications
                        lastImplFound = -1
                        break
                    }
                }
            }
            if (lastImplFound != -1) {
                extractImplicant(table, lastImplFound)
                return lastImplFound
            }
        }
        return -1
    }

    static private void extractImplicant(boolean[][] table, int impl) {
        for (int term = 0; term < table[0].length; term++) {
            if (table[impl][term]) {
                for (int impl2 = 0; impl2 < table.length; impl2++) {
                    table[impl2][term] = false
                }
            }
        }
    }

    static private int extractLargestImplicant(boolean[][] table) {
        int maxNumTerms = 0
        int maxNumTermsImpl = -1
        for (int impl = 0; impl < table.length; impl++) {
            int numTerms = 0
            for (int term = 0; term < table[0].length; term++) {
                if (table[impl][term]) {
                    numTerms++
                }
            }
            if (numTerms > maxNumTerms) {
                maxNumTerms = numTerms
                maxNumTermsImpl = impl
            }
        }
        if (maxNumTermsImpl != -1) {
            extractImplicant(table, maxNumTermsImpl)
            return maxNumTermsImpl
        }
        return -1
    }

    void reducePrimeImplicantsToSubset() {
        // create implies table
        int numPrimeImplicants = termList.size()
        int numOriginalTerms = originalTermList.size()

        if (numOriginalTerms == 0) return
        if (numPrimeImplicants == 0) return

        boolean[][] table = new boolean[numPrimeImplicants][numOriginalTerms]
        for (int impl = 0; impl < numPrimeImplicants; impl++) {
            for (int term = 0; term < numOriginalTerms; term++) {
                table[impl][term] = termList.get(impl).implies(originalTermList.get(term))
            }
        }

        // extract implicants heuristically until done
        ArrayList<Term> newTermList = new ArrayList()
        boolean done = false
        int impl
        while (!done) {
            impl = extractEssentialImplicant(table)
            if (impl != -1) {
                newTermList.add(termList.get(impl))
            } else {
                impl = extractLargestImplicant(table)
                if (impl != -1) {
                    newTermList.add(termList.get(impl))
                } else {
                    done = true
                }
            }
        }
        termList = newTermList
        originalTermList = null
    }

    static Formula read(List<String> stringTerms) {
        ArrayList<Term> terms = new ArrayList<Term>()
        Term term
        for (stringTerm in stringTerms) {
            term = Term.read(stringTerm)
            if (term != null) // remove the terms null
                terms.add(term)
        }
        return new Formula(terms)
    }

    Formula applyQuineMcCluskey() {
        reduceToPrimeImplicants()
        reducePrimeImplicantsToSubset()
        return this
    }

    List<String> toStringList() {
        List<String> stringTerms = []
        for (int i = 0; i < termList.size(); i++) {
            stringTerms << termList.get(i).toString()
        }
        return stringTerms
    }
}
