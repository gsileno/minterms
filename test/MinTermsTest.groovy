package util.minterms

class MinTermsTest extends GroovyTestCase {

    void test1() {
        String test = '''000
010
011
110
101
111'''

        def sr = new StringReader(test)
        Formula f = Formula.read(sr)

        assert (6 == f.size())
        f.reduceToPrimeImplicants()
        assert (3 == f.size())
        f.reducePrimeImplicantsToSubset()
        assert (3 == f.size())

    }

    void test2() {
        String test = '''0000
0001
0010
0011
0101
0111
1000
1010
1100
1101
1111'''

        def sr = new StringReader(test)
        Formula f = Formula.read(sr)

        assert (11 == f.size())
        f.reduceToPrimeImplicants()
        assert (6 == f.size())
        f.reducePrimeImplicantsToSubset()
        assert (4 == f.size())

    }

    void test3() {
        String test = '''110110
111110
111111
110111
010101
010100
010110
010111
011111
011101
011100
011110'''

        def sr = new StringReader(test)
        Formula f = Formula.read(sr)

        assert (12 == f.size())
        f.reduceToPrimeImplicants()
        assert (2 == f.size())
        f.reducePrimeImplicantsToSubset()
        assert (2 == f.size())
    }

}