package ece351.vhdl.test;

import static org.junit.Assert.assertTrue;
import ece351.util.ExaminableProperties;
import ece351.vhdl.VParser;
import ece351.vhdl.ast.VProgram;

public class TestVParser2 extends AbstractVHDLTest {

	@Override
	protected void test(final String name, final VProgram vp1) {
		// pretty-print the input AST
		final String pp = vp1.toString();
		//System.out.println("pretty-print the input AS:  " + pp);
		// parse the pretty-print
		final VProgram vp2 = VParser.parse(pp);
		//final String pp1  = fp2.toString();
		//System.out.println("pretty-print the input AS:  " + pp1);
		// check that they are the same
		assertTrue("unexpectedly not isomorphic: " + name, vp1.isomorphic(vp2));
		// check object contract
		ExaminableProperties.checkAllUnary(vp1);
		ExaminableProperties.checkAllUnary(vp2);
		ExaminableProperties.checkAllBinary(vp1, vp2);
		// success!
		System.out.println("accepted, as expected:  " + name);
	}

}
