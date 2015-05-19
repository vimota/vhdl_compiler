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
import ece351.vhdl.Splitter;
import ece351.vhdl.VParser;
import ece351.vhdl.ast.VProgram;


@RunWith(Parameterized.class)
public final class TestSplitter extends BaseTest351 {

	private final File f;

	public TestSplitter(final File f) {
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
		System.out.println("processing " + inputSpec);
		// parse from the file to construct first AST
		final VProgram vp1 = Splitter.split(c); 
		assertTrue(vp1.repOk());
		// pretty-print the first AST
		final String pp = vp1.toString();
		System.out.println("after process splitting: ");
		System.out.println(pp);
		
		// find the appropriate solution file for comparison
		String solnSpec = "";
		for (final Object[] obj : TestInputs351.processSplitVhdlFiles()) {
			if (obj[0] instanceof File) {
				final File soln = (File)obj[0];
				if (f.getName().equals(soln.getName())) {
					solnSpec = soln.getAbsolutePath();
					break;
				}
			}
		}
		
		assertTrue("no matching file found to compare the input file: " + inputSpec, solnSpec.length() > 0);
		System.out.println("checking process split output against: " + solnSpec);
		final CommandLine sc = new CommandLine(solnSpec);
		final VProgram vp2 = VParser.parse(sc.readInputSpec());
		assertTrue(vp2.repOk());
		System.out.println("solution: ");
		System.out.println(vp2.toString());
		// check that the two ASTs are equivalent (logically the same)
		assertTrue("ASTs differ for " + inputSpec, vp1.equivalent(vp2));
		// check examinable sanity
		ExaminableProperties.checkAllUnary(vp1);
		ExaminableProperties.checkAllUnary(vp2);
		ExaminableProperties.checkAllBinary(vp1, vp2);
		// success!
		System.out.println("accepted, as expected:  " + inputSpec + "\n\n\n");
	}

}
