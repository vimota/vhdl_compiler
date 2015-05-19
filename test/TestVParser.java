package ece351.vhdl.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ece351.util.BaseTest351;
import ece351.util.CommandLine;
import ece351.util.ExaminableProperties;
import ece351.util.TestInputs351;
import ece351.vhdl.VParser;
import ece351.vhdl.ast.VProgram;


@RunWith(Parameterized.class)
public final class TestVParser extends BaseTest351 {

	private final File f;

	public TestVParser(final File f) {
		this.f = f;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> files() {
		return TestInputs351.vhdlFiles();
	}

	@Test
	public void parse() {
		final String inputSpec = f.getAbsolutePath();
		final CommandLine c = new CommandLine(inputSpec);
		final String input = c.readInputSpec();
		System.out.println("processing " + inputSpec);
		System.out.println("input: ");
		System.out.println(input);
		// parse from the file to construct first AST
		final VProgram vp1 = VParser.parse(input);
		assertTrue(vp1.repOk());
		// pretty-print the first AST
		final String pp = vp1.toString();
		System.out.println("pretty-print: ");
		System.out.println(pp);
		// construct a second AST from the pretty-print
		final VProgram vp2 = VParser.parse(pp);
		assertTrue(vp2.repOk());
		// check that the two ASTs are isomorphic (syntactically the same)
		assertTrue("ASTs differ for " + inputSpec, vp1.isomorphic(vp2));
		// check examinable sanity
		ExaminableProperties.checkAllUnary(vp1);
		ExaminableProperties.checkAllUnary(vp2);
		ExaminableProperties.checkAllBinary(vp1, vp2);
		// success!
		System.out.println("accepted, as expected:  " + inputSpec);
	}

}

